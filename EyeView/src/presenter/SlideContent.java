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
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
import database.User;

public class SlideContent extends Window {

	public ImageView profilePictureView;
	public static ComboBox<ImageView> languageComboBox = new ComboBox<ImageView>();
	public static HouseOverview houseAdverts;
	public static Label labelLogin, labelRegister, labelLogOut, labelProfile,
			labelSavedProperties, labelLandlordProperties;
	public static Button videoButton, mapButton, infoButton, reviewsButton,
			buttonBack;

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
			default:
				break;
			}
		}
	}

	private void createStartSlide(){
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

		House test = Database.getHouse(currentPropertyID);
		ArrayList<House> testArray = new ArrayList<House>();
		testArray.add(test);

		new HouseOverview(true, testArray);
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

			labelSavedProperties = new Label(Translate.translateText(languageIndex,
					"Saved Properties"));
			labelSavedProperties.setFont(new Font(16));
			labelSavedProperties.setMaxWidth(150);
			labelSavedProperties.setWrapText(true);
			labelSavedProperties
					.setOnMouseClicked(new EventHandler<MouseEvent>() {
						public void handle(MouseEvent arg0) {
							loadSlide(SAVEDPROPERTIES);
						}
					});
			
			labelLandlordProperties = new Label(Translate.translateText(languageIndex,
					"My Properties"));
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
			if(currentUser.landlord)sidebar.getChildren().add(labelLandlordProperties);
			sidebar.getChildren().add(labelLogOut);
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

		ImageView buttonBack = new ImageView(new Image("file:resources/advert_icons/back.png"));
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
	
	public ArrayList<House> getDisplayHouses(ArrayList<Integer> houseIDs){
		
		ArrayList<House> houses = new ArrayList<House>();
		
		if(houseIDs.size()<10){
			for(int i  = 0 ; i < houseIDs.size() ; i++){
				houses.add(Database.getHouse(houseIDs.get(i)));
			}
		}
		else{
			for(int i = 0 ; i < 10 ; i++){
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
		
		/* Create warning message*/
		ImageView iconView = new ImageView(icon);
		iconView.relocate(15, 15);
		Label warning = new Label("Login for full features.");
		warning.setFont(Font.font(null, 17));	
		warning.relocate(75, 30);
		root.getChildren().addAll(iconView, warning);
				
		warningStage.setScene(new Scene(root));
		warningStage.show();
	}
}
