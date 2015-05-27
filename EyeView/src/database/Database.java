/*
 * 
 * 
 */
package database;

import java.awt.BorderLayout;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
//import com.mysql.jdbc.PreparedStatement;
import java.sql.PreparedStatement;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.mysql.jdbc.Blob;

public class Database {

	// Public variables
	public static Connection con = null;
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
	 * A void function used to open the database connection
	 */
	public static String dbConnect() {
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
				System.out.print("Success\n");
			} catch (SQLException ex) {
				ex.printStackTrace();
				// handle any errors
				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());
			}
		}
		// Print url
		System.out.println("\n" + url);

		return url;
	}

	// different
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
			File picture = null;
			PreparedStatement insertUser = con
					.prepareStatement("INSERT INTO users "
							+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
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
				picture = FileManager
						.readFile("eyehouse/defaults/default_profpic.jpg");
				FileInputStream fis = new FileInputStream(picture);
				insertUser.setBinaryStream(profileIMG, fis, fis.available());
				// insertUser.setBinaryStream(profileIMG, is, is.available());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("\nTheres been an sftp error");
			}
			insertUser.setString(properties, null);
			insertUser.setString(skype, userDetails.skype);
			insertUser.setString(bio, userDetails.bio);
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
	 * @param userDetails
	 * @param videoDetails
	 * @return true if exists
	 * @return false if does not exist
	 */
	public static boolean checkHouseVideoExists(User userDetails,
			HouseVideo videoDetails) {
		int id = getID(userDetails, null, 1);
		ResultSet title;
		String videoStr;
		try {
			PreparedStatement checkTitle = con
					.prepareStatement("SELECT video_loc FROM house_videos WHERE vid=?");
			checkTitle.setInt(1, id);
			title = checkTitle.executeQuery();
			while (title.next()) {
				videoStr = title.getString("video_loc");
				if (!videoDetails.videoLocation.equals(videoStr)) {
					System.out.println("\nVideo doesn't Exist for this user");
					return false;
				} else {
					System.out
							.println("\nVideo with same title already exists for this user");
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("\nSQL error in house check");
			return false;
		}
		System.out
				.println("\nVideo with same location already exists for this user");
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
			updateHouse.setInt(2, uid);
			updateHouse.setInt(3, hid);
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
	public static boolean userUpdate(User user, String fieldSelect,
			Boolean priv, String newField) {
		// prepare a statement to update a field 'field'
		try {
			PreparedStatement editUser = con
					.prepareStatement("UPDATE users SET " + fieldSelect
							+ "=? WHERE username=? AND email=?");
			// if there's a string use string data else it must be a bool
			if (newField != null || fieldSelect.equals("properties"))
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

	/**
	 * To be used if the user has been confirmed to exist in the login stage if
	 * they do exist then the username and password is correct this gets all the
	 * users details and stores them in an object
	 * 
	 * This method is currently superfluous (while loginCheck does the same
	 * thing)
	 */
	public static String getUsername(int uid) throws NullPointerException {
		// Takes a unique field (username) and returns the user
		String username = null;
		ResultSet userDetails = null;
		// select that user
		try {
			PreparedStatement getUsername = con
					.prepareStatement("SELECT username FROM users WHERE id=?");
			// parameterise inputs
			getUsername.setInt(1, uid);
			// execute
			userDetails = getUsername.executeQuery();
			// take all the users details and put them in an instance of user
			if (userDetails.next()) {
				// construct an instance using the logged on users details
				username = userDetails.getString("username");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			e.getMessage();
		}
		if (username == null)
			throw new NullPointerException();
		else
			return username;
	}

	public static House getHouse(int hid) {
		House house = null;
		ResultSet houseDetails = null;
		// select that user
		try {

			// select all the columns of house
			PreparedStatement getHouse = con
					.prepareStatement("SELECT * FROM houses WHERE hid=?");
			// parameterise inputs
			getHouse.setInt(1, hid);
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

	public static ArrayList<House> getLandlordProperties(int uid) {
		ResultSet houses;
		ArrayList<House> list = new ArrayList<House>();
		try {
			PreparedStatement getHouses = con
					.prepareStatement("SELECT * FROM houses WHERE uid=?");
			getHouses.setInt(1, uid);
			houses = getHouses.executeQuery();

			while (houses.next()) {
				list.add(new House(houses));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
					.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
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

	/**
	 * Inserts an image into the house_images table
	 * 
	 * @param localFilepath
	 * @param houseDetails
	 * @param userDetails
	 * @return true on success
	 * @return false on failure
	 * @throws SQLException
	 */
	public static boolean insertHouseImage(String localFilepath,
			House houseDetails, User userDetails) throws SQLException {
		PreparedStatement insertImage;
		try {
			insertImage = con
					.prepareStatement("INSERT INTO house_images VALUES (?,?,?)");
			// enter the relevant house in parameters so we can use its unqiue
			// id
			insertImage.setInt(1, 0);
			insertImage.setInt(2, houseDetails.hid);
			// check on the image they want to insert
			File picture = new File(localFilepath);
			if (!picture.exists())
				throw new RuntimeException("Error. Local file not found");
			else {
				FileInputStream fis = new FileInputStream(picture);
				System.out.println(picture);
				insertImage.setBinaryStream(3, fis, fis.available());
			}
			insertImage.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("\nThe Insert Query Failed");
			return false;
		}
		return true;
	}

	public static ArrayList<HouseImage> getHouseImageSet(int hid) {
		ResultSet images;
		ArrayList<HouseImage> list = new ArrayList<HouseImage>();
		try {
			PreparedStatement getImages = con
					.prepareStatement("SELECT * FROM house_images WHERE hid=?");
			getImages.setInt(1, hid);
			images = getImages.executeQuery();

			while (images.next()) {
				list.add(new HouseImage(images));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return list;
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
			PreparedStatement dropUser = con
					.prepareStatement("DELETE FROM house_images WHERE iid=?");
			// Parameterise inputs
			dropUser.setInt(1, image.iid);
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
	 * Checks the database for the automatic filepath based on logon details,
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
			// Check if video path already exists in the database
			PreparedStatement checkExists = con
					.prepareStatement("SELECT * FROM house_videos WHERE video_loc=?");

			// Enter field data
			checkExists.setString(1, vidLocation);

			// Execute query
			check = checkExists.executeQuery();

			// Check ResultSet
			if (check.next()) {
				// if the video location exists
				System.out
						.println("\nVideo path already exists.\nCheck if video has already been uploaded or change file name");
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("\nSQL error. Check Syntax");
			return false;
		}

		try {
			// Insert video location into database
			PreparedStatement insertVideo = con
					.prepareStatement("INSERT INTO house_videos VALUES (?,?,?)");

			// Enter data fields
			insertVideo.setInt(1, 0);
			insertVideo.setInt(2, hid);
			insertVideo.setString(3, vidLocation);

			// Execute query
			insertVideo.executeUpdate();

		} catch (SQLException e) {

			e.printStackTrace();
			System.out.println("\nSQL error. Check Syntax");

			return false;
		}
		// Initialise file manager and upload the file to the vidLocation
		// NOTE: the location also includes a U-PW-SRVR element that needs
		// to be constant.
		FileManager update = new FileManager();
		update.uploadVideo(userDetails, houseDetails, localDirectory, filename);

		return true;
	}

	public static HouseVideo getVideoInfo(User userDetails, House houseDetails,
			String filename) {

		// Find video_loc field with the same filename + path
		int hid = getID(userDetails, houseDetails, 2);
		String vidLocation = "/eyehouse/" + userDetails.username + "/" + hid
				+ "/" + filename;
		ResultSet videoRS;
		HouseVideo video = null;

		try {
			// Check if video path already exists in the database
			PreparedStatement checkExists = con
					.prepareStatement("SELECT * FROM house_videos WHERE video_loc=?");

			// Enter field data
			checkExists.setString(1, vidLocation);

			// Execute query
			videoRS = checkExists.executeQuery();

			if (videoRS.next()) {
				video = new HouseVideo(videoRS);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("\nQuery Failed");
			return null;
		}
		return video;
	}

	public static boolean deleteVideo(User userDetails, HouseVideo videoDetails) {

		Boolean exists;

		exists = checkHouseVideoExists(userDetails, videoDetails);

		if (!exists) {
			System.out.println("\nVideo does not exist");
			return false;
		} else {

			// Delete the video
			try {
				// if the video location exists
				PreparedStatement dropVideo = con
						.prepareStatement("DELETE FROM house_videos WHERE hid=?");

				// Enter field data
				dropVideo.setInt(1, videoDetails.hid);

				System.out.println("\n\n" + videoDetails.hid);

				// Delete record of filepath
				dropVideo.executeUpdate();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Delete file on server
		FileManager.deleteVideo(videoDetails);

		return true;
	}

	public static boolean checkReviewExists(UserReview reviewDetails) {
		ResultSet userReviewSet;
		int review;
		try {
			PreparedStatement checkUserReview = con
					.prepareStatement("SELECT * FROM user_reviews WHERE urid=?");

			checkUserReview.setInt(1, reviewDetails.urid);

			userReviewSet = checkUserReview.executeQuery();

			while (userReviewSet.next()) {
				review = userReviewSet.getInt(id);
				if (review == reviewDetails.urid) {
					System.out.println("\nUser review exists");
					return true;
				} else {
					System.out.println("\nUser review does not exist");
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			e.getMessage();
			System.out.println("\nSQL error in user review check");
			return false;
		}
		return true;
	}

	public static boolean insertUserReview(UserReview reviewDetails) {

		try {
			PreparedStatement insertUserReview = con
					.prepareStatement("INSERT INTO user_reviews "
							+ "VALUES (?,?,?,?,?,?,?)");

			if (reviewDetails.rating > 5) {
				System.out.println("\nRating is out of 5");
				return false;
			}

			// Values to insert
			insertUserReview.setInt(id, 0);
			insertUserReview.setInt(2, reviewDetails.uid_target);
			insertUserReview.setInt(3, reviewDetails.uid_reviewer);
			insertUserReview.setString(4, reviewDetails.review);
			insertUserReview.setInt(5, reviewDetails.rating);
			insertUserReview.setInt(6, reviewDetails.like);
			insertUserReview.setInt(7, reviewDetails.dislike);

			// execute the query
			insertUserReview.executeUpdate();

		} catch (SQLException e) {
			System.out.println("\nSQL Error: in insertUserReview");
			e.printStackTrace();
			e.getMessage();
			return false;
		}
		System.out.println("\nSucces! Inserted User Review");
		return true;
	}

	public static UserReview getUserReview(int urid) {

		ResultSet userReview;
		UserReview userRev = null;

		try {
			PreparedStatement getUserReview = con
					.prepareStatement("SELECT * FROM user_reviews WHERE urid=?");

			getUserReview.setInt(1, urid);
			userReview = getUserReview.executeQuery();

			if (userReview.next()) {
				userRev = new UserReview(userReview);
				return userRev;
			}
		} catch (SQLException e) {
			System.out.println("\nNo user review with that ID exists");
			e.printStackTrace();
			return null;
		}
		return userRev;
	}

	public static ArrayList<UserReview> getUserReviewList(int target) {

		ResultSet userReview;
		ArrayList<UserReview> list = new ArrayList<UserReview>();

		try {
			PreparedStatement getUserReview = con
					.prepareStatement("SELECT * FROM user_reviews WHERE uid_target=?");

			getUserReview.setInt(1, target);

			userReview = getUserReview.executeQuery();
			while (userReview.next()) {
				//
				list.add(new UserReview(userReview));
			}

		} catch (SQLException e) {
			System.out.println("\nNo user review with that ID exists");
			e.printStackTrace();
			return null;
		}
		return list;
	}

	public static boolean deleteUserReview(UserReview reviewDetails) {

		Boolean exists;

		exists = checkReviewExists(reviewDetails);

		if (!exists) {
			System.out.println("\nReview does not exist.");
			return false;
		} else {
			try {
				// if the video location exists
				PreparedStatement dropReview = con
						.prepareStatement("DELETE FROM user_reviews WHERE urid=?");

				// Enter field data
				dropReview.setInt(1, reviewDetails.urid);

				// Delete record of filepath
				dropReview.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("\nSQL Error: deleteUserReview");
				return false;
			}
			return true;
		}
	}

	public static boolean insertHouseReview(HouseReview reviewDetails) {

		try {
			PreparedStatement insertUserReview = con
					.prepareStatement("INSERT INTO house_reviews "
							+ "VALUES (?,?,?,?,?,?,?)");

			if (reviewDetails.rating > 5) {
				System.out.println("\nRating is out of 5");
				return false;
			}
			if (reviewDetails.rating % 0 != 0) {
				System.out.println("\nRating must be an integer");
				return false;
			}

			// Values to insert
			insertUserReview.setInt(id, 0);
			insertUserReview.setInt(2, reviewDetails.hid);
			insertUserReview.setInt(3, reviewDetails.uid);
			insertUserReview.setString(4, reviewDetails.review);
			insertUserReview.setInt(5, reviewDetails.rating);
			insertUserReview.setInt(6, reviewDetails.like);
			insertUserReview.setInt(7, reviewDetails.dislike);

			// execute the query
			insertUserReview.executeUpdate();

		} catch (SQLException e) {
			System.out.println("\nSQL Error: in insertHouseReview");
			e.printStackTrace();
			e.getMessage();
			return false;
		}
		System.out.println("\nSuccess! Inserted House Review");
		return true;
	}

	public static HouseReview getHouseReview(int hrid) {

		ResultSet houseReview;
		HouseReview newReview;
		try {
			PreparedStatement getUserReview = con
					.prepareStatement("SELECT * FROM house_reviews WHERE hrid=?");

			getUserReview.setInt(1, hrid);

			houseReview = getUserReview.executeQuery();
			if (houseReview.next()) {
				newReview = new HouseReview(houseReview);
				System.out.println("\nReview id: " + newReview.hrid);
				return newReview;
			} else {
				System.out.println("\nNo house review with that ID exists");
				return null;
			}

		} catch (SQLException e) {
			System.out.println("\nNo house review with that ID exists");
			e.printStackTrace();
			return null;
		}
	}

	public static boolean checkHouseReviewExists(HouseReview reviewDetails) {
		ResultSet houseReviewSet;
		int review;
		try {
			PreparedStatement checkHouseReview = con
					.prepareStatement("SELECT * FROM house_reviews WHERE hrid=?");

			checkHouseReview.setInt(1, reviewDetails.hrid);

			houseReviewSet = checkHouseReview.executeQuery();

			while (houseReviewSet.next()) {
				review = houseReviewSet.getInt(id);
				if (review == reviewDetails.hrid) {
					System.out.println("\nHouse review exists");
					return true;
				} else {
					System.out.println("\nHouse review does not exist");
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

	public static boolean deleteHouseReview(HouseReview reviewDetails) {

		Boolean exists;

		exists = checkHouseReviewExists(reviewDetails);

		if (!exists) {
			System.out.println("\nReview does not exist.");
			return false;
		} else {
			try {
				// if the video location exists
				PreparedStatement dropReview = con
						.prepareStatement("DELETE FROM house_reviews WHERE hrid=?");

				// Enter field data
				dropReview.setInt(1, reviewDetails.hrid);

				// Delete record of filepath
				dropReview.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("\nSQL Error: deleteHouseReview");
				return false;
			}
			return true;
		}
	}

	public static boolean likeReview(User userDetails, HouseReview houseReview,
			UserReview userReview, int type) {

		// type 1 = User review
		// type 2 = House review
		ResultSet likeCheck;
		// Check User-Review like record exists
		try {
			PreparedStatement checkLikes = con
					.prepareStatement("SELECT * FROM likes WHERE type=? AND uid=? AND rid=?");
			// Users is
			checkLikes.setInt(1, type);
			checkLikes.setInt(2, userDetails.uid);
			if (type == 1)
				checkLikes.setInt(3, userReview.urid);
			if (type == 2)
				checkLikes.setInt(3, houseReview.hrid);
			likeCheck = checkLikes.executeQuery();

			boolean likeStatus;

			if (likeCheck.next()) {
				// Record already exists
				// Check if they liked or disliked
				likeStatus = likeCheck.getBoolean("liked");
				if (!likeStatus) {
					// If disliked
					try {
						PreparedStatement like = con
								.prepareStatement("UPDATE likes SET liked=? WHERE rid=? AND type=?");
						like.setBoolean(1, true);
						if (type == 1)
							like.setInt(2, userReview.urid);
						if (type == 2)
							like.setInt(2, houseReview.hrid);
						like.setInt(3, type);
						like.executeUpdate();
						try {
							// iterate the reviews like count and decrement
							// dislikes
							if (type == 1) {
								PreparedStatement iterateUserLikes = con
										.prepareStatement("UPDATE user_reviews SET `like`=?, dislike=? WHERE urid=?");
								iterateUserLikes.setInt(1, userReview.like + 1);
								iterateUserLikes.setInt(2,
										userReview.dislike - 1);
								iterateUserLikes.setInt(3, userReview.urid);
								iterateUserLikes.executeUpdate();
							}
							if (type == 2) {
								PreparedStatement iterateHouseLikes = con
										.prepareStatement("UPDATE house_reviews SET `like`=?, dislike=? WHERE hrid=?");
								iterateHouseLikes.setInt(1,
										houseReview.like + 1);
								iterateHouseLikes.setInt(2,
										houseReview.dislike - 1);
								iterateHouseLikes.setInt(3, houseReview.hrid);
								iterateHouseLikes.executeUpdate();
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
					// If already liked delete record
					// and decrement likes
					// if the video location exists
					try {
						PreparedStatement dropLikeRec = con
								.prepareStatement("DELETE FROM likes WHERE type=? AND rid=?");
						dropLikeRec.setInt(1, type);
						if (type == 1)
							dropLikeRec.setInt(2, userReview.urid);
						if (type == 2)
							dropLikeRec.setInt(2, houseReview.hrid);
						dropLikeRec.executeUpdate();
						try {
							// decrememt the reviews like count
							if (type == 1) {
								PreparedStatement iterateUserLikes = con
										.prepareStatement("UPDATE user_reviews SET `like`=? WHERE urid=?");
								iterateUserLikes.setInt(1, userReview.like - 1);
								iterateUserLikes.setInt(2, userReview.urid);
								iterateUserLikes.executeUpdate();
							}
							if (type == 2) {
								PreparedStatement iterateHouseLikes = con
										.prepareStatement("UPDATE house_reviews SET `like`=? WHERE hrid=?");
								iterateHouseLikes.setInt(1,
										houseReview.like - 1);
								iterateHouseLikes.setInt(2, houseReview.hrid);
								iterateHouseLikes.executeUpdate();
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
				// If record doesn't exist create it
				// and add a like to the review
				try {
					PreparedStatement newRecord = con
							.prepareStatement("INSERT INTO likes VALUES (?,?,?,?)");
					// ID of the user submitting a like
					newRecord.setInt(1, userDetails.uid);
					newRecord.setInt(2, type);
					// ID of the review being like
					if (type == 1)
						newRecord.setInt(3, userReview.urid);
					if (type == 2)
						newRecord.setInt(3, houseReview.hrid);
					newRecord.setBoolean(4, true);
					newRecord.executeUpdate();
				} catch (Exception e) {
					System.out.println("\nFailure to insert like record");
					e.printStackTrace();
				}
				try {
					// iterate the reviews like count
					if (type == 1) {
						PreparedStatement iterateUserLikes = con
								.prepareStatement("UPDATE user_reviews SET `like`=? WHERE urid=?");
						iterateUserLikes.setInt(1, userReview.like + 1);
						iterateUserLikes.setInt(2, userReview.urid);
						iterateUserLikes.executeUpdate();
					}
					if (type == 2) {
						PreparedStatement iterateHouseLikes = con
								.prepareStatement("UPDATE house_reviews SET `like`=? WHERE hrid=?");
						iterateHouseLikes.setInt(1, houseReview.like + 1);
						iterateHouseLikes.setInt(2, houseReview.hrid);
						iterateHouseLikes.executeUpdate();
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

	public static boolean dislikeReview(User userDetails,
			HouseReview houseReview, UserReview userReview, int type) {

		// type 1 = User review
		// type 2 = House review
		ResultSet likeCheck;
		// Check User-Review like record exists
		try {
			PreparedStatement checkLikes = con
					.prepareStatement("SELECT * FROM likes WHERE type=? AND uid=? AND rid=?");
			// Users is
			checkLikes.setInt(1, type);
			checkLikes.setInt(2, userDetails.uid);
			if (type == 1)
				checkLikes.setInt(3, userReview.urid);
			if (type == 2)
				checkLikes.setInt(3, houseReview.hrid);
			likeCheck = checkLikes.executeQuery();

			boolean likeStatus;

			if (likeCheck.next()) {
				// Record already exists
				// Check if they liked or disliked
				likeStatus = likeCheck.getBoolean("liked");
				if (!likeStatus) {
					// If already disliked delete record
					// and decrement dislikes
					try {
						PreparedStatement dropLikeRec = con
								.prepareStatement("DELETE FROM likes WHERE type=? AND rid=? AND uid=?");
						dropLikeRec.setInt(1, type);
						if (type == 1)
							dropLikeRec.setInt(2, userReview.urid);
						if (type == 2)
							dropLikeRec.setInt(2, houseReview.hrid);
						dropLikeRec.setInt(3, userDetails.uid);
						dropLikeRec.executeUpdate();
						try {
							// decrememt the reviews dislike count
							if (type == 1) {
								PreparedStatement iterateUserLikes = con
										.prepareStatement("UPDATE user_reviews SET dislike=? WHERE urid=?");
								iterateUserLikes.setInt(1,
										userReview.dislike - 1);
								iterateUserLikes.setInt(2, userReview.urid);
								iterateUserLikes.executeUpdate();
							}
							if (type == 2) {
								PreparedStatement iterateHouseLikes = con
										.prepareStatement("UPDATE house_reviews SET dislike=? WHERE hrid=?");
								iterateHouseLikes.setInt(1,
										houseReview.dislike - 1);
								iterateHouseLikes.setInt(2, houseReview.hrid);
								iterateHouseLikes.executeUpdate();
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
					// if liked switch like status and ++ dis -- likes
					try {
						PreparedStatement like = con
								.prepareStatement("UPDATE likes SET liked=? WHERE rid=? AND type=? AND uid=?");
						like.setBoolean(1, false);
						if (type == 1)
							like.setInt(2, userReview.urid);
						if (type == 2)
							like.setInt(2, houseReview.hrid);
						like.setInt(3, type);
						like.setInt(4, userDetails.uid);
						like.executeUpdate();
						try {
							// iterate the reviews like count and decrement
							// dislikes
							if (type == 1) {
								PreparedStatement iterateUserLikes = con
										.prepareStatement("UPDATE user_reviews SET `like`=?, dislike=? WHERE urid=?");
								iterateUserLikes.setInt(1, userReview.like - 1);
								iterateUserLikes.setInt(2,
										userReview.dislike + 1);
								iterateUserLikes.setInt(3, userReview.urid);
								iterateUserLikes.executeUpdate();
							}
							if (type == 2) {
								PreparedStatement iterateHouseLikes = con
										.prepareStatement("UPDATE house_reviews SET `like`=?, dislike=? WHERE hrid=?");
								iterateHouseLikes.setInt(1,
										houseReview.like - 1);
								iterateHouseLikes.setInt(2,
										houseReview.dislike + 1);
								iterateHouseLikes.setInt(3, houseReview.hrid);
								iterateHouseLikes.executeUpdate();
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
				// If record doesn't exist create it
				// and add a dislike to the review
				try {
					PreparedStatement newRecord = con
							.prepareStatement("INSERT INTO likes VALUES (?,?,?,?)");
					// ID of the user submitting a like
					newRecord.setInt(1, userDetails.uid);
					newRecord.setInt(2, type);
					// ID of the review being like
					if (type == 1)
						newRecord.setInt(3, userReview.urid);
					if (type == 2)
						newRecord.setInt(3, houseReview.hrid);
					newRecord.setBoolean(4, false);
					newRecord.executeUpdate();
				} catch (Exception e) {
					System.out.println("\nFailure to insert dislike record");
					e.printStackTrace();
				}
				try {
					// iterate the reviews dislike count
					if (type == 1) {
						PreparedStatement iterateUserLikes = con
								.prepareStatement("UPDATE user_reviews SET dislike=? WHERE urid=?");
						iterateUserLikes.setInt(1, userReview.dislike + 1);
						iterateUserLikes.setInt(2, userReview.urid);
						iterateUserLikes.executeUpdate();
					}
					if (type == 2) {
						PreparedStatement iterateHouseLikes = con
								.prepareStatement("UPDATE house_reviews SET dislike=? WHERE hrid=?");
						iterateHouseLikes.setInt(1, houseReview.dislike + 1);
						iterateHouseLikes.setInt(2, houseReview.hrid);
						iterateHouseLikes.executeUpdate();
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
	 * Selects all houses
	 * 
	 * @return ArrayList of house id's
	 */
	public static ArrayList<Integer> selectAllHouses() {

		ArrayList<Integer> list = new ArrayList<Integer>();
		ResultSet houses = null;
		try {
			PreparedStatement allHouses = con
					.prepareStatement("SELECT * FROM houses");

			houses = allHouses.executeQuery();
			// if houses do exist loop through all of them

			while (houses.next()) {
				// store all hid's in an arraylist
				list.add(houses.getInt("hid"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			list = null;
			System.out.println("\nSelect all houses failed.");
		}
		return list;
	}

	public static void main(String[] args) throws Exception {
		// Connect to the Database
		dbConnect();

		boolean check;
		User insert = null;
		// User checkUse = null
		String username = "DefTest3";
		String password = "Eyehouse1";
		String hashPassword = DataHandler.crypt(password);

		String email = "DefProfTest3@york.ac.uk";

		String title = "Example";

		int mode = 11;
		boolean insertSuccess;
		boolean houseDeleted;
		boolean updateSuccess;

		// Henries ID
		int targetID = 3106;

		// testing switch
		switch (mode) {
		case 11:
			// insert the houses basic info
			House eyehouseHQ = null;
			int pricepermonth = 56;
			boolean house;

			String brc = "M:/Distributed Computer Systems/DNSTex.pdf";
			String enrg = "M:/Distributed Computer Systems/ddos.jpg";
			eyehouseHQ = new House(title);
			eyehouseHQ.postcode("YO1 7HP");
			eyehouseHQ.address("Testing Date");
			eyehouseHQ.price(pricepermonth);
			eyehouseHQ.deposit(pricepermonth);
			eyehouseHQ.rooms(pricepermonth);
			eyehouseHQ.bathrooms(pricepermonth);
			eyehouseHQ.dateAvailable("2015-04-24");
			eyehouseHQ.furnished(true);
			eyehouseHQ.description("Test");
			User temp = getUser("MVPTom");
			if (!checkHouseExists(temp, eyehouseHQ)) {
				house = houseInsert(eyehouseHQ, brc, enrg, temp);
				eyehouseHQ.printHouse();
				System.out.println(house);
			}
			break;
		case 14:
			int tempPrc = 9001;
			// for a date string
			int varType = 1;
			User tempu3 = getUser("MVPTom");
			House temph3 = getHouse(8);
			check = updateHouse(tempu3, temph3, "date_available", "2015-06-22",
					null, null, tempPrc, varType);
			if (check == true)
				System.out.println("\nUpdate Successful");
			else
				System.out.println("\nFailure");
			break;
		case 21:

			User tempu14 = getUser("MVPTom");

			// MVPTom review on
			// HouseReview one = getHouseReview(2);

			HouseReview test1 = getHouseReview(2);
			UserReview test2 = getUserReview(2);

			// likeReview(tempu14, test1, test2, 2);

			dislikeReview(tempu14, test1, test2, 2);

			break;
		case 2: // insert User

			// user to be inserted
			insert = new User(username);
			insert.firstName("Testing");
			insert.secondName("File read");
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
		case 4: // edit details

			User tempu9 = getUser("MVPTom");

			// Update user
			updateSuccess = userUpdate(tempu9, "password", null, hashPassword);

			System.out.println("User update method returns: " + updateSuccess);

			tempu9.printUser();

			break;
		case 10:
			// String tablename, String filepath, String fieldSelect, String id
			String tablename = "users";
			String filepath = "D:/EE course/SWEng/Java/Compilation1.jpg";
			String fieldSelect = "profileIMG";

			int id = 3104;

			// Update image
			updateImage(tablename, filepath, fieldSelect, id);

			break;
		case 12:
			// a lot of cases!
			// get House id method and get house as well
			User tempu = getUser("MVPTom");
			// gets house and puts it into memory
			House temph = getHouse(8);
			int uid = getID(tempu, null, 1);
			int hid = getID(tempu, temph, 2);
			System.out.println("User ID: " + uid + "\nHouse ID: " + hid);
			break;
		case 13:
			User tempu2 = getUser("MVPTom");
			House temph2 = getHouse(8);
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

		case 15:

			User tempu4 = getUser("MVPTom");
			House temph4 = getHouse(8);

			// perhaps enter your own local file to test here, unless you're on
			// my laptop
			String localFilePath = "D:/EE course/SWEng/Java/eyehouseHQ1.jpg";

			// Insert image
			check = insertHouseImage(localFilePath, temph4, tempu4);

			if (check == true)
				System.out.println("\nInsert Successful");
			else
				System.out.println("\nFailure");

			break;
		case 16:

			// int hid5 = getID(tempu5, temph5, 2);
			ArrayList<HouseImage> list = new ArrayList<HouseImage>();
			list = getHouseImageSet(10);

			int i;
			for (i = 0; i < list.size(); i++) {
				HouseImage image = list.get(i);
				System.out.println(image.imageIS);

				// InputStream binaryStream = image.imageBlob.getBinaryStream(1,
				// image.imageBlob.length());
				Image picture = ImageIO.read(image.imageIS);

				JFrame frame = new JFrame();
				JLabel label = new JLabel(new ImageIcon(picture));
				frame.getContentPane().add(label, BorderLayout.CENTER);
				frame.pack();
				frame.setVisible(true);
				// Deletes all the images for this house
				if (image.iid == 19) {
					deleteHouseImage(image);
				}
			}

			break;
		case 17:

			User tempu6 = getUser("MVPTom");

			House temph6 = getHouse(8);

			String localDir = "D:/EE course/SWEng/Java/";
			String filename = "example_clip.mp4";

			check = insertHouseVideo(tempu6, temph6, filename, localDir);

			if (check == true)
				System.out.println("\nInsert Successful");
			else
				System.out.println("\nFailure");

			break;
		case 18:

			User tempu7 = getUser("MVPTom");
			House temph7 = getHouse(8);

			String filename1 = "example_clip.mp4";

			HouseVideo house1;

			house1 = getVideoInfo(tempu7, temph7, filename1);

			house1.printHouseInfo();

			check = deleteVideo(tempu7, house1);
			if (check == true)
				System.out.println("\nDelete Successful");
			else
				System.out.println("\nFailure");

			break;

		case 19:
			// logged in user
			User tempu12 = getUser("Alxandir");

			// fill in target ID
			UserReview reviewDetails = new UserReview(targetID);
			reviewDetails.uid_reviewer = tempu12.uid;
			reviewDetails.review = "Hes such a dick. Thank god hes not in charge of anything important";
			reviewDetails.rating(1);
			reviewDetails.like(1000);
			reviewDetails.dislike(0);

			// check = insertUserReview(reviewDetails);

			checkReviewExists(reviewDetails);

			ArrayList<UserReview> list2 = new ArrayList<UserReview>();
			list2 = getUserReviewList(targetID);

			int k;
			for (k = 0; k < list2.size(); k++) {
				System.out.println("\nReview: " + list2.get(k).review);
				System.out.println("\nReview id: " + list2.get(k).urid);
				System.out.println("\nReviewer id: "
						+ list2.get(k).uid_reviewer);
			}
			// check = deleteUserReview(list2.get(1));

			// if (check == true)
			// System.out.println("\nSuccessful");
			// else
			// System.out.println("\nFailure");

			break;
		case 20:
			// logged in user
			User tempu13 = getUser("MVPTom");
			House temph13 = getHouse(8);
			// get user id
			int uid13 = getID(tempu13, null, 1);
			// get hid
			int hid13 = getID(tempu13, temph13, 2);

			// fill in target ID
			HouseReview hreviewDetails = new HouseReview(hid13);
			hreviewDetails.uid = uid13;
			hreviewDetails.review = "A kind house. Slightly too fond of children";
			hreviewDetails.rating(0);
			hreviewDetails.like(23);
			hreviewDetails.dislike(49);

			// check = insertHouseReview(hreviewDetails);

			HouseReview newHouseReview = getHouseReview(2);

			// checkHouseReviewExists(newHouseReview);

			System.out.println("\nNew review: " + newHouseReview.review);

			// check = deleteHouseReview(newHouseReview);

			checkHouseReviewExists(newHouseReview);

			// if (check == true)
			// System.out.println("\nSuccessful");
			// else
			// System.out.println("\nFailure");

			break;

		default: // no mode selected
			System.out.println("\nSelect a valid switch case mode");
			break;
		}
	}
}