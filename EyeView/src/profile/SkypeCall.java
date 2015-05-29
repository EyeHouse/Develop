package profile;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;

import presenter.Window;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;

public class SkypeCall extends Window {
	
	public ImageView addCallButton(String skypeID, double width){
		Image logo = new Image("file:resources/images/Skype.png");
		ImageView skypeLogo = new ImageView(logo);
		skypeLogo.setPreserveRatio(true);
		skypeLogo.setFitWidth(width);
		skypeLogo.setCursor(Cursor.HAND);
		skypeLogo.setOnMouseClicked(new callHandler(skypeID));
		
		return skypeLogo;
	}
	
	private class callHandler implements EventHandler<MouseEvent> {
		
		final String skypeID;
		
		callHandler(String skypeID){
			this.skypeID = skypeID;
		}
		
		@Override
		public void handle(MouseEvent arg0) {
			try {
				URI uri = new URI("skype:" + skypeID + "?call");
				if (Desktop.isDesktopSupported()) {
				    try {
						Desktop.getDesktop().browse(uri);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null,
								"Skype must be installed to utilise this feature.", "Skype Call Error",
								JOptionPane.WARNING_MESSAGE);
					}
				}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
