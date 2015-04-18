package presenter;

import presenter.SlideContent;
import presenter.TextElement;
import presenter.TextHandler;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.layout.StackPane;

public class MoreInfo extends presenter.Window {

	StackPane pane;


	public MoreInfo(){
		//setupGrid();
		pane = new StackPane();
		pane.setAlignment(Pos.CENTER);
		pane.relocate(250,200);
		pane.resize(400, 400);
		addTextElement(null, 0, 0, 0, null, null, 0);
		root.getChildren().add(pane);
		//add BackButton
		SlideContent.setupBackButton();
	}


	public void addTextElement(String text, double xpos, double ypos, 
			double wrappingWidth, Color color, Font font, double timeout) {
		
		// Add text element
		TextElement textElement = new TextElement("Test String\nText 123456");
		TextHandler textHandler = new TextHandler();
		textHandler.addTextElement(textElement);
		textHandler.display(pane);
	}


}
