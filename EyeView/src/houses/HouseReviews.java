package houses;

import button.ButtonType;
import button.SetupButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import language.Translator;
import presenter.SlideContent;
import presenter.Window;

public class HouseReviews extends Window {

	private static Label pageTitle = new Label("");
	private static Label ratingLabel = new Label(Translator.translateText(languageIndex, "Rating") + ":    ");
	private static Label giveRatingLabel = new Label("");
	private static ButtonType button2 = new ButtonType("166,208,255", null,
			"Submit", 70, 30);
	private static Button buttonSubmit = new SetupButton()
			.CreateButton(button2);

	private int houseRating = 2; // Overall house rating from database

	private ListView<VBox> reviewsView;
	private ObservableList<VBox> reviews = FXCollections.observableArrayList();

	private Button[] buttonStar;
	private ImageView[] reviewStar;
	private Image reviewStarFull = new Image(
			"file:resources/images/stars/starFull_28.png");
	private Image reviewStarOutline = new Image(
			"file:resources/images/stars/starOutline_28.png");
	private int newRating;

	GridPane pane;
	TextArea newReviewText;
	HBox hBoxOverallRating = new HBox(5);

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

	public void setupGrid() {

		pane.setVgap(20);
		pane.setHgap(30);
		pane.relocate(315, 80);
		pane.setPrefWidth(450);

		// pane.setGridLinesVisible(true);

		// Set column widths of grid.
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setMinWidth(150);
		pane.getColumnConstraints().addAll(col1, col1, col1);
	}

	private void setupTitle() {

		pageTitle = new Label(Translator.translateText(languageIndex, "Reviews"));
		pageTitle.setFont(new Font(32));
		pageTitle.setTextFill(Color.web("#162252FF"));
		pane.add(pageTitle, 0, 0);
		GridPane.setConstraints(pageTitle, 0, 0, 3, 1, HPos.CENTER, VPos.CENTER);
	}

	public void setupRatingLabel() {

		ratingLabel.setFont(new Font(20));
		hBoxOverallRating.getChildren().add(ratingLabel);
	}

	/**
	 * Create stars to display average house rating from database
	 */
	public void setupAveRating() {

		reviewStar = new ImageView[5];

		for (int i = 0; i < 5; i++) {
			if (i <= houseRating - 1) {
				reviewStar[i] = new ImageView(reviewStarFull);
				reviewStar[i].setFitHeight(28);
				reviewStar[i].setFitWidth(28);
			} else {
				reviewStar[i] = new ImageView(reviewStarOutline);
			}
		}

		hBoxOverallRating.getChildren().addAll(reviewStar);
		hBoxOverallRating.setAlignment(Pos.CENTER_LEFT);
		pane.add(hBoxOverallRating, 0, 1);
		GridPane.setConstraints(hBoxOverallRating, 0, 1, 3, 1, HPos.LEFT,
				VPos.CENTER);
	}

	public void displayReviews() {

		Label newReviewLabel = new Label(Translator.translateText(languageIndex,
				"Reviews") + ":");
		newReviewLabel.setFont(new Font(20));
		pane.add(newReviewLabel, 0, 2);

		reviewsView = new ListView<VBox>();
		reviewsView.setPrefHeight(200);
		reviewsView.setItems(reviews);

		pane.add(reviewsView, 0, 3);
		GridPane.setConstraints(reviewsView, 0, 3, 3, 1, HPos.CENTER,
				VPos.CENTER);
	}

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

	public void setupAddStarRating() {

		giveRatingLabel.setText(Translator.translateText(languageIndex,
				"Give a rating") + ":");
		giveRatingLabel.setFont(new Font(20));

		// Create star buttons
		ButtonType button1 = new ButtonType(null, null, null, 28, 28);
		// Declare array of review star buttons
		buttonStar = new Button[5];

		// Create array of outline star buttons
		for (int i = 0; i < 5; i++) {
			buttonStar[i] = new SetupButton().CreateButton(button1);
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

		// Add review star buttons to gridpane
		pane.add(giveRatingLabel, 0, 6);
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

	public void setupButtons() {

		SlideContent.setupBackButton();

		buttonSubmit.relocate(785, 720);
		buttonSubmit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				if (newReviewText.getText().length() != 0) {

					HBox newStarRatingHBox = new HBox(2);

					ImageView[] newReviewStar = new ImageView[5];

					for (int i = 0; i < 5; i++) {
						if (i <= newRating - 1) {
							newReviewStar[i] = new ImageView(reviewStarFull);
							newReviewStar[i].setFitHeight(14);
							newReviewStar[i].setFitWidth(14);
						} else {
							newReviewStar[i] = new ImageView(reviewStarOutline);
							newReviewStar[i].setFitHeight(14);
							newReviewStar[i].setFitWidth(14);
						}
					}

					newStarRatingHBox.getChildren().addAll(newReviewStar);
					newStarRatingHBox.setAlignment(Pos.CENTER_LEFT);

					Label newReviewTextLabel = new Label(newReviewText
							.getText());
					newReviewTextLabel.setPrefWidth(445);
					newReviewTextLabel.setWrapText(true);

					VBox newReviewVBox = new VBox(10);
					newReviewVBox.setPrefWidth(445);
					newReviewVBox.getChildren().addAll(newReviewTextLabel,
							newStarRatingHBox);
					reviews.add(newReviewVBox);

					reviewsView.setItems(reviews);

					newReviewText.clear();
				}
			}
		});

		pane.add(buttonSubmit, 2, 6);
		GridPane.setConstraints(buttonSubmit, 2, 6, 1, 1, HPos.RIGHT,
				VPos.CENTER);
	}

	public static void updateLanguage() {

		pageTitle.setText(Translator.translateText(languageIndex, "Reviews"));
		ratingLabel.setText(Translator.translateText(languageIndex, "Rating")
				+ ":     ");
		buttonSubmit.setText(Translator.translateText(languageIndex, "Submit"));
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
					// System.out.println(reviewStarFull.toString());
				} else {
					buttonStar[i].setGraphic(new ImageView(reviewStarOutline));
				}
			}
		}
	}
}