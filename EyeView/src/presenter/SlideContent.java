package presenter;

import houses.HouseOverview;
import houses.HouseReviews;
import houses.MoreInfo;
import houses.VideoPage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import landlord.EditProperty;
import landlord.LandlordProperties;
import language.Translate;
import houses.GoogleMapsPage;
import Button.ButtonType;
import Button.SetupButton;
import Profile.AccountSettings;
import Profile.Login;
import Profile.ProfileViewer;
import Profile.Register;
import Profile.SavedProperties;
import database.Database;
import database.House;
import database.Search;
import database.User;

public class SlideContent extends Window {

	public ImageView profilePictureView;
	public static ComboBox<ImageView> languageComboBox = new ComboBox<ImageView>();
	public static HouseOverview houseAdverts;
	public static StartPage startPage;
	public static Login login;
	public static Register register;
	public static ProfileViewer profile;
	public static AccountSettings accountSettings;
	public static SavedProperties savedProperties;
	public static MoreInfo moreInfo;
	public static VideoPage videoPage;
	public static GoogleMapsPage mapPage;
	public static HouseReviews houseReviews;
	public static LandlordProperties landlordProperties;
	public static EditProperty editProperty;
	public static Label labelLogin, labelRegister, labelLogOut, labelProfile,
			labelSavedProperties, labelLandlordProperties, labelFilter,
			labelBedSearch, labelBedMin, labelBedMax, labelPriceSearch,
			labelPriceMin, labelPriceMax, labelDistanceSearch;
	public static Button videoButton, mapButton, infoButton, reviewsButton,
			buttonBack;
	public final ComboBox<Integer> minBeds = new ComboBox<Integer>();
	public final ComboBox<Integer> maxBeds = new ComboBox<Integer>();
	public final ComboBox<Integer> minPrice = new ComboBox<Integer>();
	public final ComboBox<Integer> maxPrice = new ComboBox<Integer>();
	public final ComboBox<Integer> distance = new ComboBox<Integer>();

	public void createSlide() {

		// Load all objects from XML file first
		try {
			new LoadXML();
		} catch (IOException e) {
			System.out.println("Error loading XML data.");
		}

		// Checks if the XML belongs to group 5 (EyeHouse)
		if (groupID.matches("5")) {

			// Cancel Advert timer if it is running
			if (advertTimer != null)
				advertTimer.stop();
			clearSlideData();
			// Loads objects not within the XML for a given slide
			switch (slideID) {
			case STARTPAGE:
				createStartSlide();
				break;
			case INDEX:
				createLoggedOutSlide();
				break;
			case HOUSES:
				createHomeSlide();
				break;
			case LOGIN:
				createLoginSlide();
				break;
			case REGISTER:
				createRegisterSlide();
				break;
			case PROFILE:
				createProfileSlide();
				break;
			case ACCOUNTSETTINGS:
				createAccountSettingsSlide();
				break;
			case SAVEDPROPERTIES:
				createSavedPropertySlide();
				break;
			case HOUSE:
				createPropertySlide();
				break;
			case MOREINFO:
				createMoreInfoSlide();
				break;
			case REVIEWS:
				createReviewsSlide();
				break;
			case MAP:
				createMapSlide();
				break;
			case VIDEO:
				createVideoSlide();
				break;
			case LANDLORDPROPERTIES:
				createLandlordPropertiesSlide();
				break;
			case EDITPROPERTY:
				createEditPropertySlide();
				break;
			case RESULTS:
				createResultsSlide();
				break;
			default:
				break;
			}
		}
	}

	private void createStartSlide() {
		startPage = new StartPage();
	}

	private void createLoggedOutSlide() {

		ArrayList<Integer> houseIDs = new ArrayList<Integer>();
		houseIDs = Database.selectAllHouses();
		ArrayList<House> houses = getDisplayHouses(houseIDs);

		houseAdverts = new HouseOverview(false, houses);
		createSidebar();
		createMenuBar();
	}

	private void createHomeSlide() {

		ArrayList<Integer> houseIDs = new ArrayList<Integer>();
		houseIDs = Database.selectAllHouses();
		ArrayList<House> houses = getDisplayHouses(houseIDs);

		houseAdverts = new HouseOverview(false, houses);
		createSidebar();
		createMenuBar();
	}

	private void createLoginSlide() {

		login = new Login();
		createSidebar();
	}

	private void createRegisterSlide() {

		register = new Register();
		createSidebar();
	}

	private void createProfileSlide() {

		profile = new ProfileViewer(viewedUsername);
		createSidebar();
	}

	private void createAccountSettingsSlide() {

		accountSettings = new AccountSettings();
		createSidebar();
	}

	private void createSavedPropertySlide() {

		savedProperties = new SavedProperties();
		createSidebar();
	}

	private void createPropertySlide() {

		House house = Database.getHouse(currentPropertyID);
		ArrayList<House> houseArray = new ArrayList<House>();
		houseArray.add(house);

		houseAdverts = new HouseOverview(true, houseArray);
		createSidebar();
		createMenuBar();
	}

	public void createMoreInfoSlide() {

		moreInfo = new MoreInfo();
		createSidebar();
		createMenuBar();
	}

	public void createReviewsSlide() {

		houseReviews = new HouseReviews();
		createSidebar();
		createMenuBar();
	}

	public void createMapSlide() {

		mapPage = new GoogleMapsPage();
		createSidebar();
		createMenuBar();
	}

	public void createVideoSlide() {

		videoPage = new VideoPage();
		createSidebar();
		createMenuBar();
	}

	public void createLandlordPropertiesSlide() {
		landlordProperties = new LandlordProperties(currentUsername);
		createSidebar();
	}

	public void createEditPropertySlide() {
		editProperty = new EditProperty(0, currentPropertyID);
		createSidebar();
	}

	public void createResultsSlide() {
		houseAdverts = new HouseOverview(false, searchResults);
		createSidebar();
		createMenuBar();
	}

	public void createSidebar() {

		VBox sidebar = new VBox(18);

		if (currentUsername != null) {

			User currentUser = Database.getUser(currentUsername);
			try {
				InputStream binaryStream = currentUser.profimg.getBinaryStream(
						1, currentUser.profimg.length());
				Image profilePicture = new Image(binaryStream);
				profilePictureView = new ImageView(profilePicture);
				// Set maximum dimensions for profile picture
				profilePictureView.setFitWidth(100);
				profilePictureView.setFitHeight(100);
				profilePictureView.setPreserveRatio(true);
				sidebar.getChildren().add(profilePictureView);
			} catch (SQLException e) {
				System.out.println("Failed to retrieve profile picture.");
				e.printStackTrace();
			}

			Label labelName = new Label(currentUser.first_name);
			labelName.setWrapText(true);
			labelName.setFont(Font.font(null, FontWeight.BOLD, 20));
			labelName.setMinHeight(40);
			labelName.setAlignment(Pos.TOP_CENTER);
			labelName.setTextFill(Color.web("#162252FF"));

			labelProfile = new Label(Translate.translateText(languageIndex,
					"Profile"));
			labelProfile.setFont(new Font(16));
			labelProfile.setMaxWidth(140);
			labelProfile.setAlignment(Pos.CENTER);
			labelProfile.setTextAlignment(TextAlignment.CENTER);
			labelProfile.setWrapText(true);
			labelProfile.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent arg0) {
					viewedUsername = currentUsername;
					loadSlide(PROFILE);
				}
			});

			labelSavedProperties = new Label(Translate.translateText(
					languageIndex, "Saved Properties"));
			labelSavedProperties.setFont(new Font(16));
			labelSavedProperties.setMaxWidth(140);
			labelSavedProperties.setAlignment(Pos.CENTER);
			labelSavedProperties.setTextAlignment(TextAlignment.CENTER);
			labelSavedProperties.setWrapText(true);
			labelSavedProperties
					.setOnMouseClicked(new EventHandler<MouseEvent>() {
						public void handle(MouseEvent arg0) {
							loadSlide(SAVEDPROPERTIES);
						}
					});

			labelLandlordProperties = new Label(Translate.translateText(
					languageIndex, "My Properties"));
			labelLandlordProperties.setFont(new Font(16));
			labelLandlordProperties.setMaxWidth(140);
			labelLandlordProperties.setAlignment(Pos.CENTER);
			labelLandlordProperties.setTextAlignment(TextAlignment.CENTER);
			labelLandlordProperties.setWrapText(true);
			labelLandlordProperties
					.setOnMouseClicked(new EventHandler<MouseEvent>() {
						public void handle(MouseEvent arg0) {
							loadSlide(LANDLORDPROPERTIES);
						}
					});
			labelLogOut = new Label(Translate.translateText(languageIndex,
					"Log Out"));
			labelLogOut.setFont(new Font(16));
			labelLogOut.setMaxWidth(140);
			labelLogOut.setAlignment(Pos.CENTER);
			labelLogOut.setTextAlignment(TextAlignment.CENTER);
			labelLogOut.setWrapText(true);
			labelLogOut.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent arg0) {
					currentUsername = null;
					loadSlide(INDEX);
				}
			});

			if (slideID == SAVEDPROPERTIES || slideID == HOUSE)
				labelSavedProperties.setFont(Font.font(null, FontWeight.BOLD,
						16.5));
			else if (slideID == PROFILE || slideID == ACCOUNTSETTINGS)
				labelProfile.setFont(Font.font(null, FontWeight.BOLD, 16.5));
			else if (slideID == LANDLORDPROPERTIES || slideID == EDITPROPERTY)
				labelLandlordProperties.setFont(Font.font(null,
						FontWeight.BOLD, 16.5));

			setupLabelHover(labelProfile);
			setupLabelHover(labelSavedProperties);
			setupLabelHover(labelLandlordProperties);
			setupLabelHover(labelLogOut);

			sidebar.getChildren().addAll(labelName, labelProfile,
					labelSavedProperties);
			if (currentUser.landlord)
				sidebar.getChildren().add(labelLandlordProperties);

			sidebar.getChildren().addAll(labelLogOut);

			if (slideID == HOUSES || slideID == HOUSE || slideID == RESULTS)
				createSearchBar();
		} else {
			labelLogin = new Label(Translate.translateText(languageIndex,
					"Login"));
			labelLogin.setFont(new Font(16));
			labelLogin.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent arg0) {
					root.getChildren().clear();
					slideID = LOGIN;
					createSlide();
				}
			});

			labelRegister = new Label(Translate.translateText(languageIndex,
					"Register"));
			labelRegister.setFont(new Font(16));
			labelRegister.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent arg0) {
					root.getChildren().clear();
					slideID = REGISTER;
					createSlide();
				}
			});

			if (slideID == LOGIN)
				labelLogin.setFont(Font.font(null, FontWeight.BOLD, 16.5));
			else if (slideID == REGISTER)
				labelRegister.setFont(Font.font(null, FontWeight.BOLD, 16.5));

			setupLabelHover(labelLogin);
			setupLabelHover(labelRegister);

			sidebar.getChildren().addAll(labelLogin, labelRegister);
		}

		sidebar.relocate(15, 30);
		sidebar.setMinWidth(150);
		sidebar.setAlignment(Pos.CENTER);

		root.getChildren().add(sidebar);
	}

	public void createMenuBar() {

		HBox buttonRow = new HBox(5);

		ButtonType button1 = new ButtonType(null, null, "Video Tour", 140, 50);
		videoButton = new SetupButton().CreateButton(button1);
		videoButton.setFocusTraversable(false);
		videoButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				loadSlide(VIDEO);
			}
		});

		ButtonType button2 = new ButtonType(null, null, "Map", 140, 50);
		mapButton = new SetupButton().CreateButton(button2);
		mapButton.setFocusTraversable(false);
		mapButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				loadSlide(MAP);
			}
		});

		ButtonType button3 = new ButtonType(null, null, "Information", 140, 50);
		infoButton = new SetupButton().CreateButton(button3);
		infoButton.setFocusTraversable(false);
		infoButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				loadSlide(MOREINFO);
			}
		});

		ButtonType button4 = new ButtonType(null, null, "Reviews", 140, 50);
		reviewsButton = new SetupButton().CreateButton(button4);
		reviewsButton.setFocusTraversable(false);
		reviewsButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				loadSlide(REVIEWS);
			}
		});

		if (slideID == INDEX) {
			createWarningPopup("Please login for full features.");
			buttonRow.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent t) {
					dialogStage.show();
				}
			});
			videoButton.setDisable(true);
			mapButton.setDisable(true);
			infoButton.setDisable(true);
			reviewsButton.setDisable(true);
		}

		setupTranslate();
		buttonRow.relocate(280, 0);
		buttonRow.getChildren().addAll(videoButton, mapButton, infoButton,
				reviewsButton, new Label("    "), languageComboBox,
				new Label("        "));
		buttonRow.setId("menubar");
		buttonRow.getStylesheets().add(
				new File("resources/menubarStyle.css").toURI().toString());
		root.getChildren().add(buttonRow);
	}

	private void setupLabelHover(final Label input) {

		final boolean selected;
		if (input.getFont().getSize() == 16.5)
			selected = true;
		else
			selected = false;

		input.setCursor(Cursor.HAND);
		input.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				if (selected)
					input.setFont(Font.font(null, FontWeight.BOLD, 17));
				else
					input.setFont(new Font(17));
			}
		});

		input.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				if (selected)
					input.setFont(Font.font(null, FontWeight.BOLD, 16.5));
				else
					input.setFont(new Font(16));
			}
		});
	}

	public static void setupBackButton() {

		ImageView buttonBack = new ImageView(new Image(
				"file:resources/advert_icons/back.png"));
		buttonBack.relocate(200, 20);
		buttonBack.setPreserveRatio(true);
		buttonBack.setFitWidth(80);
		buttonBack.setCursor(Cursor.HAND);
		buttonBack.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent ae) {
				if (currentUsername != null)
					loadSlide(HOUSES);
				else
					loadSlide(INDEX);
				HouseOverview.setTimerState("PAUSE");
			}
		});

		root.getChildren().add(buttonBack);
	}

	public void setupTranslate() {

		Translate.translateBox();
		languageComboBox.valueProperty().addListener(new LanguageChange());
		languageComboBox.getStylesheets().add(
				new File("resources/languageStyle.css").toURI().toString());
	}

	public class LanguageChange implements ChangeListener<ImageView> {

		@Override
		public void changed(ObservableValue<? extends ImageView> arg0,
				ImageView arg1, ImageView arg2) {
			languageIndex = languageComboBox.getSelectionModel()
					.getSelectedIndex();

			if (slideID == INDEX || slideID == HOUSES) {
				houseAdverts.updateLanguage();

				videoButton.setText(Translate.translateText(languageIndex,
						"Video Tour"));
				mapButton
						.setText(Translate.translateText(languageIndex, "Map"));
				infoButton.setText(Translate.translateText(languageIndex,
						"Information"));
				reviewsButton.setText(Translate.translateText(languageIndex,
						"Reviews"));
			}

			if (slideID == LOGIN || slideID == REGISTER || slideID == INDEX) {
				labelLogin.setText(Translate.translateText(languageIndex,
						"Login"));
				labelRegister.setText(Translate.translateText(languageIndex,
						"Register"));
			}

			if (slideID != INDEX && slideID != LOGIN && slideID != REGISTER) {

				labelProfile.setText(Translate.translateText(languageIndex,
						"Profile"));
				labelSavedProperties.setText(Translate.translateText(
						languageIndex, "Saved Properties"));
				labelLandlordProperties.setText(Translate.translateText(
						languageIndex, "My Properties"));
				labelLogOut.setText(Translate.translateText(languageIndex,
						"Log Out"));
			}
			if (slideID == HOUSES || slideID == HOUSE || slideID == RESULTS) {
				labelBedSearch.setText(Translate.translateText(languageIndex,
						"Bedrooms"));
				labelBedMin.setText(Translate.translateText(languageIndex,
						"Min"));
				labelBedMax.setText(Translate.translateText(languageIndex,
						"Max"));

				labelPriceSearch.setText(Translate.translateText(languageIndex,
						"Price: "));
				labelPriceMin.setText(Translate.translateText(languageIndex,
						"Min") + " (�)");
				labelPriceMax.setText(Translate.translateText(languageIndex,
						"Max") + " (�)");

				labelDistanceSearch.setText(Translate.translateText(
						languageIndex, "Distance to University") + " (km)");
			}
		}
	}

	public ArrayList<House> getDisplayHouses(ArrayList<Integer> houseIDs) {

		ArrayList<House> houses = new ArrayList<House>();

		if (houseIDs.size() < 10) {
			for (int i = 0; i < houseIDs.size(); i++) {
				houses.add(Database.getHouse(houseIDs.get(i)));
			}
		} else {
			for (int i = 0; i < 10; i++) {
				houses.add(Database.getHouse(houseIDs.get(i)));
			}
		}

		return houses;
	}

	public void createSearchBar() {

		GridPane searchFields = new GridPane();
		searchFields.setVgap(5);
		searchFields.setHgap(5);
		VBox minBedsColumn = new VBox(5);
		VBox maxBedsColumn = new VBox(5);
		VBox minPriceColumn = new VBox(5);
		VBox maxPriceColumn = new VBox(5);

		labelFilter = new Label(Translate.translateText(languageIndex,
				"Filter results") + ":");
		labelFilter.setFont(new Font(15));

		labelBedSearch = new Label(Translate.translateText(languageIndex,
				"Bedrooms"));
		labelBedSearch.setFont(Font.font(null, FontWeight.BOLD, 12));
		labelBedMin = new Label(Translate.translateText(languageIndex, "Min"));
		labelBedMax = new Label(Translate.translateText(languageIndex, "Max"));

		labelPriceSearch = new Label(Translate.translateText(languageIndex,
				"Price"));
		labelPriceSearch.setFont(Font.font(null, FontWeight.BOLD, 12));
		labelPriceMin = new Label(Translate.translateText(languageIndex, "Min")
				+ " (�)");
		labelPriceMax = new Label(Translate.translateText(languageIndex, "Max")
				+ " (�)");

		labelDistanceSearch = new Label(Translate.translateText(languageIndex,
				"Distance to University") + " (km)");

		labelDistanceSearch.setFont(Font.font(null, FontWeight.BOLD, 12));
		labelDistanceSearch.setMaxWidth(110);
		labelDistanceSearch.setWrapText(true);

		// Button setup
		ButtonType button1 = new ButtonType("166,208,255", null,
				Translate.translateText(languageIndex, "Search"), 75, 25);
		Button goButton = new SetupButton().CreateButton(button1);
		goButton.setOnAction(new searchHandler());

		// Combobox for
		minBeds.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		maxBeds.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		minBeds.getSelectionModel().select(0);
		maxBeds.getSelectionModel().select(9);

		minPrice.getItems().addAll(40, 50, 60, 70, 80, 90, 100, 110, 120, 130);
		maxPrice.getItems().addAll(40, 50, 60, 70, 80, 90, 100, 110, 120, 130);
		minPrice.getSelectionModel().select(0);
		maxPrice.getSelectionModel().select(9);

		distance.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		distance.getSelectionModel().select(0);

		minBeds.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0,
					Integer oldValue, Integer newValue) {
				if (newValue > maxBeds.getSelectionModel().getSelectedItem()) {
					maxBeds.getSelectionModel().select(newValue - 1);
				}
			}
		});
		maxBeds.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0,
					Integer oldValue, Integer newValue) {
				if (newValue < minBeds.getSelectionModel().getSelectedItem()) {
					minBeds.getSelectionModel().select(newValue - 1);
				}
			}
		});
		minPrice.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0,
					Integer oldValue, Integer newValue) {
				if (newValue > maxPrice.getSelectionModel().getSelectedItem()) {
					maxPrice.getSelectionModel().select(newValue / 10 - 4);
				}
			}
		});
		maxPrice.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0,
					Integer oldValue, Integer newValue) {
				if (newValue < minPrice.getSelectionModel().getSelectedItem()) {
					minPrice.getSelectionModel().select(newValue / 10 - 4);
				}
			}
		});

		maxBedsColumn.getChildren().addAll(labelBedMax, maxBeds);
		minBedsColumn.getChildren().addAll(labelBedMin, minBeds);

		maxPriceColumn.getChildren().addAll(labelPriceMax, maxPrice);
		minPriceColumn.getChildren().addAll(labelPriceMin, minPrice);

		Rectangle centreBox = RectangleBuilder.create().arcWidth(30)
				// Curved edges
				.arcHeight(30).x(15).y(420).fill(Color.TRANSPARENT)
				.strokeWidth(1).stroke(Color.rgb(33, 51, 76)).build();
		centreBox.setWidth(163);
		centreBox.setHeight(285);

		// Include content on each row and column
		searchFields.addRow(0, labelFilter);
		searchFields.addRow(2, labelBedSearch);
		GridPane.setConstraints(labelBedSearch, 0, 2, 2, 1, HPos.CENTER,
				VPos.CENTER);
		searchFields.add(minBedsColumn, 0, 3);
		searchFields.add(maxBedsColumn, 1, 3);
		searchFields.addRow(5, labelPriceSearch);
		GridPane.setConstraints(labelPriceSearch, 0, 5, 2, 1, HPos.CENTER,
				VPos.CENTER);
		searchFields.add(minPriceColumn, 0, 6);
		searchFields.add(maxPriceColumn, 1, 6);
		searchFields.addRow(8, labelDistanceSearch);
		GridPane.setConstraints(labelDistanceSearch, 0, 8, 2, 1, HPos.CENTER,
				VPos.CENTER);
		searchFields.addRow(9, distance);
		GridPane.setConstraints(distance, 0, 9, 2, 1, HPos.CENTER, VPos.TOP);
		searchFields.addRow(12, goButton);
		GridPane.setConstraints(goButton, 0, 12, 2, 1, HPos.CENTER, VPos.BOTTOM);

		searchFields.relocate(30, 390);
		root.getChildren().addAll(centreBox, searchFields);
	}

	public class searchHandler implements EventHandler<ActionEvent> {

		public void handle(ActionEvent arg0) {
			ArrayList<House> result1 = new ArrayList<House>();
			ArrayList<House> result2 = new ArrayList<House>();
			ArrayList<House> result3 = new ArrayList<House>();
			ArrayList<House> result4 = new ArrayList<House>();
			ArrayList<Integer> result5 = new ArrayList<Integer>();

			ArrayList<House> outputBeds = new ArrayList<House>();
			ArrayList<House> outputPrice = new ArrayList<House>();
			ArrayList<House> outputPriceBed = new ArrayList<House>();
			ArrayList<House> output = new ArrayList<House>();

			result1 = Search.rooms(minBeds.getSelectionModel()
					.getSelectedItem(), true);
			result2 = Search.rooms(maxBeds.getSelectionModel()
					.getSelectedItem(), false);
			result3 = Search.price(minPrice.getSelectionModel()
					.getSelectedItem(), true);
			result4 = Search.price(maxPrice.getSelectionModel()
					.getSelectedItem(), false);
			result5 = Search.searchProximity("YO10 5DD", distance
					.getSelectionModel().getSelectedItem());

			for (int i = 0; i < result1.size(); i++) {
				for (int j = 0; j < result2.size(); j++) {
					if (result2.get(j).hid == result1.get(i).hid) {
						outputBeds.add(result1.get(i));
					}
				}
			}

			for (int i = 0; i < result3.size(); i++) {
				for (int j = 0; j < result4.size(); j++) {
					if (result4.get(j).hid == result3.get(i).hid) {
						outputPrice.add(result3.get(i));
					}
				}
			}

			for (int i = 0; i < outputBeds.size(); i++) {
				for (int j = 0; j < outputPrice.size(); j++) {
					if (outputPrice.get(j).hid == outputBeds.get(i).hid) {
						outputPriceBed.add(outputBeds.get(i));
					}
				}
			}

			for (int i = 0; i < result5.size(); i++) {
				for (int j = 0; j < outputPriceBed.size(); j++) {
					if (outputPriceBed.get(j).hid == result5.get(i)) {
						output.add(outputPriceBed.get(j));
					}
				}
			}

			searchResults = output;
			if (output.size() > 0) {
				loadSlide(RESULTS);
			}
		}
	}
	
	private void clearSlideData(){
		
		switch (prevSlideID) {
		case STARTPAGE:
			startPage = null;
			break;
		case RESULTS:
		case INDEX:
		case HOUSE:	
		case HOUSES:
			houseAdverts.dispose();
			houseAdverts = null;
			break;
		case LOGIN:
			login = null;
			break;
		case REGISTER:
			register = null;
			break;
		case PROFILE:
			profile = null;
			break;
		case ACCOUNTSETTINGS:
			accountSettings = null;
			break;
		case SAVEDPROPERTIES:
			savedProperties.dispose();
			savedProperties = null;
			break;
		case MOREINFO:
			moreInfo = null;
			break;
		case REVIEWS:
			houseReviews = null;
			break;
		case MAP:
			mapPage = null;
			break;
		case VIDEO:
			videoPage = null;
			break;
		case LANDLORDPROPERTIES:
			landlordProperties = null;
			break;
		case EDITPROPERTY:
			editProperty = null;
			break;
		default:
			break;
		}
		System.gc();
	}
}
