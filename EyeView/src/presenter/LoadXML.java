package presenter;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import maps.GoogleMapsPage;
import database.Database;
import database.House;
import database.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import parser.GraphicData;
import parser.ImageData;
import parser.TextData;
import parser.VideoData;
import Button.ButtonType;
import Button.SetupButton;
import Profile.AccountSettings;
import Profile.Login;
import Profile.ProfileViewer;
import Profile.Register;
import Profile.SavedProperties;

public class LoadXML extends Window{

public void loadXMLText() throws IOException {
		
		StackPane textPane = new StackPane();
		textPane.relocate(0, 0);
		textPane.resize(xResolution, yResolution);
		
		TextHandler th = new TextHandler();
		List<TextData> textList = slideData.getTextList();
		
		for (TextData currentText : textList) {
			//System.out.println(currentText.getSource());
			
			if (currentText.getSource().endsWith(".txt")) {
				th.addTextElementByFile(
						currentText.getSource(), currentText.getXstart(),
						currentText.getYstart(), 1,
						Color.web(currentText.getFontColor()),
						new Font(currentText.getFont(), currentText.getFontSize()),
						currentText.getDuration());
			} else {
				th.addTextElement(
						currentText.getSource(), currentText.getXstart(),
						currentText.getYstart(), 1,
						Color.web(currentText.getFontColor()),
						new Font(currentText.getFont(), currentText.getFontSize()),
						currentText.getDuration());
			}
		}
		
		th.display(textPane);
		root.getChildren().add(textPane);
	}
	
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
	
	public void loadXMLImages() {
		
		ImageHandler ih = new ImageHandler();
		List<ImageData> imageList = slideData.getImageList();
		
		for (ImageData currentImage : imageList) {
			ImageElement image = new ImageElement(
					currentImage.getSource(), currentImage.getXstart(),
					currentImage.getYstart(), currentImage.getScale(),
					currentImage.getDuration(), currentImage.getStarttime(),
					0);
			ih.createImage(image);
		}
	}
	
	public void loadXMLVideos() {
		
		
		
		List<VideoData> videoList = slideData.getVideoList();
		
		for (VideoData currentVideo : videoList) {
			StackPane videoPane = new StackPane();
			videoPane.relocate(0, 0);
			videoPane.resize(xResolution, yResolution);
			VideoElement video = new VideoElement(
					currentVideo.getSource());
			video.setWidth(500);
			video.setAutoplay(true);
			video.setXpos(currentVideo.getXstart());
			video.setYpos(currentVideo.getYstart());
			video.display(videoPane);
			root.getChildren().add(videoPane);
		}
		
	}
	
}
