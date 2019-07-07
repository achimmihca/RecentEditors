package de.achimmihca.recenteditors.utils;

public final class StringUtils {

	private StringUtils() {
	}

	public static boolean isNullOrEmpty(CharSequence s) {
		return ( s == null || s.length() == 0 );
	}

}
