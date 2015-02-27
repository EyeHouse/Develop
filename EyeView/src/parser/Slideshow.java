package parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Description of the class goes here
 *
 * @version 1.3
 * @author EyeHouse Ltd.
 */
public class Slideshow {

    private String title;
    private List<Slide> slides;
	private DocumentInfo info;
	private DefaultSettings defaults;
	
    public Slideshow() {
        slides = new ArrayList<Slide>();
    }
	
	public void setTitle(String title) {
		this.title = title;
	}
    
    public String getTitle() {
        return title;
    }
	
    public void addSlide(Slide slide) {
        this.slides.add(slide);
    }
    
    public List<Slide> getSlides() {
        return slides;
    }
	
	public void setDefaults(DefaultSettings defaults) {
		this.defaults = defaults;
	}
    
	public DefaultSettings getDefaults() {
		return defaults;
	}
	
	public void setInfo(DocumentInfo info) {
		this.info = info;
	}
	
	public DocumentInfo getInfo() {
		return info;
	}
	
}
