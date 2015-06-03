package database;

import java.util.Arrays;

/**
 * This class restricts the users to enter SQL keywords into textfields, which
 * might otherwise be executed into the database.
 * 
 * @version 1.1
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class SQLFilter {

	/**
	 * Checks that the user has not entered a SQL keyword before saving it to
	 * the database.
	 * 
	 * @param str
	 *            The input string.
	 * 
	 * @return A boolean. True means a SQL word is present.
	 */
	public static boolean SQLWordCheck(String str) {

		boolean SQLWordPresent = false;

		// A list of SQL keywords/statements.
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

	// Checking method's functionality:
	/*
	 * static String sampleWord = "*";
	 * 
	 * public static void main(String[] args) { SQLWordCheck(sampleWord); }
	 */

}
