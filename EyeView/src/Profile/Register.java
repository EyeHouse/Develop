package Profile;

//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.text.SimpleDateFormat;
//import java.util.Locale;

import database.DataHandler;
import database.Database;
import database.User;
import javafx.event.EventHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

public class Register extends presenter.Window {

	public String userFirstname, userLastName, userEmail, userName,
			encryptedPassword, dateOfBirth;
	public User user = null;
	private String userPassword, userRepeatPassword;
	// private static MessageDigest digester;
	private int year, month, day;
	private int i, k, j;
	final Button saveButton = new Button("Save");
	final Button cancelButton = new Button("Cancel");

	TextField username;
	TextField firstname;
	TextField lastname;
	TextField email;
	PasswordField password;
	PasswordField repeatPassword;
	GridPane registerGrid = new GridPane();

	ComboBox<Integer> comboDoBYear = new ComboBox<Integer>();
	ComboBox<Integer> comboDoBMonth = new ComboBox<Integer>();
	ComboBox<Integer> comboDoBDay = new ComboBox<Integer>();

	public static void main(String[] args) {
		Database.dbConnect();
		launch(args);
	}

	public Register() {
		setupGrid();
		setupTextFields(registerGrid);
		setupPasswordFields(registerGrid);
		setupDoB();
		setupButtons();
		setupTitle();

		root.getChildren().add(registerGrid);

	}

	public void setupGrid() {

		registerGrid.setVgap(10);
		registerGrid.setHgap(10);
		registerGrid.setPadding(new Insets(40, 5, 5, 170));
	}

	public void setupTextFields(GridPane grid) {

		// username field
		username = new TextField();
		registerGrid.add(new Label("Enter your username: "), 0, 5);
		username.setPromptText("Username");
		username.setPrefColumnCount(10);
		registerGrid.getChildren().add(username);
		GridPane.setConstraints(username, 1, 5);

		// First Name field
		firstname = new TextField();
		registerGrid.add(new Label("First name: "), 0, 6);
		firstname.setPromptText("Joe");
		firstname.setPrefColumnCount(10);
		registerGrid.getChildren().add(firstname);
		GridPane.setConstraints(firstname, 1, 6);

		// Last Name field
		lastname = new TextField();
		registerGrid.add(new Label("Last name: "), 0, 7);
		lastname.setPromptText("Bloggs");
		lastname.setPrefColumnCount(10);
		registerGrid.getChildren().add(lastname);
		GridPane.setConstraints(lastname, 1, 7);

		// Email field
		email = new TextField();
		registerGrid.add(new Label("Email: "), 0, 10);
		email.setPromptText("example@mail.com");
		email.setPrefColumnCount(10);
		registerGrid.getChildren().add(email);
		GridPane.setConstraints(email, 1, 10);
	}

	public void setupPasswordFields(GridPane grid) {

		// Password field
		password = new PasswordField();
		registerGrid.add(new Label("Password: "), 0, 11);
		password.setPromptText("Your Password");
		password.setPrefColumnCount(10);
		registerGrid.getChildren().add(password);
		GridPane.setConstraints(password, 1, 11);

		// Repeat Password field
		repeatPassword = new PasswordField();
		registerGrid.add(new Label("Confirm Password: "), 0, 12);
		repeatPassword.setPromptText("Your Password");
		repeatPassword.setPrefColumnCount(10);
		registerGrid.getChildren().add(repeatPassword);
		GridPane.setConstraints(repeatPassword, 1, 12);
	}

	public void setupDoB() {

		// Dropdown for year
		for (i = 2005; i > 1949; i--) {
			comboDoBYear.getItems().addAll(i);
		}
		comboDoBYear.setValue(1993);
		// Dropdown for month
		for (k = 1; k < 13; k++) {
			comboDoBMonth.getItems().addAll(k);
		}
		comboDoBMonth.setValue(1);

		// Dropdown for day
		// 30 day months

		for (j = 1; j < 32; j++) {
			comboDoBDay.getItems().addAll(j);
		}
		comboDoBDay.setValue(1);

		registerGrid.add(new Label("Date of Birth: "), 0, 8);
		registerGrid.add(new Label("Year: "), 4, 9);
		registerGrid.add(comboDoBYear, 5, 9);
		registerGrid.add(new Label("Month: "), 2, 9);
		registerGrid.add(comboDoBMonth, 3, 9);

		comboDoBMonth.valueProperty().addListener(
				new ChangeListener<Integer>() {
					@Override
					public void changed(
							ObservableValue<? extends Integer> arg0,
							Integer arg1, Integer arg2) {

						comboDoBDay.getSelectionModel().clearSelection();
						comboDoBDay.getItems().clear();
						comboDoBDay.setValue(null);
						switch (arg2) {
						case 9:
						case 4:
						case 6:
						case 11:
							for (j = 1; j < 31; j++) {
								comboDoBDay.getItems().addAll(j);
							}
							break;
						case 2:
							for (j = 1; j < 29; j++) {
								comboDoBDay.getItems().addAll(j);
							}
							break;
						default:
							for (j = 1; j < 32; j++) {
								comboDoBDay.getItems().addAll(j);
							}
						}
						comboDoBDay.setValue(1);
					}
				});
		registerGrid.add(new Label("Day: "), 0, 9);
		registerGrid.add(comboDoBDay, 1, 9);

	}

	public void setupTitle() {

		final Text topTitle = new Text(230, 40, "Registration Page");
		topTitle.setFill(Color.TEAL);
		topTitle.setFont(Font.font(java.awt.Font.MONOSPACED, 35));
		root.getChildren().add(topTitle);

	}

	public void setupButtons() {

		HBox hbox = new HBox(5);

		// Add buttons to grid
		hbox.getChildren().addAll(saveButton, cancelButton);
		hbox.setAlignment(Pos.CENTER);
		registerGrid.add(hbox, 1, 14);
		GridPane.setConstraints(hbox, 1, 14, 1, 1, HPos.CENTER, VPos.CENTER);

		// Save button
		saveButton.setCursor(Cursor.HAND);
		saveButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {

				saveChanges();
			}

		});

		// Cancel button
		// Right now just clears the window (group); should take you back to
		// previous slide (scene) ideally
		cancelButton.setCursor(Cursor.HAND);
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				loadSlide(INDEX);
			}
		});
	}

	public boolean saveChanges() {

		boolean regSuccess;

		user = new User((String) username.getText());

		// Storing inputs from user
		year = (Integer) comboDoBYear.getValue();
		month = (Integer) comboDoBMonth.getValue();
		day = (Integer) comboDoBDay.getValue();
		userPassword = (String) password.getText();
		userFirstname = (String) firstname.getText();

		userLastName = (String) lastname.getText();

		userName = (String) username.getText();
		userEmail = (String) email.getText();

		userRepeatPassword = (String) repeatPassword.getText();

		String yearStr = Integer.toString(year);
		String monthStr = Integer.toString(month);
		String dayStr = Integer.toString(day);
		dateOfBirth = yearStr + "-" + monthStr + "-" + dayStr;

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
				Login login = new Login();

				System.out.println("User: " + user.username
						+ " created successfully");
			}
		}
		return regSuccess;
	}
}