package landlord;

import java.util.ArrayList;

import button.ButtonType;
import button.SetupButton;
import database.Database;
import database.House;
import database.HouseImage;
import database.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import language.Translator;
import presenter.SlideContent;
import presenter.Window;

public class LandlordProperties extends Window {

	GridPane grid = new GridPane();
	ArrayList<House> properties = new ArrayList<House>();
	ListView<HBox> propertyList = new ListView<HBox>();
	ObservableList<HBox> items = FXCollections.observableArrayList();
	User viewedUser;

	public LandlordProperties(String pageUsername) {

		viewedUser = Database.getUser(pageUsername);

		setupGrid();
		setupTitle();
		setupLandlordButtons();
		setupPropertyList();

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
		grid.relocate(220, 130);
	}

	private void setupTitle() {

		Label labelTitle = new Label(Translator.translateText(languageIndex,
				"Manage Properties"));
		labelTitle.setTextFill(Color.web("#162252"));
		labelTitle.setFont(new Font(35));
		labelTitle.setPrefWidth(550);
		labelTitle.setAlignment(Pos.CENTER);
		labelTitle.relocate(275, 80);
		
		root.getChildren().add(labelTitle);
	}

	private void setupLandlordButtons() {
		
		VBox buttons = new VBox(30);

		ButtonType button1 = new ButtonType("166,208,255", null,
				Translator.translateText(languageIndex, "Edit"), 100, 30);
		ButtonType button2 = new ButtonType("166,208,255", null,
				Translator.translateText(languageIndex, "Delete"), 100, 30);
		ButtonType button3 = new ButtonType("166,208,255", null,
				Translator.translateText(languageIndex, "New"), 100, 30);
		ButtonType button4 = new ButtonType("166,208,255", null,
				Translator.translateText(languageIndex, "View"), 100, 30);

		Button buttonEdit = new SetupButton().CreateButton(button1);
		Button buttonDelete = new SetupButton().CreateButton(button2);
		Button buttonNew = new SetupButton().CreateButton(button3);
		Button buttonView = new SetupButton().CreateButton(button4);

		buttonEdit.setCursor(Cursor.HAND);
		buttonDelete.setCursor(Cursor.HAND);
		buttonNew.setCursor(Cursor.HAND);
		buttonView.setCursor(Cursor.HAND);

		buttonEdit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (propertyList.getSelectionModel().getSelectedIndex() >= 0) {
					int index = propertyList.getSelectionModel()
							.getSelectedIndex();
					currentPropertyID = properties.get(index).hid;

					loadSlide(EDITPROPERTY);
				}
			}
		});
		buttonDelete.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (propertyList.getSelectionModel().getSelectedIndex() >= 0) {
					int index = propertyList.getSelectionModel()
							.getSelectedIndex();

					for (int i = index; i < items.size() - 1; i++) {
						items.set(i, items.get(i + 1));
					}

					items.remove(items.size() - 1);

					Database.houseDelete(properties.get(index), viewedUser);
					properties.remove(index); // update database of current user

				}
			}
		});

		buttonView.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (propertyList.getSelectionModel().getSelectedIndex() >= 0) {
					if (propertyList.getSelectionModel().getSelectedIndex() >= 0) {
						int index = propertyList.getSelectionModel()
								.getSelectedIndex();
						currentPropertyID = properties.get(index).hid;
						loadSlide(HOUSE);
					}

				}
			}
		});

		buttonNew.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				currentPropertyID = 0;

				loadSlide(EDITPROPERTY);
			}
		});

		buttons.getChildren().addAll(buttonNew, buttonView, buttonEdit,
				buttonDelete);
		buttons.setAlignment(Pos.CENTER);
		grid.add(buttons, 1, 1);
	}

	private void setupPropertyList() {

		propertyList.setPrefHeight(550);

		properties = Database.getLandlordProperties(viewedUser.uid);

		// Loop based on number of houses saved in profile.
		for (int i = 0; i < properties.size(); i++) {

			HBox listItem = new HBox(10);
			VBox propertyInfo = new VBox(10);

			ArrayList<HouseImage> houseImages = new ArrayList<HouseImage>();
			houseImages = Database.getHouseImageSet(properties.get(i).hid);
			HouseImage input = houseImages.get(0);
			ImageView thumbnail = new ImageView(new Image(input.imageIS));
			thumbnail.setFitHeight(100);
			thumbnail.setFitWidth(100);

			House house = properties.get(i);
			Label propertyAddress = new Label(house.address);
			propertyAddress.setWrapText(true);
			propertyAddress.setMaxWidth(300);
			Label propertyDetails = new Label(Translator.translateText(
					languageIndex, "Bedrooms: ")
					+ house.rooms
					+ "\n"
					+ Translator.translateText(languageIndex, "Price:")
					+ " £"
					+ house.price + " pppw");

			propertyAddress.setFont(Font.font(null, FontWeight.BOLD, 20));

			propertyInfo.getChildren().addAll(propertyAddress, propertyDetails);
			listItem.getChildren().addAll(thumbnail, propertyInfo);

			items.add(listItem);
		}
		propertyList.setItems(items);

		grid.add(propertyList, 0, 1);
	}
	
	public void dispose() {
		
		properties.clear();
		properties.trimToSize();
		properties = null;
		propertyList = null;
		items.clear();
		items = null;
	}

}
