/**
 * Description of the class goes here
 *
 * @company EyeHouse Ltd.
 * @version 1.5, 26/02/15
 * @authors Peter
 */


package parser;

public class Text {

	private String sourcefile, font, fontcolor, duration;
	float xstart, ystart;
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

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getDuration() {
		return duration;
	}

}
