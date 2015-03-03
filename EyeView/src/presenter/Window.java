package presenter;

import java.util.List;

import parser.Slideshow;
import parser.XMLParser;
import parser.Slide;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Window extends Application {
	
	private Slideshow slideshow;
	private List<Slide> slideList;
	private SlideContent sc;

	public void init(Stage primaryStage) {
		
		XMLParser parser = new XMLParser();
		slideshow = parser.loadSlideshow("Example PWS XML.xml");
		slideList = slideshow.getSlides();
		/* Initialises primary stage with an empty group */
		
		primaryStage.setTitle(slideshow.getTitle());
        primaryStage.setWidth(960);
        primaryStage.setHeight(720);
        primaryStage.setResizable(false);
        
        sc = new SlideContent();
        for (Slide currentSlide : slideList) {
        	if (currentSlide.getTitle().equals("First Slide")) {
	        	primaryStage.setScene(new Scene(sc.loadSlide(currentSlide.getTitle())));
        	}
        	System.out.println(currentSlide.getTitle());
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
