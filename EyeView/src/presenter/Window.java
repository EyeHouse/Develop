package presenter;

import java.util.List;

import parser.Graphic;
import parser.Slideshow;
import parser.XMLParser;
import parser.Slide;
//import presenter.GraphicHandler.GraphicElement;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Window extends Application {
	
	public enum SlideName {
		LOGGEDOUT, HOME, LOGIN, REGISTER
	}
	private Group root;
	private static final double xResolution = 960;
	private static final double yResolution = 720;
	private Slideshow slideshow;
	private List<Slide> slideList;
	private Slide currentSlide;
	private int slideIndex = 1;

	public void init(Stage primaryStage) {
		
		/* Runs the XML parser */
		XMLParser parser = new XMLParser();
		slideshow = parser.loadSlideshow("EyeView.xml");
		slideList = slideshow.getSlides();
		
		/* Initialises primary stage */
		primaryStage.setTitle(slideshow.getTitle());
		primaryStage.getIcons().add(new Image("file:./resources/icons/xxxhdpi.png"));
        primaryStage.setWidth(xResolution);
        primaryStage.setHeight(yResolution);
        primaryStage.setResizable(false);
        
        root = new Group();
        loadSlide(slideIndex);
        
        /*GraphicHandler gh = new GraphicHandler(root, xResolution, yResolution);
        GraphicElement graphic = gh.new GraphicElement("oval", 0.25f, 0.25f, 0.75f, 0.75f, true, 0, "#FFFF0000", "#00000000");*/
        
	    primaryStage.setScene(new Scene(root));
              
	}
	
	public void loadSlide(int id) {
		
		root.getChildren().clear();
		currentSlide = slideList.get(id);
		
		//GraphicHandler gh = new GraphicHandler(root, xResolution, yResolution);
		/*List<Graphic> graphicList = slideList.get(id).getGraphicList();
		for (Graphic currentGraphic : graphicList) {
			GraphicElement graphic = gh.new GraphicElement(currentGraphic.getType(),
					currentGraphic.getXstart(),
					currentGraphic.getYstart(),
					currentGraphic.getXend(),
					currentGraphic.getYend(),
					currentGraphic.isSolid(),
					currentGraphic.getDuration(),
					currentGraphic.getGraphicColor(),
					currentGraphic.getGraphicColor());
		}*/
		
		
		//ImageHandler ih = new ImageHandler();
		/* something else here */
		//slide.getChildren().add(img);
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
