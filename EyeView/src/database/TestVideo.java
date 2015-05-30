package database;

import java.io.File;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TestVideo extends Application {
	// private String Dir = System.getProperty("user.dir");

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		Database.dbConnect();
		
		// goes to user Directory
		HouseVideo house1;
		User tempu7 = Database.getUser("Henry");
		House temph7 = Database.getHouse(9);
		File file = null;

		// Get video info into HouseVideo
		house1 = Database.getVideoInfo(tempu7, temph7);

		house1.printHouseInfo();
		
		// Get temporary video file
		file = FileManager.readVideo(tempu7, house1);

		// Converts media to string URL
		Media media = new Media(file.toURI().toURL().toString());
		javafx.scene.media.MediaPlayer player = new javafx.scene.media.MediaPlayer(
				media);
		MediaView viewer = new MediaView(player);

		// change width and height to fit video
		DoubleProperty width = viewer.fitWidthProperty();
		DoubleProperty height = viewer.fitHeightProperty();
		width.bind(Bindings.selectDouble(viewer.sceneProperty(), "width"));
		height.bind(Bindings.selectDouble(viewer.sceneProperty(), "height"));
		viewer.setPreserveRatio(true);

		StackPane root = new StackPane();
		root.getChildren().add(viewer);

		
		// set the Scene
		Scene scenes = new Scene(root, 500, 500, Color.BLACK);
		stage.setScene(scenes);
		stage.setTitle(temph7.title);
		stage.setFullScreen(true);
		stage.show();
		player.play();
	}
}