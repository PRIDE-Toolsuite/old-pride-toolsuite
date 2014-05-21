package uk.ac.ebi.pride.pia.modeller.protein.inference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import uk.ac.ebi.pride.data.core.Peptide;
import uk.ac.ebi.pride.pia.intermediate.Accession;
import uk.ac.ebi.pride.pia.intermediate.Group;
import uk.ac.ebi.pride.pia.modeller.peptide.ReportPeptide;
import uk.ac.ebi.pride.pia.modeller.protein.ReportProtein;
import uk.ac.ebi.pride.pia.modeller.psm.ReportPSMSet;
import uk.ac.ebi.pride.pia.modeller.report.filter.AbstractFilter;
import uk.ac.ebi.pride.pia.modeller.report.filter.FilterFactory;


public class OccamsRazorWorkerThread extends Thread {
	
	/** the ID of this worker thread */
	private int ID;
	
	/** the caller of this thread */
	private OccamsRazorInference parent;
	
	/** the applied inference filters */
	private List<AbstractFilter> filters;
	
	/** maps of the ReportPSMSets (build by the PSM Viewer) */
	private Map<String, ReportPSMSet> reportPSMSetMap;
	
	/** whether modifications are considered while inferring the peptides */
	private boolean considerModifications;
	
	/** settings for PSMSet creation */
	private Map<String, Boolean> psmSetSettings;
	
	
	/** logger for this class */
	private static final Logger logger = Logger.getLogger(OccamsRazorWorkerThread.class);
	
	
	public OccamsRazorWorkerThread(int ID,
                                   OccamsRazorInference parent,
                                   List<AbstractFilter> filters,
                                   boolean considerModifications,
                                   Map<String, Boolean> psmSetSettings) {
		this.ID = ID;
		this.parent = parent;
		this.filters = filters;
		this.reportPSMSetMap = reportPSMSetMap;
		this.considerModifications = considerModifications;
		this.psmSetSettings = psmSetSettings;
		
		this.setName("OccamsRazorWorkerThread-" + this.ID);
	}
	
	
	@Override
	public void run() {
		int treeCount = 0;
		Map.Entry<Long, Map<Long, Group>> treeEntry;
		
		while (null != (treeEntry = parent.getNextTree())) {
			processTree(treeEntry.getValue());
			treeCount++;
		}
		
		logger.debug("worker " + ID + " finished after " + treeCount);
	}
	
	
	
	private void processTree(Map<Long, Group> groupMap) {
		// get the filtered report peptides mapping from the groups' IDs
		Map<Long, List<Peptide>> reportPeptidesMap =
				parent.createFilteredReportPeptides(groupMap, reportPSMSetMap,
						considerModifications, psmSetSettings);
		
		// the map of actually reported proteins
		Map<Long, ReportProtein> proteins =
				new HashMap<Long, ReportProtein>(reportPeptidesMap.size());
		
		// maps from the protein/group IDs to the peptide keys 
		Map<Long, Set<String>> peptideKeysMap =
				new HashMap<Long, Set<String>>();
		
		// maps from the groups ID to the IDs, which have the same peptides
		Map<Long, Set<Long>> sameSetMap =
				new HashMap<Long, Set<Long>>(reportPeptidesMap.size());
		
		// create for each group, which has at least one peptide and accession, a ReportProtein
		for (Map.Entry<Long, Group> groupIt : groupMap.entrySet()) {
			if ((groupIt.getValue().getProteinIds().size() == 0) ||
					!parent.groupHasReportPeptides(
							groupIt.getValue(), reportPeptidesMap)) {
				// this group has no peptides, skip it
				continue;
			}
			
			ReportProtein protein = new ReportProtein(groupIt.getKey());
			
			// add the accessions
			for (Accession acc : groupIt.getValue().getProteinIds().values()) {
				protein.addAccession(acc);
			}
			
			// add the peptides
			Set<Long> pepGroupIDs = new HashSet<Long>();
			Set<String> peptideKeys = new HashSet<String>();
			pepGroupIDs.add(groupIt.getKey());
			pepGroupIDs.addAll(
					groupIt.getValue().getAllPeptideChildren().keySet());
			for (Long pepGroupID : pepGroupIDs) {
				if (reportPeptidesMap.containsKey(pepGroupID)) {
					for (ReportPeptide peptide
							: reportPeptidesMap.get(pepGroupID)) {
						if (!peptideKeys.add(peptide.getStringID())) {
							logger.warn(
									"Peptide already in list of peptides '" +
									peptide.getStringID() + "'");
						} else {
							protein.addPeptide(peptide);
						}
					}
				}
			}
			
			// get the proteins with same peptides and subgroups
			Set<Long> sameSet = new HashSet<Long>();
			for (Map.Entry<Long, Set<String>> peptideKeyIt
					: peptideKeysMap.entrySet()) {
				if (peptideKeyIt.getValue().equals(peptideKeys)) {
					sameSet.add(peptideKeyIt.getKey());
					sameSetMap.get(peptideKeyIt.getKey()).add(groupIt.getKey());
				}
			}
			sameSetMap.put(groupIt.getKey(), sameSet);
			
			peptideKeysMap.put(groupIt.getKey(), peptideKeys);
			
			proteins.put(protein.getID(), protein);
		}
		
		if (proteins.size() < 1) {
			// no proteins could be created (e.g. due to filters?) 
			return;
		}
		
		// merge proteins with same peptides
		for (Map.Entry<Long, Set<Long>> sameSetIt : sameSetMap.entrySet()) {
			Long protID = sameSetIt.getKey();
			ReportProtein protein = proteins.get(protID);
			if (protein != null) {
				// the protein is not yet deleted due to samesets
				for (Long sameID : sameSetIt.getValue()) {
					if (sameID != protID) {
						ReportProtein sameProtein = proteins.get(sameID);
						if (sameProtein != null) {
							// add the accessions of sameProtein to protein
							for (Accession acc : sameProtein.getAccessions()) {
								protein.addAccession(acc);
							}
							
							// and remove the same-protein
							proteins.remove(sameID);
							peptideKeysMap.remove(sameID);
							
							// this makes sure, the protein does not get removed, when it is iterated over sameProtein
							sameSetMap.get(sameID).remove(protID);
						}
					}
				}
			}
		}
		// the sameSetMap is no longer needed
		sameSetMap = null;
		
		// check the proteins whether they satisfy the filters
		Set<Long> removeProteins = new HashSet<Long>(proteins.size());
		for (ReportProtein protein : proteins.values()) {
			// score the proteins before filtering
			Double protScore =
					parent.getScoring().calculateProteinScore(protein); 
			protein.setScore(protScore);
			
			if (!FilterFactory.satisfiesFilterList(protein, 0L, filters)) {
				removeProteins.add(protein.getID());
			}
		}
		for (Long rID : removeProteins) {
			proteins.remove(rID);
			peptideKeysMap.remove(rID);
		}
		
		// this will be the list of reported proteins
		List<ReportProtein> reportProteins = new ArrayList<ReportProtein>();
		
		// the still unreported proteins
		HashMap<Long, ReportProtein> unreportedProteins =
				new HashMap<Long, ReportProtein>(proteins);
		
		// check proteins for sub-proteins and intersections. this cannot be
		// done before, because all proteins have to be built beforehand
		Map<Long, Set<Long>> subProteinMap =
				new HashMap<Long, Set<Long>>(reportPeptidesMap.size());
		Map<Long, Set<Long>> intersectingProteinMap =
				new HashMap<Long, Set<Long>>(reportPeptidesMap.size());
		Set<Long> isSubProtein = new HashSet<Long>();
		Set<String> reportedPeptides = new HashSet<String>();
		for (Long proteinID : proteins.keySet()) {
			Set<String> peptideKeys = peptideKeysMap.get(proteinID);
			
			Set<Long> subProteins = new HashSet<Long>();
			subProteinMap.put(proteinID, subProteins);
			
			Set<Long> intersectingProteins = new HashSet<Long>();
			intersectingProteinMap.put(proteinID, intersectingProteins);
			
			for (Long subProtID : proteins.keySet()) {
				if (proteinID == subProtID) {
					continue;
				}
				
				Set<String> intersection = new HashSet<String>(
						peptideKeysMap.get(subProtID));
				intersection.retainAll(peptideKeys);
				
				if (intersection.size() > 0) {
					if (intersection.size() ==
							peptideKeysMap.get(subProtID).size()) {
						// the complete subProtID is in proteinID
						subProteins.add(subProtID);
					} else if (intersection.size() == peptideKeys.size()) {
						// the complete proteinID is in subProtID
						isSubProtein.add(proteinID);
					} else if (intersection.size() != peptideKeys.size()) {
						// subProtID intersects proteinID somehow
						intersectingProteins.add(subProtID);
					}
				}
			}
			
			if ((intersectingProteins.size() == 0) &&
					!isSubProtein.contains(proteinID)) {
				// this protein is no subProtein and has no intersections (but
				// maybe subProteins) -> report this protein
				ReportProtein protein = proteins.get(proteinID);
				
				reportProteins.add(protein);
				reportedPeptides.addAll(peptideKeysMap.get(proteinID));
				unreportedProteins.remove(proteinID);
				
				// add the subproteins
				for (Long subID : subProteins) {
					protein.addToSubsets(proteins.get(subID));
					unreportedProteins.remove(subID);
				}
			}
		}
		
		// report all the proteins ordered by which explains the most new peptides
		while (unreportedProteins.size() > 0) {
			Set<Long> mostPepsIDs = null;
			Set<String> mostCanReport = null;
			int nrMostPeps = -1;
			
			for (ReportProtein protein : unreportedProteins.values()) {
				if (isSubProtein.contains(protein.getID())) {
					// subproteins are reported indirectly
					continue;
				}
				Set<String> canReport = peptideKeysMap.get(protein.getID());
				canReport.removeAll(reportedPeptides);
				
				if (canReport.size() > nrMostPeps) {
					mostPepsIDs = new HashSet<Long>();
					mostPepsIDs.add(protein.getID());
					nrMostPeps = canReport.size();
					mostCanReport = canReport;
				} else if ((canReport.size() == nrMostPeps) &&
						canReport.equals(mostCanReport)) {
					mostPepsIDs.add(protein.getID());
				}
			}
			
			for (Long protID : mostPepsIDs) {
				ReportProtein protein = proteins.get(protID);
				if (nrMostPeps > 0) {
					// TODO: for now, the proteins which "explain" no more peptides are not reported (this happens sometimes)
					reportProteins.add(protein);
					reportedPeptides.addAll(peptideKeysMap.get(protID));
					
				} else {
					logger.debug("protein has no more peptides: " + protein.getAccessions().get(0).getAccession());
				}
				unreportedProteins.remove(protID);
				
				// add the subproteins
				for (Long subID : subProteinMap.get(protID)) {
					protein.addToSubsets(proteins.get(subID));
					unreportedProteins.remove(subID);
				}
			}
		}
		
		if (reportProteins.size() > 0) {
			parent.addToReports(reportProteins);
		}
	}
}
