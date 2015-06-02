package handlers;

import java.util.ArrayList;

import presenter.Window;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * This class implements an image element function with values passed in.
 * 
 * @version 2.5 12.03.15
 * @author EyeHouse
 * 
 *         Copyright 2015 EyeHouse
 */

public class ImageHandler extends Window {

	private float xPosition; // Horizontal position of image to be displayed
	private float yPosition; // Vertical position of image to be displayed
	private float width; // Pixel width of image
	private float scaleFactor; // Factor to scale image by specified width
	private float scaledWidth; // Pixel width after image scaling

	// ArrayList of ImageViews to contain images added to group.
	private ArrayList<ImageView> imageObjectArray = new ArrayList<ImageView>();

	// ImageElement to contain information of current image.
	private ImageElement imageData;

	private int imageIndex; // Index of current image.

	/**
	 * Passes in a specific ImageElement object and displays it on the screen
	 * with specified parameters
	 * 
	 * @param imageData
	 *            the ImageElement object to be displayed on the current Scene
	 */
	public void createImage(ImageElement imageData) {
		this.imageData = imageData;
		/*
		 * Convert relative screen position of image to a pixel value using the
		 * native resolution of the user's display
		 */
		xPosition = (float) (xResolution * imageData.xstart);
		yPosition = (float) (yResolution * imageData.ystart);

		// Load Image from source file
		Image image = new Image(imageData.sourcefile);
		ImageView imageObject = new ImageView(image);

		// Get the width of the image file in pixels.
		width = (float) imageObject.getImage().getWidth();

		// Calculations for scaling the image by width if a width is specified
		if (imageData.specifiedWidth != 0) {
			scaleFactor = (imageData.specifiedWidth / width);
			scaledWidth = (scaleFactor * width);

			imageObject.setFitWidth(scaledWidth * imageData.scale);
		} else {
			// If a width is not specified then do not scale by width
			imageObject.setFitWidth(width * imageData.scale);
		}

		// Set top left pixel of image as x and y position anchor
		imageObject.setX(xPosition);
		imageObject.setY(yPosition);

		// Preserve the aspect ratio
		imageObject.setPreserveRatio(true);
		imageObject.setVisible(false);
		imageObjectArray.add(imageObject);
		root.getChildren().add(imageObject);

		// Initialise timer to add image
		SetupImageTimer();
	}

	/**
	 * Returns the width of the image
	 * 
	 * @param widthImage
	 *            ImageElement object containing the source file in question
	 * 
	 * @return the original width of the image file before any processing by the
	 *         handler
	 */
	public float GetImageWidth(ImageElement widthImage) {

		// Load Image from source file
		Image image = new Image(widthImage.sourcefile);
		ImageView imageObject = new ImageView(image);

		// Return the width of the image file in pixels.
		return (float) imageObject.getImage().getWidth();
	}

	/**
	 * Setup start and duration timers to show and remove images
	 */
	private void SetupImageTimer() {

		// Store index
		final int currentImageIndex = imageIndex;
		imageIndex++;

		// Add Timeline if starttime is greater than zero.
		if (imageData.starttime > 0) {

			// Instantiate timeline to show image after start time.
			new Timeline(new KeyFrame(
					Duration.millis(imageData.starttime * 1000),
					new EventHandler<ActionEvent>() {
						public void handle(ActionEvent ae) {
							imageObjectArray.get(currentImageIndex).setVisible(
									true);
						}
					})).play();
		} else {

			// Show image if start time is 0.
			imageObjectArray.get(currentImageIndex).setVisible(true);
		}

		// Add timeline if duration is greater than zero.
		if (imageData.duration > 0) {

			// Instantiate Timeline to remove image after total image time.
			new Timeline(
					new KeyFrame(
							Duration.millis((imageData.starttime + imageData.duration) * 1000),
							new EventHandler<ActionEvent>() {
								public void handle(ActionEvent ae) {
									imageObjectArray.get(currentImageIndex)
											.setVisible(false);
								}
							})).play();
		}
	}
}
