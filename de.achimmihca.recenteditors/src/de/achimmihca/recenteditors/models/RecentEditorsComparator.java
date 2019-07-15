package de.achimmihca.recenteditors.models;

import java.util.Comparator;

public class RecentEditorsComparator implements Comparator<EditorModel> {
	public static enum SortDirection {
		ASCENDING, DESCENDING;
	}

	private SortDirection sortDirection = SortDirection.DESCENDING;

	public RecentEditorsComparator() {
	}

	@Override
	public int compare(EditorModel editor1, EditorModel editor2) {
		int result = compare( editor1.getLastCloseTime(), editor2.getLastCloseTime() );
		if( sortDirection == SortDirection.DESCENDING ) {
			result *= -1;
		}
		return result;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static int compare(final Comparable one, final Comparable two) {
		if( one == null ^ two == null ) {
			return ( one == null ) ? -1 : 1;
		}

		if( one == null && two == null ) {
			return 0;
		}

		return one.compareTo( two );
	}
}
