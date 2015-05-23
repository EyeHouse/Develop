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

/**
 * This class implements the EyeView Start Page
 * 
 * @version 2.2 20.04.15
 * @author EyeHouse
 * 
 * Copyright 2015 EyeHouse
 */
public class StartPage extends Window {

	private Button buttonStart; 					//Start button
	private Font fontMain = new Font(18);			//Set font size for labels
	private static ImageView logo;					//ImageView in which logo image is drawn
	public static Image companyLogo;				//EyeHouse logo
	private Label welcomeMessage1, welcomeMessage2;	//Labels for welcome message (2 lines)
	private static Timeline scrollTimer;			//Timer to scroll pictures banner
	private PicturesBanner bannerPictures;
	
	private static double rectangleWidth = 280;		
	private static double rectangleHeight = 130;
	private static double bannerHeight = 300;
	private Color c = Color.rgb(104,158,239,0.3);	//Colour of background

	/**
	 * Constructor calls the method to instantiate all the page elements
	 */
	public StartPage() {
		
		createPageElements();
	}
	
	/**
	 * Method to instantiate elements of Start Page
	 */
	private void createPageElements() {
		
		//VBox to group the welcome message labels and the start button
		VBox elementBox = new VBox(12);
		
		//Set background colour by filling the screen with a rectangle
		Rectangle background = new Rectangle();
		background.setWidth(xResolution); 	//Width of the screen
		background.setHeight(yResolution);	//Height of the screen
		background.setFill(c);
		
		//Create box around start button
		Rectangle centreBox = RectangleBuilder.create()
	              .arcWidth(30)		//Curved edges
	              .arcHeight(30)
	              .fill(Color.WHITE)
	              .x(xResolution/2 - rectangleWidth/2)
	              .y(200)
	              .strokeWidth(3)
	              .stroke(Color.BLACK)
	              .build();
		centreBox.setWidth(rectangleWidth);
		centreBox.setHeight(rectangleHeight);
		
		//Define start button type
		ButtonType button1 = new ButtonType("104,158,239", "104,158,239", "Start", 100,
				30);
		
		//Create start button
		buttonStart = new SetupButton().CreateButton(button1);
		
		//Set the action of the start button
		buttonStart.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				loadSlide(INDEX);
			}		
		});
		
		//Create welcome message labels (split into two for two lines)
		welcomeMessage1 = new Label ("Welcome to");
		welcomeMessage2 = new Label ("EyeView - York");
		welcomeMessage1.setFont(fontMain);
		welcomeMessage2.setFont(fontMain);
		
		//EyeHouse logo
		companyLogo = new Image("file:resources/images/EyeHouse-Logo.png");
		logo = new ImageView(companyLogo);
		logo.setPreserveRatio(true);
		logo.setFitWidth(250);	
		logo.relocate(30, 30);
		
		//Create VBox containing the welcome message and the start button
		elementBox.setAlignment(Pos.CENTER);
		double elementsPosition = ((xResolution/2) - 65);
		elementBox.relocate(elementsPosition, 210);
		elementBox.getChildren().addAll(welcomeMessage1, welcomeMessage2, buttonStart);
		
		//Create an arraylist of pictures for the pictures banner
		ArrayList<Image> gallery = new ArrayList<Image>();
		Image image1 = new Image("file:resources/start_page_images/House1.JPG");
		Image image2 = new Image("file:resources/start_page_images/House2.jpg");
		Image image3 = new Image("file:resources/start_page_images/House3.jpg");
		Image image4 = new Image("file:resources/start_page_images/House4.jpg");
		Image image5 = new Image("file:resources/start_page_images/House5.jpg");
		Image image6 = new Image("file:resources/start_page_images/House6.jpg");
		Image image7 = new Image("file:resources/start_page_images/House7.jpg");
		Image image8 = new Image("file:resources/start_page_images/House8.jpg");
		Image image9 = new Image("file:resources/start_page_images/House9.jpg");
		Image image10 = new Image("file:resources/start_page_images/House10.jpg");		
		gallery.add(image1);
		gallery.add(image2);
		gallery.add(image3);
		gallery.add(image4);
		gallery.add(image5);
		gallery.add(image6);
		gallery.add(image7);
		gallery.add(image8);
		gallery.add(image9);
		gallery.add(image10);

		//Create pictures banner
		bannerPictures = new PicturesBanner(gallery);
		bannerPictures.setPrefSize(xResolution, bannerHeight);
		
		//Add all elements to the page
		root.getChildren().addAll(background, centreBox, elementBox, logo, bannerPictures);
	}
	
		/**
		 * This class defines the working of the picture banner 
		 */
	    public static class PicturesBanner extends Region {
	    	
	    	//ArrayLost of spaces for the picture banner images
			private ArrayList<BannerPictures> items = new ArrayList<BannerPictures>();
	        private static final Interpolator INTERPOLATOR = Interpolator.EASE_BOTH;
	        
	        //Groups for each section of the picture banner
	        private Group centred = new Group();
	        private Group left = new Group();
	        private Group centre = new Group();
	        private Group right = new Group();
	        
	        //TimeLine for picture banner
	        private Timeline timeline;
	        
	        private int centreIndex = 7;	//Centre picture index
	        private int index = 7;			//Picture index
	        
	        //Time offset of each KeyFrame of the TimeLine
	        private static final Duration DURATION = Duration.millis(500);
	        
	        //Spacing between each image of the picture banner
	        private static final double SPACING = 320;
	        
	    	/**
	    	 * Constructor calls the method to instantiate all the page elements
	    	 * 
	    	 * @param galleryImages
	    	 * 					An ArrayList of images of the picture banner
	    	 */
	        public PicturesBanner(final ArrayList<Image> galleryImages) {	          	           
				  	
	            //Loop to add images to the spaces in the array
	            for (int i=0; i < galleryImages.size(); i++) {
	            	
	            	items.add(new BannerPictures(galleryImages.get(i)));		            
	            }
	            
	            //Timer sets the delay of the shift from picture to picture
	            scrollTimer = new Timeline(new KeyFrame(Duration.millis(0.4 * 1000),
	    				new EventHandler<ActionEvent>() {
	    					public void handle(ActionEvent ae) {
	    						
	    						//Shift picture to the centre of the window
	    						shiftToCentre(items.get(index));
	    						
	    						//Create a continuous loop by adding the first image to
	    						//the end and removing the first image from the start 
	    						if(index == items.size() - 3){
	    							items.add(items.get(0));
		    						items.remove(0);
		    						index = 5;
	    						}
	    						else{
	    							index++;
	    						}
	    					}
	    				}));
	            
	            //Set the timer to continue forever
	            scrollTimer.setCycleCount(Timeline.INDEFINITE);
	            scrollTimer.setAutoReverse(true);
	            scrollTimer.play();

	            //Add items 
	            centred.getChildren().addAll(left, right, centre);
	            getChildren().add(centred);
	            
	            //Update
	            update();
	        }
	        
			/**
			 * Override method to set the position of the picture banner
			 */
	        @Override protected void layoutChildren() {
	            
	            //Keep centred centred
	            centred.setLayoutY(yResolution - (bannerHeight + 100));
	            centred.setLayoutX(0);
	        }
	        
	        /**
			 * Update method sets the positions of the KeyFrames in the TimeLine,
			 * 	stops any old TimeLine and starts a new TimeLine animation
			 */
	        private void update() {

	            //Move items to group positions
	            left.getChildren().clear();
	            centre.getChildren().clear();
	            right.getChildren().clear();

	            for (int i = 0; i < centreIndex; i++) {
	                left.getChildren().add(items.get(i));
	            }

	            centre.getChildren().add(items.get(centreIndex));
	            for (int i = items.size() - 1; i > centreIndex; i--) {
	                right.getChildren().add(items.get(i));
	            }

	            //Stop old TimeLine if there is one running
	            if (timeline!=null) timeline.stop();

	            //Create TimeLine to animate to new positions
	            timeline = new Timeline();

	            //Add KeyFrames for left group item
	            final ObservableList<KeyFrame> keyFrames = timeline.getKeyFrames();
	            for (int i = 0; i < left.getChildren().size(); i++) {

	                final BannerPictures it = items.get(i);
	                double newX = -left.getChildren().size() *
	                        SPACING + SPACING * i;
	                keyFrames.add(new KeyFrame(DURATION,
	                    new KeyValue(it.translateXProperty(), newX, INTERPOLATOR)));
	            }

	            //Add KeyFrame for centre item
	            final BannerPictures centreItem = items.get(centreIndex);
	            keyFrames.add(new KeyFrame(DURATION,
	                    new KeyValue(centreItem.translateXProperty(), 0, INTERPOLATOR)));

	            //Add KeyFrames for right items
	            for (int i = 0; i < right.getChildren().size(); i++) {
	                final BannerPictures it = items.get(items.size() - i - 1);
	                final double newX = right.getChildren().size() *
	                        SPACING - SPACING * i;
	                keyFrames.add(new KeyFrame(DURATION,
	                        new KeyValue(it.translateXProperty(), newX, INTERPOLATOR)));
	            }

	            //Play animation
	            timeline.play();
	        }
	 
	        /**
			 * Method to shift an item to the centre
			 */
	        private void shiftToCentre(BannerPictures item) {
	        	
	        	//Left items
	            for (int i = 0; i < left.getChildren().size(); i++) {
	                if (left.getChildren().get(i) == item) {
	                    int shiftAmount = left.getChildren().size() - i;
	                    shift(shiftAmount);
	                    return;
	                }
	            }

	            //Centre item 
	            if (centre.getChildren().get(0) == item) {
	                return;
	            }

	            //Right items
	            for (int i = 0; i < right.getChildren().size(); i++) {
	                if (right.getChildren().get(i) == item) {
	                    int shiftAmount = -(right.getChildren().size() - i);
	                    shift(shiftAmount);
	                    return;
	                }
	            }
	        }
	 
	        /**
			 * Method to set the amount to shift an item
			 */
	        public void shift(int shiftAmount) {
	            if (centreIndex <= 0 && shiftAmount > 0) return;
	            if (centreIndex >= items.size() - 1 && shiftAmount < 0) return;
	            centreIndex -= shiftAmount;
	            update();
	        }
	    }

		/**
		 * This class sets the ImageView holders for the picture banner images 
		 */
	    public static class BannerPictures extends Parent {
	        
	    	/**
	    	 * Constructor instantiates a holder for an image and populates it 
	    	 * 	with an image
	    	 *
	    	 * @param image
	    	 * 			An image to be added to the picture banner 
	    	 */
	        public BannerPictures(Image image) {

	            ImageView imageView = new ImageView(image);
				imageView.setFitHeight(bannerHeight);
				imageView.setFitWidth(xResolution/3);	//Three images wide
				imageView.setPreserveRatio(false);
	            getChildren().addAll(imageView);
	        }
	    }
	}
