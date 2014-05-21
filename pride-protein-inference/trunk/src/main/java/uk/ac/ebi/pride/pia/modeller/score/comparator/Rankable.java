package uk.ac.ebi.pride.pia.modeller.score.comparator;


/**
 * Interface for simple ranking.<br/>
 * 
 * The values of the rank may range from 0 to {@link Long#MAX_VALUE}. A value
 * smaller 0 is interpreted as not ranked.
 * 
 * @author julian
 *
 */
public interface Rankable extends ScoreComparable {
	/**
	 * Returns the score value with the given name.
	 * @param scoreName
	 * @return
	 */
	public abstract Double getScore(String scoreName);
	
	
	/**
	 * Getter for the rank.
	 * @return
	 */
	public abstract Long getRank();
	
	
	/**
	 * Setter for the rank.
	 * @return
	 */
	public abstract void setRank(Long rank);
}
