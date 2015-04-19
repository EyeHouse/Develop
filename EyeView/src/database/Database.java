/*
 * 
 * 
 */
package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
//import com.mysql.jdbc.PreparedStatement;
import java.sql.PreparedStatement;
import java.util.*;

import com.mysql.jdbc.Blob;

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
	private final static int profileIMG = 10;
	private final static int properties = 11;

	private final static int UID = 1;
	private final static int TITLE = 3;
	private final static int POSTCODE = 4;
	private final static int ADDRESS = 5;
	private final static int PRICE = 6;
	private final static int DEPOSIT = 7;
	private final static int ROOMS = 8;
	private final static int BATHROOMS = 9;
	private final static int DATEAVAILABLE = 10;
	private final static int FURNISHED = 11;
	private final static int BROCHURE = 12;
	private final static int DESCRIPTION = 13;
	private final static int ENERGYRATING = 14;

	public static String url = "10.10.0.1";

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
		// Create a connection with db:master_db user:root pw:
		String url = "127.0.0.1";
		try {
			System.out.print("Establishing connection via PuTTY... ");
			con = DriverManager.getConnection("jdbc:mysql://" + url
					+ ":3306/eyehouse", "eyehouseuser", "Toothbrush50");
			System.out.print("Success");
		} catch (SQLException ex1) {
			System.out.print("Fail\nEstablishing connection via OpenVPN... ");
			url = "10.10.0.1";
			try {
				con = DriverManager.getConnection("jdbc:mysql://" + url
						+ ":3306/eyehouse", "eyehouseuser", "Toothbrush50");
				System.out.print("Success");
			} catch (SQLException ex) {
				ex.printStackTrace();
				// handle any errors
				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());
			}
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
	public static boolean userInsert(User userDetails) {
		// query
		try {
			FileInputStream fis = null;
			PreparedStatement insertUser = con
					.prepareStatement("INSERT INTO users "
							+ "VALUES (?,?,?,?,?,?,?,?,?,?,?)");
			File file = new File("D:/EE course/SWEng/Java/Disco1.jpg");
			// InputStream is = null;
			// try {
			// is = new URL("sftp://eyehouseuser:Toothbrush50@" + url
			// + ":8080/eyehouse/defaults/default_profpic.jpg")
			// .openConnection().getInputStream();
			// } catch (MalformedURLException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// } catch (IOException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			try {
				fis = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			try {
				insertUser.setBinaryStream(profileIMG, fis, fis.available());
				// insertUser.setBinaryStream(profileIMG, is, is.available());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			insertUser.setString(properties, null);
			// execute the query
			insertUser.executeUpdate();
		} catch (SQLException e) {
			// catch the error get the message
			e.printStackTrace();
			e.getMessage();
		}
		return true;
	}

	public static int getID(User userDetails, House houseDetails, int idType) {
		// IMPORTANT: idType key:
		// 1 = User id from table users - found by username
		// 2 = hid from table houses - found by the user logged in and the
		// postcode
		ResultSet id = null;
		// select that user
		int idnumber = 0;
		try {
			if (idType == 1 && userDetails != null) {
				PreparedStatement userID = con
						.prepareStatement("SELECT id FROM users WHERE username=?");
				userID.setString(1, userDetails.username);
				id = userID.executeQuery();
				while (id.next()) {
					idnumber = id.getInt("id");
				}
			}
			if (idType == 2 && userDetails != null) {
				PreparedStatement houseID = con
						.prepareStatement("SELECT hid FROM houses WHERE uid=? AND postcode=?");
				int uid = getID(userDetails, null, 1);
				houseID.setInt(1, uid);
				houseID.setString(2, houseDetails.postcode);
				id = houseID.executeQuery();
				while (id.next()) {
					idnumber = id.getInt("hid");
				}
			}

			// take all the users details and put them in an instance of user

		} catch (SQLException e) {
			e.printStackTrace();
			e.getMessage();
		}
		return idnumber;
	}

	public static boolean checkHouseExists(User userDetails, House houseDetails) {
		int id = getID(userDetails, null, 1);
		ResultSet title;
		try {
			PreparedStatement checkTitle = con
					.prepareStatement("SELECT title FROM houses WHERE uid=?");
			checkTitle.setInt(1, id);
			title = checkTitle.executeQuery();
			if (!title.next()) {
				System.out.println("\nHouse doesn't Exist for this user");
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		System.out
				.println("\nHouse with same title already exists for this user");
		return true;
	}

	public static boolean houseInsert(House houseDetails, String brochurefp,
			String energyratingfp, User userDetails) throws IOException {
		int hid = 2;
		int id;
		FileInputStream bis = null, eis = null;
		// get user ID
		id = getID(userDetails, null, 1);
		try {
			// if the user doesn't have a house under the same name already
			PreparedStatement insertHouse = con
					.prepareStatement("INSERT INTO houses "
							+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			if (brochurefp != null) {
				File brochure = new File(brochurefp);
				try {
					bis = new FileInputStream(brochure);
					insertHouse.setBinaryStream(BROCHURE, bis, bis.available());
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else
				insertHouse.setBinaryStream(BROCHURE, null);

			if (energyratingfp != null) {
				File energyRatings = new File(energyratingfp);
				try {
					eis = new FileInputStream(energyRatings);
					insertHouse.setBinaryStream(ENERGYRATING, eis,
							eis.available());
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else
				insertHouse.setBinaryStream(ENERGYRATING, null);
			// insert the house data
			insertHouse.setInt(hid, 0);
			insertHouse.setInt(UID, id);
			insertHouse.setString(TITLE, houseDetails.title);
			insertHouse.setString(POSTCODE, houseDetails.postcode);
			insertHouse.setString(ADDRESS, houseDetails.address);
			insertHouse.setInt(PRICE, houseDetails.price);
			insertHouse.setInt(DEPOSIT, houseDetails.deposit);
			insertHouse.setInt(ROOMS, houseDetails.rooms);
			insertHouse.setInt(BATHROOMS, houseDetails.bathrooms);
			insertHouse.setString(DATEAVAILABLE, houseDetails.dateAvailable);
			insertHouse.setBoolean(FURNISHED, houseDetails.furnished);
			insertHouse.setString(DESCRIPTION, houseDetails.description);
			// execute the query
			insertHouse.executeUpdate();
		} catch (SQLException e) {
			// catch the error get the message
			e.printStackTrace();
			e.getMessage();
		}
		return true;
	}

	public static boolean updateHouse(User userDetails, House houseDetails,
			String field, String value1, Boolean value2, Blob value3,
			int value4, int varType) {
		int hid;
		int uid;
		try {
			hid = getID(userDetails, houseDetails, 2);
			uid = getID(userDetails, null, 1);
			PreparedStatement updateHouse = con
					.prepareStatement("UPDATE houses SET " + field
							+ "=? WHERE uid=? AND hid=?");
			// IMPORTANT: filetype int specifies which variable to use 
			// 1 = string, 2 = bool, 3 = blob, 4 = int
			switch (varType) {
			case 1:
				updateHouse.setString(1,value1);
				break;
			case 2:
				updateHouse.setBoolean(1,value2);
				break;
			case 3:
				updateHouse.setBlob(1,value3);
				break;
			case 4:
				updateHouse.setInt(1,value4);
				break;
			default:
				break;
			}
			updateHouse.setInt(2,uid);
			updateHouse.setInt(3,hid);
			updateHouse.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Someone done messed up");
			return false;
		}
		return true;
	}

	public static boolean houseDelete(House houseDetails, User userDetails) {
		int uid;
		int hid;
		try {
			// Takes a unique field (username) and deletes the users details
			PreparedStatement dropHouse = con
					.prepareStatement("DELETE FROM houses WHERE uid=? AND hid=?");
			// get user ID
			uid = getID(userDetails, null, 1);
			// get hid
			hid = getID(userDetails, houseDetails, 2);
			dropHouse.setInt(1, uid);
			dropHouse.setInt(2, hid);
			// Execute SQL drop statement
			dropHouse.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			e.getMessage();
			return false;
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
	public static boolean userUpdate(User user, String fieldSelect, Boolean priv,
			String newField) {
		// prepare a statement to update a field 'field'
		try {
			PreparedStatement editUser = con
					.prepareStatement("UPDATE users SET " + fieldSelect
							+ "=? WHERE username=? AND email=?");
			// if there's a string use string data else it must be a bool
			if (newField != null)
				editUser.setString(1, newField);
			else
				editUser.setBoolean(1, priv);
			editUser.setString(2, user.username);
			editUser.setString(3, user.email);
			// execute query
			editUser.executeUpdate();
		} catch (SQLException e) {
			e.getMessage();
			e.getErrorCode();
			e.printStackTrace();
			return false;
		}
		// updated a string field
		return true;
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

	public static House getHouse(User userDetails, String title) {
		House house = null;
		ResultSet houseDetails = null;
		// select that user
		int uid;
		try {
			// get uid
			uid = getID(userDetails, null, 1);
			// select all the columns of house
			PreparedStatement getHouse = con
					.prepareStatement("SELECT * FROM houses WHERE uid=? AND title=?");
			// parameterise inputs
			getHouse.setInt(1, uid);
			getHouse.setString(2, title);
			// execute
			houseDetails = getHouse.executeQuery();
			// take all the users details and put them in an instance of user
			if (houseDetails.next()) {
				// construct an instance using the logged on users details
				house = new House(houseDetails);
			} else {
				System.out.println("\nUser has no houses with this title");
				house = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			e.getMessage();
		}
		if (house == null)
			throw new NullPointerException();
		else
			return house;
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
	public static boolean userDelete(String username) {
		try {
			// Takes a unique field (username) and deletes the users details
			PreparedStatement dropUser = con
					.prepareStatement("DELETE FROM users WHERE username=?");
			// Parameterise inputs
			dropUser.setString(1, username);
			// Execute SQL drop statement
			dropUser.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			e.getMessage();
			return false;
		}
	}

	/**
	 * Lets you check for a user that has a X of Y AND a Z of W where X and Z
	 * can be any field you want eg(username, password or DOB, email) and Y and
	 * W are the specific data you are checking exists eg for username, password
	 * you might put (Eyehouse1,Password1)
	 * 
	 * @param Field1
	 * @param Data1
	 * @param Field2
	 * @param Data2
	 * @return
	 */
	public static boolean twoFieldCheck(String Field1, String Data1,
			String Field2, String Data2) {

		ResultSet result = null;
		// check database to see if EITHER of the fields exist
		try {
			PreparedStatement checkExists = con
					.prepareStatement("SELECT * FROM users WHERE " + Field1
							+ "=? OR " + Field2 + "=?");
			// parameterise queries
			checkExists.setString(1, Data1);
			checkExists.setString(2, Data2);

			result = checkExists.executeQuery();
			// loop through every row until
			if (result.next()) {
				// if the user exists there will only be one instance so the
				// next result
				// will trigger returning true
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			e.getMessage();
			return false;
		}
		// the user doesnt exist
		return false;
	}

	public static boolean login(String username, String password) {
		ResultSet result = null;
		// check to see if Username and password exist in db

		try {
			PreparedStatement checkExists = con
					.prepareStatement("SELECT * FROM users WHERE "
							+ usernameField + "=? AND " + passwordField + "=?");
			// parameterise queries
			checkExists.setString(1, username);
			checkExists.setString(2, password);

			result = checkExists.executeQuery();
			// loop through every row until
			if (result.next()) {
				// if the user exists there will only be one instance so the
				// next result
				// will trigger returning true
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			e.getMessage();
			return false;
		}
		// the user doesnt exist
		return false;
	}

	/**
	 * Checks the database for a single piece of data existing
	 * 
	 * @param Field1
	 *            eg(username)
	 * @param Data1
	 *            eg(Eyehouse1)
	 * @return
	 */
	public static boolean oneFieldCheck(String Field1, String Data1) {

		ResultSet result = null;
		// check database to see if username password exists
		try {
			PreparedStatement checkExists = con
					.prepareStatement("SELECT * FROM users WHERE " + Field1
							+ "=?");
			// parameterise queries
			checkExists.setString(1, Data1);

			result = checkExists.executeQuery();
			// loop through every row until
			if (result.next()) {
				// if the user exists there will only be one instance so the
				// next result
				// will trigger returning true
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			e.getMessage();
			return false;
		}
		// the user doesnt exist
		return false;
	}

	/**
	 * Takes the information required to create a new user checks they don't
	 * already exist and enters them into the database.
	 * 
	 * @param newUser
	 * @return 1 success
	 * @return 0 failure
	 */
	public static boolean userRegister(User newUser) {
		// boolean success = true / failure = false
		boolean insertCheck = false;
		// Check user email && username aren't already in database
		try {
			insertCheck = userInsert(newUser);
		} catch (Exception e) {
			e.printStackTrace();
			if (insertCheck == false) {
				System.out.println("Failure to insert. Please Try Again.");
				return false;
			}
		}
		return true;
	}

	public static boolean updateImage(String tablename, String filepath,
			String fieldSelect, int id) throws FileNotFoundException {

		try {

			FileInputStream fis = null;
			PreparedStatement updateImage = con.prepareStatement("UPDATE "
					+ tablename + " SET " + fieldSelect + "=? WHERE id=?");
			File file = new File(filepath);
			fis = new FileInputStream(file);

			updateImage.setBinaryStream(1, fis, (int) file.length());
			updateImage.setInt(2, id);
			updateImage.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("\nImage update executed");
		return true;
	}

	public static void main(String[] args) throws Exception {
		// Connect to the Database
		dbConnect();

		boolean check;
		User insert = null;
		User update = null;
		User get = null;
		// User checkUse = null
		String username = "Henry";
		String password = "PumpkinBoy";
		String email = "hcw515@york.ac.uk";

		String title = "Spatious Living accomodation with a Bath and eveything";

		int mode = 14;
		boolean insertSuccess;
		boolean deleteSuccess;
		boolean houseDeleted;
		boolean updateSuccess;
		

		// testing switch
		switch (mode) {
		case 1: // userCheck
			try {
				check = twoFieldCheck(usernameField, username, emailField,
						email);
				if (check == true) {
					System.out.println("User Exists");
				} else

					System.out.println("User Does not exist");
			} catch (Exception e) {
				System.out
						.println("User does not exist: Check details and try again");
			}
			break;
		case 2: // insert User
			// user to be inserted
			insert = new User(username);
			insert.firstName("Henry");
			insert.secondName("Waddlesworth");
			insert.email(email);
			insert.admin(true);
			insert.landlord(true);
			insert.DOB("0000-01-01");
			String encryptedPassword = DataHandler.crypt(password);
			insert.password(encryptedPassword);
			// insert
			insertSuccess = userInsert(insert);
			if (insertSuccess == false) {
				System.out.println("Failure to insert user");
			} else
				System.out.println("User inserted into database");
			break;
		case 3: // delete user
			deleteSuccess = userDelete(username);
			if (deleteSuccess) {
				// user deleted
				System.out.println("User deleted");
			} else
				System.out.println("Deletion failed: check user details exist");
			break;
		case 4: // edit details
			// user to be inserted
			update = new User(username);
			update.firstName("New");
			update.secondName("User");
			update.email(email);
			update.admin(false);
			update.landlord(false);
			update.DOB("2000-02-20");
			update.password(password);
			updateSuccess = userUpdate(update, "username", null, username);
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
			// use a runtime to send one via a ssh session
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
		case 8:
			boolean oneCheck;
			oneCheck = oneFieldCheck("username", username);
			System.out.println("User: " + username + " Exists: " + oneCheck);
			break;
		case 9:
			boolean loginCheck;
			try {
				loginCheck = login(username, password);

				if (loginCheck == true) {
					System.out.println("User Exists, log them in");
				} else {
					System.out.println("Login Failed, user does not exist");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 10:
			// String tablename, String filepath, String fieldSelect, String id
			String tablename = "users";
			String filepath = "D:/EE course/SWEng/Java/Compilation1.jpg";
			String fieldSelect = "profileIMG";
			int id = 3104;
			updateImage(tablename, filepath, fieldSelect, id);
			break;
		case 11:
			// insert the houses basic info
			House eyehouseHQ = null;
			int pricepermonth = 1000;
			boolean house;
			String brc = "D:/EE course/SWEng/Java/testbrochure.pdf";
			String enrg = "D:/EE course/SWEng/Java/energy-rating-card.jpg";
			eyehouseHQ = new House(title);
			eyehouseHQ.postcode("YO10 5DD");
			eyehouseHQ.address("Exhibition center");
			eyehouseHQ.price(pricepermonth);
			eyehouseHQ.deposit(pricepermonth);
			eyehouseHQ.rooms(pricepermonth);
			eyehouseHQ.bathrooms(pricepermonth);
			eyehouseHQ.dateAvailable("2015-04-18");
			eyehouseHQ.furnished(true);
			eyehouseHQ
					.description("A fine fine building made of dreams and aspirations. As the spring rains fall, soaking in them, on the roof, is a child's rag ball.");
			User temp = getUser("MVPTom");
			if (!checkHouseExists(temp, eyehouseHQ)) {
				house = houseInsert(eyehouseHQ, brc, enrg, temp);
				eyehouseHQ.printHouse();
				System.out.println(house);
			}
			break;
		case 12:
			// a lot of cases!
			// get House id method and get house as well
			User tempu = getUser("MVPTom");
			// gets house and puts it into memory
			House temph = getHouse(tempu, title);
			int uid = getID(tempu, null, 1);
			int hid = getID(tempu, temph, 2);
			System.out.println("User ID: " + uid + "\nHouse ID: " + hid);
			break;
		case 13:
			User tempu2 = getUser("MVPTom");
			House temph2 = getHouse(tempu2, title);
			try {
				houseDeleted = houseDelete(temph2, tempu2);
				if (!houseDeleted)
					System.out.println("House not Deleted");
				else
					System.out.println("\nHouse succesfully deleted");
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("House not Deleted");
			}
			break;
		case 14: 
			int tempPrc = 9001;
			int varType = 4;
			User tempu3 = getUser("MVPTom");
			House temph3 = getHouse(tempu3, title);
			check = updateHouse(tempu3, temph3,"price",null, null, null,
					tempPrc, varType);
			if(check == true) System.out.println("\nUpdate Successful");
			else System.out.println("\nFailure");
			break;
		default: // no mode selected
			System.out.println("Select a valid switch case mode");
			break;
		}

	}
}