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
	//database row names
	private final int id = 1;
	private final int firstName = 2;
	private final int secondName = 3;
	private final int userName = 4;
	private final int email = 5;
	private final int password = 6;
	private final int landlord = 7;
	private final int DOB = 8;
	private final int admin = 9;
	
	

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

	// login check
	public String loginCheck(String username, String password) {

		String userKey = null;
		ResultSet result = null;
		// check database to see if username password exists

		try {
			PreparedStatement stmt = con
					.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
			// parameterize queries
			stmt.setString(1, username);
			stmt.setString(2, password);
			result = stmt.executeQuery();
			// loop through every row until
			while (result.next()) {
				//get id
				result.getString(id);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.getMessage();	}
		
		userKey = "id";
		
		return userKey;
	}

	// add method
	// delete method
	// filter input method

	public static void main(String[] args) throws Exception {

		// Connect to the Database
		dbConnect();
		
		

		// execute a test query (find username ISeeYou with pw default from
		// table users)
		PreparedStatement stmt = con
				.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
		// parameterize queries
		stmt.setString(1, "ISeeYou");
		stmt.setString(2, "default");
		ResultSet result = stmt.executeQuery();

		while (result.next()) {
			System.out.println("Found user: " + result.getString(4)
					+ "\nBorn: " + result.getString(8));
			// getString returns data
			// 1 and 2 are fields
		}

	}

}
