package houseAdverts;

import handlers.TextElement;
import handlers.TextHandler;
import database.Database;
import database.House;
import presenter.SlideContent;
import presenter.Window;
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
		
		House house = Database.getHouse(currentPropertyID);
		
		addTextElement(house.description, 0, 0, 1, null, null, 0);
		root.getChildren().add(pane);
		//add BackButton
		SlideContent.setupBackButton();
	}


	public void addTextElement(String text, double xpos, double ypos, 
			double wrappingWidth, Color color, Font font, double timeout) {
		
		// Add text element
		TextElement textElement = new TextElement(text);
		textElement.setWrappingWidth(wrappingWidth);
		TextHandler textHandler = new TextHandler();
		textHandler.addTextElement(textElement);
		textHandler.display(pane);
	}


}
