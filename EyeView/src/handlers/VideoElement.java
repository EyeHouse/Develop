package handlers;


import java.io.File;

import javafx.animation.FadeTransitionBuilder;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ParallelTransitionBuilder;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.util.Duration;

/** 
 * Video player for use within a JavaFX StackPane. 
 * 
 * <p>The appearance of the video player can be controlled using JavaFX's CSS 
 * system. For a reference guide, see <a href="http://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html">
 * this</a> page. 
 * 
 * <p>The individual elements are addressed by ID. Here is a list of the IDs:
 * <ul>
 * <li>Background of video player - "video-container" - class Pane</li>
 * <li>Time text - "time-label" - class Text</li>
 * <li>Time slider - "time-slider" - class Slider</li>
 * <li>Volume text - "volume-label" - class Text</li>
 * <li>Volume slider - "volume-slider" - class Slider</li>
 * <li>Play button - "play-btn" - class Button</li>
 * <li>Pause button - "pause-btn" - class Button</li>
 * <li>Stop button - "stop-btn" - class Button</li>
 * <li>Rewind button - "rewind-btn" - class Button</li>
 * </ul>
 * 
 * <p>So to change all button's background hover color, this CSS snuppet can be 
 * used.
 * 
 * <p><code>.button:hover {-fx-background-color: #FF00FF;}</code>
 * 
 * <p>Or to change the volume text color:
 * <p><code>#volume-label {-fx-fill: #FFFFFF;}</code>
 * 
 * @author Joel Fergusson
 *
 */
@SuppressWarnings("deprecation")
public class VideoElement {
	/**
	 * Relative x position (0.0 - 1.0 as a fraction of the width of the parent 
	 * pane) 
	 */
	private double xpos;
	/**
	 * Relative y position (0.0 - 1.0 as a fraction of the height of the parent 
	 * pane) 
	 */
	private double ypos;
	/**
	 * Preferred width of video in pixels.
	 */
	private double prefWidth;
	/**
	 * Preferred width of video (just the video, not the whole player) in pixels
	 */
	private double prefHeight;
	/**
	 * Whether video should play when autoplay is called. Default: false
	 */
	private boolean autoplay;
	/**
	 * Whether automatic sizing should happen based on resolution of video. 
	 * Can be disabled manually, but is also disabled when either setPrefWidth
	 * or setPrefHeight are called.
	 */
	private boolean automaticSizing;
	
	private Media media;
	private MediaPlayer mediaPlayer;
	private MediaView mediaView;
	
	private StackPane containerPane;
	private HBox buttonsPane;
	private HBox timePane;
	
	private Text volumeLabel;
	private Slider volumeSlider;
	private Text timeLabel;
	private Slider timeSlider;
	
	public double currentVideoTime;
	
	
	private ParallelTransition transition = null;
	
	/**
	 * Creates a VideoElement object associated with a file. 
	 * 
	 * @param filename
	 */
	public VideoElement(String filename) {
		
		/* Default values */
		xpos = 0;
		ypos = 0;
		prefWidth = 0;
		prefHeight = 0;
		automaticSizing = true;
		autoplay = false;

		/* Set up video player */
		media = new Media(new File(filename).toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		mediaView = new MediaView(mediaPlayer);
		mediaView.setId("video-container");
		
		/* Time change listener */
		final InvalidationListener mediaTimeListener = 
				new InvalidationListener() {
            public void invalidated(Observable ov) {
                updateValues();
            }
        };
        
        mediaPlayer.currentTimeProperty().addListener(mediaTimeListener);
				
		/* Button event handlers */
        
        //play
		final EventHandler<ActionEvent> playBtnEventHandler = 
				new EventHandler<ActionEvent> () {
			public void handle(ActionEvent e) {
				mediaPlayer.play();
			}
		};

		//pause
		final EventHandler<ActionEvent> pauseBtnEventHandler = 
				new EventHandler<ActionEvent> () {
			public void handle(ActionEvent e) {
				mediaPlayer.pause();
			}
		};

		//stop
		final EventHandler<ActionEvent> stopBtnEventHandler = 
				new EventHandler<ActionEvent> () {
			public void handle(ActionEvent e) {
				mediaPlayer.stop();
			}
		};

		//rewind
		final EventHandler<ActionEvent> rewindBtnEventHandler = 
				new EventHandler<ActionEvent> () {
			public void handle(ActionEvent e) {
				mediaPlayer.seek(Duration.seconds(0));
			}
		};
		
		/* Slider change handlers */
		
		// Volume slider
		final ChangeListener<Number> volumeSliderChangeHandler = 
				new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> 
            observableValue, Number oldVal, Number newVal) {
                    mediaPlayer.setVolume(newVal.intValue() / 100.0);
            }
        };

        // Time Slider
		final ChangeListener<Number> timeSliderChangeHandler = 
				new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> 
            		observableValue, Number oldVal, Number newVal) {
            	/* If it's changed by a considerable value (i.e. if it's not an
            	 * update caused by the slider being moved automatically) then
            	 * seek in the video to the correct point.
            	 */
            	if (Math.abs(newVal.doubleValue() - oldVal.doubleValue()) > 2) {
                    mediaPlayer.seek(new Duration(newVal.intValue() * 1000));	
            	}
            	
            	// Update Time Label
                updateValues();
            }
        };
		
		/* Set up GUI */
		
		/* Add buttons to button pane */
		buttonsPane = new HBox();
		buttonsPane.setId("video-container");
		buttonsPane.setPadding(new Insets(10, 10, 10, 10));
		buttonsPane.setAlignment(Pos.BOTTOM_CENTER);
		buttonsPane.setOpacity(0.0);
		
		buttonsPane.getChildren().addAll(
			ButtonBuilder.create()
				.id("play-btn")
				.text("Play")
				.onAction(playBtnEventHandler)
				.build(),
			ButtonBuilder.create()
				.id("pause-btn")
				.text("Pause")
				.onAction(pauseBtnEventHandler)
				.build(),
			ButtonBuilder.create()
				.id("stop-btn")
				.text("Stop")
				.onAction(stopBtnEventHandler)
				.build(),
			ButtonBuilder.create()
				.id("rewind-btn")
				.text("Rewind")
				.onAction(rewindBtnEventHandler)
				.build()
		);
		
		/* Set up time pane */
		timePane = new HBox();
		timePane.setId("video-container");
		timePane.setPadding(new Insets(10, 10, 10, 10));
		timePane.setSpacing(5);
		timePane.setAlignment(Pos.BASELINE_CENTER);
		timePane.setOpacity(0.0);
		
		// The time label will display the time of the video in h:mm:ss format
		timeLabel = new Text(formatTime(new Duration(0.0)));
		timeLabel.setId("time-label");
		
		timeSlider = new Slider();
		timeSlider.valueProperty().addListener(timeSliderChangeHandler);
		timeSlider.setId("time-slider");
		HBox.setHgrow(timeSlider, Priority.ALWAYS);
		
		volumeLabel = new Text("     Volume:");
		volumeLabel.setId("volume-label");
		
		volumeSlider = new Slider(0, 100, 100);
		volumeSlider.valueProperty().addListener(volumeSliderChangeHandler);
		volumeSlider.setId("volume-slider");
		volumeSlider.setPrefWidth(80);
		
		timePane.getChildren().add(timeLabel);
		timePane.getChildren().add(timeSlider);
		timePane.getChildren().add(volumeLabel);
		timePane.getChildren().add(volumeSlider);
				
		containerPane = new StackPane();
		containerPane.getChildren().add(mediaView);
		containerPane.getChildren().add(timePane);
		containerPane.getChildren().add(buttonsPane);
		
		turnOffPickOnBoundsFor(timePane);
		turnOffPickOnBoundsFor(buttonsPane);
		
		containerPane.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent t) {
                if (transition != null) transition.stop();
                transition = ParallelTransitionBuilder.create()
                    .children(
                        FadeTransitionBuilder.create()
                            .node(timePane)
                            .toValue(1.0)
                            .duration(Duration.millis(200))
                            .interpolator(Interpolator.EASE_OUT)
                            .build(),
                        FadeTransitionBuilder.create()
                            .node(buttonsPane)
                            .toValue(1.0)
                            .duration(Duration.millis(200))
                            .interpolator(Interpolator.EASE_OUT)
                            .build()
                    )
                    .build();
                transition.play();
            }
        });
		containerPane.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent t) {
                if (transition != null) transition.stop();
                transition = ParallelTransitionBuilder.create()
                    .children(
                        FadeTransitionBuilder.create()
                            .node(timePane)
                            .toValue(0.0)
                            .duration(Duration.millis(200))
                            .interpolator(Interpolator.EASE_OUT)
                            .build(),
                        FadeTransitionBuilder.create()
                            .node(buttonsPane)
                            .toValue(0.0)
                            .duration(Duration.millis(200))
                            .interpolator(Interpolator.EASE_OUT)
                            .build()
                    )
                    .build();
                transition.play();
            }
        });
	}
	
	private void turnOffPickOnBoundsFor(Node n) {
		
		n.setPickOnBounds(false);
		if (n instanceof Parent) {
			for (Node c: ((Parent) n).getChildrenUnmodifiable()) {
				turnOffPickOnBoundsFor(c);
			}
		}
	}
	
	/**
	 *  Selects the CSS file that determines the look of the video player.
	 * 
	 * @param filename Location of the css file to be used.
	 */
	public void setStylesheet(String filename) {
		containerPane.getStylesheets().add(
        		new File(filename).toURI().toString());
	}
	
	/**
	 * Updates the slider and time label values based on the media player's time
	 */
	private void updateValues() {
		timeSlider.setValue(mediaPlayer.getCurrentTime().toSeconds());
		
		timeLabel.setText(formatTime(mediaPlayer.getCurrentTime()));

	}

	public String printCurrentVideoTime() {
		currentVideoTime = mediaPlayer.getCurrentTime().toSeconds();
		
		int seconds = (int) Math.floor(mediaPlayer.getCurrentTime().toSeconds() - 
				(Math.floor(mediaPlayer.getCurrentTime().toMinutes()) * 60));
		int minutes = (int) Math.floor(mediaPlayer.getCurrentTime().toMinutes() - 
				(Math.floor(mediaPlayer.getCurrentTime().toHours()) * 60));
		return String.format("%02d:%02d", minutes, seconds);
	}
	
	
	/**
	 * Turns a duration into a string, format "h:mm:ss"
	 * 
	 * @param duration
	 * @return String of format h:mm:ss
	 */
	private String formatTime(Duration duration) {
		
		int seconds = (int) Math.floor(duration.toSeconds() - 
				(Math.floor(duration.toMinutes()) * 60));
		int minutes = (int) Math.floor(duration.toMinutes() - 
				(Math.floor(duration.toHours()) * 60));
		return String.format("%02d:%02d", minutes, seconds);
	}
	
	/**
	 * Display the video player on the specified StackPane
	 * 
	 * @param pane
	 */
	public void display(StackPane pane) {
		
		pane.getChildren().add(containerPane);

		StackPane.setMargin(containerPane, new Insets(
				pane.getHeight() * ypos, 0, 0, pane.getWidth() * xpos));
		
		/* Functions to run when media is ready */
		Runnable mediaPlayerRunnable = new Runnable() {
			@Override
			public void run() {
				timeSlider.setMax(media.getDuration().toSeconds());
				setSize();
				if (autoplay == true) {
					mediaPlayer.play();
				}
			}
		};
		
		mediaPlayer.setOnReady(mediaPlayerRunnable);
	}
	

	/**
	 * Set X position as a proportion of the width of a pane with 0 being 
	 * completely left and 1 being completely right.
	 * 
	 * @param xpos
	 */
	public void setXpos(double xpos) {
		this.xpos = xpos;
	}

	/**
	 * Set Y position as a proportion of the height of a pane with 0 being 
	 * top and 1 being bottom.
	 * 
	 * @param ypos
	 */
	public void setYpos(double ypos) {
		this.ypos = ypos;
	}
	
	/**
	 *  Sets the width 0.0-1.0 as a proportion of the width of the pane. Turns 
	 *  automatic sizing off.
	 * 
	 * @param width
	 * @param pane
	 */
	public void setWidth(double width, StackPane pane) {
		this.prefWidth = pane.getWidth() * width;
		setAutomaticSizing(false);
	}
	
	/**
	 * Sets the height 0.0-1.0 as a proportion of the height of the pane. Turns 
	 * automatic sizing off.
	 * 
	 * @param height
	 * @param pane
	 */
	public void setHeight(double height, StackPane pane) {
		this.prefHeight = pane.getHeight() * height;
		setAutomaticSizing(false);
	}
	
	/**
	 * Sets the absolute width in pixels. Turns automatic sizing off.
	 * 
	 * @param width
	 */
	public void setWidth(double width) {
		this.prefWidth = width;
		setAutomaticSizing(false);
	}
	
	/**
	 * Sets the absolute height in pixels. Turns automatic sizing off.
	 * 
	 * @param height
	 */
	public void setHeight(double height) {
		this.prefHeight = height;
		setAutomaticSizing(false);
	}
	
	/**
	 * Returns whether automatic sizing is enabled for the player
	 * 
	 * @return Automatic Sizing
	 */
	public boolean isAutomaticSizing() {
		return automaticSizing;
	}

	/**
	 * Enables or disables automatic sizing.
	 * 
	 * @param automaticSizing
	 */
	public void setAutomaticSizing(boolean automaticSizing) {
		this.automaticSizing = automaticSizing;
	}

	/**
	 * Configures the javafx elements to have the correct widths and heights 
	 * based on the current settings of the videoElement.
	 */
	private void setSize() {
		
		double width = this.prefWidth;
		double height = this.prefHeight;
		
		// If they're both not 0, allow stretching
		if (width != 0 && height != 0) {
			mediaView.setPreserveRatio(false);
		}
		
		if (automaticSizing == true) {
			if (mediaPlayer.getStatus() != MediaPlayer.Status.UNKNOWN) {
				width = media.getWidth();
			}
			height = 0;
		}
		
		// If height has been specified, set height
		if (height != 0) {
			mediaView.setFitHeight(height);
			if (mediaPlayer.getStatus() != MediaPlayer.Status.UNKNOWN) {
				width = (media.getWidth() * height ) / media.getHeight();
			}
		}
		
		// If width has been specified, set height
		if (width != 0) {
			mediaView.setFitWidth(width);
			containerPane.setMaxWidth(width);
		}
	}

	/**
	 * Enables or disables autoplay (i.e. start playing when display() is 
	 * called).
	 * 
	 * @param autoplay
	 */
	public void setAutoplay(boolean autoplay) {
		this.autoplay = autoplay;
	}

}