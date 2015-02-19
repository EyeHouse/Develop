/**
 * Description of the class goes here
 *
 * @company EyeHouse Ltd.
 * @version <version>, <date>
 * @authors <name> & <name>
 */


package parser;

import java.util.List;

public class Slide {

	private String id;
	private int duration;
	private String title;
	private List<Text> textList;
	private List<Graphic> graphicList;
	private List<Image> imageList;
	private List<Audio> audioList;
	private List<Video> videoList;
	
	/* Slide get/set functions */
	
	public Slide(String id) {
		this.id = id;
	}

	public String getID() {
		return id;
	}

	/* Duration get/set functions */
	
	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getDuration() {
		return duration;
	}

	/* Title get/set functions */
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	/* Text get/set functions */
	
	public void addText(Text text) {
		this.textList.add(text);
	}
	
	public List<Text> getTextList() {
		return textList;
	}

	/* Graphic get/set functions */
	
	public void addGraphic(Graphic graphic) {
		this.graphicList.add(graphic);
	}
	
	public List<Graphic> getGraphicList() {
		return graphicList;
	}

	/* Image get/set functions */
	
	public void addImage(Image image) {
		this.imageList.add(image);
	}
	
	public List<Image> getImageList() {
		return imageList;
	}

	/* Audio get/set functions */
	
	public void addImage(Audio audio) {
		this.audioList.add(audio);
	}
	
	public List<Audio> getAudioList() {
		return audioList;
	}

	/* Video get/set functions */
	
	public void addImage(Video video) {
		this.videoList.add(video);
	}
	
	public List<Video> getVideoList() {
		return videoList;
	}

}
