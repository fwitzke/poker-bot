package br.poker.util;

public class Helper {
	public static boolean defined(Object object) {
		return object != null && !"".equals(object);
	}

	public static int toCents(String number) {
		if("All In".equalsIgnoreCase(number))
			return 0;
		try {
			number = number.replaceAll("\\$", "");
			number = number.replaceAll(",", "");
			double doubleNumber = Double.parseDouble(number);
			doubleNumber = doubleNumber * 100; // converts to cents
			return (int) doubleNumber;
		} catch (NumberFormatException e) {
			return -1;
		}
	}
}