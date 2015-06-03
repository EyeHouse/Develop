package database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class consists of methods to verify if the user has entered the correct
 * E-mail address, has followed the password rules and re-entered it correctly
 * and includes a method to hash the password of the user before storing it to
 * the database.
 * 
 * @version 1.1
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class DataHandler {

	private static MessageDigest digester;

	/**
	 * Checks if the entered E-mail address is valid by making sure it contains
	 * a "@" and "." in the string.
	 * 
	 * @param email
	 *            The entered E-mail string.
	 * @return True implies that the E-mail address is valid.
	 */
	public static boolean isValidEmailAddress(String email) {
		boolean result;
		if (email.indexOf("@") != -1 && email.indexOf(".") != -1) {
			result = true;
		} else {
			System.out.println("Email invalid");
			result = false;
		}
		return result;
	}

	/**
	 * Checks if the password entered passes all the following criteria: contain
	 * at least one upper case letter, one lower case letter, one digit, and is
	 * 6 - 20 characters long. Also checks if the password entered and the
	 * password re-entered match.
	 * 
	 * @param password
	 *            Password string from the user.
	 * @param retypePassword
	 *            Re-entered password string from the user.
	 * @return True implies the passwords is strong and meets the
	 *         criteria and the re-entered password is correct.
	 */
	public static boolean passwordChecker(String password, String retypePassword) {

		if (password.equals(retypePassword)) {
			if (password.equals("")) {
				System.out.println("Password Fields Null");
				return false;
			}
		}
		// Setting rules for the password string.
		String regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,20}$";
		boolean result = password.matches(regexp);

		if (password.equals(retypePassword) && result == false)
			System.out.println("Please try another password");

		if (password.equals(retypePassword) && result == true)
			System.out.println("Strong password!");

		else if (!password.equals(retypePassword)) {
			result = false;
			System.out.println("Passwords do not match!");
		}
		return result;
	}

	// Method for encrypting the password starts here
	static {
		try {
			digester = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generates the MD5 of a given string. It addds to the security of the user
	 * as the passwords are hashed before they're stored in the database and the
	 * process cannot reversed.
	 * 
	 * @param password
	 *            The password string to be encrypted.
	 * @return The generated MD5 string. If the passwords do not pass the
	 *         passwordChecker method, it returns a null string.
	 */
	public static String crypt(String password) {
		if (password == null || password.length() == 0) {
			throw new IllegalArgumentException("Please enter a password!");
		}

		digester.update(password.getBytes());
		byte[] hash = digester.digest();
		StringBuffer hexString = new StringBuffer();

		for (int i = 0; i < hash.length; i++) {
			if ((0xff & hash[i]) < 0x10) {
				hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
			} else {
				hexString.append(Integer.toHexString(0xFF & hash[i]));
			}
		}
		return hexString.toString();
	}
}
