package presenter;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javafx.util.Duration;
import Button.ButtonType;
import Button.SetupButton;
import Profile.SavedProperties;
import database.Database;
import database.House;
import database.HouseImage;
import database.User;

public class HousePages extends Window {

	private ImageGallery gallery;
	
	private Pagination pagination;
	private static Button buttonTimerControl;
	private ArrayList<String> savedProperties = new ArrayList<String>();
	private ArrayList<House> houses = new ArrayList<House>();
	private ArrayList<ArrayList<Image>> galleries = new ArrayList<ArrayList<Image>>();

	public HousePages(boolean singlePropertyView, ArrayList<House> houses1) {
		
		this.houses = houses1;
		createGalleryLists();
		if(singlePropertyView){
			Pane houseAdvert = createHousePage(0);
			houseAdvert.relocate(195,80);
			root.getChildren().add(houseAdvert);
		}
		else{
			savedProperties = User.getSavedProperties(currentUsername);
			
			setupPagination();
			setupAdvertTimer();
			setupTimerControl();
		}
	}
	
	private void createGalleryLists(){
		
		for(int i = 0;i<houses.size();i++){
			ArrayList<Image> galleryList = new ArrayList<Image>();
			ArrayList<HouseImage> houseImages = new ArrayList<HouseImage>();
			
			houseImages = Database.getHouseImageSet(houses.get(i).hid);
			
			for(int j = 0; j < houseImages.size(); j++){
				HouseImage input = houseImages.get(j);
				Image image = new Image(input.imageIS);
				galleryList.add(image);
			}
			if(houses.get(i).energyRatingIS != null){
				Image image = new Image(houses.get(i).energyRatingIS);
				galleryList.add(image);
			}
			galleries.add(galleryList);
		}
	}
	
	protected Pane createHousePage(Integer pageIndex) {
		
		StackPane pane = new StackPane();	
		
		pane.resize(300,400);
		Pane galleryPane = new Pane();
		galleryPane.setPrefSize(750, 550);
		galleryPane.getChildren().clear();

		gallery = new ImageGallery(galleries.get(pageIndex), 20, 80);
		
		galleryPane.getChildren().add(gallery.getGallery());


		TextElement address = new TextElement(houses.get(pageIndex).address);
		TextElement price = new TextElement("£" + Integer.toString(houses.get(pageIndex).price) + " pcm");
		price.setYpos(0.2);
		TextElement bedrooms = new TextElement("Bedrooms: " + Integer.toString(houses.get(pageIndex).rooms));
		bedrooms.setYpos(0.4);
		TextHandler textHandler = new TextHandler();
		textHandler.addTextElement(address);
		textHandler.addTextElement(price);
		textHandler.addTextElement(bedrooms);
		textHandler.display(pane);
		
		pane.relocate(450, 0);
				
		setupSaveButton(galleryPane);
		
		if(slideID == HOUSE){
			SavedProperties.setupPropertyBackButton();
		}
		
		galleryPane.getChildren().add(pane);

		return galleryPane;
	}
	
	private void setupSaveButton(Pane galleryPane){
		ButtonType button1 = new ButtonType("150,150,150", null, "Save", 130,
				30);
		final Button buttonSave = new SetupButton().CreateButton(button1);
		if (savedProperties.contains(String.format("%03d", currentPropertyID))) {
			buttonSave.setDisable(true);
			buttonSave.setText("Saved");
		}
		
		buttonSave.relocate(450, 400);

		buttonSave.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				// Add house save to database.
				buttonSave.setDisable(true);
				buttonSave.setText("Saved");
				savedProperties.add(String.format("%03d", currentPropertyID));
				User.updateSavedProperties(currentUsername, savedProperties);
			}
		});
		
		if (slideID != INDEX && slideID != HOUSE) {
			galleryPane.getChildren().add(buttonSave);
		}
	}

	private void setupPagination() {
		pagination = new Pagination(houses.size(), 0);

		pagination.setPageFactory(new Callback<Integer, Node>() {
			public Node call(Integer pageIndex) {
				if (buttonTimerControl.getText().equals("Pause")) {
					advertTimer.playFromStart();
				}
				currentPropertyID = houses.get(pageIndex).hid;
				return createHousePage(pageIndex);
			}
		});
		pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
		pagination.relocate(195, 80);

		root.getChildren().addAll(pagination);
	}

	private void setupTimerControl() {
		ButtonType button1 = new ButtonType("150,150,150", null, "Pause", 100,
				30);
		buttonTimerControl = new SetupButton().CreateButton(button1);
		buttonTimerControl.relocate(800, 700);

		buttonTimerControl.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				if (buttonTimerControl.getText().equals("Pause")) {
					setTimerState("PAUSE");
				} else {
					setTimerState("PLAY");
				}
			}
		});

		root.getChildren().add(buttonTimerControl);
	}

	private void setupAdvertTimer() {
		advertTimer = new Timeline(new KeyFrame(Duration.millis(5 * 1000),
				new EventHandler<ActionEvent>() {
					public void handle(ActionEvent ae) {
						int index = pagination.getCurrentPageIndex();
						index++;
						if (index >= pagination.getPageCount())
							index = 0;
						pagination.setCurrentPageIndex(index);

					}
				}));
		advertTimer.play();
	}

	public static void setTimerState(String newState) {
		switch (newState) {
		case "PLAY":
			buttonTimerControl.setText("Pause");
			advertTimer.play();
			break;
		case "PAUSE":
			buttonTimerControl.setText("Play");
			advertTimer.pause();
			break;
		}
	}
}
