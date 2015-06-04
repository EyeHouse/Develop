package presenter;

import handlers.AudioHandler;
import handlers.GraphicElement;
import handlers.GraphicHandler;
import handlers.ImageElement;
import handlers.ImageHandler;
import handlers.TextHandler;
import handlers.VideoElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import parser.AudioData;
import parser.GraphicData;
import parser.ImageData;
import parser.TextData;
import parser.VideoData;

/**
 * This class is used to load all the objects from an XML file.
 * 
 * @version 3.8 (02.06.15)
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class LoadXML extends Window {

	private static ArrayList<VideoElement> slideVideos = new ArrayList<VideoElement>();
	private static ArrayList<AudioHandler> slideAudio = new ArrayList<AudioHandler>();

	/**
	 * Constructor method
	 * 
	 * @throws IOException
	 */
	public LoadXML() throws IOException {

		loadXMLBackGround();
		loadXMLText();
		loadXMLGraphics();
		loadXMLImages();
		loadXMLVideos();
		loadXMLAudio();
	}

	/**
	 * Loads the text for the slide onto the screen. Refers to defaults if
	 * settings have not been set.
	 * 
	 * @throws IOException
	 */
	public void loadXMLText() throws IOException {

		List<TextData> textList = slideData.getTextList();

		for (TextData currentText : textList) {
			TextHandler th = new TextHandler();
			StackPane textPane = new StackPane();
			textPane.relocate(0, 0);
			textPane.resize(xResolution, yResolution);

			if (currentText.getSource().endsWith(".txt")) {
				th.addTextElementByFile(
						currentText.getSource(),
						currentText.getXstart(),
						currentText.getYstart(),
						1,
						Color.web(currentText.getFontColor()),
						new Font(currentText.getFont(), currentText
								.getFontSize()), currentText.getDuration());
			} else {
				th.addTextElement(
						currentText.getSource(),
						currentText.getXstart(),
						currentText.getYstart(),
						1,
						Color.web(currentText.getFontColor()),
						new Font(currentText.getFont(), currentText
								.getFontSize()), currentText.getDuration());
			}
			th.display(textPane);
			root.getChildren().add(textPane);
		}
	}

	/**
	 * Loads the background colour from the XML.
	 */
	public void loadXMLBackGround() {

		GraphicHandler gh = new GraphicHandler();
		GraphicElement graphic = new GraphicElement("rectangle", 0, 0,
				(float) xResolution, (float) yResolution, 0, true, slideshow
						.getDefaults().getBackgroundColor(), null);
		gh.addShapeToCanvas(graphic);
	}

	/**
	 * Loads all the shapes and other graphics data from the XML.
	 */
	public void loadXMLGraphics() {

		GraphicHandler gh = new GraphicHandler();
		List<GraphicData> graphicList = slideData.getGraphicList();

		for (GraphicData currentGraphic : graphicList) {
			GraphicElement graphic = new GraphicElement(
					currentGraphic.getType(), currentGraphic.getXstart(),
					currentGraphic.getYstart(), currentGraphic.getXend(),
					currentGraphic.getYend(), currentGraphic.getDuration(),
					currentGraphic.isSolid(), currentGraphic.getGraphicColor(),
					currentGraphic.getShadingColor());
			gh.addShapeToCanvas(graphic);
		}
	}

	/**
	 * Loads all the images data from the XML.
	 */
	public void loadXMLImages() {

		ImageHandler ih = new ImageHandler();
		List<ImageData> imageList = slideData.getImageList();

		for (ImageData currentImage : imageList) {
			ImageElement image = new ImageElement(currentImage.getSource(),
					currentImage.getXstart(), currentImage.getYstart(),
					currentImage.getScale(), currentImage.getDuration(),
					currentImage.getStarttime(), 0);
			ih.createImage(image);
		}
	}

	/**
	 * Loads all the videos data from the XML.
	 */
	public void loadXMLVideos() {

		List<VideoData> videoList = slideData.getVideoList();
		int i = 0;
		for (VideoData currentVideo : videoList) {
			StackPane videoPane = new StackPane();
			videoPane.relocate(0, 0);
			videoPane.resize(xResolution, yResolution);
			slideVideos.add(new VideoElement(currentVideo.getSource(), true));
			slideVideos.get(i).setWidth(500);
			slideVideos.get(i).setAutoplay(true);
			slideVideos.get(i).setXpos(currentVideo.getXstart());
			slideVideos.get(i).setYpos(currentVideo.getYstart());
			slideVideos.get(i).display(videoPane);
			slideVideos.get(i).setStylesheet("resources/videoStyle.css");
			root.getChildren().add(videoPane);
			i++;
		}
	}

	/**
	 * Loads all the audio data from the XML.
	 */
	public void loadXMLAudio() {

		List<AudioData> audioList = slideData.getAudioList();
		int i = 0;
		for (AudioData currentAudio : audioList) {
			slideAudio.add(new AudioHandler());
			slideAudio.get(i).setupAudioElement(currentAudio.getSource(),
					currentAudio.getStarttime());
			i++;
		}
	}

	/**
	 * Stops any video and audio currently playing when the slide is changed.
	 */
	public static void stopMedia() {

		for (int i = 0; i < slideVideos.size(); i++) {
			slideVideos.get(i).stopVideo();
		}
		for (int i = 0; i < slideAudio.size(); i++) {
			slideAudio.get(i).stopAudio();
		}
		slideVideos.clear();
		slideAudio.clear();
	}

}
