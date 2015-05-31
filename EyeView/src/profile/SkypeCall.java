package profile;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import presenter.Window;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import language.Translator;

public class SkypeCall extends Window {

	public HBox addCallButton(String skypeID) {

		final HBox skypeBox = new HBox(5);
		
		// Hidden label appears next to button when enabled and hovered over
		final Label skypeLabel = new Label(Translator.translateText(languageIndex,
				"Call on Skype"));
		skypeLabel.setFont(Font.font(null, FontWeight.BOLD, 14));
		
		ImageView skypeLogo = new ImageView(new Image(
				"file:resources/images/Skype.png"));
		skypeLogo.setPreserveRatio(true);
		skypeLogo.setCursor(Cursor.HAND);
		skypeLogo.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				skypeBox.getChildren().add(skypeLabel);
			}
		});
		skypeLogo.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				skypeBox.getChildren().remove(skypeLabel);
			}
		});
		skypeLogo.setOnMouseClicked(new callHandler(skypeID));

		skypeBox.setAlignment(Pos.CENTER_LEFT);
		skypeBox.getChildren().add(skypeLogo);

		return skypeBox;
	}

	private class callHandler implements EventHandler<MouseEvent> {

		final String skypeID;

		callHandler(String skypeID) {
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
						createWarningPopup("Skype must be installed to utilise this feature.");
						dialogStage.show();
					}
				}
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}

	}
}
