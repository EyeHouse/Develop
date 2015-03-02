package parser;

/**
 * This class creates a new <code>Video</code> object.
 * It contains the source file path of the video file, and the (x,y) position
 * of the top-left corner of the video frame.
 * 
 * @version 2.0
 * @author  EyeHouse Ltd.
 */
public class Video {
	
	private String sourcefile;
	private float xstart, ystart;
	
	/**
	 * Sets the source file location of the video.
	 * @param sourcefile The video's file path
	 */
	public void setSource(String sourcefile) {
		this.sourcefile = sourcefile;
	}
	
	/**
	 * Returns the source file location of the video.
	 * @return The video's file path
	 */
	public String getSource() {
		return sourcefile;
	}
	
	/**
	 * Sets the horizontal position of the top-left corner of the video.
	 * @param xstart Position of the top-left corner
	 */
	public void setXstart(float xstart) {
		this.xstart = xstart;
	}
	
	/**
	 * Returns the horizontal position of the top-left corner of the video.
	 * @return Position of the top-left corner
	 */
	public float getXstart() {
		return xstart;
	}
	
	/**
	 * Sets the vertical position of the top-left corner of the video.
	 * @param ystart Position of the top-left corner
	 */
	public void setYstart(float ystart) {
		this.ystart = ystart;
	}
	
	/**
	 * Returns the vertical position of the top-left corner of the video.
	 * @return Position of the top-left corner
	 */
	public float getYstart() {
		return ystart;
	}
	
}
