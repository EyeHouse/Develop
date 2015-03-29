package Button;

/**
 * This class implements button element functions
 * 
 * @version 1.3
 * 12.03.15
 * @author EyeHouse
 * 
 * Copyright 2015 EyeHouse
 */

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SetupButton {
	ButtonType buttonData;
	Button button = new Button();

	/**
	 * Create button with input data values
	 * 
	 * @param buttonData
	 *            ButtonType containing button variables.
	 * @return Button Instance of JavaFX Button with values input.
	 * */
	public Button CreateButton(ButtonType buttonData) {
		this.buttonData = buttonData;
		setButtonRGB();
		setButtonText();
		setButtonSize();
		return this.button;
	}

	/* Set the fill and border colours of the created button */
	private void setButtonRGB() {

		// Set fill and border colour using CSS commands
		if (buttonData.borderColourRGB != null) {
			button.setStyle("-fx-base: rgb(" + buttonData.colourRGB
					+ ");-fx-border-color: rgb(" + buttonData.borderColourRGB
					+ ");");
		} else {
			button.setStyle("-fx-base: rgb(" + buttonData.colourRGB + ");");
		}

	}

	/* Set the text of the created button */
	private void setButtonText() {

		// Set text of button to input button data
		button.setText(buttonData.text);
	}

	/* Set the size of the created button */
	private void setButtonSize() {

		// Set size of button from buttonData object.
		button.setMinSize(buttonData.xSize, buttonData.ySize);
		button.setMaxSize(buttonData.xSize, buttonData.ySize);
	}

	/**
	 * Add image to a button
	 * 
	 * @param imageButton
	 *            The button to be modified.
	 * 
	 * @param image
	 *            The image to be added to the button.
	 * @return Button Instance of JavaFX Button with image set.
	 */
	public Button setButtonImage(Button imageButton, Image image) {

		// Add the image and text to the button
		imageButton.setGraphic(new ImageView(image));
		// Display the image and text in the centre of the button
		return imageButton;
	}
}