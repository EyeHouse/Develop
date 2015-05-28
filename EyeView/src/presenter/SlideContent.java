package presenter;

import houseAdverts.HouseOverview;
import houseAdverts.HouseReviews;
import houseAdverts.MoreInfo;
import houseAdverts.VideoPage;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import landlord.EditProperty;
import landlord.LandlordProperties;
import language.Translate;
import maps.GoogleMapsPage;
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
	public static Label labelLogin, labelRegister, labelLogOut, labelProfile,
			labelSavedProperties, labelLandlordProperties, labelBedSearch,
			labelBedMin, labelBedMax, labelPriceSearch, labelPriceMin,
			labelPriceMax, labelDistanceSearch;
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
		new StartPage();
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

		new Login();
		createSidebar();
	}

	private void createRegisterSlide() {

		new Register();
		createSidebar();
	}

	private void createProfileSlide() {

		new ProfileViewer(viewedUsername);
		createSidebar();
	}

	private void createAccountSettingsSlide() {

		new AccountSettings();
		createSidebar();
	}

	private void createSavedPropertySlide() {

		new SavedProperties();
		createSidebar();
		prevSlideID = slideID;
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

		new MoreInfo();
		createSidebar();
		createMenuBar();
	}

	public void createReviewsSlide() {

		new HouseReviews();
		createSidebar();
		createMenuBar();
	}

	public void createMapSlide() {

		new GoogleMapsPage();
		createSidebar();
		createMenuBar();
	}

	public void createVideoSlide() {

		new VideoPage();
		createSidebar();
		createMenuBar();
	}

	public void createLandlordPropertiesSlide() {
		new LandlordProperties(currentUsername);
		createSidebar();
		prevSlideID = slideID;
	}

	public void createEditPropertySlide() {
		new EditProperty(0, currentPropertyID);
		createSidebar();
	}

	public void createResultsSlide() {
		houseAdverts = new HouseOverview(false, searchResults);
		createSidebar();
		createMenuBar();
	}

	public void createSidebar() {
		VBox sidebar = new VBox(20);
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
			labelName.setTextFill(Color.web("#162252FF"));

			labelProfile = new Label(Translate.translateText(languageIndex,
					"Profile"));
			labelProfile.setFont(new Font(16));
			labelProfile.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent arg0) {
					viewedUsername = currentUsername;
					loadSlide(PROFILE);
				}
			});

			labelSavedProperties = new Label(Translate.translateText(
					languageIndex, "Saved Properties"));
			labelSavedProperties.setFont(new Font(16));
			labelSavedProperties.setMaxWidth(150);
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
			labelLandlordProperties
					.setOnMouseClicked(new EventHandler<MouseEvent>() {
						public void handle(MouseEvent arg0) {
							loadSlide(LANDLORDPROPERTIES);
						}
					});
			labelLogOut = new Label(Translate.translateText(languageIndex,
					"Log Out"));
			labelLogOut.setFont(new Font(16));
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
			
			if(slideID == HOUSES || slideID == HOUSE || slideID == RESULTS)
				sidebar.getChildren().add(createSearchBar());
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

		sidebar.relocate(30, 30);

		root.getChildren().add(sidebar);
	}

	public void createMenuBar() {
		HBox buttonRow = new HBox(5);
		ButtonType button1 = new ButtonType("144,171,199", null, "Video Tour",
				140, 40);
		videoButton = new SetupButton().CreateButton(button1);
		videoButton.setFocusTraversable(false);
		videoButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				loadSlide(VIDEO);
			}
		});

		ButtonType button2 = new ButtonType("144,171,199", null, "Map", 140, 40);
		mapButton = new SetupButton().CreateButton(button2);
		mapButton.setFocusTraversable(false);
		mapButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				root.getChildren().clear();
				slideID = MAP;
				createSlide();
			}
		});

		ButtonType button3 = new ButtonType("144,171,199", null, "Information",
				140, 40);
		infoButton = new SetupButton().CreateButton(button3);
		infoButton.setFocusTraversable(false);
		infoButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				root.getChildren().clear();
				slideID = MOREINFO;
				createSlide();
			}
		});

		ButtonType button4 = new ButtonType("144,171,199", null, "Reviews",
				140, 40);
		reviewsButton = new SetupButton().CreateButton(button4);
		reviewsButton.setFocusTraversable(false);
		reviewsButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				root.getChildren().clear();
				slideID = REVIEWS;
				createSlide();
			}
		});

		if (slideID == INDEX) {
			buttonRow.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent t) {
					createLoginWarning();
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
				reviewsButton, languageComboBox);

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
	}

	public class LanguageChange implements ChangeListener<ImageView> {

		@Override
		public void changed(ObservableValue<? extends ImageView> arg0,
				ImageView arg1, ImageView arg2) {
			languageIndex = languageComboBox.getSelectionModel()
					.getSelectedIndex();

			if (slideID == INDEX || slideID == HOUSES) {
				houseAdverts.UpdateLanguage();

				videoButton.setText(Translate.translateText(languageIndex,
						"Video Tour"));
				mapButton
						.setText(Translate.translateText(languageIndex, "Map"));
				infoButton.setText(Translate.translateText(languageIndex,
						"Information"));
				reviewsButton.setText(Translate.translateText(languageIndex,
						"Reviews"));
				labelBedSearch.setText(Translate.translateText(languageIndex,
						"Bedrooms: "));
				labelBedMin.setText(Translate.translateText(languageIndex,
						"Min"));
				labelBedMax.setText(Translate.translateText(languageIndex,
						"Max"));

				labelPriceSearch.setText(Translate.translateText(
						languageIndex, "Price: "));
				labelPriceMin.setText(Translate.translateText(languageIndex,
						"Min") + " (£)");
				labelPriceMax.setText(Translate.translateText(languageIndex,
						"Max") + " (£)");

				labelDistanceSearch.setText(Translate.translateText(
						languageIndex, "Distance to University") + ": (km)");
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
				labelLogOut.setText(Translate.translateText(languageIndex,
						"Log Out"));
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

	public void createLoginWarning() {

		Stage warningStage = new Stage();
		Group root = new Group();
		Image icon = new Image("file:./resources/images/warning.png");

		/* Initialises the warning stage */
		warningStage.setWidth(255);
		warningStage.setHeight(110);
		warningStage.setResizable(false);
		warningStage.getIcons().add(icon);
		warningStage.setTitle("Warning");

		/* Create warning message */
		ImageView iconView = new ImageView(icon);
		iconView.relocate(15, 15);
		Label warning = new Label("Login for full features.");
		warning.setFont(Font.font(null, 17));
		warning.relocate(75, 30);
		root.getChildren().addAll(iconView, warning);

		warningStage.setScene(new Scene(root));
		warningStage.show();
	}

	public GridPane createSearchBar() {

		GridPane searchFields = new GridPane();
		searchFields.setVgap(10);
		searchFields.setHgap(5);
		VBox minBedsColumn = new VBox(10);
		VBox maxBedsColumn = new VBox(10);
		VBox minPriceColumn = new VBox(10);
		VBox maxPriceColumn = new VBox(10);
		HBox beds = new HBox(10);
		HBox prices = new HBox(10);

		labelBedSearch = new Label(Translate.translateText(languageIndex,
				"Bedrooms: "));
		labelBedMin = new Label(Translate.translateText(languageIndex,
				"Min"));
		labelBedMax = new Label(Translate.translateText(languageIndex,
				"Max"));

		labelPriceSearch = new Label(Translate.translateText(
				languageIndex, "Price: "));
		labelPriceMin = new Label(Translate.translateText(languageIndex,
				"Min") + " (£)");
		labelPriceMax = new Label(Translate.translateText(languageIndex,
				"Max") + " (£)");

		labelDistanceSearch = new Label(Translate.translateText(
				languageIndex, "Distance to University") + ": (km)");

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

		beds.getChildren().addAll(minBedsColumn, maxBedsColumn);

		maxPriceColumn.getChildren().addAll(labelPriceMax, maxPrice);
		minPriceColumn.getChildren().addAll(labelPriceMin, minPrice);

		prices.getChildren().addAll(minPriceColumn, maxPriceColumn);

		ButtonType button1 = new ButtonType("166,208,255", null,
				Translate.translateText(languageIndex, "Search"), 100, 30);
		Button searchButton = new SetupButton().CreateButton(button1);
		searchButton.setOnAction(new searchHandler());
		searchFields.addRow(0, labelBedSearch);
		searchFields.addRow(1, beds);
		searchFields.addRow(2, labelPriceSearch);
		searchFields.addRow(3, prices);
		searchFields.addRow(4, labelDistanceSearch);
		searchFields.addRow(5, distance);
		searchFields.addRow(6, searchButton);
		GridPane.setConstraints(labelDistanceSearch, 0, 4, 2, 1, HPos.LEFT,
				VPos.CENTER);
		return searchFields;
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
}
