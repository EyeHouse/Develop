package parser;

/**
 * Description of the class goes here
 *
 * @version 1.5
 * @author EyeHouse Ltd.
 */
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
