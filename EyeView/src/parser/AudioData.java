package parser;

/**
 * This class creates a new <code>AudioData</code> object. It contains the
 * source file path of the audio file, as well as the the start time after which
 * the audio starts playing on the slide.
 * 
 * @version 2.1
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class AudioData {

	private String sourcefile;
	private float starttime;

	/**
	 * Sets the source file location of the audio.
	 * 
	 * @param sourcefile
	 *            The audio's file path
	 */
	public void setSource(String sourcefile) {
		this.sourcefile = sourcefile;
	}

	/**
	 * Returns the source file location of the audio.
	 * 
	 * @return The audio's file path
	 */
	public String getSource() {
		return sourcefile;
	}

	/**
	 * Sets the time delay after which the audio starts playing on the slide.
	 * (default 0)
	 * 
	 * @param starttime
	 *            Time in seconds
	 */
	public void setStarttime(float starttime) {
		this.starttime = starttime;
	}

	/**
	 * Returns the time delay after which the audio starts playing on the slide.
	 * (default 0)
	 * 
	 * @return Time in seconds
	 */
	public float getStarttime() {
		return starttime;
	}

}
