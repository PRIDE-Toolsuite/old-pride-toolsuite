package uk.ac.ebi.pride.pia.modeller.protein.scoring.settings;


import java.util.ArrayList;
import java.util.List;


/**
 * Enum for the setting of used spectra scores of peptide.
 * 
 * @author julian
 */
public enum PSMForScoring {
	/**
	 * only use the best spectrum score of the peptide for score calculation
	 */
	ONLY_BEST {
		@Override
		public String getName() {
			return "only the best PSM per peptide";
		}
		
		@Override
		public String getShortName() {
			return "best";
		}
		
		/*
		@Override
		public List<ScoreModel> getProteinsScores(ReportProtein protein,
				String scoreShortName) {
			List<ScoreModel> scores = new ArrayList<ScoreModel>(protein.getNrPeptides());
			
			// get the best score of each peptide
			for (ReportPeptide peptide : protein.getPeptides()) {
				
				ScoreModel pepScore = peptide.getBestScoreModel(scoreShortName);
				
				if (pepScore != null) {
					scores.add(pepScore);
				}
			}
			return scores;
		}
		*/
	},
	/**
	 * use all PSMs of the peptide for score calculation
	 */
	ALL_PSMS {
		@Override
		public String getName() {
			return "all PSMs per peptide";
		}
		
		@Override
		public String getShortName() {
			return "all";
		}
		
		/*
		@Override
		public List<ScoreModel> getProteinsScores(ReportProtein protein,
				String scoreShortName) {
			// TODO: implement
			List<ScoreModel> scores = new ArrayList<ScoreModel>(protein.getNrPeptides());
			
			// go through all peptides...
			for (ReportPeptide peptide : protein.getPeptides()) {
				// ...go through all PSM sets...
				for (PSMReportItem repPSM : peptide.getPSMs()) {
					if (repPSM instanceof ReportPSMSet) {
						// the repPSM has to be a ReportPSMSet, we have only the overview here
						if (ScoreModelEnum.PSM_LEVEL_COMBINED_FDR_SCORE.isValidDescriptor(scoreShortName)) {
							// ... the COMBINED_FDR_SCORE is in the PSM set 
							ScoreModel psmScore = repPSM.getCompareScore(scoreShortName);
							if (psmScore != null) {
								scores.add(psmScore);
							}
						} else {
							// ... go through all PSMs and take all their (scoring) scores
							for (ReportPSM psm : ((ReportPSMSet) repPSM).getPSMs()) {
								if (!peptide.getNonScoringPSMIDs().contains(psm.getId())) {
									ScoreModel psmScore = psm.getCompareScore(scoreShortName);
									if (psmScore != null) {
										scores.add(psmScore);
									}
								}
							}
							
						}
					}
				}
			}
			
			return scores;
		}
		*/
	},
	;
	
	/**
	 * Getter for the method's name.
	 * @return
	 */
	public abstract String getName();
	
	
	/**
	 * Getter for the method's shortName
	 * @return
	 */
	public abstract String getShortName();
	
	
	/**
	 * Gets all the scores, this method needs for its calculation.
	 * 
	 * @return
	 */
	//public abstract List<ScoreModel> getProteinsScores(ReportProtein protein, String scoreShortName);
	
	
	/**
	 * Gets all the scores, the method given by the methodName needs for its
	 * calculation.
	 * 
	 * @return
	 */
	/*
	public static List<ScoreModel> getProteinsScores(String methodName,
			ReportProtein protein, String scoreShortName) {
		for (PSMForScoring method : values()) {
			if (method.getShortName().equals(methodName)) {
				return method.getProteinsScores(protein, scoreShortName);
			}
		}
		
		return new ArrayList<ScoreModel>(0);
	}
	*/
}