package uk.ac.ebi.pride.pia.modeller.report.filter.psm;

import uk.ac.ebi.pride.pia.modeller.psm.PSMReportItem;
import uk.ac.ebi.pride.pia.modeller.report.filter.AbstractFilter;
import uk.ac.ebi.pride.pia.modeller.report.filter.FilterComparator;
import uk.ac.ebi.pride.pia.modeller.report.filter.FilterType;
import uk.ac.ebi.pride.pia.modeller.report.filter.RegisteredFilters;

public class DeltaMassFilter extends AbstractFilter {
	
	protected static final String shortName = RegisteredFilters.DELTA_MASS_FILTER.getShortName();
	
	protected static final String name = "dMass Filter";
	
	protected static final String filteringName = "dMass (PSM)";
	
	public static final FilterType filterType = FilterType.numerical;
	
	protected static final Class<Number> valueInstanceClass = Number.class;
	
	private Double value;
	
	
	
	public DeltaMassFilter(FilterComparator arg, Double value, boolean negate) {
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
		if (o instanceof PSMReportItem) {
			// if we get an ReportItem, return its charge value
			return ((PSMReportItem)o).getDeltaMass();
		} else if (o instanceof Number) {
			// if we get an Number, just return it
			return o;
		} else {
			// nothing supported
			return null;
		}
	}
	
	@Override
	public boolean supportsClass(Object c) {
		if ((c instanceof PSMReportItem) ||
				(c instanceof Number)) {
			return true;
		} else {
			return false;
		}
	}
}
