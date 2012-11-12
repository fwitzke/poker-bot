package br.poker.util;

public class Logger {
	public static final int OUT = 1;
	public static final int ERROR = 2;
	public static final int LOG = 3;
	public static final int DEBUG = 4;

	private static int log_level = LOG;
	
	public static void setLevel(int level) {
		log_level = level;
	}

	public static void debug(String message) {
		if (log_level >= DEBUG)
			System.out.println(message);
	}

	public static void log(String message) {
		if (log_level >= LOG)
			System.out.println(message);
	}

	public static void error(String message) {
		if (log_level >= ERROR)
			System.err.println(message);
	}

	public static void out(Object message) {
		if (log_level >= OUT)
			System.out.println(message);
	}
}
