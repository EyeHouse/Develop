package presenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import Button.ButtonType;
import Button.SetupButton;
import Profile.Login;

import database.Database;
import database.House;

import parser.XMLParser;
import parser.SlideshowData;
import parser.SlideData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Window extends Application {

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

	public static SlideshowData slideshow;
	public static List<SlideData> slideList;
	public static SlideData slideData;
	public static String groupID;
	public static int slideID;
	public static int prevSlideID = STARTPAGE;

	public static String currentUsername = null;
	public static String viewedUsername = null;
	public static int currentPropertyID = 0;
	public static boolean firstLogin = false;
	public static int languageIndex = 0;
	public static ArrayList<House> searchResults = new ArrayList<House>();

	public static Stage dialogStage;
	public static Group root;
	private static SlideContent sc;
	public static Timeline advertTimer;

	public void init(Stage primaryStage) {

		// Initialises primary stage
		primaryStage.setWidth(xResolution);
		primaryStage.setHeight(yResolution);
		primaryStage.setResizable(false);
		primaryStage.setTitle("EyeHouse");
		
		root = new Group();
		primaryStage.setScene(new Scene(root));

		createXMLOptions(primaryStage);
	}

	public static void loadSlide(int id) {

		slideID = id;
		if (slideID < (slideList.size())) {

			root.getChildren().clear();
			slideData = slideList.get(slideID);

			sc.createSlide();

			// Add timeline if duration is greater than zero.
			if (slideData.getDuration() > 0) {
				slideID++;
				new Timeline(new KeyFrame(Duration.millis(slideData
						.getDuration() * 1000),
						new EventHandler<ActionEvent>() {
							public void handle(ActionEvent ae) {
								loadSlide(slideID);
							}
						})).play();
			}
		} else {
			System.out.println("Slideshow Finished!");
		}

	}

	public void createXMLOptions(Stage primaryStage) {

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
		Button buttonImport = new SetupButton().CreateButton(button1);
		buttonImport.setCursor(Cursor.HAND);
		buttonImport.setOnAction(new importHandler());

		ButtonType button2 = new ButtonType("166,208,255", null,
				"Open EyeView", 110, 30);
		Button buttonEyeView = new SetupButton().CreateButton(button2);
		buttonEyeView.setCursor(Cursor.HAND);
		buttonEyeView.setOnAction(new openEyeViewHandler());

		HBox buttons = new HBox(50);
		buttons.getChildren().addAll(buttonImport, buttonEyeView);
		buttons.relocate(xResolution / 2
				- ((buttonEyeView.getMaxWidth()) + buttons.getSpacing() / 2),
				yResolution / 2 - buttonEyeView.getMaxHeight());
		root.getChildren().addAll(buttons, logo);
	}

	public static void openXML(Stage primaryStage, String xmlPath) {

		// Run the XML parser
		XMLParser parser = new XMLParser();
		slideshow = parser.loadSlideshow(xmlPath);
		slideList = slideshow.getSlides();
		groupID = slideshow.getInfo().getGroupID();

		primaryStage.setTitle(slideshow.getTitle());

		if (groupID.matches("5")) {
			dialogStage = new Stage();
			primaryStage.getIcons().add(
					new Image("file:./resources/icons/xxxhdpi.png"));
			Database.dbConnect();
		}

		sc = new SlideContent();
		loadSlide(STARTPAGE);
	}

	public class importHandler implements EventHandler<ActionEvent> {

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

	public class openEyeViewHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			root.getChildren().clear();
			Stage stage = (Stage) root.getScene().getWindow();
			openXML(stage, "EyeView.xml");
		}
	}

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

	@Override
	public void start(Stage primaryStage) throws Exception {

		init(primaryStage);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
