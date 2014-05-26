package uk.ac.ebi.pride.pia.modeller.psm;


import uk.ac.ebi.pride.pia.intermediate.*;
import uk.ac.ebi.pride.pia.modeller.IdentificationKeySettings;
import uk.ac.ebi.pride.pia.modeller.score.FDRData;
import uk.ac.ebi.pride.pia.modeller.score.ScoreModel;
import uk.ac.ebi.pride.pia.modeller.score.ScoreModelEnum;
import uk.ac.ebi.pride.pia.tools.PIAConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 
 * @author julian
 * 
 */
public class ReportPSM  implements PSMReportItem {
	
	/** unique ID of the item */
	private Long ID;
	
	/** the associated spectrum */
	private Comparable spectrumID;

	/** marks, if this PSM is a decoy */
	private boolean isDecoy;
	
	/** marks, if this item is FDR good */
	private boolean isFDRGood;
	
	/** all the accessions this PSM occurs in */
	private Map<String, String> accessions;
	
	/** the rank of the PSM */
	private Long rank;
	
	/** the local fdr of the PSM */
	private Double fdrValue;
	
	/** the q-value, only available when FDR is calculated */
	private Double qValue;
	
	/** the FDR Score */
	private ScoreModel fdrScore;
	
	/** map from the scoreShorts to the identification ranks */
	private Map<String, Integer> identificationRanks;
	
	/** The maximal set of  {@link uk.ac.ebi.pride.pia.modeller.IdentificationKeySettings} which are available on this PSM */
	private Map<String, Boolean> maximalSpectraIdentificationSettings;
	
	/** The maximal set of not redundant {@link uk.ac.ebi.pride.pia.modeller.IdentificationKeySettings} which are available on this PSM */
	private Map<String, Boolean> maximalNotRedundantSpectraIdentificationSettings;
	
	
	/**
	 * Basic constructor.
	 * 
	 * @param ID
	 * @param spectrum
	 */
	public ReportPSM(Long ID, PeptideSpectrumMatch spectrum) {
		this.ID = ID;
		this.spectrum = spectrum;
		isDecoy = ((spectrum.getIsDecoy() != null) && spectrum.getIsDecoy()) ?
				true : false;
		isFDRGood = false;
		accessions = new HashMap<String, Accession>();
		qValue = null;
		fdrScore = null;
		
		fdrValue = Double.POSITIVE_INFINITY;
		rank = 0L;
		identificationRanks = new HashMap<String, Integer>(3);
		
		// set the map to available values
		maximalSpectraIdentificationSettings = new HashMap<String, Boolean>(5);
		maximalSpectraIdentificationSettings.put(
				IdentificationKeySettings.MASSTOCHARGE.name(), true);
		if (spectrum.getRetentionTime() != null) {
			maximalSpectraIdentificationSettings.put(
					IdentificationKeySettings.RETENTION_TIME.name(), true);
		}
		if (spectrum.getSourceID() != null) {
			maximalSpectraIdentificationSettings.put(
					IdentificationKeySettings.SOURCE_ID.name(), true);
		}
		if (spectrum.getSpectrumTitle() != null) {
			maximalSpectraIdentificationSettings.put(
					IdentificationKeySettings.SPECTRUM_TITLE.name(), true);
		}
		maximalSpectraIdentificationSettings.put(
				IdentificationKeySettings.CHARGE.name(), true);
		
		maximalNotRedundantSpectraIdentificationSettings =
				IdentificationKeySettings.noRedundantSettings(maximalSpectraIdentificationSettings);
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if ( !(obj instanceof ReportPSM) ) {
			return false;
		}
		
		ReportPSM objPSM = (ReportPSM)obj;
	    if (objPSM.ID.equals(this.ID) &&
	    		objPSM.spectrum.equals(this.spectrum) &&
	    		(objPSM.isDecoy == this.isDecoy) &&
	    		(objPSM.isFDRGood == this.isFDRGood) && 
	    		(objPSM.accessions.equals(this.accessions))) {
	    	return true;
	    } else {
	    	return false;
	    }
	}
	
	
	@Override
	public int hashCode() {
		int hash = 0;
		
		hash += (ID != null) ? ID.hashCode() : 0;
		hash += (spectrum != null) ? spectrum.hashCode() : 0;
		hash += isDecoy ? 1 : 0;
		hash += isFDRGood ? 1 : 0;
		hash += (accessions != null) ? accessions.hashCode() : 0;
		
		return hash;
	}
	
	
	/**
	 * Getter for the ID
	 * @return
	 */
	public Long getId() {
		return ID;
	}
	
	
	@Override
	public String getIdentificationKey(Map<String, Boolean> psmSetSettings) {
		return spectrum.getIdentificationKey(psmSetSettings);
	}
	
	
	@Override
	public Map<String, Boolean> getNotRedundantIdentificationKeySettings() {
		return maximalNotRedundantSpectraIdentificationSettings;	
	}


	@Override
	public String getPeptideStringID(boolean considerModifications) {
		return spectrum.getPeptideStringID(considerModifications);
	}
	
	
	/**
	 * Getter for the spectrum.
	 * @return
	 */
	public PeptideSpectrumMatch getSpectrum() {
		return spectrum;
	}
	
	
	/**
	 * Getter for isFDRGood
	 * @return
	 */
	public boolean getIsFDRGood() {
		return isFDRGood;
	}
	
	
	/**
	 * Setter for isFDRGood
	 * @param isGood
	 */
	@Override
	public void setIsFDRGood(boolean isGood) {
		isFDRGood = isGood;
	}
	
	
	@Override
	public boolean getIsDecoy() {
		return isDecoy;
	}
	
	
	/**
	 * Setter for isDecoy
	 * @param isDecoy
	 */
	public void setIsDecoy(boolean isDecoy) {
		this.isDecoy = isDecoy;
	}
	
	
	/**
	 * Add the given accession into the accessions map/trie.
	 * @param accession
	 */
	public void addAccession(Accession accession) {
		accessions.put(accession.getAccession(), accession);
	}
	
	
	@Override
	public List<Accession> getAccessions() {
		// TODO: only put the accessions into the PSM which are in the file...
		List<Accession> accList = new ArrayList<Accession>(accessions.size());
		
		for (Map.Entry<String, Accession> accIt : accessions.entrySet()) {
			// only add the accession, if it was found in the file
			if (accIt.getValue().foundInFile(spectrum.getFile().getID())) {
				accList.add(accIt.getValue());
			}
		}
		
		return accList;
	}
	
	
	@Override
	public String getSequence() {
		return spectrum.getSequence();
	}
	
	
	@Override
	public int getCharge() {
		return spectrum.getCharge();
	}
	
	
	@Override
	public String getSourceID() {
		return spectrum.getSourceID();
	}
	
	
	@Override
	public String getSpectrumTitle() {
		return spectrum.getSpectrumTitle();
	}
	
	
	/**
	 * Getter for the file
	 * @return
	 */
	public PIAInputFile getFile() {
		return spectrum.getFile();
	}
	
	
	/**
	 * Getter for the name of the {@link PIAInputFile}
	 * @return
	 */
	public String getInputFileName() {
		String name = spectrum.getFile().getName();
		
		if (name == null) {
			name = spectrum.getFile().getFileName();
		}
		
		return name;
	}
	
	
	/**
	 * Getter for the fileName of the {@link PIAInputFile}
	 * @return
	 */
	public String getFileName() {
		return spectrum.getFile().getFileName();
	}
	
	/**
	 * Getter for the file id
	 * @return
	 */
	public Long getFileID() {
		return spectrum.getFile().getID();
	}
	
	
	@Override
	public Map<Integer, Modification> getModifications() {
		return spectrum.getModifications();
	}
	
	
	@Override
	public String getModificationsString() {
		return spectrum.getModificationString();
	}
	
	
	@Override
	public int getMissedCleavages() {
		return spectrum.getMissedCleavages();
	}
	
	
	@Override
	public double getMassToCharge() {
		return spectrum.getMassToCharge();
	}
	
	
	@Override
	public double getDeltaMass() {
		return spectrum.getDeltaMass();
	}
	
	
	@Override
	public Double getRetentionTime() {
		return spectrum.getRetentionTime();
	}
	
	
	@Override
	public double getDeltaPPM() {
		double mass = spectrum.getCharge() *
				(spectrum.getMassToCharge() - PIAConstants.H_MASS.doubleValue());
		
		return (spectrum.getDeltaMass()) / mass * 1000000;
	}
	
	
	@Override
	public Double getScore(String scoreName) {
		if (scoreName.equals(ScoreModelEnum.PSM_LEVEL_FDR_SCORE.getShortName())) {
			// special case: FDR-Score
			if (fdrScore != null) {
				return fdrScore.getValue();
			}
		} else {
			// for all other cases: get score from spectrum
			ScoreModel score = spectrum.getScore(scoreName);
			if (score != null) {
				return score.getValue();
			}
		}
		// no score found
		return Double.NaN;
	}
	
	
	@Override
	public String getScoresString() {
		StringBuilder scoresSB = new StringBuilder();
		
		for (ScoreModel model : getScores()) {
			if (scoresSB.length() > 0) {
				scoresSB.append(",");
			}
			scoresSB.append(model.getName());
			scoresSB.append(":");
			scoresSB.append(model.getValue());
		}
		
		return scoresSB.toString();
	}
	
	
	/**
	 * Returns a list of the score models of this PSM.
	 * 
	 * @return
	 */
	public List<ScoreModel> getScores() {
		List<ScoreModel> scores = new ArrayList<ScoreModel>();
		
		for (ScoreModel score : spectrum.getScores()) {
			scores.add(score);
		}
		
		if (fdrScore != null) {
			scores.add(fdrScore);
		}
		
		return scores;
	}
	
	
	@Override
	public double getFDR() {
		if (fdrValue == null) {
			return Double.NaN;
		} else {
			return fdrValue;
		}
	}
	
	
	@Override
	public void setFDR(double fdr) {
		this.fdrValue = fdr;
	}
	
	
	@Override
	public Long getRank() {
		return rank;
	}
	
	
	@Override
	public void setRank(Long rank) {
		this.rank = rank;
	}
	
	
	/**
	 * Sets the rank for the score given by scoreShort.
	 * 
	 * @param scoreShort
	 * @param rank
	 */
	public void setIdentificationRank(String scoreShort, Integer rank) {
		identificationRanks.put(scoreShort, rank);
	}
	
	
	/**
	 * Returns the identification rank for the given score type.<br/>
	 * The identification ranks must be calculated, or null will be returned
	 * always.
	 * 
	 * @param scoreShort
	 * @return
	 */
	public Integer getIdentificationRank(String scoreShort) {
		return identificationRanks.get(scoreShort);
	}
	
	
	/**
	 * Returns all the identificationRanks
	 * @return
	 */
	public Map<String, Integer> getIdentificationRanks() {
		return identificationRanks;
	}
	
	
	@Override
	public void dumpFDRCalculation() {
		isFDRGood = false;
		qValue = null;
		fdrScore = null;
		fdrValue = Double.POSITIVE_INFINITY;
	}
	
	
	@Override
	public void updateDecoyStatus(FDRData.DecoyStrategy strategy, Pattern p) {
		switch (strategy) {
		case ACCESSIONPATTERN:
			this.isDecoy = isDecoyWithPattern(p);
			break;
			
		case SEARCHENGINE:
			if (spectrum.getIsDecoy() != null) {
				this.isDecoy = spectrum.getIsDecoy();
			} else {
				// TODO: how to handle this...
				this.isDecoy = false;
			}
			break;
		}
	}
	
	
	/**
	 * Returns true, if the PSM is a decoy with the given pattern.
	 * @param p
	 */
	private boolean isDecoyWithPattern(Pattern p) {
		Matcher m;
		boolean decoy = true;
		
		for (Map.Entry<String, Accession> accIt : accessions.entrySet()) {
			m = p.matcher(accIt.getValue().getAccession());
			decoy &= m.matches();
		}
		
		return decoy;
	}
	
	
	@Override
	public double getQValue() {
		if (qValue == null) {
			return Double.NaN;
		} else {
			return qValue;
		}
	}
	
	
	@Override
	public void setQValue(double value) {
		this.qValue = value;
	}
	
	
	@Override
	public ScoreModel getFDRScore() {
		return fdrScore;
	}
	
	
	@Override
	public void setFDRScore(Double score) {
		if (fdrScore != null) {
			fdrScore.setValue(score);
		} else {
			fdrScore = new ScoreModel(score,
					ScoreModelEnum.PSM_LEVEL_FDR_SCORE);
		}
	}
	
	
	@Override
	public ScoreModel getCompareScore(String scoreShortname) {
		if (ScoreModelEnum.PSM_LEVEL_FDR_SCORE.getShortName().equals(scoreShortname)) {
			return fdrScore;
		}
		else {
			return spectrum.getScore(scoreShortname);
		}
	}


	@Override
	public Map<String, Boolean> getAvailableIdentificationKeySettings() {
		return maximalSpectraIdentificationSettings;
	}
	
	
	@Override
	public String getNiceSpectrumName() {
		return spectrum.getNiceSpectrumName();
	}
	
	
	@Override
	public IntermediatePeptide getPeptide() {
		return getSpectrum().getPeptide();
	}
}
