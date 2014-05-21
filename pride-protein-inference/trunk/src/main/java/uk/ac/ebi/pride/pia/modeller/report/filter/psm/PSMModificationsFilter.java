package uk.ac.ebi.pride.pia.modeller.report.filter.psm;

import uk.ac.ebi.pride.pia.intermediate.Modification;
import uk.ac.ebi.pride.pia.modeller.psm.PSMReportItem;
import uk.ac.ebi.pride.pia.modeller.report.filter.AbstractFilter;
import uk.ac.ebi.pride.pia.modeller.report.filter.FilterComparator;
import uk.ac.ebi.pride.pia.modeller.report.filter.FilterType;
import uk.ac.ebi.pride.pia.modeller.report.filter.RegisteredFilters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PSMModificationsFilter extends AbstractFilter {
	
	protected static final String shortName = RegisteredFilters.PSM_MODIFICATIONS_FILTER.getShortName();
	
	protected static final String name = "Modifications Filter for PSM";
	
	protected static final String filteringName = "Modifications (PSM)";
	
	public static final FilterType filterType = FilterType.modification;
	
	protected static final Class<String> valueInstanceClass = String.class;
	
	private String value;
	
	
	
	public PSMModificationsFilter(FilterComparator arg, String value, boolean negate) {
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
			List<Modification> modList = new ArrayList<Modification>();
			for (Map.Entry<Integer, Modification> modIt : ((PSMReportItem) o).getModifications().entrySet()) {
				modList.add(modIt.getValue());
			}
			return modList;
		} else if (o instanceof List<?>) {
			List<Modification> modList = new ArrayList<Modification>();
			for (Object obj : (List<?>)o) {
				if (obj instanceof Modification) {
					modList.add((Modification)obj);
				}
			}
			return modList;
		}
		
		// nothing supported
		return null;
	}

	@Override
	public boolean supportsClass(Object c) {
		if (c instanceof PSMReportItem) {
			return true;
		} else {
			return false;
		}
	}
}
