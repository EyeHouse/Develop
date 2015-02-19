/**
 * Description of the class goes here
 *
 * @company  EyeHouse Ltd.
 * @version <version>, <date>
 * @authors <name> & <name>
 */


package parser;

import java.util.ArrayList;
import java.util.List;

public class Slideshow {

    private String title;
    private List<Slide> slides;
	private DocumentInfo info;
	private DefaultSettings defaults;
	
	/* Constructor method */
	
    public Slideshow() {
        slides = new ArrayList<Slide>();
    }

	/* Title get/set functions */
	
	public void setTitle(String title) {
		this.title = title;
	}
    
    public String getTitle() {
        return title;
    }
    
	/* Slide get/set functions */
	
    public void addSlide(Slide slide) {
        this.slides.add(slide);
    }
    
    public List<Slide> getSlides() {
        return slides;
    }

	/* Default Settings get/set functions */
	
	public void setDefaults(DefaultSettings defaults) {
		this.defaults = defaults;
	}
    
	public DefaultSettings getDefaults() {
		return defaults;
	}

	/* Document Info get/set functions */
	
	public void setInfo(DocumentInfo info) {
		this.info = info;
	}
	
	public DocumentInfo getInfo() {
		return info;
	}
	
}
