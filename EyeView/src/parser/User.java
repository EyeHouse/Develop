package parser;

public class User {
 
	//id should automatically be created on insertion
	String first_name;
	String second_name;
	String email;
	String username;
	boolean landlord;
	String password;
	String DOB;
	boolean admin;
	
	//user contructor method
	public User(String username) {
		this.username = username; 
	}
	//fill user details
	public void firstName(String firstname) {
		first_name = firstname;
	}
	//user second name
	public void secondName(String secondname) {
		second_name = secondname;
	}
	//user email address
	public void email(String emailAddress) {
		email = emailAddress;
	}
	//true if user is to be granted landlord privileges
	public void landlord(boolean isLandlord) {
		landlord = isLandlord;
	}
	//user password
	public void password(String pw) {
		password = pw;
	}
	//dates in the form year/month/day xxxx-xx-xx
	public void DOB(String dateBirth) {
		DOB = dateBirth;
	}
	//true if user is to be granted admin privileges
	public void admin(boolean isAdmin) {
		admin = isAdmin;
	}
	//option to print details for developer tests
	public void printUser() {
		System.out.println("\nUsername: " + username);
		System.out.println("Email: " + email);
		System.out.println("Password: " + password);
	}
	
}
