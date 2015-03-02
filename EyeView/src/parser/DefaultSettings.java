package parser;

/**
 * This class creates a new object of type <code>DefaultSettings</code>.
 * It contains various default settings for the slideshow, including the
 * background colour, font, font size and font colour to be used as default
 * if the information is not given for a specific object.
 * 
 * @version 2.0
 * @author  EyeHouse Ltd.
 */
public class DefaultSettings {
	
	private String backgroundcolor;
	private String font;
	private int fontsize;
	private String fontcolor;
	
	/**
	 * Sets the default background colour for the slideshow.
	 * @param backgroundColor Default background colour 
	 */
	public void setBackgroundColor(String backgroundcolor) {
		this.backgroundcolor = backgroundcolor;
	}
	
	/**
	 * Returns the default background colour for the slideshow.
	 * @return Default background colour
	 */
	public String getBackgroundColor() {
		return backgroundcolor;
	}
	
	/**
	 * Sets the default font for text in the slideshow.
	 * @param font Default font name
	 */
	public void setFont(String font) {
		this.font = font;
	}
	
	/**
	 * Returns the default font for text in the slideshow.
	 * @return Default font name
	 */
	public String getFont() {
		return font;
	}
	
	/**
	 * Sets the default font size for text in the slideshow.
	 * @param fontSize Default font size in 'pt'
	 */
	public void setFontSize(int fontsize) {
		this.fontsize = fontsize;
	}
	
	/**
	 * Returns the default font size for text in the slideshow.
	 * @return Default font size in 'pt'
	 */
	public int getFontSize() {
		return fontsize;
	}
	
	/**
	 * Sets the font colour for the text.
	 * @param fontcolor Default font colour (8-digit hex)
	 */
	public void setFontColor(String fontcolor) {
		this.fontcolor = fontcolor;
	}
	
	/**
	 * Returns the default font colour for the slideshow.
	 * @return Default font colour (8-digit hex)
	 */
	public String getFontColor() {
		return fontcolor;
	}
	
}
