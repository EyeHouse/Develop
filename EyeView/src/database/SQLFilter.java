package database;

import java.util.Arrays;

/**
 * Filters dangerous words out of the user inputs.
 * 
 * @version 1.48 (15.03.15)
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 * 
 */
public class SQLFilter {

	/**
	 * The SQLWordCheck method makes sure that the user has not entered a SQL
	 * keyword before saving it to the database.
	 * 
	 * @param str
	 *            is the input string
	 * 
	 * @return Returns a boolean. True = SQL word present.
	 */
	public static boolean SQLWordCheck(String str) {

		boolean SQLWordPresent = false;

		// Dangerous words
		String[] words = { "DELETE", "COALESCE", "NULL", "CONNECT BY",
				"NULLIF", "ORDER BY", "FROM", "SELECT", "UNION", "HAVING",
				"WHERE", "TRUNCATE", "INSERT", "UPDATE", "JOIN", "MERGE",
				"DROP", "UPDATE", "*", ";", "DATABASE", "AND", "OR", "ALTER",
				"=", "TABLE", "CREATE" };

		if ((Arrays.asList(words).contains(str)) == true
				|| (Arrays.asList(words).contains(str.toUpperCase())) == true
				|| (Arrays.asList(words).contains(";"))
				|| (Arrays.asList(words).contains("*"))
				|| (Arrays.asList(words).contains("="))) {

			SQLWordPresent = true;
			System.out.println("SQL Word Detected!");
			str = "";
		}
		return SQLWordPresent;
	}

	static String sampleWord = "*";

	public static void main(String[] args) {
		SQLWordCheck(sampleWord);
	}

}
