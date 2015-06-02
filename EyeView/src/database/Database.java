package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.*;

import com.mysql.jdbc.Blob;

/**
 * 
 * Database.java has methods to set up a connection with the database and 
 * handle SQL queries.
 * 
 * @version 1.48 (15.03.15)
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 * 
 *         Copyright 2015 EyeHouse
 */
public class Database {

	// Connection initialised
	public static Connection con = null;
	/*
	 * Define the column numbers of the db table as integer variables 'id' auto
	 * increments due to the table set up, enter any integer when required
	 */
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
	private final static int skype = 12;
	private final static int bio = 13;

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

	/**
	 * A void function used to open the database connection.
	 */
	public static boolean dbConnect() {
		// Create a connection
		url = "127.0.0.1";

		try {

			System.out.print("Establishing connection via PuTTY... ");

			// Uses get connection to open a connection stored in con.
			con = DriverManager.getConnection("jdbc:mysql://" + url
					+ ":3306/eyehouse", "eyehouseuser", "Toothbrush50");
			System.out.print("Success");

			// Print local address we are using.
			System.out.println("\n" + url);

			return true;

		} catch (SQLException ex1) {

			System.out.print("Fail\nEstablishing connection via OpenVPN... ");

			url = "10.10.0.1";

			try {

				con = DriverManager.getConnection("jdbc:mysql://" + url
						+ ":3306/eyehouse", "eyehouseuser", "Toothbrush50");

				System.out.print("Success\n");

				// Print url
				System.out.println("\n" + url);

				return true;

			} catch (SQLException ex) {

				// handle any errors
				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());

				return false;
			}
		}
	}

	/**
	 * A basic function that take an instance of User.java, and enters it into
	 * the database you're connected to.
	 * 
	 * @param userDetails
	 * @return true on success
	 */
	public static boolean userInsert(User userDetails) {
		// query
		try {
			// Initialise a File
			File picture = null;

			// Prepare a MySQL statment to insert data into users table
			PreparedStatement insertUser = con
					.prepareStatement("INSERT INTO users "
							+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
			// Parameterise the inputs
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
				/*
				 * Try to read in the profile picture from the remote server
				 * into File picture.
				 */
				picture = FileManager
						.readFile("eyehouse/defaults/default_profpic.jpg");

				// Create a new input stream out of picture
				FileInputStream fis = new FileInputStream(picture);

				/*
				 * The profile image column in the database takes binary large
				 * objects (BLOB) type.
				 */
				insertUser.setBinaryStream(profileIMG, fis, fis.available());

			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("\nError user Insert: " + e.getMessage());
			}

			// More insert data
			insertUser.setString(properties, null);
			insertUser.setString(skype, userDetails.skype);
			insertUser.setString(bio, userDetails.bio);

			// Execute the query
			insertUser.executeUpdate();

			// Close prepared statement
			insertUser.close();

		} catch (SQLException e) {
			e.printStackTrace();
			e.getMessage();
		}
		return true;
	}

	/**
	 * Gets the id of a user or of a house/
	 * 
	 * @param userDetails
	 * @param houseDetails
	 * @param idType
	 * @return id
	 */
	public static int getID(User userDetails, House houseDetails, int idType) {
		/*
		 * IMPORTANT: idType key: 1 = User id from table users - found by user
		 * name. 2 = hid from table houses - found by the user logged in and the
		 * post code.
		 */
		ResultSet id = null;

		int idnumber = 0;

		try {
			// If looking for a user id
			if (idType == 1 && userDetails != null) {
				// Select the user.
				PreparedStatement userID = con
						.prepareStatement("SELECT id FROM users WHERE username=?");

				userID.setString(1, userDetails.username);
				// Execute query and enter it into result set.
				id = userID.executeQuery();

				// Get id number
				while (id.next()) {
					idnumber = id.getInt("id");
				}

				// Close query
				userID.close();
			}
			// If require house ID
			if (idType == 2 && userDetails != null) {
				// Select the house
				PreparedStatement houseID = con
						.prepareStatement("SELECT hid FROM houses WHERE uid=? AND postcode=?");

				// Use this method to get user ID
				int uid = getID(userDetails, null, 1);

				// Parameterise inputs
				houseID.setInt(1, uid);
				houseID.setString(2, houseDetails.postcode);

				id = houseID.executeQuery();
				// Get ID
				while (id.next()) {
					idnumber = id.getInt("hid");
				}

				// Close query
				houseID.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			e.getMessage();
		}

		return idnumber;
	}

	/**
	 * Checks a house exists in the database.
	 * 
	 * @param userDetails
	 * @param houseDetails
	 * @return true on success
	 */
	public static boolean checkHouseExists(User userDetails, House houseDetails) {
		ResultSet title;
		String titleStr;
		try {
			PreparedStatement checkTitle = con
					.prepareStatement("SELECT title FROM houses WHERE uid=?");
			checkTitle.setInt(1, userDetails.uid);
			title = checkTitle.executeQuery();
			while (title.next()) {
				titleStr = title.getString("title");
				if (!houseDetails.title.equals(titleStr)) {
					System.out.println("\nHouse doesn't Exist for this user");
					return false;
				} else {
					System.out
							.println("\nHouse with same title already exists for this user");
					return true;
				}
			}
			if (!title.next()) {
				System.out.println("\nHouse doesnt exist");
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("\nSQL error in house check");
			return false;
		}
		System.out.println("\nCase unspecified");

		return false;
	}

	/**
	 * 
	 * @param houseDetails
	 * @param brochurefp
	 * @param energyratingfp
	 * @param userDetails
	 * @return true on success
	 * @throws IOException
	 */
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

			// Handle the pdf
			if (brochurefp != null) {

				File brochure = new File(brochurefp);

				try {
					// Convert to input stream
					bis = new FileInputStream(brochure);
					// Insert BLOB into database
					insertHouse.setBinaryStream(BROCHURE, bis, bis.available());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			} else

				// If no PDF set to null
				insertHouse.setBinaryStream(BROCHURE, null);

			// Handle energy ratings image
			if (energyratingfp != null) {

				File energyRatings = new File(energyratingfp);

				try {
					// Convert to input stream
					eis = new FileInputStream(energyRatings);
					// Insert BLOB into database
					insertHouse.setBinaryStream(ENERGYRATING, eis,
							eis.available());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			} else
				// If no image set to null
				insertHouse.setBinaryStream(ENERGYRATING, null);

			// Insert the house data
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

			// Execute the query
			insertHouse.executeUpdate();

			// Close query
			insertHouse.close();

		} catch (SQLException e) {
			// catch the error get the message
			e.printStackTrace();
			e.getMessage();
		}

		return true;
	}

	/**
	 * 
	 * @param userDetails
	 * @param houseDetails
	 * @param field
	 * @param value1
	 * @param value2
	 * @param value3
	 * @param value4
	 * @param varType
	 * @return true on success
	 */
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
			/*
			 * IMPORTANT: int varType specifies which variable to use (1 =
			 * String) (2 = boolean) (3 = blob) (4 = integer)
			 */
			switch (varType) {
			case 1:
				updateHouse.setString(1, value1);
				break;
			case 2:
				updateHouse.setBoolean(1, value2);
				break;
			case 3:
				updateHouse.setBlob(1, value3);
				break;
			case 4:
				updateHouse.setInt(1, value4);
				break;
			default:
				break;
			}
			// Parameterise inputs
			updateHouse.setInt(2, uid);
			updateHouse.setInt(3, hid);

			// Execute query
			updateHouse.executeUpdate();

			// Close query
			updateHouse.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("\nError updateHouse: " + e.getMessage());

			return false;
		}

		return true;
	}

	/**
	 * Deletes the house and all information attributed to that house.
	 * 
	 * @param houseDetails
	 * @param userDetails
	 * @return true on success
	 */
	public static boolean houseDelete(House houseDetails, User userDetails) {
		int uid;
		int hid;
		int i;

		try {

			// ArrayList of markers for deletion and House Reviews
			ArrayList<Marker> deleteMarkers = new ArrayList<Marker>();
			ArrayList<HouseReview> deleteHouseReviews = new ArrayList<HouseReview>();

			// Check video exists delete videos of house and markers
			HouseVideo deleteVideo = checkHouseVideo(userDetails,
					houseDetails.hid);

			if (deleteVideo != null) {
				// Delete video
				deleteVideo(userDetails, deleteVideo);

				// Get video markers
				deleteMarkers = getVideoMarkers(houseDetails.hid);

				// remove the ArrayList from memory and delete from database
				// Delete markers
				for (i = 0; i < deleteMarkers.size(); i++) {
					// Delete from database
					deleteVideoMarker(deleteMarkers.get(i));

					// Remove info from memory
					deleteMarkers.remove(0);
				}
			}

			// Delete images associated with house
			System.out.println("\nDeleting Image data...");

			deleteAllHouseImage(houseDetails.hid);

			// Find and delete reviews
			deleteHouseReviews = getHouseReviews(houseDetails.hid);
			for (i = 0; i < deleteHouseReviews.size(); i++) {
				boolean delete = checkHouseReviewExists(deleteHouseReviews
						.get(i));
				if (delete) {
					// Delete house review
					deleteHouseReview(deleteHouseReviews.get(i));

					// Free object
					deleteHouseReviews.remove(0);
				}
			}

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

			// Suggest garbage collection
			System.gc();

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
	 * @return true on success
	 */
	public static boolean userUpdate(User user, String fieldSelect,
			Boolean priv, String newValue) {

		try {
			PreparedStatement editUser = con
					.prepareStatement("UPDATE users SET " + fieldSelect
							+ "=? WHERE username=? AND email=?");

			// If theres a String use string else use the boolean value.
			if (newValue != null || fieldSelect.equals("properties")
					|| fieldSelect.equals("bio") || fieldSelect.equals("skype"))

				// Parameterise inputs
				editUser.setString(1, newValue);
			else
				editUser.setBoolean(1, priv);
			editUser.setString(2, user.username);
			editUser.setString(3, user.email);

			// Execute query
			editUser.executeUpdate();

			// Close query
			editUser.close();

		} catch (SQLException e) {
			e.getMessage();
			e.getErrorCode();
			e.printStackTrace();

			return false;
		}

		return true;
	}

	/**
	 * To be used if the user has been confirmed to exist in the login stage if
	 * they do exist then the username and password is correct this gets all the
	 * users details and stores them in an object
	 * 
	 * This method is currently superfluous (while loginCheck does the same
	 * thing)
	 * 
	 * @return the users details in an instance of User
	 */
	public static User getUser(String username) {
		// Takes a unique field (username) and returns the user
		User user = null;
		ResultSet userDetails = null;

		try {
			// Prepare query
			PreparedStatement getUser = con
					.prepareStatement("SELECT * FROM users WHERE username=?");

			// Parameterise inputs
			getUser.setString(1, username);

			// Execute query
			userDetails = getUser.executeQuery();

			// Take all the users details and put them in an instance of user.
			while (userDetails.next()) {

				// Construct an instance using the logged on users details
				user = new User(userDetails);
			}

			// Close query
			getUser.close();

		} catch (SQLException e) {
			e.printStackTrace();
			e.getMessage();
		}
		return user;
	}

	/**
	 * To be used if the user has been confirmed to exist in the login stage if
	 * they do exist then the username and password is correct this gets all the
	 * users details and stores them in an object
	 * 
	 * This method is currently superfluous (while loginCheck does the same
	 * thing)
	 * 
	 * @param uid
	 * @return username
	 */
	public static String getUsername(int uid) {

		String username = null;
		ResultSet userDetails = null;

		try {
			// Prepare query
			PreparedStatement getUsername = con
					.prepareStatement("SELECT username FROM users WHERE id=?");

			// Parameterise inputs
			getUsername.setInt(1, uid);

			// Execute query
			userDetails = getUsername.executeQuery();

			// Take all the users details and put them in an instance of user
			if (userDetails.next()) {
				// Construct an instance using the logged on users details
				username = userDetails.getString("username");
			}

			// Close query
			getUsername.close();

			return username;
		} catch (SQLException e) {
			e.printStackTrace();
			e.getMessage();

			return username;
		}
	}

	/**
	 * Gets a house and puts it into a instance of House.
	 * 
	 * @param hid
	 * @return House
	 */
	public static House getHouse(int hid) {
		House house = null;
		ResultSet houseDetails = null;

		try {

			// Select the house
			PreparedStatement getHouse = con
					.prepareStatement("SELECT * FROM houses WHERE hid=?");

			// Parameterise inputs
			getHouse.setInt(1, hid);

			// Execute query
			houseDetails = getHouse.executeQuery();

			// If house exists use its data to create a new instance of house.
			if (houseDetails.next()) {
				house = new House(houseDetails);
			} else {
				System.out.println("\nUser has no houses with this title");
				house = null;
			}
			// Close query
			getHouse.close();

		} catch (SQLException e) {
			e.printStackTrace();
			e.getMessage();
		}

		return house;
	}

	/**
	 * Get the houses owned by a user.
	 * 
	 * @param uid
	 * @return ArrayList<House>
	 */
	public static ArrayList<House> getLandlordProperties(int uid) {

		ResultSet houses;
		ArrayList<House> list = new ArrayList<House>();

		try {
			// Prepare query
			PreparedStatement getHouses = con
					.prepareStatement("SELECT * FROM houses WHERE uid=?");

			// Parameterise inputs
			getHouses.setInt(1, uid);

			// Execute query
			houses = getHouses.executeQuery();

			// Add houses to the list
			while (houses.next()) {
				list.add(new House(houses));
			}

			// Close query
			getHouses.close();

		} catch (SQLException e) {
			e.printStackTrace();

			return null;
		}

		return list;
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
	 * account information and information belonging to them.
	 * 
	 * @param username
	 * @return true on success
	 */
	public static boolean userDelete(String username) {
		try {

			// Get user
			User deleteUser = getUser(username);

			// Getting the runtime reference from system
			Runtime runtime = Runtime.getRuntime();

			// Find all user houses and reviews and delete them
			ArrayList<House> deleteHouses = new ArrayList<House>();
			ArrayList<UserReview> deleteReviews = new ArrayList<UserReview>();

			// Get all houses uploaded by user
			deleteHouses = getLandlordProperties(deleteUser.uid);

			int i;
			for (i = 0; i < deleteHouses.size(); i++) {
				// Delete houses that belong to user
				houseDelete(deleteHouses.get(i), deleteUser);

				// Free house objects
				deleteHouses.remove(0);
			}

			// Find all user reviews associated with the user
			deleteReviews = getUserReviewList(deleteUser.uid);

			for (i = 0; i < deleteReviews.size(); i++) {
				// Delete user reviews
				deleteUserReview(deleteReviews.get(i));

				// Free review objects
				deleteReviews.remove(0);
			}

			// Free user object
			deleteUser = null;

			// Takes a unique field (username) and deletes the users details
			PreparedStatement dropUser = con
					.prepareStatement("DELETE FROM users WHERE username=?");
			// Parameterise inputs
			dropUser.setString(1, username);
			// Execute SQL drop statement
			dropUser.executeUpdate();

			System.out.println("\nMemory Before gc : " + runtime.totalMemory());

			// Suggest garbage collection
			System.gc();

			System.out.println("\nMemory After gc : " + runtime.totalMemory());

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
	 * @return true on success
	 */
	public static boolean twoFieldCheck(String Field1, String Data1,
			String Field2, String Data2) {

		ResultSet result = null;

		try {
			// Prepare query
			PreparedStatement checkExists = con
					.prepareStatement("SELECT * FROM users WHERE " + Field1
							+ "=? OR " + Field2 + "=?");

			// Parameterise queries
			checkExists.setString(1, Data1);
			checkExists.setString(2, Data2);

			// Execute query
			result = checkExists.executeQuery();

			if (result.next()) {
				// If exists return true
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			e.getMessage();

			return false;
		}

		return false;
	}
	
	/**
	 * Uses entered details to log a user in.
	 * @param username
	 * @param password
	 * @return true on success
	 */
	public static boolean login(String username, String password) {
		ResultSet result = null;

		try {
			// Prepare query
			PreparedStatement checkExists = con
					.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");

			// Parameterise queries
			checkExists.setString(1, username);
			checkExists.setString(2, password);

			// Execute query
			result = checkExists.executeQuery();

			// If exists return true
			if (result.next()) {
				return true;
			}
			
			// Close query
			checkExists.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			e.getMessage();
			return false;
		}
		return false;
	}

	/**
	 * Checks the database for a single piece of data existing
	 * 
	 * @param Field1
	 *            eg(username)
	 * @param Data1
	 *            eg(Eyehouse1)
	 * @return true on success
	 */
	public static boolean oneFieldCheck(String Field1, String Data1) {

		ResultSet result = null;

		try {
			// Prepare query
			PreparedStatement checkExists = con
					.prepareStatement("SELECT * FROM users WHERE " + Field1
							+ "=?");

			// Parameterise queries
			checkExists.setString(1, Data1);

			// Execute query
			result = checkExists.executeQuery();

			if (result.next()) {
				// If exists return true
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			e.getMessage();
			return false;
		}
		return false;
	}

	/**
	 * Takes the information required to create a new user checks they don't
	 * already exist and enters them into the database.
	 * 
	 * @param newUser
	 * @return true on success
	 */
	public static boolean userRegister(User newUser) {

		// Check user email && username aren't already in database
		if (!twoFieldCheck("username", newUser.username, "email", newUser.email)) {
			try {

				// Insert User
				userInsert(newUser);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		if (twoFieldCheck("username", newUser.username, "email", newUser.email)) {
			// User already exists
			System.out
					.println("\nAn account with the same username or Email already exists.\n");
		}
		return false;
	}

	/**
	 * 
	 * @param tablename
	 * @param filepath
	 * @param fieldSelect
	 * @param id
	 * @return true on success
	 * @throws FileNotFoundException
	 */
	public static boolean updateImage(String tablename, String filepath,
			String fieldSelect, int id) throws FileNotFoundException {

		try {
			// Initialise an input stream
			FileInputStream fis = null;

			// Prepare query
			PreparedStatement updateImage = con.prepareStatement("UPDATE "
					+ tablename + " SET " + fieldSelect + "=? WHERE id=?");

			// Put the file specified in parameters into a File
			File file = new File(filepath);

			// Create an input stream from File
			fis = new FileInputStream(file);

			// Parameterise inputs
			updateImage.setBinaryStream(1, fis, (int) file.length());
			updateImage.setInt(2, id);

			// Execute query
			updateImage.executeUpdate();

			// Close query
			updateImage.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("\nImage update executed");
		return true;
	}

	/**
	 * Inserts an image into the house_images table
	 * 
	 * @param localFilepath
	 * @param houseDetails
	 * @param userDetails
	 * @return true on success
	 * @throws SQLException
	 */
	public static boolean insertHouseImage(String localFilepath,
			House houseDetails, User userDetails) throws SQLException {
		PreparedStatement insertImage;

		try {
			// Prepare query
			insertImage = con
					.prepareStatement("INSERT INTO house_images VALUES (?,?,?)");

			// Parameterise inputs
			insertImage.setInt(1, 0);
			insertImage.setInt(2, houseDetails.hid);

			// Check image file exists
			File picture = new File(localFilepath);

			if (!picture.exists())
				throw new RuntimeException("Error. Local file not found");
			else {
				FileInputStream fis = new FileInputStream(picture);
				System.out.println(picture);
				insertImage.setBinaryStream(3, fis, fis.available());
			}
			// Execute query
			insertImage.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("\nThe Insert Query Failed");
			return false;
		}

		// Close query
		insertImage.close();
		return true;
	}

	/**
	 * Gets all house images associated with a house and returns them and their
	 * information in an ArrayList.
	 * 
	 * @param hid
	 * @return ArrayList<HouseImage>
	 */
	public static ArrayList<HouseImage> getHouseImageSet(int hid) {
		ResultSet images;
		ArrayList<HouseImage> list = new ArrayList<HouseImage>();

		try {
			// Prepare query
			PreparedStatement getImages = con
					.prepareStatement("SELECT * FROM house_images WHERE hid=?");

			// Parameterise inputs
			getImages.setInt(1, hid);

			// Execute query
			images = getImages.executeQuery();

			// Add houses to list
			while (images.next()) {
				list.add(new HouseImage(images));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}

	/**
	 * Check a video exists for a house.
	 * 
	 * @param userDetails
	 * @param videoDetails
	 * @return true if exists
	 */
	public static boolean checkHouseVideoExists(User userDetails,
			HouseVideo videoDetails) {

		ResultSet title;
		String videoStr;

		try {
			// Prepare query
			PreparedStatement checkTitle = con
					.prepareStatement("SELECT video_loc FROM house_videos WHERE vid=?");

			// Parametise inputs
			checkTitle.setInt(1, videoDetails.vid);

			// Execute query
			title = checkTitle.executeQuery();

			// Check record matches parameter
			while (title.next()) {

				videoStr = title.getString("video_loc");

				if (!videoDetails.videoLocation.equals(videoStr)) {
					System.out.println("\nVideo doesn't Exist for this user");
					return false;
				}
			}
			checkTitle.close();
			return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("\nSQL error in house check");
			return false;
		}
	}

	/**
	 * Tries to return a HouseVideo.
	 * 
	 * @param userDetails
	 * @param hid
	 * @return HouseVideo
	 */
	public static HouseVideo checkHouseVideo(User userDetails, int hid) {

		ResultSet videoExists = null;
		HouseVideo video = null;

		try {
			// Execute query
			PreparedStatement checkTitle = con
					.prepareStatement("SELECT * FROM house_videos WHERE hid=?");

			// Parameterise input
			checkTitle.setInt(1, hid);

			// Execute query
			videoExists = checkTitle.executeQuery();

			// If exists get the house video
			if (videoExists.next()) {
				video = new HouseVideo(videoExists);
			}

			// Close query
			checkTitle.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return video;
	}

	/**
	 * Deletes the database entry of an image when passed its HouseImage type
	 * 
	 * @param image
	 * @return true on success
	 * @return false on failure
	 */
	public static boolean deleteHouseImage(HouseImage image) {
		try {
			// Prepare query
			PreparedStatement dropUser = con
					.prepareStatement("DELETE FROM house_images WHERE iid=?");

			// Parameterise inputs
			dropUser.setInt(1, image.iid);

			// Execute query
			dropUser.executeUpdate();

			// Close query
			dropUser.close();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			e.getMessage();
			return false;
		}
	}

	/**
	 * Deletes all house images for a house.
	 * 
	 * @param hid
	 * @return true on success
	 */
	public static boolean deleteAllHouseImage(int hid) {
		try {
			// Prepare query
			PreparedStatement dropUser = con
					.prepareStatement("DELETE FROM house_images WHERE hid=?");

			// Parameterise inputs
			dropUser.setInt(1, hid);

			// Execute query
			dropUser.executeUpdate();

			// Close query
			dropUser.close();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			e.getMessage();
			return false;
		}
	}

	/**
	 * Checks the database for the automatic file path based on login details,
	 * house and filename. If auto generated location doesn't exist inserts
	 * location pointer into video_locs table and uploads the file to the
	 * server.
	 * 
	 * @param userDetails
	 * @param houseDetails
	 * @param filename
	 * @param localDirectory
	 * @return true on success
	 * @return false on failure
	 * 
	 */
	public static boolean insertHouseVideo(User userDetails,
			House houseDetails, String filename, String localDirectory) {

		ResultSet check = null;

		int hid = getID(userDetails, houseDetails, 2);

		String vidLocation = "/eyehouse/" + userDetails.username + "/" + hid
				+ "/" + filename;

		try {
			// Prepare query
			PreparedStatement checkExists = con
					.prepareStatement("SELECT * FROM house_videos WHERE video_loc=?");

			// Parameterise query
			checkExists.setString(1, vidLocation);

			// Execute query
			check = checkExists.executeQuery();

			if (check.next()) {
				// If the video location exists return false
				System.out
						.println("\nVideo path already exists.\nCheck if video has already been uploaded or change file name");
				checkExists.close();
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("\nSQL error. Check Syntax");
			return false;
		}

		try {
			// Prepare query
			PreparedStatement insertVideo = con
					.prepareStatement("INSERT INTO house_videos VALUES (?,?,?)");

			// Parameterise query
			insertVideo.setInt(1, 0);
			insertVideo.setInt(2, hid);
			insertVideo.setString(3, vidLocation);

			// Execute query
			insertVideo.executeUpdate();

			// Close query
			insertVideo.close();

		} catch (SQLException e) {

			e.printStackTrace();
			System.out.println("\nSQL error. Check Syntax");

			return false;
		}
		/*
		 * Initialise file manager and upload the file to the vidLocation NOTE:
		 * the location also includes a User-Password-ServerLocation element
		 * that needs to be constant.
		 */
		FileManager update = new FileManager();

		// Upload file to database
		update.uploadVideo(userDetails, houseDetails, localDirectory, filename);

		return true;
	}

	/**
	 * Get video info from the database.
	 * 
	 * @param userDetails
	 * @param houseDetails
	 * @return HouseVideo
	 */
	public static HouseVideo getVideoInfo(User userDetails, House houseDetails) {

		ResultSet videoRS;
		HouseVideo video = null;

		try {
			// Prepare query
			PreparedStatement checkExists = con
					.prepareStatement("SELECT * FROM house_videos WHERE hid=?");

			// Parameterise inputs
			checkExists.setInt(1, houseDetails.hid);

			// Execute query
			videoRS = checkExists.executeQuery();

			// If exists make a new HouseVideo
			if (videoRS.next()) {
				video = new HouseVideo(videoRS);
			}

			// Close query
			checkExists.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("\nQuery Failed");
			return null;
		}
		return video;
	}

	/**
	 * Delete a video.
	 * 
	 * @param userDetails
	 * @param videoDetails
	 * @return true on success
	 */
	public static boolean deleteVideo(User userDetails, HouseVideo videoDetails) {

		Boolean exists;
		ArrayList<Marker> deleteMarkers = new ArrayList<Marker>();

		// Check the video entered exists
		exists = checkHouseVideoExists(userDetails, videoDetails);

		if (!exists) {
			System.out.println("\nVideo does not exist");
			return false;
		} else {
			System.out.println("\n\n" + videoDetails.hid);

			// Delete the video
			try {

				// If the video location exists
				PreparedStatement dropVideo = con
						.prepareStatement("DELETE FROM house_videos WHERE hid=?");

				// Parameterise Inputs
				dropVideo.setInt(1, videoDetails.hid);

				System.out.println("\n\n" + videoDetails.hid);

				// Execute Update
				dropVideo.executeUpdate();

				// Close query
				dropVideo.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// Delete file on server
		FileManager.deleteVideo(videoDetails);

		// Get markers
		deleteMarkers = getVideoMarkers(videoDetails.hid);

		System.out.println(deleteMarkers);

		int i;
		for (i = 0; i < deleteMarkers.size(); i++) {
			// Delete markers
			System.out.println(deleteMarkers.get(i).vid);

			deleteVideoMarker(deleteMarkers.get(i));

			// Free marker objects
			deleteMarkers.remove(0);
		}

		// Suggest free memory
		System.gc();

		return true;
	}

	/**
	 * Check review exists in the database.
	 * 
	 * @param reviewDetails
	 * @return true on success
	 */
	public static boolean checkReviewExists(UserReview reviewDetails) {
		ResultSet userReviewSet;
		int review;

		try {
			// Prepare query
			PreparedStatement checkUserReview = con
					.prepareStatement("SELECT * FROM user_reviews WHERE urid=?");

			// Parameterise inputs
			checkUserReview.setInt(1, reviewDetails.urid);

			// Execute query
			userReviewSet = checkUserReview.executeQuery();

			while (userReviewSet.next()) {
				review = userReviewSet.getInt(id);

				// Check review exists
				if (review == reviewDetails.urid) {
					System.out.println("\nUser review exists");

					// Close query
					checkUserReview.close();
					return true;
				} else {

					// Close query
					checkUserReview.close();

					System.out.println("\nUser review does not exist");
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			e.getMessage();
			System.out.println("\nError checkReviewExists: " + e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Insert review for a user.
	 * 
	 * @param reviewDetails
	 * @return true on success
	 */
	public static boolean insertUserReview(UserReview reviewDetails) {

		try {
			// Prepare query
			PreparedStatement insertUserReview = con
					.prepareStatement("INSERT INTO user_reviews "
							+ "VALUES (?,?,?,?,?,?,?)");

			// Handle error
			if (reviewDetails.rating > 5) {
				System.out.println("\nRating is out of 5");
				return false;
			}

			// Parameterise query
			insertUserReview.setInt(id, 0);
			insertUserReview.setInt(2, reviewDetails.uid_target);
			insertUserReview.setInt(3, reviewDetails.uid_reviewer);
			insertUserReview.setString(4, reviewDetails.review);
			insertUserReview.setInt(5, reviewDetails.rating);
			insertUserReview.setInt(6, reviewDetails.like);
			insertUserReview.setInt(7, reviewDetails.dislike);

			// Execute query
			insertUserReview.executeUpdate();

			// Close query
			insertUserReview.close();

		} catch (SQLException e) {
			System.out.println("\nError insertUserReview: " + e.getMessage());
			e.printStackTrace();
			e.getMessage();
			return false;
		}
		System.out.println("\nSuccess! Inserted User Review");
		return true;
	}

	/**
	 * Get a user review and associated data.
	 * 
	 * @param urid
	 * @return
	 */
	public static UserReview getUserReview(int urid) {

		ResultSet userReview;
		UserReview userRev = null;

		try {
			// Prepare query
			PreparedStatement getUserReview = con
					.prepareStatement("SELECT * FROM user_reviews WHERE urid=?");

			// Parameterise query
			getUserReview.setInt(1, urid);

			// Execute query
			userReview = getUserReview.executeQuery();

			// Get review
			if (userReview.next()) {
				// Create UserReview
				userRev = new UserReview(userReview);

				// Close query
				getUserReview.close();

				return userRev;
			}
			// Close query
			getUserReview.close();

		} catch (SQLException e) {
			System.out.println("\nNo user review with that ID exists");
			e.printStackTrace();
			return null;
		}
		return userRev;
	}

	/**
	 * Get all user reviews that are targeted at a user (target) and return them
	 * in an ArrayList.
	 * 
	 * @param target
	 * @return ArrayList<UserReview>
	 */
	public static ArrayList<UserReview> getUserReviewList(int target) {

		ResultSet userReview;
		ArrayList<UserReview> list = new ArrayList<UserReview>();

		try {
			// Prepare query
			PreparedStatement getUserReview = con
					.prepareStatement("SELECT * FROM user_reviews WHERE uid_target=?");

			// Parameterise inputs
			getUserReview.setInt(1, target);

			// Execute query
			userReview = getUserReview.executeQuery();

			// Add reviews to list
			while (userReview.next()) {
				list.add(new UserReview(userReview));
			}

			// Close query
			getUserReview.close();

		} catch (SQLException e) {
			System.out.println("\nNo user review with that ID exists");
			e.printStackTrace();
			return null;
		}
		return list;
	}

	/**
	 * Delete user review.
	 * 
	 * @param reviewDetails
	 * @return true on success
	 */
	public static boolean deleteUserReview(UserReview reviewDetails) {

		Boolean exists;

		// Check the review exists
		exists = checkReviewExists(reviewDetails);

		if (!exists) {
			System.out.println("\nReview does not exist.");
			return false;
		} else {
			try {
				// Prepare query
				PreparedStatement dropReview = con
						.prepareStatement("DELETE FROM user_reviews WHERE urid=?");

				// Parameterise inputs
				dropReview.setInt(1, reviewDetails.urid);

				// Execute Update
				dropReview.executeUpdate();

				// Close query
				dropReview.close();

			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("\nSQL Error: deleteUserReview");
				return false;
			}
			return true;
		}
	}

	/**
	 * Inserts a house review into the database.
	 * 
	 * @param reviewDetails
	 * @return true on success
	 */
	public static boolean insertHouseReview(HouseReview reviewDetails) {

		try {
			// Prepare query
			PreparedStatement insertUserReview = con
					.prepareStatement("INSERT INTO house_reviews "
							+ "VALUES (?,?,?,?,?,?,?)");

			// Handle error
			if (reviewDetails.rating > 5) {
				System.out.println("\nRating is out of 5");
				return false;
			}

			// Parameterise query
			insertUserReview.setInt(id, 0);
			insertUserReview.setInt(2, reviewDetails.hid);
			insertUserReview.setInt(3, reviewDetails.uid);
			insertUserReview.setString(4, reviewDetails.review);
			insertUserReview.setInt(5, reviewDetails.rating);
			insertUserReview.setInt(6, reviewDetails.like);
			insertUserReview.setInt(7, reviewDetails.dislike);

			// Execute query
			insertUserReview.executeUpdate();

			// Close query
			insertUserReview.close();

		} catch (SQLException e) {
			System.out.println("\nError insertHouseReview: " + e.getMessage());
			e.printStackTrace();
			e.getMessage();
			return false;
		}
		System.out.println("\nSuccess! Inserted House Review");
		return true;
	}

	/**
	 * Get house reviews.
	 * 
	 * @param hid
	 * @return ArrayList<HouseReview>
	 */
	public static ArrayList<HouseReview> getHouseReviews(int hid) {

		ArrayList<HouseReview> list = new ArrayList<HouseReview>();
		ResultSet houseReview;

		try {
			// Prepare query
			PreparedStatement getUserReview = con
					.prepareStatement("SELECT * FROM house_reviews WHERE hid=?");

			// Parameterise query
			getUserReview.setInt(1, hid);

			// Execute query
			houseReview = getUserReview.executeQuery();

			// Add reviews to the list
			while (houseReview.next()) {
				list.add(new HouseReview(houseReview));
			}

			// Close query
			getUserReview.close();

		} catch (SQLException e) {
			System.out.println("\nNo house review with that ID exists");
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * Check a house review exists.
	 * 
	 * @param reviewDetails
	 * @return true if exists
	 */
	public static boolean checkHouseReviewExists(HouseReview reviewDetails) {
		ResultSet houseReviewSet;
		int review;

		try {
			// Prepare query
			PreparedStatement checkHouseReview = con
					.prepareStatement("SELECT * FROM house_reviews WHERE hrid=?");

			// Parameterise inputs
			checkHouseReview.setInt(1, reviewDetails.hrid);

			// Execute query
			houseReviewSet = checkHouseReview.executeQuery();

			while (houseReviewSet.next()) {
				review = houseReviewSet.getInt(id);

				// Check if review exists
				if (review == reviewDetails.hrid) {
					System.out.println("\nHouse review exists");

					// Close query
					checkHouseReview.close();
					return true;
				} else {
					System.out.println("\nHouse review does not exist");

					// Close query
					checkHouseReview.close();
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			e.getMessage();
			System.out.println("\nSQL error in house review check");
			return false;
		}
		return true;
	}

	/**
	 * Delete house review.
	 * 
	 * @param reviewDetails
	 * @return true on success
	 */
	public static boolean deleteHouseReview(HouseReview reviewDetails) {

		Boolean exists;

		// Check review exists
		exists = checkHouseReviewExists(reviewDetails);

		if (!exists) {
			System.out.println("\nReview does not exist.");
			return false;
		} else {
			try {
				// Prepare query
				PreparedStatement dropReview = con
						.prepareStatement("DELETE FROM house_reviews WHERE hrid=?");

				// Parameterise inputs
				dropReview.setInt(1, reviewDetails.hrid);

				// Execute query
				dropReview.executeUpdate();

				// Close query
				dropReview.close();

			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("\nSQL Error: deleteHouseReview");
				return false;
			}
			return true;
		}
	}

	/**
	 * Like a review. If the user has already liked the review delete the like
	 * record and switch status to no like or dislike. If the user has disliked
	 * the review switch the record to like. If the user has not liked or
	 * disliked the review insert a like record.
	 * 
	 * @param userDetails
	 * @param houseReview
	 * @param userReview
	 * @param type
	 * @return true on success
	 */
	public static boolean likeReview(User userDetails, HouseReview houseReview,
			UserReview userReview, int type) {

		// type 1 = User review
		// type 2 = House review
		ResultSet likeCheck;

		try {
			// Prepare query
			PreparedStatement checkLikes = con
					.prepareStatement("SELECT * FROM likes WHERE type=? AND uid=? AND rid=?");
			// Parameterise inputs
			checkLikes.setInt(1, type);
			checkLikes.setInt(2, userDetails.uid);
			if (type == 1)
				checkLikes.setInt(3, userReview.urid);
			if (type == 2)
				checkLikes.setInt(3, houseReview.hrid);

			// Execute query
			likeCheck = checkLikes.executeQuery();

			// Close query
			checkLikes.close();

			boolean likeStatus;

			if (likeCheck.next()) {
				// Record exists check like status
				likeStatus = likeCheck.getBoolean("liked");

				if (!likeStatus) {
					// If disliked
					try {
						// Prepare query
						PreparedStatement like = con
								.prepareStatement("UPDATE likes SET liked=? WHERE rid=? AND type=?");
						// Parameterise inputs
						like.setBoolean(1, true);
						if (type == 1)
							like.setInt(2, userReview.urid);
						if (type == 2)
							like.setInt(2, houseReview.hrid);
						like.setInt(3, type);

						// Execute query
						like.executeUpdate();

						// Close query
						like.close();

						try {
							// Update like count of user review
							if (type == 1) {
								// Prepare query
								PreparedStatement iterateUserLikes = con
										.prepareStatement("UPDATE user_reviews SET `like`=?, dislike=? WHERE urid=?");

								// Parameterise inputs
								iterateUserLikes.setInt(1, userReview.like + 1);
								iterateUserLikes.setInt(2,
										userReview.dislike - 1);
								iterateUserLikes.setInt(3, userReview.urid);
								// Execute query
								iterateUserLikes.executeUpdate();

								// Close query
								iterateUserLikes.close();
							}
							// Update like count for house review
							if (type == 2) {
								// Prepare query
								PreparedStatement iterateHouseLikes = con
										.prepareStatement("UPDATE house_reviews SET `like`=?, dislike=? WHERE hrid=?");

								// Parameterise inputs
								iterateHouseLikes.setInt(1,
										houseReview.like + 1);
								iterateHouseLikes.setInt(2,
										houseReview.dislike - 1);
								iterateHouseLikes.setInt(3, houseReview.hrid);

								// Execute query
								iterateHouseLikes.executeUpdate();

								// Close query
								iterateHouseLikes.close();
							}
						} catch (Exception e) {
							System.out
									.println("\nFailure to iterate like number");
							e.printStackTrace();
						}
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("\nFailure to change like status");
					}

				} else {
					// If already like delete record
					try {
						// Prepare query
						PreparedStatement dropLikeRec = con
								.prepareStatement("DELETE FROM likes WHERE type=? AND rid=?");

						// Parameterise inputs
						dropLikeRec.setInt(1, type);
						if (type == 1)
							dropLikeRec.setInt(2, userReview.urid);
						if (type == 2)
							dropLikeRec.setInt(2, houseReview.hrid);

						// Execute query
						dropLikeRec.executeUpdate();

						// Close query
						dropLikeRec.close();

						try {
							// Decrement the reviews like count
							if (type == 1) {
								// Prepare query
								PreparedStatement iterateUserLikes = con
										.prepareStatement("UPDATE user_reviews SET `like`=? WHERE urid=?");

								// Parameterise inputs
								iterateUserLikes.setInt(1, userReview.like - 1);
								iterateUserLikes.setInt(2, userReview.urid);

								// Execute query
								iterateUserLikes.executeUpdate();

								// Close query
								iterateUserLikes.close();
							}
							if (type == 2) {
								// Prepare query
								PreparedStatement iterateHouseLikes = con
										.prepareStatement("UPDATE house_reviews SET `like`=? WHERE hrid=?");

								// Parameterise inputs
								iterateHouseLikes.setInt(1,
										houseReview.like - 1);
								iterateHouseLikes.setInt(2, houseReview.hrid);

								// Execute query
								iterateHouseLikes.executeUpdate();

								// Close query
								iterateHouseLikes.close();
							}
						} catch (Exception e) {
							System.out
									.println("\nFailure to iterate like number");
							e.printStackTrace();
						}
					} catch (Exception e) {
						System.out.println("\nFailure to drop like record");
						e.printStackTrace();
					}
				}
			} else {
				/*
				 * If record doesn't exist create it and add a like to the
				 * review.
				 */
				try {
					// Prepare query
					PreparedStatement newRecord = con
							.prepareStatement("INSERT INTO likes VALUES (?,?,?,?)");

					// Parameterise inputs
					newRecord.setInt(1, userDetails.uid);
					newRecord.setInt(2, type);

					// ID of the review being liked
					if (type == 1)
						newRecord.setInt(3, userReview.urid);
					if (type == 2)
						newRecord.setInt(3, houseReview.hrid);
					newRecord.setBoolean(4, true);

					// Execute update
					newRecord.executeUpdate();

					// Close query
					newRecord.close();

				} catch (Exception e) {
					System.out.println("\nFailure to insert like record");
					e.printStackTrace();
				}
				try {
					// Iterate the reviews like count
					if (type == 1) {
						// Prepare query
						PreparedStatement iterateUserLikes = con
								.prepareStatement("UPDATE user_reviews SET `like`=? WHERE urid=?");

						// Parameterise inputs
						iterateUserLikes.setInt(1, userReview.like + 1);
						iterateUserLikes.setInt(2, userReview.urid);

						// Execute query
						iterateUserLikes.executeUpdate();

						// Close query
						iterateUserLikes.close();
					}
					if (type == 2) {
						// Prepare query
						PreparedStatement iterateHouseLikes = con
								.prepareStatement("UPDATE house_reviews SET `like`=? WHERE hrid=?");

						// Parameterise inputs
						iterateHouseLikes.setInt(1, houseReview.like + 1);
						iterateHouseLikes.setInt(2, houseReview.hrid);

						// Execute queries
						iterateHouseLikes.executeUpdate();

						// Close queries
						iterateHouseLikes.close();
					}
				} catch (Exception e) {
					System.out.println("\nFailure to iterate like number : 2");
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Dislike review. If review already disliked delete record. If liked switch
	 * to disliked. If no like or dislike create dislike record.
	 * 
	 * @param userDetails
	 * @param houseReview
	 * @param userReview
	 * @param type
	 * @return true on success
	 */
	public static boolean dislikeReview(User userDetails,
			HouseReview houseReview, UserReview userReview, int type) {

		// type == 1 for User review
		// type == 2 for House review
		ResultSet likeCheck;
		// Check User-Review like record exists
		try {
			// Prepare query
			PreparedStatement checkLikes = con
					.prepareStatement("SELECT * FROM likes WHERE type=? AND uid=? AND rid=?");

			// Parameterise inputs
			checkLikes.setInt(1, type);
			checkLikes.setInt(2, userDetails.uid);
			if (type == 1)
				checkLikes.setInt(3, userReview.urid);
			if (type == 2)
				checkLikes.setInt(3, houseReview.hrid);

			// Execute query
			likeCheck = checkLikes.executeQuery();

			// Close query
			checkLikes.close();

			boolean likeStatus;

			if (likeCheck.next()) {
				// Record already exists check if they liked or disliked
				likeStatus = likeCheck.getBoolean("liked");

				if (!likeStatus) {
					// If already disliked delete record and decrement dislikes
					try {
						// Prepare query
						PreparedStatement dropLikeRec = con
								.prepareStatement("DELETE FROM likes WHERE type=? AND rid=? AND uid=?");

						// Parameterise inputs
						dropLikeRec.setInt(1, type);
						if (type == 1)
							dropLikeRec.setInt(2, userReview.urid);
						if (type == 2)
							dropLikeRec.setInt(2, houseReview.hrid);
						dropLikeRec.setInt(3, userDetails.uid);

						// Execute update
						dropLikeRec.executeUpdate();

						// Close query
						dropLikeRec.close();

						try {
							// Decrement the reviews dislike count.
							if (type == 1) {
								// Prepare query
								PreparedStatement iterateUserLikes = con
										.prepareStatement("UPDATE user_reviews SET dislike=? WHERE urid=?");

								// Parameterise query
								iterateUserLikes.setInt(1,
										userReview.dislike - 1);
								iterateUserLikes.setInt(2, userReview.urid);

								// Execute query
								iterateUserLikes.executeUpdate();

								// Close query
								iterateUserLikes.close();
							}
							if (type == 2) {
								// Prepare query
								PreparedStatement iterateHouseLikes = con
										.prepareStatement("UPDATE house_reviews SET dislike=? WHERE hrid=?");

								// Parameterise query
								iterateHouseLikes.setInt(1,
										houseReview.dislike - 1);
								iterateHouseLikes.setInt(2, houseReview.hrid);

								// Execute update
								iterateHouseLikes.executeUpdate();

								// Close query
								iterateHouseLikes.close();
							}
						} catch (Exception e) {
							System.out
									.println("\nFailure to iterate dislike number");
							e.printStackTrace();
						}
					} catch (Exception e) {
						System.out.println("\nFailure to drop dislike record");
						e.printStackTrace();
					}

				} else {
					// If liked switch like status and ++ dis -- likes
					try {
						// Prepare query
						PreparedStatement like = con
								.prepareStatement("UPDATE likes SET liked=? WHERE rid=? AND type=? AND uid=?");

						// Parameterise query
						like.setBoolean(1, false);
						if (type == 1)
							like.setInt(2, userReview.urid);
						if (type == 2)
							like.setInt(2, houseReview.hrid);
						like.setInt(3, type);
						like.setInt(4, userDetails.uid);

						// Execute update
						like.executeUpdate();

						// Close query
						like.close();

						try {
							/*
							 * Iterate the reviews like count and decrement
							 * dislikes.
							 */
							if (type == 1) {
								// Prepare query
								PreparedStatement iterateUserLikes = con
										.prepareStatement("UPDATE user_reviews SET `like`=?, dislike=? WHERE urid=?");

								// Parameterise inputs
								iterateUserLikes.setInt(1, userReview.like - 1);
								iterateUserLikes.setInt(2,
										userReview.dislike + 1);
								iterateUserLikes.setInt(3, userReview.urid);

								// Execute query
								iterateUserLikes.executeUpdate();

								// Close query
								iterateUserLikes.close();
							}
							if (type == 2) {
								// Prepare query
								PreparedStatement iterateHouseLikes = con
										.prepareStatement("UPDATE house_reviews SET `like`=?, dislike=? WHERE hrid=?");

								// Parameterise inputs
								iterateHouseLikes.setInt(1,
										houseReview.like - 1);
								iterateHouseLikes.setInt(2,
										houseReview.dislike + 1);
								iterateHouseLikes.setInt(3, houseReview.hrid);

								// Execute update
								iterateHouseLikes.executeUpdate();

								// Close query
								iterateHouseLikes.close();
							}
						} catch (Exception e) {
							System.out
									.println("\nFailure to iterate dislike number");
							e.printStackTrace();
						}
					} catch (Exception e) {
						e.printStackTrace();
						System.out
								.println("\nFailure to change dislike status");
					}

				}
			} else {
				/*
				 * If record doesn't exist create it and add a dislike to the
				 * review
				 */
				try {
					// Prepare query
					PreparedStatement newRecord = con
							.prepareStatement("INSERT INTO likes VALUES (?,?,?,?)");
					// Parameterise inputs
					newRecord.setInt(1, userDetails.uid);
					newRecord.setInt(2, type);

					if (type == 1)
						newRecord.setInt(3, userReview.urid);
					if (type == 2)
						newRecord.setInt(3, houseReview.hrid);
					newRecord.setBoolean(4, false);

					// Execute update
					newRecord.executeUpdate();

					// Close query
					newRecord.close();

				} catch (Exception e) {
					System.out.println("\nFailure to insert dislike record");
					e.printStackTrace();
				}
				try {
					// Iterate the reviews dislike count
					if (type == 1) {
						// Prepare query
						PreparedStatement iterateUserLikes = con
								.prepareStatement("UPDATE user_reviews SET dislike=? WHERE urid=?");

						// Parameterise input
						iterateUserLikes.setInt(1, userReview.dislike + 1);
						iterateUserLikes.setInt(2, userReview.urid);

						// Execute update
						iterateUserLikes.executeUpdate();

						// Close query
						iterateUserLikes.close();
					}
					if (type == 2) {
						// Prepare query
						PreparedStatement iterateHouseLikes = con
								.prepareStatement("UPDATE house_reviews SET dislike=? WHERE hrid=?");

						// Parameterise inputs
						iterateHouseLikes.setInt(1, houseReview.dislike + 1);
						iterateHouseLikes.setInt(2, houseReview.hrid);

						// Execute query
						iterateHouseLikes.executeUpdate();

						// Close query
						iterateHouseLikes.close();
					}
				} catch (Exception e) {
					System.out
							.println("\nFailure to iterate dislike number : 2");
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Insert video marker.
	 * 
	 * @param vid
	 * @param room
	 * @param time
	 * @return true on success
	 */
	public static boolean insertVideoMarker(int vid, String room, double time) {

		ResultSet check = null;
		/*
		 * Check if video has marker with the same room name already room names
		 * for each video are unique within that video eg vid=3 cannot have two
		 * 'living room' markers
		 */
		try {
			// Prepare query
			PreparedStatement checkVideoMarkers = con
					.prepareStatement("SELECT * FROM markers WHERE vid=? AND room=?");

			// Parameterise inputs
			checkVideoMarkers.setInt(1, vid);
			checkVideoMarkers.setString(2, room);

			// Execute query
			check = checkVideoMarkers.executeQuery();

			// Close query
			checkVideoMarkers.close();

			if (check.next()) {
				System.out
						.println("\nThis room marker already exists.\nDelete or enter different room name.");
				return false;
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
			System.out.println("\nInsert Marker Error: " + e1.getErrorCode());
			return false;
		}

		try {
			// Prepare query
			PreparedStatement insertMarker = con
					.prepareStatement("INSERT INTO markers "
							+ "VALUES (?,?,?,?)");

			// Parameterise inputs
			insertMarker.setInt(1, 0);
			insertMarker.setInt(2, vid);
			insertMarker.setString(3, room);
			insertMarker.setDouble(4, time);

			// Execute update
			insertMarker.executeUpdate();

			// Close query
			insertMarker.close();

			System.out.println("\nMarker Inserted");

			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("\nInsert Marker Error: " + e.getErrorCode());
			return false;
		}
	}

	/**
	 * Delete video marker.
	 * 
	 * @param videoMarker
	 * @return
	 */
	public static boolean deleteVideoMarker(Marker videoMarker) {

		try {
			// Prepare query
			PreparedStatement dropMarker = con
					.prepareStatement("DELETE FROM markers WHERE vid=?");

			// Parameterise inputs
			dropMarker.setInt(1, videoMarker.vid);

			// Execute update
			dropMarker.executeUpdate();

			// Close query
			dropMarker.close();

			System.out.println("\nMarker Deleted");
			return true;
		} catch (SQLException e) {
			System.out.println("\nDelete Marker Error: " + e.getErrorCode());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Gets all video markers associated with vid and returns them in an
	 * ArrayList of type Marker
	 * 
	 * @param vid
	 * @return ArrayList<Marker>
	 */
	public static ArrayList<Marker> getVideoMarkers(int vid) {

		ResultSet markers;
		ArrayList<Marker> list = new ArrayList<Marker>();

		try {
			// Prepare query
			PreparedStatement getVideoMarkers = con
					.prepareStatement("SELECT * FROM markers WHERE vid=?");

			// Paramterise inputs
			getVideoMarkers.setInt(1, vid);

			// Execute query
			markers = getVideoMarkers.executeQuery();

			// Add markers to the list
			while (markers.next()) {
				list.add(new Marker(markers));
			}

			// Close query
			getVideoMarkers.close();

			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("\nGet Markers Error: " + e.getErrorCode());

			return list;
		}
	}

	/**
	 * Selects all houses.
	 * 
	 * @return ArrayList<Integer>
	 */
	public static ArrayList<Integer> selectAllHouses() {

		ArrayList<Integer> list = new ArrayList<Integer>();
		ResultSet houses = null;

		try {
			// Prepare query
			PreparedStatement allHouses = con
					.prepareStatement("SELECT * FROM houses");

			// Execute query
			houses = allHouses.executeQuery();

			// Add houses
			while (houses.next()) {
				list.add(houses.getInt("hid"));
			}

			// Close query
			allHouses.close();

		} catch (Exception e) {
			e.printStackTrace();
			list = null;
			System.out.println("\nSelect all houses failed.");
		}
		return list;
	}
}
