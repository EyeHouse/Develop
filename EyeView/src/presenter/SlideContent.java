package presenter;

import java.util.List;

import parser.Graphic;
import presenter.GraphicHandler.GraphicElement;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class SlideContent {

	private Group slide;
	private double xResolution;
	private double yResolution;
	private ImageView imageView;

	public SlideContent(Group slide) {
		this.xResolution = Window.xResolution;
		this.yResolution = Window.yResolution;
		this.slide = slide;
	}

	public void BuildSlide(int slideID) {

		LoadXMLGraphics();
		 System.out.println(Window.groupID);
		//if (Window.groupID == "5") {
			switch (slideID) {
			case Window.INDEX:
				LoggedOutSlide();
				break;
			case Window.HOUSES:
				HomeSlide();
				break;
			default:
				break;
			}
		//}
	}

	public void LoadXMLGraphics() {
		GraphicHandler gh = new GraphicHandler(slide);
		List<Graphic> graphicList = Window.slideData.getGraphicList();

		for (Graphic currentGraphic : graphicList) {
			GraphicElement graphic = gh.new GraphicElement(
					currentGraphic.getType(), currentGraphic.getXstart(),
					currentGraphic.getYstart(), currentGraphic.getXend(),
					currentGraphic.getYend(), currentGraphic.getDuration(),
					currentGraphic.isSolid(), currentGraphic.getGraphicColor(),
					currentGraphic.getShadingColor());
			gh.AddShapeToCanvas(graphic);
		}
	}

	private void LoggedOutSlide() {

		Label label = new Label(
				"This is a really really long amount of text. It is testing the wrappability of the Label class.");
		label.setTextAlignment(TextAlignment.CENTER);
		label.setLayoutX(20);
		label.setLayoutY(50);
		label.setWrapText(true);
		label.setPrefWidth(0.1*xResolution);
		label.setFont(new Font(0.015*xResolution));
		label.setTextFill(Color.web("#162252FF"));
		label.setContentDisplay(ContentDisplay.CENTER);
		slide.getChildren().add(label);

		ImageView image = new ImageView(new Image("file:./resources/images/buckingham-palace.jpg"));
		image.setLayoutX(220);
		image.setLayoutY(140);
		image.setFitWidth(400);
		image.setFitHeight(375);
		slide.getChildren().add(image);

	}

	private void HomeSlide() {

		imageView = new ImageView(new Image("file:./resources/images/icon-48x48.png"));
		Button button = new Button("Click Me!", imageView);
		button.setContentDisplay(ContentDisplay.LEFT);
		slide.getChildren().add(button);
	}

}
