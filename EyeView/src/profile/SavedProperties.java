package profile;

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
import javafx.scene.input.MouseEvent;
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

/**
 * This class creates the saved properties page which displays all the
 * properties that have been saved by the user to view at a later date.
 * 
 * @version 2.4 (01.06.15)
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class SavedProperties extends Window {

	// Saved Properties instance variables
	GridPane grid = new GridPane();
	ArrayList<String> properties = new ArrayList<String>();
	ListView<HBox> propertyList = new ListView<HBox>();
	ObservableList<HBox> items = FXCollections.observableArrayList();

	/**
	 * Constructor method
	 */
	public SavedProperties() {

		// Setup size and spacing of gridpane layout object
		setupGrid();

		// Add the title to the screen
		setupTitle();

		// Add controls to the screen, and handle the inputs
		setupButtons();

		// Populate list with saved properties
		setupPropertyList();

		// Add back button to the screen
		SlideContent.setupBackButton();

		// Add layout object to the screen group
		root.getChildren().add(grid);
	}

	/**
	 * Sets up grid layout object.
	 */
	public void setupGrid() {

		// Setup column sizes
		ColumnConstraints col1 = new ColumnConstraints();
		ColumnConstraints col2 = new ColumnConstraints();
		col1.setMinWidth(500);
		col2.setMinWidth(100);
		grid.getColumnConstraints().addAll(col1, col2);

		// Setup grid spacing
		grid.setVgap(30);
		grid.setHgap(30);
		grid.relocate(220, 130);
	}

	/**
	 * Sets up title text.
	 */
	public void setupTitle() {

		// Create label in chosen language
		Label labelTitle = new Label(Translator.translateText(languageIndex,
				"Saved Properties"));

		// Configure style of text
		labelTitle.setTextFill(Color.web("#162252"));
		labelTitle.setFont(new Font(35));
		labelTitle.setPrefWidth(550);
		labelTitle.setAlignment(Pos.CENTER);

		// Locate title and add to the group
		labelTitle.relocate(275, 80);
		root.getChildren().add(labelTitle);
	}

	/**
	 * Sets up control buttons.
	 */
	public void setupButtons() {

		// Create box to contain buttons
		VBox buttons = new VBox(30);

		// Create buttons in chosen language
		ButtonType button1 = new ButtonType("166,208,255", null,
				Translator.translateText(languageIndex, "View"), 100, 30);
		ButtonType button2 = new ButtonType("166,208,255", null,
				Translator.translateText(languageIndex, "Remove"), 100, 30);

		Button buttonView = new SetupButton().createButton(button1);
		Button buttonRemove = new SetupButton().createButton(button2);

		// Set cursor image on hover
		buttonView.setCursor(Cursor.HAND);
		buttonRemove.setCursor(Cursor.HAND);

		// Set button handler for "Remove" button
		buttonRemove.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {

				// Check a property has been selected
				if (propertyList.getSelectionModel().getSelectedIndex() >= 0) {

					// Check which property is selected
					int index = propertyList.getSelectionModel()
							.getSelectedIndex();

					// Shuffle items after deletion up the list
					for (int i = index; i < items.size() - 1; i++) {
						items.set(i, items.get(i + 1));
					}

					// Remove last item which is now a duplicate
					items.remove(items.size() - 1);

					// Remove selected house ID from list of saved house IDs
					properties.remove(index);

					// Update profile saved properties on the database
					User.updateSavedProperties(currentUsername, properties);
				}

			}
		});

		// Set button handler for "View" button
		buttonView.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {

				// Check a property has been selected
				if (propertyList.getSelectionModel().getSelectedIndex() >= 0) {

					// Check which property is selected
					int index = propertyList.getSelectionModel()
							.getSelectedIndex();

					// Set the current property ID to the selected house
					currentPropertyID = Integer.parseInt(properties.get(index));
					loadSlide(HOUSE);
				}
			}
		});

		// Add buttons to grid and align button group
		buttons.getChildren().addAll(buttonView, buttonRemove);
		buttons.setAlignment(Pos.CENTER);
		grid.add(buttons, 1, 1);

	}

	/**
	 * Populate property list view with saved properties from the database.
	 */
	public void setupPropertyList() {

		// Set ListView object height
		propertyList.setPrefHeight(550);

		// Retrieve saved property IDs from database
		properties = User.getSavedProperties(currentUsername);

		// Loop based on number of houses saved in profile.
		for (int i = 0; i < properties.size(); i++) {

			// Setup property information containers
			HBox listItem = new HBox(10);
			VBox propertyInfo = new VBox(10);

			// Retrieve current property images
			ArrayList<HouseImage> houseImages = new ArrayList<HouseImage>();
			houseImages = Database.getHouseImageSet(Integer.parseInt(properties
					.get(i)));

			// Setup thumbnail of first property image
			HouseImage input = houseImages.get(0);
			ImageView thumbnail = new ImageView(new Image(input.imageIS));

			// Resize thumbnail
			thumbnail.setFitHeight(100);
			thumbnail.setFitWidth(100);

			// Get current property from the database using the house ID
			House house = Database
					.getHouse(Integer.parseInt(properties.get(i)));

			// Create label with address of current property
			Label propertyAddress = new Label(house.address);

			// Setup address label text wrapping, width and make bold
			propertyAddress.setWrapText(true);
			propertyAddress.setMaxWidth(300);
			propertyAddress.setFont(Font.font(null, FontWeight.BOLD, 20));

			// Create label with price and number of bedrooms
			Label propertyDetails = new Label(Translator.translateText(
					languageIndex, "Bedrooms: ")
					+ house.rooms
					+ "\n"
					+ Translator.translateText(languageIndex, "Price:")
					+ " £"
					+ house.price + " pppw");

			// Add the address and house info labels to a VBox
			propertyInfo.getChildren().addAll(propertyAddress, propertyDetails);

			// Add the thumbnail and property information to the list item
			listItem.getChildren().addAll(thumbnail, propertyInfo);

			// Free memory space by removing temporary image files
			houseImages.clear();
			houseImages = null;
			house = null;

			// Add the current list item to list
			items.add(listItem);
		}

		// Add the list to the ListView object
		propertyList.setItems(items);

		// Add the list view to the grid pane
		grid.add(propertyList, 0, 1);
	}

	/**
	 * Create a back button from the property page based on the origin of the
	 * page.
	 */
	public static void setupPropertyBackButton() {

		// Create ImageView of the back arrow
		ImageView buttonBack = new ImageView(new Image(
				"file:resources/advert_icons/back.png"));

		// Set the location of the back button
		buttonBack.relocate(195, 8);

		// Prevent image warping and resize
		buttonBack.setPreserveRatio(true);
		buttonBack.setFitHeight(35);

		// Set cursor image on hover
		buttonBack.setCursor(Cursor.HAND);

		// Set button handler for back button
		buttonBack.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent ae) {

				// If the house is viewed via the saved properties page
				if (originSavedProperties) {
					switch (slideID) {
					case VIDEO:
					case REVIEWS:
					case MAP:
					case MOREINFO:
						// Return to the house overview page
						loadSlide(HOUSE);
						break;
					default:
						// Return to the saved properties page
						loadSlide(SAVEDPROPERTIES);
						break;
					}
				}
				// If the house is viewed via the landlord properties page
				else if (originManageProperties) {
					switch (slideID) {
					case VIDEO:
					case REVIEWS:
					case MAP:
					case MOREINFO:
						// Return to the house overview page
						loadSlide(HOUSE);
						break;
					default:
						// Return to the landlord properties page.
						loadSlide(LANDLORDPROPERTIES);
						break;
					}
				} else {
					/*
					 * Return to the main house overview section if house pages
					 * did not originate from saved or landlord properties
					 */
					loadSlide(HOUSES);
				}
			}
		});

		// Add the back button to the group
		root.getChildren().add(buttonBack);
	}

	/**
	 * Free allocated memory from objects in the landlord properties page.
	 */
	public void dispose() {

		// Clear all local lists and array lists to free memory space
		properties.clear();
		properties.trimToSize();
		properties = null;
		propertyList = null;
		items.clear();
		items = null;
	}
}
