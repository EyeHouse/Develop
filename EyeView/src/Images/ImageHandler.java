package Images;

import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import Images.ImageType;

/**
 * This class implements an image element function with values passed in
 * 
 * @version 2.3 05.03.15
 * @author EyeHouse
 * 
 * Copyright 2015 EyeHouse
 */

public class ImageHandler {

	private float xResolution; 		// Horizontal resolution of user's screen
	private float yResolution; 		// Vertical resolution of user's screen
	private float xPosition; 		// Horizontal position of image to be displayed
	private float yPosition; 		// Vertical position of image to be displayed
	private float width; 			// Pixel width of image
	private float scaleFactor; 		// Factor to scale image by specified width
	private float scaledWidth; 		// Pixel width after image scaling
	private Timer imageTimer;
	private Group group;
	private ImageView houseImage; 	// ImageView in which Image is drawn

	/**
	 * Constructor for the ImageHandler class, passes in the resolution of the
	 * screen
	 * 
	 * @param group
	 *            this is passed in from the higher level class to be populated
	 *            by images within the ImageHandler class
	 * @param xResolution
	 *            number of pixels in the horizontal dimension of the screen
	 * @param yResolution
	 *            number of pixels in the vertical dimension of the screen
	 */
	public ImageHandler(Group group, float xResolution, float yResolution) {

		this.group = group;
		this.xResolution = xResolution;
		this.yResolution = yResolution;
	}

	/**
	 * This method loads an image from the XML file and places it in the group
	 * at the specified x and y position, and with specified scale, and displays
	 * it at a specified time and for a specified duration
	 * 
	 * @param image
	 *            a container containing the data values required to place an
	 *            image on the screen at a specified x and y position, with a
	 *            specified scale, and displays it at a specified time and for a
	 *            specified duration
	 * @param sourcefile
	 *            the image source file taken from the XML file
	 */
	public void ImageElement(String sourcefile, ImageType image) {

		/*
		 * Convert relative screen position of image to a pixel value using the
		 * native resolution of the user's display
		 */
		xPosition = xResolution * image.xstart;
		yPosition = yResolution * image.ystart;

		// Load Image from source file
		Image house = new Image(sourcefile);
		houseImage = new ImageView(house);

		// Get the width of the image
		width = (float) houseImage.getImage().getWidth();

		// Calculations for scaling the image by width if a width is specified
		if (image.specifiedWidth != 0) {
			scaleFactor = (image.specifiedWidth / width);
			scaledWidth = (scaleFactor * width);

			houseImage.setFitWidth(scaledWidth * image.scale);
		} else {
			// If a width is not specified then do not scale by width
			houseImage.setFitWidth(width * image.scale);
		}

		// Set top left pixel of image as x and y position anchor
		houseImage.setX(xPosition);
		houseImage.setY(yPosition);

		// Preserve the aspect ratio
		houseImage.setPreserveRatio(true);

		houseImage.setVisible(false); 			// Make image invisible
		group.getChildren().add(houseImage); 	// Add image to group

		// Initialise timer to add image
		imageTimer = new Timer();
		// Initialise timer task to display image on screen
		AddImageTask addImage = new AddImageTask(image.duration);
		/*
		 * Schedule adding the image to the screen when timer has reached time
		 * 'starttime'
		 * 
		 * Multiply 'starttime' by 1000 to convert milliseconds to seconds
		 */
		imageTimer.schedule(addImage, (long) image.starttime * 1000);
	}

	/**
	 * This returns the original width of the image
	 * 
	 * @return the original width of the image before any processing by the
	 *         handler
	 */
	public float GetImageWidth() {
		return width;
	}

	/**
	 * This class sets up and runs timer to add an image to screen after a
	 * pre-defined delay
	 */
	private class AddImageTask extends TimerTask {

		private float duration;

		/**
		 * Constructor allows duration to be passed into AddImageTask timer task
		 * 
		 * @param duration
		 *            the length of time that the image should be displayed on
		 *            the screen
		 */
		private AddImageTask(float duration) {
			this.duration = duration;
		}

		/**
		 * Method to determine what timer does when it is run
		 */
		public void run() {
			houseImage.setVisible(true); 	// Make image visible
			imageTimer.cancel(); 			// Cancel timer

			// Initialise a new timer to remove image
			imageTimer = new Timer();
			// Initialise timer task to remove image from screen
			RemoveImageTask removeImage = new RemoveImageTask();
			/*
			 * Schedule the removal of the image from the screen when timer has
			 * reached time 'starttime'
			 * 
			 * Multiply 'starttime' by 1000 to convert milliseconds to seconds
			 */
			imageTimer.schedule(removeImage, (long) duration * 1000);
		}
	}

	/**
	 * This class sets up and runs timer to remove an image from the screen
	 * after a pre-defined delay
	 */
	private class RemoveImageTask extends TimerTask {

		/**
		 * Method to determine what timer does when it is run
		 */
		public void run() {
			houseImage.setVisible(false); 	// Make image invisible
			imageTimer.cancel(); 			// Cancel timer
		}
	}
}
