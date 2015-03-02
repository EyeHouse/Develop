package parser;

/**
 * This class creates a new <code>Image</code> object.
 * It contains the source file path of the image file, the (x,y) position
 * of the top-left corner of the image, the scale of the image, the duration
 * it is displayed on the screen for, and the start time at which the image
 * first appears on the slide.
 *
 * @version 2.0
 * @author  EyeHouse Ltd.
 */
public class Image {

	private String sourcefile;
	private float xstart, ystart, scale, duration, starttime;
	
	/**
	 * Sets the source file location of the image.
	 * @param sourcefile The image's file path
	 */
	public void setSource(String sourcefile) {
		this.sourcefile = sourcefile;
	}
	
	/**
	 * Returns the source file location of the image.
	 * @return The image's file path
	 */
	public String getSource() {
		return sourcefile;
	}
	
	/**
	 * Sets the horizontal position of the top-left corner of the image.
	 * @param xstart Position of the top-left corner
	 */
	public void setXstart(float xstart) {
		this.xstart = xstart;
	}
	
	/**
	 * Returns the horizontal position of the top-left corner of the image.
	 * @return Position of the top-left corner
	 */
	public float getXstart() {
		return xstart;
	}
	
	/**
	 * Sets the vertical position of the top-left corner of the image.
	 * @param ystart Position of the top-left corner
	 */
	public void setYstart(float ystart) {
		this.ystart = ystart;
	}
	
	/**
	 * Returns the vertical position of the top-left corner of the image.
	 * @return Position of the top-left corner
	 */
	public float getYstart() {
		return ystart;
	}
	
	/**
	 * Sets the scale of the image relative to its original size. (default 1)
	 * @param scale Relative scale of image
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	/**
	 * Returns the scale of the image relative to its original size. (default 1)
	 * @return Relative scale of image
	 */
	public float getScale() {
		return scale;
	}
	
	/**
	 * Sets the duration that the image stays visible for on the slide.
	 * (default 0)
	 * @param duration Duration in seconds
	 */
	public void setDuration(float duration) {
		this.duration = duration;
	}
	
	/**
	 * Returns the duration that the image stays visible for on the slide.
	 * (default 0)
	 * @return Duration in seconds
	 */
	public float getDuration() {
		return duration;
	}
	
	/**
	 * Sets the time delay after which the image is first displayed
	 * on the slide. (default 0)
	 * @param starttime Time in seconds
	 */
	public void setStarttime(float starttime) {
		this.starttime = starttime;
	}
	
	/**
	 * Returns the time delay after which the image is first displayed
	 * on the slide. (default 0)
	 * @return Time in seconds
	 */
	public float getStarttime() {
		return starttime;
	}
	
}
