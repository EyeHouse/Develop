package handlers;

/**
 * This class contains all a the data values required to place an image on the
 * screen at a specified x and y position, with a specified scale, and displays
 * it at a specified time and for a specified duration.
 * 
 * @version 1.2 (05.03.15)
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class ImageElement {

	// Data variables for the image
	public String sourcefile;
	public float xstart;
	public float ystart;
	public float scale;
	public float duration;
	public float starttime;
	public float specifiedWidth;

	/**
	 * Constructor method for ImageElement object.
	 * 
	 * @param sourcefile
	 *            the filename of the image
	 * @param xstart
	 *            the horizontal position of the top-left of the image, relative
	 *            to the resolution of the screen
	 * @param ystart
	 *            the vertical position of the top-left of the image, relative
	 *            to the resolution of the screen
	 * @param scale
	 *            scalar factor which scales the image size (default 1)
	 * @param duration
	 *            the length of time that the image should be displayed on the
	 *            screen
	 * @param starttime
	 *            the time the image should be first displayed on the screen
	 * @param specifiedWidths
	 *            the required width of the image which is used to scale the
	 *            image, preserving the aspect ratio
	 */
	public ImageElement(String sourcefile, float xstart, float ystart,
			float scale, float duration, float starttime, float specifiedWidth) {

		this.sourcefile = sourcefile;
		this.xstart = xstart;
		this.ystart = ystart;
		this.scale = scale;
		this.duration = duration;
		this.starttime = starttime;
		this.specifiedWidth = specifiedWidth;
	}
}
