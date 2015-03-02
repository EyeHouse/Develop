package parser;

import java.util.ArrayList;
import java.util.List;

/**
 * This class creates a new object of type <code>Slideshow</code>.
 * It contains the slideshow's title, holds all the document information and default settings,
 * and a list containing all the <code>Slide</code> objects.
 * 
 * @version 2.0
 * @author  EyeHouse Ltd.
 */
public class Slideshow {
	
    private String title;
	private DocumentInfo info;
	private DefaultSettings defaults;
    private List<Slide> slideList;
	
	/**
	 * Constructor method
	 */
    public Slideshow() {
        slideList = new ArrayList<Slide>();
    }
    
	/**
	 * Sets the title of the slideshow.
	 * @param title Slideshow's title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Returns the title of the slideshow.
	 * @return Slideshow's title
	 */
    public String getTitle() {
        return title;
    }
    
	/**
	 * Sets the default settings for the slideshow.
	 * @param defaults Slideshow's default settings
	 */
	public void setDefaults(DefaultSettings defaults) {
		this.defaults = defaults;
	}
	
	/**
	 * Returns the slideshow's default settings.
	 * @return Slideshow's default settings
	 */
	public DefaultSettings getDefaults() {
		return defaults;
	}

	/**
	 * Sets the document information for the slideshow.
	 * @param info Document information for the slideshow
	 */
	public void setInfo(DocumentInfo info) {
		this.info = info;
	}

	/**
	 * Returns the information about the slideshow document.
	 * @return Document information for the slideshow
	 */
	public DocumentInfo getInfo() {
		return info;
	}

	/**
	 * Adds a slide to the slideshow's list of
	 * <code>Slide</code> objects.
	 * @param slide New <code>Slide</code> object
	 */
    public void addSlide(Slide slide) {
        this.slideList.add(slide);
    }

	/**
	 * Returns the list of all the slides stored in
	 * the <code>Slideshow</code> object.
	 * @return List of <code>Slide</code> objects
	 */
    public List<Slide> getSlides() {
        return slideList;
    }
	
}
