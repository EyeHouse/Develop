package Profile;

/*
 * AccountSettings.java
 * 
 * Version: 1.9
 * 
 * Copyright:
 */

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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import javax.swing.JOptionPane;

import landlord.EditProperty;
import language.BadWordCheck;
import presenter.SlideContent;
import Button.ButtonType;
import Button.SetupButton;
import database.Database;
import database.User;

public class AccountSettings extends presenter.Window{

	/* Account Settings Static Variables */
	private static final int gridCellWidth = 50;
	private static final int gridCellHeight = 30;

	/* Account Settings Global Variables */
	private GridPane grid = new GridPane();
	private TextField fieldFName, fieldLName, fieldUsername, fieldEmail;
	private PasswordField fieldPassword = new PasswordField();
	private PasswordField fieldNewPassword = new PasswordField();
	private PasswordField fieldConfNewPassword = new PasswordField();
	private TextArea profileText = new TextArea();
	private RadioButton buttonStudent = new RadioButton("Student");
	private RadioButton buttonLandlord = new RadioButton("Landlord");
	private Label labelPasswordIncorrect, labelNewPasswordInvalid;
	private Label labelUsernameError, labelBadLanguage;
	private User currentUser;
	ArrayList<ComboBox<String>> dateComboArray;

	/* Account Settings Methods */

	/* Open account settings of user input */
	public AccountSettings() {

		currentUser = Database.getUser(currentUsername);

		setupGrid();
		setupInfo();
		SetupPassword();
		SetupProfileText();
		SetupButtons();

		root.getChildren().add(grid);
	}

	/* Setup grid layout object to contain user information */
	public void setupGrid() {

		// Set grid size and spacing in group.
		grid.setHgap(gridCellWidth);
		grid.setVgap(gridCellHeight);
		grid.relocate(220, 80);
		
		ColumnConstraints col1 = new ColumnConstraints();
		ColumnConstraints col2 = new ColumnConstraints();
		col1.setMinWidth(100);
		col2.setMinWidth(200);
		grid.getColumnConstraints().addAll(col1, col2);
	}

	/* Add existing account information to grid */
	public void setupInfo() {
		
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
		labelUsernameError = new Label();
		labelUsernameError.setVisible(false);

		// Setup labels with current account settings
		fieldFName = new TextField(currentUser.first_name);
		fieldLName = new TextField(currentUser.second_name);
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

		dateComboArray = EditProperty.setupDate(currentUser.DOB);
		HBox hBoxDoB = new HBox(10);
		hBoxDoB.getChildren().addAll(dateComboArray.get(0),
				dateComboArray.get(1), dateComboArray.get(2));

		hBoxAccountType.getChildren().addAll(buttonStudent, buttonLandlord);

		grid.addColumn(0, labelFName, labelLName, labelUsername, labelEmail,
				labelDoB, labelAccountType);
		grid.addColumn(1, fieldFName, fieldLName, fieldUsername, fieldEmail,
				hBoxDoB, hBoxAccountType);
		grid.add(labelUsernameError, 2, 2);
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
		labelBadLanguage = new Label("Contains Bad Language");
		
		labelBadLanguage.setVisible(false);
		// Load profile text area with current user profile and set size
		profileText.setText(currentUser.bio);
		profileText.setMaxHeight(gridCellHeight * 3);
		profileText.setPrefWidth(150);
		profileText.setWrapText(true);
		
		profileText.setPrefWidth(200);

		// Add profile label and text area to grid
		grid.addRow(9, labelProfileText, profileText,labelBadLanguage);
	}

	/* Add apply and cancel buttons to grid */
	public void SetupButtons() {
		
		ButtonType button1 = new ButtonType("150,150,150",null,"Apply Changes",150,30);
		Button buttonApply = new SetupButton().CreateButton(button1);
		
		ButtonType button2 = new ButtonType("150,150,150",null,"Cancel",100,30);
		Button buttonCancel = new SetupButton().CreateButton(button2);
		
		ButtonType button3 = new ButtonType("150,150,150",null,"Delete Account",150,30);
		Button buttonDelete = new SetupButton().CreateButton(button3);

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

				loadSlide(PROFILE);
			}
		});
		
		buttonDelete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				boolean check = Database.userDelete(currentUser.username);
				if(check){
					currentUsername = null;
					loadSlide(LOGIN);
				}
				else JOptionPane.showMessageDialog(null,
						"Failed to delete user", "Account Error",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		// Set button sizes
		buttonCancel.setPrefWidth(120);
		buttonApply.setPrefWidth(120);
		buttonDelete.setPrefWidth(120);

		// Add buttons to grid
		hBoxButtons.getChildren().addAll(buttonApply, buttonCancel, buttonDelete);
		hBoxButtons.setAlignment(Pos.CENTER);
		grid.add(hBoxButtons, 0, 10);
		GridPane.setConstraints(hBoxButtons, 0, 10, 2, 1, HPos.CENTER,
				VPos.CENTER);
		SlideContent.setupBackButton();
	}

	/* Send account changes to database(WHEN IMPLEMENTED FULLY) */
	private void ApplyChanges() {
		
		String doB = dateComboArray.get(2).getValue() + "-" + dateComboArray.get(1).getValue() + "-" + dateComboArray.get(0).getValue();
		
		Database.userUpdate(currentUser,"first_name",null,fieldFName.getText());
		Database.userUpdate(currentUser,"second_name",null,fieldLName.getText());
		Database.userUpdate(currentUser,"email",null,fieldEmail.getText());
		Database.userUpdate(currentUser,"DOB",null,doB);
		Database.userUpdate(currentUser,"first_name",null,fieldFName.getText());
		Database.userUpdate(currentUser,"landlord",buttonLandlord.isSelected(),null);
		

		CheckBio();
		
		// Check username availability
		CheckUsername();

		// Check password validity
		CheckPassword();

		// Clear password inputs if not valid
		fieldPassword.setText("");
		fieldNewPassword.setText("");
		fieldConfNewPassword.setText("");
	}
	
	private void CheckBio(){
		BadWordCheck bwc = new BadWordCheck();
		if(bwc.containsBlackListedWords(profileText.getText())){
			labelBadLanguage.setVisible(true);
			profileText.setText(bwc.highlightBlackListedWords(profileText.getText()));
			
		}
		else{
			//System.out.println(bwc.highlightBlackListedWords(profileText.getText()));
			labelBadLanguage.setVisible(false);
			Database.userUpdate(currentUser, "bio", null, profileText.getText());
		}
	}

	/* Check whether new username is available */
	private void CheckUsername() {
		
		// Skip check if username unchanged
		if (!fieldUsername.getText().equals(currentUser.username)&&(!fieldUsername.getText().equals(""))) {

			BadWordCheck bwc = new BadWordCheck();
			
			if(bwc.containsBlackListedWords(fieldUsername.getText())){
				labelUsernameError.setText("Contains Bad Language");
				labelUsernameError.setVisible(true);
			}
			// If username is unavailable
			else if (!Database.oneFieldCheck("username", fieldUsername.getText())) {
				// Send new username to database and remove error message
				Database.userUpdate(currentUser,"username",null,fieldUsername.getText());
				currentUser.username = fieldUsername.getText();
				labelUsernameError.setVisible(false);
			} else {
				
				// Show error message if username is unavailable
				labelUsernameError.setText("Username Already Exists");
				labelUsernameError.setVisible(true);
			}
		} else {
			
			// Remove error message if username is unchanged
			labelUsernameError.setVisible(false);
		}
	}

	/* Check current password is correct and new password is valid */
	private void CheckPassword() {

		// Return to profile if password is not entered and there is
		// no username error
		if (fieldPassword.getText().equals("")) {
			if (!labelUsernameError.isVisible() && !labelBadLanguage.isVisible()) {
				loadSlide(PROFILE);
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
				Database.userUpdate(currentUser,"password",null,fieldPassword.getText());

				// Return to profile if there is no username error
				if (!labelUsernameError.isVisible() && !labelBadLanguage.isVisible()) {
					loadSlide(PROFILE);
				}

				// Show new password invalid error if new password fields do not
				// match
			} else {
				labelNewPasswordInvalid.setVisible(true);
			}
		}
	}
}
