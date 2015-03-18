package presenter;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import parser.GraphicData;
import parser.ImageData;
import Profile.Login;
import Profile.Register;

public class SlideContent extends Window {

	public SlideContent() {
	}

	public void createSlide() {

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
			ImageElement image = new ImageElement(
					currentImage.getSource(), currentImage.getXstart(),
					currentImage.getYstart(), currentImage.getScale(),
					currentImage.getDuration(), currentImage.getStarttime(),
					200);
			ih.createImage(image);
		}
	}

	/* These methods hold the hard-coded information for certain slides */

	private void createLoggedOutSlide() {
				
		new HousePages();
		
		Button loginbutton = new Button("Login");
		loginbutton.relocate(20, 100);
		loginbutton.setPrefSize(140, 30);
		loginbutton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				loadSlide(LOGIN);
			}
		});
		loginbutton.setFocusTraversable(false);
		
				
		Button registerbutton = new Button("Register");
		registerbutton.relocate(20,140);
		registerbutton.setPrefSize(140, 30);
		registerbutton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				loadSlide(REGISTER);
			}
		});
		registerbutton.setFocusTraversable(false);
		
		HBox buttonRow = new HBox();
		
		Button videobutton = new Button("Video Tour");
		videobutton.relocate(386, 0);
		videobutton.setPrefSize(140, 40);
		videobutton.setFocusTraversable(false);
		videobutton.setDisable(true);
		videobutton.setStyle("-fx-base: #90abc7");
		buttonRow.getChildren().add(videobutton);
		
		Button mapbutton= new Button("Map");
		mapbutton.relocate(528, 0);
		mapbutton.setPrefSize(140, 40);
		mapbutton.setFocusTraversable(false);
		mapbutton.setDisable(true);
		mapbutton.setStyle("-fx-base: #90abc7");
		buttonRow.getChildren().add(mapbutton);
	
		Button infobutton = new Button("Information");
		infobutton.relocate(670, 0);
		infobutton.setPrefSize(140, 40);
		infobutton.setFocusTraversable(false);
		infobutton.setDisable(true);
		infobutton.setStyle("-fx-base: #90abc7");
		buttonRow.getChildren().add(infobutton);
		
		Button reviewsbutton = new Button("Reviews");
		reviewsbutton.relocate(812, 0);
		reviewsbutton.setPrefSize(140, 40);
		reviewsbutton.setFocusTraversable(false);
		reviewsbutton.setDisable(true);
		reviewsbutton.setStyle("-fx-base: #90abc7");
		buttonRow.getChildren().add(reviewsbutton);
		
		buttonRow.relocate(350, 0);
		buttonRow.setSpacing(5);
		root.getChildren().add(buttonRow);
		
		root.getChildren().add(loginbutton);
		root.getChildren().add(registerbutton);
		
	}

	private void createHomeSlide() {
        
		new HousePages();

		Label label = new Label("Jake");
		label.setTextAlignment(TextAlignment.CENTER);
		label.relocate(80, 35);
		label.setWrapText(true);
		label.setFont(new Font(20));
		label.setTextFill(Color.web("#162252FF"));
		label.setContentDisplay(ContentDisplay.CENTER);
		
		
		Button logoutbutton = new Button ("Log out");
		logoutbutton.relocate(20, 100);
		logoutbutton.resize(140, 30);
		logoutbutton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				loadSlide(INDEX);
			}
		});
				
		HBox buttonRow = new HBox();
		
		Button videobutton = new Button("Video Tour");
		videobutton.setPrefSize(140, 40);
		videobutton.setFocusTraversable(false);
		videobutton.setStyle("-fx-base: #90abc7");
		buttonRow.getChildren().add(videobutton);
		
		Button mapbutton= new Button("Map");
		mapbutton.setFocusTraversable(false);
		mapbutton.setStyle("-fx-base: #90abc7");
		buttonRow.getChildren().add(mapbutton);
	
		Button infobutton = new Button("Information");
		infobutton.setPrefSize(140, 40);
		infobutton.setFocusTraversable(false);
		infobutton.setStyle("-fx-base: #90abc7");
		buttonRow.getChildren().add(infobutton);
		
		Button reviewsbutton = new Button("Reviews");
		reviewsbutton.setPrefSize(140, 40);
		reviewsbutton.setFocusTraversable(false);
		reviewsbutton.setStyle("-fx-base: #90abc7");
		buttonRow.getChildren().add(reviewsbutton);
		
		buttonRow.relocate(350, 0);
		buttonRow.setSpacing(5);
		root.getChildren().add(buttonRow);
		
		root.getChildren().add(label);
		root.getChildren().add(logoutbutton);
		/*root.getChildren().add(videobutton);
		root.getChildren().add(mapbutton);
		root.getChildren().add(infobutton);
		root.getChildren().add(reviewsbutton);*/
	}

	private void createLoginSlide() {
		
		new Login();
	}
	
	private void createRegisterSlide() {
		
		new Register();
	}
	
}
