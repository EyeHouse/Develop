/*
 * Profile.java
 * 
 * Version: 1.0
 * 
 * Copyright:
 */

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos ;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

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

    Button buttonMessage = new Button("Message");
    Polygon star = new Polygon(0,0,50,0,50,50,0,50);
	
	User user = new User("John","Smith","j.smith@gmail.com","JSmith1", false, "password","20/01/1993",false);
	
	@Override public void start(Stage primaryStage) {
		GetProfile();
		OpenProfile(primaryStage);
    }
	
	
	public void OpenProfile(Stage primaryStage){
		
		Group screenGroup = new Group();
		GridPane grid = new GridPane();
		VBox vBoxUserText = new VBox(10);
		HBox hBoxNameMessage = new HBox(gridCellWidth);
		
		int nbSides = 2*5;
		int r = 100;
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
        //star = new Polygon(xpoints, ypoints, nbSides);
		
		grid.setHgap(gridCellWidth);
        grid.setVgap(gridCellHeight);
        grid.setPadding(new Insets(yResolution*yStart,xResolution*xStart,100,200));
        
        Rectangle profilePicture = new Rectangle(75,75, Color.GRAY);
        
        Label labelFName = new Label(user.fName);
        Label labelEmail = new Label("Email: " + user.email);
        Label labelDoB = new Label("Date of Birth: " + user.doB);
        //double c = hBoxNameMessage.g
        //Label labelC = new Label(String.valueOf(c));
        
        hBoxNameMessage.getChildren().addAll(labelFName, buttonMessage);
        vBoxUserText.getChildren().addAll(hBoxNameMessage,labelEmail,labelDoB);
        
        buttonMessage.setOnAction(new EventHandler<ActionEvent>(){
        	@Override public void handle(ActionEvent event){
        		sendMessage();
            }
        });
        
        grid.addRow(0, profilePicture, vBoxUserText);
        grid.addRow(1, star);
        
        screenGroup.getChildren().add(grid);
        
		primaryStage.setScene(new Scene(screenGroup, xResolution, yResolution));
		primaryStage.show();
	}
	
	public void GetProfile(){
		//GetCurrentUser();
	}
	
	private void sendMessage(){
		buttonMessage.setVisible(false);
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
