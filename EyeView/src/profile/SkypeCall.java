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

/**
 * This class implements skype calling in the form of a clickable skype logo.
 * 
 * @version 3.3 01.06.15
 * @author EyeHouse
 * 
 *         Copyright 2015 EyeHouse
 */
public class SkypeCall extends Window {

	/**
	 * Create and return a skype call button based on the input Skype ID.
	 * 
	 * @param skypeID
	 *            The Skype ID of the target contact
	 * @return HBox A HBox containing the logo with hover text.
	 */
	public HBox addCallButton(String skypeID) {

		// Create a HBox to contain the logo and hover label
		final HBox skypeBox = new HBox(5);

		// Create the hover label and set to bold
		final Label skypeLabel = new Label(Translator.translateText(
				languageIndex, "Call on Skype"));
		skypeLabel.setFont(Font.font(null, FontWeight.BOLD, 14));

		// Create an imageview of the skype lgo
		ImageView skypeLogo = new ImageView(new Image(
				"file:resources/images/Skype.png"));
		
		// Prevent warping of the logo
		skypeLogo.setPreserveRatio(true);
		
		// Set the cursor to appear as a hand when hovering over the button
		skypeLogo.setCursor(Cursor.HAND);
		
		// Hidden label appears next to button when the mouse enters the button
		skypeLogo.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				skypeBox.getChildren().add(skypeLabel);
			}
		});
		
		// Hidden label disappears when mouse exits the button
		skypeLogo.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				skypeBox.getChildren().remove(skypeLabel);
			}
		});
		
		// Add the call handler to the imageview mouse click event
		skypeLogo.setOnMouseClicked(new callHandler(skypeID));

		// Left align the HBox
		skypeBox.setAlignment(Pos.CENTER_LEFT);
		
		// Add the Skype logo to the HBox
		skypeBox.getChildren().add(skypeLogo);

		// Return the populated HBox
		return skypeBox;
	}

	/**
	 * This event handler handles skype calling when the skype logo is pressed.
	 * 
	 * @author EyeHouse
	 * 
	 *         Copyright 2015 EyeHouse
	 */
	private class callHandler implements EventHandler<MouseEvent> {

		final String skypeID;

		/**
		 * Mouse Event Constructor
		 * 
		 * @param skypeID
		 * 			Skype ID to be called when logo is pressed.
		 */
		public callHandler(String skypeID) {
			this.skypeID = skypeID;
		}

		/**
		 * Mouse Event Handler
		 */
		@Override
		public void handle(MouseEvent arg0) {
			try {
				
				// Construct a URI containing the target skype contact
				URI uri = new URI("skype:" + skypeID + "?call");
				
				// If the desktop is available
				if (Desktop.isDesktopSupported()) {
					try {
						
						// Execute the URI
						Desktop.getDesktop().browse(uri);
					} catch (IOException e) {
						
						// Open a popup if skype is not installed on the computer
						createWarningPopup("Skype must be installed to utilise this feature.");
						dialogStage.show();
					}
				}
			} catch (URISyntaxException e) {
				
				// Catch URI syntax errors
				System.out.println("Invalid Skype ID");
			}
		}

	}
}
