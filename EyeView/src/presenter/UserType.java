package presenter;

class UserType {
	public String fName;
	public String lName;
	public String email;
	public String username;
	public boolean landlord;
	public String password;
	public String doB;
	public boolean admin;
	public String profileText;

	public UserType(String fName, String lName, String email, String username,
			boolean landlord, String password, String doB, boolean admin,
			String profileText) {
		this.fName = fName;
		this.lName = lName;
		this.email = email;
		this.username = username;
		this.landlord = landlord;
		this.password = password;
		this.doB = doB;
		this.admin = admin;
		this.profileText = profileText;
	}
}
