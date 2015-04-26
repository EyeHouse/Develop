package database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HouseReview {
	private final static int HRID = 1;
	private final static int HID = 2;
	private final static int UID = 3;
	private final static int REVIEW = 4;
	private final static int RATING = 5;
	private final static int LIKE = 6;
	private final static int DISLIKE = 7;

	int hrid;
	int hid;
	int uid;
	int rating;

	String review;

	boolean like;
	boolean dislike;

	// Manually create a view off of new input
	public HouseReview(int hrTG) {
		this.hid = hrTG;
	}

	// Get a reviews details after searching/ selecting it
	public HouseReview(ResultSet reviewDetails) {
		try {
			this.hrid = reviewDetails.getInt(HRID);
			this.hid = reviewDetails.getInt(HID);
			this.uid = reviewDetails.getInt(UID);
			this.review = reviewDetails.getString(REVIEW);
			this.rating = reviewDetails.getInt(RATING);
			this.like = reviewDetails.getBoolean(LIKE);
			this.dislike = reviewDetails.getBoolean(DISLIKE);
		} catch (SQLException e) {
			System.out.println("\nError in HouseReview class.");
			e.printStackTrace();
		}
	}

	public void uid(int urRV) {
		this.uid = urRV;
	}

	public void review(String rv) {
		this.review = rv;
	}

	public void rating(int rating) {
		this.rating = rating;
	}

	public void like(boolean lk) {
		this.like = lk;
	}

	public void dislike(boolean dslk) {
		this.dislike = dslk;
	}

}
