package presenter;

import java.util.List;

import parser.Graphic;
import parser.Slideshow;
import parser.XMLParser;
import parser.Slide;
import presenter.GraphicHandler.GraphicElement;
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
	
	private static final double xResolution = 400;
	private static final double yResolution = 400;
	private static final int graphicSlide = 1;
	
	private Group root;
	private Slideshow slideshow;
	private List<Slide> slideList;
	private Slide currentSlide;
	private int slideIndex = graphicSlide;

	public void init(Stage primaryStage) {
		
		/* Runs the XML parser */
		XMLParser parser = new XMLParser();
		slideshow = parser.loadSlideshow("EyeView.xml");
		parser.printLists();
		slideList = slideshow.getSlides();
		
		/* Initialises primary stage */
		primaryStage.setTitle(slideshow.getTitle());
		primaryStage.getIcons().add(new Image("file:./resources/icons/xxxhdpi.png"));
        primaryStage.setWidth(xResolution);
        primaryStage.setHeight(yResolution);
        primaryStage.setResizable(false);
        
        root = new Group();
        loadSlide(slideIndex);
        
	    primaryStage.setScene(new Scene(root));
              
	}
	
	public void loadSlide(int id) {
		
		root.getChildren().clear();
		currentSlide = slideList.get(id);
		switch(id){
		case graphicSlide:
			LoadGraphics();
			break;
		default:
			break;
		}
		
		//ImageHandler ih = new ImageHandler();
		/* something else here */
		//slide.getChildren().add(img);
	}
	
	public void LoadGraphics(){
		GraphicHandler gh = new GraphicHandler(root, xResolution, yResolution);
		List<Graphic> graphicList = slideList.get(graphicSlide).getGraphicList();

		for (Graphic currentGraphic : graphicList) {
			GraphicElement graphic = gh.new GraphicElement(currentGraphic.getType(),
					currentGraphic.getXstart(),
					currentGraphic.getYstart(),
					currentGraphic.getXend(),
					currentGraphic.getYend(),
					currentGraphic.getDuration(),
					currentGraphic.isSolid(),
					currentGraphic.getGraphicColor(),
					currentGraphic.getShadingColor());
			gh.AddShapeToCanvas(graphic);
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
