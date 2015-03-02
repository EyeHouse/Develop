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

//import javax.activation.*;

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
	// when using userCheck, don't allow the user to enter the string, display a
	// drop
	// down box or another selection method that calls your own string for the
	// field
	// required to check if you even give the user the option
	private final static String emailField = "email";
	private final static String usernameField = "username";
	private final static String passwordField = "password";

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
	 * @param userDetails
	 * @return 1 if user is inserted successfully
	 * @return 0 if failure to insert
	 */
	public static int userInsert(User userDetails) {
		// Check if unique details don't exist already (email, username)
		boolean blockInsert = false;
		// query
		try {
			PreparedStatement checkUser = con
					.prepareStatement("SELECT * FROM users WHERE username=? AND email=?");
			// parameterise queries
			checkUser.setString(1, userDetails.username);
			checkUser.setString(2, userDetails.email);
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
				insertUser.setString(firstName, userDetails.first_name);
				insertUser.setString(secondName, userDetails.second_name);
				insertUser.setString(userName, userDetails.username);
				insertUser.setString(email, userDetails.email);
				insertUser.setString(password, userDetails.password);
				insertUser.setBoolean(landlord, userDetails.landlord);
				insertUser.setString(DOB, userDetails.DOB);
				insertUser.setBoolean(admin, userDetails.admin);
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
	public static int userUpdate(User user, String fieldSelect, Boolean priv,
			String newField) {
		// prepare a statement to update a field 'field'
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
			return 0;
		}

		// updated a string field
		return 1;
	}

	/**
	 * To be used if the user has been confirmed to exist in the login stage if
	 * they do exist then the username and password is correct this gets all the
	 * users details and stores them in an object
	 * 
	 * This method is currently superfluous (while loginCheck does the same
	 * thing)
	 */
	public static User getUser(String username) throws NullPointerException {
		// Takes a unique field (username) and returns the user
		User user = null;
		ResultSet userDetails = null;
		// select that user
		try {
			PreparedStatement getUser = con
					.prepareStatement("SELECT * FROM users WHERE username=?");
			// parameterise inputs
			getUser.setString(1, username);
			// execute
			userDetails = getUser.executeQuery();
			// take all the users details and put them in an instance of user
			while (userDetails.next()) {
				// construct an instance using the logged on users details
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
	 * Empties the instance of user entered
	 * 
	 * @param user
	 */
	public static void logout(User user) {
		user = null;
	}

	/**
	 * Takes a unique detail from the user who is logged on and deletes their
	 * account information.
	 * 
	 * @param username
	 * @return 1 if success
	 * @return 0 if failure
	 */
	public static int userDelete(String username) {
		try {
			// Takes a unique field (username) and deletes the users details
			PreparedStatement dropUser = con
					.prepareStatement("DELETE FROM users WHERE username=?");
			// Parameterise inputs
			dropUser.setString(1, username);
			// Execute SQL drop statement
			dropUser.executeUpdate();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			e.getMessage();
			return 0;
		}
	}

	// Temp Commit notes: changed login check to a general two field check
	/**
	 * Checks a user with username and pw exists Can be used to log users in.
	 * 
	 * @param A
	 *            field from the database 'users'
	 * @param Data
	 *            relevant to that Field
	 * @param Another
	 *            field from the database 'users'
	 * @param Data
	 *            relevant to that Field
	 * @return user
	 */
	public static User userCheck(String Field1, String Data1, String Field2,
			String Data2) {

		User user = null;
		ResultSet result = null;
		// check database to see if username password exists
		try {

			PreparedStatement checkExists = con
					.prepareStatement("SELECT * FROM users WHERE " + Field1
							+ "=? AND " + Field2 + "=?");
			// parameterise queries
			checkExists.setString(1, Data1);
			checkExists.setString(2, Data2);
			// checkExists.setString(1, Field2);
			// checkExists.setString(1, Data2);
			result = checkExists.executeQuery();
			// loop through every row until
			while (result.next()) {
				// get id
				user = new User(result);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			e.getMessage();
		}
		return user;

	}
	/**
	 * Takes the information required to create a new user checks they don't
	 * already exist and enters them into the database.
	 * 
	 * Rules: Username is all lowercase + numbers Password contains at least 1
	 * capital, 1 number from 0-9, and one ! No other special characters Valid
	 * Emails only
	 * 
	 * 
	 * @param newUser
	 * @return 1 success
	 * @return 0 failure
	 */
	public static boolean userRegister(User newUser) {
		// bool success = true / failure = false
		int insertCheck = -1;
		// Filter attacks

		// Filter user input
		// if(user == invalid) {
		// return 1;
		// }

		// Check user email && username aren't already in database
		try {
			userCheck(usernameField, newUser.username, emailField,
					newUser.email);
		} catch (Exception e) {
			System.out.println("User Already Exists");
			e.printStackTrace();
			return false;
		}
		if (insertCheck != 0) {
			try {
				insertCheck = userInsert(newUser);
			} catch (Exception e) {
				e.printStackTrace();
				if (insertCheck == 0) {
					System.out.println("Failure to insert. Please Try Again.");
					return false;
				}
			}
		} else {
			// it worked!
			System.out.println("User Successfully inserted");
		}
		return true;

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
			message.setText("Test Message - timestamp: " + date);
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

		User check = null;
		User insert = null;
		User update = null;
		User get = null;

		String username = "uniqueusername";
		String password = "nohash";
		String email = "unique1@email.com";

		int mode = 7;
		int insertSuccess = 0;
		int deleteSuccess = 0;
		int updateSuccess = 0;

		// testing switch
		switch (mode) {
		case 1: // userCheck
			try {
				check = userCheck(usernameField, username, emailField, email);
				check.printUser();
			} catch (Exception e) {
				System.out
						.println("User does not exist: Check details and try again");
			}
			break;
		case 2: // insert User
			// user to be inserted
			insert = new User(username);
			insert.firstName("Tom");
			insert.secondName("Time");
			insert.email(email);
			insert.admin(true);
			insert.landlord(true);
			insert.DOB("1993-10-31");
			insert.password(password);
			// insert
			insertSuccess = userInsert(insert);
			if (insertSuccess == 0) {
				System.out.println("Failure to insert user");
			} else
				System.out.println("User inserted into database");
			break;
		case 3: // delete user
			deleteSuccess = userDelete(username);
			if (deleteSuccess == 1) {
				// user deleted
				System.out.println("User deleted");
			}
			else System.out.println("Deletion failed: check user details exist");
			break;
		case 4: // edit details
			updateSuccess = userUpdate(update, "password", null, password);
			System.out.println("User update method returns: " + updateSuccess);
			break;
		case 5: // try to get user into an object
			try {
				get = getUser(username);
				get.printUser();
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("User not found");
			}
			break;
		case 6: // send an email
			sendMail();
			break;
		case 7: // register
			boolean regCheck;
			User reg = null;
			// user to be inserted
			reg = new User(username);
			reg.firstName("New");
			reg.secondName("User");
			reg.email(email);
			reg.admin(false);
			reg.landlord(false);
			reg.DOB("2000-02-20");
			reg.password(password);
			// reg new user
			regCheck = userRegister(reg);
			if (regCheck == false) {
				// open a error box and make user retry registration
			} else {
				// carry on to whatever interface you want, maybe login
				System.out.println("User: " + reg.username
						+ " created successfully");
			}
			break;
		default: // no mode selected
			System.out.println("Select a valid switch case mode");
			break;

		}
	}
}
