package Profile;

/**
 * This class implements the EyeView Register Page
 * 
 * @version 2.2 20.04.15
 * @author EyeHouse
 * 
 * Copyright 2015 EyeHouse
 */
import java.awt.Insets;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import landlord.EditProperty;
import language.BadWordCheck;
import language.Translate;
import presenter.SlideContent;
import Button.ButtonType;
import Button.SetupButton;
import database.DataHandler;
import database.Database;
import database.User;

public class Register extends presenter.Window {

	public String encryptedPassword, dateOfBirth;
	public User user = null;

	private TextField username, firstname, lastname, email, skypeID;
	private PasswordField password;
	private PasswordField repeatPassword;
	private GridPane registerGrid = new GridPane();
	Label topTitle;

	private RadioButton buttonStudent = new RadioButton("Student");
	private RadioButton buttonLandlord = new RadioButton("Landlord");
	BadWordCheck bwc;
	ArrayList<ComboBox<String>> dateOfBirthCombo = new ArrayList<ComboBox<String>>();

	public static void main(String[] args) {
		Database.dbConnect();
		launch(args);
	}

	public Register() {

		bwc = new BadWordCheck();
		setupGrid();
		setupTitle();
		setupTextFields(registerGrid);
		setupButtons();

		Label required = new Label("*Required field.");
		Label passwordRequirements = new Label(
				"**Password should contain at least an upper case letter, a lower case letter and a digit. "
						+ " It should have between 6 and 20 characters.");
		passwordRequirements.setWrapText(true);
		VBox vbox = new VBox(0);
		vbox.setPrefWidth(400);
		vbox.relocate(370, 685);
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(required, passwordRequirements);
		root.getChildren().addAll(topTitle, registerGrid, vbox);

	}

	public void setupGrid() {

		registerGrid.setVgap(30);
		registerGrid.setHgap(30);
		registerGrid.relocate(375, 130);
	}

	public void setupTitle() {

		topTitle = new Label(Translate.translateText(languageIndex, "Register"));
		topTitle.setTextFill(Color.web("#162252FF"));
		topTitle.setFont(new Font(35));
		topTitle.relocate(470, 90);

	}

	public void setupTextFields(GridPane grid) {
		HBox hBoxAccountType = new HBox(10);
		ToggleGroup group = new ToggleGroup();

		// username field
		username = new TextField();
		username.setPromptText("Username");
		username.setPrefColumnCount(10);
		registerGrid.addRow(1, new Label("*Choose a username"), username);

		// First Name field
		firstname = new TextField();
		firstname.setPromptText("Joe");
		firstname.setPrefColumnCount(10);
		registerGrid.addRow(2, new Label("First name:"), firstname);

		// Last Name fieldzz
		lastname = new TextField();
		lastname.setPromptText("Bloggs");
		lastname.setPrefColumnCount(10);
		registerGrid.addRow(3, new Label("Last name:"), lastname);

		setupPasswordFields(registerGrid);

		// Date of birth field
		dateOfBirthCombo = EditProperty.setupDate("2015-01-01");
		HBox comboBoxes = new HBox(30);
		comboBoxes.getChildren().addAll(dateOfBirthCombo.get(0),
				dateOfBirthCombo.get(1), dateOfBirthCombo.get(2));
		registerGrid.addRow(6, new Label("*Date of Birth:"), comboBoxes);

		// Account Type radio buttons
		buttonStudent.setToggleGroup(group);
		buttonLandlord.setToggleGroup(group);
		hBoxAccountType.getChildren().addAll(buttonStudent, buttonLandlord);
		registerGrid.addRow(7, new Label("*Account Type:"), hBoxAccountType);

		// Email field
		email = new TextField();
		email.setPromptText("example@mail.com");
		email.setPrefColumnCount(10);
		registerGrid.addRow(8, new Label("*Email:"), email);

		// Skype field
		skypeID = new TextField();
		skypeID.setPromptText("jBloggs1");
		skypeID.setPrefColumnCount(10);
		registerGrid.addRow(9, new Label("Skype Username:"), skypeID);
	}

	public void setupPasswordFields(GridPane grid) {

		// Password field
		password = new PasswordField();
		registerGrid.add(new Label("*Create Password: "), 0, 4);
		password.setPromptText("Your Password");
		password.setPrefColumnCount(10);
		registerGrid.add(password, 1, 4);

		// Repeat Password field
		repeatPassword = new PasswordField();
		registerGrid.add(new Label("*Confirm Password: "), 0, 5);
		repeatPassword.setPromptText("Your Password");
		repeatPassword.setPrefColumnCount(10);
		registerGrid.add(repeatPassword, 1, 5);
	}

	public void setupButtons() {

		// Add button to grid
		ButtonType button1 = new ButtonType("166,208,255", null,
				Translate.translateText(languageIndex, "Register"), 100, 30);
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

			boolean regSuccess = false;

			// Check username
			if ((bwc.containsBlackListedWords(username.getText()) == true)
					|| (bwc.containsBlackListedWords(firstname.getText()) == true)
					|| (bwc.containsBlackListedWords(lastname.getText()) == true)) {
				createWarningPopup("Rude Word Detected.");
				dialogStage.show();
				System.out.println("Rude word detected.");
			}

			else {
				user = new User(username.getText());
				// Get date of birth
				dateOfBirth = dateOfBirthCombo.get(2).getValue() + "-"
						+ dateOfBirthCombo.get(1).getValue() + "-"
						+ dateOfBirthCombo.get(0).getValue();

				String userEmail = email.getText();
				// Check if the entered email address is valid
				if (DataHandler.isValidEmailAddress(email.getText()) == false) {
					userEmail = "";
					createWarningPopup("Invalid Email.");
					dialogStage.show();
				}

				// Check if the entered passwords address is valid
				// If valid, it is encrypted and stored
				if (DataHandler.passwordChecker(password.getText(),
						repeatPassword.getText()) == false) {
					System.out.println("Password check failed");
					createWarningPopup(" Incorrect Password. Try Again");
					dialogStage.show();
				} else {
					encryptedPassword = DataHandler.crypt(password.getText());
					user.password(encryptedPassword);

					// Initialize user
					user.DOB(dateOfBirth);
					user.firstName(firstname.getText());
					user.secondName(lastname.getText());
					user.email(userEmail);

					// If there is a skype ID
					if (!skypeID.getText().equals("")) {
						user.skype(skypeID.getText());
					}
					// no privileges
					user.admin(false);

					user.landlord(buttonLandlord.isSelected());
				
				// register user
				regSuccess = Database.userRegister(user);
				}
			

			// If the registration is successful log the user in
			if (regSuccess == true) {
				// log the new user in
				User user = Database.getUser(username.getText());

				// go to users new profile
				firstLogin = true;
				currentUsername = user.username;
				viewedUsername = user.username;
				loadSlide(PROFILE);

				System.out.println("User: " + user.username
						+ " created successfully");
			} else {
				System.out.println("Registration failed.");
			}
			}
		}
	}
}
