package presenter;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import Button.ButtonType;
import Button.SetupButton;

public class SkypePage extends Window {
	public SkypePage(){
		
		ButtonType button1 = new ButtonType("150,150,150",null,"Call Echo",100,30);
		Button buttonCall = new SetupButton().CreateButton(button1);
		
		buttonCall.setOnAction(new callHandler("adam.m.252337"));
		root.getChildren().add(buttonCall);
		buttonCall.relocate(400,300);
	}
	
	public class callHandler implements EventHandler<ActionEvent> {
		
		final String skypeID;
		
		callHandler(String skypeID){
			this.skypeID = skypeID;
		}
		
		@Override
		public void handle(ActionEvent arg0) {
			try {
				URI uri = new URI("skype:" + skypeID + "?call");
				if (Desktop.isDesktopSupported()) {
				    try {
						Desktop.getDesktop().browse(uri);
					} catch (IOException e) {
						System.out.println("Skype not available");
					}
				}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
