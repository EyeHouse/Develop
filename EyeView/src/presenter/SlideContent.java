package presenter;

import houses.HouseOverview;
import houses.HouseReviews;
import houses.MoreInfo;
import houses.VideoPage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import profile.AccountSettings;
import profile.Login;
import profile.ProfileViewer;
import profile.Register;
import profile.SavedProperties;
import button.ButtonType;
import button.SetupButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
import language.Translator;
import houses.GoogleMapsPage;
import database.Database;
import database.House;
import database.Search;
import database.User;

/**
 * This class contain all the code for creating new slides in the presentation.
 * 
 * @version 3.2 (27.05.15)
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class SlideContent extends Window {

	public ImageView profilePictureView;
	private static ImageView buttonslideBack;
	public static ComboBox<ImageView> languageComboBox;
	public LoadXML loadXML;
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
			buttonBack, searchButton;
	public final ComboBox<Integer> minBeds = new ComboBox<Integer>();
	public final ComboBox<Integer> maxBeds = new ComboBox<Integer>();
	public final ComboBox<Integer> minPrice = new ComboBox<Integer>();
	public final ComboBox<Integer> maxPrice = new ComboBox<Integer>();
	public final ComboBox<Integer> distance = new ComboBox<Integer>();

	public void createSlide() {

		// Load all objects from XML file first
		try {
			if (loadXML != null) {
				LoadXML.stopMedia();
			}
			loadXML = new LoadXML();
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
		originSavedProperties = false;
		originManageProperties = false;
		startPage = new StartPage();
	}

	private void createLoggedOutSlide() {

		originSavedProperties = false;
		originManageProperties = false;
		ArrayList<Integer> houseIDs = new ArrayList<Integer>();
		houseIDs = Database.selectAllHouses();
		ArrayList<House> houses = getDisplayHouses(houseIDs);

		houseAdverts = new HouseOverview(false, houses);
		createSidebar();
		createMenuBar();

		houseIDs.clear();
		houseIDs = null;
		houses = null;
	}

	private void createHomeSlide() {

		originSavedProperties = false;
		originManageProperties = false;
		ArrayList<Integer> houseIDs = new ArrayList<Integer>();
		houseIDs = Database.selectAllHouses();
		ArrayList<House> houses = getDisplayHouses(houseIDs);

		houseAdverts = new HouseOverview(false, houses);
		createSidebar();
		createMenuBar();

		houseIDs.clear();
		houseIDs = null;
	}

	private void createLoginSlide() {

		originSavedProperties = false;
		originManageProperties = false;
		login = new Login();
		createSidebar();
	}

	private void createRegisterSlide() {

		originSavedProperties = false;
		originManageProperties = false;
		register = new Register();
		createSidebar();
	}

	private void createProfileSlide() {

		originSavedProperties = false;
		originManageProperties = false;
		profile = new ProfileViewer(viewedUsername);
		createSidebar();
	}

	private void createAccountSettingsSlide() {

		originSavedProperties = false;
		originManageProperties = false;
		accountSettings = new AccountSettings();
		createSidebar();
	}

	private void createSavedPropertySlide() {

		originSavedProperties = true;
		originManageProperties = false;
		savedProperties = new SavedProperties();
		createSidebar();
	}

	private void createPropertySlide() {

		House house = Database.getHouse(currentPropertyID);
		ArrayList<House> houseArray = new ArrayList<House>();
		houseArray.add(house);

		createMenuBar();
		houseAdverts = new HouseOverview(true, houseArray);
		createSidebar();
	}

	private void createMoreInfoSlide() {

		createMenuBar();
		moreInfo = new MoreInfo();
		createSidebar();

	}

	private void createReviewsSlide() {

		createMenuBar();
		houseReviews = new HouseReviews();
		createSidebar();
	}

	private void createMapSlide() {

		createMenuBar();
		mapPage = new GoogleMapsPage();
		createSidebar();
	}

	private void createVideoSlide() {

		createMenuBar();
		videoPage = new VideoPage();
		createSidebar();
	}

	private void createLandlordPropertiesSlide() {

		originSavedProperties = false;
		originManageProperties = true;
		landlordProperties = new LandlordProperties(currentUsername);
		createSidebar();
	}

	private void createEditPropertySlide() {
		originSavedProperties = false;
		originManageProperties = true;
		editProperty = new EditProperty(0, currentPropertyID);
		createSidebar();
	}

	private void createResultsSlide() {

		originSavedProperties = false;
		originManageProperties = false;
		createMenuBar();
		houseAdverts = new HouseOverview(false, searchResults);
		createSidebar();
	}

	private void createSidebar() {

		VBox sidebar = new VBox(18);

		if (currentUsername != null) {

			User currentUser = Database.getUser(currentUsername);
			try {
				// InputStream binaryStream =
				// currentUser.profimg.getBinaryStream(
				// 1, currentUser.profimg.length());
				Image profilePicture = new Image(
						currentUser.profimg.getBinaryStream());

				profilePictureView = new ImageView(profilePicture);
				// Set maximum dimensions for profile picture
				profilePictureView.setFitWidth(100);
				profilePictureView.setFitHeight(100);
				profilePictureView.setPreserveRatio(true);
				sidebar.getChildren().add(profilePictureView);
				profilePicture = null;
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

			labelProfile = new Label(Translator.translateText(languageIndex,
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

			labelSavedProperties = new Label(Translator.translateText(
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

			labelLandlordProperties = new Label(Translator.translateText(
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
			labelLogOut = new Label(Translator.translateText(languageIndex,
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

			if (originSavedProperties)
				labelSavedProperties.setFont(Font.font(null, FontWeight.BOLD,
						16.5));
			else if (slideID == PROFILE || slideID == ACCOUNTSETTINGS)
				labelProfile.setFont(Font.font(null, FontWeight.BOLD, 16.5));
			else if (originManageProperties)
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

			if (slideID == HOUSES || slideID == RESULTS)
				createSearchBar();
			currentUser = null;
		} else {
			labelLogin = new Label(Translator.translateText(languageIndex,
					"Login"));
			labelLogin.setFont(new Font(16));
			labelLogin.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent arg0) {
					loadSlide(LOGIN);
				}
			});

			labelRegister = new Label(Translator.translateText(languageIndex,
					"Register"));
			labelRegister.setFont(new Font(16));
			labelRegister.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent arg0) {
					loadSlide(REGISTER);
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

	private void createMenuBar() {

		HBox buttonRow = new HBox(5);

		ButtonType button1 = new ButtonType(null, null,
				Translator.translateText(languageIndex, "Video Tour"), 150, 50);
		videoButton = new SetupButton().createButton(button1);
		videoButton.setFocusTraversable(false);
		videoButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				loadSlide(VIDEO);
			}
		});

		ButtonType button2 = new ButtonType(null, null,
				Translator.translateText(languageIndex, "Map"), 150, 50);
		mapButton = new SetupButton().createButton(button2);
		mapButton.setFocusTraversable(false);
		mapButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				loadSlide(MAP);
			}
		});

		ButtonType button3 = new ButtonType(null, null,
				Translator.translateText(languageIndex, "Information"), 150, 50);
		infoButton = new SetupButton().createButton(button3);
		infoButton.setFocusTraversable(false);
		infoButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				loadSlide(MOREINFO);
			}
		});

		ButtonType button4 = new ButtonType(null, null,
				Translator.translateText(languageIndex, "Reviews"), 150, 50);
		reviewsButton = new SetupButton().createButton(button4);
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

		Label blank = new Label("");
		blank.setMinWidth(70);

		buttonRow.relocate(182.2, 0);
		buttonRow.setMinWidth(xResolution - (xResolution * 0.19));
		buttonRow.setMaxWidth(xResolution - (xResolution * 0.19));
		buttonRow.getChildren().addAll(blank, videoButton, mapButton,
				infoButton, reviewsButton, new Label("  "), languageComboBox,
				new Label("   "));
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

		buttonslideBack = new ImageView(new Image(
				"file:resources/advert_icons/back.png"));
		buttonslideBack.relocate(195, 8);
		buttonslideBack.setPreserveRatio(true);
		buttonslideBack.setFitHeight(35);
		buttonslideBack.setCursor(Cursor.HAND);
		buttonslideBack.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent ae) {
				if (currentUsername != null)
					loadSlide(HOUSES);
				else
					loadSlide(INDEX);
				HouseOverview.setTimerState("PAUSE");
			}
		});

		root.getChildren().add(buttonslideBack);
	}

	private void setupTranslate() {

		languageComboBox = new ComboBox<ImageView>();
		Translator.translateBox();
		languageComboBox.valueProperty().addListener(new LanguageChange());
		languageComboBox.getStylesheets().add(
				new File("resources/languageStyle.css").toURI().toString());
	}

	private class LanguageChange implements ChangeListener<ImageView> {

		@Override
		public void changed(ObservableValue<? extends ImageView> arg0,
				ImageView arg1, ImageView arg2) {

			languageIndex = languageComboBox.getSelectionModel()
					.getSelectedIndex();

			switch (slideID) {
			case INDEX:
				updateLoginRegisterLanguage();
				updateMenuBarLanguage();
				houseAdverts.updateLanguage();
				break;
			case LOGIN:
				break;
			case REGISTER:
				break;
			case HOUSES:
				updateSidebarLanguage();
				updateMenuBarLanguage();
				updateSearchBarLanguage();
				houseAdverts.updateLanguage();
				break;
			case PROFILE:
				break;
			case ACCOUNTSETTINGS:
				break;
			case SAVEDPROPERTIES:
				break;
			case HOUSE:
				updateSidebarLanguage();
				updateMenuBarLanguage();
				houseAdverts.updateLanguage();
				break;
			case MOREINFO:
				updateSidebarLanguage();
				updateMenuBarLanguage();
				moreInfo.updateLanguage();
				break;
			case REVIEWS:
				updateSidebarLanguage();
				updateMenuBarLanguage();
				houseReviews.updateLanguage();
				break;
			case MAP:
				updateSidebarLanguage();
				updateMenuBarLanguage();
				mapPage.updateLanguage();
				break;
			case VIDEO:
				updateSidebarLanguage();
				updateMenuBarLanguage();
				videoPage.updateLanguage();
				break;
			case LANDLORDPROPERTIES:
				break;
			case EDITPROPERTY:
				break;
			case RESULTS:
				updateSidebarLanguage();
				updateMenuBarLanguage();
				updateSearchBarLanguage();
				houseAdverts.updateLanguage();
				break;
			default:
				break;
			}
		}
	}

	private void updateLoginRegisterLanguage() {

		labelLogin.setText(Translator.translateText(languageIndex, "Login"));
		labelRegister.setText(Translator.translateText(languageIndex,
				"Register"));
	}

	private void updateMenuBarLanguage() {

		videoButton.setText(Translator.translateText(languageIndex,
				"Video Tour"));
		mapButton.setText(Translator.translateText(languageIndex, "Map"));
		infoButton.setText(Translator.translateText(languageIndex,
				"Information"));
		reviewsButton.setText(Translator
				.translateText(languageIndex, "Reviews"));
	}

	private void updateSidebarLanguage() {

		User currentUser = Database.getUser(currentUsername);

		labelProfile
				.setText(Translator.translateText(languageIndex, "Profile"));
		labelSavedProperties.setText(Translator.translateText(languageIndex,
				"Saved Properties"));
		if (currentUser.landlord)
			labelLandlordProperties.setText(Translator.translateText(
					languageIndex, "My Properties"));
		labelLogOut.setText(Translator.translateText(languageIndex, "Log Out"));

		currentUser = null;
	}

	private void updateSearchBarLanguage() {

		labelFilter.setText(Translator.translateText(languageIndex,
				"Filter results:"));
		labelBedSearch.setText(Translator.translateText(languageIndex,
				"Bedrooms"));
		labelPriceSearch.setText(Translator.translateText(languageIndex,
				"Price"));
		labelDistanceSearch.setText(Translator.translateText(languageIndex,
				"Distance to University") + " (km)");
		searchButton.setText(Translator.translateText(languageIndex, "Search"));
	}

	private ArrayList<House> getDisplayHouses(ArrayList<Integer> houseIDs) {

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

	private void createSearchBar() {

		VBox searchFields = new VBox(0);
		VBox minBedsColumn = new VBox(5);
		VBox maxBedsColumn = new VBox(5);
		VBox minPriceColumn = new VBox(5);
		VBox maxPriceColumn = new VBox(5);
		HBox bedsRow = new HBox(10);
		HBox priceRow = new HBox(10);

		labelFilter = new Label(Translator.translateText(languageIndex,
				"Filter results:"));
		labelFilter.setFont(Font.font(null, FontWeight.BOLD, 14));

		labelBedSearch = new Label(Translator.translateText(languageIndex,
				"Bedrooms"));
		labelBedSearch.setFont(Font.font(null, FontWeight.BOLD, 12));
		labelBedMin = new Label("Min");
		labelBedMax = new Label("Max");

		labelPriceSearch = new Label(Translator.translateText(languageIndex,
				"Price"));
		labelPriceSearch.setFont(Font.font(null, FontWeight.BOLD, 12));
		labelPriceMin = new Label("Min (£)");
		labelPriceMax = new Label("Max (£)");

		labelDistanceSearch = new Label(Translator.translateText(languageIndex,
				"Distance to University") + " (km)");
		labelDistanceSearch.setFont(Font.font(null, FontWeight.BOLD, 12));
		labelDistanceSearch.setMaxWidth(110);
		labelDistanceSearch.setWrapText(true);
		labelDistanceSearch.setTextAlignment(TextAlignment.CENTER);

		// Button setup
		ButtonType button1 = new ButtonType("166,208,255", null,
				Translator.translateText(languageIndex, "Search"), 90, 25);
		searchButton = new SetupButton().createButton(button1);
		searchButton.setOnAction(new searchHandler());

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

		Rectangle centreBox = RectangleBuilder.create().arcWidth(30)
				.arcHeight(30).x(15).y(450).fill(Color.TRANSPARENT)
				.strokeWidth(1).stroke(Color.rgb(33, 51, 76)).build();
		centreBox.setWidth(150);
		centreBox.setHeight(290);

		maxBedsColumn.getChildren().addAll(labelBedMax, maxBeds);
		maxBedsColumn.setAlignment(Pos.CENTER);
		minBedsColumn.getChildren().addAll(labelBedMin, minBeds);
		minBedsColumn.setAlignment(Pos.CENTER);
		bedsRow.getChildren().addAll(minBedsColumn, maxBedsColumn);
		bedsRow.setAlignment(Pos.CENTER);

		maxPriceColumn.getChildren().addAll(labelPriceMax, maxPrice);
		maxPriceColumn.setAlignment(Pos.CENTER);
		minPriceColumn.getChildren().addAll(labelPriceMin, minPrice);
		minPriceColumn.setAlignment(Pos.CENTER);
		priceRow.getChildren().addAll(minPriceColumn, maxPriceColumn);
		priceRow.setAlignment(Pos.CENTER);

		searchFields.getChildren().addAll(labelFilter, labelBedSearch, bedsRow,
				labelPriceSearch, priceRow, labelDistanceSearch, distance,
				searchButton);
		searchFields.relocate(15, 425);
		searchFields.setAlignment(Pos.CENTER);
		searchFields.setMinWidth(150);
		searchFields.setMaxWidth(150);

		VBox.setMargin(labelFilter, new Insets(0, 0, 15, 0));
		VBox.setMargin(bedsRow, new Insets(5, 0, 15, 0));
		VBox.setMargin(priceRow, new Insets(5, 0, 15, 0));
		VBox.setMargin(distance, new Insets(10, 0, 20, 0));

		root.getChildren().addAll(centreBox, searchFields);
	}

	private class searchHandler implements EventHandler<ActionEvent> {

		@Override
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

			result1.clear();
			result2.clear();
			result3.clear();
			result4.clear();
			result5.clear();

			outputBeds.clear();
			outputPrice.clear();
			outputPriceBed.clear();

			outputBeds.trimToSize();
			outputPrice.trimToSize();
			outputPriceBed.trimToSize();

			result1 = null;
			result2 = null;
			result3 = null;
			result4 = null;
			result5 = null;
			outputBeds = null;
			outputPrice = null;
			outputPriceBed = null;

			searchResults = output;
			
			if (output.size() > 0) {
				loadSlide(RESULTS);
			} else {
				createWarningPopup("No matching properties.");
				dialogStage.show();
			}
		}
	}

	private void clearSlideData() {

		switch (prevSlideID) {
		case STARTPAGE:
			startPage.dispose();
			startPage = null;
			break;
		case RESULTS:
		case INDEX:
		case HOUSE:
		case HOUSES:
			languageComboBox.getItems().clear();
			languageComboBox = null;
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
			videoPage.dispose();
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

		profilePictureView = null;
		System.gc();
	}
}
