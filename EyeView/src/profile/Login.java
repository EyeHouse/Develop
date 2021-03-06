package profile;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import language.Translator;
import presenter.SlideContent;
import presenter.Window;
import button.ButtonType;
import button.SetupButton;
import database.DataHandler;
import database.Database;
import database.User;

/**
 * This class implements the EyeView Login page.
 * 
 * @version 2.2 (20.04.15)
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class Login extends Window {

	private TextField username, password;
	private GridPane loginGrid = new GridPane();
	private static double sideBarWidth = 182;
	private int xBox = 350;
	private int yBox = 200;
	private Label labelLogin, labelPassword;

	/**
	 * Constructor method
	 */
	public Login() {

		setLayout();
		setupGrid();
		setupTextFields();
		setupPasswordFields();
		setupButtons();
		setupTitle();
		createWarningPopup("Login Failed. Please try again.");
		root.getChildren().add(loginGrid);
	}

	/**
	 * Creates the background of the login page.
	 */
	public void setLayout() {

		setBackground(true);
		setWhiteBox(xBox, yBox, true);
		Image icon = new Image("file:./resources/icons/xxhdpi.png");
		ImageView iconView = new ImageView(icon);
		iconView.relocate(250, 260);
		root.getChildren().add(iconView);
	}

	/**
	 * Creates a white box of a specified size with a border and rounded edges.
	 * 
	 * @param boxWidth
	 *            desired width of the white box containing the data
	 * @param boxHeight
	 *            desired height of the white box containing the data
	 */
	public static void setWhiteBox(int boxWidth, int boxHeight, boolean sidebar) {

		Rectangle centreBox;
		if (sidebar) {
			centreBox = RectangleBuilder
					.create()
					.arcWidth(30)
					// Curved edges
					.arcHeight(30)
					.fill(Color.WHITE)
					.x(sideBarWidth + ((xResolution - sideBarWidth) / 2)
							- (boxWidth / 2))
					.y(((yResolution) / 2) - (boxHeight / 2) - 15)
					.strokeWidth(2).stroke(Color.rgb(33, 51, 76)).build();
		} else {
			centreBox = RectangleBuilder
					.create()
					.arcWidth(30)
					// Curved edges
					.arcHeight(30).fill(Color.WHITE)
					.x((xResolution / 2) - (boxWidth / 2))
					.y(((yResolution) / 2) - (boxHeight / 2) - 15)
					.strokeWidth(2).stroke(Color.rgb(33, 51, 76)).build();
		}
		double width = centreBox.getWidth();
		System.out.println(width);
		centreBox.setWidth(boxWidth);
		centreBox.setHeight(boxHeight);
		root.getChildren().addAll(centreBox);
	}

	/**
	 * Creates the two rectangles for the background and gives them the
	 * appropriate size.
	 */
	public static void setBackground(boolean sidebar) {

		// Set top half rectangle
		Rectangle topRectangle = new Rectangle();
		topRectangle.setHeight(yResolution);
		topRectangle.setFill(Color.rgb(104, 158, 239, 0.3));

		// Set bottom half rectangle
		Rectangle bottomRectangle = new Rectangle();
		bottomRectangle.setHeight(yResolution / 2);
		bottomRectangle.setFill(Color.rgb(91, 138, 209, 0.3));

		if (sidebar) {
			topRectangle.setWidth(xResolution - sideBarWidth);
			topRectangle.relocate(sideBarWidth, 0); // Width of sidebar
			bottomRectangle.setWidth(xResolution - sideBarWidth);
			bottomRectangle.relocate(sideBarWidth, (yResolution / 2) - 15);
		} else {
			topRectangle.setWidth(xResolution);
			topRectangle.relocate(0, 0); // Width of sidebar
			bottomRectangle.setWidth(xResolution);
			bottomRectangle.relocate(0, (yResolution / 2) - 15);
		}

		root.getChildren().addAll(topRectangle, bottomRectangle);
	}

	/**
	 * Sets up the spacing between the elements in rows and in columns.
	 */
	public void setupGrid() {

		loginGrid.setVgap(30);
		loginGrid.setHgap(20);
		loginGrid.setMaxWidth(320);
		loginGrid.setMinWidth(320);

		// Set position of the grid
		loginGrid.relocate(375, 290);
	}

	/**
	 * Sets up the username field for the user to fill in.
	 */
	public void setupTextFields() {

		// Username field
		username = new TextField();
		labelLogin = new Label(Translator.translateText(languageIndex,
				"Username") + ": ");
		labelLogin.setFont(new Font(14));
		GridPane.setHgrow(labelLogin, Priority.ALWAYS);
		GridPane.setHalignment(labelLogin, HPos.RIGHT);
		loginGrid.add(labelLogin, 0, 1);
		username.setPromptText(Translator.translateText(languageIndex,
				"Username"));
		username.setPrefColumnCount(10);
		username.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					login();
				}
			}
		});
		loginGrid.getChildren().add(username);
		GridPane.setConstraints(username, 1, 1);

		username.setPickOnBounds(true);
	}

	/**
	 * Sets up the password field for the user to fill in and used an event
	 * handler to login when enter is pressed.
	 */
	public void setupPasswordFields() {

		// Password field
		password = new PasswordField();
		labelPassword = new Label(Translator.translateText(languageIndex,
				"Password") + ": ");
		labelPassword.setFont(new Font(14));
		GridPane.setHgrow(labelLogin, Priority.ALWAYS);
		GridPane.setHalignment(labelPassword, HPos.RIGHT);
		loginGrid.add(labelPassword, 0, 2);
		password.setPromptText(Translator.translateText(languageIndex,
				"Password"));
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

	/**
	 * Sets up the buttons on the side menu and the save button, which calls the
	 * function to login the user.
	 */
	public void setupButtons() {

		// Add buttons
		ButtonType button1 = new ButtonType("166,208,255", null,
				(Translator.translateText(languageIndex, "Login")), 140, 35);
		Button loginButton = new SetupButton().createButton(button1);
		loginButton.relocate(500, 420);
		loginButton.setFont(Font.font(null, FontWeight.BOLD, 14));
		loginButton.setCursor(Cursor.HAND);
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				login();
			}
		});
		root.getChildren().add(loginButton);
		SlideContent.setupBackButton();
	}

	/**
	 * Creates and places the title of the page.
	 */
	public void setupTitle() {

		Label topTitle = new Label(Translator.translateText(languageIndex,
				"Login"));
		topTitle.setTextFill(Color.web("#162252"));
		topTitle.setFont(new Font(35));
		topTitle.relocate(370, 210);
		topTitle.setPrefWidth(400);
		topTitle.setAlignment(Pos.CENTER);
		root.getChildren().add(topTitle);
	}

	/**
	 * Checks if the provided details correspond to an existing user. If they do
	 * the user is logged in, otherwise it displays a warning message.
	 */
	public boolean login() {

		boolean userExists = false;

		if (password.getText().isEmpty()) {
			dialogStage.show();
		} else {
			// check the user exits
			String hashpass = DataHandler.crypt((String) password.getText());

			userExists = Database.login((String) username.getText(), hashpass);
			// if exists create user object
			// System.out.println("User:" + (String) username.getText());

			if (userExists == true) {
				User loggedIn = Database.getUser(username.getText());
				currentUsername = loggedIn.username;
				loadSlide(HOUSES);
			} else {
				dialogStage.show();
			}
		}
		return userExists;
	}
}
