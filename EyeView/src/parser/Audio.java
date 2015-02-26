/**
 * Description of the class goes here
 *
 * @company EyeHouse Ltd.
 * @version 1.5, 26/02/15
 * @authors Peter
 */


package parser;

public class Audio {

	private String sourcefile;
	private float starttime;

	public void setSource(String sourcefile) {
		this.sourcefile = sourcefile;
	}
	
	public String getSource() {
		return sourcefile;
	}
	
	public void setStarttime(float starttime) {
		this.starttime = starttime;
	}

	public float getStarttime() {
		return starttime;
	}

}
