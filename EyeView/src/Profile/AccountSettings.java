package Profile;

/*
 * AccountSettings.java
 * 
 * Version: 1.9
 * 
 * Copyright:
 */

import Profile.UserType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class AccountSettings {

	/* Account Settings Static Variables */
	private static final int gridCellWidth = 50;
	private static final int gridCellHeight = 30;
	private static final double xStart = 0.5;
	private static final double yStart = 0.05;

	/* Account Settings Global Variables */
	private Group screenGroup;
	private GridPane grid = new GridPane();
	private double xResolution;
	private double yResolution;
	private TextField fieldFName, fieldLName, fieldUsername, fieldEmail;
	private PasswordField fieldPassword = new PasswordField();
	private PasswordField fieldNewPassword = new PasswordField();
	private PasswordField fieldConfNewPassword = new PasswordField();
	private TextArea profileText = new TextArea();
	private ComboBox<String> comboDoBDay = new ComboBox<String>();
	private ComboBox<String> comboDoBMonth = new ComboBox<String>();
	private ComboBox<String> comboDoBYear = new ComboBox<String>();
	private RadioButton buttonStudent = new RadioButton("Student");
	private RadioButton buttonLandlord = new RadioButton("Landlord");
	private Label labelPasswordIncorrect;
	private Label labelNewPasswordInvalid;
	private Label labelUsernameAvailability;
	private UserType currentUser;
	private String username2 = new String("JSmith2");

	/* Account Settings constructor */
	public AccountSettings(Group group, double xresolution, double yresolution) {
		screenGroup = group;
		xResolution = xresolution;
		yResolution = yresolution;
	}

	/* Account Settings Methods */

	/* Open account settings of user input */
	public void OpenAccountSettings(UserType user) {

		currentUser = user;

		setupGrid();
		setupInfo();
		SetupPassword();
		SetupProfileText();
		SetupButtons();

		screenGroup.getChildren().add(grid);
	}

	/* Setup grid layout object to contain user information */
	public void setupGrid() {

		// Set grid size and spacing in group.
		grid.setHgap(gridCellWidth);
		grid.setVgap(gridCellHeight);
		grid.setPadding(new Insets(yResolution * yStart, xResolution * xStart,
				100, 100));
	}

	/* Add existing account information to grid */
	public void setupInfo() {
		HBox hBoxDoB = new HBox(5);
		HBox hBoxAccountType = new HBox(30);
		ToggleGroup group = new ToggleGroup();

		// Setup row labels
		Label labelFName = new Label("First Name");
		Label labelLName = new Label("Last Name");
		Label labelUsername = new Label("User Name");
		Label labelEmail = new Label("Email");
		Label labelDoB = new Label("Date of Birth");
		Label labelAccountType = new Label("Account Type");

		// Setup username error label
		labelUsernameAvailability = new Label("Username already exists");
		labelUsernameAvailability.setVisible(false);

		// Setup labels with current account settings
		fieldFName = new TextField(currentUser.fName);
		fieldLName = new TextField(currentUser.lName);
		fieldUsername = new TextField(currentUser.username);
		fieldEmail = new TextField(currentUser.email);

		// Group radio buttons
		buttonStudent.setToggleGroup(group);
		buttonLandlord.setToggleGroup(group);

		// Setup radio buttons with current account type
		if (currentUser.landlord)
			buttonLandlord.setSelected(true);
		else
			buttonStudent.setSelected(true);

		SetupDoB();

		hBoxDoB.getChildren().addAll(comboDoBDay, comboDoBMonth, comboDoBYear);
		hBoxAccountType.getChildren().addAll(buttonStudent, buttonLandlord);

		grid.addColumn(0, labelFName, labelLName, labelUsername, labelEmail,
				labelDoB, labelAccountType);
		grid.addColumn(1, fieldFName, fieldLName, fieldUsername, fieldEmail,
				hBoxDoB, hBoxAccountType);
		grid.add(labelUsernameAvailability, 2, 2);
	}

	/* Add Date of Birth comboboxes to grid */
	public void SetupDoB() {

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

		// Set the selected item of the combo boxes to the current account
		// settings
		int doBDay = Integer.valueOf(currentUser.doB.substring(0, 2), 10)
				.intValue();
		int doBMonth = Integer.valueOf(currentUser.doB.substring(3, 5), 10)
				.intValue();
		int doBYear = Integer.valueOf(currentUser.doB.substring(6, 10), 10)
				.intValue();
		comboDoBDay.getSelectionModel().select(doBDay - 1);
		comboDoBMonth.getSelectionModel().select(doBMonth - 1);
		comboDoBYear.getSelectionModel().select(2015 - doBYear);

		// Add change listener to month combobox
		comboDoBMonth.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String previousValue,
					String newValue) {
				// Add days 29, 30 and 31 to combobox if previous month selected
				// was February
				if (previousValue.equals("02")) {
					comboDoBDay.getItems().add(String.format("%02d", 29));
					comboDoBDay.getItems().add(String.format("%02d", 30));
					comboDoBDay.getItems().add(String.format("%02d", 31));
					// Remove days 29, 30 and 31 if new month selected is
					// February
				} else if (newValue.equals("02")) {
					comboDoBDay.getItems().remove(30);
					comboDoBDay.getItems().remove(29);
					comboDoBDay.getItems().remove(28);
				}
			}
		});
	}

	/* Add password labels and text areas to grid */
	public void SetupPassword() {

		// Setup Password labels
		Label labelCurrentPassword = new Label("Current Password");
		Label labelNewPassword = new Label("New Password");
		Label labelConfNewPassword = new Label("Confirm New Password");

		// Setup password errors
		labelPasswordIncorrect = new Label("Incorrect Password");
		labelNewPasswordInvalid = new Label("New Password Invalid");
		labelPasswordIncorrect.setVisible(false);
		labelNewPasswordInvalid.setVisible(false);

		// Add objects to grid
		grid.addRow(6, labelCurrentPassword, fieldPassword,
				labelPasswordIncorrect);
		grid.addRow(7, labelNewPassword, fieldNewPassword,
				labelNewPasswordInvalid);
		grid.addRow(8, labelConfNewPassword, fieldConfNewPassword);
	}

	/* Add profile label and text area to grid */
	public void SetupProfileText() {
		Label labelProfileText = new Label("Profile");

		// Load profile text area with current user profile and set size
		profileText.setText(currentUser.profileText);
		profileText.setMaxHeight(gridCellHeight * 3);
		profileText.setMaxWidth(250);
		
		// Add profile label and text area to grid
		grid.addRow(9, labelProfileText, profileText);
	}
	
	/* Add apply and cancel buttons to grid*/
	public void SetupButtons() {
		Button buttonApply = new Button("Apply Changes");
		Button buttonCancel = new Button("Cancel");
		HBox hBoxButtons = new HBox(40);
		
		// Add action listener to Apply button
		buttonApply.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				// Save changes and return to profile if valid
				ApplyChanges();
			}
		});
		
		// Add action listener to Cancel button
		buttonCancel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				// Return to profile page
				OpenProfile();
			}
		});
		
		// Set button sizes
		buttonCancel.setPrefWidth(100);
		buttonApply.setPrefWidth(100);

		// Add buttons to grid
		hBoxButtons.getChildren().addAll(buttonApply, buttonCancel);
		hBoxButtons.setAlignment(Pos.CENTER);
		grid.add(hBoxButtons, 0, 10);
		GridPane.setConstraints(hBoxButtons, 0, 10, 2, 1, HPos.CENTER,
				VPos.CENTER);
	}

	/* Send account changes to database(WHEN IMPLEMENTED FULLY) */
	private void ApplyChanges() {
		currentUser.fName = fieldFName.getText();
		currentUser.lName = fieldLName.getText();
		currentUser.email = fieldEmail.getText();
		currentUser.doB = comboDoBDay.getValue() + "/"
				+ comboDoBMonth.getValue() + "/" + comboDoBYear.getValue();
		currentUser.landlord = buttonLandlord.isSelected();
		currentUser.profileText = profileText.getText();
		
		// Check username availability
		CheckUsername();
		
		// Check password validity
		CheckPassword();
		
		// Clear password inputs if not valid
		fieldPassword.setText("");
		fieldNewPassword.setText("");
		fieldConfNewPassword.setText("");
	}

	/* Check whether new username is available */
	private void CheckUsername() {
		// Skip check if username unchanged
		if (!fieldUsername.getText().equals(currentUser.username)) {
			// CheckUsernameAvailability();
			// If username is unavailable
			if (!fieldUsername.getText().equals(username2)) { //TESTING ONLY
				// Send new username to database and remove error message
				currentUser.username = fieldUsername.getText();
				labelUsernameAvailability.setVisible(false);
			} else {
				// Show error message if username is unavailable
				labelUsernameAvailability.setVisible(true);
			}
		} else {
			// Remove error message if username is unchanged
			labelUsernameAvailability.setVisible(false);
		}
	}
	
	/* Check current password is correct and new password is valid*/
	private void CheckPassword() {
		
		// Return to profile if password is not entered and there is
		// no username error
		if (fieldPassword.getText().equals("")) {
			if (!labelUsernameAvailability.isVisible()) {
				OpenProfile();
			}
			
		// Show password incorrect error if current password does not match
		// stored user password.
		} else if (!fieldPassword.getText().equals(currentUser.password)) {
			labelPasswordIncorrect.setVisible(true);
			
		// Check new password validity if current password is correct
		} else if (fieldPassword.getText().equals(currentUser.password)) {
			labelPasswordIncorrect.setVisible(false);
			
			// If new password is not null and matches the confirm password
			// field, store new password in database
			if ((!fieldNewPassword.getText().equals(""))
					&& (fieldNewPassword.getText().equals(fieldConfNewPassword
							.getText()))) {
				currentUser.password = fieldNewPassword.getText();
				
				// Return to profile if there is no username error
				if (!labelUsernameAvailability.isVisible()) {
					OpenProfile();
				}
				
			// Show new password invalid error if new password fields do not match 
			} else {
				labelNewPasswordInvalid.setVisible(true);
			}
		}
	}

	/* Return to the profile page */
	private void OpenProfile() {
		
		// Instantiate a new ProfileViewer object and open with current user
		screenGroup.getChildren().clear();
		ProfileViewer profile = new ProfileViewer(screenGroup, xResolution,
				yResolution);
		profile.OpenProfile(currentUser);
	}
}