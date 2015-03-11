package presenter;

/*
 * Profile.java
 * 
 * Version: 1.9
 * 
 * Copyright:
 */

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class ProfileViewer {

	/* Profile Static Variables */
	private static final int gridCellWidth = 20;
	private static final int gridCellHeight = 30;
	private static final double xStart = 0.5;
	private static final double yStart = 0.1;
	private static final int rating = 3;

	/* Profile Global Variables */
	private GridPane grid = new GridPane();
	private Group screenGroup;
	private double xResolution;
	private double yResolution;
	private Font fontTitle = new Font("Cambria", 24);
	private Font fontMain = new Font("Cambria", 18);
	private UserType currentUser;

	/* ProfileView Constructor */
	public ProfileViewer(Group group, double xresolution, double yresolution) {
		screenGroup = group;
		xResolution = xresolution;
		yResolution = xresolution;
	}

	/* Profile Methods */

	/* Open profile of user input */
	public void OpenProfile(UserType user) {
		this.currentUser = user;
		SetupGrid();
		SetupUserInfo();
		SetupStars();
		SetupProfileReview();

		screenGroup.getChildren().add(grid);
	}

	/* Setup grid layout object to contain user information */
	private void SetupGrid() {
		grid.setPrefWidth(600);

		// Set column widths of grid.
		ColumnConstraints col1 = new ColumnConstraints();
		ColumnConstraints col2 = new ColumnConstraints();
		col1.setMinWidth(250);
		col2.setMinWidth(100);
		grid.getColumnConstraints().addAll(col1, col1, col2);

		// Set grid size and spacing in group.
		grid.setHgap(gridCellWidth);
		grid.setVgap(gridCellHeight);
		grid.setPadding(new Insets(yResolution * yStart, xResolution * xStart,
				100, 200));
	}

	/* Add basic user information to grid */
	private void SetupUserInfo() {
		Label labelName, labelEmail, labelDoB, labelType;

		// Instantiates a VBox to contain the user information
		VBox vBoxUserText = new VBox(30);

		// Square placeholder for profile picture
		Rectangle profilePicture = new Rectangle(200, 200, Color.GRAY);

		// Setup labels with information of current user
		labelName = new Label(currentUser.fName + "\t(" + currentUser.username
				+ ")");
		labelName.setFont(fontTitle);
		labelEmail = new Label("Email: " + currentUser.email);
		labelEmail.setFont(fontMain);
		labelDoB = new Label("Date of Birth: " + currentUser.doB);
		labelDoB.setFont(fontMain);

		// Setup account type label based on boolean landlord value
		if (currentUser.landlord) {
			labelType = new Label("Landlord");
		} else
			labelType = new Label("Student");
		labelType.setFont(fontMain);
		vBoxUserText.getChildren().addAll(labelName, labelType, labelEmail,
				labelDoB);

		// Add profile picture and user text to grid
		grid.addRow(0, profilePicture, vBoxUserText);
	}

	/* Create star polygons and add them to the grid */
	private void SetupStars() {

		int nbSides = 2 * 5;
		int r = 15;
		int[] xpoints = new int[nbSides];
		int[] ypoints = new int[nbSides];
		double angle = Math.PI / 2;

		// Arrays of stars to allow multiple stars to be added to grid
		Polygon[] star = new Polygon[5];
		Polyline[] starOutline = new Polyline[5];

		// HBox to contain the stars
		HBox hBoxStars = new HBox(5);

		// Calculate the vertices of the star
		for (int i = 0; i < nbSides; i++) {

			// Maths for even vertices
			if (i % 2 == 0) {
				xpoints[i] = (int) 0 - (int) ((double) r * Math.cos(angle));
				ypoints[i] = (int) 0 - (int) ((double) r * Math.sin(angle));

				// Maths for odd vertices
			} else {
				xpoints[i] = (int) 0
						- (int) ((double) 2 * r / 6 * Math.cos(angle));
				ypoints[i] = (int) 0
						- (int) ((double) 2 * r / 6 * Math.sin(angle));
			}
			angle += Math.PI / 5;
		}

		// Populate arrays of solid and outline stars
		for (int i = 0; i < 5; i++) {
			star[i] = new Polygon(xpoints[0], ypoints[0], xpoints[1],
					ypoints[1], xpoints[2], ypoints[2], xpoints[3], ypoints[3],
					xpoints[4], ypoints[4], xpoints[5], ypoints[5], xpoints[6],
					ypoints[6], xpoints[7], ypoints[7], xpoints[8], ypoints[8],
					xpoints[9], ypoints[9]);

			starOutline[i] = new Polyline(xpoints[0], ypoints[0], xpoints[1],
					ypoints[1], xpoints[2], ypoints[2], xpoints[3], ypoints[3],
					xpoints[4], ypoints[4], xpoints[5], ypoints[5], xpoints[6],
					ypoints[6], xpoints[7], ypoints[7], xpoints[8], ypoints[8],
					xpoints[9], ypoints[9], xpoints[0], ypoints[0]);
		}

		// Populate hBox based on user rating
		for (int i = 0; i < 5; i++) {
			if (i < rating)
				hBoxStars.getChildren().add(star[i]);
			else
				hBoxStars.getChildren().add(starOutline[i]);
		}

		// Add star HBox to grid
		grid.addRow(1, hBoxStars);
	}

	/* Setup profile and appendable review text areas */
	private void SetupProfileReview() {

		TextArea textProfile = new TextArea();
		final TextArea textReview = new TextArea();
		final TextArea textNewReview = new TextArea();
		Label labelProfile, labelReview, labelNewReview;
		Button buttonEditProfile = new Button("Edit Profile");
		Button buttonReview = new Button("Submit");

		// VBox to contain Profile label and text area
		VBox vBoxProfile = new VBox(10);

		// VBox to contain Review label and text area
		VBox vBoxReview = new VBox(10);

		// VBox to contain Add Review label and text area
		VBox vBoxNewReview = new VBox(5);

		// Setup labels
		labelProfile = new Label("Profile");
		labelProfile.setFont(fontMain);
		labelReview = new Label("Reviews");
		labelReview.setFont(fontMain);
		labelNewReview = new Label("Add Review");
		labelNewReview.setFont(fontMain);

		// Setup text areas with text wrapping
		textProfile.setText(currentUser.profileText);
		textProfile.setEditable(false);
		textProfile.setWrapText(true);
		textReview.setText("Crackin bloke!");
		textReview.setEditable(false);
		textReview.setWrapText(true);

		// Limit height of new review text area
		textNewReview.setMaxHeight(50);

		// Setup edit profile button event
		buttonEditProfile.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Instantiate account settings page
				screenGroup.getChildren().clear();
				AccountSettings accountSettings = new AccountSettings(
						screenGroup, xResolution, yResolution);
				accountSettings.OpenAccountSettings(currentUser);
			}
		});

		// Setup add review button event
		buttonReview.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Add new review to new line of review text area
				textReview.appendText("\n\n" + textNewReview.getText());
				textNewReview.clear();
			}
		});

		// Populate grid with profile and review information
		vBoxProfile.getChildren().addAll(labelProfile, textProfile);
		vBoxReview.getChildren().addAll(labelReview, textReview);
		vBoxNewReview.getChildren().addAll(labelNewReview, textNewReview);
		grid.addRow(2, vBoxProfile, vBoxReview);
		grid.addRow(3, buttonEditProfile, vBoxNewReview, buttonReview);

		// Centre align buttons
		GridPane.setConstraints(buttonEditProfile, 0, 3, 1, 1, HPos.CENTER,
				VPos.CENTER);
		GridPane.setConstraints(buttonReview, 2, 3, 1, 1, HPos.CENTER,
				VPos.CENTER);
	}
}
