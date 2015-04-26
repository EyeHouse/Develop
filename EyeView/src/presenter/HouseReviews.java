package presenter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import Button.ButtonType;
import Button.SetupButton;

public class HouseReviews extends presenter.Window {

	GridPane pane;
	TextArea newReviewText, reviewText;
	String review;
	int allRating = 3;
	int safetyRating = 1;
	int transRating = 3;
	int quietRating = 4;
	int entertRating = 2;
	HBox hBoxOverallRating = new HBox(30);

	public HouseReviews() {
		pane = new GridPane();

		setupGrid();
		setupButtons();
		setupTitle();
		setupStars();
		textfield();
		textShow();
		rating();
		root.getChildren().add(pane);
	}

	public void setupGrid() {

		pane.setVgap(20);
		pane.setHgap(30);
		pane.relocate(250, 80);
		pane.setPrefWidth(450);

		// Set column widths of grid.
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setMinWidth(150);
		pane.getColumnConstraints().addAll(col1, col1, col1);
	}

	public void setupButtons() {

		SlideContent.setupBackButton();

		ButtonType button2 = new ButtonType("144,171,199", null, "Submit", 60,
				25);
		Button buttonSubmit = new SetupButton().CreateButton(button2);
		buttonSubmit.relocate(785, 720);
		buttonSubmit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				System.out.println(newReviewText.getText());
				if (reviewText.getText().length() != 0) {
					reviewText.appendText("\n\n");
				}
				reviewText.appendText(newReviewText.getText());
				newReviewText.clear();

			}
		});

		pane.add(buttonSubmit, 2, 8);
		GridPane.setConstraints(buttonSubmit, 2, 8, 1, 1, HPos.RIGHT,
				VPos.CENTER);

	}

	public void setupTitle() {

		Label Overall = new Label("Overall:");
		Overall.setTextFill(Color.web("#000000"));
		Overall.setFont(new Font(25));
		hBoxOverallRating.getChildren().add(Overall);

		Label Safety = new Label("Safety:");
		Safety.setTextFill(Color.web("#000000"));
		Safety.setFont(new Font(16));

		Label Transport = new Label("Transport:");
		Transport.setTextFill(Color.web("#000000"));
		Transport.setFont(new Font(16));

		Label Noise = new Label("Quiet:");
		Noise.setTextFill(Color.web("#000000"));
		Noise.setFont(new Font(16));

		Label Entertainment = new Label("Entertainment:");
		Entertainment.setTextFill(Color.web("#000000"));
		Entertainment.setFont(new Font(16));

		pane.add(Safety, 0, 1);
		pane.add(Transport, 0, 2);
		pane.add(Noise, 0, 3);
		pane.add(Entertainment, 0, 4);

	}

	private void setupStars() {
		allStar();
		safetyStar();
		transStar();
		quietStar();
		entertStar();
	}

	public void allStar() {
		Image stars = new Image("file:./resources/images/stars/star"
				+ allRating + ".png");
		ImageView blue = new ImageView(stars);
		blue.setFitHeight(40);
		blue.setFitWidth(250);
		hBoxOverallRating.getChildren().add(blue);
		hBoxOverallRating.setAlignment(Pos.CENTER);

		pane.add(hBoxOverallRating, 0, 0);
		GridPane.setConstraints(hBoxOverallRating, 0, 0, 3, 1, HPos.CENTER,
				VPos.CENTER);
	}

	public void safetyStar() {

		Image stars = new Image("file:./resources/images/stars/star"
				+ safetyRating + ".png");
		ImageView blue = new ImageView(stars);
		blue.setFitHeight(25);
		blue.setFitWidth(150);
		pane.add(blue, 1, 1);
	}

	public void transStar() {

		Image stars = new Image("file:./resources/images/stars/star"
				+ transRating + ".png");
		ImageView blue = new ImageView(stars);
		blue.setFitHeight(25);
		blue.setFitWidth(150);
		pane.add(blue, 1, 2);
	}

	public void quietStar() {

		Image stars = new Image("file:./resources/images/stars/star"
				+ quietRating + ".png");
		ImageView blue = new ImageView(stars);
		blue.setFitHeight(25);
		blue.setFitWidth(150);
		pane.add(blue, 1, 3);
	}

	public void entertStar() {

		Image stars = new Image("file:./resources/images/stars/star"
				+ entertRating + ".png");
		ImageView blue = new ImageView(stars);
		blue.setFitHeight(25);
		blue.setFitWidth(150);
		pane.add(blue, 1, 4);
	}

	public void textShow() {

		reviewText = new TextArea();

		reviewText.setEditable(false);
		reviewText.setWrapText(true);
		reviewText.resize(300, 300);
		pane.add(reviewText, 0, 5);
		GridPane.setConstraints(reviewText, 0, 5, 3, 1, HPos.CENTER,
				VPos.CENTER);
	}

	public void textfield() {

		newReviewText = new TextArea();
		newReviewText.setPromptText("Add your feedback...");
		newReviewText.resize(300, 100);
		newReviewText.setWrapText(true);
		pane.add(newReviewText, 0, 7);
		GridPane.setConstraints(newReviewText, 0, 7, 3, 1, HPos.CENTER,
				VPos.CENTER);
	}

	private final ObservableList strings = FXCollections.observableArrayList(
			"1", "2", "3", "4", "5");

	public void rating() {
		HBox hBoxComboboxes = new HBox(40);
		
		Image star = new Image("file:./resources/images/stars/greyStar.png");
		ImageView blue = new ImageView(star);
		
		ButtonType button = new ButtonType("000,000,000", null, "Submit", 25,
				30);
		Button buttonStar = new SetupButton().CreateButton(button);
		buttonStar = new SetupButton().setButtonImage(buttonStar, star);
		
		//hBoxComboboxes.getChildren().add(blue);
		
		ComboBox rate1 = ComboBoxBuilder
				.create()
				.id("uneditable-combobox")
				.promptText("Safety")
				.items(FXCollections.observableArrayList(strings.subList(0, 5)))
				.build();
		hBoxComboboxes.getChildren().add(rate1);

		ComboBox rate2 = ComboBoxBuilder
				.create()
				.id("uneditable-combobox")
				.promptText("Transport")
				.items(FXCollections.observableArrayList(strings.subList(0, 5)))
				.build();
		hBoxComboboxes.getChildren().add(rate2);

		ComboBox rate3 = ComboBoxBuilder
				.create()
				.id("uneditable-combobox")
				.promptText("Quiet")
				.items(FXCollections.observableArrayList(strings.subList(0, 5)))
				.build();
		hBoxComboboxes.getChildren().add(rate3);

		ComboBox rate4 = ComboBoxBuilder
				.create()
				.id("uneditable-combobox")
				.promptText("Entertainment")
				.items(FXCollections.observableArrayList(strings.subList(0, 5)))
				.build();
		hBoxComboboxes.getChildren().add(rate4);
		
		pane.add(hBoxComboboxes, 0, 6);
		hBoxComboboxes.setAlignment(Pos.CENTER);
		GridPane.setConstraints(hBoxComboboxes, 0, 6, 3, 1, HPos.CENTER,
				VPos.CENTER);

	}
}
