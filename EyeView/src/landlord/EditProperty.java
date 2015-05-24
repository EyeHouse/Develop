package landlord;

import handlers.VideoElement;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import presenter.SlideContent;

import Button.ButtonType;
import Button.SetupButton;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import database.Database;
import database.House;
import database.HouseImage;
import database.User;

public class EditProperty extends presenter.Window {

	private int page;
	private int hid;
	private static final int INFO = 0;
	private static final int PICS = 1;
	private static final int VIDEO = 2;
	private GridPane grid = new GridPane();
	private House house;
	private TextField address = new TextField();
	private TextField postcode = new TextField();
	private TextField price = new TextField();
	private TextField beds = new TextField();
	private TextField baths = new TextField();
	private TextField deposit = new TextField();
	private TextField uploadPathImage;
	private TextField uploadPathVideo;
	private TextField newRoomField;
	private CheckBox furnished = new CheckBox();
	private TextArea description = new TextArea();
	ArrayList<ComboBox<String>> dateComboArray = new ArrayList<ComboBox<String>>();
	private VBox allButtons;
	private Button buttonUpload;
	Button uploadVideoButton;
	private String filePath;
	private ArrayList<String> imagePaths = new ArrayList<String>();

	private String videoPath;
	private VideoElement video;
	ListView<HBox> markerList = new ListView<HBox>();
	ObservableList<HBox> items = FXCollections.observableArrayList();

	private ArrayList<HouseImage> houseImages;
	private ArrayList<CheckBox> deleteImage = new ArrayList<CheckBox>();
	private ImageView infoStatus, picStatus;
	Image tick, cross;

	public EditProperty(int page, int houseID) {
		this.page = page;
		this.hid = houseID;
		infoStatus = new ImageView();
		picStatus = new ImageView();
		tick = new Image("file:resources/images/Status/Tick.png");
		cross = new Image("file:resources/images/Status/Cross.png");

		createEditPage();

		root.getChildren().add(grid);
	}

	public void createEditPage() {
		setupGrid();
		setupTabLabels(page);
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
		setupButtons();

	}

	private void setupGrid() {
		// Set grid size and spacing in group.
		grid.setHgap(50);
		if (page == VIDEO) {
			grid.setVgap(20);
		} else {
			grid.setVgap(30);
		}

		grid.relocate(220, 20);

		grid.setMaxWidth(650);
		grid.setMinWidth(650);
	}

	private void setupTabLabels(int page) {

		HBox info = new HBox(5);
		HBox pics = new HBox(5);
		HBox vids = new HBox(5);

		Label labelInfoTab = new Label("1. House Information");
		Label labelPictureTab = new Label("2. House Pictures");
		Label labelVideoTab = new Label("3. House Videos");
		labelInfoTab.setFont(new Font(14));
		labelPictureTab.setFont(new Font(14));
		labelVideoTab.setFont(new Font(14));

		labelInfoTab.setMinWidth(140);
		labelPictureTab.setMinWidth(120);
		labelVideoTab.setMinWidth(100);

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

		UpdateTabLabels();

		info.getChildren().addAll(labelInfoTab, infoStatus);
		pics.getChildren().addAll(labelPictureTab, picStatus);
		vids.getChildren().add(labelVideoTab);

		if (hid == 0) {
			grid.addRow(0, info, pics, vids);
		} else {
			grid.addRow(0, labelInfoTab, labelPictureTab, labelVideoTab);
		}
	}

	private void setupButtons() {
		root.getChildren().removeAll(allButtons);
		allButtons = new VBox(10);
		HBox buttons = new HBox(10);
		ButtonType button1 = new ButtonType("150,150,150", null, "Next", 50, 30);
		Button buttonNext = new SetupButton().CreateButton(button1);
		buttonNext.setCursor(Cursor.HAND);
		buttonNext.setOnAction(new ChangePage(1));

		ButtonType button2 = new ButtonType("150,150,150", null, "Prev", 50, 30);
		Button buttonPrev = new SetupButton().CreateButton(button2);
		buttonPrev.setCursor(Cursor.HAND);
		buttonPrev.setOnAction(new ChangePage(-1));

		ButtonType button3 = new ButtonType("150,150,150", null, "Finish", 110,
				30);
		Button buttonFinish = new SetupButton().CreateButton(button3);
		buttonFinish.setCursor(Cursor.HAND);
		buttonFinish.setOnAction(new CreateHouse());

		ButtonType button4 = new ButtonType("150,150,150", null, "Cancel", 110,
				30);
		Button buttonCancel = new SetupButton().CreateButton(button4);
		buttonCancel.setCursor(Cursor.HAND);
		buttonCancel.setOnAction(new Cancel());

		buttons.getChildren().addAll(buttonPrev, buttonNext);
		allButtons.getChildren().add(buttons);
		if (hid == 0)
			allButtons.getChildren().add(buttonFinish);

		allButtons.getChildren().add(buttonCancel);
		if (page == 0)
			buttonPrev.setVisible(false);
		if (page == 2)
			buttonNext.setVisible(false);
		allButtons.relocate(840, 20);
		root.getChildren().add(allButtons);
	}

	private void setupHouseInfo() {

		VBox vBoxDesc = new VBox(10);
		Label labelAddress = new Label("Address:");
		Label labelPostcode = new Label("Postcode:");
		Label labelPrice = new Label("Price (£pppw):");
		Label labelBeds = new Label("Bedrooms:");
		Label labelBaths = new Label("Bathrooms:");
		Label labelFurnished = new Label("Furnished:");
		Label labelDate = new Label("Date Available:");
		Label labelDeposit = new Label("Deposit (£):");
		Label labelDesc = new Label("Description");

		labelAddress.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelPostcode.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelPrice.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelBeds.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelBaths.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelFurnished.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelDate.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelDeposit.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelDesc.setFont(Font.font(null, FontWeight.BOLD, 14));

		if (hid != 0) {
			house = Database.getHouse(hid);
			address = new TextField(house.address);
			postcode = new TextField(house.postcode);
			price = new TextField(Integer.toString(house.price));
			beds = new TextField(Integer.toString(house.rooms));
			baths = new TextField(Integer.toString(house.bathrooms));
			furnished = new CheckBox();
			deposit = new TextField(Integer.toString(house.deposit));
			description = new TextArea(house.description);

			dateComboArray = setupDate(house.dateAvailable);
			furnished.setSelected(house.furnished);
		} else {
			address.textProperty().addListener(new TextChanged());
			postcode.textProperty().addListener(new TextChanged());
			price.textProperty().addListener(new TextChanged());
			beds.textProperty().addListener(new TextChanged());
			baths.textProperty().addListener(new TextChanged());
			deposit.textProperty().addListener(new TextChanged());
			description.textProperty().addListener(new TextChanged());
		}

		if (dateComboArray.size() == 0) {
			dateComboArray = setupDate("2015-01-01");
		}

		HBox dateAvailable = new HBox(10);
		dateAvailable.getChildren().addAll(dateComboArray.get(0),
				dateComboArray.get(1), dateComboArray.get(2));
		description.setPrefSize(300, 100);
		description.setWrapText(true);

		vBoxDesc.getChildren().addAll(labelDesc, description);

		ButtonType button1 = new ButtonType("150,150,150", null, "Save", 150,
				30);
		Button buttonSave = new SetupButton().CreateButton(button1);
		buttonSave.setCursor(Cursor.HAND);
		buttonSave.setOnAction(new ApplyChanges());

		grid.addRow(2, labelAddress, address);
		grid.addRow(3, labelPostcode, postcode);
		grid.addRow(4, labelPrice, price);
		grid.addRow(5, labelBeds, beds);
		grid.addRow(6, labelBaths, baths);
		grid.addRow(7, labelFurnished, furnished);
		grid.addRow(8, labelDate, dateAvailable);
		grid.addRow(9, labelDeposit, deposit);
		grid.addRow(10, vBoxDesc);
		if (hid != 0) {
			grid.add(buttonSave, 2, 11);
		}

		GridPane.setConstraints(address, 1, 2, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(vBoxDesc, 0, 10, 3, 1, HPos.CENTER, VPos.CENTER);

	}

	public void setupHousePictures() {

		TilePane imageTiles = new TilePane();

		imageTiles.setVgap(25);
		imageTiles.setHgap(25);
		imageTiles.setPadding(new Insets(20, 20, 20, 20));
		imageTiles.setTileAlignment(Pos.CENTER);
		imageTiles.setPrefColumns(3);

		houseImages = new ArrayList<HouseImage>();
		ScrollPane imageWindow = new ScrollPane();
		imageWindow.setMinSize(545, 600);

		Label labelUpload = new Label("Add a new image:");
		labelUpload.setFont(Font.font(null, FontWeight.BOLD, 14));
		uploadPathImage = new TextField();
		uploadPathImage.setEditable(false);

		ButtonType button1 = new ButtonType("150,150,150", null, "Browse", 70,
				30);
		Button buttonBrowse = new SetupButton().CreateButton(button1);
		buttonBrowse.setCursor(Cursor.HAND);
		buttonBrowse.setOnAction(new Browse());

		ButtonType button2 = new ButtonType("150,150,150", null, "Upload", 70,
				30);
		buttonUpload = new SetupButton().CreateButton(button2);
		buttonUpload.setDisable(true);
		buttonUpload.setOnAction(new Upload());

		ButtonType button3 = new ButtonType("150,150,150", null, "Delete", 70,
				30);
		Button buttonDelete = new SetupButton().CreateButton(button3);
		buttonDelete.setOnAction(new DeleteImage());

		HBox buttons = new HBox(10);
		buttons.getChildren().addAll(buttonBrowse, buttonUpload);
		grid.addRow(1, labelUpload, uploadPathImage, buttons);

		if (hid != 0) {
			houseImages = Database.getHouseImageSet(hid);

			for (int i = 0; i < houseImages.size(); i++) {
				HouseImage input = houseImages.get(i);
				Image image = new Image(input.imageIS);
				ImageView propertyImage = new ImageView(image);
				propertyImage.setFitWidth(150);
				propertyImage.setFitHeight(120);

				CheckBox delete = new CheckBox();
				deleteImage.add(delete);

				VBox tile = new VBox(5);
				tile.setAlignment(Pos.CENTER);
				tile.getChildren().addAll(propertyImage, delete);
				imageTiles.getChildren().add(tile);
			}
		} else {
			for (int i = 0; i < imagePaths.size(); i++) {
				Image image = new Image("file:" + imagePaths.get(i));
				ImageView propertyImage = new ImageView(image);
				propertyImage.setFitWidth(150);
				propertyImage.setFitHeight(120);

				CheckBox delete = new CheckBox();
				deleteImage.add(delete);

				VBox tile = new VBox(5);
				tile.setAlignment(Pos.CENTER);
				tile.getChildren().addAll(propertyImage, delete);
				imageTiles.getChildren().add(tile);
			}
		}

		imageWindow.setContent(imageTiles);
		grid.add(imageWindow, 0, 2);
		grid.add(buttonDelete, 3, 2);
		GridPane.setConstraints(imageWindow, 0, 2, 3, 1, HPos.CENTER,
				VPos.CENTER);
	}

	/**
	 * Setup text field and browse button to select video file to upload
	 */
	private void setupHouseVideo() {
		// Create HBox to contain file browser
		HBox fileHBox = new HBox(10);
		// fileHBox.setAlignment(VPos.CENTER);
		Label fileLabel = new Label("File:");
		// Create file directory text field
		uploadPathVideo = new TextField();
		uploadPathVideo.setPrefWidth(250);
		uploadPathVideo.textProperty().addListener(new UploadTextChanged());

		// Add FileChooser button
		ButtonType button1 = new ButtonType("150,150,150", null, "Browse", 100,
				30);
		Button fileChooserButton = new SetupButton().CreateButton(button1);

		// Add Upload Video button
		if (uploadPathVideo.getText() != null) {

			// uploadVideoButton.setDisable(false);

			ButtonType button2 = new ButtonType("150,150,150", null, "Upload",
					100, 30);
			uploadVideoButton = new SetupButton().CreateButton(button2);
			uploadVideoButton.setDisable(true);

			// Setup Upload video button event
			uploadVideoButton.setOnAction(new Upload());
		}

		// Setup FileChooser (browse) button event
		fileChooserButton.setOnAction(new Browse());

		// Add text field to hbox
		fileHBox.getChildren().addAll(fileLabel, uploadPathVideo,
				fileChooserButton, uploadVideoButton);
		fileHBox.setAlignment(Pos.CENTER);

		// Add hbox to gridpane row 0, spanning two columns
		// (Node child, int columnIndex, int rowIndex, int colspan, int rowspan)
		grid.add(fileHBox, 0, 1);
		GridPane.setConstraints(fileHBox, 0, 1, 3, 1, HPos.CENTER, VPos.CENTER);

		if (hid == 0) {
			if (videoPath != null) {
				SetupVideoPlayer(videoPath);
				SetupRoomMarkers();
			}
		}
	}

	/**
	 * Sets up video player within page
	 */
	private void SetupVideoPlayer(String newVideoFileString) {

		StackPane videoPane = new StackPane();

		// House house = Database.getHouse(currentPropertyID);
		// HouseVideo vid = Database.getVideoInfo()
		video = new VideoElement(newVideoFileString);
		video.setStylesheet("resources/videoStyle.css");
		video.setWidth(500);
		video.setAutoplay(true);
		video.display(videoPane);

		// Add video player to GridPane
		grid.add(videoPane, 0, 2);
		GridPane.setConstraints(videoPane, 0, 2, 3, 1, HPos.CENTER, VPos.CENTER);
	}

	private void SetupRoomMarkers() {

		HBox markerSetup = new HBox(10);
		markerList.setPrefHeight(220);
		markerList.setMaxWidth(300);

		HBox markerListHeader = new HBox(10);
		markerListHeader.setMaxWidth(300);

		/* Add room name label and textfield */
		Label addNewRoomLabel = new Label("Add New Room: ");
		newRoomField = new TextField();

		/* Add Set Marker Button */
		ButtonType button1 = new ButtonType("150,150,150", null, "Set Marker",
				100, 30);
		Button setMarkerButton = new SetupButton().CreateButton(button1);
		setMarkerButton.setOnAction(new AddMarker());

		/* Add Set Marker Button */
		ButtonType button2 = new ButtonType("150,150,150", null, "Delete", 100,
				30);
		Button deleteMarker = new SetupButton().CreateButton(button2);
		deleteMarker.setOnAction(new deleteMarker());

		Label roomNamesHeader = new Label("Rooms");
		roomNamesHeader.setStyle("-fx-font-weight: bold");
		roomNamesHeader.setPrefWidth(190);
		Label videoTimesHeader = new Label("Times in Video");
		videoTimesHeader.setStyle("-fx-font-weight: bold");

		// Add marker setup to HBox
		markerSetup.getChildren().addAll(addNewRoomLabel, newRoomField,
				setMarkerButton);
		markerSetup.setAlignment(Pos.CENTER);
		markerListHeader.getChildren()
				.addAll(roomNamesHeader, videoTimesHeader);
		// markerListHeader.setAlignment(Pos.CENTER);
		markerList.setItems(items);
		// Add to GridPane
		grid.add(markerSetup, 0, 3);
		GridPane.setConstraints(markerSetup, 0, 3, 3, 1, HPos.CENTER,
				VPos.CENTER);
		grid.add(markerListHeader, 0, 4);
		GridPane.setConstraints(markerListHeader, 0, 4, 3, 1, HPos.CENTER,
				VPos.CENTER);
		grid.add(markerList, 0, 5);
		GridPane.setConstraints(markerList, 0, 5, 3, 1, HPos.CENTER,
				VPos.CENTER);
		grid.add(deleteMarker, 0, 6);
		GridPane.setConstraints(deleteMarker, 0, 6, 3, 1, HPos.CENTER,
				VPos.CENTER);
	}

	public class ApplyChanges implements EventHandler<ActionEvent> {

		public void handle(ActionEvent arg0) {
			User owner = Database.getUser("MVPTom");

			String dateAvailableString = dateComboArray.get(2).getValue() + "-"
					+ dateComboArray.get(1).getValue() + "-"
					+ dateComboArray.get(0).getValue();
			System.out.println(house.dateAvailable);
			System.out.println(dateAvailableString);
			Database.updateHouse(owner, house, "address", address.getText(),
					null, null, 0, 1);
			Database.updateHouse(owner, house, "postcode", postcode.getText(),
					null, null, 0, 1);
			Database.updateHouse(owner, house, "price", null, null, null,
					Integer.parseInt(price.getText()), 4);
			Database.updateHouse(owner, house, "rooms", null, null, null,
					Integer.parseInt(beds.getText()), 4);
			Database.updateHouse(owner, house, "bathrooms", null, null, null,
					Integer.parseInt(baths.getText()), 4);
			Database.updateHouse(owner, house, "furnished", null,
					furnished.isSelected(), null, 0, 2);
			Database.updateHouse(owner, house, "deposit", null, null, null,
					Integer.parseInt(deposit.getText()), 4);
			Database.updateHouse(owner, house, "description",
					description.getText(), null, null, 0, 1);
			Database.updateHouse(owner, house, "date_available",
					dateAvailableString, null, null, 0, 1);
			grid.getChildren().clear();
			createEditPage();
		}
	}

	public class CreateHouse implements EventHandler<ActionEvent> {

		public void handle(ActionEvent arg0) {
			String dateAvailableString = dateComboArray.get(2).getValue() + "-"
					+ dateComboArray.get(1).getValue() + "-"
					+ dateComboArray.get(0).getValue();

			if (CheckInfoPage() && imagePaths.size() > 0) {
				House newHouse = new House(address.getText());
				newHouse.address(address.getText());
				newHouse.bathrooms(Integer.parseInt(baths.getText()));
				newHouse.postcode(postcode.getText());
				newHouse.price(Integer.parseInt(price.getText()));
				newHouse.deposit(Integer.parseInt(deposit.getText()));
				newHouse.rooms(Integer.parseInt(beds.getText()));
				newHouse.dateAvailable(dateAvailableString);
				newHouse.furnished(furnished.isSelected());
				newHouse.description(description.getText());

				User temp = Database.getUser(currentUsername);
				if (!Database.checkHouseExists(temp, newHouse)) {
					int newhid = 0;
					try {
						Database.houseInsert(newHouse, null, null, temp);

						newhid = Database.getID(temp, newHouse, 2);
						System.out.println("House ID: " + newhid);
						House house = Database.getHouse(newhid);
						for (int i = 0; i < imagePaths.size(); i++) {
							try {
								Database.insertHouseImage(imagePaths.get(i),
										house, temp);
							} catch (SQLException e) {
								System.out.println("Failed to add image");
							}
						}

					} catch (IOException e) {
						System.out.println("House creation failed");
					}
					ArrayList<String> savedProperties = User
							.getSavedProperties(currentUsername);
					savedProperties.add(String.format("%03d", newhid));
					User.updateSavedProperties(currentUsername, savedProperties);

					root.getChildren().clear();
					slideID = prevSlideID;
					SlideContent sc = new SlideContent();
					sc.createSlide();
				}
			} else {
				System.out.println("House info incomplete");
			}
		}
	}

	public class Cancel implements EventHandler<ActionEvent> {

		public void handle(ActionEvent arg0) {
			root.getChildren().clear();
			slideID = LANDLORDPROPERTIES;
			SlideContent sc = new SlideContent();
			sc.createSlide();
		}

	}

	public boolean CheckInfoPage() {
		int check = 0;
		if (!address.getText().equals(""))
			check++;
		if (!baths.getText().equals("") && CheckNumber(baths.getText()))
			check++;
		if (!postcode.getText().equals(""))
			check++;
		if (!price.getText().equals("") && CheckNumber(price.getText()))
			check++;
		if (!beds.getText().equals("") && CheckNumber(beds.getText()))
			check++;
		if (!address.getText().equals(""))
			check++;
		if (!description.getText().equals(""))
			check++;

		if (check == 7) {
			return true;
		} else
			return false;
	}

	public boolean CheckNumber(String input) {

		try {
			Integer.parseInt(input);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public void UpdateTabLabels() {

		if (CheckInfoPage()) {
			infoStatus.setImage(tick);
		} else
			infoStatus.setImage(cross);

		if (imagePaths.size() > 0) {
			picStatus.setImage(tick);
		} else
			picStatus.setImage(cross);

	}

	public class TextChanged implements ChangeListener<String> {

		public void changed(ObservableValue<? extends String> arg0,
				String arg1, String arg2) {
			UpdateTabLabels();
		}

	}

	public class ChangePage implements EventHandler<ActionEvent> {
		final int direction;

		public ChangePage(int dir) {
			direction = dir;
		}

		public void handle(ActionEvent arg0) {
			grid.getChildren().clear();
			page = page + direction;
			createEditPage();
		}
	}

	public class Browse implements EventHandler<ActionEvent> {

		public void handle(ActionEvent arg0) {
			File newFile;

			// Open file chooser window
			FileChooser uploadChooser = new FileChooser();
			configureFileChooser(uploadChooser);
			Window fileChooserStage = null;

			// Replace profile picture with new one from selected file
			newFile = uploadChooser.showOpenDialog(fileChooserStage);
			if (newFile != null) {
				switch (page) {
				case INFO:
					break;
				case PICS:
					uploadPathImage.setText(newFile.getName());
					buttonUpload.setCursor(Cursor.HAND);
					buttonUpload.setDisable(false);
					filePath = newFile.getAbsolutePath();
					break;
				case VIDEO:
					uploadPathVideo.clear();
					uploadPathVideo.appendText(newFile.getName());
					uploadVideoButton.setDisable(false);
					filePath = newFile.getAbsolutePath();
					System.out.println(filePath);
					break;
				}

			}
		}

		private void configureFileChooser(FileChooser uploadChooser) {

			// Set directory that the file chooser will initially open into
			uploadChooser.setInitialDirectory(new File(System
					.getProperty("user.home")));

			switch (page) {
			case INFO:
				break;
			case PICS:
				// Set title of file chooser
				uploadChooser.setTitle("Choose Property Image to Upload");
				// Set file types displayed in the file chooser as png and jpg
				uploadChooser.getExtensionFilters().addAll(
						new FileChooser.ExtensionFilter("JPG, PNG", "*.jpg",
								"*.png"),
						new FileChooser.ExtensionFilter("PNG", "*.png"));
				break;
			case VIDEO:
				// Set title of file chooser
				uploadChooser.setTitle("Choose Video to Upload");
				// Set file types displayed in the file chooser as mp4
				uploadChooser.getExtensionFilters().add(
						new FileChooser.ExtensionFilter("MP4", "*.mp4"));
				break;
			}

		}
	}

	public class Upload implements EventHandler<ActionEvent> {

		public void handle(ActionEvent arg0) {

			if (hid == 0) {
				UpdateTabLabels();
				switch (page) {
				case INFO:
					break;
				case PICS:
					imagePaths.add(filePath);
					grid.getChildren().clear();
					createEditPage();
					break;
				case VIDEO:
					videoPath = filePath;
					SetupVideoPlayer(videoPath);
					SetupRoomMarkers();
					break;
				}
				return;
			}

			User owner = Database.getUser(currentUsername);
			try {
				boolean check = false;
				if (page == PICS) {
					check = Database.insertHouseImage(filePath, house, owner);
				} else if (page == VIDEO) {

				}

				if (check) {
					grid.getChildren().clear();
					createEditPage();
				}
			} catch (SQLException e) {
				System.out.println("Upload Failed");
			}
		}
	}

	public class DeleteImage implements EventHandler<ActionEvent> {

		public void handle(ActionEvent arg0) {

			if (hid == 0) {
				for (int i = 0; i < imagePaths.size(); i++) {
					if (deleteImage.get(i).isSelected()) {
						UpdateTabLabels();
						imagePaths.remove(i);
					}
				}
			}

			else {
				for (int i = 0; i < houseImages.size(); i++) {
					System.out.println("Check");
					if (deleteImage.get(i).isSelected()) {
						System.out.println(i);
						boolean check = Database.deleteHouseImage(houseImages
								.get(i));
						if (check) {
							System.out.println("Image Deleted");
						}
					}
				}
			}

			grid.getChildren().clear();
			createEditPage();
		}
	}

	/**
	 * Change listener class to enable upload button when text is entered in
	 * text field and disable if no text in field
	 * 
	 * @author hcw515
	 * 
	 */
	public class UploadTextChanged implements ChangeListener<String> {

		public void changed(ObservableValue<? extends String> arg0,
				String arg1, String arg2) {

			if (uploadPathVideo.getText() != null) {
				uploadVideoButton.setDisable(false);
			} else {
				uploadVideoButton.setDisable(true);
			}
		}
	}

	public class AddMarker implements EventHandler<ActionEvent> {

		public void handle(ActionEvent arg0) {

			if (!newRoomField.getText().equals("")) {

				// Create room name and video time labels
				HBox newMarker = new HBox(0);
				Label newRoomLabel = new Label(newRoomField.getText());
				newRoomLabel.setPrefWidth(200);
				Label newVideoTimeLabel = new Label(
						video.printCurrentVideoTime());
				newVideoTimeLabel.setPrefWidth(80);
				newMarker.getChildren().addAll(newRoomLabel, newVideoTimeLabel);

				newMarker.setAlignment(Pos.CENTER_LEFT);

				items.add(newMarker);
				newRoomField.clear();
			}
		}

	}

	/**
	 * Button handler for deleteMarkerButton
	 * 
	 * @author hcw515
	 */
	public class deleteMarker implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			int index = markerList.getSelectionModel().getSelectedIndex();

			if (index >= 0) {
				for (int i = index; i < items.size() - 1; i++) {
					items.set(i, items.get(i + 1));
				}

				items.remove(items.size() - 1);
			}

		}
	}

	public static ArrayList<ComboBox<String>> setupDate(String initialDate) {

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

		// Set the selected item of the combo boxes to the current account
		// settings
		int day = Integer.valueOf(initialDate.substring(8, 10), 10).intValue();
		int month = Integer.valueOf(initialDate.substring(5, 7), 10).intValue();
		int year = Integer.valueOf(initialDate.substring(0, 4), 10).intValue();
		comboAvailableDay.getSelectionModel().select(day - 1);
		comboAvailableMonth.getSelectionModel().select(month - 1);
		comboAvailableYear.getSelectionModel().select(2015 - year);

		// Add change listener to month combobox
		comboAvailableMonth.valueProperty().addListener(
				new ChangeListener<String>() {
					@Override
					public void changed(
							@SuppressWarnings("rawtypes") ObservableValue ov,
							String previousValue, String newValue) {
						int index = comboAvailableDay.getSelectionModel()
								.getSelectedIndex();
						comboAvailableDay.getItems().clear();
						switch (newValue) {
						case "09":
						case "04":
						case "06":
						case "11":
							for (int j = 1; j < 31; j++) {
								comboAvailableDay.getItems().add(
										String.format("%02d", j));
							}
							break;
						case "02":
							for (int j = 1; j < 29; j++) {
								comboAvailableDay.getItems().add(
										String.format("%02d", j));
							}
							break;
						default:
							for (int j = 1; j < 32; j++) {
								comboAvailableDay.getItems().add(
										String.format("%02d", j));
							}
							break;
						}
						if (index >= comboAvailableDay.getItems().size())
							index = comboAvailableDay.getItems().size() - 1;
						comboAvailableDay.getSelectionModel().select(index);
					}
				});

		ArrayList<ComboBox<String>> date = new ArrayList<ComboBox<String>>();
		date.add(comboAvailableDay);
		date.add(comboAvailableMonth);
		date.add(comboAvailableYear);
		return date;
	}
}
