package presenter;


import java.util.ArrayList;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ImageGallery extends Window {
	
	private static Group galleryGroup;
	public static ImageView mainHouseImage; // ImageView in which Image is drawn
	public static ArrayList<Image> galleryImages;
	private static final double fitWidth = 410; // Make dependent on input image.
	public static final double smallHouseHeight = 80;
	public static final double smallHouseWidth = 130;
	private static final double fitHeight = 300; // Make dependent on input image.
	public static Image mainImage;

	public ImageGallery(ArrayList<Image> galleryList, float xPosition, float yPosition) {
		
		galleryImages = galleryList;
		
		mainImage = galleryImages.get(0);
		mainHouseImage = new ImageView(mainImage);
		
		mainHouseImage.setFitWidth(fitWidth);
		mainHouseImage.setFitHeight(fitHeight);
		mainHouseImage.setPreserveRatio(false);
		
		Rectangle rect1 = new Rectangle();
		rect1.setX(fitWidth);
		rect1.setY(fitHeight+15);
		rect1.setWidth(xResolution - (fitWidth + xPosition));
		rect1.setHeight(smallHouseHeight);
		rect1.setFill(Color.WHITE);
		
		Rectangle rect2 = new Rectangle();
		rect2.setX(-xPosition);
		rect2.setY(fitHeight+15);
		rect2.setWidth(xPosition);
		rect2.setHeight(smallHouseHeight);
		rect2.setFill(Color.WHITE);
		
		// create display shelf
		GalleryPictures galleryPictures = new GalleryPictures(galleryImages, mainImage);
		
		galleryGroup = new Group();
		galleryGroup.getChildren().clear();
		galleryGroup.getChildren().add(galleryPictures);
		galleryGroup.getChildren().add(rect1);
		galleryGroup.getChildren().add(rect2);
		galleryGroup.getChildren().add(mainHouseImage);
		galleryGroup.setLayoutX(xPosition);
		galleryGroup.setLayoutY(yPosition);
	}
	
	public Node getGallery() {
		return galleryGroup;
	}

	public static void setYPosition(float yPosition) {
		
	}

	/**
	 * A UI control which displays a browsable display shelf of images
	 */
	public static class GalleryPictures extends Region {
		private static final Duration DURATION = Duration.millis(250);

		private static final Interpolator INTERPOLATOR = Interpolator.EASE_OUT;
		private static final double SPACING = 140;
		private static final double scrollbarHeight = 15;
		public static final double smallHouseHeight = 80;
		public static final double smallHouseWidth = 130;

		private SmallHouseImages[] items;

		private Group centered = new Group();
		private Group left = new Group();
		private Group center = new Group();
		private Group right = new Group();
		private int centerIndex = 1;
		private Timeline timeline;
		private ScrollBar scrollBar = new ScrollBar();
		private boolean localChange = false;
		private float xPosition;
		private float yPosition;
		private int currentIndex = 1;

		public GalleryPictures(final ArrayList<Image> galleryImages, final Image mainImage) {

			// style scroll bar color
			scrollBar.setStyle("-fx-base: #202020; -fx-background: #202020;");

			// create items
			items = new SmallHouseImages[galleryImages.size()];

			for (int i = 0; i < galleryImages.size(); i++) {

				items[i] = new SmallHouseImages(galleryImages.get(i));
				final int index = i;

				items[i].setOnMouseClicked(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent me) {
						HousePages.setTimerState("PAUSE");
						
						localChange = true;
						scrollBar.setValue(index - 1);
						localChange = false;
						changeImage(index, mainImage);

						if ((index == galleryImages.size() - 1)
								|| (index == galleryImages.size() - 2)) {
							items[galleryImages.size() - 3].setVisible(true);
						}

						if (index != 0 && index != (galleryImages.size() - 1)) {
							shiftToCenter(items[index]);
							currentIndex = index;
						}
					}
				});
			}

			// setup scroll bar
			scrollBar.setMax(items.length - 3);
			scrollBar.setVisibleAmount(1);
			scrollBar.setUnitIncrement(1);
			scrollBar.setBlockIncrement(1);
			scrollBar.valueProperty().addListener(new InvalidationListener() {
				public void invalidated(Observable ov) {
					HousePages.setTimerState("PAUSE");
					if (!localChange && (currentIndex != 0)){
						shiftToCenter(items[(int) (scrollBar.getValue() + 1.5)]);
					}
					if (!localChange && (currentIndex == 0)){
						shiftToCenter(items[(int) (scrollBar.getValue() + 0.5)]);
					}
				}
			});

			// create content
			centered.getChildren().addAll(left, right, center);
			getChildren().addAll(centered, scrollBar);

			// update
			update();
		}

		public void changeImage(int i, Image mainImage) {

			galleryGroup.getChildren().remove(mainHouseImage);

			mainImage = galleryImages.get(i);
			mainHouseImage.setImage(mainImage);
			
			mainHouseImage.setX(xPosition);
			mainHouseImage.setY(yPosition);

			mainHouseImage.setFitWidth(fitWidth);
			mainHouseImage.setFitHeight(fitHeight);

			mainHouseImage.setPreserveRatio(false);

			galleryGroup.getChildren().add(mainHouseImage);
		}

		@Override
		protected void layoutChildren() {

			if (currentIndex == 0) {
				// keep centered centered
				centered.setLayoutX(xPosition);
			} else if (currentIndex == galleryImages.size() - 1) {
				// centered.setLayoutX((SPACING * 2.5) + (SPACING -
				// smallHouseWidth));
				centered.setLayoutX(xPosition + (fitWidth / 2));
			} else {
				// centered.setLayoutX((fitWidth/2) + (1.5 * (SPACING -
				// smallHouseWidth)));
				centered.setLayoutX((xPosition + (fitWidth / 2))
						- (smallHouseWidth / 2));
			}

			// centered.setLayoutX(fitWidth/2);
			// Change to main image + amount
			centered.setLayoutY(fitHeight + yPosition + scrollbarHeight); 

			// position scroll bar at bottom
			scrollBar.setLayoutX(xPosition); // change to main image start x
												// position
			scrollBar.setLayoutY(fitHeight + yPosition); // change to small
															// images + amount
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
				double newX = -left.getChildren().size() * SPACING + SPACING
						* i;
				keyFrames.add(new KeyFrame(DURATION, new KeyValue(it
						.translateXProperty(), newX, INTERPOLATOR)));
			}

			// add keyframe for center item
			final SmallHouseImages centerItem = items[centerIndex];
			keyFrames.add(new KeyFrame(DURATION, new KeyValue(centerItem
					.translateXProperty(), 0, INTERPOLATOR)));

			// add keyframes for right items
			for (int i = 0; i < right.getChildren().size(); i++) {
				final SmallHouseImages it = items[items.length - i - 1];
				final double newX = right.getChildren().size() * SPACING
						- SPACING * i;
				keyFrames.add(new KeyFrame(DURATION, new KeyValue(it
						.translateXProperty(), newX, INTERPOLATOR)));
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

			ImageView imageView = new ImageView(image);
			imageView.setFitHeight(GalleryPictures.smallHouseHeight);
			imageView.setFitWidth(GalleryPictures.smallHouseWidth);
			imageView.setPreserveRatio(false);
			getChildren().addAll(imageView);
		}
	}

}
