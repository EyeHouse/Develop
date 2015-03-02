package parser;

import java.util.ArrayList;
import java.util.List;

/**
 * This class creates a new object of type <code>Slide</code>.
 * It contains the title and duration of a single slide, and lists containing all the
 * <code>Text</code>, <code>Image</code>, <code>Audio</code>,
 * <code>Video</code>, & <code>Graphic</code> objects on this slide.
 *
 * @version 2.0
 * @author  EyeHouse Ltd.
 */
public class Slide {

	private String title;
	private float duration;
	private List<Text> textList;
	private List<Graphic> graphicList;
	private List<Image> imageList;
	private List<Audio> audioList;
	private List<Video> videoList;
	
	/**
	 * Constructor method
	 */
	public Slide() {
		textList = new ArrayList<Text>();
		imageList = new ArrayList<Image>();
		audioList = new ArrayList<Audio>();
		videoList = new ArrayList<Video>();
		graphicList = new ArrayList<Graphic>();
	}
	
	/**
	 * Sets the title of the current slide.
	 * @param title Slide title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Returns the title of the current slide.
	 * @return Slide title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Sets the duration of the current slide.
	 * @param duration Slide duration in seconds
	 */
	public void setDuration(float duration) {
		this.duration = duration;
	}
	
	/**
	 * Returns the duration of the current slide.
	 * @return Slide duration in seconds
	 */
	public float getDuration() {
		return duration;
	}
	
	/**
	 * Adds a <code>Text</code> object to the current slide's
	 * list of <code>Text</code> objects.
	 * @param text New <code>Text</code> object
	 */
	public void addText(Text text) {
		this.textList.add(text);
	}
	
	/**
	 * Returns the list of <code>Text</code> objects for the current slide.
	 * @return List of <code>Text</code> objects
	 */
	public List<Text> getTextList() {
		return textList;
	}
	
	/**
	 * Adds an <code>Image</code> object to the current slide's
	 * list of <code>Image</code> objects.
	 * @param New <code>Image</code> object
	 */
	public void addImage(Image image) {
		this.imageList.add(image);
	}
	
	/**
	 * Returns the list of <code>Image</code> objects for the current slide.
	 * @return List of <code>Image</code> objects
	 */
	public List<Image> getImageList() {
		return imageList;
	}
	
	/**
	 * Adds an <code>Audio</code> object to the current slide's
	 * list of <code>Audio</code> objects.
	 * @param New <code>Audio</code> object
	 */
	public void addAudio(Audio audio) {
		this.audioList.add(audio);
	}
	
	/**
	 * Returns the list of <code>Audio</code> objects for the current slide.
	 * @return List of <code>Audio</code> objects
	 */
	public List<Audio> getAudioList() {
		return audioList;
	}
	
	/**
	 * Adds a <code>Video</code> object to the current slide's
	 * list of <code>Video</code> objects.
	 * @param New <code>Video</code> object
	 */
	public void addVideo(Video video) {
		this.videoList.add(video);
	}
	
	/**
	 * Returns the list of <code>Video</code> objects for the current slide.
	 * @return List of <code>Video</code> objects
	 */
	public List<Video> getVideoList() {
		return videoList;
	}
	
	/**
	 * Adds a <code>Graphic</code> object to the current slide's
	 * list of <code>Graphic</code> objects.
	 * @param New <code>Graphic</code> object
	 */
	public void addGraphic(Graphic graphic) {
		this.graphicList.add(graphic);
	}
	
	/**
	 * Returns the list of <code>Graphic</code> objects for the current slide.
	 * @return List of <code>Graphic</code> objects
	 */
	public List<Graphic> getGraphicList() {
		return graphicList;
	}

}
