package parser;

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
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class LoginInterFaceDemo extends Application {

	TextField username;
	TextField password;
	User user = null;
	Button loginButton = new Button("Login");
	Button cancelButton = new Button("Cancel");

	Group root = new Group();
	GridPane grid = new GridPane();

	@Override
	public void start(Stage stage) {
		setupGrid();
		setupTextFields(grid);
		setupPasswordFields(grid);
		setupButtons();
		setupTitle();

		Scene scene = new Scene(root, 800, 480);

		Group root = (Group) scene.getRoot();
		root.getChildren().add(grid);
		stage.setTitle("Login");
		stage.setScene(scene);
		stage.show();

	}

	public void setupGrid() {

		grid.setVgap(10);
		grid.setHgap(10);
		grid.setPadding(new Insets(50, 5, 5, 270));
	}

	public void setupTextFields(GridPane grid) {

		// username field
		username = new TextField();
		grid.add(new Label("Username: "), 0, 8);
		username.setPromptText("Username");
		username.setPrefColumnCount(10);
		grid.getChildren().add(username);
		GridPane.setConstraints(username, 1, 8);
	}

	public void setupPasswordFields(GridPane grid) {

		// Password field
		password = new PasswordField();
		grid.add(new Label("Password: "), 0, 10);
		password.setPromptText("Your Password");
		password.setPrefColumnCount(10);
		grid.getChildren().add(password);
		GridPane.setConstraints(password, 1, 10);
	}

	public void setupButtons() {
		// add buttons
		HBox hbox = new HBox(5);

		// Add buttons to grid
		hbox.getChildren().addAll(loginButton, cancelButton);
		hbox.setAlignment(Pos.CENTER);
		grid.add(hbox, 1, 12);
		GridPane.setConstraints(hbox, 1, 12, 1, 1, HPos.CENTER,
				VPos.CENTER);

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
				root.getChildren().clear();
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

		userExists = Database.userCheck("username",
				(String) username.getText(), "password", hashpass);
		// if exists create user object
		System.out.println("User:" + (String) username.getText());
		if (userExists == true) {
			user = Database.getUser(username.getText());
			user.printUser();
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
