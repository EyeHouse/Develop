package parser;

/**
 * Description of the class goes here
 *
 * @version 1.6
 * @author EyeHouse Ltd.
 */
public class Text {

	private String sourcefile, font, fontcolor;
	float xstart, ystart, duration;
	int fontsize;

	public void setSource(String sourcefile) {
		this.sourcefile = sourcefile;
	}

	public String getSource() {
		return sourcefile;
	}

	public void setXstart(float xstart) {
		this.xstart = xstart;
	}

	public float getXstart() {
		return xstart;
	}

	public void setYstart(float ystart) {
		this.ystart = ystart;
	}

	public float getYstart() {
		return ystart;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public String getFont() {
		return font;
	}

	public void setFontSize(int fontsize) {
		this.fontsize = fontsize;
	}

	public int getFontSize() {
		return fontsize;
	}

	public void setFontColor(String fontcolor) {
		this.fontcolor = fontcolor;
	}

	public String getFontColor() {
		return fontcolor;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public float getDuration() {
		return duration;
	}

}
