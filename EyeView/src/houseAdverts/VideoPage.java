package houseAdverts;

import presenter.SlideContent;
import presenter.Window;
import handlers.VideoElement;
import database.Database;
import database.House;
import database.HouseVideo;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import language.Translate;

public class VideoPage extends Window {
	public Label topTitle = new Label();
	
	public VideoPage() {
		setupTitle();
		setupVideoPlayer();
		SlideContent.setupBackButton();

	}

	private void setupVideoPlayer() {

		StackPane videoPane = new StackPane();

		House house = Database.getHouse(currentPropertyID);
		// HouseVideo vid = Database.getVideoInfo()
		VideoElement video = new VideoElement("resources/videos/avengers-featurehp.mp4");
		video.setStylesheet("resources/videoStyle.css");
		video.setWidth(500);
		video.setAutoplay(true);
		video.display(videoPane);
		
		videoPane.relocate(300, 100);
		
		root.getChildren().add(videoPane);
	}
	

	public void setupTitle() {

		topTitle = new Label(Translate.translateText(languageIndex,"Property Video Tour"));
		topTitle.setTextFill(Color.web("#162252FF"));
		topTitle.setFont(new Font(28));
		topTitle.relocate(450, 60);
		root.getChildren().add(topTitle);
	}	
	
}
