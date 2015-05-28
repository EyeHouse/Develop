package Profile;

import java.util.ArrayList;

import database.Database;
import database.House;
import database.HouseImage;
import database.User;
import Button.ButtonType;
import Button.SetupButton;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import language.Translate;
import presenter.SlideContent;

public class SavedProperties extends presenter.Window {

	GridPane grid = new GridPane();
	ArrayList<String> properties = new ArrayList<String>();
	ListView<HBox> propertyList = new ListView<HBox>();
	ObservableList<HBox> items = FXCollections.observableArrayList();

	public Label labelTitle = new Label();

	public SavedProperties() {

		setupGrid();
		setupTitle();
		setupButtons();
		setupPropertyList();

		// add BackButton
		SlideContent.setupBackButton();

		root.getChildren().add(grid);
	}

	private void setupGrid() {

		ColumnConstraints col1 = new ColumnConstraints();
		ColumnConstraints col2 = new ColumnConstraints();
		col1.setMinWidth(500);
		col2.setMinWidth(100);
		grid.getColumnConstraints().addAll(col1, col2);

		grid.setVgap(30);
		grid.setHgap(30);
		grid.relocate(220, 80);
	}

	private void setupTitle() {

		labelTitle = new Label(Translate.translateText(languageIndex,
				"Saved Properties"));
		labelTitle.setTextFill(Color.web("#162252FF"));
		labelTitle.setFont(new Font(35));
		grid.add(labelTitle, 0, 0);
		GridPane.setConstraints(labelTitle, 0, 0, 2, 1, HPos.CENTER,
				VPos.CENTER);
	}

	private void setupButtons() {
		VBox buttons = new VBox(30);

		// View button//
		ButtonType button1 = new ButtonType("150,150,150", null,
				Translate.translateText(languageIndex, "View"), 100, 30);

		// Remove button//
		ButtonType button2 = new ButtonType("150,150,150", null,
				Translate.translateText(languageIndex, "Remove"), 100, 30);

		Button buttonView = new SetupButton().CreateButton(button1);
		Button buttonRemove = new SetupButton().CreateButton(button2);

		buttonView.setCursor(Cursor.HAND);
		buttonRemove.setCursor(Cursor.HAND);
		buttonRemove.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				int index = propertyList.getSelectionModel().getSelectedIndex();

				for (int i = index; i < items.size() - 1; i++) {
					items.set(i, items.get(i + 1));
				}

				items.remove(items.size() - 1);

				properties.remove(index); // update database of current user
				User.updateSavedProperties(currentUsername, properties);
			}
		});

		buttonView.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				int index = propertyList.getSelectionModel().getSelectedIndex();
				currentPropertyID = Integer.parseInt(properties.get(index));
				root.getChildren().clear();
				slideID = HOUSE;
				SlideContent sc = new SlideContent();
				sc.createSlide();
				// Open single property advert of selection
			}
		});

		buttons.getChildren().addAll(buttonView, buttonRemove);
		buttons.setAlignment(Pos.CENTER);
		grid.add(buttons, 1, 1);

	}

	private void setupPropertyList() {

		propertyList.setPrefHeight(550);

		properties = User.getSavedProperties(currentUsername);

		// Loop based on number of houses saved in profile.
		for (int i = 0; i < properties.size(); i++) {

			HBox listItem = new HBox(10);
			VBox propertyInfo = new VBox(10);

			ArrayList<HouseImage> houseImages = new ArrayList<HouseImage>();
			houseImages = Database.getHouseImageSet(Integer.parseInt(properties
					.get(i)));
			HouseImage input = houseImages.get(0);
			ImageView thumbnail = new ImageView(new Image(input.imageIS));
			thumbnail.setFitHeight(100);
			thumbnail.setFitWidth(100);

			House house = Database
					.getHouse(Integer.parseInt(properties.get(i)));
			// Rectangle propertyImage = new Rectangle(100, 100,
			// Color.BLUEVIOLET);
			Label propertyAddress = new Label(house.address);
			Label propertyDetails = new Label("No. of Bedrooms: " + house.rooms
					+ "\nPrice: £" + house.price + " pcm");

			propertyAddress.setFont(Font.font(null, FontWeight.BOLD, 20));

			propertyInfo.getChildren().addAll(propertyAddress, propertyDetails);
			listItem.getChildren().addAll(thumbnail, propertyInfo);

			items.add(listItem);
		}
		propertyList.setItems(items);

		grid.add(propertyList, 0, 1);
	}

	public static void setupPropertyBackButton() {
		
		ImageView buttonBack = new ImageView(new Image("file:resources/advert_icons/back.png"));
		buttonBack.relocate(200, 20);
		buttonBack.setPreserveRatio(true);
		buttonBack.setFitWidth(80);
		buttonBack.setCursor(Cursor.HAND);
		buttonBack.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent ae) {
				loadSlide(prevSlideID);
			}
		});
		
		root.getChildren().add(buttonBack);
	}

	public void UpdateLanguage() {
		labelTitle.setText(Translate.translateText(languageIndex,
				"Saved Properties") + ": ");
	}
}
