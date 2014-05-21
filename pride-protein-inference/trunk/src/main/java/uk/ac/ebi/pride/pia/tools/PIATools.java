package uk.ac.ebi.pride.pia.tools;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import uk.ac.ebi.pride.pia.modeller.PIAModeller;


/**
 * Some handy functions.
 * @author julian
 *
 */
public class PIATools {
	
	/**
	 * We don't ever want to instantiate this class
	 */
	private PIATools() {
		throw new AssertionError();
	}
	
	
	/**
	 * Checks whether both objects are null or are equal.
	 */ 
	public  static boolean bothNullOrEqual(Object x, Object y) {
		return ( x == null ? y == null : x.equals(y) );
	}
	
	
	/**
	 * Round a double value and keeping (at max) the given number of decimal
	 * places.
	 * 
	 * @param value
	 * @param dec
	 * @return
	 */
	public static double round(double value, int dec) {
		double factor = Math.pow(10, dec);
		return Math.round(value * factor) / factor;
	}
	
	
	/**
	 * Calls the compareTo for the given objects, if none of them are null. If
	 * one of them is null, null is ordered before the other. If both are null,
	 * they are ccompared as equal.
	 * 
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static <T extends Comparable<T>> int CompareProbableNulls(T o1, T o2) {
		if (o1 == null) {
			if (o2 == null) {
				return 0;
			} else {
				return -1;
			}
		} else {
			if (o2 == null) {
				return 1;
			} else {
				return o1.compareTo(o2);
			}
		}
	}
	
	
	/**
	 * Print out the help, given the options
	 * @param options
	 */
	public static void printCommandLineHelp(Options options, String header) {
		HelpFormatter formatter = new HelpFormatter();
		
		formatter.printHelp(PIAModeller.class.getSimpleName(),
				header + "\nOptions:",
				options,
				"\nCopyright (C) 2013 Medizinisches Proteom-Center, " +
				"julian.uszkoreit@rub.de" +
				"\nThis is free software; see the source for copying conditions. " +
				"There is ABSOLUTELY NO warranty!",
				true);
	}
}
