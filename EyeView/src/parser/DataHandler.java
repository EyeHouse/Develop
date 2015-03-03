package parser;

public class DataHandler {

	public DataHandler() {

	}

	/**
	 * Filter out SQL injections.
	 * 
	 * @param userInput
	 * @return userInput (if it passes filter)
	 * @throws Exception
	 */
	public static String SQLFilter(String userInput) throws Exception {

		return userInput;
	}

	/**
	 * Filter tags off strings.
	 * 
	 * @param userInput
	 * @return userInput (if it passes filter)
	 * @throws Exception
	 */
	public static String normalizeString(String userInput) throws Exception {

		return userInput;
	}
	
	/**
	 * 
	 * @param email
	 * @return 
	 */
	public static boolean isValidEmailAddress(String email) {
		boolean result; 
		// email = "fahim@yahoo.com";
		if (email.indexOf("@") != -1 && email.indexOf(".") != -1) {
			result = true;
		} else {
			System.out.println("Email invalid");
			result = false;
		}
		return result;
	}
	/**
	 * 
	 * @param password
	 * @param retypePassword
	 * @return 
	 */
	public static boolean passwordChecker(String password, String retypePassword) {
		// Password should contain at least:
		// one upper case letter,
		// one lower case letter,
		// one digit,
		// be 6 - 20 character long.
		String regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,20}$";
		boolean result = password.matches(regexp);

		if (password == retypePassword && result == false)
			System.out.println("Please try another password");

		if (password == retypePassword && result == true)
			System.out.println("Strong password!");

		else if (password != retypePassword) {
			result = false;
			System.out.println("Passwords do not match!");
		}

		return result;
	}
}
