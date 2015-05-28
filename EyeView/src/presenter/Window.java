package presenter;

import java.util.ArrayList;
import java.util.List;

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
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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

	private void init(Stage primaryStage) {

		/* Runs the XML parser */
		XMLParser parser = new XMLParser();
		slideshow = parser.loadSlideshow("EyeView.xml");
		slideList = slideshow.getSlides();
		groupID = slideshow.getInfo().getGroupID();
		primaryStage.setTitle(slideshow.getTitle());
		/* Initialises primary stage */
		
		primaryStage.setWidth(xResolution);
		primaryStage.setHeight(yResolution);
		primaryStage.setResizable(false);

		dialogStage = new Stage();
		root = new Group();
		primaryStage.setScene(new Scene(root));
		if (groupID.matches("5")) {
			primaryStage.getIcons().add(
					new Image("file:./resources/icons/xxxhdpi.png"));
			Database.dbConnect();
		}
		
		
		sc = new SlideContent();
		loadSlide(STARTPAGE);
		// Database.getRating(property.id);
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
				new Timeline(new KeyFrame(Duration.millis(slideData.getDuration() * 1000),
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
	
	public static void createWarningPopup(String message) { 
		 
 		Text dialogText = new Text(message); 
 		dialogText.setFont(new Font(14)); 
 		 
 		Button okButton = new Button("OK"); 
 		okButton.setPrefSize(80, 30); 
 		okButton.setOnAction(new EventHandler<ActionEvent>() { 
 			public void handle(ActionEvent event) { 
 				dialogStage.close(); 
 			} 
 		}); 
 		 
 		Image icon = new Image("file:./resources/images/warning.png");
		ImageView iconView = new ImageView(icon);
		
		
 		dialogStage.setResizable(false); 
 		dialogStage.getIcons().add(new Image("file:./resources/icons/xxxhdpi.png")); 
 		dialogStage.setWidth(300); 
 		dialogStage.setHeight(150); 
 		HBox hbox = new HBox(10);
 		hbox.setAlignment(Pos.CENTER);
 		hbox.getChildren().addAll(iconView,dialogText);
 		
 		dialogStage.setScene(new Scene(VBoxBuilder.create() 
 				.children(hbox, okButton) 
 				.alignment(Pos.CENTER) 
 				.spacing(5) 
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

