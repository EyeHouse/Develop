package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Blob;

/**
 * Holds information associated with a user.
 * 
 * @version 1.48 (15.03.15)
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class User {

	private final static int UID = 1;
	private final static int FNAME = 2;
	private final static int SNAME = 3;
	private final static int USER = 4;
	private final static int EMAIL = 5;
	private final static int PW = 6;
	private final static int LANDLORD = 7;
	private final static int BIRTHDATE = 8;
	private final static int ADMIN = 9;
	private final static int PROFIMG = 10;
	private final static int PROP = 11;
	private final static int SKYPE = 12;
	private final static int BIO = 13;

	// ID should automatically be created on insertion
	public int uid;
	public String first_name;
	public String second_name;
	public String email;
	public String username;
	public boolean landlord;
	public String password;
	public String DOB;
	public boolean admin;
	public Blob profimg;
	public String properties;
	public String skype;
	public String bio;

	/**
	 * Constructor method
	 * 
	 * @param username
	 */
	public User(String username) {
		this.username = username;
	}

	/**
	 * Constructor method
	 * 
	 * @param userDetails
	 */
	public User(ResultSet userDetails) {
		
		try {
			this.uid = userDetails.getInt(UID);
			this.username = userDetails.getString(USER);
			this.first_name = userDetails.getString(FNAME);
			this.second_name = userDetails.getString(SNAME);
			this.email = userDetails.getString(EMAIL);
			this.landlord = userDetails.getBoolean(LANDLORD);
			this.password = userDetails.getString(PW);
			this.DOB = userDetails.getString(BIRTHDATE);
			this.admin = userDetails.getBoolean(ADMIN);
			this.profimg = (Blob) userDetails.getBlob(PROFIMG);
			this.properties = userDetails.getString(PROP);
			this.skype = userDetails.getString(SKYPE);
			this.bio = userDetails.getString(BIO);
		} catch (SQLException e) {
			e.printStackTrace();
			e.getMessage();
		}
	}

	// Set details
	public void firstName(String firstname) {
		first_name = firstname;
	}

	public void secondName(String secondname) {
		second_name = secondname;
	}

	public void email(String emailAddress) {
		email = emailAddress;
	}

	public void landlord(boolean isLandlord) {
		landlord = isLandlord;
	}

	public void password(String pw) {
		password = pw;
	}

	// Dates in the form year/month/day xxxx-xx-xx
	public void DOB(String dateBirth) {
		DOB = dateBirth;
	}

	public void admin(boolean isAdmin) {
		admin = isAdmin;
	}

	public void profimg(Blob imageblob) {
		profimg = imageblob;
	}

	public void properties(String userHouses) {
		properties = userHouses;
	}

	public void skype(String username) {
		skype = username;
	}

	public void bio(String biography) {
		bio = biography;
	}

	/**
	 * Gets the saved properties for a user.
	 * 
	 * @param username
	 *            Username of user to retrieve
	 * @return Arraylist of property IDs stored by the user
	 */
	public static ArrayList<String> getSavedProperties(String username) {

		// Instantiate an arraylist to contain the property IDs
		ArrayList<String> properties = new ArrayList<String>();

		// If a valid username has been input
		if (username != null) {

			// Retrieve the user from the database
			User currentUser = Database.getUser(username);

			// If the "properties" field of the user is not empty
			if (currentUser.properties != null) {

				// Check the length of the "properties" field
				int length = currentUser.properties.length();

				/*
				 * Loop through the "properties" field populate the property ID
				 * arraylist with the stored IDs
				 */
				for (int i = 0; i < length; i += 4) {

					// Extract the current substring of the "properties" field
					String propertyID = currentUser.properties.substring(i,
							i + 3);

					// Attempt to retrieve the current property ID
					House house = Database.getHouse(Integer
							.parseInt(propertyID));

					// If the property exists
					if (house != null) {

						// Add the current ID to the output arraylist
						properties.add(propertyID);
					}
				}
			}
		}

		/*
		 * Update the database with any changes to the saved properties as a
		 * result of unbound property IDs
		 */
		updateSavedProperties(username, properties);
		
		// Return the arraylist of property IDs
		return properties;
	}

	/**
	 * Updates saved properties.
	 * 
	 * @param username
	 *            Username of user to update
	 * @param properties
	 *            Arraylist of strings containing property IDs to save
	 */
	public static void updateSavedProperties(String username,
			ArrayList<String> properties) {

		// Retrieve the specified user from the database
		User currentUser = Database.getUser(username);

		// Instantiate a string to store parsed list of propert IDs
		String savedProperties = null;

		// If there are no property IDs to store
		if (properties.size() == 0) {

			// Set the "properties" field of the user to null in the database
			Database.userUpdate(currentUser, "properties", null, null);
		}

		/*
		 * Otherwise loop through the property IDs and parse into a database
		 * compatible string
		 */
		else {
			for (int i = 0; i < properties.size(); i++) {

				// If this is the first property ID in the set
				if (i == 0) {

					// Add the ID to the database string
					savedProperties = properties.get(0);
				} else {

					/*
					 * Otherwise append the current property ID to the end of
					 * the database string
					 */
					savedProperties = properties.get(i) + "," + savedProperties;
				}
			}

			/*
			 * Update the "property" field of the current user with the parsed
			 * database string
			 */
			Database.userUpdate(currentUser, "properties", null,
					savedProperties);
		}
	}
}
