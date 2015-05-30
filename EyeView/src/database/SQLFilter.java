package database;

import java.util.Arrays;

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

		String[] words = { "DELETE", "COALESCE", "NULL", "CONNECT BY",
				"NULLIF", "ORDER BY", "FROM", "SELECT", "UNION", "HAVING",
				"WHERE", "TRUNCATE", "INSERT", "UPDATE", "JOIN", "MERGE",
				"DROP", "UPDATE", "*", ";", "DATABASE", "AND", "OR", "ALTER" , "=" ,
				"TABLE", "CREATE" };

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

	// Checking
	static String sampleWord = "*";

	public static void main(String[] args) {
		SQLWordCheck(sampleWord);
	}

}

/*
 * This can to be put the same way as BadWordCheck..
 * 
 * In Register.java it could be:
 * 
 * 
 * (inside saveChanges..)
 * 
 * if ((DataHandler.SQLWordCheck(username.getText()) == true) ||
 * (DataHandler.SQLWordCheck(firstname.getText()) == true) ||
 * (DataHandler.SQLWordCheck(lastname.getText()) == true)) { createWarningPopup(
 * "SQL keyword found, please check username, first name or last name!");
 * dialogStage.show(); }
 */

