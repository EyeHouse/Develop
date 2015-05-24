package landlord;

/**
 * VideoUploadPage provides methods necessary for landlords to upload a new
 * house video and add markers to markers to mark the times that each room in 
 * the house occurs in the video
 * 
 * Version: 1.0
 * 
 * Copyright Eyehouse
 */

import handlers.VideoElement;

import java.io.File;

import presenter.SlideContent;
import presenter.Window;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import Button.ButtonType;
import Button.SetupButton;

public class VideoUploadPage extends Window {

	// Variable Declarations
	private GridPane videoGrid = new GridPane();
	private File newVideoFile;
	private String newVideoFileString;
	private VideoElement video;

	private static final int gridCellWidth = 250;
	private static final int gridCellHeight = 10;

	private TextField fileTextField;

	private Button uploadVideoButton;
	private TextField addNewRoomTextField;
	ListView<HBox> markerList = new ListView<HBox>();
	ObservableList<HBox> items = FXCollections.observableArrayList();

	/**
	 * Sets up video upload page
	 */
	public VideoUploadPage() {

		// Setup layout manager
		SetupVideoGrid();
		// Setup file chooser
		SetupFileChooser();
		// Setup back button
		SlideContent.setupBackButton();
		// Add GridPane to page
		root.getChildren().add(videoGrid);
	}

	/**
	 * Sets up GridPane to layout video upload page contents
	 */
	private void SetupVideoGrid() {
		// Set grid size and spacing in group.
		videoGrid.setPrefWidth(600);
		videoGrid.setHgap(gridCellWidth);
		videoGrid.setVgap(gridCellHeight);
		videoGrid.relocate(300, 100);
	}

	/**
	 * Setup text field and browse button to select video file to upload
	 */
	private void SetupFileChooser() {
		// Create HBox to contain file browser
		HBox fileHBox = new HBox(10);
		// fileHBox.setAlignment(VPos.CENTER);
		Label fileLabel = new Label("File:");
		// Create file directory text field
		fileTextField = new TextField();
		fileTextField.setPrefWidth(250);
		fileTextField.textProperty().addListener(new TextChanged());

		// Add FileChooser button
		ButtonType button1 = new ButtonType("150,150,150", null, "Browse", 100,
				30);
		Button fileChooserButton = new SetupButton().CreateButton(button1);

		// Add Upload Video button
		if (fileTextField.getText() != null) {

			// uploadVideoButton.setDisable(false);

			ButtonType button2 = new ButtonType("150,150,150", null, "Upload",
					100, 30);
			uploadVideoButton = new SetupButton().CreateButton(button2);
			uploadVideoButton.setDisable(true);

			// Setup Upload video button event
			uploadVideoButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {

					// Get video URL from text field and call video handler to
					// display
					newVideoFileString = fileTextField.getText();
					if (newVideoFileString != null) {
						SetupVideoPlayer(newVideoFileString);
						SetupRoomMarkers();
					}
				}
			});
		}

		// Setup FileChooser (browse) button event
		fileChooserButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				// File chooser to browse for video file
				FileChooser videoFileChooser = new FileChooser();
				configureFileChooser(videoFileChooser);
				javafx.stage.Window fileChooserStage = null;

				// Append text field with chosen file
				newVideoFile = videoFileChooser
						.showOpenDialog(fileChooserStage);
				if (newVideoFile != null) {
					fileTextField.clear();
					fileTextField.appendText(newVideoFile.toString());
					uploadVideoButton.setDisable(false);
				}
			}
		});

		// Add text field to hbox
		fileHBox.getChildren().addAll(fileLabel, fileTextField,
				fileChooserButton, uploadVideoButton);
		fileHBox.setAlignment(Pos.CENTER);

		// Add hbox to gridpane row 0, spanning two columns
		// (Node child, int columnIndex, int rowIndex, int colspan, int rowspan)
		videoGrid.add(fileHBox, 0, 0);
	}

	/**
	 * Configures file chooser to select only videos
	 * 
	 * @param fileChooser
	 *            File Chooser to choose new video file to upload
	 */
	private void configureFileChooser(FileChooser videoFileChooser) {

		// Set title of file chooser
		videoFileChooser.setTitle("Choose Video File to Upload");
		// Set directory that the file chooser will initially open into
		videoFileChooser.setInitialDirectory(new File(System
				.getProperty("user.home")));
		// Set file types displayed in the file chooser as png and jpg
		videoFileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter("MP4", "*.mp4"));
	}

	/**
	 * Sets up video player within page
	 */
	private void SetupVideoPlayer(String newVideoFileString) {

		StackPane videoPane = new StackPane();

		// House house = Database.getHouse(currentPropertyID);
		// HouseVideo vid = Database.getVideoInfo()
		video = new VideoElement(newVideoFileString);
		video.setStylesheet("resources/videoStyle.css");
		video.setWidth(500);
		video.setAutoplay(true);
		video.display(videoPane);

		// Add video player to GridPane
		videoGrid.add(videoPane, 0, 1);
	}

	private void SetupRoomMarkers() {

		HBox markerSetup = new HBox(10);
		markerList.setPrefHeight(220);
		markerList.setMaxWidth(300);
		
		HBox markerListHeader = new HBox(10);
		markerListHeader.setMaxWidth(300);
		
		
		/* Add room name label and textfield */
		Label addNewRoomLabel = new Label("Add New Room: ");
		addNewRoomTextField = new TextField();
		
		/* Add Set Marker Button */
		ButtonType button1 = new ButtonType("150,150,150", null, "Set Marker",
				100, 30);
		Button setMarkerButton = new SetupButton().CreateButton(button1);
		setMarkerButton.setOnAction(new AddMarker());
		
		/* Add Set Marker Button */
		ButtonType button2 = new ButtonType("150,150,150", null, "Delete",
				100, 30);
		Button deleteMarker = new SetupButton().CreateButton(button2);
		deleteMarker.setOnAction(new deleteMarker());
		
		Label roomNamesHeader = new Label("Rooms");
		roomNamesHeader.setStyle("-fx-font-weight: bold");
		roomNamesHeader.setPrefWidth(190);
		Label videoTimesHeader = new Label("Times in Video");
		videoTimesHeader.setStyle("-fx-font-weight: bold");
		
		// Add marker setup to HBox
		markerSetup.getChildren().addAll(addNewRoomLabel, addNewRoomTextField,setMarkerButton);
		markerSetup.setAlignment(Pos.CENTER);
		markerListHeader.getChildren().addAll(roomNamesHeader, videoTimesHeader);
//		markerListHeader.setAlignment(Pos.CENTER);
		markerList.setItems(items);
		// Add to GridPane
		videoGrid.add(markerSetup, 0, 2);
		videoGrid.add(markerListHeader, 0, 3);
		GridPane.setConstraints(markerListHeader, 0, 3, 1, 1,HPos.CENTER, VPos.CENTER);
		videoGrid.add(markerList, 0, 4);
		GridPane.setConstraints(markerList, 0, 4, 1, 1,HPos.CENTER, VPos.CENTER);
		videoGrid.add(deleteMarker, 0, 5);
		GridPane.setConstraints(deleteMarker, 0, 5, 1, 1,HPos.CENTER, VPos.CENTER);
	}

	/**
	 * Change listener class to enable upload button when text is entered in
	 * text field and disable if no text in field
	 * 
	 * @author hcw515
	 * 
	 */
	public class TextChanged implements ChangeListener<String> {

		public void changed(ObservableValue<? extends String> arg0,
				String arg1, String arg2) {

			if (fileTextField.getText() != null) {
				uploadVideoButton.setDisable(false);
			} else {
				uploadVideoButton.setDisable(true);
			}
		}
	}
	
	public class AddMarker implements EventHandler<ActionEvent>{

		public void handle(ActionEvent arg0) {
			
			if (!addNewRoomTextField.getText().equals("")) {

				// Create room name and video time labels
				HBox newMarker = new HBox(0);
				Label newRoomLabel = new Label(addNewRoomTextField.getText());
				newRoomLabel.setPrefWidth(200);
				Label newVideoTimeLabel = new Label(video.printCurrentVideoTime());
				newVideoTimeLabel.setPrefWidth(80);
				newMarker.getChildren().addAll(newRoomLabel,newVideoTimeLabel);
				
				newMarker.setAlignment(Pos.CENTER_LEFT);
				
				items.add(newMarker);
				addNewRoomTextField.clear();
			}
		}
		
	}

	/**
	 * Button handler for deleteMarkerButton
	 * 
	 * @author hcw515
	 */
	public class deleteMarker implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			int index = markerList.getSelectionModel().getSelectedIndex();

			for (int i = index; i < items.size() - 1; i++) {
				items.set(i, items.get(i + 1));
			}
			
			items.remove(items.size() - 1);
		}
	}

}
