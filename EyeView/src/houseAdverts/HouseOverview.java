package houseAdverts;

import java.util.ArrayList;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import javafx.util.Duration;
import language.Translate;
import presenter.Window;
import Button.ButtonType;
import Button.SetupButton;
import Profile.SavedProperties;
import database.Database;
import database.House;
import database.HouseImage;
import database.User;

public class HouseOverview extends Window {

	private ImageGallery gallery;
	
	private Pagination pagination;
	private ArrayList<String> savedProperties = new ArrayList<String>();
	private ArrayList<House> houses = new ArrayList<House>();
	private ArrayList<ArrayList<Image>> galleries = new ArrayList<ArrayList<Image>>();
	public Label labelBedrooms = new Label();
	public Label labelLandlord= new Label();
	static Image play;

	static Image pause;
	static ImageView playButton;

	public HouseOverview(boolean singlePropertyView, ArrayList<House> houses1) {
		
		play = new Image("file:resources/advert_icons/play.png");
		pause = new Image("file:resources/advert_icons/pause.png");
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
		
		VBox propertyInfo = new VBox(20);
		
		Pane galleryPane = new Pane();
		galleryPane.setPrefSize(750, 550);
		galleryPane.getChildren().clear();

		gallery = new ImageGallery(galleries.get(pageIndex), 20, 80);
		
		galleryPane.getChildren().add(gallery.getGallery());


		Label address = new Label(houses.get(pageIndex).address);
		address.setFont(Font.font(null, FontWeight.BOLD, 24));
		Label price = new Label("£" + Integer.toString(houses.get(pageIndex).price) + " pcm");
		price.setFont(new Font(20));
		
		HBox bedroomsBox = new HBox(0);
		labelBedrooms = new Label(Translate.translateText(languageIndex, "Bedrooms") + ": ");
		labelBedrooms.setFont(new Font(20));
		Label bedrooms = new Label(Integer.toString(houses.get(pageIndex).rooms));
		bedroomsBox.getChildren().addAll(labelBedrooms,bedrooms);
		bedrooms.setFont(new Font(20));
		
		HBox landlordBox = new HBox(0);
		final User landlordUser = Database.getUser(Database.getUsername(houses.get(pageIndex).uid));
		labelLandlord = new Label(Translate.translateText(languageIndex, "Landlord") + ": ");
		labelLandlord.setFont(new Font(20));
		Label landlord = new Label(landlordUser.first_name);
		if(currentUsername != null){
			landlord.setCursor(Cursor.HAND);
			landlord.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent ae) {
					// Add house save to database.
					viewedUsername = landlordUser.username;
					loadSlide(PROFILE);
				}
			});
		}
				
		landlordBox.getChildren().addAll(labelLandlord,landlord);
		landlord.setFont(new Font(20));
		
		propertyInfo.getChildren().addAll(address,price,bedroomsBox,landlordBox);
		propertyInfo.relocate(450, 100);

				
		setupSaveButton(galleryPane);
		
		if(slideID == HOUSE){
			SavedProperties.setupPropertyBackButton();
		}
		
		galleryPane.getChildren().add(propertyInfo);

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
				if (advertTimer.getStatus().equals(Status.RUNNING)) {
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

		playButton = new ImageView(play);
		playButton.setPreserveRatio(true);
		playButton.setFitWidth(35);
		playButton.relocate(800,680);
		playButton.setCursor(Cursor.HAND);
		playButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent ae) {
				if (!advertTimer.getStatus().equals(Status.PAUSED)) {
					setTimerState("PAUSE");
				} else {
					setTimerState("PLAY");
				}
			}
		});

		root.getChildren().addAll(playButton);
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
			playButton.setImage(play);
			advertTimer.play();
			break;
		case "PAUSE":
			playButton.setImage(pause);
			advertTimer.pause();
			break;
		}
	}
	
	public void UpdateLanguage(){
		labelBedrooms.setText(Translate.translateText(
				languageIndex, "Bedrooms") + ": ");
		labelLandlord.setText(Translate.translateText(
				languageIndex, "Landlord") + ": ");
	}
}
