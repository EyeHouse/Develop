package database;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Blob;

/**
 * Holds images and associated information.
 * 
 * @version 1.48 (15.03.15)
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 * 
 */
public class HouseImage {

	public int iid;
	public int hid;
	public Blob imageBlob;
	public InputStream imageIS;

	private final static int IID = 1;
	private final static int HID = 2;
	private final static int IMAGE = 3;

	// HouseImage constructor
	public HouseImage(int imageID) {
		this.iid = imageID;
	}

	// Creates a HouseImage from a result set
	public HouseImage(ResultSet imageDetails) {
		try {
			this.iid = imageDetails.getInt(IID);
			this.hid = imageDetails.getInt(HID);
			this.imageBlob = (Blob) imageDetails.getBlob(IMAGE);
			this.imageIS = imageBlob.getBinaryStream(1, imageBlob.length());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Sets info
	public void iid(int imageID) {
		this.iid = imageID;
	}

	public void hid(int houseID) {
		this.hid = houseID;
	}

	public void imageBlob(Blob img) {
		this.imageBlob = img;
	}

	public void imageInputStream(Blob img) {
		try {
			this.imageIS = img.getBinaryStream(1, img.length());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
