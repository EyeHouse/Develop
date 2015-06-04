package houses;

import java.util.ArrayList;

import database.Database;
import database.House;
import database.HouseReview;
import database.User;
import button.ButtonType;
import button.SetupButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import language.BadWordCheck;
import language.Translator;
import presenter.Window;
import profile.ProfileViewer;
import profile.SavedProperties;

/**
 * This class creates the reviews page for a specific house advert. Users can
 * view and add reviews for a house and like/dislike other people's reviews.
 * 
 * @version 2.6
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class HouseReviews extends Window {

	private Label pageTitle, ratingLabel, newReviewLabel, giveRatingLabel;
	private ListView<HBox> reviewsView;
	private ObservableList<HBox> reviews = FXCollections.observableArrayList();
	private Button[] buttonStar;
	private Image reviewStarFull = new Image(
			"file:resources/images/stars/starFull_28.png");
	private Image reviewStarOutline = new Image(
			"file:resources/images/stars/starOutline_28.png");
	private int newRating;
	private GridPane pane;
	private TextArea newReviewText;
	private HBox hBoxOverallRating = new HBox(5);
	private Button buttonSubmit;

	/**
	 * Constructor method
	 */
	public HouseReviews() {

		pane = new GridPane();

		setupGrid();
		setupTitle();
		setupButtons();
		setupRatingLabel();
		setupAveRating();
		setupReviewsTextField();
		displayReviews();
		setupAddStarRating();

		root.getChildren().add(pane);
	}

	/**
	 * Sets up the grid pane and column constraints for the page.
	 */
	public void setupGrid() {

		pane.setVgap(20);
		pane.setHgap(30);
		pane.relocate(315, 130);
		pane.setPrefWidth(450);

		// Set column widths of grid.
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setMinWidth(200);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setMinWidth(125);
		pane.getColumnConstraints().addAll(col1, col2, col2);
	}

	/**
	 * Creates the page title.
	 */
	private void setupTitle() {

		pageTitle = new Label(
				Translator.translateText(languageIndex, "Reviews"));
		pageTitle.setFont(new Font(32));
		pageTitle.setTextFill(Color.web("#162252"));
		pageTitle.setPrefWidth(550);
		pageTitle.setAlignment(Pos.CENTER);
		pageTitle.relocate(275, 80);

		root.getChildren().add(pageTitle);
	}

	/**
	 * Sets up the label for the rating text field.
	 */
	public void setupRatingLabel() {

		ratingLabel = new Label(Translator.translateText(languageIndex,
				"Rating") + ":    ");
		ratingLabel.setFont(new Font(20));
		hBoxOverallRating.getChildren().add(ratingLabel);
	}

	/**
	 * Creates stars to display average house rating.
	 */
	public void setupAveRating() {

		int rating = 0;

		ArrayList<HouseReview> houseReviews = Database
				.getHouseReviews(currentPropertyID);
		if (houseReviews.size() > 0) {
			for (int i = 0; i < houseReviews.size(); i++) {
				rating += houseReviews.get(i).rating;
			}
			rating /= houseReviews.size();
		}

		hBoxOverallRating = ProfileViewer.createStarHBox(5, 28, rating);
		pane.add(hBoxOverallRating, 0, 1);
		GridPane.setConstraints(hBoxOverallRating, 0, 1, 3, 1, HPos.LEFT,
				VPos.CENTER);
	}

	/**
	 * Displays all the reviews in a ListView.
	 */
	public void displayReviews() {

		newReviewLabel = new Label(Translator.translateText(languageIndex,
				"Reviews") + ":");
		newReviewLabel.setFont(new Font(20));
		pane.add(newReviewLabel, 0, 2);

		reviewsView = new ListView<HBox>();
		reviewsView.setPrefHeight(200);
		reviewsView.setItems(reviews);

		House house = Database.getHouse(currentPropertyID);
		User landlord = Database.getUser(Database.getUsername(house.uid));
		ArrayList<HouseReview> houseReviews = Database
				.getHouseReviews(currentPropertyID);

		Image thumbsUp = new Image("file:resources/images/stars/thumbs_up.png");
		Image thumbsDown = new Image(
				"file:resources/images/stars/thumbs_down.png");

		for (int i = houseReviews.size() - 1; 0 < i + 1; i = i - 1) {

			HBox reviewItem = new HBox(10);
			VBox reviewLeft = new VBox(10);
			VBox buttons = new VBox(12);
			HBox stars = ProfileViewer.createStarHBox(2, 14,
					houseReviews.get(i).rating);

			Label reviewText = new Label(houseReviews.get(i).review + "\n"
					+ "Likes: " + houseReviews.get(i).like + "  Dislikes: "
					+ houseReviews.get(i).dislike);
			reviewText.setWrapText(true);
			reviewText.setPrefWidth(450);

			ImageView like = new ImageView(thumbsUp);
			ImageView dislike = new ImageView(thumbsDown);

			if (!landlord.username.equals(currentUsername)) {
				like.setOnMouseClicked(new likeHandler(houseReviews.get(i)));
				like.setCursor(Cursor.HAND);
				dislike.setOnMouseClicked(new dislikeHandler(houseReviews
						.get(i)));
				dislike.setCursor(Cursor.HAND);
			}

			reviewLeft.getChildren().addAll(reviewText, stars);
			buttons.getChildren().addAll(like, dislike);
			reviewItem.getChildren().addAll(reviewLeft, buttons);
			reviews.add(reviewItem);
		}

		pane.add(reviewsView, 0, 3);
		GridPane.setConstraints(reviewsView, 0, 3, 3, 1, HPos.CENTER,
				VPos.CENTER);
	}

	/**
	 * Sets up a text field for entering a review.
	 */
	public void setupReviewsTextField() {

		newReviewText = new TextArea();
		newReviewText.setPromptText(Translator.translateText(languageIndex,
				"Submit a review") + "...");
		newReviewText.resize(300, 100);
		newReviewText.setWrapText(true);
		pane.add(newReviewText, 0, 5);
		GridPane.setConstraints(newReviewText, 0, 5, 3, 1, HPos.CENTER,
				VPos.CENTER);
	}

	/**
	 * Adds the star rating buttons to the display.
	 */
	public void setupAddStarRating() {

		giveRatingLabel = new Label(Translator.translateText(languageIndex,
				"Give a rating") + ":");
		giveRatingLabel.setFont(new Font(16));

		// Create star buttons
		ButtonType button1 = new ButtonType(null, null, null, 28, 28);
		// Declare array of review star buttons
		buttonStar = new Button[5];

		// Create array of outline star buttons
		for (int i = 0; i < 5; i++) {
			buttonStar[i] = new SetupButton().createButton(button1);
			buttonStar[i] = new SetupButton().setButtonImage(buttonStar[i],
					reviewStarOutline);
			buttonStar[i].setStyle("-fx-background-color: transparent;");
		}

		// HBox to contain the star buttons
		HBox hBoxNewStars = new HBox(5);
		// Populate hBox with stars
		for (int i = 0; i < 5; i++) {
			hBoxNewStars.getChildren().add(buttonStar[i]);
		}

		// Add review star buttons to GridPane
		pane.add(giveRatingLabel, 0, 6);
		GridPane.setHgrow(giveRatingLabel, Priority.ALWAYS);
		GridPane.setConstraints(giveRatingLabel, 0, 6, 1, 1, HPos.RIGHT,
				VPos.CENTER);
		pane.add(hBoxNewStars, 1, 6);
		hBoxNewStars.setAlignment(Pos.CENTER_LEFT);
		GridPane.setConstraints(hBoxNewStars, 1, 6, 1, 1, HPos.LEFT,
				VPos.CENTER);

		// Add review star button handlers
		for (int i = 0; i < 5; i++) {
			buttonStar[i].setOnAction(new starButtonHandler(i));
		}
	}

	/**
	 * Sets up the back button and submit review button.
	 */
	public void setupButtons() {

		SavedProperties.setupPropertyBackButton();

		ButtonType button2 = new ButtonType("166,208,255", null, "Submit", 70,
				30);
		buttonSubmit = new SetupButton().createButton(button2);
		buttonSubmit.relocate(785, 720);
		buttonSubmit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				if (newReviewText.getText().length() != 0) {

					BadWordCheck bwd = new BadWordCheck();

					if (bwd.containsBlackListedWords(newReviewText.getText())) {
						newReviewText.setText(bwd
								.highlightBlackListedWords(newReviewText
										.getText()));
						createWarningPopup("Inappropriate Language");
						dialogStage.show();
					} else {
						House house = Database.getHouse(currentPropertyID);
						User landlord = Database.getUser(Database
								.getUsername(house.uid));
						// Add new review to new line of review text area
						HouseReview newReview = new HouseReview(
								currentPropertyID);
						newReview.uid(landlord.uid);
						newReview.review(newReviewText.getText());
						newReview.rating(newRating);
						newReview.like(0);
						newReview.dislike(0);
						Database.insertHouseReview(newReview);

						loadSlide(REVIEWS);
					}
				}
			}
		});

		pane.add(buttonSubmit, 2, 6);
		GridPane.setConstraints(buttonSubmit, 2, 6, 1, 1, HPos.RIGHT,
				VPos.CENTER);
	}

	/**
	 * Updates the text for the house review page.
	 */
	public void updateLanguage() {

		pageTitle.setText(Translator.translateText(languageIndex, "Reviews"));
		ratingLabel.setText(Translator.translateText(languageIndex, "Rating")
				+ ":    ");
		newReviewLabel.setText(Translator.translateText(languageIndex,
				"Reviews") + ":");
		giveRatingLabel.setText(Translator.translateText(languageIndex,
				"Give a rating") + ":");
		newReviewText.setPromptText(Translator.translateText(languageIndex,
				"Submit a review") + "...");
		buttonSubmit.setText(Translator.translateText(languageIndex, "Submit"));
	}

	/**
	 * This class handles changing the buttons for the star rating.
	 * 
	 * @version 2.6
	 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
	 */
	public class starButtonHandler implements EventHandler<ActionEvent> {

		private int buttonNumber;

		/**
		 * Constructor method
		 * 
		 * @param number
		 *            New rating number
		 */
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

	/**
	 * This class handles updating a like of a review.
	 * 
	 * @version 2.6
	 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
	 */
	public class likeHandler implements EventHandler<MouseEvent> {

		private HouseReview review;

		/**
		 * Constructor method
		 * 
		 * @param review
		 *            The selected review to update like for
		 */
		public likeHandler(HouseReview review) {

			this.review = review;
		}

		@Override
		public void handle(MouseEvent event) {

			Database.likeReview(Database.getUser(currentUsername), review,
					null, 2);
			loadSlide(REVIEWS);
		}
	}

	/**
	 * This class handles updating a dislike of a review.
	 * 
	 * @version 2.6
	 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
	 */
	public class dislikeHandler implements EventHandler<MouseEvent> {

		private HouseReview review;

		/**
		 * Constructor method
		 * 
		 * @param review
		 *            The selected review to update dislike for
		 */
		public dislikeHandler(HouseReview review) {

			this.review = review;
		}

		@Override
		public void handle(MouseEvent event) {

			Database.dislikeReview(Database.getUser(currentUsername), review,
					null, 2);
			loadSlide(REVIEWS);
		}
	}
}