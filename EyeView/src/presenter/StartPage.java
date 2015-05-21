package presenter;

import java.util.ArrayList;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.text.Font;
import javafx.util.Duration;
import Button.ButtonType;
import Button.SetupButton;

public class StartPage extends Window {

	private Button buttonStart;
	
	private static double rectangleWidth = 280;
	private static double rectangleHeight = 130;
	private static double bannerHeight = 300;
	
	private Font fontMain = new Font(18);
	
	private static ImageView logo;
	public static Image companyLogo;
	
	private Label welcomeMessage1, welcomeMessage2;
	
	private static Timeline scrollTimer;
	private PicturesBanner bannerPictures;
	
	public ArrayList<Image> galleryImages = new ArrayList<Image>();
	public static final double picturesHeight = 80;
	public static final double picturesWidth = 130;
	Color c = Color.rgb(104,158,239,0.3);
	
	public StartPage(ArrayList<Image> galleryList, float xPosition, float yPosition) {
		
		this.galleryImages = galleryList;
		Image image1 = new Image("file:resources/images/palace1.jpg");
		Image image2 = new Image("file:resources/images/palace2.jpg");
		Image image3 = new Image("file:resources/images/palace3.jpg");
		Image image4 = new Image("file:resources/images/palace4.jpg");
		galleryImages.add(image1);
		galleryImages.add(image2);
		galleryImages.add(image3);
		galleryImages.add(image4);
		
		// create display shelf
		bannerPictures = new PicturesBanner(galleryImages);
		bannerPictures.setPrefSize(xResolution, bannerHeight);
		
		root.getChildren().add(bannerPictures);
		createPageElements();
	}
	
	private void createPageElements() {
		
		VBox elementBox = new VBox(12);
		
		//Background colour 
		Rectangle background = new Rectangle();
		background.setWidth(xResolution);
		background.setHeight(yResolution);
		background.setFill(c);
		
		//Create box around start button
		Rectangle centreBox = RectangleBuilder.create()
	              .arcWidth(30)
	              .arcHeight(30)
	              .fill(Color.WHITE)
	              .x(xResolution/2 - rectangleWidth/2)
	              .y(200)
	              .strokeWidth(3)
	              .stroke(Color.BLACK)
	              .build();
		centreBox.setWidth(rectangleWidth);
		centreBox.setHeight(rectangleHeight);
		
		//Create Start Button
		ButtonType button1 = new ButtonType("104,158,239", "104,158,239", "Start", 100,
				30);
		
		buttonStart = new SetupButton().CreateButton(button1);
		
		buttonStart.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				loadSlide(INDEX);
			}		
		});
		
		//Put welcome message above start button
		welcomeMessage1 = new Label ("Welcome to");
		welcomeMessage2 = new Label ("EyeView - York");
		
		welcomeMessage1.setFont(fontMain);
		welcomeMessage2.setFont(fontMain);
		
		//Put EyeHouse logo on page
		companyLogo = new Image("file:resources/images/EyeHouse-Logo.png");

		logo = new ImageView(companyLogo);
		logo.setPreserveRatio(true);
		logo.setFitWidth(250);
			
		logo.relocate(30, 30);
		
		elementBox.setAlignment(Pos.CENTER);
		double elementsPosition = ((xResolution/2) - 65);
		elementBox.relocate(elementsPosition, 210);
		
		elementBox.getChildren().addAll(welcomeMessage1, welcomeMessage2, buttonStart);
		
		root.getChildren().addAll(background, centreBox, elementBox, logo);
		
	}
	
		//Image Slide
	    public static class PicturesBanner extends Region {

	        private static final Duration DURATION = Duration.millis(500);
	        private static final Interpolator INTERPOLATOR = Interpolator.EASE_BOTH;
	        private static final double SPACING = 320;
	        
			private BannerPictures[] items;
	        
	        private Group centered = new Group();
	        private Group left = new Group();
	        private Group center = new Group();
	        private Group right = new Group();
	        private int centerIndex = 0;
	        private Timeline timeline;
	        
	    	public static final double picturesHeight = 80;
	    	public static final double picturesWidth = 130;
	    	private int index;
	        
	        public PicturesBanner(final ArrayList<Image> galleryImages) {	          	           
				  	
	            // create items
	            for (int i=0; i < galleryImages.size(); i++) {
	            	
	            	items[i] = new BannerPictures(galleryImages.get(i));
	            	index = i;
					
					scrollTimer = new Timeline(new KeyFrame(Duration.millis(2 * 1000),
		    				new EventHandler<ActionEvent>() {
		    					public void handle(ActionEvent ae) {
		    						shiftToCenter(items[index]);
			    					index++;
			    				if (index >= galleryImages.size()){
			    						index = 0;
			    				}
		    					}
		    				}));
					scrollTimer.play();
	            }

	            // create content
	            centered.getChildren().addAll(left, right, center);
	            getChildren().add(centered);
	            
	            // update
	            update();
	        }
	        
	        @Override protected void layoutChildren() {
	            
	            // keep centered centered
	            centered.setLayoutY(yResolution - (bannerHeight + 100));
	            centered.setLayoutX(0);
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
	            if (timeline!=null) timeline.stop();

	            // create timeline to animate to new positions
	            timeline = new Timeline();

	            // add keyframes for left items
	            final ObservableList<KeyFrame> keyFrames = timeline.getKeyFrames();
	            for (int i = 0; i < left.getChildren().size(); i++) {

	                final BannerPictures it = items[i];
	                double newX = -left.getChildren().size() *
	                        SPACING + SPACING * i;
	                keyFrames.add(new KeyFrame(DURATION,
	                    new KeyValue(it.translateXProperty(), newX, INTERPOLATOR)));
	            }

	            // add keyframe for center item
	            final BannerPictures centerItem = items[centerIndex];
	            keyFrames.add(new KeyFrame(DURATION,
	                    new KeyValue(centerItem.translateXProperty(), 0, INTERPOLATOR)));

	            // add keyframes for right items
	            for (int i = 0; i < right.getChildren().size(); i++) {
	                final BannerPictures it = items[items.length - i - 1];
	                final double newX = right.getChildren().size() *
	                        SPACING - SPACING * i;
	                keyFrames.add(new KeyFrame(DURATION,
	                        new KeyValue(it.translateXProperty(), newX, INTERPOLATOR)));
	            }

	            // play animation
	            timeline.play();
	        }
	 
	        private void shiftToCenter(BannerPictures item) {
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
	            if (centerIndex <= 0 && shiftAmount > 0) return;
	            if (centerIndex >= items.length - 1 && shiftAmount < 0) return;
	            centerIndex -= shiftAmount;
	            update();
	        }
	    }

	    /**

	     * A Node that displays a image with some 2.5D perspective rotation around the Y axis.

	     */

	    public static class BannerPictures extends Parent {
	        
	        public BannerPictures(Image image) {

	            ImageView imageView = new ImageView(image);
				imageView.setFitHeight(bannerHeight);
				imageView.setFitWidth(xResolution/3);
				imageView.setPreserveRatio(false);
	            getChildren().addAll(imageView);
	        }
	    }
	}
