package database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Holds a video and associated data.
 * 
 * @version 1.48 (15.03.15)
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class HouseVideo {

	public int vid;
	public int hid;
	public String videoLocation;

	private final static int VID = 1;
	private final static int HID = 2;
	private final static int VIDEO = 3;

	/**
	 * Constructor method
	 * 
	 * @param imageID
	 */
	public HouseVideo(int imageID) {
		this.vid = imageID;
	}

	/**
	 * Constructor method
	 * 
	 * @param videoDetails
	 */
	public HouseVideo(ResultSet videoDetails) {
		try {
			this.vid = videoDetails.getInt(VID);
			this.hid = videoDetails.getInt(HID);
			this.videoLocation = videoDetails.getString(VIDEO);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Set details
	public void vid(int videoID) {
		this.vid = videoID;
	}

	public void hid(int houseID) {
		this.hid = houseID;
	}

	public void videoLocation(String vidLoc) {
		this.videoLocation = vidLoc;
	}
}
