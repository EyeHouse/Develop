package profile;

/**
 * This class implements the EyeView Register Page
 * 
 * @version 2.2 20.04.15
 * @author EyeHouse
 * 
 * Copyright 2015 EyeHouse
 */

import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import landlord.EditProperty;
import language.BadWordCheck;
import language.Translator;
import presenter.SlideContent;
import button.ButtonType;
import button.SetupButton;
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
	Label topTitle, labelPassword, labelConfirm;

	private RadioButton buttonStudent = new RadioButton("Student");
	private RadioButton buttonLandlord = new RadioButton("Landlord");
	BadWordCheck bwc;
	ArrayList<ComboBox<String>> dateOfBirthCombo = new ArrayList<ComboBox<String>>();

	/**
	 * This method creates the register slide and calls the methods that will create it's elements
	 */
	public Register() {

		bwc = new BadWordCheck();
		Login.setBackground(true);
		Login.setWhiteBox(440, 530,true);
		setupGrid();
		setupTitle();
		setupTextFields(registerGrid);
		setupButtons();
		
		//Insert EyeHouse icon
		Image icon = new Image("file:./resources/icons/xxhdpi.png");
		ImageView iconView = new ImageView(icon);
		iconView.relocate(200, 260);
		root.getChildren().add(iconView);
		
		//Extra information for the user
		Label requiredField = new Label(Translator.translateText(languageIndex,"*Required field."));
		Label passwordRequirements = new Label(
				Translator.translateText(languageIndex,"**Password should contain at least one upper case letter, one lower case letter and one digit. "
						+ " It should have between 6 and 20 characters."));
		passwordRequirements.setWrapText(true);
		//Create box to contain the previous labels
		VBox vbox = new VBox(5);
		vbox.setPrefWidth(400);
		vbox.relocate(380, 665);
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(requiredField, passwordRequirements);
		root.getChildren().addAll(topTitle, registerGrid, vbox);

	}
	
	/**
	 * This method sets up the spacing between the elements in rows and in columns 
	 */
	public void setupGrid() {

		registerGrid.setVgap(30);
		registerGrid.setHgap(30);
		registerGrid.setMinWidth(390);
		registerGrid.setMaxWidth(390);
		registerGrid.relocate(375, 110);
	}
	
	/**
	 * This method creates and places the title of the page
	 */
	public void setupTitle() {

		topTitle = new Label(Translator.translateText(languageIndex, "Register"));
		topTitle.setTextFill(Color.web("#162252FF"));
		topTitle.setFont(new Font(35));
		topTitle.relocate(505, 65);
	}

	/**
	 * This method sets up the text fields, the boxes for the date of birth and the buttons for account type
	 */
	public void setupTextFields(GridPane grid) {
		HBox hBoxAccountType = new HBox(10);
		ToggleGroup group = new ToggleGroup();

		// username field
		username = new TextField();
		username.setPromptText(Translator.translateText(languageIndex, "Username"));
		username.setPrefColumnCount(10);
		Label chooseNameLabel = new Label(Translator.translateText(languageIndex, "*Choose a username:"));
		GridPane.setHalignment(chooseNameLabel, HPos.RIGHT);
		registerGrid.addRow(1, chooseNameLabel, username);
		GridPane.setHgrow(username, Priority.ALWAYS);

		// First Name field
		firstname = new TextField();
		firstname.setPromptText("Joe");
		firstname.setPrefColumnCount(10);
		Label nameLabel = new Label(Translator.translateText(languageIndex, "*First name:"));
		GridPane.setHalignment(nameLabel, HPos.RIGHT);
		registerGrid.addRow(2, nameLabel, firstname);

		// Last Name field
		lastname = new TextField();
		lastname.setPromptText("Bloggs");
		lastname.setPrefColumnCount(10);
		Label lastNameLabel = new Label (Translator.translateText(languageIndex, "*Last name:"));
		GridPane.setHalignment(lastNameLabel, HPos.RIGHT);
		registerGrid.addRow(3, lastNameLabel, lastname);

		setupPasswordFields(registerGrid);

		// Date of birth field
		dateOfBirthCombo = EditProperty.setupDate("2015-01-01");
		HBox comboBoxes = new HBox(10);
		Label birthLabel = new Label(Translator.translateText(languageIndex, "*Date of Birth:"));
		GridPane.setHalignment(birthLabel, HPos.RIGHT);
		comboBoxes.getChildren().addAll(dateOfBirthCombo.get(0),
				dateOfBirthCombo.get(1), dateOfBirthCombo.get(2));
		registerGrid.addRow(6, birthLabel, comboBoxes);

		// Account Type radio buttons
		buttonStudent.setToggleGroup(group);
		buttonLandlord.setToggleGroup(group);
		Label accountLabel = new Label(Translator.translateText(languageIndex, "*Account Type:"));
		GridPane.setHalignment(accountLabel, HPos.RIGHT);
		hBoxAccountType.getChildren().addAll(buttonStudent, buttonLandlord);
		registerGrid.addRow(7, accountLabel, hBoxAccountType);

		// Email field
		email = new TextField();
		email.setPromptText("example@mail.com");
		email.setPrefColumnCount(10);
		Label emailLabel = new Label(Translator.translateText(languageIndex, "*Email:"));
		GridPane.setHalignment(emailLabel, HPos.RIGHT);
		registerGrid.addRow(8, emailLabel, email);

		// Skype field
		skypeID = new TextField();
		skypeID.setPromptText("jBloggs1");
		skypeID.setPrefColumnCount(10);
		Label skypeLabel = new Label(Translator.translateText(languageIndex, "Skype Username:"));
		GridPane.setHalignment(skypeLabel, HPos.RIGHT);
		registerGrid.addRow(9, skypeLabel, skypeID);
		
		username.textProperty().addListener(
				new ChangeListener<String>() {
					@Override
					public void changed(
							final ObservableValue<? extends String> observable,
							String oldValue, String newValue) {
						if (newValue.length() >= 15) {
							newValue = oldValue;
							username.setText(newValue);
						} 
					}
				});
		
		firstname.textProperty().addListener(
				new ChangeListener<String>() {
					@Override
					public void changed(
							final ObservableValue<? extends String> observable,
							String oldValue, String newValue) {
						if (newValue.length() >= 21) {
							newValue = oldValue;
							firstname.setText(newValue);
						} 
					}
				});
		
		lastname.textProperty().addListener(
				new ChangeListener<String>() {
					@Override
					public void changed(
							final ObservableValue<? extends String> observable,
							String oldValue, String newValue) {
						if (newValue.length() >= 21) {
							newValue = oldValue;
							lastname.setText(newValue);
						} 
					}
				});
		
		email.textProperty().addListener(
				new ChangeListener<String>() {
					@Override
					public void changed(
							final ObservableValue<? extends String> observable,
							String oldValue, String newValue) {
						if (newValue.length() >= 41) {
							newValue = oldValue;
							email.setText(newValue);
						} 
					}
				});
				
		skypeID.textProperty().addListener(
				new ChangeListener<String>() {
					@Override
					public void changed(
							final ObservableValue<? extends String> observable,
							String oldValue, String newValue) {
						if (newValue.length() >= 21) {
							newValue = oldValue;
							skypeID.setText(newValue);
						} 
					}
				});
		
	}
	
	/**
	 * This method sets up the password field and the confirm you password field for the user to fill
	 */
	public void setupPasswordFields(GridPane grid) {

		// Password field
		password = new PasswordField();
		Label passwordLabel = new Label(Translator.translateText(languageIndex, "*Create Password :"));
		GridPane.setHalignment(passwordLabel, HPos.RIGHT);
		registerGrid.add(passwordLabel, 0, 4);
		password.setPromptText(Translator.translateText(languageIndex,"Your Password"));
		password.setPrefColumnCount(10);
		registerGrid.add(password, 1, 4);

		// Repeat Password field
		repeatPassword = new PasswordField();
		Label confirmPasswordLabel = new Label(Translator.translateText(languageIndex, "*Confirm Password :"));
		GridPane.setHalignment(confirmPasswordLabel, HPos.RIGHT);
		registerGrid.add(confirmPasswordLabel, 0, 5);
		repeatPassword.setPromptText(Translator.translateText(languageIndex,"Your Password"));
		repeatPassword.setPrefColumnCount(10);
		registerGrid.add(repeatPassword, 1, 5);
	}
	
	/**
	 * This method sets up the buttons on the side menu and the save button, which calls the function to login the user
	 */
	public void setupButtons() {

		// Add button to grid
		ButtonType button1 = new ButtonType("166,208,255", null,
				Translator.translateText(languageIndex, Translator.translateText(languageIndex,"Register")), 100, 30);
		Button registerButton = new SetupButton().CreateButton(button1);
		registerGrid.add(registerButton, 0, 10);
		GridPane.setConstraints(registerButton, 0, 10, 2, 1, HPos.CENTER,
				VPos.CENTER);

		// Save button
		registerButton.setCursor(Cursor.HAND);
		registerButton.setOnAction(new saveChanges());

		SlideContent.setupBackButton();
	}

	/**
	 * This method saves the changes and checks if the information entered in each field is valid. 
	 * If they all are the user is registered and their profile page is displayed 
	 */
	public class saveChanges implements EventHandler<ActionEvent> {

		public void handle(ActionEvent arg0) {

			boolean regSuccess = false;

			// Check there is no rude words in the text fields
			if ((bwc.containsBlackListedWords(username.getText()) == true)
					|| (bwc.containsBlackListedWords(firstname.getText()) == true)
					|| (bwc.containsBlackListedWords(lastname.getText()) == true)) {
				createWarningPopup("Inappropiate Language");
				dialogStage.show();
			} else if ((!buttonStudent.isSelected()) && (!buttonLandlord.isSelected())) {
				createWarningPopup("Select an account type");
				dialogStage.show();
			} else if (Database.getUser(username.getText()) != null) {
				createWarningPopup("Username is already registered");
				dialogStage.show();
			} 			
			// If there are no rude words in the text field it checks all the information is correct
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
				else{
				// Check if the entered passwords address is valid
				// If valid, it is encrypted and stored
				if (DataHandler.passwordChecker(password.getText(),
						repeatPassword.getText()) == false) {
					System.out.println(Translator.translateText(languageIndex,"Password check failed"));
					createWarningPopup(Translator.translateText(languageIndex,"Incorrect Password. Try Again"));
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
					// No privileges
					user.admin(false);

					user.landlord(buttonLandlord.isSelected());

					// Register user
					regSuccess = Database.userRegister(user);

					// If the registration is successful log the user in
					if (regSuccess == true) {
						// log the new user in
						User user = Database.getUser(username.getText());

						// Go to users new profile
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
	}
}
