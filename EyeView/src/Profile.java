/*
 * Profile.java
 * 
 * Version: 1.0
 * 
 * Copyright:
 */

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;

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
	public static final int gridCellWidth = 100;
	public static final int gridCellHeight = 30;
	public static final double xStart = 0.25;
	public static final double yStart = 0.166;
	
	ComboBox<Integer> comboDoBDay = new ComboBox<Integer>();
    ComboBox<Integer> comboDoBMonth = new ComboBox<Integer>();
    ComboBox<Integer> comboDoBYear = new ComboBox<Integer>();
	
	ProfileItem user = new ProfileItem("John","Smith","JSmith1","j.smith@gmail.com","20/01/1993","Male");
	
	@Override public void start(Stage primaryStage) {
		OpenProfile(primaryStage);
    }
	
	
	
	public void OpenProfile(Stage primaryStage){
		
		Group screenGroup = new Group();
		GridPane grid = new GridPane();
		HBox hBoxDoB = new HBox(5);
		
		grid.setHgap(gridCellWidth);
        grid.setVgap(gridCellHeight);
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
        TextField fieldGender = new TextField(user.gender);
        
        Button buttonApply = new Button("Apply Changes");
        //buttonApply.addEventHandler(MouseEvent.MOUSE_CLICKED, );
        
        SetupDoB();
        
        hBoxDoB.getChildren().addAll(comboDoBDay,comboDoBMonth,comboDoBYear);
        
        grid.addColumn(0, labelFName, labelLName, labelUsername, labelEmail, labelDoB, labelGender);
        grid.addColumn(1, fieldFName, fieldLName, fieldUsername, fieldEmail, hBoxDoB, fieldGender);
        
        grid.add(buttonApply, 0, 6);
        GridPane.setConstraints(buttonApply,0,6,2,1,HPos.CENTER,VPos.CENTER);
        
        screenGroup.getChildren().add(grid);
		primaryStage.setScene(new Scene(screenGroup, xResolution, yResolution));
		primaryStage.show();
	}
	
	public void SetupDoB(){
		
		for(int i = 1; i < 32; i++){
        	comboDoBDay.getItems().add(i);
        }
        for(int i = 1; i < 13; i++){
        	comboDoBMonth.getItems().add(i);
        }
        for(int i = 2015; i > 1919; i--){
        	comboDoBYear.getItems().add(i);
        }
        
    	int doBDay = Integer.valueOf(user.doB.substring(0,2), 10).intValue();
    	int doBMonth = Integer.valueOf(user.doB.substring(3,5), 10).intValue();
    	int doBYear = Integer.valueOf(user.doB.substring(6,10), 10).intValue();
    	comboDoBDay.getSelectionModel().select(doBDay - 1);
    	comboDoBMonth.getSelectionModel().select(doBMonth-1);
    	comboDoBYear.getSelectionModel().select(2015 - doBYear);
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
