/**
 * Description of the class goes here
 *
 * @company EyeHouse Ltd.
 * @version 1.4, 24/02/15
 * @authors Teresa
 */


package parser;

public class Video {

	private String sourcefile;
	private float xstart, ystart;
	
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

}
