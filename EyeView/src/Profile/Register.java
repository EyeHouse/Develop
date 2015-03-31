package Profile;

//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.text.SimpleDateFormat;
//import java.util.Locale;

import Button.ButtonType;
import Button.SetupButton;
import presenter.SlideContent;
import database.DataHandler;
import database.Database;
import database.User;
import javafx.event.EventHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

public class Register extends presenter.Window {

	public String userFirstname, userLastName, userEmail, userName,
			encryptedPassword, dateOfBirth;
	public User user = null;
	private String userPassword, userRepeatPassword;
	// private static MessageDigest digester;

	private TextField username;
	private TextField firstname;
	private TextField lastname;
	private TextField email;
	private PasswordField password;
	private PasswordField repeatPassword;
	private GridPane registerGrid = new GridPane();
	
	private RadioButton buttonStudent = new RadioButton("Student");
	private RadioButton buttonLandlord = new RadioButton("Landlord");
	private RadioButton buttonOther = new RadioButton("Other");

	private ComboBox<String> comboDoBYear = new ComboBox<String>();
	private ComboBox<String> comboDoBMonth = new ComboBox<String>();
	private ComboBox<String> comboDoBDay = new ComboBox<String>();

	public static void main(String[] args) {
		Database.dbConnect();
		launch(args);
	}

	public Register() {
		
		setupGrid();
		setupTitle();
		setupTextFields(registerGrid);
		setupDoB();
		setupPasswordFields(registerGrid);
		setupButtons();
		
		root.getChildren().add(registerGrid);

	}

	public void setupGrid() {

		registerGrid.setVgap(30);
		registerGrid.setHgap(30);
		registerGrid.relocate(250,90);
	}
	
	public void setupTitle() {

		final Label topTitle = new Label("Register");
		topTitle.setTextFill(Color.web("#162252FF"));
		topTitle.setFont(new Font(35));
		registerGrid.add(topTitle, 0, 0);
		GridPane.setConstraints(topTitle, 0, 0, 2, 1, HPos.CENTER, VPos.CENTER);

	}

	public void setupTextFields(GridPane grid) {
		HBox hBoxAccountType = new HBox(10);
		ToggleGroup group = new ToggleGroup();
		
		// username field
		username = new TextField();
		username.setPromptText("Username");
		username.setPrefColumnCount(10);
		registerGrid.addRow(1,new Label("Enter your username:"),username);

		// First Name field
		firstname = new TextField();
		firstname.setPromptText("Joe");
		firstname.setPrefColumnCount(10);
		registerGrid.addRow(2,new Label("First name:"),firstname);

		// Last Name field
		lastname = new TextField();
		lastname.setPromptText("Bloggs");
		lastname.setPrefColumnCount(10);
		registerGrid.addRow(3,new Label("Last name:"),lastname);
		
		// Account Type radio buttons
		buttonStudent.setToggleGroup(group);
		buttonLandlord.setToggleGroup(group);
		buttonOther.setToggleGroup(group);
		hBoxAccountType.getChildren().addAll(buttonStudent, buttonLandlord, buttonOther);
		registerGrid.addRow(5,new Label("Account Type:"),hBoxAccountType);
		
		// Email field
		email = new TextField();
		email.setPromptText("example@mail.com");
		email.setPrefColumnCount(10);
		registerGrid.addRow(6,new Label("Email:"),email);
	}
	
	public void setupDoB() {

		// Populate comboboxes with relevant string values
				for (int i = 1; i < 32; i++) {
					comboDoBDay.getItems().add(String.format("%02d", i));
				}
				for (int i = 1; i < 13; i++) {
					comboDoBMonth.getItems().add(String.format("%02d", i));
				}
				for (int i = 2015; i > 1919; i--) {
					comboDoBYear.getItems().add(String.format("%04d", i));
				}

		registerGrid.add(new Label("Date of Birth: "), 0, 4);

		comboDoBDay.setValue("DD");
		comboDoBMonth.setValue("MM");
		comboDoBYear.setValue("YYYY");
		
		comboDoBMonth.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, String previousValue,
					String newValue) {
				
				comboDoBDay.setValue("DA");
				comboDoBDay.getItems().clear();
				switch (newValue) {
					case "09":
					case "04":
					case "06":
					case "11":
						for (int i = 1; i < 31; i++) {
							comboDoBDay.getItems().add(String.format("%02d", i));
						}
						break;
					case "02":
						for (int i = 1; i < 29; i++) {
							comboDoBDay.getItems().add(String.format("%02d", i));
						}
						break;
					default:
						for (int i = 1; i < 32; i++) {
							comboDoBDay.getItems().add(String.format("%02d", i));
						}
						break;
				
				}
				comboDoBDay.setValue("DD");
				
			}
		});
		
		HBox comboBoxes = new HBox(30);
		comboBoxes.getChildren().addAll(comboDoBDay,comboDoBMonth,comboDoBYear);
		registerGrid.add(comboBoxes, 1, 4);

	}

	public void setupPasswordFields(GridPane grid) {

		// Password field
		password = new PasswordField();
		registerGrid.add(new Label("Password: "), 0, 7);
		password.setPromptText("Your Password");
		password.setPrefColumnCount(10);
		registerGrid.add(password, 1, 7);

		// Repeat Password field
		repeatPassword = new PasswordField();
		registerGrid.add(new Label("Confirm Password: "), 0, 8);
		repeatPassword.setPromptText("Your Password");
		repeatPassword.setPrefColumnCount(10);
		registerGrid.add(repeatPassword, 1, 8);
	}

	

	public void setupButtons() {
		
		// Add button to grid
		ButtonType button1 = new ButtonType("150,150,150",null,"Register",100,30);
		Button registerButton = new SetupButton().CreateButton(button1);
		registerGrid.add(registerButton, 0, 9);
		GridPane.setConstraints(registerButton, 0, 9, 2, 1, HPos.CENTER, VPos.CENTER);

		// Save button
		registerButton.setCursor(Cursor.HAND);
		registerButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {

				saveChanges();
			}

		});

		SlideContent.setupBackButton();
	}

	public boolean saveChanges() {

		boolean regSuccess;

		user = new User((String) username.getText());

		// Storing inputs from user
		userPassword = (String) password.getText();
		userFirstname = (String) firstname.getText();

		userLastName = (String) lastname.getText();

		userName = (String) username.getText();
		userEmail = (String) email.getText();

		userRepeatPassword = (String) repeatPassword.getText();

		dateOfBirth = comboDoBYear.getValue() + "-" + comboDoBMonth.getValue() + "-" + comboDoBDay.getValue();

		// Check if the entered email address is valid
		if (DataHandler.isValidEmailAddress(userEmail) == false) {
			userEmail = "";
		}

		// Check if the entered passwords address is valid
		// If valid, it is encrypted and stored
		if (DataHandler.passwordChecker(userPassword, userRepeatPassword) == false) {
			System.out.println("Password check failed");
			return false;
		} else {
			encryptedPassword = DataHandler.crypt(userPassword);
			user.password(encryptedPassword);

			// initialize user
			user.DOB(dateOfBirth);
			user.firstName(userFirstname);
			user.secondName(userLastName);
			user.email(userEmail);
			// no priviledges
			user.admin(false);
			user.landlord(false);

			// register user
			regSuccess = Database.userRegister(user);
			if (regSuccess == true) {
				// carry on to whatever interface you want, maybe login, or

				// go to users new profile
				root.getChildren().clear();
				slideID = LOGIN;
				SlideContent sc = new SlideContent();
				sc.createSlide();

				System.out.println("User: " + user.username
						+ " created successfully");
			}
		}
		return regSuccess;
	}
}