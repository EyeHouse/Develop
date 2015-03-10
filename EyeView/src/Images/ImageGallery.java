package Images;

/**
 * Copyright (c) 2008, 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 */

import javafx.scene.Group;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.util.Duration;

/**
 * 
 * A display shelf of images using the PerspectiveTransform effect. 
 * 
 * @see javafx.scene.control.ScrollBar
 * @see javafx.scene.input.MouseEvent
 * @see javafx.scene.input.KeyEvent
 */
public class ImageGallery extends ImagePresenter {
	
	private float xPosition; 		// Horizontal position of image to be displayed
	private float yPosition; 		// Vertical position of image to be displayed
	private static ImageView mainHouseImage; 	// ImageView in which Image is drawn
	private Image[] galleryImages;
	private static final double fitWidth = 400;
	private static final double fitHeight = 300;
	private static final double galleryWidth = 450; 
	private static final double galleryHeight = 300;
	
	public void createImageGallery(ImageElement image) {
		
		Image mainImage = new Image(image.sourcefile);
		mainHouseImage = new ImageView(mainImage);
		
		xPosition = (float) (xResolution * image.xstart);
		yPosition = (float) (yResolution * image.ystart);
		mainHouseImage.setX(xPosition);
		mainHouseImage.setY(yPosition);

		mainHouseImage.setFitWidth(fitWidth);
		mainHouseImage.setFitHeight(fitHeight);
			
		mainHouseImage.setPreserveRatio(true);
		
		galleryImages = new Image[3];
		for (int i = 0; i < 3; i++) {
			galleryImages[i] = new Image("file:house" + (i + 1) + ".jpg", false);
		}

		// create display shelf
		GalleryPictures galleryPictures = new GalleryPictures(galleryImages, image);
		galleryPictures.setPrefSize(galleryWidth, galleryHeight);
		root.getChildren().add(galleryPictures);
		root.getChildren().add(mainHouseImage);
	}
	/**
	 * A ui control which displays a browsable display shelf of images
	 */

	public static class GalleryPictures extends Region {
		private static final Duration DURATION = Duration.millis(500);
		private static final Interpolator INTERPOLATOR = Interpolator.EASE_BOTH;
		private static final double SPACING = 180;
		private static final double scrollbarHeight = 15;
		private SmallHouseImages[] items;

		private Group centered = new Group();
		private Group left = new Group();
		private Group center = new Group();
		private Group right = new Group();
		private int centerIndex = 0;
		private Timeline timeline;
		private ScrollBar scrollBar = new ScrollBar();
		private boolean localChange = false;
		private float mainImageHeight;
		private float xPosition;
		private float yPosition;
		
		
		public GalleryPictures(Image[] images, ImageElement image) {
			
			xPosition = (float) (image.xstart * xResolution);
			yPosition = (float) (image.ystart * yResolution);
			
			// style scroll bar color
			scrollBar.setStyle("-fx-base: #202020; -fx-background: #202020;");
			
			// create items
			items = new SmallHouseImages[images.length];
			for (int i = 0; i < images.length; i++) {
				final SmallHouseImages item = items[i] = new SmallHouseImages(images[i]);
				final double index = i;
				
				item.setOnMouseClicked(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent me) {
						localChange = true;
						scrollBar.setValue(index);
						localChange = false;
						shiftToCenter(item);
					}
				});
			}

			// setup scroll bar
			scrollBar.setMax(items.length - 1);
			scrollBar.setVisibleAmount(1);
			scrollBar.setUnitIncrement(1);
			scrollBar.setBlockIncrement(1);
			scrollBar.valueProperty().addListener(new InvalidationListener() {
				public void invalidated(Observable ov) {
					if (!localChange)
						shiftToCenter(items[(int) scrollBar.getValue()]);
				}
			});

			// create content
			centered.getChildren().addAll(left, right, center);
			getChildren().addAll(centered, scrollBar);

			// update
			update();
		}

		@Override
		protected void layoutChildren() {
			
			// keep centered centered
			centered.setLayoutX(xPosition);
			centered.setLayoutY(fitHeight + yPosition); // Change to main image + amount

			// position scroll bar at bottom
			scrollBar.setLayoutX(xPosition); // change to main image start x position
			scrollBar.setLayoutY(fitHeight + yPosition); // change to small images + amount
			scrollBar.resize(fitWidth, scrollbarHeight);
		}

		private void update() {
			
			// move items to new homes in groups
			left.getChildren().clear();
			center.getChildren().clear();
			right.getChildren().clear();
			for (int i = 0; i < centerIndex; i++) {
				left.getChildren().add(items[i]);
			}

			center.getChildren().add(items[centerIndex]);
			for (int i = items.length - 1; i > centerIndex; i--) {
				right.getChildren().add(items[i]);
			}

			// stop old timeline if there is one running
			if (timeline != null)
				timeline.stop();
			
			// create timeline to animate to new positions
			timeline = new Timeline();
			
			// add keyframes for left items
			final ObservableList<KeyFrame> keyFrames = timeline.getKeyFrames();

			for (int i = 0; i < left.getChildren().size(); i++) {
				final SmallHouseImages it = items[i];
				double newX = -left.getChildren().size() * SPACING + SPACING * i;
				keyFrames.add(new KeyFrame(DURATION, new KeyValue(it.translateXProperty(), newX, INTERPOLATOR)));
			}

			// add keyframe for center item
			final SmallHouseImages centerItem = items[centerIndex];
			keyFrames.add(new KeyFrame(DURATION, new KeyValue(centerItem.translateXProperty(), 0, INTERPOLATOR)));

			// add keyframes for right items
			for (int i = 0; i < right.getChildren().size(); i++) {
				final SmallHouseImages it = items[items.length - i - 1];
				final double newX = right.getChildren().size() * SPACING - SPACING * i;
				keyFrames.add(new KeyFrame(DURATION, new KeyValue(it.translateXProperty(), newX, INTERPOLATOR)));
			}

			// play animation
			timeline.play();
		}

		private void shiftToCenter(SmallHouseImages item) {
			for (int i = 0; i < left.getChildren().size(); i++) {
				if (left.getChildren().get(i) == item) {
					int shiftAmount = left.getChildren().size() - i;
					shift(shiftAmount);
					return;
				}
			}

			if (center.getChildren().get(0) == item) {
				return;
			}

			for (int i = 0; i < right.getChildren().size(); i++) {
				if (right.getChildren().get(i) == item) {
					int shiftAmount = -(right.getChildren().size() - i);
					shift(shiftAmount);
					return;
				}
			}
		}

		public void shift(int shiftAmount) {
			if (centerIndex <= 0 && shiftAmount > 0)
				return;
			if (centerIndex >= items.length - 1 && shiftAmount < 0)
				return;
			centerIndex -= shiftAmount;
			update();
		}
	}

	/**
	 * A Node that displays a image with some 2.5D perspective rotation around
	 * the Y axis.
	 */
	public static class SmallHouseImages extends Parent {
		public SmallHouseImages(Image image) {
			
			//Rectangle2D croppedPortion = new Rectangle2D(x, y, width, height);
			ImageView imageView = new ImageView(image);
			imageView.setFitWidth(150);
			imageView.setPreserveRatio(true);
			getChildren().addAll(imageView);
		}
	}
}
