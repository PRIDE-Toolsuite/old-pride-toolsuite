package uk.ac.ebi.pride.pia.modeller.report.filter.peptide;

import uk.ac.ebi.pride.data.core.PeptideSequence;
import uk.ac.ebi.pride.pia.modeller.peptide.ReportPeptide;
import uk.ac.ebi.pride.pia.modeller.report.filter.AbstractFilter;
import uk.ac.ebi.pride.pia.modeller.report.filter.FilterComparator;
import uk.ac.ebi.pride.pia.modeller.report.filter.FilterType;
import uk.ac.ebi.pride.pia.modeller.report.filter.RegisteredFilters;

/**
 * Filter a Sequence of a Peptide that are not interested for the inference algorithm.
 *
 */
public class PeptideSequenceFilter extends AbstractFilter {
	
	protected static final String shortName = RegisteredFilters.PEPTIDE_SEQUENCE_FILTER.getShortName();
	
	protected static final String name = "Sequence Filter for Peptide";
	
	protected static final String filteringName = "Sequence (Peptide)";
	
	public static final FilterType filterType = FilterType.literal;
	
	protected static final Class<String> valueInstanceClass = String.class;
	
	private String value;
	
	
	
	public PeptideSequenceFilter(FilterComparator arg, String value, boolean negate) {
		this.comparator = arg;
		this.value = value;
		this.negate = negate;
	}
	
	
	@Override
	public String getShortName() {
		return shortName;
	}
	
	
	public static String shortName() {
		return shortName;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	
	public static String name() {
		return name;
	}
	
	@Override
	public String getFilteringName() {
		return filteringName;
	}
	
	
	public static String filteringName() {
		return filteringName;
	}
	
	@Override
	public Object getFilterValue() {
		return value;
	}
	
	@Override
	public FilterType getFilterType() {
		return filterType;
	}
	
	
	public static boolean isCorrectValueInstance(Object value) {
		return valueInstanceClass.isInstance(value);
	}
	
	@Override
	public Object getObjectsValue(Object o) {
		if (o instanceof PeptideSequence) {
			return ((PeptideSequence) o).getSequence();
		} else if (o instanceof String) {
			return o;
		} else {
			// nothing supported
			return null;
		}
	}
	
	@Override
	public boolean supportsClass(Object c) {
		if (c instanceof ReportPeptide) {
			return true;
		} else {
			return false;
		}
	}
}