package presenter;

import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout.Alignment;

import parser.GraphicData;
import parser.ImageData;
import presenter.GraphicElement;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

public class SlideContent extends Window {

	ArrayList<Image> houseImages = new ArrayList<Image>();

	public SlideContent() {
	}

	public void createSlide() {

		// Load all objects from XML file first
		loadXMLGraphics();

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
				
		ImageView image = new ImageView(new Image("file:./resources/images/buckingham-palace.jpg"));
		image.relocate(220, 140);
		image.setFitWidth(400);
		image.setFitHeight(300);
				
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
		
		root.getChildren().add(image);
		root.getChildren().add(loginbutton);
		root.getChildren().add(registerbutton);
		/*root.getChildren().add(videobutton);
		root.getChildren().add(mapbutton);
		root.getChildren().add(infobutton);
		root.getChildren().add(reviewsbutton);*/
		
	}

	private void createHomeSlide() {
        
		createHousePagination();
		
		ImageView profileimage = new ImageView(new Image("file:./resources/images/profile.png"));
		profileimage.relocate(15, 15);
		profileimage.setFitWidth(60);
		profileimage.setFitHeight(60);
				

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
		videobutton.relocate(386, 0);
		videobutton.setPrefSize(140, 40);
		videobutton.setFocusTraversable(false);
		videobutton.setStyle("-fx-base: #90abc7");
		buttonRow.getChildren().add(videobutton);
		
		Button mapbutton= new Button("Map");
		mapbutton.relocate(528, 0);
		mapbutton.setPrefSize(140, 40);
		mapbutton.setFocusTraversable(false);
		mapbutton.setStyle("-fx-base: #90abc7");
		buttonRow.getChildren().add(mapbutton);
	
		Button infobutton = new Button("Information");
		infobutton.relocate(670, 0);
		infobutton.setPrefSize(140, 40);
		infobutton.setFocusTraversable(false);
		infobutton.setStyle("-fx-base: #90abc7");
		buttonRow.getChildren().add(infobutton);
		
		Button reviewsbutton = new Button("Reviews");
		reviewsbutton.relocate(812, 0);
		reviewsbutton.setPrefSize(140, 40);
		reviewsbutton.setFocusTraversable(false);
		reviewsbutton.setStyle("-fx-base: #90abc7");
		buttonRow.getChildren().add(reviewsbutton);
		
		buttonRow.relocate(350, 0);
		buttonRow.setSpacing(5);
		root.getChildren().add(buttonRow);
		
		root.getChildren().add(profileimage);
		root.getChildren().add(label);
		root.getChildren().add(logoutbutton);
		/*root.getChildren().add(videobutton);
		root.getChildren().add(mapbutton);
		root.getChildren().add(infobutton);
		root.getChildren().add(reviewsbutton);*/
	}

	private void createLoginSlide() {
		
		Label label = new Label("INCLUDE LOGIN SLIDE");
		label.relocate(400, 300);
		
		Button loginbutton = new Button ("Log in");
		loginbutton.relocate(480, 330);
		loginbutton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				loadSlide(HOUSES);
			}
		});
		
		Button backbutton = new Button ("Back");
		backbutton.relocate(15, 15);
		backbutton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				loadSlide(INDEX);
			}
		});
		
		root.getChildren().add(label);
		root.getChildren().add(backbutton);
		root.getChildren().add(loginbutton);
	}
	
	private void createRegisterSlide() {
		
		Label label = new Label("INCLUDE REGISTER SLIDE");
		label.relocate(390, 300);
		
		
		Button backbutton = new Button ("Back");
		backbutton.relocate(15, 15);
		backbutton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				loadSlide(INDEX);
			}
		});
		
		root.getChildren().add(label);
		root.getChildren().add(backbutton);
	}
	
	private void createHousePagination() {
		
		houseImages.clear();
		houseImages.add(new Image("file:./resources/images/buckingham-palace.jpg", 400, 300, false, false));
        houseImages.add(new Image("file:./resources/images/palace1.jpg", 400, 300, false, false));
        houseImages.add(new Image("file:./resources/images/palace2.jpg", 400, 300, false, false));
        houseImages.add(new Image("file:./resources/images/palace3.jpg", 400, 300, false, false));
        houseImages.add(new Image("file:./resources/images/palace4.jpg", 400, 300, false, false));
		
        Pagination pagination = new Pagination(houseImages.size(), 0);
        pagination.setPageFactory(new Callback<Integer, Node>() {           
            public Node call(Integer houseIndex) {
            	return createHousePage(houseIndex);
            }
        });
        pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
        pagination.relocate(195, 80);
        root.getChildren().add(pagination);
	}

	protected Node createHousePage(Integer pageIndex) {
		
	    StackPane housePane = new StackPane();
	    housePane.setPrefSize(750, 550);
	    
	    HBox box = new HBox();
	    ImageView iv = new ImageView(houseImages.get(pageIndex));
        Label desc = new Label("Random Text Goes Here!!!");
        desc.setContentDisplay(ContentDisplay.CENTER);
        box.setSpacing(10);
        box.setTranslateX(40);
        box.setTranslateY(80);
	    box.getChildren().addAll(iv, desc);
        housePane.getChildren().add(box);
        
        return housePane;        
	}
}
