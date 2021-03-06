package landlord;

import java.util.ArrayList;

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
import button.ButtonType;
import button.SetupButton;
import database.Database;
import database.House;
import database.HouseImage;
import database.User;

/**
 * This class creates the landlord properties page for viewing a list of all the
 * properties created and managed by the current landlord logged in.
 * 
 * @version 3.8 (01.06.15)
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class LandlordProperties extends Window {

	// Landlord Properties instance variables
	private GridPane grid = new GridPane();
	private ArrayList<House> properties = new ArrayList<House>();
	private ListView<HBox> propertyList = new ListView<HBox>();
	private ObservableList<HBox> items = FXCollections.observableArrayList();
	private User viewedUser;

	/**
	 * Retrieves and displays landlord properties based on username input.
	 * 
	 * @param pageUsername
	 *            The username of the landlord properties to display.
	 */
	public LandlordProperties(String pageUsername) {

		// Retrieve current user information from database
		viewedUser = Database.getUser(pageUsername);

		// Setup size and spacing of gridpane layout object
		setupGrid();

		// Add the title to the screen
		setupTitle();

		// Add controls to the screen, and handle the inputs
		setupLandlordButtons();

		// Populate list with owned properties
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
				"Manage Properties"));

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
	public void setupLandlordButtons() {

		// Create box to contain buttons
		VBox buttons = new VBox(30);

		// Create buttons in chosen language
		ButtonType button1 = new ButtonType("166,208,255", null,
				Translator.translateText(languageIndex, "Edit"), 100, 30);
		ButtonType button2 = new ButtonType("166,208,255", null,
				Translator.translateText(languageIndex, "Delete"), 100, 30);
		ButtonType button3 = new ButtonType("166,208,255", null,
				Translator.translateText(languageIndex, "New"), 100, 30);
		ButtonType button4 = new ButtonType("166,208,255", null,
				Translator.translateText(languageIndex, "View"), 100, 30);

		Button buttonEdit = new SetupButton().createButton(button1);
		Button buttonDelete = new SetupButton().createButton(button2);
		Button buttonNew = new SetupButton().createButton(button3);
		Button buttonView = new SetupButton().createButton(button4);

		// Set cursor image on hover
		buttonEdit.setCursor(Cursor.HAND);
		buttonDelete.setCursor(Cursor.HAND);
		buttonNew.setCursor(Cursor.HAND);
		buttonView.setCursor(Cursor.HAND);

		// Set button handler for "Edit" button
		buttonEdit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {

				// Check a property has been selected
				if (propertyList.getSelectionModel().getSelectedIndex() >= 0) {

					// Check which property is selected
					int index = propertyList.getSelectionModel()
							.getSelectedIndex();

					// Set the current property ID to the selected house
					currentPropertyID = properties.get(index).hid;

					// Open the edit property page
					loadSlide(EDITPROPERTY);
				}
			}
		});

		// Set button handler for "Delete" button
		buttonDelete.setOnAction(new EventHandler<ActionEvent>() {
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

					// Remove the selected house from the database
					Database.houseDelete(properties.get(index), viewedUser);

					// Remove the selected house from local list of houses
					properties.remove(index);

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
					currentPropertyID = properties.get(index).hid;

					// Open the single property view page
					loadSlide(HOUSE);
				}
			}
		});

		// Set button handler for "New" button
		buttonNew.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {

				// Load an edit property slide in the "create" configuration
				currentPropertyID = 0;
				loadSlide(EDITPROPERTY);
			}
		});

		// Add buttons to grid and align button group
		buttons.getChildren().addAll(buttonNew, buttonView, buttonEdit,
				buttonDelete);
		buttons.setAlignment(Pos.CENTER);
		grid.add(buttons, 1, 1);
	}

	/**
	 * Populates property list view with owned properties from the database.
	 */
	public void setupPropertyList() {

		// Set ListView object height
		propertyList.setPrefHeight(550);

		// Retrieve owned properties from database
		properties = Database.getLandlordProperties(viewedUser.uid);

		// Loop based on number of houses owned by landlord
		for (int i = 0; i < properties.size(); i++) {

			// Setup property information containers
			HBox listItem = new HBox(10);
			VBox propertyInfo = new VBox(10);

			// Retrieve current property images
			ArrayList<HouseImage> houseImages = new ArrayList<HouseImage>();
			houseImages = Database.getHouseImageSet(properties.get(i).hid);

			// Setup thumbnail of first property image
			HouseImage input = houseImages.get(0);
			ImageView thumbnail = new ImageView(new Image(input.imageIS));

			// Resize thumbnail
			thumbnail.setFitHeight(100);
			thumbnail.setFitWidth(100);

			// Get current property from list of landlord properties
			House house = properties.get(i);

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
					+ " �"
					+ house.price + " pppw");

			// Add the address and house info labels to a VBox
			propertyInfo.getChildren().addAll(propertyAddress, propertyDetails);

			// Add thumbnail and property information to the current list item
			listItem.getChildren().addAll(thumbnail, propertyInfo);

			// Add the current list item to list
			items.add(listItem);
		}

		// Add the list to the ListView object
		propertyList.setItems(items);

		// Add the list view to the grid pane
		grid.add(propertyList, 0, 1);
	}

	/**
	 * Frees allocated memory from objects in the landlord properties page.
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
