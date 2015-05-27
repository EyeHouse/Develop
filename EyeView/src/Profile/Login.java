package Profile;

/**
 * This class implements the EyeView Login Page
 * 
 * @version 2.2 20.04.15
 * @author EyeHouse
 * 
 * Copyright 2015 EyeHouse
 */

import Button.ButtonType;
import Button.SetupButton;
import presenter.SlideContent;
import database.DataHandler;
import database.Database;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import language.Translate;

public class Login extends presenter.Window {

	TextField username;
	TextField password;

	Label topTitle;
	GridPane loginGrid = new GridPane();

	public Label labelLogin = new Label();

	public Login() {
		setupGrid();
		setupTextFields();
		setupPasswordFields();
		setupButtons();
		setupTitle();
		createWarningPopup("Password incorrect. Please try again.");
		root.getChildren().addAll(topTitle, loginGrid);
	}

	public void setupGrid() {

		loginGrid.setVgap(30);
		loginGrid.setHgap(30);
		loginGrid.relocate(435, 130);
	}

	public void setupTextFields() {

		// Username field
		username = new TextField();
		// labelLogin = new Label(Translate.translateText(languageIndex,
		// "Username") + ": ");
		labelLogin.setText("Username");
		loginGrid.add(labelLogin, 0, 1);
		username.setPromptText("Username");
		username.setPrefColumnCount(10);
		loginGrid.getChildren().add(username);
		GridPane.setConstraints(username, 1, 1);

	}

	public void setupPasswordFields() {

		// Password field
		password = new PasswordField();
		loginGrid.add(new Label("Password: "), 0, 2);
		password.setPromptText("Your Password");
		password.setPrefColumnCount(10);

		password.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					login();
				}
			}
		});

		loginGrid.getChildren().add(password);
		GridPane.setConstraints(password, 1, 2);
	}

	public void setupButtons() {

		// Add buttons to grid
		ButtonType button1 = new ButtonType("166,208,255", null,
				(Translate.translateText(languageIndex, "Login")), 100, 30);
		Button loginButton = new SetupButton().CreateButton(button1);
		loginGrid.add(loginButton, 0, 3);
		GridPane.setConstraints(loginButton, 0, 3, 2, 1, HPos.CENTER,
				VPos.CENTER);

		// Save button
		loginButton.setCursor(Cursor.HAND);
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				login();
			}
		});

		SlideContent.setupBackButton();
	}

	public void setupTitle() {

		topTitle = new Label(Translate.translateText(languageIndex, "Login"));
		topTitle.setTextFill(Color.web("#162252FF"));
		topTitle.setFont(new Font(35));
		topTitle.relocate(470, 90);

	}

	public boolean login() {

		boolean userExists;
		// check the user exits
		String hashpass = DataHandler.crypt((String) password.getText());

		userExists = Database.login((String) username.getText(), hashpass);
		// if exists create user object
		System.out.println("User:" + (String) username.getText());
		if (userExists == true) {
			currentUsername = username.getText();
			loadSlide(HOUSES);
		} else {
			dialogStage.show();
		}
		return userExists;
	}

	public static void main(String[] args) {
		Database.dbConnect();
		Application.launch(args);
	}

}