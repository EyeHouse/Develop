package houses;

import handlers.VideoElement;

import java.io.File;
import java.util.ArrayList;

import button.ButtonType;
import button.SetupButton;
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
import language.Translator;
import presenter.Window;
import profile.SavedProperties;
import database.Database;
import database.FileManager;
import database.House;
import database.HouseVideo;
import database.Marker;
import database.User;

/**
 * This class creates the video tour page within the EyeView software. The video
 * page displays a video tour, that has been uploaded to the database, for a
 * particular house.
 * 
 * Markers for selecting specific times within the video are also retrieved from
 * the database and displayed in a pane containing buttons which, when clicked,
 * skip to the designated time in that particular video tour.
 * 
 * @version 3.1
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class VideoPage extends Window {

	private Label topTitle = new Label();
	private Label noVideoTitle = new Label();
	private ArrayList<Marker> videoMarkers;
	private TilePane markerTiles;
	private VideoElement video;

	/**
	 * Constructor method
	 */
	public VideoPage() {

		setupTitle();
		setupVideoPlayer();
		SavedProperties.setupPropertyBackButton();
	}

	/**
	 * Creates the title for the video page and displays it on the scene.
	 */
	public void setupTitle() {

		topTitle = new Label(Translator.translateText(languageIndex,
				"Video Tour"));
		topTitle.setFont(new Font(32));
		topTitle.setTextFill(Color.web("#162252"));
		topTitle.setPrefWidth(550);
		topTitle.setAlignment(Pos.CENTER);
		topTitle.relocate(275, 80);
		root.getChildren().add(topTitle);
	}

	/**
	 * Creates the video container with CSS style and displays it on the scene.
	 */
	private void setupVideoPlayer() {

		StackPane videoPane = new StackPane();

		// Retrieve the video tour from the databse for the current property
		House house = Database.getHouse(currentPropertyID);
		User owner = Database.getUser(Database.getUsername(house.uid));
		HouseVideo videoInfo = Database.checkHouseVideo(owner, house.hid);
		String videoPath = null;

		/*
		 * If a video exists for this house, it is added to the screen. If not,
		 * a message is displayed telling the user there is no video for this
		 * property.
		 */
		if (videoInfo != null) {
			File file = FileManager.readVideo(
					Database.getUser(currentUsername), videoInfo);
			videoPath = file.getAbsolutePath();
			video = new VideoElement(videoPath, true);
			video.setStylesheet("resources/videoStyle.css");
			video.setWidth(600);
			video.setAutoplay(false);
			video.display(videoPane);

			setupMarkerButtons();
		} else {
			noVideoTitle = new Label(Translator.translateText(languageIndex,
					"No video available for this property"));
			noVideoTitle.setFont(new Font(24));
			noVideoTitle.setTextFill(Color.web("#8b8989"));
			noVideoTitle.setPrefWidth(550);
			noVideoTitle.setAlignment(Pos.CENTER);
			noVideoTitle.relocate(275, 180);
			root.getChildren().add(noVideoTitle);
		}

		videoPane.relocate(250, 130);

		root.getChildren().add(videoPane);
	}

	/**
	 * Updates the text for the video page when the language has been changed.
	 */
	public void updateLanguage() {

		topTitle.setText(Translator.translateText(languageIndex, "Video Tour"));

		if (video == null) {
			noVideoTitle.setText(Translator.translateText(languageIndex,
					"No video available for this property"));
		} else {
			markerTiles.getChildren().clear();

			for (int i = 0; i < videoMarkers.size(); i++) {
				Marker marker = videoMarkers.get(i);
				ButtonType button = new ButtonType("166,208,255", null,
						Translator.translateText(languageIndex, marker.room),
						125, 30);
				Button buttonTime = new SetupButton().createButton(button);
				buttonTime.setCursor(Cursor.HAND);
				buttonTime.setOnAction(new VideoTime(i));

				markerTiles.getChildren().add(buttonTime);
			}
		}

	}

	/**
	 * Retrieves the custom video markers for the current house video and
	 * displays them in a pane underneath the video.
	 */
	public void setupMarkerButtons() {

		// Retrieve the video markers from the databse for the current property
		House house = Database.getHouse(currentPropertyID);
		User owner = Database.getUser(Database.getUsername(house.uid));
		HouseVideo videoInfo = Database.checkHouseVideo(owner, house.hid);
		videoMarkers = Database.getVideoMarkers(videoInfo.vid);

		// Set up a new pane to contain the video marker buttons
		markerTiles = new TilePane();
		markerTiles.setVgap(10);
		markerTiles.setHgap(19);
		markerTiles.setPadding(new Insets(20, 20, 20, 20));
		markerTiles.setTileAlignment(Pos.CENTER);
		markerTiles.setPrefColumns(4);
		ScrollPane markersWindow = new ScrollPane();
		markersWindow.setMinSize(600, 200);
		markersWindow.setMaxSize(600, 200);

		// Populate the tile pane with buttons
		for (int i = 0; i < videoMarkers.size(); i++) {

			Marker marker = videoMarkers.get(i);
			ButtonType button = new ButtonType("166,208,255", null,
					Translator.translateText(languageIndex, marker.room), 125,
					30);
			Button buttonTime = new SetupButton().createButton(button);
			buttonTime.setWrapText(true);
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

	/**
	 * This private class implements the action for changing the position in the
	 * video after a video marker button has been clicked.
	 */
	private class VideoTime implements EventHandler<ActionEvent> {

		private int index;

		public VideoTime(int index) {
			this.index = index;
		}

		public void handle(ActionEvent arg0) {

			Duration markerTime = Duration
					.seconds(videoMarkers.get(index).markerTime);
			video.setVideoTime(markerTime);
			video.playVideo();
		}
	}

	/**
	 * Removes and nullifies the video and video marker objects after the user
	 * leaves this page.
	 */
	public void dispose() {

		if (video != null) {
			video.stopVideo();
			video = null;
			videoMarkers.clear();
			videoMarkers.trimToSize();
			videoMarkers = null;
		}
	}
}
