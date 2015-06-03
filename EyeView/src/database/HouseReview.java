package database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Holds information for a house review
 * 
 * @version 1.48 (15.03.15)
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class HouseReview {
	
	private final static int HRID = 1;
	private final static int HID = 2;
	private final static int UID = 3;
	private final static int REVIEW = 4;
	private final static int RATING = 5;
	private final static int LIKE = 6;
	private final static int DISLIKE = 7;

	public int hrid;
	public int hid;
	public int uid;
	public int rating;

	public String review;

	public int like;
	public int dislike;

	// HouseReview Contructor
	public HouseReview(int hrTG) {
		this.hid = hrTG;
	}

	// Create HouseReview from a result set
	public HouseReview(ResultSet reviewDetails) {
		
		try {
			this.hrid = reviewDetails.getInt(HRID);
			this.hid = reviewDetails.getInt(HID);
			this.uid = reviewDetails.getInt(UID);
			this.review = reviewDetails.getString(REVIEW);
			this.rating = reviewDetails.getInt(RATING);
			this.like = reviewDetails.getInt(LIKE);
			this.dislike = reviewDetails.getInt(DISLIKE);
		} catch (SQLException e) {
			System.out.println("\nError in HouseReview class.");
			e.printStackTrace();
		}
	}

	// Set details
	public void uid(int urRV) {
		this.uid = urRV;
	}

	public void review(String rv) {
		this.review = rv;
	}

	public void rating(int rating) {
		this.rating = rating;
	}

	public void like(int lk) {
		this.like = lk;
	}

	public void dislike(int dslk) {
		this.dislike = dslk;
	}

}
