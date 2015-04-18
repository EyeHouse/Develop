package database;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Blob;

public class House {

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

	public int uid;
	public String title;
	public String postcode;
	public String address;
	public int price;
	public int deposit;
	public int rooms;
	public int bathrooms;
	public String dateAvailable;
	public boolean furnished;
	public Blob brochure;
	public String description;
	public Blob energyRating;

	// user contructor method
	public House(String title) {
		this.title = title;
	}

	public House(ResultSet houseDetails) {
			// fill details
			try {
				this.uid = houseDetails.getInt(UID);
				this.title = houseDetails.getString(TITLE);
				this.postcode = houseDetails.getString(POSTCODE);
				this.address = houseDetails.getString(ADDRESS);
				this.price = houseDetails.getInt(PRICE);
				this.deposit = houseDetails.getInt(DEPOSIT); 
				this.rooms = houseDetails.getInt(ROOMS); 
				this.bathrooms = houseDetails.getInt(BATHROOMS);
				this.dateAvailable = houseDetails.getString(DATEAVAILABLE);
				this.furnished = houseDetails.getBoolean(FURNISHED);
				this.brochure = (Blob) houseDetails.getBlob(BROCHURE);
				this.description = houseDetails.getString(DESCRIPTION);
				this.energyRating = (Blob) houseDetails.getBlob(ENERGYRATING);
			} catch (SQLException e) {
				e.printStackTrace();
				e.getMessage();
			}
		}

	// fill house details
	public void uid(int userId) {
		uid = userId;
	}

	public void postcode(String pc) {
		postcode = pc;
	}

	public void address(String adr) {
		address = adr;
	}

	public void price(int prc) {
		price = prc;
	}

	public void deposit(int dps) {
		deposit = dps;
	}

	public void rooms(int rms) {
		rooms = rms;
	}

	public void bathrooms(int brms) {
		bathrooms = brms;
	}

	public void dateAvailable(String dav) {
		dateAvailable = dav;
	}

	public void furnished(Boolean fnsd) {
		furnished = fnsd;
	}

	public void brochure(Blob brchr) {
		brochure = brchr;
	}

	public void description(String dsc) {
		description = dsc;
	}
	
	public void energyRating(Blob enrt) {
		energyRating = enrt;
	}
	
	public void printHouse() {
		System.out.println("\ntitle " + title);
		System.out.println("Id of owner: " + uid);
	}
}
