package fatture.util;

public class StringComparator {

	public static boolean containsInsensitive(String str, String splitStr, String sub, String splitSub) {
		return contains(str.toUpperCase(), splitStr.toUpperCase(), sub.toUpperCase(), splitSub.toUpperCase());
	}

	public static boolean contains(String str, String splitStr, String sub, String splitSub) {

		String[] subArr = sub.split(splitSub);
		String[] strArr = str.split(splitStr);
		int si = 0;

		for (String tmpSub : subArr) {
			for (String tmpStr : strArr)
				if (compare(tmpStr, tmpSub) > 0.7) // controllo se contenuta
					si++;
		}

		if (si > 0)
			return true;
		else
			return false;
	}

	public static double compare(final String s1, final String s2) {
		double retval = 0.0;
		final int n = s1.length();
		final int m = s2.length();
		if (0 == n) {
			retval = m;
		} else if (0 == m) {
			retval = n;
		} else {
			retval = 1.0 - (compare(s1, n, s2, m) / (Math.max(n, m)));
		}
		return retval;
	}

	private static double compare(final String s1, final int n, final String s2, final int m) {
		int matrix[][] = new int[n + 1][m + 1];
		for (int i = 0; i <= n; i++) {
			matrix[i][0] = i;
		}
		for (int i = 0; i <= m; i++) {
			matrix[0][i] = i;
		}

		for (int i = 1; i <= n; i++) {
			int s1i = s1.codePointAt(i - 1);
			for (int j = 1; j <= m; j++) {
				int s2j = s2.codePointAt(j - 1);
				final int cost = s1i == s2j ? 0 : 1;
				matrix[i][j] = min3(matrix[i - 1][j] + 1, matrix[i][j - 1] + 1, matrix[i - 1][j - 1] + cost);
			}
		}
		return matrix[n][m];
	}

	private static int min3(final int a, final int b, final int c) {
		return Math.min(Math.min(a, b), c);
	}
}
