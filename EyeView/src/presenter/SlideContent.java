package presenter;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import landlord.EditProperty;
import landlord.LandlordProperties;
import language.Translate;
import maps.GoogleMapsPage;
import database.Database;
import database.House;
import database.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import parser.GraphicData;
import parser.ImageData;
import Button.ButtonType;
import Button.SetupButton;
import Profile.AccountSettings;
import Profile.Login;
import Profile.ProfileViewer;
import Profile.Register;
import Profile.SavedProperties;

public class SlideContent extends Window {

	public ImageView profilePictureView;
	public static ComboBox<ImageView> languageComboBox = new ComboBox<ImageView>();
	public static HousePages houseAdverts;
	public static Label labelLogin, labelRegister, labelLogOut, labelProfile,
			labelSavedProperties, labelLandlordProperties;
	public static Button videoButton, mapButton, infoButton, reviewsButton,
			buttonBack;

	public void createSlide() {

		// Cancel Advert timer if it is running
		if (advertTimer != null)
			advertTimer.stop();

		// Load all objects from XML file first
		loadXMLGraphics();
		loadXMLImages();

		// Checks if the XML belongs to group 5 (EyeHouse)
		if (groupID.matches("5")) {
			// Loads objects not within the XML for a given slide
			switch (slideID) {
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

	public void loadXMLGraphics() {

		GraphicHandler gh = new GraphicHandler();
		List<GraphicData> graphicList = slideData.getGraphicList();

		for (GraphicData currentGraphic : graphicList) {
			GraphicElement graphic = new GraphicElement(
					currentGraphic.getType(), currentGraphic.getXstart(),
					currentGraphic.getYstart(), currentGraphic.getXend(),
					currentGraphic.getYend(), currentGraphic.getDuration(),
					currentGraphic.isSolid(), currentGraphic.getGraphicColor(),
					currentGraphic.getShadingColor());
			gh.addShapeToCanvas(graphic);
		}
	}

	public void loadXMLImages() {

		ImageHandler ih = new ImageHandler();
		List<ImageData> imageList = slideData.getImageList();

		for (ImageData currentImage : imageList) {
			ImageElement image = new ImageElement(currentImage.getSource(),
					currentImage.getXstart(), currentImage.getYstart(),
					currentImage.getScale(), currentImage.getDuration(),
					currentImage.getStarttime(), 200);
			ih.createImage(image);
		}
	}

	private void createLoggedOutSlide() {

		House test = Database.getHouse(8);
		ArrayList<House> testArray = new ArrayList<House>();
		testArray.add(test);
		test = Database.getHouse(10);
		testArray.add(test);

		houseAdverts = new HousePages(false, testArray);
		createSidebar();
		createMenuBar();
	}

	private void createHomeSlide() {

		House test = Database.getHouse(8);
		ArrayList<House> testArray = new ArrayList<House>();
		testArray.add(test);
		test = Database.getHouse(10);
		testArray.add(test);

		houseAdverts = new HousePages(false, testArray);
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

		new ProfileViewer(currentUsername);
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

		new HousePages(true, testArray);
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

		if (slideID != INDEX && slideID != LOGIN && slideID != REGISTER) {

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
					root.getChildren().clear();
					slideID = PROFILE;
					createSlide();
				}
			});

			labelSavedProperties = new Label(Translate.translateText(languageIndex,
					"Saved Properties"));
			labelSavedProperties.setFont(new Font(16));
			labelSavedProperties
					.setOnMouseClicked(new EventHandler<MouseEvent>() {
						public void handle(MouseEvent arg0) {
							root.getChildren().clear();
							slideID = SAVEDPROPERTIES;
							createSlide();
						}
					});
			labelLandlordProperties = new Label(Translate.translateText(languageIndex,
					"My Properties"));
			labelLandlordProperties.setFont(new Font(16));
			labelLandlordProperties
					.setOnMouseClicked(new EventHandler<MouseEvent>() {
						public void handle(MouseEvent arg0) {
							root.getChildren().clear();
							slideID = LANDLORDPROPERTIES;
							createSlide();
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
					labelSavedProperties, labelLandlordProperties, labelLogOut);
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
					JOptionPane.showMessageDialog(null,
							"Login for full features.", "",
							JOptionPane.PLAIN_MESSAGE);
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

		ButtonType button1 = new ButtonType("150,150,150", null, "Back", 100,
				30);
		Button buttonBack = new SetupButton().CreateButton(button1);
		buttonBack.relocate(195, 20);

		buttonBack.setCursor(Cursor.HAND);
		buttonBack.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				if (currentUsername != null)
					loadSlide(HOUSES);
				else
					loadSlide(INDEX);
				HousePages.setTimerState("PAUSE");
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
				/*houseAdverts.desc.setText(Translate.translateText(
						languageIndex, "What a lovely house!"));*/
				
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
}
