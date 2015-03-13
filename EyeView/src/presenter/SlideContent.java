package presenter;

import java.util.ArrayList;
import java.util.List;

import Profile.Login;
import Profile.Register;
import parser.GraphicData;
import parser.ImageData;
import presenter.GraphicElement;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

public class SlideContent extends Window {

	private List<GraphicData> graphicList;
	private List<ImageData> imageList;
	private ArrayList<Image> galleryList1, galleryList2, galleryList3;
	private ImageGallery gallery, gallery1, gallery2, gallery3;

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
		graphicList = slideData.getGraphicList();

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
		imageList = slideData.getImageList();
		
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
				
		createHousePagination();
				
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
		/*root.getChildren().add(videobutton);
		root.getChildren().add(mapbutton);
		root.getChildren().add(infobutton);
		root.getChildren().add(reviewsbutton);*/
		
	}

	private void createHomeSlide() {
        
		createHousePagination();				

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
	
	private void createHousePagination() {
		
		galleryList1 = new ArrayList<Image>();
		galleryList2 = new ArrayList<Image>();
		galleryList3 = new ArrayList<Image>();
		
		for (int i = 1; i < 16; i++) {
			galleryList1.add(new Image("file:./resources/houses/Modern-A-House-" + i + ".jpg", false));
		}
		
		for (int i = 1; i < 8; i++) {
			galleryList2.add(new Image("file:./resources/houses/Empty-Nester-" + i + ".jpg", false));
		}
		
		for (int i = 1; i < 9; i++) {
			galleryList3.add(new Image("file:./resources/houses/modern-apartment-" + i + ".jpg", false));
		}
		
        Pagination pagination = new Pagination(3, 0);
        pagination.setPageFactory(new Callback<Integer, Node>() {
            public Node call(Integer pageIndex) {
            	return createHousePage(pageIndex);
            }
        });
        pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
        pagination.relocate(195, 80);
        root.getChildren().add(pagination);
	}

	protected Pane createHousePage(Integer pageIndex) {
		
	    Pane housePane = new Pane();
	    housePane.setPrefSize(750, 550);
	    
	    switch (pageIndex) {
	    case 0:
		    housePane.getChildren().clear();
		    gallery1 = new ImageGallery(galleryList1, 40, 80);
	    	housePane.getChildren().add(gallery1.getGallery());
    		break;
	    case 1:
		    housePane.getChildren().clear();
			gallery2 = new ImageGallery(galleryList2, 40, 80);
	    	housePane.getChildren().add(gallery2.getGallery());
    		break;
	    case 2:
		    housePane.getChildren().clear();
			gallery3 = new ImageGallery(galleryList3, 40, 80);
	    	housePane.getChildren().add(gallery3.getGallery());
    		break;
    	default:
    		break;
	    }
	    
        Label desc = new Label("Random Text Goes Here!!!");
        desc.setContentDisplay(ContentDisplay.CENTER);
        
        housePane.getChildren().add(desc);
        
        return housePane;        
	}
}
