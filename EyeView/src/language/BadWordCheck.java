package language;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to check strings for words listed in the black listed
 * words text file
 * 
 * @version 3.8 (02.06.15)
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class BadWordCheck {
	
	private Scanner x;
	private String fileName = "src/language/blackListedWords.txt";
	private String badWord;
	private Pattern punctuation;

	public BadWordCheck() {
		punctuation = Pattern.compile("[A-Za-z]{1,40}");
	}

	/**
	 * This method opens the black listed words file.
	 */
	public void openFile() {
		try {
			File file = new File(fileName);
			x = new Scanner(file);
		} catch (Exception e) {
			System.out.println("Could not find file: " + fileName);
		}
	}

	/**
	 * This method closes the black listed words file.
	 */
	public void closeFile() {
		x.close();
	}

	/**
	 * This method checks if a single word contains a word listed in the black
	 * listed words file.
	 * 
	 * @param inputText
	 *            The word that the will be checked against the black listed
	 *            words file.
	 * 
	 * @return Whether a word contains a word stored in the black listed words
	 *         file. True if it does, false if it matches no word in the black
	 *         listed words file.
	 */
	public boolean checkforBlackListedWords(String inputText) {

		boolean badWordDectected = false;

		Matcher regexMatcher = punctuation.matcher(inputText);
		regexMatcher.find();
		openFile();

		while (x.hasNext()) {
			badWord = x.nextLine();
			if (inputText == "") {
				badWordDectected = false;
				break;
			}
			try {
				badWordDectected = regexMatcher.group().trim()
						.replace("ing", "").replace("er", "").toLowerCase()
						.equals(badWord);
				if (badWordDectected == true) {
					break;
				}
			} catch (Exception e) {
			}
		}

		closeFile();

		return badWordDectected;
	}

	/**
	 * Locates and returns the location of a black listed word within the
	 * inputed string.
	 * 
	 * @param inputText
	 *            Sentence that will have its black listed words located.
	 * 
	 * @return A list of the locations, 0 meaning no black listed words were
	 *         found, 1 being the first word.
	 */
	public List<Integer> locateBadWords(String inputText) {

		List<Integer> badWordLocations = new ArrayList<Integer>();
		boolean badWordDetected = false;

		String[] inputTextWordArray = inputText.split(" ");

		for (int i = 0; i < inputTextWordArray.length; i++) {
			badWordDetected = checkforBlackListedWords(inputTextWordArray[i]);

			if (badWordDetected == true) {
				badWordLocations.add(i + 1);
			}
		}
		return badWordLocations;
	}

	/**
	 * Locates and returns the original inputed string but amended to have
	 * brackets (eg. []) around black listed words.
	 * 
	 * @param inputText
	 *            Sentence that will have its black listed words located and
	 *            highlighted
	 * 
	 * @return The sentence that has its black listed words located and
	 *         highlighted
	 */
	public String highlightBlackListedWords(String inputText) {

		StringBuilder sb = new StringBuilder();
		boolean badWordDetected = false;
		String amendedInputString = null;
		ArrayList<String> inputStringWithBadWordsFlagged = new ArrayList<String>();

		String[] inputTextWordArray = inputText.split(" ");

		for (int i = 0; i < inputTextWordArray.length; i++) {
			badWordDetected = checkforBlackListedWords(inputTextWordArray[i]);

			if (badWordDetected == true) {
				inputStringWithBadWordsFlagged
						.add(("[" + inputTextWordArray[i] + "]"));
			} else {
				inputStringWithBadWordsFlagged.add(inputTextWordArray[i]);
			}
		}

		for (String str : inputStringWithBadWordsFlagged) {
			sb.append(str).append(" ");
		}

		amendedInputString = sb.toString();
		return amendedInputString;
	}

	/**
	 * Determines whether a black listed word is located somewhere within the
	 * inputed sentence.
	 * 
	 * @param inputText
	 *            Sentence that will be checked for black listed words.
	 * 
	 * @return True if black listed word located, false if not.
	 */
	public boolean containsBlackListedWords(String inputText) {

		boolean badWordDetected = false;
		String[] inputTextWordArray = null;

		if (inputText.equals("")) {
			return false;
		}

		inputTextWordArray = inputText.split(" ");

		for (int i = 0; i < inputTextWordArray.length; i++) {
			badWordDetected = checkforBlackListedWords(inputTextWordArray[i]);

			if (badWordDetected == true) {
				break;
			}
		}

		return badWordDetected;
	}
}
