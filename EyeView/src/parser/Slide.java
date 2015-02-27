package parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Description of the class goes here
 *
 * @version 1.3
 * @author EyeHouse Ltd.
 */
public class Slide {

	private float duration;
	private String title;
	private List<Text> textList;
	private List<Graphic> graphicList;
	private List<Image> imageList;
	private List<Audio> audioList;
	private List<Video> videoList;
	
	/**
	 * Creates a new Slide object with new lists for containing
	 * Text, Image, Audio, Video, & Graphic types.
	 */
	public Slide() {
		textList = new ArrayList<Text>();
		imageList = new ArrayList<Image>();
		audioList = new ArrayList<Audio>();
		videoList = new ArrayList<Video>();
		graphicList = new ArrayList<Graphic>();
	}
	
	/**
	 * Set duration of current slide.
	 * @param duration
	 */
	public void setDuration(float duration) {
		this.duration = duration;
	}

	public float getDuration() {
		return duration;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
	
	public void addText(Text text) {
		this.textList.add(text);
	}
	
	public List<Text> getTextList() {
		return textList;
	}
	
	public void addImage(Image image) {
		this.imageList.add(image);
	}
	
	public List<Image> getImageList() {
		return imageList;
	}
	
	public void addAudio(Audio audio) {
		this.audioList.add(audio);
	}
	
	public List<Audio> getAudioList() {
		return audioList;
	}
	
	public void addVideo(Video video) {
		this.videoList.add(video);
	}
	
	public List<Video> getVideoList() {
		return videoList;
	}
	
	public void addGraphic(Graphic graphic) {
		this.graphicList.add(graphic);
	}
	
	public List<Graphic> getGraphicList() {
		return graphicList;
	}

}
