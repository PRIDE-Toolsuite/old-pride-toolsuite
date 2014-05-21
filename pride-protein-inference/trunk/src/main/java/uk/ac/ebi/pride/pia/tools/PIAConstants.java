package uk.ac.ebi.pride.pia.tools;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * This class holds some constants, which are universally used within PIA.
 * 
 * @author julian
 *
 */
public class PIAConstants {
	
	/** the current version of PIA */
	public static String version;
	
	
	/** the mass of one hydrogen ion (H+) */
	public static BigDecimal H_MASS = new BigDecimal("1.007276");
	
	
	/** the mass tolerance for finding a modification by mass in Unimod */
	public static Double unimod_mass_tolerance = 0.001;
	
	
	// prefixes for the IDs from mzIdentML
	public static String software_prefix = "software_";
	public static String identification_protocol_prefix = "identProtocol_";
	public static String spectrum_identification_prefix = "specIdent_";
	public static String databases_prefix = "searchDB_";
	public static String spectra_data_prefix = "spectraData_";
	public static String enzyme_prefix = "enzyme_";
	
	
	// prefixes for mzTab export
	public static String MZTAB_MISSED_CLEAVAGES_COLUMN_NAME = "missed_cleavages";
	public static String MZTAB_NR_PEPTIDES_COLUMN_NAME = "number_of_peptides";
	public static String MZTAB_NR_PSMS_COLUMN_NAME = "number_of_psms";
	public static String MZTAB_NR_SPECTRA_COLUMN_NAME = "number_of_spectra";
	
	
	static { 
		Logger logger = Logger.getLogger(PIAConstants.class);
		
		InputStream inputStream =
				PIAConstants.class.getResourceAsStream("/de/mpc/pia/general.properties");
		Properties properties = new Properties();
		
		if (inputStream != null) {
			try {
				properties.load(inputStream);
			} catch (IOException e) {
				logger.error("Error reading the propertiuk.ac.ebi.pridee 'de.mpc.pia.general.properties'! " + e);
			}
		} else {
			logger.error("Could not open the properties file'! ");
		}
		
		version = properties.getProperty("pia_version");
	}
	
	
	/** the location of the PIA source */
	public static final String PIA_REPOSITORY_LOCATION = "https://github.com/julianu/pia";
	
	
	// some information for the used CVs
	/** the label for the PSI-MS CV */
	public static final String CV_PSI_MS_LABEL = "MS";
	
	
	/** PIAs accession in the PSI-CV*/
	public static final String CV_PIA_ACCESSION = "MS:1002387";
	/** PIAs name in the PSI-CV*/
	public static final String CV_PIA_NAME = "PIA";
	
	/** accession for the "PIA XML file" in PSI-MS */
	public static final String CV_PIA_XML_FILE_ACCESSION = "MS:1002388";
	/** name for the "PIA XML file" in PSI-MS */
	public static final String CV_PIA_XML_FILE_NAME = "PIA XML format";
	
	/** accession for the "PIA workflow parameter" in PSI-MS */
	public static final String CV_PIA_WORKFLOW_PARAMETER_ACCESSION = "MS:1002389";
	/** name for the "PIA workflow parameter" in PSI-MS */
	public static final String CV_PIA_WORKFLOW_PARAMETER_NAME = "PIA workflow parameter";
	
	/** accession for the "PIA:FDRScore calculated" in PSI-MS */
	public static final String CV_PIA_FDRSCORE_CALCULATED_ACCESSION = "MS:1002390";
	/** name for the "PIA:FDRScore calculated" in PSI-MS */
	public static final String CV_PIA_FDRSCORE_CALCULATED_NAME = "PIA:FDRScore calculated";
	
	/** accession for the "PIA:Combined FDRScore calculated" in PSI-MS */
	public static final String CV_PIA_COMBINED_FDRSCORE_CALCULATED_ACCESSION = "MS:1002391";
	/** name for the "PIA:Combined FDRScore calculated" in PSI-MS */
	public static final String CV_PIA_COMBINED_FDRSCORE_CALCULATED_NAME = "PIA:Combined FDRScore calculated";
	
	/** accession for the "PIA:PSM sets created" in PSI-MS */
	public static final String CV_PIA_PSM_SETS_CREATED_ACCESSION = "MS:1002392";
	/** name for the "PIA:PSM sets created" in PSI-MS */
	public static final String CV_PIA_PSM_SETS_CREATED_NAME = "PIA:PSM sets created";
	
	/** accession for the "PIA:used top identifications for FDR" in PSI-MS */
	public static final String CV_PIA_USED_TOP_IDENTIFICATIONS_ACCESSION = "MS:1002393";
	/** name for the "PIA:used top identifications for FDR" in PSI-MS */
	public static final String CV_PIA_USED_TOP_IDENTIFICATIONS_DNAME = "PIA:used top identifications for FDR";
	
	/** accession for the "PIA protein score" in PSI-MS */
	public static final String CV_PIA_PROTEIN_SCORE_ACCESSION = "MS:1002394";
	/** name for the "PIA protein score" in PSI-MS */
	public static final String CV_PIA_PROTEIN_SCORE_NAME = "PIA:protein score";
	
	/** accession for the "PIA:protein inference" in PSI-MS */
	public static final String CV_PIA_PROTEIN_INFERENCE_ACCESSION = "MS:1002395";
	/** name for the "PIA:protein inference" in PSI-MS */
	public static final String CV_PIA_PROTEIN_INFERENCE_NAME = "PIA:protein inference";
	
	/** accession for the "PIA:protein inference filter" in PSI-MS */
	public static final String CV_PIA_PROTEIN_INFERENCE_FILTER_ACCESSION = "MS:1002396";
	/** name for the "PIA:protein inference filter" in PSI-MS */
	public static final String CV_PIA_PROTEIN_INFERENCE_FILTER_NAME = "PIA:protein inference filter";
	
	/** accession for the "PIA:protein inference scoring" in PSI-MS */
	public static final String CV_PIA_PROTEIN_INFERENCE_SCORING_ACCESSION = "MS:1002397";
	/** name for the "PIA:protein inference scoring" in PSI-MS */
	public static final String CV_PIA_PROTEIN_INFERENCE_SCORING_NAME = "PIA:protein inference scoring";
	
	/** accession for the "PIA:protein inference used score" in PSI-MS */
	public static final String CV_PIA_PROTEIN_INFERENCE_USED_SCORE_ACCESSION = "MS:1002398";
	/** name for the "PIA:protein inference used score" in PSI-MS */
	public static final String CV_PIA_PROTEIN_INFERENCE_USED_SCORE_NAME = "PIA:protein inference used score";
	
	/** accession for the "PIA:protein inference used PSMs" in PSI-MS */
	public static final String CV_PIA_PROTEIN_INFERENCE_USED_PSMS_ACCESSION = "MS:1002399";
	/** name for the "PIA:protein inference used PSMs" in PSI-MS */
	public static final String CV_PIA_PROTEIN_INFERENCE_USED_PSMS_NAME = "PIA:protein inference used PSMs";
	
	/** accession for the "PIA:protein inference used PSMs" in PSI-MS */
	public static final String CV_PIA_FILTER_ACCESSION = "MS:1002400";
	/** name for the "PIA:protein inference used PSMs" in PSI-MS */
	public static final String CV_PIA_FILTER_NAME = "PIA:filter";
	
	
	/** accession for "SEQUEST:probability" in PSI-MS */
	public static final String CV_SEQUEST_PROBABILITY_ACCESSION = "MS:1001154";
	/** name for "SEQUEST:probability" in PSI-MS */
	public static final String CV_SEQUEST_PROBABILITY_NAME = "SEQUEST:probability";
	
	/** accession for "SEQUEST:xcorr" in PSI-MS */
	public static final String CV_SEQUEST_XCORR_ACCESSION = "MS:1001155";
	/** name for "SEQUEST:xcorr" in PSI-MS */
	public static final String CV_SEQUEST_XCORR_NAME = "SEQUEST:xcorr";
	
	/** accession for "SEQUEST:sp" in PSI-MS */
	public static final String CV_SEQUEST_SP_ACCESSION = "MS:1001157";
	/** name for "SEQUEST:sp" in PSI-MS */
	public static final String CV_SEQUEST_SP_NAME = "SEQUEST:sp";

	/** accession for "Mascot:score" in PSI-MS */
	public static final String CV_MASCOT_SCORE_ACCESSION = "MS:1001171";
	/** name for "Mascot:score" in PSI-MS */
	public static final String CV_MASCOT_SCORE_NAME = "Mascot:score";
	
	/** accession for "Mascot:expectation value" in PSI-MS */
	public static final String CV_MASCOT_EXPECTATION_VALUE_ACCESSION = "MS:1001172";
	/** name for "Mascot:expectation value" in PSI-MS */
	public static final String CV_MASCOT_EXPECTATION_VALUE_NAME = "Mascot:expectation value";
	
	/** accession for "Trypsin" in PSI-MS */
	public static final String CV_TRYPSIN_ACCESSION = "MS:1001251";
	/** name for "Trypsin" in PSI-MS */
	public static final String CV_TRYPSIN_NAME = "Trypsin";
	
	/** accession for "X!Tandem:expect" in PSI-MS */
	public static final String CV_XTANDEM_EXPECT_ACCESSION = "MS:1001330";
	/** name for "X!Tandem:expect" in PSI-MS */
	public static final String CV_XTANDEM_EXPECT_NAME = "X!Tandem:expect";
	
	/** accession for "X!Tandem:hyperscore" in PSI-MS */
	public static final String CV_XTANDEM_HYPERSCORE_ACCESSION = "MS:1001331";
	/** name for "X!Tandem:hyperscore" in PSI-MS */
	public static final String CV_XTANDEM_HYPERSCORE_NAME = "X!Tandem:hyperscore";
	
	/** accession for "modification specificity N-term" in PSI-MS */
	public static final String CV_MODIFICATION_SPECIFICITY_N_TERM_ACCESSION = "MS:1001189";
	/** name for "modification specificity N-term" in PSI-MS */
	public static final String CV_MODIFICATION_SPECIFICITY_N_TERM_NAME = "modification specificity N-term";
	
	/** accession for "modification specificity C-term" in PSI-MS */
	public static final String CV_MODIFICATION_SPECIFICITY_C_TERM_ACCESSION = "MS:1001190";
	/** name for "modification specificity C-term" in PSI-MS */
	public static final String CV_MODIFICATION_SPECIFICITY_C_TERM_NAME = "modification specificity C-term";

	/** accession for "sequence same-set protein" in PSI-MS */
	public static final String CV_SEQUENCE_SAME_SET_PROTEIN_ACCESSION = "MS:1001594";
	/** name for "sequence same-set protein" in PSI-MS */
	public static final String CV_SEQUENCE_SAME_SET_PROTEIN_NAME = "sequence same-set protein";
	
	/** accession for "sequence sub-set protein" in PSI-MS */
	public static final String CV_SEQUENCE_SUB_SET_PROTEIN_ACCESSION = "MS:1001596";
	/** name for "sequence sub-set protein" in PSI-MS */
	public static final String CV_SEQUENCE_SUB_SET_PROTEIN_NAME = "sequence sub-set protein";
	
	/** accession for "PSM-level FDRScore" in PSI-MS */
	public static final String CV_PSM_LEVEL_FDRSCORE_ACCESSION = "MS:1002355";
	/** name for "PSM-level FDRScore" in PSI-MS */
	public static final String CV_PSM_LEVEL_FDRSCORE_NAME = "PSM-level FDRScore";
	
	/** accession for "PSM-level combined FDRScore" in PSI-MS */
	public static final String CV_PSM_LEVEL_COMBINED_FDRSCORE_ACCESSION = "MS:1002356";
	/** name for "PSM-level combined FDRScore" in PSI-MS */
	public static final String CV_PSM_LEVEL_COMBINED_FDRSCORE_NAME = "PSM-level combined FDRScore";
	
	/** accession for "leading protein" in PSI-MS */
	public static final String CV_LEADING_PROTEIN_ACCESSION = "MS:1002401";
	/** name for "leading protein" in PSI-MS */
	public static final String CV_LEADING_PROTEIN_NAME = "leading protein";
	
	/** accession for "non-leading protein" in PSI-MS */
	public static final String CV_NON_LEADING_PROTEIN_ACCESSION = "MS:1002402";
	/** name for "non-leading protein" in PSI-MS */
	public static final String CV_NON_LEADING_PROTEIN_NAME = "non-leading protein";
	
	/** accession for "group representative" in PSI-MS */
	public static final String CV_GROUP_REPRESENTATIVE_ACCESSION = "MS:1002403";
	/** name for "non-leading protein" in PSI-MS */
	public static final String CV_GROUP_REPRESENTATIVE_NAME = "group representative";
	
	/** accession for "count of identified proteins" in PSI-MS */
	public static final String CV_COUNT_OF_IDENTIFIED_PROTEINS_ACCESSION = "MS:1002404";
	/** name for "count of identified proteins" in PSI-MS */
	public static final String CV_COUNT_OF_IDENTIFIED_PROTEINS_NAME = "count of identified proteins";
	
	/** accession for "cluster identifier" in PSI-MS */
	public static final String CV_CLUSTER_IDENTIFIER_ACCESSION = "MS:1002407";
	/** name for "cluster identifier" in PSI-MS */
	public static final String CV_CLUSTER_IDENTIFIER_NAME = "cluster identifier";
	
	/** accession for "protein group passes threshold" in PSI-MS */
	public static final String CV_PROTEIN_GROUP_PASSES_THRESHOLD_ACCESSION = "MS:1002415";
	/** name for "protein group passes thresholde" in PSI-MS */
	public static final String CV_PROTEIN_GROUP_PASSES_THRESHOLD_NAME = "protein group passes threshold";

	
	/**
	 * We don't ever want to instantiate this class
	 */
	private PIAConstants() {
		throw new AssertionError();
	}
}
