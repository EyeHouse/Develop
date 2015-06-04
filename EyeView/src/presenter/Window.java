package presenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import parser.SlideData;
import parser.SlideshowData;
import parser.XMLParser;
import profile.Login;
import button.ButtonType;
import button.SetupButton;

import com.memetix.mst.translate.Translate;

import database.Database;
import database.House;

/**
 * This class is the top-level class that contains the methods for running the
 * MultiMedia Presentation Environment, and run the EyeView software.
 * 
 * It contains methods to create the EyeHouse portal page, allowing the user to
 * either select an XML file to import, or to run the EyeView software.
 * 
 * @version 1.4 (15.03.15)
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class Window extends Application {

	// Static final global constants
	public static final double xResolution = 960;
	public static final double yResolution = 800;
	public static final int STARTPAGE = 0;
	public static final int INDEX = 1;
	public static final int HOUSES = 2;
	public static final int LOGIN = 3;
	public static final int REGISTER = 4;
	public static final int VIDEO = 5;
	public static final int PROFILE = 6;
	public static final int ACCOUNTSETTINGS = 7;
	public static final int SAVEDPROPERTIES = 8;
	public static final int HOUSE = 9;
	public static final int MOREINFO = 10;
	public static final int REVIEWS = 11;
	public static final int MAP = 12;
	public static final int LANDLORDPROPERTIES = 13;
	public static final int EDITPROPERTY = 14;
	public static final int RESULTS = 15;

	// Static global variables
	public static SlideshowData slideshow;
	public static List<SlideData> slideList;
	public static SlideData slideData;
	public static String groupID;
	public static int slideID = -1;
	public static int prevSlideID = -1;
	public static boolean originSavedProperties = false;
	public static boolean originManageProperties = false;
	public static String currentUsername = null;
	public static String viewedUsername = null;
	public static int currentPropertyID = 0;
	public static boolean firstLogin = false;
	public static int languageIndex = 0;
	public static ArrayList<House> searchResults = new ArrayList<House>();
	public static Timeline advertTimer;
	public static Stage dialogStage;
	public static Group root;

	// Private static variables
	private static SlideContent sc;
	private static Scene scene;
	private static Timeline genericTimer;

	/**
	 * Initialises the primary stage and sets the window size.
	 * 
	 * @param primaryStage
	 *            The main stage window
	 */
	private void init(Stage primaryStage) {

		// Initialises primary stage
		primaryStage.setWidth(xResolution);
		primaryStage.setHeight(yResolution);
		primaryStage.setResizable(false);
		primaryStage.setTitle("EyeHouse");

		root = new Group();
		scene = new Scene(root);
		primaryStage.setScene(scene);
		dialogStage = new Stage();
		createXMLOptions(primaryStage);
	}

	/**
	 * Loads a new slide by clearing the display and creating the content for
	 * the new slide.
	 * 
	 * @param id
	 *            slideID for the slide to display
	 */
	public static void loadSlide(int id) {

		prevSlideID = slideID;
		slideID = id;

		if (slideID < (slideList.size())) {

			root.getChildren().clear();
			slideData = slideList.get(slideID);

			sc.createSlide();

			// Add timeline if duration is greater than zero.
			if (slideData.getDuration() > 0) {
				final int nextSlide = slideID + 1;
				genericTimer = new Timeline(new KeyFrame(
						Duration.millis(slideData.getDuration() * 1000),
						new EventHandler<ActionEvent>() {
							public void handle(ActionEvent ae) {
								loadSlide(nextSlide);
							}
						}));
				genericTimer.play();
			}
		} else {
			slideID = slideList.size() - 1;
		}

	}

	/**
	 * Creates buttons on the screen to select either importing an XML or
	 * opening the EyeView software.
	 * 
	 * @param primaryStage
	 *            The main stage window
	 */
	private void createXMLOptions(Stage primaryStage) {

		Login.setBackground(false);
		Image companyLogo = new Image(
				"file:resources/start_page_images/StartPageLOGO.png");
		ImageView logo = new ImageView(companyLogo);
		logo.setPreserveRatio(true);
		logo.setFitWidth(300);
		logo.relocate(xResolution / 2 - logo.getFitWidth() / 2, 150);

		Login.setWhiteBox(350, 80, false);

		ButtonType button1 = new ButtonType("166,208,255", null, "Import XML",
				110, 30);
		Button buttonImport = new SetupButton().createButton(button1);
		buttonImport.setCursor(Cursor.HAND);
		buttonImport.setOnAction(new importHandler());

		ButtonType button2 = new ButtonType("166,208,255", null,
				"Open EyeView", 110, 30);
		Button buttonEyeView = new SetupButton().createButton(button2);
		buttonEyeView.setCursor(Cursor.HAND);
		buttonEyeView.setOnAction(new openEyeViewHandler());

		HBox buttons = new HBox(50);
		buttons.getChildren().addAll(buttonImport, buttonEyeView);
		buttons.relocate(xResolution / 2
				- ((buttonEyeView.getMaxWidth()) + buttons.getSpacing() / 2),
				yResolution / 2 - buttonEyeView.getMaxHeight());
		root.getChildren().addAll(buttons, logo);
	}

	/**
	 * Imports and opens the XML file chosen in the FileChooser.
	 * 
	 * @param primaryStage
	 *            The main stage window
	 * @param xmlPath
	 *            File path of the XML file to import
	 */
	private static void openXML(Stage primaryStage, String xmlPath) {

		// Run the XML parser
		XMLParser parser = new XMLParser();
		slideshow = parser.loadSlideshow(xmlPath);
		slideList = slideshow.getSlides();
		groupID = slideshow.getInfo().getGroupID();

		primaryStage.setTitle(slideshow.getTitle());

		if (groupID.matches("5")) {
			primaryStage.getIcons().add(
					new Image("file:./resources/icons/xxxhdpi.png"));
		} else {
			scene.setOnKeyPressed(new arrowKeyEvent());
		}

		sc = new SlideContent();
		loadSlide(STARTPAGE);
	}

	/**
	 * This class handles opening of an external XML slideshow from the EyeHouse
	 * portal screen.
	 * 
	 * @version 1.4 (15.03.15)
	 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
	 */
	private class importHandler implements EventHandler<ActionEvent> {

		public void handle(ActionEvent arg0) {

			File newFile;

			// Open file chooser window
			FileChooser uploadChooser = new FileChooser();
			uploadChooser.setInitialDirectory(new File(System
					.getProperty("user.home")));
			// Set title of file chooser
			uploadChooser.setTitle("Choose XML to Import");
			// Set file types displayed in the file chooser as mp4
			uploadChooser.getExtensionFilters().add(
					new FileChooser.ExtensionFilter("XML", "*.xml"));
			javafx.stage.Window fileChooserStage = null;

			// Replace profile picture with new one from selected file
			newFile = uploadChooser.showOpenDialog(fileChooserStage);
			if (newFile != null) {
				root.getChildren().clear();
				Stage stage = (Stage) root.getScene().getWindow();

				openXML(stage, newFile.getAbsolutePath());
			}
		}
	}

	/**
	 * This class handles opening of the EyeView software from the EyeHouse
	 * portal screen.
	 * 
	 * @version 1.4 (15.03.15)
	 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
	 */
	private class openEyeViewHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			if (Database.dbConnect()) {
				root.getChildren().clear();
				Stage stage = (Stage) root.getScene().getWindow();
				openXML(stage, "EyeView.xml");
				Translate.setClientId("EyeView");
				Translate
						.setClientSecret("Nqi5bXBq6CCreKnvUBy8OlXYveT3NWRlVIoLDidFG0I=");
			} else {
				createWarningPopup("Not connected to EyeHouse server");
				dialogStage.show();
			}

		}
	}

	/**
	 * Creates a new warning stage that appears on top of the main window,
	 * containing a given warning message.
	 * 
	 * @param message
	 *            Warning message string to be displayed
	 */
	public static void createWarningPopup(String message) {

		Label dialogText = new Label(message);
		dialogText.setFont(new Font(14));
		dialogText.setAlignment(Pos.CENTER);
		dialogText.setPrefWidth(200);
		dialogText.setWrapText(true);

		Button okButton = new Button("OK");
		okButton.setPrefSize(80, 30);
		okButton.setFocusTraversable(false);
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				dialogStage.close();
			}
		});

		Image icon = new Image("file:./resources/images/warning.png");
		ImageView iconView = new ImageView(icon);

		dialogStage.setResizable(false);
		dialogStage.setTitle("Warning");
		dialogStage.getIcons().add(
				new Image("file:./resources/icons/xxxhdpi.png"));
		dialogStage.setWidth(300);
		dialogStage.setHeight(150);
		HBox hbox = new HBox(10);
		hbox.setSpacing(5);
		hbox.setAlignment(Pos.CENTER);
		hbox.getChildren().addAll(iconView, dialogText);

		dialogStage.setScene(new Scene(VBoxBuilder.create()
				.children(hbox, okButton).alignment(Pos.CENTER).spacing(5)
				.build()));
	}

	/**
	 * This class implements the event handler for switching between XML slides
	 * with the left and right arrow keys.
	 * 
	 * @version 1.4 (15.03.15)
	 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
	 */
	private static class arrowKeyEvent implements EventHandler<KeyEvent> {

		@Override
		public void handle(KeyEvent input) {
			if (input.getCode() == KeyCode.RIGHT) {
				int newSlide = slideID + 1;

				if (slideData.getDuration() > 0) {
					genericTimer.stop();
				}

				loadSlide(newSlide);
			} else if (input.getCode() == KeyCode.LEFT) {
				int newSlide = slideID - 1;

				if (slideData.getDuration() > 0) {
					genericTimer.stop();
				}
				if (slideID == 0) {
					loadSlide(0);
				} else {
					loadSlide(newSlide);
				}
			}
		}

	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		init(primaryStage);
		primaryStage.show();
	}

	/**
	 * Main executable method which launches Application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
