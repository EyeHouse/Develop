package Profile;

//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.text.SimpleDateFormat;
//import java.util.Locale;

import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import landlord.EditProperty;
import presenter.SlideContent;
import Button.ButtonType;
import Button.SetupButton;
import database.DataHandler;
import database.Database;
import database.User;

public class Register extends presenter.Window {

	public String encryptedPassword, dateOfBirth;
	public User user = null;
	// private static MessageDigest digester;

	private TextField username, firstname, lastname, email, skypeID;
	private PasswordField password;
	private PasswordField repeatPassword;
	private GridPane registerGrid = new GridPane();

	private RadioButton buttonStudent = new RadioButton("Student");
	private RadioButton buttonLandlord = new RadioButton("Landlord");

	ArrayList<ComboBox<String>> dateOfBirthCombo = new ArrayList<ComboBox<String>>();

	public static void main(String[] args) {
		Database.dbConnect();
		launch(args);
	}

	public Register() {

		setupGrid();
		setupTitle();
		setupTextFields(registerGrid);
		setupPasswordFields(registerGrid);
		setupButtons();

		root.getChildren().add(registerGrid);

	}

	public void setupGrid() {

		registerGrid.setVgap(30);
		registerGrid.setHgap(30);
		registerGrid.relocate(250, 90);
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
		registerGrid.addRow(1, new Label("Enter your username:"), username);

		// First Name field
		firstname = new TextField();
		firstname.setPromptText("Joe");
		firstname.setPrefColumnCount(10);
		registerGrid.addRow(2, new Label("First name:"), firstname);

		// Last Name field
		lastname = new TextField();
		lastname.setPromptText("Bloggs");
		lastname.setPrefColumnCount(10);
		registerGrid.addRow(3, new Label("Last name:"), lastname);

		// Account Type radio buttons
		buttonStudent.setToggleGroup(group);
		buttonLandlord.setToggleGroup(group);
		hBoxAccountType.getChildren().addAll(buttonStudent, buttonLandlord);
		registerGrid.addRow(5, new Label("Account Type:"), hBoxAccountType);

		// Email field
		email = new TextField();
		email.setPromptText("example@mail.com");
		email.setPrefColumnCount(10);
		registerGrid.addRow(6, new Label("Email:"), email);

		skypeID = new TextField();
		skypeID.setPromptText("jBloggs1");
		skypeID.setPrefColumnCount(10);
		registerGrid.addRow(7, new Label("Skype Username:"), skypeID);

		dateOfBirthCombo = EditProperty.setupDate("2015-01-01");
		HBox comboBoxes = new HBox(30);
		comboBoxes.getChildren().addAll(dateOfBirthCombo.get(0),
				dateOfBirthCombo.get(1), dateOfBirthCombo.get(2));
		registerGrid.add(comboBoxes, 1, 4);
	}

	public void setupPasswordFields(GridPane grid) {

		// Password field
		password = new PasswordField();
		registerGrid.add(new Label("Password: "), 0, 8);
		password.setPromptText("Your Password");
		password.setPrefColumnCount(10);
		registerGrid.add(password, 1, 8);
		

		// Repeat Password field
		repeatPassword = new PasswordField();
		registerGrid.add(new Label("Confirm Password: "), 0, 9);
		repeatPassword.setPromptText("Your Password");
		repeatPassword.setPrefColumnCount(10);
		registerGrid.add(repeatPassword, 1, 9);
	}

	public void setupButtons() {

		// Add button to grid
		ButtonType button1 = new ButtonType("150,150,150", null, "Register",
				100, 30);
		Button registerButton = new SetupButton().CreateButton(button1);
		registerGrid.add(registerButton, 0, 10);
		GridPane.setConstraints(registerButton, 0, 10, 2, 1, HPos.CENTER,
				VPos.CENTER);

		// Save button
		registerButton.setCursor(Cursor.HAND);
		registerButton.setOnAction(new saveChanges());

		SlideContent.setupBackButton();
	}

	public class saveChanges implements EventHandler<ActionEvent> {

		public void handle(ActionEvent arg0) {

			boolean regSuccess;

			user = new User(username.getText());

			dateOfBirth = dateOfBirthCombo.get(2).getValue() + "-"
					+ dateOfBirthCombo.get(1).getValue() + "-"
					+ dateOfBirthCombo.get(0).getValue();

			String userEmail = email.getText();
			// Check if the entered email address is valid
			if (DataHandler.isValidEmailAddress(email.getText()) == false) {
				userEmail = "";
			}

			// Check if the entered passwords address is valid
			// If valid, it is encrypted and stored
			if (DataHandler.passwordChecker(password.getText(), repeatPassword.getText()) == false) {
				System.out.println("Password check failed");
			} else {
				encryptedPassword = DataHandler.crypt(password.getText());
				user.password(encryptedPassword);

				// initialize user
				user.DOB(dateOfBirth);
				user.firstName(firstname.getText());
				user.secondName(lastname.getText());
				user.email(userEmail);
				// no priviledges
				user.admin(false);
				
				user.landlord(buttonLandlord.isSelected());

				// register user
				regSuccess = Database.userRegister(user);
				
				if (regSuccess == true) {
					// carry on to whatever interface you want, maybe login, or

					// register user
					regSuccess = Database.userRegister(user);
					if (regSuccess == true) {

						System.out.println("User: " + user.username
								+ " created successfully");
					}
					// log the new user in
					User user = Database.getUser(username.getText());
					currentUsername = username.getText();
					try {
						user.printUser();
					} catch (IOException e) {
						e.printStackTrace();
					}
					// go to users new profile
					firstLogin = true;
					currentUsername = user.username;
					loadSlide(PROFILE);

					System.out.println("User: " + user.username
							+ " created successfully");
				}
			}
		}
	}
}
