package uk.ac.ebi.pride.pia.intermediate.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import uk.ac.ebi.pride.data.controller.DataAccessController;
import uk.ac.ebi.pride.data.core.Peptide;
import uk.ac.ebi.pride.data.core.Protein;
import uk.ac.ebi.pride.pia.intermediate.DataImportController;
import uk.ac.ebi.pride.pia.intermediate.IntermediatePeptide;
import uk.ac.ebi.pride.pia.intermediate.IntermediatePeptideSpectrumMatch;
import uk.ac.ebi.pride.pia.intermediate.IntermediateProtein;
import uk.ac.ebi.pride.pia.intermediate.IntermediateStructureCreator;
import uk.ac.ebi.pride.pia.modeller.filter.AbstractFilter;
import uk.ac.ebi.pride.pia.modeller.filter.FilterUtilities;


public class PrideImportController implements DataImportController {
	
	/** the logger for this class */
	private static final Logger logger= Logger.getLogger(PrideImportController.class);
	
	/** the data access controller */
	private DataAccessController controller;
	
	/** the applied filters during the import */
	private List<AbstractFilter> filters;
	
	
	public PrideImportController(DataAccessController dataAccessController) {
		this(dataAccessController, null);
	}
	
	
	public PrideImportController(DataAccessController dataAccessController, List<AbstractFilter> filters) {
		this.controller = dataAccessController;
		this.filters = (filters == null) ? new ArrayList<AbstractFilter>() : filters;
	}
	
	
	@Override
	public void addAllSpectrumIdentificationsToStructCreator(IntermediateStructureCreator structCreator) {
		
		int nrProteins = controller.getNumberOfProteins();
		logger.info(nrProteins + " proteins to go");
		
		int processedProtIDs = 0;
		for (Comparable proteinId : controller.getProteinIds()) {
			
			// create the protein, add it later (when there is a filtered PSM)
			IntermediateProtein protein = new PrideIntermediateProtein(controller, proteinId);
			
			for (Comparable peptideId : controller.getPeptideIds(proteinId)) {
				// add the peptides
				IntermediatePeptideSpectrumMatch psm =
						new PrideIntermediatePeptideSpectrumMatch(controller, proteinId, peptideId);
				
				if ((filters.size() == 0) || (FilterUtilities.satisfiesFilterList(psm, filters))) {
					// add the protein (only, if any PSM passes filters)
					if (!structCreator.proteinsContains(protein.getID())) {
						structCreator.addProtein(protein);
					}
					
					String pepSequence = psm.getSpectrumIdentification().getSequence();
					Comparable pepID = IntermediatePeptide.computeID(pepSequence);
					
					IntermediatePeptide peptide;
					if (structCreator.peptidesContains(pepID)) {
						peptide = structCreator.getPeptide(pepID);
					} else {
						peptide = new IntermediatePeptide(pepSequence);
						structCreator.addPeptide(peptide);
					}
					
					// add the PSM to the peptide (if it does not already exist)
					peptide.addPeptideSpectrumMatch(psm);
					
					// connect the peptide and protein
					structCreator.addPeptideToProteinConnection(pepID, protein.getID());
				}
			}
			processedProtIDs++;
			if (((processedProtIDs % 1000) == 0) && (processedProtIDs > 1)) {
				logger.info("processed proteins " + processedProtIDs + " / " + nrProteins);
			}
		}
	}
}