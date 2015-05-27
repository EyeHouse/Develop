package database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HouseVideo {

	public int vid;
	int hid;
	String videoLocation;

	private final static int VID = 1;
	private final static int HID = 2;
	private final static int VIDEO = 3;

	public HouseVideo(int imageID) {
		this.vid = imageID;
	}

	public HouseVideo(ResultSet videoDetails) {
		try {
			this.vid = videoDetails.getInt(VID);
			this.hid = videoDetails.getInt(HID);
			this.videoLocation = videoDetails.getString(VIDEO);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void vid(int videoID) {
		this.vid = videoID;
	}

	public void hid(int houseID) {
		this.hid = houseID;
	}

	public void videoLocation(String vidLoc) {
		this.videoLocation = vidLoc;
	}
	
	public void printHouseInfo() {
		System.out.println("\nVideo id: " + this.vid);
		System.out.println("\nHouse id: " + this.hid);
		System.out.println("\nVideo Location: " + this.videoLocation);
		
	}
}