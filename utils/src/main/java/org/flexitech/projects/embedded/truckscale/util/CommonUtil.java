package org.flexitech.projects.embedded.truckscale.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;

import com.google.myanmartools.TransliterateU2Z;
import com.google.myanmartools.TransliterateZ2U;
import com.google.myanmartools.ZawgyiDetector;

public class CommonUtil {

	private static final DecimalFormat formatter = new DecimalFormat("#,###");

	public static String formatNumber(Number b) {
		return (b != null) ? formatter.format(b) : "0";
	}

	public static Number formatNumber(String s) {
		try {
			return (s != null && !s.isEmpty()) ? formatter.parse(s) : 0;
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static Integer convertNumberToInteger(Number value) {
		if(value == null) return 0;
		return value.intValue();
	}
	
	public static String UnicodeToZawgyi(String value) {
		if (value != null) {

			if (isUnicode(value)) {
				final TransliterateU2Z u2Z = new TransliterateU2Z("Unicode to Zawgyi");
				String result = u2Z.convert(value);
				return result;
			} else {
				return value;
			}

		}
		return "";
	}

	public static String ZawgyiToUnicode(String string) {
		if (string != null) {
			final TransliterateZ2U z2U = new TransliterateZ2U("Zawgyi to Unicode");
			String result = z2U.convert(string);
			return result;
		}
		return "";
	}

	public static boolean isUnicode(String string) {
		final ZawgyiDetector detector = new ZawgyiDetector();
		DecimalFormat df2 = new DecimalFormat("#.#");
		double score = detector.getZawgyiProbability(string);
		df2.setRoundingMode(RoundingMode.UP);
		String uniorzawgyi = String.valueOf(df2.format(score));
		if (uniorzawgyi.equals("1")) {
			return false;
		} else {
			return true;
		}
	}

	public static String analyseReportContent(String string) {
		if(string == null) return "";
		if(isUnicode(string)) {
			return UnicodeToZawgyi(string);
		}
		return string;
	}
	
	public static String getWeightDesc(Double weight, Number perKgValue, String postfix) {
		
		if(weight == null) return "0";
		
		if(!CommonValidators.isValidObject(perKgValue)) {
			return formatNumber(weight) + " " + postfix;
		}
		
		Double finalWeight = weight * perKgValue.doubleValue();
		
		return formatNumber(finalWeight) + " " + postfix;
	}

}
