package presenter;

import handlers.AudioHandler;
import handlers.GraphicElement;
import handlers.GraphicHandler;
import handlers.ImageElement;
import handlers.ImageHandler;
import handlers.TextHandler;
import handlers.VideoElement;

import java.io.IOException;
import java.util.List;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import parser.AudioData;
import parser.GraphicData;
import parser.ImageData;
import parser.TextData;
import parser.VideoData;

public class LoadXML extends Window {

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

		for (VideoData currentVideo : videoList) {
			StackPane videoPane = new StackPane();
			videoPane.relocate(0, 0);
			videoPane.resize(xResolution, yResolution);
			VideoElement video = new VideoElement(currentVideo.getSource(),
					false);
			video.setWidth(500);
			video.setAutoplay(true);
			video.setXpos(currentVideo.getXstart());
			video.setYpos(currentVideo.getYstart());
			video.display(videoPane);
			video.setStylesheet("resources/videoStyle.css");
			root.getChildren().add(videoPane);
		}
	}

	/**
	 * Loads all the audio data from the XML.
	 */
	public void loadXMLAudio() {

		List<AudioData> audioList = slideData.getAudioList();

		for (AudioData currentAudio : audioList) {
			AudioHandler.setupAudioElement(currentAudio.getSource(),
					currentAudio.getStarttime());
		}
	}

}
