package houses;

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
import presenter.Window;

/**
 * This class groups together the image gallery with rectangles to cover
 * thumbnails that are out of bounds, and set the background of the thumbnails
 * 
 * @version 1.4 15.03.15
 * @author EyeHouse
 * 
 *         Copyright 2015 EyeHouse
 */
public class ImageGallery extends Window {

	private static Group galleryGroup;
	private GalleryPictures galleryPictures;
	public static ArrayList<Image> galleryImages;
	public static Image mainImage;
	public static ImageView mainHouseImage; // ImageView in which Image is drawn

	public static final double thumbnailHeight = 80;
	public static final double thumbnailWidth = 130;
	private static final double mainImageHeight = 300;
	private static final double mainImageWidth = 410;

	/**
	 * Constructor instantiates the image gallery and relocates it to a position
	 * on the screen
	 * 
	 * @param galleryList
	 *            an array list of images for the image gallery
	 * @param xPosition
	 *            the horizontal position of the top-left pixel of the image as
	 *            it is displayed on the screen
	 * @param yPosition
	 *            the vertical position of the top-left pixel of the image as it
	 *            is displayed on the screen
	 */
	public ImageGallery(ArrayList<Image> galleryList, float xPosition,
			float yPosition) {

		galleryImages = galleryList;

		// Set the first image in the array to be the initial main picture
		mainImage = galleryImages.get(0);
		mainHouseImage = new ImageView(mainImage);

		// Set the main image to be the specified dimensions and stretch to fit
		mainHouseImage.setFitWidth(mainImageWidth);
		mainHouseImage.setFitHeight(mainImageHeight);
		mainHouseImage.setPreserveRatio(false);

		// Set the colour behind the thumbnails to be black using a rectangle
		Rectangle thumbnailBackground = new Rectangle();
		thumbnailBackground.setX(0);
		thumbnailBackground.setY(mainImageHeight + 15);
		thumbnailBackground.setWidth(mainImageWidth);
		thumbnailBackground.setHeight(thumbnailHeight);
		thumbnailBackground.setFill(Color.web("#080808"));

		/*
		 * White rectangles cover the thumbnails that go past the edge of the
		 * main image on both sides when the scrollbar moves the thumbnails left
		 * and right
		 */
		Rectangle rightCoverBox = new Rectangle();
		rightCoverBox.setX(mainImageWidth);
		rightCoverBox.setY(mainImageHeight + 15);
		rightCoverBox.setWidth(xResolution - (mainImageWidth + xPosition));
		rightCoverBox.setHeight(thumbnailHeight);
		rightCoverBox.setFill(Color.WHITE);

		Rectangle leftCoverBox = new Rectangle();
		leftCoverBox.setX(-xPosition);
		leftCoverBox.setY(mainImageHeight + 15);
		leftCoverBox.setWidth(xPosition);
		leftCoverBox.setHeight(thumbnailHeight);
		leftCoverBox.setFill(Color.WHITE);

		// Create image gallery
		galleryPictures = new GalleryPictures(galleryImages, mainImage);

		// Create image gallery group containing image gallery & rectangles
		galleryGroup = new Group();
		galleryGroup.getChildren().clear();
		galleryGroup.getChildren().add(thumbnailBackground);
		galleryGroup.getChildren().add(galleryPictures);
		galleryGroup.getChildren().add(rightCoverBox);
		galleryGroup.getChildren().add(leftCoverBox);
		galleryGroup.getChildren().add(mainHouseImage);
		galleryGroup.setLayoutX(xPosition);
		galleryGroup.setLayoutY(yPosition);

		mainImage = null;
	}

	/**
	 * Return the image gallery group
	 */
	public Node getGallery() {
		return galleryGroup;
	}

	/**
	 * This class defines the functionality of the image gallery
	 * 
	 * @version 1.4 15.03.15
	 * @author EyeHouse
	 * 
	 *         Copyright 2015 EyeHouse
	 */
	public static class GalleryPictures extends Region {

		// Time for thumbnails to move to position with the scrollbar
		private static final Duration DURATION = Duration.millis(250);

		// Set the pattern of movement for the thumbnails as the scrollbar moves
		private static final Interpolator INTERPOLATOR = Interpolator.EASE_OUT;

		// Space between the top left corners of the thumbnails
		private static final double SPACING = 140;

		private static final double scrollbarHeight = 15;
		public static final double thumbnailHeight = 80;
		public static final double thumbnailWidth = 130;

		public static ArrayList<Thumbnails> thumbnails;
		private Group centered = new Group();
		private Group left = new Group();
		private Group center = new Group();
		private Group right = new Group();
		private Timeline timeline;
		private ScrollBar scrollBar = new ScrollBar();
		private boolean localChange = false; // Boolean state of change
		private float xPosition;
		private float yPosition;
		private int currentIndex = 1;
		private int centerIndex = 1;

		/**
		 * Constructor creates the content and checks for any changes, and
		 * updates the image positions accordingly
		 * 
		 * @param galleryImages
		 *            an array list of images for the thumbnails
		 * @param mainImage
		 *            the main image of the image gallery
		 */
		public GalleryPictures(final ArrayList<Image> galleryImages,
				final Image mainImage) {

			// Set the appearance of the scroll bar
			scrollBar.setStyle("-fx-base: #202020; -fx-background: #202020;");

			// Create thumbnails
			thumbnails = new ArrayList<Thumbnails>();

			for (int i = 0; i < galleryImages.size(); i++) {

				Thumbnails thumbnail = new Thumbnails(galleryImages.get(i));
				thumbnails.add(thumbnail);
				final int index = i;

				// Define what happens when a thumbnail is clicked
				thumbnails.get(i).setOnMouseClicked(
						new EventHandler<MouseEvent>() {
							public void handle(MouseEvent me) {
								// Pause house advert slideshow
								if (slideID != HOUSE) {
									HouseOverview.setTimerState("PAUSE");
								}

								localChange = true;
								scrollBar.setValue(index - 1);
								localChange = false;
								// Change thumbnail image to main image
								changeImage(index, mainImage);

								if ((index == galleryImages.size() - 1)
										|| (index == galleryImages.size() - 2)) {
									thumbnails.get(galleryImages.size() - 3)
											.setVisible(true);
								}

								if (index != 0
										&& index != (galleryImages.size() - 1)) {
									shiftToCenter(thumbnails.get(index));
									currentIndex = index;
								}
							}
						});
				thumbnail = null;
			}

			// setup scroll bar
			scrollBar.setMax(thumbnails.size() - 3);
			scrollBar.setVisibleAmount(1);
			scrollBar.setUnitIncrement(1);
			scrollBar.setBlockIncrement(1);
			scrollBar.valueProperty().addListener(new InvalidationListener() {
				public void invalidated(Observable ov) {
					if (slideID != HOUSE) {
						HouseOverview.setTimerState("PAUSE");
					}

					if (!localChange && (currentIndex != 0)) {
						shiftToCenter(thumbnails.get((int) (scrollBar
								.getValue() + 1.5)));
					}
					if (!localChange && (currentIndex == 0)) {
						shiftToCenter(thumbnails.get((int) (scrollBar
								.getValue() + 0.5)));
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

			mainHouseImage.setFitWidth(mainImageWidth);
			mainHouseImage.setFitHeight(mainImageHeight);

			mainHouseImage.setPreserveRatio(false);

			galleryGroup.getChildren().add(mainHouseImage);
			mainImage = null;
		}

		@Override
		protected void layoutChildren() {

			if (currentIndex == 0) {
				// keep centered centered
				centered.setLayoutX(xPosition);
			} else if (currentIndex == galleryImages.size() - 1) {
				centered.setLayoutX(xPosition + (mainImageWidth / 2));
			} else {
				centered.setLayoutX((xPosition + (mainImageWidth / 2))
						- (thumbnailWidth / 2));
			}

			// centered.setLayoutX(mainImageWidth/2);
			// Change to main image + amount
			centered.setLayoutY(mainImageHeight + yPosition + scrollbarHeight);

			// position scroll bar at bottom
			scrollBar.setLayoutX(xPosition); // change to main image start x
												// position
			scrollBar.setLayoutY(mainImageHeight + yPosition); // change to
																// small
																// images +
																// amount
			scrollBar.resize(mainImageWidth, scrollbarHeight);
		}

		private void update() {

			// move thumbnails to new homes in groups
			left.getChildren().clear();
			center.getChildren().clear();
			right.getChildren().clear();
			for (int i = 0; i < centerIndex; i++) {
				left.getChildren().add(thumbnails.get(i));
			}

			center.getChildren().add(thumbnails.get(centerIndex));
			for (int i = thumbnails.size() - 1; i > centerIndex; i--) {
				right.getChildren().add(thumbnails.get(i));
			}

			// stop old timeline if there is one running
			if (timeline != null)
				timeline.stop();

			// create timeline to animate to new positions
			timeline = new Timeline();

			// add keyframes for left thumbnails
			final ObservableList<KeyFrame> keyFrames = timeline.getKeyFrames();

			for (int i = 0; i < left.getChildren().size(); i++) {
				final Thumbnails it = thumbnails.get(i);
				double newX = -left.getChildren().size() * SPACING + SPACING
						* i;
				keyFrames.add(new KeyFrame(DURATION, new KeyValue(it
						.translateXProperty(), newX, INTERPOLATOR)));
			}

			// add keyframe for center item
			final Thumbnails centerItem = thumbnails.get(centerIndex);
			keyFrames.add(new KeyFrame(DURATION, new KeyValue(centerItem
					.translateXProperty(), 0, INTERPOLATOR)));

			// add keyframes for right thumbnails
			for (int i = 0; i < right.getChildren().size(); i++) {
				final Thumbnails it = thumbnails.get(thumbnails.size() - i - 1);
				final double newX = right.getChildren().size() * SPACING
						- SPACING * i;
				keyFrames.add(new KeyFrame(DURATION, new KeyValue(it
						.translateXProperty(), newX, INTERPOLATOR)));
			}

			// play animation
			timeline.play();
		}

		private void shiftToCenter(Thumbnails item) {
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
			if (centerIndex >= thumbnails.size() - 1 && shiftAmount < 0)
				return;
			centerIndex -= shiftAmount;
			update();
		}
	}

	/**
	 * A Node that displays a image with some 2.5D perspective rotation around
	 * the Y axis.
	 */
	public static class Thumbnails extends Parent {

		public Thumbnails(Image image) {

			ImageView imageView = new ImageView(image);
			imageView.setFitHeight(GalleryPictures.thumbnailHeight);
			imageView.setFitWidth(GalleryPictures.thumbnailWidth);
			imageView.setPreserveRatio(false);
			getChildren().addAll(imageView);
			image = null;
		}
	}

	public void dispose() {
		mainImage = null;
		mainHouseImage = null;
		galleryImages.clear();
		galleryImages.trimToSize();
		galleryImages = null;
		GalleryPictures.thumbnails.clear();
		GalleryPictures.thumbnails.trimToSize();
		GalleryPictures.thumbnails = null;
		galleryPictures = null;
		galleryGroup = null;
	}

}
