package uk.ac.ebi.pride.pia.modeller.score;

/**
 * This interface implements everything needed to compute the FDRScore.
 * 
 * @author julian
 *
 */
public interface FDRScoreComputable extends FDRComputable {
	/**
	 * Setter for the FDR score.
	 * @return
	 */
	public void setFDRScore(Double score);
	
	/**
	 * Getter for the fdrScore.
	 * @return
	 */
	public ScoreModel getFDRScore();
}
