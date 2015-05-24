package handlers;

import java.io.File;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * This class implements an audio play back function
 * 
 * @version 1.0 20.05.15
 * @author EyeHouse
 *  
 *         Copyright 2015 EyeHouse
 */

public class AudioHandler{

	/**
	 * This method plays the target audio file after the defined delay.
	 * 
	 * @param filepath	File path of target audio
	 * @param delay		Time delay before playback
	 */
	public static void setupAudioElement(String filepath, float delay){
		
		/* Create audio file URI string */
		String uriString = new File(filepath).toURI().toString();
		
		/* Create media object */
		Media audio = new Media(uriString);
		
		/* Create media player object */
		final MediaPlayer mediaPlayer = new MediaPlayer(audio);
		
		/* Play audio immediately if delay set to zero */
		if(delay <= 0){
			mediaPlayer.play();
		}
		
		/* Otherwise play audio after defined delay */
		else
		{
			new Timeline(new KeyFrame(Duration.millis(delay * 1000),
					new EventHandler<ActionEvent>() {
						public void handle(ActionEvent ae) {
							mediaPlayer.play();
						}
					})).play();
		}
	}
}
