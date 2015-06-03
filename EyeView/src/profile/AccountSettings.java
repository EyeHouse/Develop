package profile;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import landlord.EditProperty;
import language.BadWordCheck;
import language.Translator;
import presenter.SlideContent;
import presenter.Window;
import button.ButtonType;
import button.SetupButton;
import database.DataHandler;
import database.Database;
import database.SQLFilter;
import database.User;

/**
 * This class creates the page within the user profile for editing account
 * details.
 * 
 * @version 2.5
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class AccountSettings extends Window {

	// Account Settings Static Variables
	private final int gridCellWidth = 50;
	private final int gridCellHeight = 25;

	// Account Settings Global Variables
	private GridPane grid = new GridPane();
	private TextField fieldFName, fieldLName, fieldUsername, fieldEmail,
			fieldSkypeUsername;
	private PasswordField fieldPassword = new PasswordField();
	private PasswordField fieldNewPassword = new PasswordField();
	private PasswordField fieldConfNewPassword = new PasswordField();
	private TextArea profileText = new TextArea();
	private Label labelPasswordIncorrect, labelNewPasswordInvalid,
			labelPasswordMismatch;
	private Label labelUsernameError, labelBadLanguage;
	private User currentUser;
	private ArrayList<ComboBox<String>> dateComboArray;

	/**
	 * Constructor method
	 */
	public AccountSettings() {

		currentUser = Database.getUser(currentUsername);

		setupGrid();
		setupInfo();
		setupPassword();
		setupProfileText();
		setupButtons();

		root.getChildren().add(grid);
	}

	/**
	 * Sets up grid layout object to contain user information.
	 */
	private void setupGrid() {

		// Set grid size and spacing in group.
		grid.setHgap(gridCellWidth);
		grid.setVgap(gridCellHeight);
		grid.relocate(320, 130);

		ColumnConstraints col1 = new ColumnConstraints();
		ColumnConstraints col2 = new ColumnConstraints();
		col1.setMinWidth(150);
		col2.setMinWidth(200);
		grid.getColumnConstraints().addAll(col1, col2);
	}

	/**
	 * Adds existing account information to grid.
	 */
	private void setupInfo() {

		// Setup row labels
		Label labelFName = new Label(Translator.translateText(languageIndex,
				"First Name"));
		Label labelLName = new Label(Translator.translateText(languageIndex,
				"Last Name"));
		Label labelUsername = new Label(Translator.translateText(languageIndex,
				"User Name"));
		Label labelEmail = new Label(Translator.translateText(languageIndex,
				"Email"));
		Label labelDoB = new Label(Translator.translateText(languageIndex,
				"Date of Birth"));
		Label labelSkypeUsername = new Label(Translator.translateText(
				languageIndex, "Skype Username"));

		// Setup username error label
		labelUsernameError = new Label();
		labelUsernameError.setVisible(false);

		// Setup labels with current account settings
		fieldFName = new TextField(currentUser.first_name);
		fieldLName = new TextField(currentUser.second_name);
		fieldUsername = new TextField(currentUser.username);
		fieldEmail = new TextField(currentUser.email);
		fieldSkypeUsername = new TextField(currentUser.skype);

		dateComboArray = EditProperty.setupDate(currentUser.DOB);
		HBox hBoxDoB = new HBox(10);
		hBoxDoB.getChildren().addAll(dateComboArray.get(0),
				dateComboArray.get(1), dateComboArray.get(2));

		grid.addColumn(0, labelFName, labelLName, labelUsername, labelEmail,
				labelDoB, labelSkypeUsername);
		grid.addColumn(1, fieldFName, fieldLName, fieldUsername, fieldEmail,
				hBoxDoB, fieldSkypeUsername);
		grid.add(labelUsernameError, 2, 2);
	}

	/**
	 * Add password labels and text areas to grid.
	 */
	private void setupPassword() {

		// Setup Password labels
		Label labelCurrentPassword = new Label(Translator.translateText(
				languageIndex, "Current Password"));
		Label labelNewPassword = new Label(Translator.translateText(
				languageIndex, "New Password"));
		Label labelConfNewPassword = new Label(Translator.translateText(
				languageIndex, "Confirm New Password"));

		// Setup password errors
		labelPasswordIncorrect = new Label(Translator.translateText(
				languageIndex, "Incorrect Password"));
		labelNewPasswordInvalid = new Label(Translator.translateText(
				languageIndex, "New Password Invalid"));
		labelPasswordMismatch = new Label(Translator.translateText(
				languageIndex, "New Passwords don't match"));
		labelPasswordIncorrect.setVisible(false);
		labelNewPasswordInvalid.setVisible(false);
		labelPasswordMismatch.setVisible(false);

		// Add objects to grid
		grid.addRow(6, labelCurrentPassword, fieldPassword,
				labelPasswordIncorrect);
		grid.addRow(7, labelNewPassword, fieldNewPassword,
				labelNewPasswordInvalid);
		grid.addRow(8, labelConfNewPassword, fieldConfNewPassword,
				labelPasswordMismatch);
		GridPane.setHalignment(labelPasswordIncorrect, HPos.LEFT);
		GridPane.setHalignment(labelNewPasswordInvalid, HPos.LEFT);
		GridPane.setHalignment(labelPasswordMismatch, HPos.LEFT);
	}

	/**
	 * Adds profile label and text area to grid.
	 */
	private void setupProfileText() {

		Label labelProfileText = new Label(Translator.translateText(
				languageIndex, "Profile"));
		labelBadLanguage = new Label(Translator.translateText(languageIndex,
				"Contains Bad Language"));

		labelBadLanguage.setVisible(false);
		// Load profile text area with current user profile and set size
		profileText.setText(currentUser.bio);
		profileText.setMaxHeight(gridCellHeight * 3);
		profileText.setPrefWidth(150);
		profileText.setWrapText(true);

		profileText.setPrefWidth(200);

		// Add profile label and text area to grid
		grid.addRow(9, labelProfileText, profileText, labelBadLanguage);
	}

	/**
	 * Adds apply and cancel buttons to grid.
	 */
	private void setupButtons() {

		ButtonType button1 = new ButtonType("166,208,255", null,
				Translator.translateText(languageIndex, "Apply Changes"), 150,
				30);
		Button buttonApply = new SetupButton().createButton(button1);

		ButtonType button2 = new ButtonType("166,208,255", null,
				Translator.translateText(languageIndex, "Cancel"), 100, 30);
		Button buttonCancel = new SetupButton().createButton(button2);

		ButtonType button3 = new ButtonType("166,208,255", null,
				Translator.translateText(languageIndex, "Delete Account"), 150,
				30);
		Button buttonDelete = new SetupButton().createButton(button3);

		HBox hBoxButtons = new HBox(40);

		// Add action listener to Apply button
		buttonApply.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				if (fieldNewPassword.getText().equals(
						fieldConfNewPassword.getText())) {
					// Save changes and return to profile if valid
					applyChanges();
				} else {
					labelPasswordMismatch.setVisible(true);
				}
			}
		});

		// Add action listener to Cancel button
		buttonCancel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				loadSlide(PROFILE);
			}
		});

		buttonDelete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				boolean check = Database.userDelete(currentUser.username);
				if (check) {
					currentUsername = null;
					loadSlide(LOGIN);
				} else {
					createWarningPopup("Failed to delete user.");
					dialogStage.show();
				}
			}
		});

		// Set button sizes
		buttonCancel.setPrefWidth(120);
		buttonApply.setPrefWidth(120);
		buttonDelete.setPrefWidth(120);

		// Add buttons to grid
		hBoxButtons.getChildren().addAll(buttonApply, buttonCancel,
				buttonDelete);
		hBoxButtons.setAlignment(Pos.CENTER);
		grid.add(hBoxButtons, 0, 10);
		GridPane.setConstraints(hBoxButtons, 0, 10, 2, 1, HPos.CENTER,
				VPos.CENTER);
		SlideContent.setupBackButton();
	}

	/**
	 * Sends account changes to the database.
	 */
	private void applyChanges() {

		String doB = dateComboArray.get(2).getValue() + "-"
				+ dateComboArray.get(1).getValue() + "-"
				+ dateComboArray.get(0).getValue();

		Database.userUpdate(currentUser, "first_name", null,
				fieldFName.getText());
		Database.userUpdate(currentUser, "second_name", null,
				fieldLName.getText());
		Database.userUpdate(currentUser, "email", null, fieldEmail.getText());
		Database.userUpdate(currentUser, "DOB", null, doB);
		Database.userUpdate(currentUser, "first_name", null,
				fieldFName.getText());
		Database.userUpdate(currentUser, "skype", null,
				fieldSkypeUsername.getText());

		checkBio();

		// Check username availability
		checkUsername();

		// Check password validity
		checkPassword();

		// Clear password inputs if not valid
		fieldPassword.setText("");
		fieldNewPassword.setText("");
		fieldConfNewPassword.setText("");
	}

	/**
	 * Checks the biography filed for any blacklisted words.
	 */
	private void checkBio() {

		BadWordCheck bwc = new BadWordCheck();
		if (bwc.containsBlackListedWords(profileText.getText())) {
			labelBadLanguage.setVisible(true);
			profileText.setText(bwc.highlightBlackListedWords(profileText
					.getText()));
		} else {
			labelBadLanguage.setVisible(false);
			Database.userUpdate(currentUser, "bio", null, profileText.getText());
		}
	}

	/**
	 * Checks whether new username is available or already taken.
	 */
	private void checkUsername() {

		// Skip check if username unchanged
		if (!fieldUsername.getText().equals(currentUser.username)
				&& (!fieldUsername.getText().equals(""))) {

			BadWordCheck bwc = new BadWordCheck();

			if (bwc.containsBlackListedWords(fieldUsername.getText())) {
				labelUsernameError.setText(Translator.translateText(
						languageIndex, "Contains Bad Language"));
				labelUsernameError.setVisible(true);
			} else if (SQLFilter.SQLWordCheck(fieldUsername.getText())) {
				labelUsernameError.setText(Translator.translateText(
						languageIndex, "Contains Invalid Characters"));
				labelUsernameError.setVisible(true);
			}
			// If username is unavailable
			else if (!Database.oneFieldCheck("username",
					fieldUsername.getText())) {
				// Send new username to database and remove error message
				Database.userUpdate(currentUser, "username", null,
						fieldUsername.getText());
				currentUser.username = fieldUsername.getText();
				labelUsernameError.setVisible(false);
			} else {
				// Show error message if username is unavailable
				labelUsernameError.setText(Translator.translateText(
						languageIndex, "Username Already Exists"));
				labelUsernameError.setVisible(true);
			}
		} else {
			// Remove error message if username is unchanged
			labelUsernameError.setVisible(false);
		}
	}

	/**
	 * Checks current password is correct and new password is valid.
	 */
	private void checkPassword() {

		/*
		 * Return to the profile if password not entered and there is no
		 * username error
		 */
		boolean passwordCorrect = false;
		if (!fieldPassword.getText().equals("")) {
			String hashPass = DataHandler.crypt((String) fieldPassword
					.getText());

			passwordCorrect = Database
					.login((String) currentUsername, hashPass);
		}

		if (fieldPassword.getText().equals("")) {
			if (!labelUsernameError.isVisible()
					&& !labelBadLanguage.isVisible()) {
				loadSlide(PROFILE);
			}

			/*
			 * Show password incorrect error if current password does not match
			 * stored user password
			 */
		} else if (!passwordCorrect) {
			labelPasswordIncorrect.setVisible(true);

			// Check new password validity if current password is correct
		} else if (passwordCorrect) {

			labelPasswordIncorrect.setVisible(false);

			/*
			 * If new password is not empty and matches the confirm password
			 * field, store new password in database
			 */
			if ((!fieldNewPassword.getText().equals(""))
					&& (fieldNewPassword.getText().equals(fieldConfNewPassword
							.getText()))) {
				String newHash = DataHandler.crypt((String) fieldNewPassword
						.getText());
				Database.userUpdate(currentUser, "password", null, newHash);

				// Return to profile if there is no username error
				if (!labelUsernameError.isVisible()
						&& !labelBadLanguage.isVisible()) {
					loadSlide(PROFILE);
				}

				// Show password invalid error if password fields do not match
			} else {
				labelNewPasswordInvalid.setVisible(true);
			}
		}
	}
}
