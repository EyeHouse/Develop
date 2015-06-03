package button;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class implements button element functions by taking the information from
 * a ButtonType object and creating a JavaFX Button object with specified
 * height, width, text, and colour.
 * 
 * @version 1.3 (12.03.15)
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class SetupButton {

	ButtonType buttonData;
	Button button = new Button();

	/**
	 * Create button with input data values
	 * 
	 * @param buttonData
	 *            ButtonType containing button variables.
	 * @return Button instance of JavaFX Button with values input.
	 */
	public Button createButton(ButtonType buttonData) {

		this.buttonData = buttonData;

		setButtonRGB();
		setButtonText();
		setButtonSize();

		return button;
	}

	/**
	 * Set the fill and border colours of the created button
	 */
	private void setButtonRGB() {

		// Set fill and border colour using CSS commands
		if (buttonData.borderColourRGB != null && buttonData.colourRGB != null) {
			button.setStyle("-fx-base: rgb(" + buttonData.colourRGB
					+ ");-fx-border-color: rgb(" + buttonData.borderColourRGB
					+ ");");
		} else if (buttonData.colourRGB != null) {
			button.setStyle("-fx-base: rgb(" + buttonData.colourRGB + ");");
		} else if (buttonData.borderColourRGB != null) {
			button.setStyle("-fx-border-color: rgb("
					+ buttonData.borderColourRGB + ");");
		}
	}

	/**
	 * Set the text of the created button
	 */
	private void setButtonText() {

		// Set text of button to input button data
		button.setText(buttonData.text);
	}

	/**
	 * Set the size of the created button
	 */
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
		ImageView buttonImage = new ImageView(image);
		System.out.println(imageButton.getMaxHeight());
		buttonImage.setFitHeight(imageButton.getMaxHeight());
		buttonImage.setFitWidth(imageButton.getMaxWidth());

		imageButton.setGraphic(buttonImage);
		// Display the image and text in the centre of the button
		return imageButton;
	}
}