package landlord;

import handlers.VideoElement;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import button.ButtonType;
import button.SetupButton;

import presenter.Window;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import language.BadWordCheck;
import language.Translator;
import database.Database;
import database.FileManager;
import database.House;
import database.HouseImage;
import database.HouseVideo;
import database.Marker;
import database.User;

/**
 * This class creates the edit property page. This consists of 3 pages:
 * Information upload, Pictures upload, and Video upload.
 * 
 * @version 3.10 (01.06.15)
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class EditProperty extends Window {

	// Saved Properties instance variables
	private User owner;
	private GridPane grid = new GridPane();
	private House house;

	// Navigation variables
	private int page;
	private int hid;
	private static final int INFO = 0;
	private static final int PICS = 1;
	private static final int VIDEO = 2;
	private HBox pageChangeButtons;
	private VBox backButtons;
	private Button buttonSave = new Button();
	private ImageView infoStatus, picStatus;
	Image tick, cross;

	// House Information variables
	private TextField address = new TextField();
	private TextField postcode = new TextField();
	private TextField price = new TextField();
	private TextField beds = new TextField();
	private TextField baths = new TextField();
	private TextField deposit = new TextField();
	private CheckBox furnished = new CheckBox();
	private TextArea description = new TextArea();
	private ArrayList<ComboBox<String>> dateComboArray = new ArrayList<ComboBox<String>>();

	// House Image variables
	private Button uploadImageButton;
	private TextField uploadPathImage;
	private ArrayList<HouseImage> houseImages;
	private ArrayList<CheckBox> deleteImage = new ArrayList<CheckBox>();
	private ArrayList<String> imagePaths = new ArrayList<String>();

	// House Video variables
	private Button uploadVideoButton;
	private TextField uploadPathVideo;
	private TextField newRoomField;
	private String videoPath;
	private VideoElement video;
	private HouseVideo videoInfo = null;
	private ListView<HBox> markerList = new ListView<HBox>();
	private ObservableList<HBox> items = FXCollections.observableArrayList();
	private ArrayList<Marker> videoMarkers = new ArrayList<Marker>();
	private Button deleteMarker;
	private Button setMarkerButton;
	private int markersIndex;

	// File Chooser variables
	private String filePath;
	private File videoFile;
	private Button fileChooserButton;

	/**
	 * Create property editing page.
	 * 
	 * @param page
	 *            Page index of property editor
	 * @param houseID
	 *            Target house ID (0 = New house)
	 */
	public EditProperty(int page, int houseID) {

		// Retrieve the current user information from the database
		owner = Database.getUser(currentUsername);

		// Intialise page variables
		this.page = page;
		this.hid = houseID;

		// Initialise page status icons
		infoStatus = new ImageView();
		picStatus = new ImageView();
		tick = new Image("file:resources/images/Status/Tick.png");
		cross = new Image("file:resources/images/Status/Cross.png");

		// Add the grid layout object to the group
		root.getChildren().add(grid);

		// Setup the contents of the editor
		createEditPage();
	}

	/**
	 * Populates the edit property page.
	 */
	public void createEditPage() {

		// Setup size and spacing of gridpane layout object
		setupGrid();

		// Setup the page tab labels based on current page
		setupTabLabels(page);

		// Setup navigation buttons
		setupButtons();

		// Populate page based on current page
		switch (page) {
		case INFO:
			setupHouseInfo();
			break;
		case PICS:
			setupHousePictures();
			break;
		case VIDEO:
			setupHouseVideo();
			break;
		}
	}

	/**
	 * Sets up grid layout object.
	 */
	private void setupGrid() {

		// Set grid size and spacing in group based on current page
		grid.setHgap(50);
		if (page == INFO) {
			grid.setVgap(30);
		} else {
			grid.setVgap(20);
		}

		// Set location of grid
		grid.relocate(255, 20);

		// Set grid width
		grid.setMaxWidth(650);
		grid.setMinWidth(650);
	}

	/**
	 * Sets up page labels based on current page.
	 * 
	 * @param page
	 *            Editor page index
	 */
	private void setupTabLabels(int page) {

		// Create containers for labels and status icons
		HBox info = new HBox(5);
		HBox pics = new HBox(5);
		HBox vids = new HBox(5);
		HBox tabs = new HBox(60);

		// Create labels in selected language
		Label labelInfoTab = new Label("1. "
				+ Translator.translateText(languageIndex, "House Information"));
		Label labelPictureTab = new Label("2. "
				+ Translator.translateText(languageIndex, "House Pictures"));
		Label labelVideoTab = new Label("3. "
				+ Translator.translateText(languageIndex, "House Videos"));

		// Set label font size and width
		labelInfoTab.setFont(new Font(14));
		labelPictureTab.setFont(new Font(14));
		labelVideoTab.setFont(new Font(14));
		labelInfoTab.setMinWidth(140);
		labelPictureTab.setMinWidth(120);
		labelVideoTab.setMinWidth(100);

		// Set the current page label to bold
		switch (page) {
		case INFO:
			labelInfoTab.setFont(Font.font(null, FontWeight.BOLD, labelInfoTab
					.getFont().getSize()));
			break;
		case PICS:
			labelPictureTab.setFont(Font.font(null, FontWeight.BOLD,
					labelPictureTab.getFont().getSize()));
			break;
		case VIDEO:
			labelVideoTab.setFont(Font.font(null, FontWeight.BOLD,
					labelVideoTab.getFont().getSize()));
			break;
		}

		// Update the tab labels to check for status information
		updateTabLabels();

		// Add labels and status icons to containers
		info.getChildren().addAll(labelInfoTab, infoStatus);
		pics.getChildren().addAll(labelPictureTab, picStatus);
		vids.getChildren().add(labelVideoTab);

		// If creating a new house
		if (hid == 0) {

			// Add the labels with the status icons
			tabs.getChildren().addAll(info, pics, vids);
		} else {

			// Otherwise add the labels only
			tabs.getChildren().addAll(labelInfoTab, labelPictureTab,
					labelVideoTab);

		}

		// Add tab labels to the grid and left align
		grid.addRow(0, tabs);
		GridPane.setConstraints(tabs, 0, 0, 3, 1, HPos.LEFT, VPos.CENTER);
	}

	/**
	 * Sets up page navigation buttons
	 */
	private void setupButtons() {

		// Remove any previous buttons from page
		root.getChildren().removeAll(backButtons);
		root.getChildren().removeAll(pageChangeButtons);

		// Create button containers
		backButtons = new VBox(10);
		pageChangeButtons = new HBox(500);

		// Create imageview of "Next" arrow
		ImageView buttonNext = new ImageView(new Image(
				"file:resources/icons/Forward-button.png"));

		// Prevent image warping and resize
		buttonNext.setPreserveRatio(true);
		buttonNext.setFitWidth(70);

		// Set cursor image on hover
		buttonNext.setCursor(Cursor.HAND);

		// Set mouse clicked handler to increment page
		buttonNext.setOnMouseClicked(new ChangePage(1));

		// Create imageview of "Previous" arrow
		ImageView buttonPrev = new ImageView(new Image(
				"file:resources/icons/Previous-button.png"));

		// Prevent image warping and resize
		buttonPrev.setPreserveRatio(true);
		buttonPrev.setFitWidth(70);

		// Set cursor image on hover
		buttonPrev.setCursor(Cursor.HAND);

		// Set mouse clicked handler to decrement page
		buttonPrev.setOnMouseClicked(new ChangePage(-1));

		// If creating a new property
		if (hid == 0) {

			// Create "Finish" button in chosen language
			ButtonType button3 = new ButtonType("166,208,255", null,
					Translator.translateText(languageIndex, "Finish"), 110, 30);
			Button buttonFinish = new SetupButton().createButton(button3);

			// Set cursor image on hover
			buttonFinish.setCursor(Cursor.HAND);

			// Set button handler to the create house method
			buttonFinish.setOnAction(new CreateHouse());

			// Create a "Cancel" button in chosen language
			ButtonType button4 = new ButtonType("166,208,255", null,
					Translator.translateText(languageIndex, "Cancel"), 110, 30);
			Button buttonCancel = new SetupButton().createButton(button4);

			// Set cursor image on hover
			buttonCancel.setCursor(Cursor.HAND);

			// Set button handler to cancel the house creation
			buttonCancel.setOnAction(new Cancel());

			// Add the "Cancel" and "Finish" buttons to the button container
			backButtons.getChildren().add(buttonCancel);
			backButtons.getChildren().add(buttonFinish);

		} else {
			// Otherwise create a "Back" button in chosen language
			ButtonType button4 = new ButtonType("166,208,255", null,
					Translator.translateText(languageIndex, "Back"), 110, 30);
			Button buttonBack = new SetupButton().createButton(button4);

			// Set cursor image on hover
			buttonBack.setCursor(Cursor.HAND);

			// Set button handler to cancel the house editing
			buttonBack.setOnAction(new Cancel());

			// Add the "Back" button to the button container
			backButtons.getChildren().add(buttonBack);
		}

		// Add the "Next" and "Previous" buttons to the page change button
		// container
		pageChangeButtons.getChildren().addAll(buttonPrev, buttonNext);

		// Set the visibility of the page change buttons based on the current
		// page
		if (page == 0)
			buttonPrev.setVisible(false);
		if (page == 2)
			buttonNext.setVisible(false);

		// Locate and add the button containers to the group
		backButtons.relocate(828, 17);
		pageChangeButtons.relocate(260, 710);
		root.getChildren().addAll(backButtons, pageChangeButtons);
	}

	/**
	 * Creates the house information editing/creation page.
	 */
	private void setupHouseInfo() {

		// Create a container for the descrtion label and field
		VBox vBoxDesc = new VBox(10);

		// Create the information labels in the selected language
		Label labelAddress = new Label(Translator.translateText(languageIndex,
				"Address:"));
		Label labelPostcode = new Label(Translator.translateText(languageIndex,
				"Postcode:"));
		Label labelPrice = new Label(Translator.translateText(languageIndex,
				"Price (£pppw):"));
		Label labelBeds = new Label(Translator.translateText(languageIndex,
				"Bedrooms:"));
		Label labelBaths = new Label(Translator.translateText(languageIndex,
				"Bathrooms:"));
		Label labelFurnished = new Label(Translator.translateText(
				languageIndex, "Furnished:"));
		Label labelDate = new Label(Translator.translateText(languageIndex,
				"Date Available:"));
		Label labelDeposit = new Label(Translator.translateText(languageIndex,
				"Deposit (£):"));
		Label labelDesc = new Label(Translator.translateText(languageIndex,
				"Description"));

		// Set the labels to bold, size 14pt
		labelAddress.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelPostcode.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelPrice.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelBeds.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelBaths.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelFurnished.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelDate.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelDeposit.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelDesc.setFont(Font.font(null, FontWeight.BOLD, 14));

		// If editing a current house
		if (hid != 0) {

			// Retrieve the house being editted
			house = Database.getHouse(hid);

			// Populate the information fields with the house information
			address = new TextField(house.address);
			postcode = new TextField(house.postcode);
			price = new TextField(Integer.toString(house.price));
			beds = new TextField(Integer.toString(house.rooms));
			baths = new TextField(Integer.toString(house.bathrooms));
			furnished = new CheckBox();
			deposit = new TextField(Integer.toString(house.deposit));
			description = new TextArea(house.description);
			furnished.setSelected(house.furnished);

			// Create array of comboboxes with set availability date
			dateComboArray = setupDate(house.dateAvailable);
		}

		// Add change listeners to the text fields to check for valid inputs
		address.textProperty().addListener(new TextChanged());
		postcode.textProperty().addListener(new TextChanged());
		price.textProperty().addListener(new TextChanged());
		beds.textProperty().addListener(new TextChanged());
		baths.textProperty().addListener(new TextChanged());
		deposit.textProperty().addListener(new TextChanged());
		description.textProperty().addListener(new TextChanged());

		// If availability date not set
		if (dateComboArray.size() == 0) {

			// Set to the default availability date
			dateComboArray = setupDate("2015-01-01");
		}

		// Create container for date comboboxes
		HBox dateAvailable = new HBox(10);

		// Populate the combobox container (Day, Month, Year)
		dateAvailable.getChildren().addAll(dateComboArray.get(0),
				dateComboArray.get(1), dateComboArray.get(2));

		// Set the size and wrapping of the description field
		description.setPrefSize(300, 100);
		description.setWrapText(true);

		// Add the description label and field to the container
		vBoxDesc.getChildren().addAll(labelDesc, description);

		// Create a "Save" button in chosen language
		ButtonType button1 = new ButtonType("166,208,255", null,
				Translator.translateText(languageIndex, "Save"), 150, 30);
		buttonSave = new SetupButton().createButton(button1);

		// Set cursor image on hover
		buttonSave.setCursor(Cursor.HAND);

		// Disable the "Save" button
		buttonSave.setDisable(true);

		// Set the button handler to apply the cahnges
		buttonSave.setOnAction(new ApplyChanges());

		// Add the labels and fields to the grid
		grid.addRow(2, labelAddress, address);
		grid.addRow(3, labelPostcode, postcode);
		grid.addRow(4, labelPrice, price);
		grid.addRow(5, labelBeds, beds);
		grid.addRow(6, labelBaths, baths);
		grid.addRow(7, labelFurnished, furnished);
		grid.addRow(8, labelDate, dateAvailable);
		grid.addRow(9, labelDeposit, deposit);
		grid.addRow(10, vBoxDesc);

		// If editing a current house
		if (hid != 0) {

			// Add the "Save" button to the grid
			grid.add(buttonSave, 2, 11);
		}

		/*
		 * Increase the size of the address and description fields by spreading
		 * them across multiple columns
		 */
		GridPane.setConstraints(address, 1, 2, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(vBoxDesc, 0, 10, 3, 1, HPos.CENTER, VPos.CENTER);

	}

	/**
	 * Creates the house images editing/creation page.
	 */
	public void setupHousePictures() {

		// Create tilepane layout object
		TilePane imageTiles = new TilePane();

		// Set the size and layout of the tilepane
		imageTiles.setVgap(25);
		imageTiles.setHgap(25);
		imageTiles.setPadding(new Insets(20, 20, 20, 20));
		imageTiles.setTileAlignment(Pos.CENTER);
		imageTiles.setPrefColumns(3);

		// Create a scrollpane and set the size
		ScrollPane imageWindow = new ScrollPane();
		imageWindow.setMinSize(545, 500);
		imageWindow.setMaxSize(545, 500);

		// Create a new image label and set to bold, size 14pt
		Label labelUpload = new Label(Translator.translateText(languageIndex,
				"Add a New Image:"));
		labelUpload.setFont(Font.font(null, FontWeight.BOLD, 14));

		// Create a textfield and disable editting
		uploadPathImage = new TextField();
		uploadPathImage.setEditable(false);

		// Create an image restriction label and set to bold, size 14pt
		Label pictureNumber = new Label(Translator.translateText(languageIndex,
				"* A Minimum of 3 Pictures is Required"));
		pictureNumber.setFont(Font.font(null, FontWeight.BOLD, 14));

		// Add image restriction label to the grid and centre within grid
		grid.addRow(2, pictureNumber);
		GridPane.setConstraints(pictureNumber, 0, 2, 3, 1, HPos.CENTER,
				VPos.CENTER);

		// Create "Browse" button in chosen language
		ButtonType button1 = new ButtonType("166,208,255", null,
				Translator.translateText(languageIndex, "Browse"), 70, 30);
		fileChooserButton = new SetupButton().createButton(button1);

		// Set cursor image on hover
		fileChooserButton.setCursor(Cursor.HAND);

		// Set button handler to the file browser method
		fileChooserButton.setOnAction(new Browse());

		// Create "Upload" button in chosen language
		ButtonType button2 = new ButtonType("166,208,255", null,
				Translator.translateText(languageIndex, "Upload"), 70, 30);
		uploadImageButton = new SetupButton().createButton(button2);

		// Disable the "Upload" button
		uploadImageButton.setDisable(true);

		// Set button handler to the file upload method
		uploadImageButton.setOnAction(new Upload());

		// Create "Delete" button in chosen language
		ButtonType button3 = new ButtonType("166,208,255", null,
				Translator.translateText(languageIndex, "Delete"), 70, 30);
		Button buttonDelete = new SetupButton().createButton(button3);

		// Set button handler to the image deletion method
		buttonDelete.setOnAction(new DeleteImage());

		// Create a container for the image upload controls
		HBox uploadControls = new HBox(10);

		// Centralise the contents on the container
		uploadControls.setAlignment(Pos.CENTER);

		/*
		 * Add the upload label, text field, browse button and upload button to
		 * the container
		 */
		uploadControls.getChildren().addAll(labelUpload, uploadPathImage,
				fileChooserButton, uploadImageButton);

		// Add the container to the grid and centralise in the grid
		grid.addRow(1, uploadControls);
		GridPane.setConstraints(uploadControls, 0, 1, 3, 1, HPos.CENTER,
				VPos.CENTER);

		// Create an array list to store the house images
		houseImages = new ArrayList<HouseImage>();

		// If editing a current house
		if (hid != 0) {

			// Retrieve the existing images from the database
			houseImages = Database.getHouseImageSet(hid);

			// Loop though the retrieved images and add them to the tilepane
			for (int i = 0; i < houseImages.size(); i++) {

				// Make an imageview of the current house image
				HouseImage input = houseImages.get(i);
				Image image = new Image(input.imageIS);
				ImageView propertyImage = new ImageView(image);

				// Set the dimensions of the images
				propertyImage.setFitWidth(150);
				propertyImage.setFitHeight(120);

				// Create a deletion check box
				CheckBox delete = new CheckBox();

				// Add the check box to the arraylist of checkboxes
				deleteImage.add(delete);

				// Create container for the tiles
				VBox tile = new VBox(5);

				// Centre align the contents of the container
				tile.setAlignment(Pos.CENTER);

				// Add the current image and check box to the container
				tile.getChildren().addAll(propertyImage, deleteImage.get(i));

				// Add the container to the tilepane
				imageTiles.getChildren().add(tile);
			}
		}
		// Otherwise if creating a new house
		else {

			// loop through the image uploads and add them to the tile pane
			for (int i = 0; i < imagePaths.size(); i++) {

				// Make an imageview of the current image filepath
				Image image = new Image("file:" + imagePaths.get(i));
				ImageView propertyImage = new ImageView(image);

				// Set the dimensions of the image
				propertyImage.setFitWidth(150);
				propertyImage.setFitHeight(120);

				// Create a deletion check box
				CheckBox delete = new CheckBox();

				// Add the check box to the arraylist of checkboxes
				deleteImage.add(delete);

				// Create container for the tiles
				VBox tile = new VBox(5);

				// Centre align the contents of the container
				tile.setAlignment(Pos.CENTER);

				// Add the current image and check box to the container
				tile.getChildren().addAll(propertyImage, deleteImage.get(i));

				// Add the container to the tilepane
				imageTiles.getChildren().add(tile);
			}
		}

		// Add the tilepane to the scrollpane
		imageWindow.setContent(imageTiles);

		// Add the scrollpane to the grid
		grid.add(imageWindow, 0, 3);

		// Add the "Delete" button to the grid
		grid.add(buttonDelete, 0, 4);

		/*
		 * Centralise the "Delete" button and scroll pane by spreading them
		 * across multiple columns
		 */
		GridPane.setConstraints(buttonDelete, 0, 4, 3, 1, HPos.CENTER,
				VPos.CENTER);
		GridPane.setConstraints(imageWindow, 0, 3, 3, 1, HPos.CENTER,
				VPos.CENTER);
	}

	/**
	 * Creates the house video editing/creation page.
	 */
	private void setupHouseVideo() {

		// If effing a current house
		if (hid != 0) {

			// Retrieve the house video from the database
			videoInfo = Database.checkHouseVideo(owner, hid);

			// If a house video exists
			if (videoInfo != null) {

				// Read the file video into a local temporary file
				File file = FileManager.readVideo(
						Database.getUser(currentUsername), videoInfo);

				/*
				 * Set the current file path of the video player to the filepath
				 * of the retrieved video
				 */
				videoPath = file.getAbsolutePath();
			}
		}

		// Create a container for the video upload fields
		HBox fileHBox = new HBox(10);

		// Create a label for the upload fields
		Label fileLabel = new Label("File:");

		// Create file directory text field and set the width
		uploadPathVideo = new TextField();
		uploadPathVideo.setPrefWidth(250);

		// Create a "Browse" button in the chosen language
		ButtonType button1 = new ButtonType("166,208,255", null,
				Translator.translateText(languageIndex, "Browse"), 100, 30);
		fileChooserButton = new SetupButton().createButton(button1);

		// If a video already exists
		if (videoPath != null) {
			// Disable the "Browse" button
			fileChooserButton.setDisable(true);
		} else {
			// Otherwise enable the "Browse" button
			fileChooserButton.setDisable(false);
		}

		// Create an "Upload" button in the chosen language
		ButtonType button2 = new ButtonType("166,208,255", null,
				Translator.translateText(languageIndex, "Upload"), 100, 30);
		uploadVideoButton = new SetupButton().createButton(button2);

		// Disable the "Upload" button
		uploadVideoButton.setDisable(true);

		// Set the button event to the upload method
		uploadVideoButton.setOnAction(new Upload());

		// Setup the textchanged listener of the video upload textfield
		uploadPathVideo.textProperty().addListener(
				new ChangeListener<String>() {
					@Override
					public void changed(
							ObservableValue<? extends String> observable,
							String oldValue, String newValue) {

						/*
						 * Enable the "Upload" button if the field is no longer
						 * empty
						 */
						if (uploadPathVideo.getText() != null
								&& !uploadPathVideo.getText().trim().isEmpty()) {
							uploadVideoButton.setDisable(false);
						}
						// Otherwise disable the "Upload" button
						else {
							uploadVideoButton.setDisable(true);
						}
					}
				});

		// Set the button event to the browse method
		fileChooserButton.setOnAction(new Browse());

		/*
		 * Add the upload label, textfield, browse button and upload button to
		 * the container and centre align
		 */
		fileHBox.getChildren().addAll(fileLabel, uploadPathVideo,
				fileChooserButton, uploadVideoButton);
		fileHBox.setAlignment(Pos.CENTER);

		// Add the container to the grid
		grid.add(fileHBox, 0, 1);

		// Centre the container by spreading it across the columns
		GridPane.setConstraints(fileHBox, 0, 1, 3, 1, HPos.CENTER, VPos.CENTER);

		// If a video already exists
		if (videoPath != null) {

			// Create the video player
			setupVideoPlayer(videoPath);

			// Create the video marker editor
			setupRoomMarkers();
		}
	}

	/**
	 * Creates a new video player.
	 * 
	 * @param newVideoFileString
	 *            File path of video to be played
	 */
	private void setupVideoPlayer(String newVideoFileString) {

		// Create a stackpane to contain the vidoe
		StackPane videoPane = new StackPane();

		// Create a new video player with the input file path
		video = new VideoElement(newVideoFileString, true);
		video.setStylesheet("resources/videoStyle.css");

		// Set size and autoplay options of the video player
		video.setWidth(500);
		video.setAutoplay(true);

		// Add the video player to the stackpane
		video.display(videoPane);

		// Add video player to grid and spread across the columns to centralise
		grid.add(videoPane, 0, 2);
		GridPane.setConstraints(videoPane, 0, 2, 3, 1, HPos.CENTER, VPos.CENTER);
	}

	/**
	 * Sets up the video marker editor.
	 */
	private void setupRoomMarkers() {

		// If editing a current house
		if (hid != 0) {

			// Retrieve the video markers from the database
			videoMarkers = Database.getVideoMarkers(videoInfo.vid);

			// Loop through the markers and add them to the marker list view
			for (int i = 0; i < videoMarkers.size(); i++) {

				// Create a container for the marker information
				HBox currentMarker = new HBox(0);

				// Get the current marker information
				Marker markers = videoMarkers.get(i);

				// Create a label with the current marker room name
				Label currentRoomLabel = new Label(markers.room);

				// Set the width of the room name label
				currentRoomLabel.setPrefWidth(200);

				// Create a label with the current marker video time
				Label currentVideoTimeLabel = new Label(
						Double.toString(markers.markerTime));

				// Set the width of the video time label
				currentVideoTimeLabel.setPrefWidth(80);

				// Add the labels to a container
				currentMarker.getChildren().addAll(currentRoomLabel,
						currentVideoTimeLabel);

				// Left align the marker label container
				currentMarker.setAlignment(Pos.CENTER_LEFT);

				// Add the label container to the list items
				items.add(currentMarker);
			}

		}

		// Create a container for new marker setup
		HBox markerSetup = new HBox(10);

		// Create a container for the marker list headers
		HBox markerListHeader = new HBox(10);

		// Create the marker list header labels and set styles and sizes
		Label roomNamesHeader = new Label(Translator.translateText(
				languageIndex, "Rooms"));
		roomNamesHeader.setStyle("-fx-font-weight: bold");
		roomNamesHeader.setPrefWidth(190);
		Label videoTimesHeader = new Label(Translator.translateText(
				languageIndex, "Times in Video"));
		videoTimesHeader.setStyle("-fx-font-weight: bold");

		// Set the width of the marker list headers container
		markerListHeader.setMaxWidth(300);

		// Add the header labels to the container
		markerListHeader.getChildren()
				.addAll(roomNamesHeader, videoTimesHeader);

		// Create a new room marker label and text field
		Label addNewRoomLabel = new Label(Translator.translateText(
				languageIndex, "Add New Room: "));
		newRoomField = new TextField();

		// Create a "Set Marker" button in the chosen language
		ButtonType button1 = new ButtonType("166,208,255", null,
				Translator.translateText(languageIndex, "Set Marker"), 100, 30);
		setMarkerButton = new SetupButton().createButton(button1);

		// Disable the "Set Marker" button
		setMarkerButton.setDisable(true);

		// Set the button handler to the new marker method
		setMarkerButton.setOnAction(new AddMarker());

		// Create a "Remove Video" button in the chosen language
		ButtonType button3 = new ButtonType("166,208,255", null,
				Translator.translateText(languageIndex, "Remove Video"), 120,
				30);
		Button videoRemove = new SetupButton().createButton(button3);

		// Set the button handler to the remove video method
		videoRemove.setOnAction(new RemoveVideo());

		// Setup the text changed listener of the marker name field
		newRoomField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {

				// Enable the "Set Marker" button if the field is no longer
				// empty
				if (newRoomField.getText() != null
						&& !newRoomField.getText().trim().isEmpty()) {
					setMarkerButton.setDisable(false);
				}

				// Otherwise disable the "Set Marker" button
				else {
					setMarkerButton.setDisable(true);
				}
			}
		});

		// Add marker setup fields and "Remove Video" button to the container
		markerSetup.getChildren().addAll(addNewRoomLabel, newRoomField,
				setMarkerButton, videoRemove);
		markerSetup.setAlignment(Pos.CENTER);

		// Create a "Delete" marker button in the chosen language
		ButtonType button2 = new ButtonType("166,208,255", null,
				Translator.translateText(languageIndex, "Delete"), 100, 30);
		deleteMarker = new SetupButton().createButton(button2);

		// Disable the "Delete" marker button
		deleteMarker.setDisable(true);

		// Set the button handler to the delete marker method
		deleteMarker.setOnAction(new DeleteMarker());

		// Set the size of the marker listview
		markerList.setPrefHeight(180);
		markerList.setMaxWidth(300);
		markerList.setItems(items);

		// Setup the selection changed listener of the listview
		markerList.getSelectionModel().selectedIndexProperty()
				.addListener(new ChangeListener<Number>() {
					@Override
					public void changed(
							ObservableValue<? extends Number> observable,
							Number oldValue, Number newValue) {
						// Get the current listview selection
						markersIndex = markerList.getSelectionModel()
								.getSelectedIndex();

						// If a marker is selected, enable the "Delete" button
						if (markersIndex >= 0) {
							deleteMarker.setDisable(false);
						}

						// Otherwise disbale the "Delete" button
						else {
							deleteMarker.setDisable(true);
						}
					}
				});

		// Add to GridPane
		grid.add(markerSetup, 0, 3);

		// Centre align the page contents using grid constraints
		GridPane.setConstraints(markerSetup, 0, 3, 3, 1, HPos.CENTER,
				VPos.CENTER);
		grid.add(markerListHeader, 0, 4);
		GridPane.setConstraints(markerListHeader, 0, 4, 3, 1, HPos.CENTER,
				VPos.BOTTOM);
		grid.add(markerList, 0, 5);
		GridPane.setConstraints(markerList, 0, 5, 3, 1, HPos.CENTER,
				VPos.CENTER);
		grid.add(deleteMarker, 0, 6);
		GridPane.setConstraints(deleteMarker, 0, 6, 3, 1, HPos.CENTER,
				VPos.CENTER);
	}

	/**
	 * This event handler handles updates to current property house information.
	 * 
	 * @version 3.10 (01.06.15)
	 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
	 */
	public class ApplyChanges implements EventHandler<ActionEvent> {

		public void handle(ActionEvent arg0) {

			// Check applicable fields for bad language and highlight if found
			BadWordCheck bwd = new BadWordCheck();
			if (bwd.containsBlackListedWords(description.getText())) {
				description.setText(bwd.highlightBlackListedWords(description
						.getText()));
				createWarningPopup("Inappropriate Language");
				dialogStage.show();

			} else if (bwd.containsBlackListedWords(address.getText())) {
				address.setText(bwd.highlightBlackListedWords(address.getText()));
				createWarningPopup("Inappropriate Language");
				dialogStage.show();

			} else if (bwd.containsBlackListedWords(postcode.getText())) {
				postcode.setText(bwd.highlightBlackListedWords(postcode
						.getText()));
				createWarningPopup("Inappropriate Language");
				dialogStage.show();

			}

			// Update the house on the database if no errors found
			else {

				// Parse the date comboboxes into a database compatible date
				// string
				String dateAvailableString = dateComboArray.get(2).getValue()
						+ "-" + dateComboArray.get(1).getValue() + "-"
						+ dateComboArray.get(0).getValue();

				// Send all fields to the database
				Database.updateHouse(owner, house, "address",
						address.getText(), null, null, 0, 1);
				Database.updateHouse(owner, house, "postcode",
						postcode.getText(), null, null, 0, 1);
				Database.updateHouse(owner, house, "price", null, null, null,
						Integer.parseInt(price.getText().trim()), 4);
				Database.updateHouse(owner, house, "rooms", null, null, null,
						Integer.parseInt(beds.getText().trim()), 4);
				Database.updateHouse(owner, house, "bathrooms", null, null,
						null, Integer.parseInt(baths.getText().trim()), 4);
				Database.updateHouse(owner, house, "furnished", null,
						furnished.isSelected(), null, 0, 2);
				Database.updateHouse(owner, house, "deposit", null, null, null,
						Integer.parseInt(deposit.getText().trim()), 4);
				Database.updateHouse(owner, house, "description",
						description.getText(), null, null, 0, 1);
				Database.updateHouse(owner, house, "date_available",
						dateAvailableString, null, null, 0, 1);

				// Clear the grid and reload the page to confirm database update
				grid.getChildren().clear();
				createEditPage();
			}
		}
	}

	/**
	 * This event handler handles creation of new houses.
	 * 
	 * @version 3.10 (01.06.15)
	 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
	 */
	public class CreateHouse implements EventHandler<ActionEvent> {

		public void handle(ActionEvent arg0) {

			// Parse the date comboboxes into a database compatible date string
			String dateAvailableString = dateComboArray.get(2).getValue() + "-"
					+ dateComboArray.get(1).getValue() + "-"
					+ dateComboArray.get(0).getValue();

			/*
			 * If information fields are filled and valid and there are at least
			 * three images uploaded
			 */
			if (checkInfoPage() && imagePaths.size() >= 3) {

				// Create a new house data type with the title set as the
				// address
				House newHouse = new House(address.getText());

				// Populate the house information fields from the information
				// fields
				newHouse.address(address.getText());
				newHouse.bathrooms(Integer.parseInt(baths.getText()));
				newHouse.postcode(postcode.getText());
				newHouse.price(Integer.parseInt(price.getText()));
				newHouse.deposit(Integer.parseInt(deposit.getText()));
				newHouse.rooms(Integer.parseInt(beds.getText()));
				newHouse.dateAvailable(dateAvailableString);
				newHouse.furnished(furnished.isSelected());
				newHouse.description(description.getText());

				// If this house does not exist already for this owner
				if (!Database.checkHouseExists(owner, newHouse)) {
					int newhid = 0;
					try {

						// Add the new house to the database
						Database.houseInsert(newHouse, null, null, owner);

						// Retrieve the new hosue ID
						newhid = Database.getID(owner, newHouse, 2);

						// Retrieve the house from the database
						House house = Database.getHouse(newhid);

						/*
						 * Loop through the uploaded images and add them to the
						 * house on the database
						 */
						for (int i = 0; i < imagePaths.size(); i++) {
							try {

								// Add image to this house on the database
								Database.insertHouseImage(imagePaths.get(i),
										house, owner);
							} catch (SQLException e) {
								System.out.println("Failed to add image");
							}

						}

						// If a video has been uploaded
						if (videoPath != null) {

							// Add the video to this house on the database
							Database.insertHouseVideo(owner, house, videoFile
									.getName(), videoFile.getParentFile()
									.getAbsolutePath());

							/*
							 * Retrieve the uploaded video information from the
							 * database
							 */
							videoInfo = Database.checkHouseVideo(owner,
									house.hid);

							/*
							 * Loop through the video markers and add them to
							 * the video on the database
							 */
							for (int i = 0; i < videoMarkers.size(); i++) {

								// Get the current marker from the array list
								Marker marker = videoMarkers.get(i);

								/*
								 * Parse the marker into strings for the
								 * database
								 */
								String roomMarker = marker.room;
								double videoTime = marker.markerTime;

								/*
								 * Add the current marker to the video on the
								 * database
								 */
								Database.insertVideoMarker(videoInfo.vid,
										roomMarker, videoTime);
							}
						}

					} catch (IOException e) {
						System.out.println("House creation failed");
					}

					/*
					 * After creating the house, return to the landlord
					 * properties page
					 */
					loadSlide(LANDLORDPROPERTIES);
				}
			} else {
				System.out.println("House info incomplete");
			}
		}
	}

	/**
	 * This event handler handles the cancelling of a house edit/creation.
	 * 
	 * @version 3.10 (01.06.15)
	 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
	 */
	public class Cancel implements EventHandler<ActionEvent> {

		public void handle(ActionEvent arg0) {

			// Free the memory associated with the video file
			video = null;

			// Return to the landlord properties page
			loadSlide(LANDLORDPROPERTIES);
		}

	}

	/**
	 * Checks the house information fields contain valid information.
	 * 
	 * @return True if all information fields contain valid information
	 */
	public boolean checkInfoPage() {

		// Create a check counter
		int check = 0;

		/*
		 * Check that the information fields have been entered and are in the
		 * correct format
		 */
		if (!address.getText().equals(""))
			check++;
		if (!baths.getText().equals("") && checkNumber(baths.getText()))
			check++;
		if (!postcode.getText().equals("") && postcode.getText().length() <= 10)
			check++;
		if (!price.getText().equals("") && checkNumber(price.getText()))
			check++;
		if (!beds.getText().equals("") && checkNumber(beds.getText()))
			check++;
		if (!address.getText().equals(""))
			check++;
		if (!description.getText().equals("")
				&& description.getText().length() <= 65535)
			check++;
		if (!deposit.getText().equals("") && checkNumber(deposit.getText()))
			check++;

		// If all field are correct, return true
		if (check == 8) {
			return true;
		}
		// Otherwise return false
		else
			return false;
	}

	/**
	 * Checks that a string only contains numbers.
	 * 
	 * @param input
	 *            String to be checked
	 * 
	 * @return True if the string only contains numbers
	 */
	public boolean checkNumber(String input) {

		try {
			// Attempt to parse an integer from the string
			int check = Integer.parseInt(input.trim());

			// If the value is greater than or equal to 2048, return false
			if (check >= 2048) {
				return false;
			}
		} catch (Exception e) {
			// Return a false if the parse was unsuccessful
			return false;
		}
		// Return true if the parse and size check was successful
		return true;
	}

	/**
	 * Updates the tab label status icons.
	 */
	public void updateTabLabels() {

		// If all house information is correct and valid
		if (checkInfoPage()) {

			// If creating a new house
			if (hid == 0) {

				// Set the information status icon to a tick
				infoStatus.setImage(tick);
			} else {

				// Otherwise enable the edit house save button
				buttonSave.setDisable(false);
			}

			// If creating a new house and house information is incorrect
		} else if (hid == 0) {

			// Set the information status icon to a cross
			infoStatus.setImage(cross);
		} else {

			// Otherwise disable the edit hosue save button
			buttonSave.setDisable(true);
		}

		// If creating a new house and three images have been uploaded
		if (imagePaths.size() >= 3) {

			// Set the pictures status icon to a tick
			picStatus.setImage(tick);
		} else

			// Otherwise set the pictures status icon to a cross
			picStatus.setImage(cross);
	}

	/**
	 * This event handler handles text changes.
	 * 
	 * @version 3.10 (01.06.15)
	 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
	 */
	public class TextChanged implements ChangeListener<String> {

		public void changed(ObservableValue<? extends String> arg0,
				String arg1, String arg2) {
			// If a text field has been changed, update the tab labels
			updateTabLabels();
		}

	}

	/**
	 * This button event hander handles the change page event
	 * 
	 * @author EyeHouse
	 * 
	 *         Copyright 2015 EyeHouse
	 */
	public class ChangePage implements EventHandler<MouseEvent> {

		final int direction;

		/**
		 * Constructor method
		 * 
		 * @param dir
		 *            1 = increment page, -1 = decrement page
		 */
		public ChangePage(int dir) {
			direction = dir;
		}

		@Override
		public void handle(MouseEvent arg0) {

			// Free memory allocated to video
			video = null;

			// Clear the grid
			grid.getChildren().clear();

			// Set the new page value
			page = page + direction;

			// Load chosen edit property page
			createEditPage();
		}
	}

	/**
	 * This button event hander handles the file browse event.
	 * 
	 * @version 3.10 (01.06.15)
	 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
	 */
	public class Browse implements EventHandler<ActionEvent> {

		public void handle(ActionEvent arg0) {

			File newFile;

			// Open file chooser window
			FileChooser uploadChooser = new FileChooser();
			configureFileChooser(uploadChooser);
			javafx.stage.Window fileChooserStage = null;

			// Disable the "Browse" button
			fileChooserButton.setDisable(true);

			// Store the returned file
			newFile = uploadChooser.showOpenDialog(fileChooserStage);

			// If chosen file is valid
			if (newFile != null && !newFile.getName().contains(".url")) {

				// Switch based on the current page
				switch (page) {
				case INFO:
					break;
				case PICS:

					// Set the new image path and input field values
					uploadPathImage.setText(newFile.getName());
					uploadImageButton.setCursor(Cursor.HAND);

					// Enable the image upload button
					uploadImageButton.setDisable(false);

					// Set the current upload filepath
					filePath = newFile.getAbsolutePath();

					break;
				case VIDEO:

					// Clear upload text field and add name of file
					uploadPathVideo.clear();
					uploadPathVideo.appendText(newFile.getName());

					// Enable the video upload button
					uploadVideoButton.setDisable(false);

					// Set the current upload filepath
					filePath = newFile.getAbsolutePath();

					// Store the upload file
					videoFile = newFile;
					break;
				}
			}

			// If file uploaded but file is a .url
			else if (newFile != null) {
				if (newFile.getName().contains(".url")) {

					// Create a warning popup message
					createWarningPopup("Invalid File Type");
					dialogStage.show();
				}
			}

			// Enable the "Browse" button
			fileChooserButton.setDisable(false);
		}

		/**
		 * Configures the file chooser based on the current page.
		 * 
		 * @param uploadChooser
		 *            Input the file chooser to be edited
		 */
		private void configureFileChooser(FileChooser uploadChooser) {

			// Set directory that the file chooser will initially open
			uploadChooser.setInitialDirectory(new File(System
					.getProperty("user.home")));

			// Switch based on the current page
			switch (page) {
			case INFO:
				break;
			case PICS:
				// Set title of file chooser
				uploadChooser.setTitle(Translator.translateText(languageIndex,
						"Choose Property Image to Upload"));
				// Set file types displayed in the file chooser as png and jpg
				uploadChooser.getExtensionFilters().add(
						new FileChooser.ExtensionFilter("JPG, PNG", "*.jpg",
								"*.png"));
				break;
			case VIDEO:
				// Set title of file chooser
				uploadChooser.setTitle(Translator.translateText(languageIndex,
						"Choose Video to Upload"));
				// Set file types displayed in the file chooser as mp4
				uploadChooser.getExtensionFilters().add(
						new FileChooser.ExtensionFilter("MP4", "*.mp4"));
				break;
			}

		}
	}

	/**
	 * This button event hander handles the upload event.
	 * 
	 * @version 3.10 (01.06.15)
	 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
	 */
	public class Upload implements EventHandler<ActionEvent> {

		/**
		 * Event handler
		 */
		public void handle(ActionEvent arg0) {

			// If on the video edit page
			if (page == VIDEO) {

				// Disable the video upload button
				uploadVideoButton.setDisable(true);

				// Clear the upload field text
				uploadPathVideo.clear();
			}

			// If creating a new house
			if (hid == 0) {

				// Update the tab label status icons
				updateTabLabels();

				// Switch based on the current page
				switch (page) {
				case INFO:
					break;
				case PICS:

					// Add the new filepath to the list of images
					imagePaths.add(filePath);

					// clear the grid and reopen the edit page
					grid.getChildren().clear();
					createEditPage();
					break;
				case VIDEO:
					// Store the new filepath as the video path
					videoPath = filePath;

					// Disable the browse button
					fileChooserButton.setDisable(true);

					// Setup the video player with the new video
					setupVideoPlayer(videoPath);

					// Setup the room marker editor
					setupRoomMarkers();
					break;
				}
				// Jump out of handler
				return;
			}

			// Otherwise try to upload the file to the database
			try {
				boolean check = false;

				// If the current page is house images
				if (page == PICS) {

					// Add the new image to the database
					check = Database.insertHouseImage(filePath, house, owner);
				}

				// Otherwise add the video to the databse
				else if (page == VIDEO) {

					// Store the new filepath as the video path
					videoPath = filePath;

					// Upload the new video to the database on the current house
					Database.insertHouseVideo(owner, house,
							videoFile.getName(), videoFile.getParentFile()
									.getAbsolutePath());

					// Retrieve the uploaded video information
					videoInfo = Database.checkHouseVideo(owner, house.hid);

					// Disable the file chooser button
					fileChooserButton.setDisable(true);

					// Setup the video player with the new video
					setupVideoPlayer(videoPath);

					// Setup the room marker editor
					setupRoomMarkers();
				}

				// If upload was successful
				if (check) {

					// Clear the grid and reload the edit property page
					grid.getChildren().clear();
					createEditPage();
				}
			} catch (SQLException e) {
				System.out.println("Upload Failed");
			}
		}
	}

	/**
	 * This event handler handles the remove video event.
	 * 
	 * @version 3.10 (01.06.15)
	 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
	 */
	public class RemoveVideo implements EventHandler<ActionEvent> {

		public void handle(ActionEvent arg0) {

			// If editing a current house
			if (hid != 0) {

				// Retrieve the house video information
				HouseVideo currentVideo = Database.checkHouseVideo(owner,
						currentPropertyID);

				// Delete the house video
				Database.deleteVideo(owner, currentVideo);
			}

			// Set the video path to null to reset page fields
			videoPath = null;

			// Set video to null to free the memory space
			video = null;

			// Enable the "Browse" button
			fileChooserButton.setDisable(false);

			// Clear the grid and reload the edit property page
			grid.getChildren().clear();
			createEditPage();
		}
	}

	/**
	 * This event handler handles the delete image event.
	 * 
	 * @version 3.10 (01.06.15)
	 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
	 */
	public class DeleteImage implements EventHandler<ActionEvent> {

		public void handle(ActionEvent arg0) {

			// if creating a new house
			if (hid == 0) {

				/*
				 * Loop through all of the house images and remove the files
				 * that have a corresponding tick in the checkbox
				 */
				int i = 0;
				while (i < imagePaths.size()) {

					// If the image checkbox is ticked
					if (deleteImage.get(i).isSelected()) {

						// Remove entries from file path and checkbox arraylists
						imagePaths.remove(i);
						deleteImage.remove(i);

						// Update tab label status icons
						updateTabLabels();

						// Restart the sweep
						i = 0;
					}
					// Otherwise continue through the arraylist
					else {
						i++;
					}
				}
			}
			// Otherwise if editing a current house
			else {
				/*
				 * Loop through all of the house images and remove the files
				 * that have a corresponding tick in the checkbox
				 */
				int i = 0;
				while (i < houseImages.size()) {

					// If the image checkbox is ticked
					if (deleteImage.get(i).isSelected()) {

						// Remove the selected image from the database
						boolean check = Database.deleteHouseImage(houseImages
								.get(i));

						// Remove entries from HouseImage and checkbox
						// arraylists
						houseImages.remove(i);
						deleteImage.remove(i);

						// Print to console if delete successful
						if (check) {
							System.out.println("Image Deleted");
						}

						// Restart the sweep
						i = 0;
					}

					// Otherwise continue through the arraylist
					else {
						i++;
					}
				}
			}

			// Clear the grid and reload the edit property page
			grid.getChildren().clear();
			createEditPage();
		}
	}

	/**
	 * This event handler handles creation of new houses.
	 * 
	 * @version 3.10 (01.06.15)
	 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
	 */
	public class AddMarker implements EventHandler<ActionEvent> {

		public void handle(ActionEvent arg0) {

			// If new marker name field is not empty
			if (!newRoomField.getText().equals("")) {

				// Create a container for the marker labels
				HBox newMarker = new HBox(0);

				// Create room name and video time labels
				Label newRoomLabel = new Label(newRoomField.getText());
				newRoomLabel.setPrefWidth(200);
				Label newVideoTimeLabel = new Label(
						video.printCurrentVideoTime());

				// Set the size of the new time label
				newVideoTimeLabel.setPrefWidth(80);

				// Add the labels to the container
				newMarker.getChildren().addAll(newRoomLabel, newVideoTimeLabel);

				// Left align the items in the container
				newMarker.setAlignment(Pos.CENTER_LEFT);

				// Parse a time in seconds from the current video time
				String[] times = video.printCurrentVideoTime().split(":");
				double seconds = Double.parseDouble(times[1]);
				double minutes = Double.parseDouble(times[0]);
				double videoTime = (minutes * 60) + seconds;

				// Create a room marker name string
				String roomMarker = newRoomField.getText();

				// If editing a current property
				if (hid != 0) {

					// Insert the new marker into the database
					Database.insertVideoMarker(videoInfo.vid, roomMarker,
							videoTime);
				}

				// Create a new marker with the marker information
				Marker marker = new Marker(roomMarker);
				marker.time(videoTime);

				// Add the new marker information container to the list items
				items.add(newMarker);

				// Add the new marker to the arraylist of markers
				videoMarkers.add(marker);

				// Clear the new marker name text field
				newRoomField.clear();

				// Disable the new marker button
				setMarkerButton.setDisable(true);
			}
		}
	}

	/**
	 * This event handler handles the delete marker event.
	 * 
	 * @version 3.2 (27.05.15)
	 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
	 */
	public class DeleteMarker implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			// If a marker is selected in the listview
			if (markerList.getSelectionModel().getSelectedIndex() >= 0) {

				// Get the currently selected marker index
				int index = markerList.getSelectionModel().getSelectedIndex();

				// Shuffle items after deletion up the list
				for (int i = index; i < items.size() - 1; i++) {
					items.set(i, items.get(i + 1));
				}

				// Remove last item which is now a duplicate
				items.remove(items.size() - 1);

				// Remove the selected marker from the database
				Database.deleteVideoMarker(videoMarkers.get(index));

				// Remove the selected marker from the marker arraylist
				videoMarkers.remove(index);

				// Disable the delete marker button
				deleteMarker.setDisable(true);

				// Clear the list view selection
				markerList.getSelectionModel().clearSelection();
			}
		}
	}

	/**
	 * Creates an ArrayList of ComboBox formated for date input.
	 * 
	 * @param initialDate
	 *            Initial date value of ComboBox
	 * @return ArrayList of date ComboBox, index 0 = days, 1 = months etc.
	 */
	public static ArrayList<ComboBox<String>> setupDate(String initialDate) {

		// Create comboboxes
		final ComboBox<String> comboAvailableDay = new ComboBox<String>();
		ComboBox<String> comboAvailableMonth = new ComboBox<String>();
		ComboBox<String> comboAvailableYear = new ComboBox<String>();

		// Populate comboboxes with relevant string values
		for (int i = 1; i < 32; i++) {
			comboAvailableDay.getItems().add(String.format("%02d", i));
		}
		for (int i = 1; i < 13; i++) {
			comboAvailableMonth.getItems().add(String.format("%02d", i));
		}
		for (int i = 2015; i > 1919; i--) {
			comboAvailableYear.getItems().add(String.format("%04d", i));
		}

		// Set the selected item of the combo boxes to the input intial date
		int day = Integer.valueOf(initialDate.substring(8, 10), 10).intValue();
		int month = Integer.valueOf(initialDate.substring(5, 7), 10).intValue();
		int year = Integer.valueOf(initialDate.substring(0, 4), 10).intValue();
		comboAvailableDay.getSelectionModel().select(day - 1);
		comboAvailableMonth.getSelectionModel().select(month - 1);
		comboAvailableYear.getSelectionModel().select(2015 - year);

		// Setup change listener to month combobox
		comboAvailableMonth.valueProperty().addListener(
				new ChangeListener<String>() {
					@Override
					public void changed(
							@SuppressWarnings("rawtypes") ObservableValue ov,
							String previousValue, String newValue) {

						// Get the currently selected day
						int index = comboAvailableDay.getSelectionModel()
								.getSelectedIndex();

						// Clear the days combobox
						comboAvailableDay.getItems().clear();

						// Switch based on the new month
						switch (newValue) {
						case "09":
						case "04":
						case "06":
						case "11":
							// Populate days combobox with 30 days
							for (int j = 1; j < 31; j++) {
								comboAvailableDay.getItems().add(
										String.format("%02d", j));
							}
							break;
						case "02":
							// Populate days combobox with 29 days
							for (int j = 1; j < 29; j++) {
								comboAvailableDay.getItems().add(
										String.format("%02d", j));
							}
							break;
						default:
							// Populate days combobox with 31 days
							for (int j = 1; j < 32; j++) {
								comboAvailableDay.getItems().add(
										String.format("%02d", j));
							}
							break;
						}

						/*
						 * If previous day selection is greater than or eqal to
						 * the number of items in the new days combobox
						 */
						if (index >= comboAvailableDay.getItems().size()) {
							/*
							 * Set the selected index to the last item in the
							 * combobox
							 */
							index = comboAvailableDay.getItems().size() - 1;
						}
						/*
						 * Otherwise set the selected index to the previously
						 * selected value
						 */
						comboAvailableDay.getSelectionModel().select(index);
					}
				});

		// Create and populate the combobox arraylist
		ArrayList<ComboBox<String>> date = new ArrayList<ComboBox<String>>();
		date.add(comboAvailableDay);
		date.add(comboAvailableMonth);
		date.add(comboAvailableYear);

		// Return the date combobox arraylist
		return date;
	}
}
