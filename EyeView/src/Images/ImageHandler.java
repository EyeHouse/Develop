package Images;
/**
 * This class implements an image element function with values passed in
 * 
 * @version 2.3
 * 05.03.15
 * @author EyeHouse
 * 
 * Copyright 2015 EyeHouse
 */

import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import Images.ImageType;

public class ImageHandler {
	
	private float xResolution;
	private float yResolution;
	private float xPosition;
	private float yPosition;
	private float width;
	private float height;
	private float scaleFactor;
	private float scaledWidth;
	private Timer imageTimer;
	private Group group;
	private ImageView houseImage;

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
	 * at the specified x and y position, and with specified scale
	 * 
	 * @param sourcefile
	 *            the image source file taken from the XML file
	 * @param xstart
	 *            the horizontal position of the top-left pixel of the image as
	 *            it is displayed on the screen, relative to the resolution of
	 *            the screen
	 * @param ystart
	 *            the vertical position of the top-left pixel of the image as it
	 *            is displayed on the screen, relative to the resolution of the
	 *            screen
	 * @param scale
	 *            a multiplication factor which scales the image i.e. 1 is the
	 *            original image size, 2 doubles the size of the image, 0.5
	 *            halves the size of the image
	 * @param duration
	 *            the length of time that the image should be displayed on the
	 *            screen
	 * @param starttime
	 *            the time the image should be first displayed on the screen
	 * @param specifiedWidth
	 *            the required width of the image which is used to scale the
	 *            image, preserving the aspect ratio
	 */

	public void ImageElement(String sourcefile, ImageType image) {

		xPosition = xResolution * image.xstart;
		yPosition = yResolution * image.ystart;

		// Load Image
		Image house = new Image(sourcefile);
		houseImage = new ImageView(house);

		// Get the width and height of the image
		width = (float) houseImage.getImage().getWidth();
		height = (float) houseImage.getImage().getHeight();

		System.out.println("the original width is: " + width);
		System.out.println("the original height is: " + height);

		// Calculations for scaling the image when the width is specified
		scaleFactor = (image.specifiedWidth / width);
		scaledWidth = (scaleFactor * width);

		houseImage.setFitWidth(((scaledWidth) * image.scale));

		houseImage.setX(xPosition);
		houseImage.setY(yPosition);

		// Preserve the aspect ratio
		houseImage.setPreserveRatio(true);

		// Visual testing
		System.out.println("the scaled width is: " + scaledWidth);
		System.out.println("the scaled height is: " + (scaleFactor * height));
		
		houseImage.setVisible(false);
		group.getChildren().add(houseImage);
		
		imageTimer = new Timer();
		AddImageTask addImage = new AddImageTask(image.duration);
		imageTimer.schedule(addImage, (long)image.starttime * 1000);
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

	
	private class AddImageTask extends TimerTask {

		private float duration;
		
		private AddImageTask(float duration){
			this.duration = duration;
		}
		
		public void run() {
			RemoveImageTask removeImage = new RemoveImageTask();
			
			houseImage.setVisible(true);
			imageTimer.cancel();
			imageTimer = new Timer();
			imageTimer.schedule(removeImage, (long) duration * 1000);
		}
	}

	private class RemoveImageTask extends TimerTask {
		
		public void run() {
			houseImage.setVisible(false);
			imageTimer.cancel();			
		}
	}
}
