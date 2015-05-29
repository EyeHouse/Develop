package houses;

import handlers.VideoElement;

import java.io.File;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import language.Translate;
import presenter.SlideContent;
import presenter.Window;
import Button.ButtonType;
import Button.SetupButton;
import database.Database;
import database.FileManager;
import database.House;
import database.HouseVideo;
import database.Marker;
import database.User;


public class VideoPage extends Window {
	
	private static Label topTitle = new Label();
	private ArrayList<Marker> videoMarkers;
	private VideoElement video;
	
	public VideoPage() {
		setupTitle();
		setupVideoPlayer();
		UpdateLanguage();
		SlideContent.setupBackButton();

	}

	private void setupVideoPlayer() {

		StackPane videoPane = new StackPane();

		House house = Database.getHouse(currentPropertyID);
		User owner = Database.getUser(Database.getUsername(house.uid));
		HouseVideo videoInfo = Database.checkHouseVideo(owner, house.hid);
		String videoPath = null;

		if (videoInfo != null) {

			File file = FileManager.readVideo(Database.getUser(currentUsername),videoInfo);
			videoPath = file.getAbsolutePath();
			video = new VideoElement(videoPath,true);
			video.setStylesheet("resources/videoStyle.css");
			video.setWidth(600);
			video.setAutoplay(false);
			video.display(videoPane);
			
			setupMarkerButtons();
		}
		
		videoPane.relocate(250, 130);
		
		root.getChildren().add(videoPane);
	}
	
	public void setupTitle() {

		topTitle = new Label(Translate.translateText(languageIndex,"Video Tour"));
		topTitle.setTextFill(Color.web("#162252FF"));
		topTitle.setFont(new Font(32));
		topTitle.relocate(480, 80);
		root.getChildren().add(topTitle);
	}	
	
	public void UpdateLanguage() {
		topTitle.setText(Translate.translateText(languageIndex, "Video Tour"));
	}
	
	public void setupMarkerButtons(){
		
		House house = Database.getHouse(currentPropertyID);
		User owner = Database.getUser(Database.getUsername(house.uid));
		HouseVideo videoInfo = Database.checkHouseVideo(owner, house.hid);
		videoMarkers = Database.getVideoMarkers(videoInfo.vid);
		
		TilePane markerTiles = new TilePane();
		markerTiles.setVgap(10);
		markerTiles.setHgap(10);
		markerTiles.setPadding(new Insets(20, 20, 20, 20));
		markerTiles.setTileAlignment(Pos.CENTER);
		markerTiles.setPrefColumns(5);
		ScrollPane markersWindow = new ScrollPane();
		markersWindow.setMinSize(600, 200);
		markersWindow.setMaxSize(600, 200);
		
		for (int i = 0; i < videoMarkers.size(); i++) {
			
			Marker marker = videoMarkers.get(i);
			
			ButtonType button = new ButtonType("166,208,255", null,
					Translate.translateText(languageIndex, marker.room), 100, 30);
			Button buttonTime = new SetupButton().CreateButton(button);
			buttonTime.setCursor(Cursor.HAND);
			buttonTime.setOnAction(new VideoTime(i));
			
			markerTiles.getChildren().add(buttonTime);
					
		}
	
		markersWindow.setContent(markerTiles);
		markersWindow.relocate(250, 500);
		root.getChildren().add(markersWindow);

		GridPane.setConstraints(markersWindow, 0, 2, 3, 1, HPos.CENTER,
				VPos.CENTER);
	}
	
	public class VideoTime implements EventHandler<ActionEvent> {

		private int index;
		
		public VideoTime(int index){
			this.index = index;
		}
		
		public void handle(ActionEvent arg0) {
			
			Duration markerTime = Duration.seconds(videoMarkers.get(index).markerTime);
			video.setVideoTime(markerTime);
			video.playVideo();
		}
	}
}

