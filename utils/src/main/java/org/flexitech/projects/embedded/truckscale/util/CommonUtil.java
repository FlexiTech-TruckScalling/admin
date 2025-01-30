package org.flexitech.projects.embedded.truckscale.util;

import java.text.DecimalFormat;
import java.text.ParseException;

public class CommonUtil {

	private static final DecimalFormat formatter = new DecimalFormat("#,###.##");

	public static String formatNumber(Number b) {
		return (b != null) ? formatter.format(b) : null;
	}

	public static Number formatNumber(String s) {
		try {
			return (s != null && !s.isEmpty()) ? formatter.parse(s) : null;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

}
