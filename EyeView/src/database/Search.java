package database;

import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Returns a list of houses based on
 * 
 * @author Tom
 * 
 */
public class Search {

	private static final String GEO_CODE_SERVER = "http://maps.googleapis.com/maps/api/geocode/json?";
	public static final double R = 6372.8; // In kilometers

	public static String url = "10.10.0.1";

	public static ArrayList<Double> getLongLat(String address) {

		ArrayList<Double> list = new ArrayList<Double>();
		String code = address;

		String response = getLocation(code);

		String[] result = parseLocation(response);

		int i = 0;
		for (i = 0; i < result.length; i++) {
			list.add(Double.parseDouble(result[i]));
		}

		return list;
	}

	private static String[] parseLocation(String response) {
		// Look for location using brute force.
		// There are much nicer ways to do this, e.g. with Google's JSON
		// library: Gson
		// https://sites.google.com/site/gson/gson-user-guide

		String[] lines = response.split("\n");

		String lat = null;
		String lng = null;

		for (int i = 0; i < lines.length; i++) {
			if ("\"location\" : {".equals(lines[i].trim())) {
				lat = getOrdinate(lines[i + 1]);
				lng = getOrdinate(lines[i + 2]);
				break;
			}
		}

		return new String[] { lat, lng };
	}

	private static String getOrdinate(String s) {
		String[] split = s.trim().split(" ");

		if (split.length < 1) {
			return null;
		}

		String ord = split[split.length - 1];

		if (ord.endsWith(",")) {
			ord = ord.substring(0, ord.length() - 1);
		}

		// Check that the result is a valid double
		Double.parseDouble(ord);

		return ord;
	}

	private static String buildUrl(String code) {
		StringBuilder builder = new StringBuilder();

		builder.append(GEO_CODE_SERVER);

		builder.append("address=");
		builder.append(code.replaceAll(" ", "+"));
		builder.append("&sensor=false");

		return builder.toString();
	}

	private static String getLocation(String code) {

		String address = buildUrl(code);

		String content = null;

		try {

			URL url = new URL(address);

			InputStream stream = url.openStream();

			try {
				int available = stream.available();

				byte[] bytes = new byte[available];

				stream.read(bytes);

				content = new String(bytes);
			} finally {
				stream.close();
			}

			return (String) content.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * From http://rosettacode.org/wiki/Haversine_formula#Java
	 * 
	 * @param lat1
	 * @param lon1
	 * @param lat2
	 * @param lon2
	 * @return distance
	 */
	public static double haversine(double lat1, double lon1, double lat2,
			double lon2) {
		// Conver Lat Long to radians and calculate differences
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);

		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);
		// Calculate the distance between ll's using haversine function maths
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2)
				* Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
		double c = 2 * Math.asin(Math.sqrt(a));
		// return distance
		return R * c;

	}

	public static ArrayList<House> rooms(int rooms, boolean filterType) {

		ArrayList<House> list = new ArrayList<House>();

		ResultSet roomRS1 = null;
		ResultSet roomRS2 = null;

		try {
			// if filter is true then search where room number is greater
			if (filterType) {
				PreparedStatement roomQuery = Database.con
						.prepareStatement("SELECT * FROM houses WHERE rooms > ? ORDER BY rooms ASC LIMIT 50");
				roomQuery.setInt(1, rooms);
				roomRS1 = roomQuery.executeQuery();

				while (roomRS1.next()) {
					list.add(new House(roomRS1));
				}
			}
			// < entered rooms
			if (!filterType) {
				PreparedStatement roomQuery = Database.con
						.prepareStatement("SELECT * FROM houses WHERE rooms < ? ORDER BY rooms ASC LIMIT 50");

				roomQuery.setInt(1, rooms);
				roomRS2 = roomQuery.executeQuery();

				while (roomRS2.next()) {
					list.add(new House(roomRS2));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error Rooms Search: " + e.getErrorCode());
		}
		return list;
	}
	
	

	public static ArrayList<House> price(int price, boolean filterType) {

		ArrayList<House> list = new ArrayList<House>();

		ResultSet priceRS1 = null;
		ResultSet priceRS2 = null;

		try {
			// if filter is true then search where price is greater
			if (filterType) {
				PreparedStatement priceQuery = Database.con
						.prepareStatement("SELECT * FROM houses WHERE price > ? ORDER BY price ASC LIMIT 50");
				priceQuery.setInt(1, price);
				priceRS1 = priceQuery.executeQuery();

				while (priceRS1.next()) {
					list.add(new House(priceRS1));
				}
			}
			// < entered price
			if (!filterType) {
				PreparedStatement priceQuery = Database.con
						.prepareStatement("SELECT * FROM houses WHERE price < ? ORDER BY price ASC LIMIT 50");

				priceQuery.setInt(1, price);
				priceRS2 = priceQuery.executeQuery();

				while (priceRS2.next()) {
					list.add(new House(priceRS2));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error Price Search: " + e.getErrorCode());
		}
		return list;
	}
	
	

	public static void main(String[] args) throws Exception {

		Database.dbConnect();

		// String address = "YO23 1JZ";
		//
		// ArrayList<Double> longLat = new ArrayList<Double>();
		//
		// longLat = getLongLat(address);
		//
		// System.out.println("Latitude: " + longLat.get(0));
		// System.out.println("Longitude: " + longLat.get(1));

		ArrayList<House> list = new ArrayList<House>();

		// list = price(5000, false);
		list = rooms(4, false);

		int i;
		for (i = 0; i < list.size(); i++) {
			House temp = list.get(i);
			temp.printHouse();
		}
	}
}
