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
 * This class implements an audio media object for playback. It may also contain
 * a specific delay time that the sound will play after.
 * 
 * @version 1.0 (20.05.15)
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class AudioHandler {

	private MediaPlayer mediaPlayer = null;

	/**
	 * Plays the target audio file after the defined delay.
	 * 
	 * @param filepath
	 *            File path of target audio
	 * @param delay
	 *            Time delay before playback
	 */
	public void setupAudioElement(String filepath, float delay) {

		/* Create audio file URI string */
		String uriString = new File(filepath).toURI().toString();

		/* Create media object */
		Media audio = new Media(uriString);

		/* Create media player object */
		mediaPlayer = new MediaPlayer(audio);

		/* Play audio immediately if delay set to zero */
		if (delay <= 0) {
			mediaPlayer.play();
		}

		/* Otherwise play audio after defined delay */
		else {
			new Timeline(new KeyFrame(Duration.millis(delay * 1000),
					new EventHandler<ActionEvent>() {
						public void handle(ActionEvent ae) {
							mediaPlayer.play();
						}
					})).play();
		}
	}

	/**
	 * Stops the playback of the audio.
	 */
	public void stopAudio() {
		mediaPlayer.stop();
	}
}
