/*
 * Profile.java
 * 
 * Version: 1.0
 * 
 * Copyright:
 */

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class ProfileViewer extends Application{

	class User {
		public String fName;
		public String lName;
		public String email;
		public String username;
		public boolean landlord;
		public String password;
		public String doB;
		public boolean admin;
		
		public User(String fName, String lName , String email, String username,
					boolean landlord, String password, String doB, boolean admin){
			this.fName = fName;
			this.lName = lName;
			this.email = email;
			this.username = username;
			this.landlord = landlord;
			this.password = password;
			this.doB = doB;
			this.admin = admin;
		}
	}

	public static final int xResolution = 960;
	public static final int yResolution = 720;
	public static final int gridCellWidth = 50;
	public static final int gridCellHeight = 30;
	public static final double xStart = 0.5;
	public static final double yStart = 0.166;
	public static final int rating = 1;

    public Button buttonMessage = new Button("Message");
    public Polygon[] star = new Polygon[5];
    public Polyline[] starOutline = new Polyline[5];
    
    public Label labelName, labelEmail, labelDoB;
	
	User user = new User("John","Smith","j.smith@gmail.com","JSmith1", false, "password","20/01/1993",false);
	
	@Override public void start(Stage primaryStage) {
		GetProfile();
		OpenProfile(primaryStage);
    }
	
	
	public void OpenProfile(Stage primaryStage){
		
		Group screenGroup = new Group();
		GridPane grid = new GridPane();
		VBox vBoxUserText = new VBox(10);
		HBox hBoxStars = new HBox(5);
		
		SetupStars();
		SetupLabels();
		
		grid.setHgap(gridCellWidth);
        grid.setVgap(gridCellHeight);
        grid.setPadding(new Insets(yResolution*yStart,xResolution*xStart,100,200));
        
        Rectangle profilePicture = new Rectangle(120,120, Color.GRAY);
        
        for(int i = 0 ; i < 5 ; i++){
        	if(i<rating)hBoxStars.getChildren().add(star[i]);
        	else hBoxStars.getChildren().add(starOutline[i]);
        }

        vBoxUserText.getChildren().addAll(labelName,labelEmail,labelDoB);
        
        grid.addRow(0, profilePicture, vBoxUserText);
        grid.addRow(1, hBoxStars);
        
        screenGroup.getChildren().add(grid);
        
		primaryStage.setScene(new Scene(screenGroup, xResolution, yResolution));
		primaryStage.show();
	}
	
	public void GetProfile(){
		//GetCurrentUser();
	}
	
	public void SetupStars(){
		int nbSides = 2*5;
		int r = 10;
        int[] xpoints = new int[nbSides];
        int[] ypoints = new int[nbSides];
        double angle = Math.PI/2;

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
	}
	
	public void SetupLabels(){
		Font fontTitle = new Font("Cambria", 24);
		Font fontMain = new Font("Cambria", 18);
		
		labelName = new Label(user.fName + "\t(" + user.username + ")");
		labelName.setFont(fontTitle);
        labelEmail = new Label("Email: " + user.email);
        labelEmail.setFont(fontMain);
        labelDoB = new Label("Date of Birth: " + user.doB);
        labelDoB.setFont(fontMain);
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
