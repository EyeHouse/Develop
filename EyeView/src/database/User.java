package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class User {

	@SuppressWarnings("unused")
	private final static int ID = 1;
	private final static int FNAME = 2;
	private final static int SNAME = 3;
	private final static int USER = 4;
	private final static int EMAIL = 5;
	private final static int PW = 6;
	private final static int LANDLORD = 7;
	private final static int BIRTHDATE = 8;
	private final static int PROPERTIES = 9;
	private final static int ADMIN = 10;

	// id should automatically be created on insertion
	public String first_name;
	public String second_name;
	public String email;
	public String username;
	public boolean landlord;
	public String password;
	public String DOB;
	public String properties;
	public boolean admin;

	// user contructor method
	public User(String username) {
		this.username = username;
	}

	// user contructor method
	public User(ResultSet userDetails) {
		// fill details
		try {
			this.username = userDetails.getString(USER);
			this.first_name = userDetails.getString(FNAME);
			this.second_name = userDetails.getString(SNAME);
			this.email = userDetails.getString(EMAIL);
			this.landlord = userDetails.getBoolean(LANDLORD);
			this.password = userDetails.getString(PW);
			this.DOB = userDetails.getString(BIRTHDATE);
			this.properties = userDetails.getString(PROPERTIES);
			this.admin = userDetails.getBoolean(ADMIN);
		} catch (SQLException e) {
			e.printStackTrace();
			e.getMessage();
		}
	}

	// fill user details
	public void firstName(String firstname) {
		first_name = firstname;
	}

	// user second name
	public void secondName(String secondname) {
		second_name = secondname;
	}

	// user email address
	public void email(String emailAddress) {
		email = emailAddress;
	}

	// true if user is to be granted landlord privileges
	public void landlord(boolean isLandlord) {
		landlord = isLandlord;
	}

	// user password
	public void password(String pw) {
		password = pw;
	}

	// dates in the form year/month/day xxxx-xx-xx
	public void DOB(String dateBirth) {
		DOB = dateBirth;
	}

	// properties in the form xxx,xxx,xxx
	public void properties(String propertyID) {
		properties = propertyID;
	}

	// true if user is to be granted admin privileges
	public void admin(boolean isAdmin) {
		admin = isAdmin;
	}

	public static ArrayList<String> getSavedProperties(String username) {

		ArrayList<String> properties = new ArrayList<String>();

		if (username != null) {
			User currentUser = Database.getUser(username);
			if (currentUser.properties != null) {
				int length = currentUser.properties.length();
				for (int i = 0; i < length; i += 4) {
					properties.add(currentUser.properties.substring(i, i + 3));
				}
			}
		}
		return properties;
	}

	public static void updateSavedProperties(String username,
			ArrayList<String> properties) {
		User currentUser = Database.getUser(username);
		String savedProperties = null;

		if (properties.size() == 0) {
			Database.userUpdate(currentUser, "properties", null, null);
		} else {
			for (int i = 0; i < properties.size(); i++) {
				if (i == 0)
					savedProperties = properties.get(0);
				else
					savedProperties = properties.get(i) + "," + savedProperties;
			}
			Database.userUpdate(currentUser, "properties", null,
					savedProperties);
		}
	}

	// option to print details for developer tests
	public void printUser() {
		System.out.println("\nUsername: " + username);
		System.out.println("Email: " + email);
		System.out.println("Password: " + password);
	}

}