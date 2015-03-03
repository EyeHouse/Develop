package presenter;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
		
		Canvas canvas = new Canvas(960, 720);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.web("#7AA9DDFF"));
		gc.fillRect(0, 0, 185, 720);
		slide.getChildren().add(canvas);
		
        Label label = new Label("This is a really really long amount of text. It is testing the wrappability of the Label class.");
        label.setTextAlignment(TextAlignment.CENTER);
        label.setLayoutX(20);
        label.setLayoutY(50);
        label.setWrapText(true);
        label.setPrefWidth(150);
        label.setFont(new Font(16));
        label.setTextFill(Color.web("#162252FF"));
        label.setContentDisplay(ContentDisplay.CENTER);
        slide.getChildren().add(label);
        
        ImageView image = new ImageView(new Image("file:buckingham-palace.jpg"));
        image.setLayoutX(220);
        image.setLayoutY(140);
        image.setFitWidth(400);
        image.setFitHeight(375);
        slide.getChildren().add(image);
		
	}
	
	public void LoggedInHomeSlide() {
		
		imageView = new ImageView(new Image("file:" + "icon-48x48.png"));
        Button button = new Button("Click Me!", imageView);
        button.setContentDisplay(ContentDisplay.LEFT);
        slide.getChildren().add(button);		
	}
	
}
