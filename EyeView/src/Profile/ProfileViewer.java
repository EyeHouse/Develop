package Profile;
/*
 * Profile.java
 * 
 * Version: 1.0
 * 
 * Copyright:
 */

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class ProfileViewer{

	public static final int gridCellWidth = 20;
	public static final int gridCellHeight = 30;
	public static final double xStart = 0.5;
	public static final double yStart = 0.1;
	public static final int rating = 1;

    
    public GridPane grid = new GridPane();
    public Group screenGroup;
	public double xResolution;
	public double yResolution;
    
    public Font fontTitle = new Font("Cambria", 24);
	public Font fontMain = new Font("Cambria", 18);
	
	UserType currentUser;
	
	public ProfileViewer(Group group, double xresolution, double yresolution){
		screenGroup = group;
		xResolution = xresolution;
		yResolution = xresolution;
	}
	
	public void OpenProfile(UserType user){
		this.currentUser = user;
		SetupGrid();
		SetupUserInfo();
		SetupStars();
		SetupProfileReview();
		
        screenGroup.getChildren().add(grid);
	}
	
	public void SetupGrid(){
		grid.setPrefWidth(1000);
		
		ColumnConstraints col1 = new ColumnConstraints();
		ColumnConstraints col2 = new ColumnConstraints();
        col1.setMinWidth(250);
        col2.setMinWidth(100);
        grid.getColumnConstraints().addAll(col1,col1,col2);
		
        grid.setHgap(gridCellWidth);
        grid.setVgap(gridCellHeight);
        grid.setPadding(new Insets(yResolution*yStart,xResolution*xStart,100,200));
	}
	
	public void SetupUserInfo(){
		Label labelName, labelEmail, labelDoB, labelType;

		VBox vBoxUserText = new VBox(30);
		Rectangle profilePicture = new Rectangle(200,200, Color.GRAY);
		
		labelName = new Label(currentUser.fName + "\t(" + currentUser.username + ")");
		labelName.setFont(fontTitle);
        labelEmail = new Label("Email: " + currentUser.email);
        labelEmail.setFont(fontMain);
        labelDoB = new Label("Date of Birth: " + currentUser.doB);
        labelDoB.setFont(fontMain);
        
        if(currentUser.landlord){
        	labelType = new Label("Landlord");
        }
        else labelType = new Label("Student");
        labelType.setFont(fontMain);
        vBoxUserText.getChildren().addAll(labelName,labelType, labelEmail,labelDoB);
        grid.addRow(0, profilePicture, vBoxUserText);
	}
	
	public void SetupStars(){
		
		int nbSides = 2*5;
		int r = 15;
        int[] xpoints = new int[nbSides];
        int[] ypoints = new int[nbSides];
        double angle = Math.PI/2;
        Polygon[] star = new Polygon[5];
	    Polyline[] starOutline = new Polyline[5];
		HBox hBoxStars = new HBox(5);

        for (int i=0; i<nbSides; i++) {
            if (i%2==0) {
                xpoints[i] = (int) 0 - (int)((double)r*Math.cos(angle));
                ypoints[i] = (int) 0 - (int)((double)r*Math.sin(angle));
            } else {
                xpoints[i] = (int) 0 - (int)((double)2*r/6*Math.cos(angle));
                ypoints[i] = (int) 0 - (int)((double)2*r/6*Math.sin(angle));
            }
            angle += Math.PI/5;
        }
        for(int i = 0; i < 5 ; i++){
	        star[i] = new Polygon(	xpoints[0], ypoints[0],xpoints[1], ypoints[1],
			       					xpoints[2], ypoints[2],xpoints[3], ypoints[3],
			       					xpoints[4], ypoints[4],xpoints[5], ypoints[5],
			       					xpoints[6], ypoints[6],xpoints[7], ypoints[7],
			       					xpoints[8], ypoints[8],xpoints[9], ypoints[9]);
	        starOutline[i] = new Polyline(	xpoints[0], ypoints[0],xpoints[1], ypoints[1],
											xpoints[2], ypoints[2],xpoints[3], ypoints[3],
											xpoints[4], ypoints[4],xpoints[5], ypoints[5],
											xpoints[6], ypoints[6],xpoints[7], ypoints[7],
											xpoints[8], ypoints[8],xpoints[9], ypoints[9],
											xpoints[0], ypoints[0]);
        }
        
        for(int i = 0 ; i < 5 ; i++){
        	if(i<rating)hBoxStars.getChildren().add(star[i]);
        	else hBoxStars.getChildren().add(starOutline[i]);
        }
        grid.addRow(1, hBoxStars);
	}
	
	public void SetupProfileReview(){
		VBox vBoxProfile = new VBox(10);
		VBox vBoxReview = new VBox(10);
		VBox vBoxNewReview = new VBox(5);
		TextArea textProfile = new TextArea();
		final TextArea textReview = new TextArea();
		final TextArea textNewReview = new TextArea();
		Label labelProfile, labelReview, labelNewReview;
		Button buttonEditProfile = new Button("Edit Profile");
		Button buttonReview = new Button("Submit");
		
		labelProfile = new Label("Profile");
        labelProfile.setFont(fontMain);
        labelReview = new Label("Reviews");
        labelReview.setFont(fontMain);
        labelNewReview = new Label("Add Review");
        labelNewReview.setFont(fontMain);
		
		textProfile.setText(currentUser.profileText);
        textProfile.setEditable(false);
        textProfile.setWrapText(true);
        textReview.setText("Crackin bloke!");
        textReview.setEditable(false);
        textReview.setWrapText(true);
        
        textNewReview.setMaxHeight(50);
        
        buttonEditProfile.setOnAction(new EventHandler<ActionEvent>(){
        	@Override public void handle(ActionEvent event){
        		screenGroup.getChildren().clear();
        		AccountSettings accountSettings = new AccountSettings(screenGroup, xResolution, yResolution);
        		accountSettings.OpenAccountSettings(currentUser);
            }
        });
        
        buttonReview.setOnAction(new EventHandler<ActionEvent>(){
        	@Override public void handle(ActionEvent event){
        		textReview.appendText("\n\n" + textNewReview.getText());
        		textNewReview.clear();
            }
        });
        
        vBoxProfile.getChildren().addAll(labelProfile,textProfile);
        vBoxReview.getChildren().addAll(labelReview,textReview);
        vBoxNewReview.getChildren().addAll(labelNewReview, textNewReview);
        
        grid.addRow(2, vBoxProfile, vBoxReview);
        grid.addRow(3, buttonEditProfile, vBoxNewReview, buttonReview);
        GridPane.setConstraints(buttonEditProfile,0,3,1,1,HPos.CENTER,VPos.CENTER);
        GridPane.setConstraints(buttonReview,2,3,1,1,HPos.CENTER,VPos.CENTER);
	}
}
