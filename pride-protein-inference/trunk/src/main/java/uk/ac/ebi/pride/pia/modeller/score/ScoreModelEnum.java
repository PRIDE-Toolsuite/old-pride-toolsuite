package uk.ac.ebi.pride.pia.modeller.score;

import java.util.ArrayList;
import java.util.List;
import uk.ac.ebi.pride.pia.modeller.psm.ReportPSMSet;
import uk.ac.ebi.pride.pia.tools.PIAConstants;

/**
 * This enumeration registers all {@link ScoreModel}s.
 * 
 * @author julian
 *
 */
public enum ScoreModelEnum {
	/**
	 * The score type is not further known, so actually nothing is known for it.
	 */
	UNKNOWN_SCORE {
		@Override
		public String getName() {
			return null;
		}

		@Override
		public String getShortName() {
			return null;
		}

		@Override
		public String getCvAccession() {
			return null;
		}

		@Override
		public String getCvName() {
			return null;
		}
		
		@Override
		public Boolean higherScoreBetter() {
			return null;
		}
		
		@Override
		public List<String> getValidDescriptors() {
			return new ArrayList<String>();
		}
	},
	/**
	 * This Score implements the Average FDR-Score.
	 */
	AVERAGE_FDR_SCORE {
		@Override
		public String getName() {
			return "Average FDR Score";
		}
		
		@Override
		public String getShortName() {
			return "average_fdr_score";
		}
		
		@Override
		public String getCvAccession() {
			return "(cvAccession not set for average_fdr_score)";
		}
		
		@Override
		public String getCvName() {
			return "(cvName not set for average_fdr_score)";
		}

		@Override
		public Boolean higherScoreBetter() {
			return false;
		}
		
		@Override
		public List<String> getValidDescriptors() {
			List<String> descs = new ArrayList<String>();
			
			descs.add(getName());
			descs.add(getName().toLowerCase());
			descs.add("Average FDR");
			descs.add("average fdr");
			descs.add(getShortName());
			
			return descs;
		}
		
		@Override
		public Boolean isPSMSetScore() {
			return true;
		}
	},
	/**
	 * This Score implements the Combined FDR Score.<br/>
	 * It does not have an own class, but is actually only for the naming. The
	 * FDR Score of a {@link ReportPSMSet} is a COMBINED_FDR_SCORE
	 */
	PSM_LEVEL_COMBINED_FDR_SCORE {
		@Override
		public String getName() {
			return "PSM Combined FDR Score";
		}
		
		@Override
		public String getShortName() {
			return "psm_combined_fdr_score";
		}
		
		@Override
		public String getCvAccession() {
			return PIAConstants.CV_PSM_LEVEL_COMBINED_FDRSCORE_ACCESSION;
		}
		
		@Override
		public String getCvName() {
			return PIAConstants.CV_PSM_LEVEL_COMBINED_FDRSCORE_NAME;
		}
		
		@Override
		public Boolean higherScoreBetter() {
			return false;
		}
		
		@Override
		public List<String> getValidDescriptors() {
			List<String> descs = new ArrayList<String>();
			
			descs.add(getName());
			descs.add(getName().toLowerCase());
			descs.add("Combined FDR");
			descs.add("combined fdr");
			descs.add(getShortName());
			
			return descs;
		}
		
		@Override
		public Boolean isPSMSetScore() {
			return true;
		}
	},
	/**
	 * This Score implements the FDR-Score.
	 */
	PSM_LEVEL_FDR_SCORE {
		@Override
		public String getName() {
			return "PSM FDRScore";
		}
		
		@Override
		public String getShortName() {
			return "psm_fdr_score";
		}
		
		@Override
		public String getCvAccession() {
			return PIAConstants.CV_PSM_LEVEL_FDRSCORE_ACCESSION;
		}
		
		@Override
		public String getCvName() {
			return PIAConstants.CV_PSM_LEVEL_FDRSCORE_NAME;
		}

		@Override
		public Boolean higherScoreBetter() {
			return false;
		}
		
		@Override
		public List<String> getValidDescriptors() {
			List<String> descs = new ArrayList<String>();
			
			descs.add(getName());
			descs.add(getName().toLowerCase());
			descs.add(getShortName());
			descs.add(getCvAccession());
			descs.add(getCvName());
			
			return descs;
		}
	},
	/**
	 *  This Score implements the Mascot expectation value
	 */
	MASCOT_EXPECT {
		@Override
		public String getName() {
			return "Mascot Expect";
		}
		
		@Override
		public String getShortName() {
			return "mascot_expect";
		}
		
		@Override
		public String getCvAccession() {
			return PIAConstants.CV_MASCOT_EXPECTATION_VALUE_ACCESSION;
		}
		
		@Override
		public String getCvName() {
			return PIAConstants.CV_MASCOT_EXPECTATION_VALUE_NAME;
		}
		
		@Override
		public Boolean higherScoreBetter() {
			return false;
		}
		
		@Override
		public List<String> getValidDescriptors() {
			List<String> descs = new ArrayList<String>();
			
			descs.add(getName());
			descs.add(getName().toLowerCase());
			descs.add(getShortName());
			descs.add(getCvAccession());
			descs.add(getCvName());
			descs.add("Mascot expect");
			descs.add("Mascot_EValue");
			
			return descs;
		}
	},
	/**
	 *  This Score implements the Mascot Ion Score.
	 */
	MASCOT_SCORE {
		@Override
		public String getName() {
			return "Mascot Ion Score";
		}
		
		@Override
		public String getShortName() {
			return "mascot_score";
		}
		
		@Override
		public String getCvAccession() {
			return PIAConstants.CV_MASCOT_SCORE_ACCESSION;
		}
		
		@Override
		public String getCvName() {
			return PIAConstants.CV_MASCOT_SCORE_NAME;
		}

		@Override
		public Boolean higherScoreBetter() {
			return true;
		}
		
		@Override
		public Boolean isSearchengineMainScore() {
			return true;
		}
		
		@Override
		public List<String> getValidDescriptors() {
			List<String> descs = new ArrayList<String>();
			
			descs.add(getName());
			descs.add(getName().toLowerCase());
			descs.add("IonScore");
			descs.add("Mascot Score");
			descs.add("mascot score");
			descs.add("Mascot score");
			descs.add("Mascot_openmsmainscore");
			descs.add(getShortName());
			descs.add(getCvAccession());
			descs.add(getCvName());
			
			return descs;
		}
	},
	/**
	 *  This Score implements the Sequest Probability.
	 */
	SEQUEST_PROBABILITY {
		@Override
		public String getName() {
			return "Sequest Probability";
		}
		
		@Override
		public String getShortName() {
			return "sequest_probability";
		}
		
		@Override
		public String getCvAccession() {
			return PIAConstants.CV_SEQUEST_PROBABILITY_ACCESSION;
		}
		
		@Override
		public String getCvName() {
			return PIAConstants.CV_SEQUEST_PROBABILITY_NAME;
		}

		@Override
		public Boolean higherScoreBetter() {
			// no transformation needed
			return false;
		}
		
		@Override
		public List<String> getValidDescriptors() {
			List<String> descs = new ArrayList<String>();
			
			descs.add(getName());
			descs.add(getName().toLowerCase());
			descs.add("Probability");
			descs.add("probability");
			descs.add(getShortName());
			descs.add(getCvAccession());
			descs.add(getCvName());
			
			return descs;
		}
	},
	/**
	 *  This Score implements the Sequest SpScore.
	 */
	SEQUEST_SPSCORE {
		@Override
		public String getName() {
			return "SpScore";
		}
		
		@Override
		public String getShortName() {
			return "sequest_spscore";
		}
		
		@Override
		public String getCvAccession() {
			return PIAConstants.CV_SEQUEST_SP_ACCESSION;
		}
		
		@Override
		public String getCvName() {
			return PIAConstants.CV_SEQUEST_SP_NAME;
		}

		@Override
		public Boolean higherScoreBetter() {
			return true;
		}
		
		@Override
		public List<String> getValidDescriptors() {
			List<String> descs = new ArrayList<String>();
			
			descs.add(getName());
			descs.add(getName().toLowerCase());
			descs.add("Sequest SpScore");
			descs.add("sequest spscore");
			descs.add(getShortName());
			descs.add(getCvAccession());
			descs.add(getCvName());
			
			return descs;
		}
	},
	/**
	 *  This Score implements the Sequest XCorr.
	 */
	SEQUEST_XCORR {
		@Override
		public String getName() {
			return "XCorr";
		}
		
		@Override
		public String getShortName() {
			return "sequest_xcorr";
		}
		
		@Override
		public String getCvAccession() {
			return PIAConstants.CV_SEQUEST_XCORR_ACCESSION;
		}
		
		@Override
		public String getCvName() {
			return PIAConstants.CV_SEQUEST_XCORR_NAME;
		}
		
		@Override
		public Boolean higherScoreBetter() {
			return true;
		}
		
		@Override
		public Boolean isSearchengineMainScore() {
			return true;
		}
		
		@Override
		public List<String> getValidDescriptors() {
			List<String> descs = new ArrayList<String>();
			
			descs.add(getName());
			descs.add(getName().toLowerCase());
			descs.add("Sequest XCorr");
			descs.add("sequest xcorr");
			descs.add(getShortName());
			descs.add(getCvAccession());
			descs.add(getCvName());
			
			return descs;
		}
	},
	/**
	 * The X!Tandem expectation value.
	 */
	XTANDEM_EXPECT {
		@Override
		public String getName() {
			return "X!Tandem Expect";
		}
		
		@Override
		public String getShortName() {
			return "xtandem_expect";
		}
		
		@Override
		public String getCvAccession() {
			return PIAConstants.CV_XTANDEM_EXPECT_ACCESSION;
		}
		
		@Override
		public String getCvName() {
			return PIAConstants.CV_XTANDEM_EXPECT_NAME;
		}
		
		@Override
		public Boolean higherScoreBetter() {
			return false;
		}
		
		@Override
		public Boolean isSearchengineMainScore() {
			return true;
		}
		
		@Override
		public List<String> getValidDescriptors() {
			List<String> descs = new ArrayList<String>();
			
			descs.add(getName());
			descs.add(getName().toLowerCase());
			descs.add("X! Tandem expect");
			descs.add("Tandem Expect");
			descs.add("tandem expect");
			descs.add("XTandem_E-Value");
			descs.add(getShortName());
			descs.add(getCvAccession());
			descs.add(getCvName());
			
			return descs;
		}
	},
	/**
	 * The X!Tandem expectation value.
	 */
	XTANDEM_HYPERSCORE {
		@Override
		public String getName() {
			return "X!Tandem Hyperscore";
		}
		
		@Override
		public String getShortName() {
			return "xtandem_hyperscore";
		}
		
		@Override
		public String getCvAccession() {
			return PIAConstants.CV_XTANDEM_HYPERSCORE_ACCESSION;
		}
		
		@Override
		public String getCvName() {
			return PIAConstants.CV_XTANDEM_HYPERSCORE_NAME;
		}

		@Override
		public Boolean higherScoreBetter() {
			return true;
		}
		
		@Override
		public List<String> getValidDescriptors() {
			List<String> descs = new ArrayList<String>();
			
			descs.add(getName());
			descs.add(getName().toLowerCase());
			descs.add("X! Tandem hyperscore");
			descs.add("Tandem Hyperscore");
			descs.add("tandem hyperscore");
			descs.add("Hyperscore");
			descs.add("hyperscore");
			descs.add("XTandem_openmsmainscore");
			descs.add(getShortName());
			descs.add(getCvAccession());
			descs.add(getCvName());
			
			return descs;
		}
	},
	/**
	 * The FASTA Sequence Count score.
	 */
	FASTA_SEQUENCE_COUNT {
		@Override
		public String getName() {
			return "FASTA Sequence Count";
		}
		
		@Override
		public String getShortName() {
			return "fasta_sequence_count";
		}
		
		@Override
		public String getCvAccession() {
			return "(cvAccession not set for fasta_sequence_count)";
		}
		
		@Override
		public String getCvName() {
			return "(cvName not set for fasta_sequence_count)";
		}

		@Override
		public Boolean higherScoreBetter() {
			return true;
		}
		
		@Override
		public List<String> getValidDescriptors() {
			List<String> descs = new ArrayList<String>();
			
			descs.add(getName());
			descs.add(getName().toLowerCase());
			descs.add(getShortName());
			descs.add(getCvAccession());
			descs.add(getCvName());
			
			return descs;
		}
	},
	/**
	 * The FASTA Accession Count score.
	 */
	FASTA_ACCESSION_COUNT {
		@Override
		public String getName() {
			return "FASTA Accession Count";
		}
		
		@Override
		public String getShortName() {
			return "fasta_accession_count";
		}
		
		@Override
		public String getCvAccession() {
			return "(cvAccession not set for fasta_accession_count)";
		}
		
		@Override
		public String getCvName() {
			return "(cvName not set for fasta_accession_count)";
		}

		@Override
		public Boolean higherScoreBetter() {
			return true;
		}
		
		@Override
		public List<String> getValidDescriptors() {
			List<String> descs = new ArrayList<String>();
			
			descs.add(getName());
			descs.add(getName().toLowerCase());
			descs.add(getShortName());
			descs.add(getCvAccession());
			descs.add(getCvName());
			
			return descs;
		}
	},
	
	/**
	 * The score of the protein.
	 * 
	 * TODO: this must be handled differently... there are scores with higherScoreBetter = true and false!
	 * 
	 */
	PROTEIN_SCORE {
		@Override
		public String getName() {
			return "Protein score";
		}
		
		@Override
		public String getShortName() {
			return "protein_score";
		}
		
		@Override
		public String getCvAccession() {
			return PIAConstants.CV_PIA_PROTEIN_SCORE_ACCESSION;
		}
		
		@Override
		public String getCvName() {
			return PIAConstants.CV_PIA_PROTEIN_SCORE_NAME;
		}
		
		@Override
		public Boolean higherScoreBetter() {
			System.err.println("higherScoreBetter() is not implemented for '"
					+ getName() +
					"' as it depends on the scores and scoring method used in the protein inference");
			return null;
		}
		
		@Override
		public List<String> getValidDescriptors() {
			List<String> descs = new ArrayList<String>();
			
			descs.add(getName());
			descs.add(getName().toLowerCase());
			descs.add(getShortName());
			descs.add(getCvAccession());
			descs.add(getCvName());
			
			return descs;
		}
	},
	;
		
	
	/**
	 * Returns the human readable name of the score model.
	 * @return
	 */
	public abstract String getName();
	
	
	/**
	 * Returns the human readable name of the score model given by the
	 * description (i.e. name, shortName or cvAccession). If the model is
	 * {@link ScoreModelEnum#UNKNOWN_SCORE}, return the description.
	 * 
	 * @return
	 */
	public final static String getName(String scoreModelDescriptor) {
		ScoreModelEnum model = getModelByDescription(scoreModelDescriptor);
		
		if (model.equals(UNKNOWN_SCORE)) {
			return scoreModelDescriptor;
		} else {
			return model.getName();
		}
	}
	
	
	/**
	 * Returns the machine readable name of the score model
	 * @return
	 */
	public abstract String getShortName();
	
	
	/**
	 * this should be overridden by any PSM set scores (like Combined FDR Score)
	 * @return
	 */
	public Boolean isPSMSetScore() {
		return false;
	}
	
	
	/**
	 * Returns the CV accession of the score model
	 * @return
	 */
	public abstract String getCvAccession();
	
	
	/**
	 * Returns the CV name of the score model
	 * @return
	 */
	public abstract String getCvName();
	
	
	/**
	 * Returns, whether a higher score is better, or null, if not known (for
	 * unknown type).
	 * 
	 * @return
	 */
	public abstract Boolean higherScoreBetter();
	
	
	/**
	 * Indicates, whether the score is the main score of the search engine.
	 * These scores are preferably used for FDR calculation.
	 */
	public Boolean isSearchengineMainScore() {
		return false;
	}
	
	
	/**
	 * Gets all the valid descriptors for the ScoreModel (i.e. name, shortName
	 * or cvAccession, some additional names)
	 * @return
	 */
	public abstract List<String> getValidDescriptors();
	
	
	/**
	 * Returns true, if the given description is a valid descriptor (i.e. name, 
	 * shortName or cvAccession) of the score model.
	 * @return
	 */
	public final boolean isValidDescriptor(String desc) {
		return getValidDescriptors().contains(desc);
	}
	
	
	/**
	 * Returns the model for the given description (i.e. name, shortName or
	 * cvAccession)or UNKNOWN_SCORE, if there is none for the description.
	 * 
	 * @param desc
	 * @return
	 */
	public final static ScoreModelEnum getModelByDescription(String desc) {
		for (ScoreModelEnum model : values()) {
			if (!model.equals(UNKNOWN_SCORE) &&
					model.isValidDescriptor(desc)) {
				return model;
			}
		}
		
		return UNKNOWN_SCORE;
	}
}