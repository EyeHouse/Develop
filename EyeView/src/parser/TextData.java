package parser;

/**
 * This class creates a new <code>TextData</code> object. It contains the source
 * of the text which may be sourced from a text file or from within in the XML,
 * the (x,y) position of the top-left corner of the text box, the font of the
 * text, as well as the font colour and size, and the duration it is displayed
 * on the screen for.
 * 
 * @version 2.1
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class TextData {

	private String sourcefile, font, fontcolor;
	float xstart, ystart, duration;
	int fontsize;

	/**
	 * Sets the source of the text. (default from text file)
	 * 
	 * @param sourcefile
	 *            The rich text content
	 */
	public void setSource(String sourcefile) {
		this.sourcefile = sourcefile;
	}

	/**
	 * Returns the source of the text. (default from text file)
	 * 
	 * @return The rich text content
	 */
	public String getSource() {
		return sourcefile;
	}

	/**
	 * Sets the horizontal position of the top-left corner of the text box.
	 * 
	 * @param xstart
	 *            Position of the top-left corner
	 */
	public void setXstart(float xstart) {
		this.xstart = xstart;
	}

	/**
	 * Returns the horizontal position of the top-left corner of the text box.
	 * 
	 * @return Position of the top-left corner
	 */
	public float getXstart() {
		return xstart;
	}

	/**
	 * Sets the vertical position of the top-left corner of the text box.
	 * 
	 * @param ystart
	 *            Position of the top-left corner
	 */
	public void setYstart(float ystart) {
		this.ystart = ystart;
	}

	/**
	 * Returns the vertical position of the top-left corner of the text box.
	 * 
	 * @return Position of the top-left corner
	 */
	public float getYstart() {
		return ystart;
	}

	/**
	 * Sets the font for the text. (default from <code>DefaultSettings</code>)
	 * 
	 * @param font
	 *            Font name
	 */
	public void setFont(String font) {
		this.font = font;
	}

	/**
	 * Returns the font for the text. (default from <code>DefaultSettings</code>
	 * )
	 * 
	 * @return Font name
	 */
	public String getFont() {
		return font;
	}

	/**
	 * Sets the font size for the text. (default from
	 * <code>DefaultSettings</code>)
	 * 
	 * @param fontsize
	 *            Font size in 'pt'
	 */
	public void setFontSize(int fontsize) {
		this.fontsize = fontsize;
	}

	/**
	 * Returns the font size for the text. (default from
	 * <code>DefaultSettings</code>)
	 * 
	 * @return Font size in 'pt'
	 */
	public int getFontSize() {
		return fontsize;
	}

	/**
	 * Sets the font colour for the text. (default from
	 * <code>DefaultSettings</code>)
	 * 
	 * @param fontcolor
	 *            Font colour (8-digit hex)
	 */
	public void setFontColor(String fontcolor) {
		this.fontcolor = fontcolor;
	}

	/**
	 * Returns the font colour for the text. (default from
	 * <code>DefaultSettings</code>)
	 * 
	 * @return Font colour (8-digit hex)
	 */
	public String getFontColor() {
		return fontcolor;
	}

	/**
	 * Sets the duration that the image stays visible for on the slide. (default
	 * 0)
	 * 
	 * @param duration
	 *            Duration in seconds
	 */
	public void setDuration(float duration) {
		this.duration = duration;
	}

	/**
	 * Returns the duration that the image stays visible for on the slide.
	 * (default 0)
	 * 
	 * @return Duration in seconds
	 */
	public float getDuration() {
		return duration;
	}

}
