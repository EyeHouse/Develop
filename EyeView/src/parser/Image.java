/**
 * Description of the class goes here
 *
 * @company EyeHouse Ltd.
 * @version <version>, <date>
 * @authors <name> & <name>
 */


package parser;

public class Image {

	private String sourcefile, xstart, ystart, scale, duration, starttime;
		
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
	
	public String getScale() {
		return scale;
	}
	
	public void setScale(String scale) {
		this.scale = scale;
	}
	
	public String getDuration() {
		return duration;
	}
	
	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	public String getStarttime() {
		return starttime;
	}
	
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
}
