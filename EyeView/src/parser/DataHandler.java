package parser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class DataHandler {
	
	private static MessageDigest digester;
	/**
	 * 
	 * @param email
	 * @return
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
	
	// Method for encrypting the password starts here
		static {
			try {
				digester = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}

		/**
		 * The crypt method is used to generate the MD5 of a given string.
		 * 
		 * @param password
		 *            is the string to be encrypted.
		 * @return Returns the generated MD5 string. If the passwords do not pass
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
