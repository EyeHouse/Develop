/**
 * Description of the class goes here
 *
 * @company EyeHouse Ltd.
 * @version 1.4, 24/02/15
 * @authors Teresa
 */


package parser;

public class Video {

	private String sourcefile, xstart, ystart;
		
	public String getSource() {
		return sourcefile;
	}
	
	public void setSource(String sourcefile) {
		this.sourcefile = sourcefile;
	}
	
	public String getXstart() {
		return xstart;
	}
	
	public void setXstart(String xstart) {
		this.xstart = xstart;
	}
	
	public String getYstart() {
		return ystart;
	}
	
	public void setYstart(String ystart) {
		this.ystart = ystart;
	}

}
