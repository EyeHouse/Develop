package parser;

import java.util.ArrayList;
import java.util.List;

/**
 * This class creates a new object of type <code>SlideData</code>. It contains
 * the title and duration of a single slide, and lists containing all the
 * <code>TextData</code>, <code>ImageData</code>, <code>AudioData</code>,
 * <code>VideoData</code>, & <code>GraphicData</code> objects on this slide.
 * 
 * @version 2.1
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class SlideData {

	private String title;
	private float duration;
	private List<TextData> textList;
	private List<GraphicData> graphicList;
	private List<ImageData> imageList;
	private List<AudioData> audioList;
	private List<VideoData> videoList;

	/**
	 * Constructor method
	 */
	public SlideData() {
		textList = new ArrayList<TextData>();
		imageList = new ArrayList<ImageData>();
		audioList = new ArrayList<AudioData>();
		videoList = new ArrayList<VideoData>();
		graphicList = new ArrayList<GraphicData>();
	}

	/**
	 * Sets the title of the current slide.
	 * 
	 * @param title
	 *            Slide title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns the title of the current slide.
	 * 
	 * @return Slide title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the duration of the current slide.
	 * 
	 * @param duration
	 *            Slide duration in seconds
	 */
	public void setDuration(float duration) {
		this.duration = duration;
	}

	/**
	 * Returns the duration of the current slide.
	 * 
	 * @return Slide duration in seconds
	 */
	public float getDuration() {
		return duration;
	}

	/**
	 * Adds a <code>TextData</code> object to the current slide's list of
	 * <code>TextData</code> objects.
	 * 
	 * @param text
	 *            New <code>TextData</code> object
	 */
	public void addText(TextData text) {
		this.textList.add(text);
	}

	/**
	 * Returns the list of <code>TextData</code> objects for the current slide.
	 * 
	 * @return List of <code>TextData</code> objects
	 */
	public List<TextData> getTextList() {
		return textList;
	}

	/**
	 * Adds an <code>ImageData</code> object to the current slide's list of
	 * <code>ImageData</code> objects.
	 * 
	 * @param image
	 *            New <code>ImageData</code> object
	 */
	public void addImage(ImageData image) {
		this.imageList.add(image);
	}

	/**
	 * Returns the list of <code>Image</code> objects for the current slide.
	 * 
	 * @return List of <code>Image</code> objects
	 */
	public List<ImageData> getImageList() {
		return imageList;
	}

	/**
	 * Adds an <code>AudioData</code> object to the current slide's list of
	 * <code>Audio</code> objects.
	 * 
	 * @param audio
	 *            New <code>AudioData</code> object
	 */
	public void addAudio(AudioData audio) {
		this.audioList.add(audio);
	}

	/**
	 * Returns the list of <code>AudioData</code> objects for the current slide.
	 * 
	 * @return List of <code>AudioData</code> objects
	 */
	public List<AudioData> getAudioList() {
		return audioList;
	}

	/**
	 * Adds a <code>VideoData</code> object to the current slide's list of
	 * <code>VideoData</code> objects.
	 * 
	 * @param video
	 *            New <code>VideoData</code> object
	 */
	public void addVideo(VideoData video) {
		this.videoList.add(video);
	}

	/**
	 * Returns the list of <code>VideoData</code> objects for the current slide.
	 * 
	 * @return List of <code>VideoData</code> objects
	 */
	public List<VideoData> getVideoList() {
		return videoList;
	}

	/**
	 * Adds a <code>GraphicData</code> object to the current slide's list of
	 * <code>GraphicData</code> objects.
	 * 
	 * @param video
	 *            New <code>GraphicData</code> object
	 */
	public void addGraphic(GraphicData graphic) {
		this.graphicList.add(graphic);
	}

	/**
	 * Returns the list of <code>GraphicData</code> objects for the current
	 * slide.
	 * 
	 * @return List of <code>GraphicData</code> objects
	 */
	public List<GraphicData> getGraphicList() {
		return graphicList;
	}

}
