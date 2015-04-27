package database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserReview {

	private final static int URID = 1;
	private final static int UID_TARGET = 2;
	private final static int UID_REVIEWER = 3;
	private final static int REVIEW = 4;
	private final static int RATING = 5;
	private final static int LIKE = 6;
	private final static int DISLIKE = 7;

	public int urid;
	public int uid_target;
	public int uid_reviewer;
	public int rating;

	public String review;

	public int like;
	public int dislike;

	// Manually create a view off of new input
	public UserReview(int urTG) {
		this.uid_target = urTG;
	}

	// Get a reviews details after searching/ selecting it
	public UserReview(ResultSet reviewDetails) {
		try {
			this.urid = reviewDetails.getInt(URID);
			this.uid_target = reviewDetails.getInt(UID_TARGET);
			this.uid_reviewer = reviewDetails.getInt(UID_REVIEWER);
			this.review = reviewDetails.getString(REVIEW);
			this.rating = reviewDetails.getInt(RATING);
			this.like = reviewDetails.getInt(LIKE);
			this.dislike = reviewDetails.getInt(DISLIKE);
		} catch (SQLException e) {
			System.out.println("\nError in UserReview class.");
			e.printStackTrace();
		}
	}

	public void uid_reviewer(int urRV) {
		this.uid_reviewer = urRV;
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
