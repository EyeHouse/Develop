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

}
