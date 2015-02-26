package parser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
//import com.mysql.jdbc.PreparedStatement;
import java.sql.PreparedStatement;

public class Database {

	// Public variables
	static Connection con = null;
	// database row names
	private final static int id = 1;
	private final static int firstName = 2;
	private final static int secondName = 3;
	private final static int userName = 4;
	private final static int email = 5;
	private final static int password = 6;
	private final static int landlord = 7;
	private final static int DOB = 8;
	private final static int admin = 9;

	public static void dbConnect() {
		// Access driver class from JAR
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			e.printStackTrace();
		}
		// Create a connection with db:master_db user:root pw:
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/master_db", "root", "");
		} catch (SQLException ex) {
			ex.printStackTrace();
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

	}

	// user insert
	public static int userInsert(User userobject) {
		// check the key details dont exist already, email,username,
		boolean blockInsert = false;
		// query
		try {
			PreparedStatement checkUser = con
					.prepareStatement("SELECT * FROM users WHERE username=? AND email=?");
			// parameterize queries
			checkUser.setString(1, userobject.username);
			checkUser.setString(2, userobject.email);
			ResultSet userExists = checkUser.executeQuery();
			
			//check if email or user fields already exist
			while (userExists.next()) {
				if (userExists.getString(email) != null) {
					blockInsert = true;
				} else if (userExists.getString(userName) != null) {
					blockInsert = true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(blockInsert == false) { 
			//execute insertion of user into table users
			try {
				PreparedStatement insertUser = con
						.prepareStatement("INSERT INTO users " + "VALUES (?,?,?,?,?,?,?,?,?)");
				//insert all the datas
				insertUser.setInt(id, 0);
				insertUser.setString(firstName, userobject.first_name);
				insertUser.setString(secondName, userobject.second_name);
				insertUser.setString(userName, userobject.username);
				insertUser.setString(email, userobject.email);
				insertUser.setString(password, userobject.password);
				insertUser.setBoolean(landlord, userobject.landlord);
				insertUser.setString(DOB, userobject.DOB);
				insertUser.setBoolean(admin, userobject.admin);
				//execute the query
				insertUser.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				e.getMessage();
			}
			//1 = success!
			return 1;
		}
		else {
			//failure to insert
			System.out.println("User or email already exists.");
			System.out.println("Please try again");	
			return 0;
		}
		

	}

	// handle login attempt
	public static String loginCheck(String username, String pw) {

		String userKey = null;
		ResultSet result = null;
		// check database to see if username password exists
		try {
			PreparedStatement stmt = con
					.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
			// parameterize queries
			stmt.setString(1, username);
			stmt.setString(2, pw);
			result = stmt.executeQuery();
			// loop through every row until
			while (result.next()) {
				// get id
				userKey = result.getString(password);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.getMessage();
		}

		return userKey;
	}

	// add method
	// delete method
	// filter input method

	public static void main(String[] args) throws Exception {

		String userKey = null;
		String username = "ISeeYou";
		String password = "Default";
		// Connect to the Database
		dbConnect();

		userKey = loginCheck(username, password);

		System.out.println("User has id number: " + userKey);

		// construct a dummy user to test user insert method
		User dummy1 = new User("JohnSmith69");
		dummy1.first_name = "John";
		dummy1.second_name = "Smith";
		dummy1.email = "js@york.ac.uk";
		// username set in contructor
		dummy1.landlord = false;
		dummy1.password = "password1";
		dummy1.DOB = "2015-02-25";
		dummy1.admin = false;
		// print details to check the instance
		dummy1.printUser();
		
		
		int successInsert = userInsert(dummy1);
		if(successInsert == 1) {
		System.out.println("\nUser Inserted!");
		}
		if(successInsert == 0) { 
			System.out.println("\nUser Already Exists!");
		}
		if(successInsert != 0 && successInsert != 1) System.out.println("Somethings went wrong");

	}

}
