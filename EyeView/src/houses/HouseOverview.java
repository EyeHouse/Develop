package houses;

import java.util.ArrayList;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import javafx.util.Duration;
import language.Translate;
import presenter.Window;
import Profile.SavedProperties;
import database.Database;
import database.House;
import database.HouseImage;
import database.User;

/**
 * This class creates the main house viewing slide within the EyeView software.
 * The house overview page displays a selection of house adverts that can be
 * played in a loop.
 * 
 * Each advert displays a gallery of images, along with the house address, price
 * (per person per week), number of bedrooms, and a link to the landlord
 * profile.
 * 
 * The page is constructed differently depending if a user is logged in, or if
 * the user is viewing a single property as opposed to a collection.
 * 
 * @version 1.0
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class HouseOverview extends Window {

	private ImageGallery gallery;
	private Pagination pagination;
	private ArrayList<String> savedProperties = new ArrayList<String>();
	private ArrayList<House> houses = new ArrayList<House>();
	private ArrayList<ArrayList<Image>> galleries = new ArrayList<ArrayList<Image>>();
	private Label labelBedrooms = new Label();
	private Label labelLandlord = new Label();
	private static ImageView playpauseButton;
	private static Image play = new Image(
			"file:resources/advert_icons/play.png");
	private static Image pause = new Image(
			"file:resources/advert_icons/pause.png");
	private Image save = new Image("file:resources/advert_icons/save.png");
	private Image saved = new Image("file:resources/advert_icons/saved.png");

	/**
	 * Constructor method
	 * 
	 * @param singlePropertyView
	 *            True if the page is constructed as a single house viewing
	 * @param houses
	 *            List containing all the houses retrieved from the database
	 */
	public HouseOverview(boolean singlePropertyView, ArrayList<House> houses) {

		this.houses = houses;

		createGalleryLists();

		// Only set up advert timer loop if viewing more than one house
		if (!singlePropertyView) {
			savedProperties = User.getSavedProperties(currentUsername);
			setupAdvertTimer();
			setupTimerControl();
		}
		setupPagination();
	}

	/**
	 * Creates an array list containing array lists of all the images for all
	 * the houses retrieved from the database.
	 */
	private void createGalleryLists() {

		// Get all the matching house image collections from the database
		for (int i = 0; i < houses.size(); i++) {
			ArrayList<Image> galleryList = new ArrayList<Image>();
			ArrayList<HouseImage> houseImages = new ArrayList<HouseImage>();

			// Get the images from the database for the current house
			houseImages = Database.getHouseImageSet(houses.get(i).hid);

			// Get the images from the database for this house
			for (int j = 0; j < houseImages.size(); j++) {
				HouseImage input = houseImages.get(j);
				Image image = new Image(input.imageIS);
				galleryList.add(image);
			}

			// If the house has an energy rating picture it is included
			if (houses.get(i).energyRatingIS != null) {
				Image image = new Image(houses.get(i).energyRatingIS);
				galleryList.add(image);
			}

			galleries.add(galleryList);
		}
	}

	/**
	 * Creates a single house page which is either shown on its own or placed as
	 * a Node within a Pagination object to display multiple adverts.
	 * 
	 * @param pageIndex
	 *            Index of the house to be displayed
	 * @return Pane containing the constructed house page
	 */
	protected Pane createHousePage(Integer pageIndex) {

		// Create new pane to contain house advert objects
		Pane galleryPane = new Pane();
		galleryPane.setPrefSize(750, 520);
		galleryPane.getChildren().clear();

		// Create new image gallery with this house's images
		gallery = new ImageGallery(galleries.get(pageIndex), 20, 80);
		galleryPane.getChildren().add(gallery.getGallery());

		// Label showing house address
		Label address = new Label(houses.get(pageIndex).address);
		address.setFont(Font.font(null, FontWeight.BOLD, 24));
		Label price = new Label("£"
				+ Integer.toString(houses.get(pageIndex).price) + "  (pppw)");
		price.setFont(new Font(20));

		// Labels showing number of bedrooms
		HBox bedroomsBox = new HBox();
		labelBedrooms = new Label(Translate.translateText(languageIndex,
				"Bedrooms") + ":  ");
		labelBedrooms.setFont(new Font(20));
		Label bedrooms = new Label(
				Integer.toString(houses.get(pageIndex).rooms));
		bedroomsBox.getChildren().addAll(labelBedrooms, bedrooms);
		bedrooms.setFont(new Font(20));

		// Label showing landlord's name
		HBox landlordBox = new HBox();
		labelLandlord = new Label(Translate.translateText(languageIndex,
				"Landlord") + ":  ");
		labelLandlord.setFont(new Font(20));
		final User landlordUser = Database.getUser(Database.getUsername(houses
				.get(pageIndex).uid));

		// Hyperlink to landlord's profile page
		Hyperlink landlord = new Hyperlink(landlordUser.first_name);
		landlord.setFont(new Font(20));
		if (currentUsername != null) {
			landlord.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent ae) {
					viewedUsername = landlordUser.username;
					loadSlide(PROFILE);
				}
			});
		} else {
			// If user is logged out, landlord's profile is not available
			landlord.setDisable(true);
		}
		landlordBox.getChildren().addAll(labelLandlord, landlord);
		landlordBox.setAlignment(Pos.BASELINE_LEFT);

		// Add all the house information to the VBox
		VBox propertyInfo = new VBox(20);
		propertyInfo.getChildren().addAll(address, price, bedroomsBox,
				landlordBox);
		propertyInfo.relocate(450, 100);

		// Creates the icon for saving a house to view later
		setupSaveButton(galleryPane);

		// Set up back button if this is a single house viewing
		if (slideID == HOUSE) {
			SavedProperties.setupPropertyBackButton();
		}

		galleryPane.getChildren().add(propertyInfo);

		return galleryPane;
	}

	/**
	 * Method for creating a button that allows the user to save a house in a
	 * list of favourites to view later.
	 * 
	 * @param galleryPane
	 *            The current pane that the save button is added to
	 */
	private void setupSaveButton(final Pane galleryPane) {

		// Hidden label appears next to button when enabled and hovered over
		final Label saveLabel = new Label("Save for later");
		saveLabel.setFont(Font.font(null, FontWeight.BOLD, 14));
		saveLabel.relocate(520, 415);

		// Save button with no border and graphic only
		final Button buttonSave = new Button("", new ImageView(save));
		buttonSave.setStyle("-fx-padding: 0;");
		buttonSave.setCursor(Cursor.HAND);
		buttonSave.relocate(460, 400);
		if (savedProperties.contains(String.format("%03d", currentPropertyID))) {
			buttonSave.setDisable(true);
			buttonSave.setGraphic(new ImageView(saved));
		}
		buttonSave.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				galleryPane.getChildren().add(saveLabel);
			}
		});
		buttonSave.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				galleryPane.getChildren().remove(saveLabel);
			}
		});
		buttonSave.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				buttonSave.setDisable(true);
				buttonSave.setGraphic(new ImageView(saved));
				savedProperties.add(String.format("%03d", currentPropertyID));
				User.updateSavedProperties(currentUsername, savedProperties);
			}
		});

		// Save button not displayed on single view house advert
		if (slideID != INDEX && slideID != HOUSE) {
			galleryPane.getChildren().add(buttonSave);
		}
	}

	/**
	 * Creates the pagination object for containing all the house pages.
	 */
	private void setupPagination() {

		pagination = new Pagination(houses.size(), 0);

		pagination.setPageFactory(new Callback<Integer, Node>() {
			public Node call(Integer pageIndex) {
				if (advertTimer.getStatus().equals(Status.RUNNING)) {
					advertTimer.playFromStart();
				}
				currentPropertyID = houses.get(pageIndex).hid;
				return createHousePage(pageIndex);
			}
		});
		pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
		pagination.relocate(195, 80);

		root.getChildren().addAll(pagination);
	}

	/**
	 * Sets up the play/pause button for controlling the house advert slideshow.
	 */
	private void setupTimerControl() {

		playpauseButton = new ImageView(pause);
		playpauseButton.setPreserveRatio(true);
		playpauseButton.setFitWidth(30);
		playpauseButton.relocate(554, 670);
		playpauseButton.setCursor(Cursor.HAND);
		playpauseButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent ae) {
				if (!advertTimer.getStatus().equals(Status.PAUSED)) {
					setTimerState("PAUSE");
				} else {
					setTimerState("PLAY");
				}
			}
		});

		root.getChildren().addAll(playpauseButton);
	}

	/**
	 * Method that handles the timeline event for playing through the pagination
	 * and sets the timeline to start playing by default.
	 */
	private void setupAdvertTimer() {
		
		advertTimer = new Timeline(new KeyFrame(Duration.millis(5 * 1000),
				new EventHandler<ActionEvent>() {
					public void handle(ActionEvent ae) {
						int index = pagination.getCurrentPageIndex();
						index++;
						if (index >= pagination.getPageCount())
							index = 0;
						pagination.setCurrentPageIndex(index);

					}
				}));
		advertTimer.play();
	}

	/**
	 * Sets the advert timer to toggle play or pause depending on the current
	 * state of the advert timer.
	 * 
	 * @param newState
	 *            The new state of the play/pause function to be set
	 */
	public static void setTimerState(String newState) {

		switch (newState) {
		case "PLAY":
			playpauseButton.setImage(pause);
			advertTimer.play();
			break;
		case "PAUSE":
			playpauseButton.setImage(play);
			advertTimer.pause();
			break;
		}
	}

	/**
	 * Updates the text for the advert page when the language has been changed.
	 */
	public void updateLanguage() {

		labelBedrooms.setText(Translate
				.translateText(languageIndex, "Bedrooms") + ":  ");
		labelLandlord.setText(Translate
				.translateText(languageIndex, "Landlord") + ":  ");
	}
}