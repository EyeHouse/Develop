package presenter;

import database.Database;
import database.House;
import database.HouseVideo;
import javafx.scene.layout.StackPane;

public class VideoPage extends presenter.Window {

	public VideoPage() {
		
		setupVideoPlayer();
		SlideContent.setupBackButton();
	}
	
	private void setupVideoPlayer(){
		StackPane videoPane = new StackPane();

		House house = Database.getHouse(currentPropertyID);
		//HouseVideo vid = Database.getVideoInfo()
		VideoElement video = new VideoElement(
				"./resources/videos/avengers-featurehp.mp4");
		video.setWidth(500);
		video.setAutoplay(true);
		video.display(videoPane);

		videoPane.relocate(300, 100);

		root.getChildren().add(videoPane);
	}
}
