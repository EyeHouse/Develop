package landlord;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;

import Button.ButtonType;
import Button.SetupButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
	private static final int INFO = 0;
	private static final int PICS = 1;
	private static final int VIDEO = 2;
	private GridPane grid = new GridPane();
	private House house;
	private TextField uploadPath;
	private Button buttonUpload;
	private String filePath;

	public EditProperty(int page, int houseID) {
		this.page = page;
		house = Database.getHouse(houseID);

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
			break;
		}
		setupButtons();

	}

	private void setupGrid() {
		// Set grid size and spacing in group.
		grid.setHgap(50);
		grid.setVgap(30);
		grid.relocate(220, 20);

		ColumnConstraints col1 = new ColumnConstraints();
		col1.setMinWidth(100);
		grid.getColumnConstraints().addAll(col1, col1, col1);
	}

	private void setupTabLabels(int page) {

		HBox hBoxTabs = new HBox(60);
		Label labelInfoTab = new Label("1. House Information");
		Label labelPictureTab = new Label("2. House Pictures");
		Label labelVideoTab = new Label("3. House Videos");
		labelInfoTab.setFont(new Font(16));
		labelPictureTab.setFont(new Font(16));
		labelVideoTab.setFont(new Font(16));

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

		hBoxTabs.getChildren().addAll(labelInfoTab, labelPictureTab,
				labelVideoTab);

		grid.add(hBoxTabs, 0, 0);
		GridPane.setConstraints(hBoxTabs, 0, 0, 3, 1, HPos.CENTER, VPos.CENTER);
	}

	private void setupButtons() {
		HBox buttons = new HBox(10);
		ButtonType button1 = new ButtonType("150,150,150", null, "Next", 50, 30);
		Button buttonNext = new SetupButton().CreateButton(button1);
		buttonNext.setCursor(Cursor.HAND);
		buttonNext.setOnAction(new ChangePage(1));

		ButtonType button2 = new ButtonType("150,150,150", null, "Prev", 50, 30);
		Button buttonPrev = new SetupButton().CreateButton(button2);
		buttonPrev.setCursor(Cursor.HAND);
		buttonPrev.setOnAction(new ChangePage(-1));

		buttons.getChildren().addAll(buttonPrev, buttonNext);
		if (page == 0)
			buttonPrev.setVisible(false);
		if (page == 2)
			buttonNext.setVisible(false);
		grid.add(buttons, 3, 0);
		GridPane.setConstraints(buttons, 3, 0, 1, 1, HPos.RIGHT, VPos.CENTER);
	}

	private void setupHouseInfo() {
		VBox vBoxDesc = new VBox(10);
		Label labelAddress = new Label("Address:");
		Label labelPostcode = new Label("Postcode:");
		Label labelPrice = new Label("Price:");
		Label labelBeds = new Label("Bedrooms:");
		Label labelBaths = new Label("Bathrooms:");
		Label labelDate = new Label("Date Available:");
		Label labelDeposit = new Label("Deposit:");
		Label labelDesc = new Label("Description");

		labelAddress.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelPostcode.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelPrice.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelBeds.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelBaths.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelDate.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelDeposit.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelDesc.setFont(Font.font(null, FontWeight.BOLD, 14));

		TextField address = new TextField(house.address);
		TextField postcode = new TextField(house.postcode);
		TextField price = new TextField("£" + Integer.toString(house.price));
		TextField beds = new TextField(Integer.toString(house.rooms));
		TextField baths = new TextField(Integer.toString(house.bathrooms));
		TextField deposit = new TextField("£" + Integer.toString(house.deposit));
		TextArea description = new TextArea(house.description);

		ArrayList<ComboBox<String>> dateComboArray = setupDate(house.dateAvailable);
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
		grid.addRow(7, labelDate, dateAvailable);
		grid.addRow(8, labelDeposit, deposit);
		grid.addRow(9, vBoxDesc);
		grid.add(buttonSave, 2, 10);
		GridPane.setConstraints(address, 1, 2, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(vBoxDesc, 0, 9, 3, 1, HPos.CENTER, VPos.CENTER);

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

	public void setupHousePictures() {

		GridPane imageGrid = new GridPane();

		imageGrid.setVgap(50);
		imageGrid.setHgap(50);
		int x = 0;
		int y = 0;
		ArrayList<HouseImage> houseImages = new ArrayList<HouseImage>();
		ScrollPane imageWindow = new ScrollPane();
		imageWindow.setPrefSize(560, 600);

		Label labelUpload = new Label("Add a new image:");
		labelUpload.setFont(Font.font(null, FontWeight.BOLD, 14));
		uploadPath = new TextField();
		uploadPath.setEditable(false);
		
		ButtonType button1 = new ButtonType("150,150,150", null, "Browse", 70, 30);
		Button buttonBrowse = new SetupButton().CreateButton(button1);
		buttonBrowse.setCursor(Cursor.HAND);
		buttonBrowse.setOnAction(new Browse());

		ButtonType button2 = new ButtonType("150,150,150", null, "Upload", 70, 30);
		buttonUpload = new SetupButton().CreateButton(button2);
		buttonUpload.setDisable(true);
		buttonUpload.setOnAction(new Upload());
		
		HBox buttons = new HBox(10);
		buttons.getChildren().addAll(buttonBrowse,buttonUpload);
		grid.addRow(1,labelUpload,uploadPath,buttons);
		
		houseImages = Database.getHouseImageSet(house.hid);

		for (int i = 0; i < houseImages.size(); i++) {
			HouseImage input = houseImages.get(i);
			Image image = new Image(input.imageIS);
			ImageView propertyImage = new ImageView(image);
			propertyImage.setFitWidth(150);
			propertyImage.setFitHeight(150);
			imageGrid.add(propertyImage, x, y);
			x++;
			if (x > 2) {
				x = 0;
				y++;
			}
		}

		imageWindow.setContent(imageGrid);
		grid.add(imageWindow, 0, 2);
		GridPane.setConstraints(imageWindow, 0, 2, 3, 1, HPos.CENTER,
				VPos.CENTER);
	}

	public class ApplyChanges implements EventHandler<ActionEvent> {

		public void handle(ActionEvent arg0) {

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
	
	public class Browse implements EventHandler<ActionEvent>{

		public void handle(ActionEvent arg0) {
			File newImageFile;

			// Open file chooser window
			FileChooser profilePictureChooser = new FileChooser();
			configureFileChooser(profilePictureChooser);
			Window fileChooserStage = null;

			// Replace profile picture with new one from selected file
			newImageFile = profilePictureChooser
					.showOpenDialog(fileChooserStage);
			if (newImageFile != null) {
				uploadPath.setText(newImageFile.getName());
				buttonUpload.setCursor(Cursor.HAND);
				buttonUpload.setDisable(false);
				filePath = newImageFile.getAbsolutePath();
			}
		}
		
		private void configureFileChooser(FileChooser profilePictureChooser) {

			// Set title of file chooser
			profilePictureChooser.setTitle("Choose Property Image to Upload");
			// Set directory that the file chooser will initially open into
			profilePictureChooser.setInitialDirectory(new File(System
					.getProperty("user.home")));
			// Set file types displayed in the file chooser as png and jpg
			profilePictureChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("JPG, PNG", "*.jpg", "*.png"),
					new FileChooser.ExtensionFilter("PNG", "*.png"));
		}
		
	}
	
	public class Upload implements EventHandler<ActionEvent>{

		public void handle(ActionEvent arg0) {
			User owner = Database.getUser("MVPTom");
			try {
				boolean check = Database.insertHouseImage(filePath, house, owner);
				if(check){
					grid.getChildren().clear();
					createEditPage();
				}
			} catch (SQLException e) {
				System.out.println("Upload Failed");
			}
		}
		
	}
}
