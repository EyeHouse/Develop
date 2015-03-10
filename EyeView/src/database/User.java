package database;

import java.sql.ResultSet;
import java.sql.SQLException;

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
	private final static int ADMIN = 9;

	// id should automatically be created on insertion
	String first_name;
	String second_name;
	String email;
	public String username;
	boolean landlord;
	String password;
	String DOB;
	boolean admin;

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

	// true if user is to be granted admin privileges
	public void admin(boolean isAdmin) {
		admin = isAdmin;
	}

	// option to print details for developer tests
	public void printUser() {
		System.out.println("\nUsername: " + username);
		System.out.println("Email: " + email);
		System.out.println("Password: " + password);
	}

}
