package presenter;

import presenter.SlideContent;
import Button.ButtonType;
import Button.SetupButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class HouseReviews extends presenter.Window{
	Scene asdf;
	GridPane pane;
	TextArea newReviewText, reviewText;
	String review;
	int allRating=3;
	int safetyRating=1;
	int transRating=3;
	int quietRating=4;
	int entertRating=2;
	
	public HouseReviews(){
		pane = new GridPane();

		setupGrid();
		setupButtons();
		setupTitle();
		greyStar();
		allStar();
		safetyStar();
		transStar();
		quietStar();
		entertStar();
		textfield();
		textShow();
		rating();
		root.getChildren().add(pane);
	}
	
	public void setupGrid() {

		pane.setVgap(30);
		pane.setHgap(30);
		pane.relocate(250,200);
	}
	
	public void setupButtons() {

		SlideContent.setupBackButton();
		
		ButtonType button2 = new ButtonType("144,171,199",null,"Submit",60,25);
		Button buttonSubmit = new SetupButton().CreateButton(button2);
		buttonSubmit.relocate(785, 720);
		buttonSubmit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {		        
				 System.out.println(newReviewText.getText());	
				 if(reviewText.getText().length() != 0){
					 reviewText.appendText("\n\n");
				 }
				 reviewText.appendText(newReviewText.getText());
				 newReviewText.clear();
				 
				
		}
		});
						
		root.getChildren().add(buttonSubmit);
		
	}
	
	public void setupTitle() {

		Label Overall = new Label("Overall:");
		Overall.setTextFill(Color.web("#000000"));
		Overall.setFont(new Font(25));
		Overall.relocate(430, 30);
		root.getChildren().add(Overall);
		
		Label Safety = new Label("Safety:");
		Safety.setTextFill(Color.web("#000000"));
		Safety.setFont(new Font(16));
		Safety.relocate(350, 80);
		root.getChildren().add(Safety);
		
		Label Transport = new Label("Transport:");
		Transport.setTextFill(Color.web("#000000"));
		Transport.setFont(new Font(16));
		Transport.relocate(350, 120);
		root.getChildren().add(Transport);
		
	    Label Noise = new Label("Quiet:");
		Noise.setTextFill(Color.web("#000000"));
		Noise.setFont(new Font(16));
		Noise.relocate(350, 160);
		root.getChildren().add(Noise);
		
		Label Entertainment = new Label("Entertainment:");
		Entertainment.setTextFill(Color.web("#000000"));
		Entertainment.setFont(new Font(16));
		Entertainment.relocate(350, 200);
		root.getChildren().add(Entertainment);
				
	} 
	
	public void greyStar(){
		Image starGrey= new Image("grey.png");
		
		ImageView greyOverall= new ImageView();
		greyOverall.setImage(starGrey);
		greyOverall.setFitHeight(40);
		greyOverall.setFitWidth(250);
		greyOverall.relocate(550,20);
		root.getChildren().add(greyOverall);
		
		ImageView greySafety= new ImageView();
		greySafety.setImage(starGrey);
		greySafety.setFitHeight(25);
		greySafety.setFitWidth(150);
		greySafety.relocate(480,80);
		root.getChildren().add(greySafety);
		
		ImageView greyTrans= new ImageView();
		greyTrans.setImage(starGrey);
		greyTrans.setFitHeight(25);
		greyTrans.setFitWidth(150);
		greyTrans.relocate(480,120);
		root.getChildren().add(greyTrans);
		
		ImageView greyQuiet= new ImageView();
		greyQuiet.setImage(starGrey);
		greyQuiet.setFitHeight(25);
		greyQuiet.setFitWidth(150);
		greyQuiet.relocate(480,160);
		root.getChildren().add(greyQuiet);
		
		ImageView greyEntert= new ImageView();
		greyEntert.setImage(starGrey);
		greyEntert.setFitHeight(25);
		greyEntert.setFitWidth(150);
		greyEntert.relocate(480,200);
		root.getChildren().add(greyEntert);
	}
	
	public void allStar(){		
		if (allRating == 1){
		Image star1= new Image("star.png");
		ImageView blue1= new ImageView();
		blue1.setImage(star1);
		blue1.setFitHeight(40);
		blue1.setFitWidth(50);
		blue1.relocate(550,20);
		root.getChildren().add(blue1);
		}
		else if(allRating==2){
		Image star2= new Image("star2.png");
		ImageView blue2= new ImageView();
		blue2.setImage(star2);
		blue2.setFitHeight(40);
		blue2.setFitWidth(100);
		blue2.relocate(550,20);
		root.getChildren().add(blue2);
		}
		else if(allRating==3){
		Image star3= new Image("star3.png");
		ImageView blue3= new ImageView();
		blue3.setImage(star3);
		blue3.setFitHeight(40);
		blue3.setFitWidth(150);
		blue3.relocate(550,20);
		root.getChildren().add(blue3);
		} 
		else if(allRating==4){
		Image star4= new Image("star4.png");
		ImageView blue4= new ImageView();
		blue4.setImage(star4);
		blue4.setFitHeight(40);
		blue4.setFitWidth(200);
		blue4.relocate(550,20);
		root.getChildren().add(blue4);
		} 
		else if(allRating==5){
		Image star5= new Image("star5.png");
		ImageView blue5= new ImageView();
		blue5.setImage(star5);
		blue5.setFitHeight(40);
		blue5.setFitWidth(250);
		blue5.relocate(550,20);
		root.getChildren().add(blue5);
		} 
	}
		
	
	public void safetyStar(){		
		if (safetyRating == 1){
		Image star1= new Image("star.png");
		ImageView blue1= new ImageView();
		blue1.setImage(star1);
		blue1.setFitHeight(25);
		blue1.setFitWidth(30);
		blue1.relocate(480,80);
		root.getChildren().add(blue1);
		}
		else if(safetyRating==2){
		Image star2= new Image("star2.png");
		ImageView blue2= new ImageView();
		blue2.setImage(star2);
		blue2.setFitHeight(25);
		blue2.setFitWidth(60);
		blue2.relocate(480,80);
		root.getChildren().add(blue2);
		}
		else if(safetyRating==3){
		Image star3= new Image("star3.png");
		ImageView blue3= new ImageView();
		blue3.setImage(star3);
		blue3.setFitHeight(25);
		blue3.setFitWidth(90);
		blue3.relocate(480,80);
		root.getChildren().add(blue3);
		} 
		else if(safetyRating==4){
		Image star4= new Image("star4.png");
		ImageView blue4= new ImageView();
		blue4.setImage(star4);
		blue4.setFitHeight(25);
		blue4.setFitWidth(120);
		blue4.relocate(480,80);
		root.getChildren().add(blue4);
		} 
		else if(safetyRating==5){
		Image star5= new Image("star5.png");
		ImageView blue5= new ImageView();
		blue5.setImage(star5);
		blue5.setFitHeight(25);
		blue5.setFitWidth(150);
		blue5.relocate(480,80);
		root.getChildren().add(blue5);
		} 
	}

	public void transStar(){
		if (transRating == 1){
		Image star1= new Image("star.png");
		ImageView blue1= new ImageView();
		blue1.setImage(star1);
		blue1.setFitHeight(25);
		blue1.setFitWidth(30);
		blue1.relocate(480,120);
		root.getChildren().add(blue1);
		}
		else if(transRating==2){
		Image star2= new Image("star2.png");
		ImageView blue2= new ImageView();
		blue2.setImage(star2);
		blue2.setFitHeight(25);
		blue2.setFitWidth(60);
		blue2.relocate(480,120);
		root.getChildren().add(blue2);
		}
		else if(transRating==3){
		Image star3= new Image("star3.png");
		ImageView blue3= new ImageView();
		blue3.setImage(star3);
		blue3.setFitHeight(25);
		blue3.setFitWidth(90);
		blue3.relocate(480,120);
		root.getChildren().add(blue3);
		} 
		else if(transRating==4){
		Image star4= new Image("star4.png");
		ImageView blue4= new ImageView();
		blue4.setImage(star4);
		blue4.setFitHeight(25);
		blue4.setFitWidth(120);
		blue4.relocate(480,120);
		root.getChildren().add(blue4);
		} 
		else if(transRating==5){
		Image star5= new Image("star5.png");
		ImageView blue5= new ImageView();
		blue5.setImage(star5);
		blue5.setFitHeight(25);
		blue5.setFitWidth(150);
		blue5.relocate(480,120);
		root.getChildren().add(blue5);
		} 
	}
	
	public void quietStar(){
		if (quietRating == 1){
		Image star1= new Image("star.png");
		ImageView blue1= new ImageView();
		blue1.setImage(star1);
		blue1.setFitHeight(25);
		blue1.setFitWidth(30);
		blue1.relocate(480,160);
		root.getChildren().add(blue1);
		}
		else if(quietRating==2){
		Image star2= new Image("star2.png");
		ImageView blue2= new ImageView();
		blue2.setImage(star2);
		blue2.setFitHeight(25);
		blue2.setFitWidth(60);
		blue2.relocate(480,160);
		root.getChildren().add(blue2);
		}
		else if(quietRating==3){
		Image star3= new Image("star3.png");
		ImageView blue3= new ImageView();
		blue3.setImage(star3);
		blue3.setFitHeight(25);
		blue3.setFitWidth(90);
		blue3.relocate(480,160);
		root.getChildren().add(blue3);
		} 
		else if(quietRating==4){
		Image star4= new Image("star4.png");
		ImageView blue4= new ImageView();
		blue4.setImage(star4);
		blue4.setFitHeight(25);
		blue4.setFitWidth(120);
		blue4.relocate(480,160);
		root.getChildren().add(blue4);
		} 
		else if(quietRating==5){
		Image star5= new Image("star5.png");
		ImageView blue5= new ImageView();
		blue5.setImage(star5);
		blue5.setFitHeight(25);
		blue5.setFitWidth(150);
		blue5.relocate(480,160);
		root.getChildren().add(blue5);
		}
	} 	
	
	public void entertStar(){
		if (entertRating == 1){
		Image star1= new Image("star.png");
		ImageView blue1= new ImageView();
		blue1.setImage(star1);
		blue1.setFitHeight(25);
		blue1.setFitWidth(30);
		blue1.relocate(480,200);
		root.getChildren().add(blue1);
		}
		else if(entertRating==2){
		Image star2= new Image("star2.png");
		ImageView blue2= new ImageView();
		blue2.setImage(star2);
		blue2.setFitHeight(25);
		blue2.setFitWidth(60);
		blue2.relocate(480,200);
		root.getChildren().add(blue2);
		}
		else if(entertRating==3){
		Image star3= new Image("star3.png");
		ImageView blue3= new ImageView();
		blue3.setImage(star3);
		blue3.setFitHeight(25);
		blue3.setFitWidth(90);
		blue3.relocate(480,200);
		root.getChildren().add(blue3);
		} 
		else if(entertRating==4){
		Image star4= new Image("star4.png");
		ImageView blue4= new ImageView();
		blue4.setImage(star4);
		blue4.setFitHeight(25);
		blue4.setFitWidth(120);
		blue4.relocate(480,200);
		root.getChildren().add(blue4);
		} 
		else if(entertRating==5){
		Image star5= new Image("star5.png");
		ImageView blue5= new ImageView();
		blue5.setImage(star5);
		blue5.setFitHeight(25);
		blue5.setFitWidth(150);
		blue5.relocate(480,200);
		root.getChildren().add(blue5);
		}
	}
    
    public void textfield(){
    	newReviewText = new TextArea();
    	newReviewText.setPromptText("Add your feedback...");
        newReviewText.setPrefSize(500, 100);
        newReviewText.relocate(350, 605);
        root.getChildren().add(newReviewText);
        
        
        newReviewText.setWrapText(true);
        
    } 
    
    public void textShow(){
    	reviewText = new TextArea();
        reviewText.relocate(350, 250);
        reviewText.setPrefSize(500, 300);
        root.getChildren().add(reviewText);
        
        reviewText.setEditable(false);
        reviewText.setWrapText(true);
    }
    
    private final ObservableList strings = FXCollections.observableArrayList(
            "1           ", "2", "3","4", "5");

    public void rating(){
        ComboBox rate1 = ComboBoxBuilder.create()
                .id("uneditable-combobox")
                .promptText("Safety")
                .items(FXCollections.observableArrayList(strings.subList(0, 5))).build();
        rate1.relocate(350, 580);
        root.getChildren().add(rate1);
        
        ComboBox rate2 = ComboBoxBuilder.create()
                .id("uneditable-combobox")
                .promptText("Transport")
                .items(FXCollections.observableArrayList(strings.subList(0, 5))).build(); 
        rate2.relocate(455, 580);
        root.getChildren().add(rate2);
        
        ComboBox rate3 = ComboBoxBuilder.create()
                .id("uneditable-combobox")
                .promptText("Quiet")
                .items(FXCollections.observableArrayList(strings.subList(0, 5))).build(); 
        rate3.relocate(560, 580);
        root.getChildren().add(rate3);
        
        ComboBox rate4 = ComboBoxBuilder.create()
                .id("uneditable-combobox")
                .promptText("Entertainment")
                .items(FXCollections.observableArrayList(strings.subList(0, 5))).build(); 
        rate4.relocate(665, 580);
        root.getChildren().add(rate4); 
        
        
    }
    
}

