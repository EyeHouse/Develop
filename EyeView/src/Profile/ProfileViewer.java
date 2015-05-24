package Profile;

/**
 * ProfileViewer Contains all information displayed on the Profile page
 * 
 * Version: 2.5
 * 
 * Copyright Eyehouse
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import Button.ButtonType;
import Button.SetupButton;
import presenter.SlideContent;
import database.Database;
import database.User;
import database.UserReview;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class ProfileViewer extends presenter.Window {

	// Profile Static Variables
	private static final int gridCellWidth = 20;
	private static final int gridCellHeight = 10;

	// Profile Global Variables
	private GridPane profileGrid = new GridPane();
	private Font fontTitle = new Font(24);
	private Font fontMain = new Font(18);
	private User profileUser;
	private Image profilePicture;
	private ImageView profilePictureView;
	private int newRating;
	ListView<HBox> reviewList;
	ObservableList<HBox> items = FXCollections.observableArrayList();

	private Button[] buttonStar;
	private Image reviewStarFull;
	private Image reviewStarOutline;

	// Arrays of stars to allow multiple stars to be added to grid
	Polygon[] star = new Polygon[5];
	Polyline[] starOutline = new Polyline[5];

	// Profile Methods

	/**
	 * Open profile of user input
	 */
	public ProfileViewer(String profileUsername) {
		profileUser = Database.getUser(profileUsername);
		SetupGrid();
		SetupUserInfo();
		AddStars();
		SetupProfileReview();
		if(!firstLogin){
			SlideContent.setupBackButton();
		}
	}

	/**
	 * Setup grid layout object to contain user information
	 */
	private void SetupGrid() {
		profileGrid.setPrefWidth(600);

		// Set column widths of grid.
		ColumnConstraints col1 = new ColumnConstraints();
		ColumnConstraints col2 = new ColumnConstraints();
		col1.setMinWidth(250);
		col2.setMinWidth(100);
		profileGrid.getColumnConstraints().addAll(col1, col1, col2);

		// Set grid size and spacing in group.
		profileGrid.setHgap(gridCellWidth);
		profileGrid.setVgap(gridCellHeight);
		profileGrid.relocate(220, 80);
		
		root.getChildren().add(profileGrid);
	}

	/**
	 * Add basic user information to grid
	 */
	private void SetupUserInfo() {
		Label labelType;

		// Instantiates a VBox to contain the user information
		VBox vBoxUserText = new VBox(30);
		VBox vBoxUserPicture = new VBox(30);

		// Silhouette placeholder for profile picture
		// Load image and add to imageview
		try {
			InputStream binaryStream = profileUser.profimg.getBinaryStream(1,
					profileUser.profimg.length());
			profilePicture = new Image(binaryStream);
		} catch (SQLException e) {
			System.out.println("Failed to retrieve profile picture.");
			e.printStackTrace();
		}

		profilePictureView = new ImageView(profilePicture);
		// Set maximum dimensions for profile picture
		profilePictureView.setFitWidth(200);
		profilePictureView.setFitHeight(200);
		profilePictureView.setPreserveRatio(true);
		// Add profile picture to VBox
		vBoxUserPicture.getChildren().add(profilePictureView);

		if (profileUser.username.equals(currentUsername)) {
			setupProfilePictureHover(profilePictureView);
			setupProfilePictureClick(profilePictureView);
		}

		// Setup labels with information of current user
		Label labelName = new Label(profileUser.first_name + "\t("
				+ profileUser.username + ")");

		labelName.setFont(fontTitle);
		Label labelEmail = new Label("Email: " + profileUser.email);
		labelEmail.setFont(fontMain);
		Label labelDoB = new Label("Date of Birth: "
				+ profileUser.DOB.substring(8, 10) + "/"
				+ profileUser.DOB.substring(5, 7) + "/"
				+ profileUser.DOB.substring(0, 4));
		labelDoB.setFont(fontMain);

		HBox skypeBox = new HBox(10);
		if(profileUser.skype != null && !profileUser.username.equals(currentUsername)){
			SkypeCall skype = new SkypeCall();
			ImageView skypeButton = skype.addCallButton(profileUser.skype, 50);
			Label labelSkype = new Label("Available on Skype:");
			labelSkype.setFont(fontMain);
			skypeBox.getChildren().addAll(labelSkype,skypeButton);
		}
		
		// Setup account type label based on boolean landlord value
		if (profileUser.landlord) {
			labelType = new Label("Landlord");
		} else
			labelType = new Label("Student");
		labelType.setFont(fontMain);
		vBoxUserText.getChildren().addAll(labelName, labelType, labelEmail,
				labelDoB,skypeBox);

		// Add profile picture and user text to grid
		profileGrid.addRow(0, vBoxUserPicture, vBoxUserText);
		GridPane.setConstraints(vBoxUserText, 1, 0, 2, 2, HPos.LEFT,
				VPos.CENTER);
	}

	/**
	 * Set up upload picture label when mouse hovers on profile picture
	 * 
	 * @param profilePictureView
	 *            Image View containing Profile Picture
	 */
	private void setupProfilePictureHover(ImageView profilePictureView) {

		final Label updateProfilePictureLabel;
		final VBox vBoxUpdateProfilePictureLabel;

		// Create update profile picture label
		updateProfilePictureLabel = new Label(
				"Click on image to update profile picture");
		// Create a VBox to contain the label
		vBoxUpdateProfilePictureLabel = new VBox(30);
		// Add update profile label to grid pane
		profileGrid.addRow(1, vBoxUpdateProfilePictureLabel);

		profilePictureView.setCursor(Cursor.HAND); // Set hand cursor

		profilePictureView.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				// Add update profile picture label to VBox underneath picture
				vBoxUpdateProfilePictureLabel.getChildren().add(
						updateProfilePictureLabel);
			}
		});

		profilePictureView.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				// Add update profile picture label to VBox underneath picture
				vBoxUpdateProfilePictureLabel.getChildren().remove(
						updateProfilePictureLabel);
			}
		});
	}

	/**
	 * Set up upload picture when profile picture is clicked
	 * 
	 * @param profilePictureView
	 *            Image View containing Profile Picture
	 */
	private void setupProfilePictureClick(final ImageView profilePictureView) {

		profilePictureView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {

				File newProfilePictureFile;

				// Open file chooser window
				FileChooser profilePictureChooser = new FileChooser();
				configureFileChooser(profilePictureChooser);
				Window fileChooserStage = null;

				// Replace profile picture with new one from selected file
				newProfilePictureFile = profilePictureChooser
						.showOpenDialog(fileChooserStage);
				if (newProfilePictureFile != null) {
					profilePicture = new Image(newProfilePictureFile.toURI()
							.toString());
					try {

						// Upload new profile picture to database
						Database.updateImage("users",
								newProfilePictureFile.getAbsolutePath(),
								"profileIMG", profileUser.uid);

						reloadProfile();
					} catch (FileNotFoundException e) {
						System.out
								.println("Failed to upload new profile picture");
						e.printStackTrace();
					}
				}

			}

		});
	}

	/**
	 * Configures file chooser to select only images of type JPG or PNG
	 * 
	 * @param fileChooser
	 *            File Chooser to choose new profile picture
	 */
	private void configureFileChooser(FileChooser profilePictureChooser) {

		// Set title of file chooser
		profilePictureChooser.setTitle("Choose Profile Picture to Upload");
		// Set directory that the file chooser will initially open into
		profilePictureChooser.setInitialDirectory(new File(System
				.getProperty("user.home")));
		// Set file types displayed in the file chooser as png and jpg
		profilePictureChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("JPG, PNG", "*.jpg", "*.png"),
				new FileChooser.ExtensionFilter("PNG", "*.png"));
	}

	/**
	 * Add review stars to profile based on average review from database
	 */
	private void AddStars() {
		// HBox to contain the stars
		HBox hBoxStars = new HBox(5);
		int rating = 0;

		ArrayList<UserReview> userReviews = Database
				.getUserReviewList(profileUser.uid);
		if (userReviews.size()>0) {
			for (int i = 0; i < userReviews.size(); i++) {
				rating += userReviews.get(i).rating;
			}
			rating /= userReviews.size();
		}

		// Populate hBox based on user rating
		hBoxStars = createStarHBox(5, 28, rating);
		// Add star HBox to grid
		profileGrid.addRow(1, hBoxStars);
	}
	/*
	 * Setup profile and appendable review text areas
	 */
	private void SetupProfileReview() {

		TextArea textProfile = new TextArea();
		reviewList = new ListView<HBox>();
		final TextArea textNewReview = new TextArea();
		Label labelProfile, labelReview, labelNewReview, labelNewRating;

		if (profileUser.username.equals(currentUsername)) {
			ButtonType button1 = new ButtonType("150,150,150", null,
					"Edit Profile", 100, 30);
			Button buttonEditProfile = new SetupButton().CreateButton(button1);

			// Setup edit profile button event
			buttonEditProfile.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// Instantiate account settings page
					loadSlide(ACCOUNTSETTINGS);
				}
			});

			profileGrid.addRow(3, buttonEditProfile);

			GridPane.setConstraints(buttonEditProfile, 0, 3, 1, 1, HPos.CENTER,
					VPos.CENTER);

		} else {
			// Create "Submit" Review button
			ButtonType button2 = new ButtonType("150,150,150", null, "Submit",
					100, 30);
			Button buttonReview = new SetupButton().CreateButton(button2);

			// VBox to contain Add Review label and text area
			VBox vBoxNewReview = new VBox(5);

			// Setup Review labels
			labelNewReview = new Label("Add Review");
			labelNewRating = new Label("Add Rating:");
			labelNewReview.setFont(fontMain);
			labelNewRating.setFont(fontMain);
			// Limit height of new review text area
			textNewReview.setMaxHeight(50);

			// Setup add review button event
			buttonReview.setOnAction(new EventHandler<ActionEvent>(){
							public void handle(ActionEvent event) {
								// Add new review to new line of review text area
								UserReview newReview = new UserReview(profileUser.uid);
								newReview.uid_reviewer(Database.getUser(currentUsername).uid);
								newReview.review(textNewReview.getText());
								newReview.rating(newRating);
								newReview.like(0);
								newReview.dislike(0);
								Database.insertUserReview(newReview);

								reloadProfile();
							}
						});
			// Add new review and rating label and text area
			vBoxNewReview.getChildren().addAll(labelNewReview, textNewReview,
					labelNewRating);
			profileGrid.addRow(3, vBoxNewReview, buttonReview);

			// Setup review star images
			reviewStarFull = new Image(
					"file:resources/images/stars/starFull_28.png");
			reviewStarOutline = new Image(
					"file:resources/images/stars/starOutline_28.png");

			// Create star buttons
			ButtonType button3 = new ButtonType("20,00,00", null, null, 28, 28);
			// Declare array of review star buttons
			buttonStar = new Button[5];

			// Create array of outline star buttons
			for (int i = 0; i < 5; i++) {
				buttonStar[i] = new SetupButton().CreateButton(button3);
				buttonStar[i] = new SetupButton().setButtonImage(buttonStar[i],
						reviewStarOutline);
				buttonStar[i].setStyle("-fx-focus-color: transparent;");
			}

			// HBox to contain the star buttons
			HBox hBoxNewStars = new HBox(5);
			// Populate hBox with stars
			for (int i = 0; i < 5; i++) {
				hBoxNewStars.getChildren().add(buttonStar[i]);
			}
			// Add star label and HBox to grid
			profileGrid.addRow(2, labelNewRating, hBoxNewStars);

			// Align Add Review fields
			GridPane.setConstraints(buttonReview, 2, 3, 1, 1, HPos.CENTER,
					VPos.BOTTOM);
			GridPane.setConstraints(vBoxNewReview, 1, 3, 1, 1);
			GridPane.setConstraints(labelNewRating, 1, 4, 1, 1);
			GridPane.setConstraints(hBoxNewStars, 2, 4, 1, 1);

			// Add review star button handlers
			for (int i = 0; i < 5; i++) {
				buttonStar[i].setOnAction(new starButtonHandler(i));
			}
			
			//if(profileUser.skype != null){
				
			//}
			
		}

		// VBox to contain Profile label and text area
		VBox vBoxProfile = new VBox(10);
		// VBox to contain Review label and text area
		VBox vBoxReview = new VBox(10);

		// Setup labels
		labelProfile = new Label("Profile");
		labelProfile.setFont(fontMain);
		labelReview = new Label("Reviews");
		labelReview.setFont(fontMain);

		// Setup text areas with text wrapping
		textProfile.setText(profileUser.bio);
		textProfile.setEditable(false);
		textProfile.setWrapText(true);

		reviewList.setPrefHeight(200);
		ArrayList<UserReview> userReviews = Database
				.getUserReviewList(profileUser.uid);
		
		Image thumbsUp = new Image("file:resources/images/stars/thumbs_up.png");
		Image thumbsDown = new Image("file:resources/images/stars/thumbs_down.png");
		
		for (int i = 0; i < userReviews.size(); i++) {

			HBox reviewItem = new HBox(10);
			VBox reviewLeft = new VBox(10);
			VBox buttons = new VBox(12);
			HBox stars = createStarHBox(2, 14, userReviews.get(i).rating);

			Label reviewText = new Label(userReviews.get(i).review + "\n"
					+ "Likes: " + userReviews.get(i).like + "  Dislikes: "
					+ userReviews.get(i).dislike);
			reviewText.setWrapText(true);
			reviewText.setPrefWidth(200);
			
			ImageView like = new ImageView(thumbsUp);
			ImageView dislike = new ImageView(thumbsDown);
			
			if (!profileUser.username.equals(currentUsername)){
				like.setOnMouseClicked(new likeHandler(userReviews.get(i)));
				like.setCursor(Cursor.HAND);
				dislike.setOnMouseClicked(new dislikeHandler(userReviews.get(i)));
				dislike.setCursor(Cursor.HAND);
			}

			reviewLeft.getChildren().addAll(reviewText, stars);
			buttons.getChildren().addAll(like,dislike);
			reviewItem.getChildren().addAll(reviewLeft, buttons);
			items.add(reviewItem);
		}
		reviewList.setItems(items);

		// Populate grid with profile and review information
		vBoxProfile.getChildren().addAll(labelProfile, textProfile);
		vBoxReview.getChildren().addAll(labelReview, reviewList);
		profileGrid.addRow(2, vBoxProfile, vBoxReview);
	}

	public HBox createStarHBox(int spacing, int size, int rating) {
		HBox stars = new HBox(spacing);
		for (int i = 0; i < 5; i++) {
			if (i < rating) {
				ImageView star = new ImageView(new Image(
						"file:resources/images/stars/starFull_28.png"));
				star.setFitWidth(size);
				star.setFitHeight(size);
				stars.getChildren().add(star);
			} else {
				ImageView star = new ImageView(new Image(
						"file:resources/images/stars/starOutline_28.png"));
				star.setFitWidth(size);
				star.setFitHeight(size);
				stars.getChildren().add(star);
			}
		}
		return stars;
	}

	public class starButtonHandler implements EventHandler<ActionEvent> {
		private int buttonNumber;

		public starButtonHandler(int number) {
			this.buttonNumber = number;
		}

		@Override
		public void handle(ActionEvent event) {
			newRating = buttonNumber + 1;

			for (int i = 0; i < 5; i++) {
				if (i <= newRating - 1) {
					buttonStar[i].setGraphic(new ImageView(reviewStarFull));
				} else {
					buttonStar[i].setGraphic(new ImageView(reviewStarOutline));
				}
			}
		}
	}

	public class likeHandler implements EventHandler<MouseEvent> {
		private UserReview review;

		public likeHandler(UserReview review) {
			this.review = review;
		}

		@Override
		public void handle(MouseEvent event) {
			Database.deleteUserReview(review);
			review.like(review.like + 1);
			Database.insertUserReview(review);
			reloadProfile();
		}
	}

	public class dislikeHandler implements EventHandler<MouseEvent> {
		private UserReview review;

		public dislikeHandler(UserReview review) {
			this.review = review;
		}

		@Override
		public void handle(MouseEvent event) {
			Database.deleteUserReview(review);
			review.dislike(review.dislike + 1);
			Database.insertUserReview(review);
			reloadProfile();
		}
	}

	private void reloadProfile() {

		root.getChildren().clear();
		slideID = PROFILE;
		SlideContent sc = new SlideContent();
		sc.createSlide();
	}
}