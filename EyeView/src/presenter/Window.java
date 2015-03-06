package presenter;

import java.util.List;

import parser.Slideshow;
import parser.XMLParser;
import parser.Slide;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Window extends Application {

	public static final double xResolution = 960;
	public static final double yResolution = 720;
	public static final int INDEX = 0;
	public static final int HOUSES = 1;
	public static final int LOGIN = 2;
	public static final int REGISTER = 3;
	
	public static Slideshow slideshow;
	public static List<Slide> slideList;
	public static Slide slideData;
	public static String groupID;

	private Group root;
	private int slideID = INDEX;
	private SlideContent sc;

	public void init(Stage primaryStage) {

		/* Runs the XML parser */
		XMLParser parser = new XMLParser();
		slideshow = parser.loadSlideshow("EyeView.xml");
		slideList = slideshow.getSlides();
		groupID = slideshow.getInfo().getGroupID();

		/* Initialises primary stage */
		primaryStage.setTitle(slideshow.getTitle());
		primaryStage.getIcons().add(new Image("file:./resources/icons/xxxhdpi.png"));
		primaryStage.setWidth(xResolution);
		primaryStage.setHeight(yResolution);
		primaryStage.setResizable(false);

		root = new Group();		
		primaryStage.setScene(new Scene(root));
		
		sc = new SlideContent(root);
		loadSlide(INDEX);
	}

	public void loadSlide(int id) {

		root.getChildren().clear();
		slideData = slideList.get(id);

		sc.BuildSlide(id);

		SlideDurationTimer();
	}

	private void SlideDurationTimer() {

		// Add timeline if duration is greater than zero.
		if (slideData.getDuration() > 0) {
			// Instantiate timer and removal task then create timer schedule.
			new Timeline(new KeyFrame(
					Duration.millis(slideData.getDuration() * 1000), ae -> {
						slideID++;
						loadSlide(slideID);
					})).play();
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
