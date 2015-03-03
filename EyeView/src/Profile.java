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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class Profile extends Application{

	class ProfileItem {
		public String fName, lName, username, email, doB, gender;
		
		public ProfileItem(String fName, String lName, String username, String email, String doB, String gender){
			this.fName = fName;
			this.lName = lName;
			this.username = username;
			this.email = email;
			this.doB = doB;
			this.gender = gender;
		}
	}

	public static final int xResolution = 800;
	public static final int yResolution = 600;
	public static final double xStart = 0.25;
	public static final double yStart = 0.166;
	
	ProfileItem user = new ProfileItem("John","Smith","JSmith1","j.smith@gmail.com","20/01/1993","Male");
	
	@Override public void start(Stage primaryStage) {
		OpenProfile(primaryStage);
    }
	
	
	
	public void OpenProfile(Stage primaryStage){
		
		Group screenGroup = new Group();
		GridPane grid = new GridPane();
		
		grid.setHgap(100);
        grid.setVgap(30);
        grid.setPadding(new Insets(yResolution*yStart,xResolution*xStart,100,200));
        
        Label labelFName = new Label("First Name");
        Label labelLName = new Label("Last Name");
        Label labelUsername = new Label("User Name");
        Label labelEmail = new Label("Email");
        Label labelDoB = new Label("Date of Birth");
        Label labelGender = new Label("Gender");
        
        TextField fieldFName = new TextField(user.fName);
        TextField fieldLName = new TextField(user.lName);
        TextField fieldUsername = new TextField(user.username);
        TextField fieldEmail = new TextField(user.email);
        TextField fieldDoB = new TextField(user.doB);
        TextField fieldGender = new TextField(user.gender);
        
        grid.addColumn(0, labelFName, labelLName, labelUsername, labelEmail, labelDoB, labelGender);
        grid.addColumn(1, fieldFName, fieldLName, fieldUsername, fieldEmail, fieldDoB, fieldGender);
        
        screenGroup.getChildren().add(grid);
		primaryStage.setScene(new Scene(screenGroup, xResolution, yResolution));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}

}
