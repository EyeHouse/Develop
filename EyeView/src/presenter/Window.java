package presenter;

import java.util.List;

import database.Database;

import parser.XMLParser;
import parser.SlideshowData;
import parser.SlideData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Window extends Application {

	public static final double xResolution = 960;
	public static final double yResolution = 800;
	public static final int INDEX = 0;
	public static final int HOUSES = 1;
	public static final int LOGIN = 2;
	public static final int REGISTER = 3;
	public static final int PROFILE = 4;
	public static final int ACCOUNTSETTINGS = 5;
	public static final int SAVEDPROPERTIES = 6;
	public static final int HOUSE = 7;

	public static SlideshowData slideshow;
	public static List<SlideData> slideList;
	public static SlideData slideData;
	public static String groupID;
	public static int slideID;

	public static String currentUsername = null;
	public static int currentPropertyID = 0;
	public static boolean firstLogin = false;
	
	public static Group root;
	private static SlideContent sc;
	public static Timeline advertTimer;

	private void init(Stage primaryStage) {

		/* Runs the XML parser */
		XMLParser parser = new XMLParser();
		slideshow = parser.loadSlideshow("EyeView.xml");
		slideList = slideshow.getSlides();
		groupID = slideshow.getInfo().getGroupID();

		/* Initialises primary stage */
		primaryStage.setTitle(slideshow.getTitle());
		primaryStage.getIcons().add(
				new Image("file:./resources/icons/xxxhdpi.png"));
		primaryStage.setWidth(xResolution);
		primaryStage.setHeight(yResolution);
		primaryStage.setResizable(false);

		root = new Group();
		primaryStage.setScene(new Scene(root));
		
		Database.dbConnect();
		
		sc = new SlideContent();
		loadSlide(INDEX);
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

	@Override
	public void start(Stage primaryStage) throws Exception {

		init(primaryStage);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
