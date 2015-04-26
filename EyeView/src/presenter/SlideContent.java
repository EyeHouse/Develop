package presenter;

import java.util.List;

import javax.swing.JOptionPane;

import maps.GoogleMapsPage;
import database.Database;
import database.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import parser.GraphicData;
import parser.ImageData;
import Button.ButtonType;
import Button.SetupButton;
import Profile.AccountSettings;
import Profile.Login;
import Profile.ProfileViewer;
import Profile.Register;
import Profile.SavedProperties;

public class SlideContent extends Window {

	public void createSlide() {

		//Cancel Advert timer if it is running
		if(advertTimer != null)advertTimer.stop();
		
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
			case PROFILE:
				createProfileSlide();
				break;
			case ACCOUNTSETTINGS:
				createAccountSettingsSlide();
				break;
			case SAVEDPROPERTIES:
				createSavedPropertySlide();
				break;
			case HOUSE:
				createPropertySlide();
				break;
			case MOREINFO:
				createMoreInfoSlide();
				break;
			case REVIEWS:
				createReviewsSlide();
				break;
			case MAP:
				createMapSlide();
				break;
			case VIDEO:
				createVideoSlide();
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

	private void createLoggedOutSlide() {
				
		new HousePages(false);
		createSidebar();
		createMenuBar();	
	}

	private void createHomeSlide() {
        
		new HousePages(false);
		createSidebar();
		createMenuBar();
	}

	private void createLoginSlide() {
		
		new Login();
		createSidebar();
	}
	
	private void createRegisterSlide() {
		
		new Register();
		createSidebar();
	}
	
	private void createProfileSlide() {
		
		new ProfileViewer();
		createSidebar();
	}
	
	private void createAccountSettingsSlide() {
		
		new AccountSettings();
		createSidebar();
	}
	
	private void createSavedPropertySlide(){
		
		new SavedProperties();
		createSidebar();
	}
	
	private void createPropertySlide(){
		
		new HousePages(true);
		createSidebar();
		createMenuBar();
	}
	
	public void createMoreInfoSlide(){
		
		new MoreInfo();
		createSidebar();
		createMenuBar();
	}
	
	public void createReviewsSlide(){
		
		new HouseReviews();
		createSidebar();
		createMenuBar();
	}
	
	public void createMapSlide(){
		
		new GoogleMapsPage();
		createSidebar();
		createMenuBar();
	}
	public void createVideoSlide(){
		
		//new VideoSlide();
		createSidebar();
		createMenuBar();
	}
	
	public void createSidebar(){
		VBox sidebar = new VBox(20);
		
		if(slideID != INDEX&& slideID !=LOGIN && slideID != REGISTER){
			User currentUser = Database.getUser(currentUsername);
			Label labelName = new Label(currentUser.first_name);
			labelName.setWrapText(true);
			labelName.setFont(Font.font(null, FontWeight.BOLD, 20));
			labelName.setTextFill(Color.web("#162252FF"));
			
			Label labelProfile = new Label("Profile");
			labelProfile.setFont(new Font(16));
			labelProfile.setOnMouseClicked(new EventHandler<MouseEvent>(){
				public void handle(MouseEvent arg0) {
					root.getChildren().clear();
					slideID = PROFILE;
					createSlide();
		        }
		    });
			
			Label labelSavedProperties = new Label("Saved Properties");
			labelSavedProperties.setFont(new Font(16));
			labelSavedProperties.setOnMouseClicked(new EventHandler<MouseEvent>(){
				public void handle(MouseEvent arg0) {
					root.getChildren().clear();
					slideID = SAVEDPROPERTIES;
					createSlide();
		        }
		    });
			Label labelLogOut = new Label("Log Out");
			labelLogOut.setFont(new Font(16));
			labelLogOut.setOnMouseClicked(new EventHandler<MouseEvent>(){
				public void handle(MouseEvent arg0) {
					currentUsername = null;
					loadSlide(INDEX);
		        }
		    });
			
			if(slideID == SAVEDPROPERTIES || slideID == HOUSE) labelSavedProperties.setFont(Font.font(null, FontWeight.BOLD, 16.5));
			else if(slideID == PROFILE || slideID == ACCOUNTSETTINGS) labelProfile.setFont(Font.font(null, FontWeight.BOLD, 16.5));
			
			setupLabelHover(labelProfile);
			setupLabelHover(labelSavedProperties);
			setupLabelHover(labelLogOut);
			
			sidebar.getChildren().addAll(labelName,labelProfile,labelSavedProperties,labelLogOut);
		}		
		else{
			Label labelLogin = new Label("Login");
			labelLogin.setFont(new Font(16));
			labelLogin.setOnMouseClicked(new EventHandler<MouseEvent>(){
				public void handle(MouseEvent arg0) {
					root.getChildren().clear();
					slideID = LOGIN;
					createSlide();
		        }
		    });	
			
			Label labelRegister = new Label("Register");
			labelRegister.setFont(new Font(16));
			labelRegister.setOnMouseClicked(new EventHandler<MouseEvent>(){
				public void handle(MouseEvent arg0) {
					root.getChildren().clear();
					slideID = REGISTER;
					createSlide();
		        }
		    });	
			
			if(slideID == LOGIN) labelLogin.setFont(Font.font(null, FontWeight.BOLD, 16.5));
			else if(slideID == REGISTER) labelRegister.setFont(Font.font(null, FontWeight.BOLD, 16.5));
			
			setupLabelHover(labelLogin);
			setupLabelHover(labelRegister);
			
			sidebar.getChildren().addAll(labelLogin,labelRegister);
		}
		
		sidebar.relocate(30, 30);
		
		root.getChildren().add(sidebar);
	}
	
	public void createMenuBar(){
		HBox buttonRow = new HBox(5);
		ButtonType button1 = new ButtonType("144,171,199",null,"Video Tour",140,40);
		Button videoButton = new SetupButton().CreateButton(button1);
		videoButton.setFocusTraversable(false);
		
		ButtonType button2 = new ButtonType("144,171,199",null,"Map",140,40);
		Button mapButton = new SetupButton().CreateButton(button2);
		mapButton.setFocusTraversable(false);
		mapButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				System.out.println("Map Button");
				root.getChildren().clear();
				slideID = MAP;
				createSlide();
			}
		});
	
		ButtonType button3 = new ButtonType("144,171,199",null,"Information",140,40);
		Button infoButton = new SetupButton().CreateButton(button3);
		infoButton.setFocusTraversable(false);
		infoButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				root.getChildren().clear();
				slideID = MOREINFO;
				createSlide();
			}
		});
		
		ButtonType button4 = new ButtonType("144,171,199",null,"Reviews",140,40);
		Button reviewsButton = new SetupButton().CreateButton(button4);
		reviewsButton.setFocusTraversable(false);
		reviewsButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				root.getChildren().clear();
				slideID = REVIEWS;
				createSlide();
			}
		});
		
		if(slideID == INDEX)
		{
			buttonRow.setOnMouseClicked(new EventHandler<MouseEvent>(){
	            public void handle(MouseEvent t) {
	            	JOptionPane.showMessageDialog(null,
	    					"Login for full features.", "",
	    					JOptionPane.PLAIN_MESSAGE);
	            }
	        });
			videoButton.setDisable(true);			
			mapButton.setDisable(true);
			infoButton.setDisable(true);
			reviewsButton.setDisable(true);
		}
		
		buttonRow.relocate(350, 0);
		buttonRow.getChildren().addAll(videoButton,mapButton,infoButton,reviewsButton);
		
		root.getChildren().add(buttonRow);
	}
	
	private void setupLabelHover(final Label input){
		
		final boolean selected;
		if(input.getFont().getSize() == 16.5) selected = true;
		else selected = false;
		
		input.setCursor(Cursor.HAND);
		input.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent arg0) {
				if(selected) input.setFont(Font.font(null, FontWeight.BOLD, 17));
				else input.setFont(new Font(17));
	        }
	    });
		
		input.setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent arg0) {
				if(selected) input.setFont(Font.font(null, FontWeight.BOLD, 16.5));
				else input.setFont(new Font(16));
	        }
	    });
	}
	
	public static void setupBackButton(){
		
		ButtonType button1 = new ButtonType("150,150,150",null,"Back",100,30);
		Button buttonBack = new SetupButton().CreateButton(button1);
		buttonBack.relocate(195, 20);
		
		buttonBack.setCursor(Cursor.HAND);
		buttonBack.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				if(currentUsername != null)
					loadSlide(HOUSES);
				else
					loadSlide(INDEX);
				HousePages.setTimerState("PAUSE");
			}
		});
		
		root.getChildren().add(buttonBack);
	}
}
