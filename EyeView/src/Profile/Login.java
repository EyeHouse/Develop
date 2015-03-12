package Profile;

//TODO Auto-generated constructor stub
import javax.swing.JOptionPane;

import database.DataHandler;
import database.Database;
import database.User;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Login extends presenter.Window {

	TextField username;
	TextField password;
	Button loginButton = new Button("Login");
	Button cancelButton = new Button("Cancel");

	GridPane loginGrid = new GridPane();

	public Login() {
		setupGrid();
		setupTextFields();
		setupPasswordFields();
		setupButtons();
		setupTitle();
		root.getChildren().add(loginGrid);
	}

	public void setupGrid() {

		loginGrid.setVgap(10);
		loginGrid.setHgap(10);
		loginGrid.setPadding(new Insets(50, 5, 5, 270));
	}

	public void setupTextFields() {

		// username field
		username = new TextField();
		loginGrid.add(new Label("Username: "), 0, 8);
		username.setPromptText("Username");
		username.setPrefColumnCount(10);
		loginGrid.getChildren().add(username);
		GridPane.setConstraints(username, 1, 8);
	}

	public void setupPasswordFields() {

		// Password field
		password = new PasswordField();
		loginGrid.add(new Label("Password: "), 0, 10);
		password.setPromptText("Your Password");
		password.setPrefColumnCount(10);
		loginGrid.getChildren().add(password);
		GridPane.setConstraints(password, 1, 10);
	}

	public void setupButtons() {
		// add buttons
		HBox hbox = new HBox(5);

		// Add buttons to grid
		hbox.getChildren().addAll(loginButton, cancelButton);
		hbox.setAlignment(Pos.CENTER);
		loginGrid.add(hbox, 1, 12);
		GridPane.setConstraints(hbox, 1, 12, 1, 1, HPos.CENTER, VPos.CENTER);

		// Save button
		loginButton.setCursor(Cursor.HAND);
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				login();
			}
		});
		// cancel button event listener
		cancelButton.setCursor(Cursor.HAND);
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				loadSlide(INDEX);
			}
		});
	}

	public void setupTitle() {

		final Text topTitle = new Text(270, 40, "Login Page");
		topTitle.setFill(Color.TEAL);
		topTitle.setFont(Font.font(java.awt.Font.MONOSPACED, 35));
		root.getChildren().add(topTitle);

	}

	public boolean login() {

		boolean userExists;
		// check the user exits
		String hashpass = DataHandler.crypt((String) password.getText());

		userExists = Database.twoFieldCheck("username",
				(String) username.getText(), "password", hashpass);
		// if exists create user object
		System.out.println("User:" + (String) username.getText());
		if (userExists == true) {
			User user = Database.getUser(username.getText());
			user.printUser();
			root.getChildren().clear();
			ProfileViewer profile = new ProfileViewer();
			profile.OpenProfile(user.username);
		} else {
			JOptionPane.showMessageDialog(null,
					"Login Failed. Please try again.", "Login Error!",
					JOptionPane.WARNING_MESSAGE);
		}
		return userExists;
	}

	public static void main(String[] args) {
		Database.dbConnect();
		Application.launch(args);
	}

}