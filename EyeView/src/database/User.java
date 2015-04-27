package database;

import java.awt.BorderLayout;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.mysql.jdbc.Blob;

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

	// id should automatically be created on insertion
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

	// user contructor method
	public User(String username) {
		this.username = username;
	}

	// user contructor method
	public User(ResultSet userDetails) {
		// fill details
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
	
	public void profimg(Blob imageblob) {
		profimg = imageblob;
	}
	
	public void properties(String userHouses) {
		properties = userHouses;
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
	public void printUser() throws IOException {
		System.out.println("\nUsername: " + username);
		System.out.println("Email: " + email);
		System.out.println("Password: " + password);
		
		try {
			InputStream binaryStream = profimg.getBinaryStream(1, profimg.length());
			
			Image image = ImageIO.read(binaryStream);
			
			JFrame frame = new JFrame();
		    JLabel label = new JLabel(new ImageIcon(image));
		    frame.getContentPane().add(label, BorderLayout.CENTER);
		    frame.pack();
		    frame.setVisible(true);
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}