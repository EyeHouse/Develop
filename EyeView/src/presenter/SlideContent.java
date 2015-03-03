package presenter;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextAlignment;

public class SlideContent {
	
	private Group slide = new Group();
	private ImageView imageView;
	
	public SlideContent() {
		
	}
	
	public Group loadSlide(String title) {
		
		slide.getChildren().clear();
		
		switch (title) {
			case "First Slide":
				LoggedOutHomeSlide();
		        break;
			case "Second Slide":
				LoggedInHomeSlide();
		        break;
	        default:
	        	break;
		}
			
		return slide;
	}
	
	public void LoggedOutHomeSlide() {
		
		imageView = new ImageView(new Image("file:" + "icon-48x48.png"));
        Button button = new Button("Click Me!", imageView);
        button.setContentDisplay(ContentDisplay.LEFT);
        slide.getChildren().add(button);
	}
	
	public void LoggedInHomeSlide() {
		
		imageView = new ImageView(new Image("file:" + "icon-48x48.png"));
        Label label = new Label("This is a really really long amount of text. It is testing the wrappability of the Label class.", imageView);
        label.setTextAlignment(TextAlignment.JUSTIFY);
        label.setLayoutX(400);
        label.setLayoutY(300);
        label.setWrapText(true);
        label.setPrefWidth(150);
        label.setContentDisplay(ContentDisplay.CENTER);
        slide.getChildren().add(label);
	}
	
}
