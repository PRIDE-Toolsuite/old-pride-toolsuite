package uk.ac.ebi.pride.pia.modeller.protein.inference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import de.mpc.pia.intermediate.Group;
import de.mpc.pia.modeller.peptide.ReportPeptide;
import de.mpc.pia.modeller.protein.ReportProtein;
import de.mpc.pia.modeller.psm.ReportPSMSet;
import de.mpc.pia.modeller.report.filter.FilterFactory;
import de.mpc.pia.modeller.report.filter.psm.NrPSMsPerPSMSetFilter;
import de.mpc.pia.modeller.report.filter.psm.PSMScoreFilter;
import de.mpc.pia.tools.LabelValueContainer;


/**
 * This inference filter reports all the PIA {@link IntermediateGroup}s as one protein.<br/>
 * This is similar to distinguish proteins simply by their peptides and report
 * every possible set and subset.
 * 
 * @author julian
 *
 */
public class ReportAllInference extends AbstractProteinInference {
	
	/** the human readable name of this filter */
	protected static final String name = "Report All";
	
	/** the machine readable name of the filter */
	protected static final String shortName = "inference_report_all";
	
	/** the progress of the inference */
	private Double progress;
	
	
	/** the logger for this class */
	private static final Logger logger= Logger.getLogger(ReportAllInference.class);
	
	
	@Override
	public List<LabelValueContainer<String>> getFilterTypes() {
		List<LabelValueContainer<String>> filters = new ArrayList<LabelValueContainer<String>>();
		
		filters.add(new LabelValueContainer<String>(null, "--- PSM ---"));
		for (Map.Entry<String, String>  scoreIt
				: getAvailableScoreShorts().entrySet()) {
			String[] filterNames = PSMScoreFilter.getShortAndFilteringName(
					scoreIt.getKey(), scoreIt.getValue());
			
			if (filterNames != null) {
				filters.add(new LabelValueContainer<String>(filterNames[0], filterNames[1]));
			}
		}
		filters.add(new LabelValueContainer<String>(NrPSMsPerPSMSetFilter.shortName(), NrPSMsPerPSMSetFilter.filteringName()));
		
		return filters;
	}
	
	@Override
	public List<ReportProtein> calculateInference(Map<Long, IntermediateGroup> groupMap,
			Map<String, ReportPSMSet> reportPSMSetMap,
			boolean considerModifications,
			Map<String, Boolean> psmSetSettings) {
		progress = 0.0;
		logger.info("calculateInference started...");
		logger.info("scoring: " + getScoring().getName() + " with " + 
				getScoring().getScoreSetting().getValue() + ", " +
				getScoring().getPSMForScoringSetting().getValue());
		
		// maps from the groups' IDs to the reportPeptides
		Map<Long, List<ReportPeptide>> reportPeptidesMap =
			createFilteredReportPeptides(groupMap, reportPSMSetMap,
					considerModifications, psmSetSettings);
		
		// groups with the IDs in this set should be reported
		Set<Long> reportGroupsIDs = new HashSet<Long>();
		
		// all the PSMs of the groups, including the PSMs in groups' children
		Map<Long, Set<String>> groupsAllPeptides = new HashMap<Long, Set<String>>(groupMap.size());
		
		// maps from the tree ID to the groupIDs
		Map<Long, Set<Long>> treeMap = new HashMap<Long, Set<Long>>();
		
		// maps from the groups' IDs to the groups' IDs with equal PSMs after filtering
		Map<Long, Set<Long>> sameSets = null;
		
		// put every group with accessions into the map
		// TODO: this COULD be parallelized for speedup, if it is too slow...
		Double progressStep = 80.0 / groupMap.size();
		for (Map.Entry<Long, IntermediateGroup> gIt : groupMap.entrySet()) {
			
			if ((gIt.getValue().getAccessions().size() > 0) &&
					groupHasReportPeptides(gIt.getValue(), reportPeptidesMap)) {
				// report this group
				reportGroupsIDs.add(gIt.getKey());
				
				// get the peptides of this group / protein
				Set<String> allPeptidesSet = new HashSet<String>();
				groupsAllPeptides.put(gIt.getKey(), allPeptidesSet);
				
				if (reportPeptidesMap.containsKey(gIt.getKey())) {
					for (ReportPeptide pepIt : reportPeptidesMap.get(gIt.getKey())) {
						allPeptidesSet.add(pepIt.getStringID());
					}
				}
				
				for (IntermediateGroup pepGroupIt : gIt.getValue().getAllPeptideChildren().values()) {
					if (reportPeptidesMap.containsKey(pepGroupIt.getID())) {
						for (ReportPeptide pepIt : reportPeptidesMap.get(pepGroupIt.getID())) {
							allPeptidesSet.add(pepIt.getStringID());
						}
					}
				}
				
				// fill the treeMap
				Set<Long> treeSet = treeMap.get(gIt.getValue().getTreeID());
				if (treeSet == null) {
					treeSet = new HashSet<Long>();
					treeMap.put(gIt.getValue().getTreeID(), treeSet);
				}
				treeSet.add(gIt.getKey());
				
			}
			
			progress += progressStep;
		}
		
		// check for sameSets (if there were active filters)
		if (filters.size() > 0) {
			sameSets = new HashMap<Long, Set<Long>>(groupsAllPeptides.size());
			Set<Long> newReportGroups = new HashSet<Long>(reportGroupsIDs.size());
			
			progressStep = 10.0 / groupsAllPeptides.size();
			for (Map.Entry<Long, Set<String>> gIt : groupsAllPeptides.entrySet()) {
				Long treeID = groupMap.get(gIt.getKey()).getTreeID();
				
				// every group gets a sameSet
				Set<Long> sameSet = sameSets.get(gIt.getKey()); 
				if (sameSet == null) {
					sameSet = new HashSet<Long>();
					sameSets.put(gIt.getKey(), sameSet);
				}
				
				// check against the groups in the tree
				for (Long checkID : treeMap.get(treeID)) {
					if (gIt.getKey() == checkID) {
						// don't check against self
						continue;
					}
					
					if (gIt.getValue().equals(groupsAllPeptides.get(checkID))) {
						// ReportPeptides are the same in checkSet and grIt
						sameSet.add(checkID);
						
						// if checkID's group had a sameSet before, merge the sameSets
						Set<Long> checkSameSet = sameSets.get(checkID);
						if (checkSameSet != null) {
							sameSet.addAll(checkSameSet);
						}
						sameSets.put(checkID, sameSet);
					}
				}
				
				
				// check, if any of the sameSet is already in the newReportGroups 
				boolean anySameInReportGroups = false;
				
				for (Long sameID : sameSet) {
					if (newReportGroups.contains(sameID)) {
						anySameInReportGroups = true;
						break;
					}
				}
				
				if (!anySameInReportGroups) {
					// no sameGroup in reportGroups yet, put this one in
					newReportGroups.add(gIt.getKey());
				}
				
				progress += progressStep;
			}
			
			reportGroupsIDs = newReportGroups;
		}
		
		progress = 90.0;
		
		// now create the proteins from the groups, which are still in reportGroupsIDs
		// the list, that will be returned
		List<ReportProtein> reportProteinList = new ArrayList<ReportProtein>(reportGroupsIDs.size());
		
		// caching the proteins, especially the subSet proteins
		Map<Long, ReportProtein> proteins = new HashMap<Long, ReportProtein>(reportGroupsIDs.size());
		
		progressStep = 10.0 / reportGroupsIDs.size();
		for (Long gID : reportGroupsIDs) {
			ReportProtein protein = createProtein(gID, proteins,
					reportPeptidesMap, groupMap, sameSets, null);
			
			if (FilterFactory.satisfiesFilterList(protein, 0L, filters)) {
				// if all the filters are satisfied, add the protein to the reportProteinList
				reportProteinList.add(protein);
			}
			
			progress += progressStep;
		}
		
		logger.info("calculateInference done.");
		progress = 100.0;
		return reportProteinList;
	}
	
	
	@Override
	public String getName() {
		return name;
	}
	
	
	@Override
	public String getShortName() {
		return shortName;
	}

	@Override
	public Long getProgressValue() {
		return progress.longValue();
	}
}