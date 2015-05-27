package database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Marker {

	private final static int MID = 1;
	private final static int VID = 2;
	private final static int ROOM = 3;
	private final static int TIME = 4;

	public int mid;
	public int vid;
	public String room;
	public double markerTime;

	// user contructor method
	public Marker(String rm) {
		this.room = rm;
	}

	public Marker(ResultSet markerDetails) {
		try {
			this.mid = markerDetails.getInt(MID);
			this.vid = markerDetails.getInt(VID);
			this.room = markerDetails.getString(ROOM);
			this.markerTime = markerDetails.getDouble(TIME);
		} catch (SQLException e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	public void time(double tm) {
		this.markerTime = tm;
	}
	
	public void videoID(int id) {
		this.vid = id;
	}

}
