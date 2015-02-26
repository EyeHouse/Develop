/**
 * Description of the class goes here
 *
 * @company EyeHouse Ltd.
 * @version 1.3, 22/02/15
 * @authors Peter
 */


package parser;

public class Image {

	private String sourcefile;
	private float xstart, ystart, scale, duration, starttime;
		
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
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public float getScale() {
		return scale;
	}
	
	public void setDuration(float duration) {
		this.duration = duration;
	}
	
	public float getDuration() {
		return duration;
	}
	
	public void setStarttime(float starttime) {
		this.starttime = starttime;
	}
	
	public float getStarttime() {
		return starttime;
	}
}
