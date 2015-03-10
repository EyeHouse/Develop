package presenter;

import java.util.List;

import parser.Graphic;
import presenter.GraphicHandler.GraphicElement;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class SlideContent extends Window {

	private ImageView imageView;

	public SlideContent() {
	}

	public void createSlide() {

		// Load all objects from XML file first
		loadXMLGraphics();

		// Checks if the XML belongs to group 5 (EyeHouse)
		if (groupID.matches("5")) {
			// Loads objects not within the XML for a given slide
			switch (slideID) {
			case INDEX:
				createLoggedOutSlide();
				break;
			case HOUSES:
				createHomeSlide();
				break;
			default:
				break;
			}
		}
	}

	public void loadXMLGraphics() {
		GraphicHandler gh = new GraphicHandler();
		List<Graphic> graphicList = slideData.getGraphicList();

		for (Graphic currentGraphic : graphicList) {
			GraphicElement graphic = gh.new GraphicElement(
					currentGraphic.getType(), currentGraphic.getXstart(),
					currentGraphic.getYstart(), currentGraphic.getXend(),
					currentGraphic.getYend(), currentGraphic.getDuration(),
					currentGraphic.isSolid(), currentGraphic.getGraphicColor(),
					currentGraphic.getShadingColor());
			gh.addShapeToCanvas(graphic);
		}
	}

	/* These methods hold the hard-coded information for certain slides */

	private void createLoggedOutSlide() {

		Label label = new Label(
				"This is a really really long amount of text. It is testing the wrappability of the Label class.");
		label.setTextAlignment(TextAlignment.CENTER);
		label.relocate(20, 50);
		label.setWrapText(true);
		label.setPrefWidth(0.12 * xResolution);
		label.setFont(new Font(0.015 * xResolution));
		label.setTextFill(Color.web("#162252FF"));
		label.setContentDisplay(ContentDisplay.CENTER);
		root.getChildren().add(label);

		ImageView image = new ImageView(new Image(
				"file:./resources/images/buckingham-palace.jpg"));
		image.relocate(220, 140);
		image.setFitWidth(400);
		image.setFitHeight(375);
		root.getChildren().add(image);

	}

	private void createHomeSlide() {

		imageView = new ImageView(new Image(
				"file:./resources/images/icon-48x48.png"));
		Button button = new Button("Click Me!", imageView);
		button.setContentDisplay(ContentDisplay.LEFT);
		root.getChildren().add(button);

		button.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				loadSlide(INDEX);
			}
		});
	}

}
