package parser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
//import com.mysql.jdbc.PreparedStatement;
import java.sql.PreparedStatement;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class Database {

	// Public variables
	static Connection con = null;
	// Define the column numbers of the db table as integer variables
	// 'id' autoincrements due to the table set up, enter any integer when
	// required
	private final static int id = 1;
	private final static int firstName = 2;
	private final static int secondName = 3;
	private final static int userName = 4;
	private final static int email = 5;
	private final static int password = 6;
	private final static int landlord = 7;
	private final static int DOB = 8;
	private final static int admin = 9;

	/**
	 * A void function used to open our database connection
	 */
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

	/**
	 * A basic function that take an instance of User.java, and enters it into
	 * the database you're connected to.
	 * 
	 * @param userobject
	 * @return 1 if user is inserted successfully
	 * @return 0 if failure to insert
	 */
	public static int userInsert(User userobject) {
		// Check if unique details don't exist already (email, username)
		boolean blockInsert = false;
		// query
		try {
			PreparedStatement checkUser = con
					.prepareStatement("SELECT * FROM users WHERE username=? AND email=?");
			// parameterise queries
			checkUser.setString(1, userobject.username);
			checkUser.setString(2, userobject.email);
			ResultSet userExists = checkUser.executeQuery();

			// Check if email or user fields already exist
			while (userExists.next()) {
				if (userExists.getString(email) != null) {
					blockInsert = true;
				} else if (userExists.getString(userName) != null) {
					blockInsert = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (blockInsert == false) {
			// execute insertion of user into table users
			try {
				PreparedStatement insertUser = con
						.prepareStatement("INSERT INTO users "
								+ "VALUES (?,?,?,?,?,?,?,?,?)");
				// insert the users data
				insertUser.setInt(id, 0);
				insertUser.setString(firstName, userobject.first_name);
				insertUser.setString(secondName, userobject.second_name);
				insertUser.setString(userName, userobject.username);
				insertUser.setString(email, userobject.email);
				insertUser.setString(password, userobject.password);
				insertUser.setBoolean(landlord, userobject.landlord);
				insertUser.setString(DOB, userobject.DOB);
				insertUser.setBoolean(admin, userobject.admin);
				// execute the query
				insertUser.executeUpdate();
			} catch (SQLException e) {
				// catch the error get the message
				e.printStackTrace();
				e.getMessage();
			}
			// 1 = success!
			return 1;
		} else {
			// failure to insert
			System.out.println("User or email already exists.");
			System.out.println("Please try again");
			return 0;
		}
	}

	/**
	 * Updates a single field for user 'user' in the database you're connected
	 * to.
	 * 
	 * @param user
	 * @param fieldSelect
	 * @param priv
	 * @param newField
	 * @return
	 */
	public static int editUser(User user, String fieldSelect, Boolean priv,
			String newField) {
		// prepare a statement to update a field 'field
		try {
			PreparedStatement editUser = con
					.prepareStatement("UPDATE users SET ?=? WHERE username=? AND email=?");
			// parameterise data
			editUser.setString(1, fieldSelect);
			// if there's a string use string data else it must be a bool
			if (newField != null)
				editUser.setString(2, newField);
			else
				editUser.setBoolean(2, priv);
			editUser.setString(3, user.username);
			editUser.setString(4, user.email);
			// execute query
			editUser.executeUpdate();
		} catch (SQLException e) {
			e.getMessage();
			e.getErrorCode();
			e.printStackTrace();
		}

		// updated a string field
		return 1;
	}

	/**
	 * To be used if the user has been confirmed to exist in the login stage if
	 * they do exist then the username and password is correct this gets all the
	 * users details and stores them in an object
	 */
	public static User getUser(String userID) throws NullPointerException {
		// Takes a unique field (username) and returns the user
		User user = null;
		ResultSet userDetails = null;
		// select that user
		try {
			PreparedStatement getUser = con
					.prepareStatement("SELECT * FROM users WHERE username=?");
			// parameterise inputs
			getUser.setString(1, userID);
			// execute
			userDetails = getUser.executeQuery();
			// take all the users details and put them in an instance of user
			while (userDetails.next()) {
				// contruct a instance using the logged on users details
				user = new User(userDetails);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			e.getMessage();
		}
		if (user == null)
			throw new NullPointerException();
		else
			return user;

	}

	/**
	 * Takes a unique detail from the user who is logged on and deletes their
	 * account information.
	 * 
	 * @param sessionKey
	 * @return 1 if success
	 * @return 0 if failure
	 */
	public static int userDelete(String sessionKey) {
		try {
			// Takes a unique field (username) and deletes the corresponding
			// account
			PreparedStatement dropUser = con
					.prepareStatement("DELETE FROM users WHERE username=?");
			// Parameterise inputs
			dropUser.setString(1, sessionKey);
			// Execute SQL drop statement
			dropUser.executeUpdate();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			e.getMessage();
			return 0;
		}
	}

	/**
	 * Checks a user with username and pw exists Can be used to log users in.
	 * 
	 * @param username
	 * @param pw
	 * @return user
	 */
	public static User loginCheck(String username, String pw) {

		User user = null;
		ResultSet result = null;
		// check database to see if username password exists
		try {
			PreparedStatement stmt = con
					.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
			// parameterise queries
			stmt.setString(1, username);
			stmt.setString(2, pw);
			result = stmt.executeQuery();
			// loop through every row until
			while (result.next()) {
				// get id
				user = new User(result);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.getMessage();
		}
		if (user == null)
			return null;
		else
			return user;

	}

	/**
	 * Takes the information required to create a new user checks they don't
	 * already exist and enters them into the database.
	 * 
	 * @param newUser
	 * @return 1 success
	 * @return 0 failure
	 */
	public static int registerUser(User newUser) {

		sendMail();

		return 0;
	}

	/**
	 * Testing the mail methods
	 */
	public static void sendMail() {
		// Recipient's email ID needs to be mentioned.
		String to = "tb789@york.ac.uk";

		// Sender's email ID needs to be mentioned
		String from = "tb789@york.ac.uk";

		// Assuming you are sending email from localhost
		String host = "localhost";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", host);

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));

			// Set Subject: header field
			message.setSubject("Hello World");

			
			// Current date and time - for identifying test mail
			Date date = new Date();
			// Now set the actual message
			message.setText("Test Message - timestamp: " +  date);

			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}

	}

	public static void main(String[] args) throws Exception {

		// Connect to the Database
		dbConnect();

		User loggedOn = null;

		String username = "iseeyou";

		try {
			loggedOn = getUser(username);
			loggedOn.printUser();
		} catch (NullPointerException e) {
			System.out.println("User Not Found");
		}

		registerUser(loggedOn);
	}

}
