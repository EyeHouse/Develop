package Profile;
/*
 * Profile.java
 * 
 * Version: 1.0
 * 
 * Copyright:
 */

import Profile.UserType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos ;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class AccountSettings{

	public static final int gridCellWidth = 50;
	public static final int gridCellHeight = 30;
	public static final double xStart = 0.5;
	public static final double yStart = 0.05;
	
	Group screenGroup;
	GridPane grid = new GridPane();
	public double xResolution;
	public double yResolution;
	TextField fieldFName, fieldLName, fieldUsername, fieldEmail;
	PasswordField fieldPassword = new PasswordField();
	PasswordField fieldNewPassword = new PasswordField();
	PasswordField fieldConfNewPassword = new PasswordField();
	TextArea profileText = new TextArea();
	ComboBox<String> comboDoBDay = new ComboBox<String>();
    ComboBox<String> comboDoBMonth = new ComboBox<String>();
    ComboBox<String> comboDoBYear = new ComboBox<String>();
    RadioButton buttonStudent = new RadioButton("Student");
    RadioButton buttonLandlord = new RadioButton("Landlord");
    Label labelPasswordIncorrect;
    Label labelNewPasswordInvalid;
	UserType currentUser;
    
	public AccountSettings(Group group, double xresolution, double yresolution){
		screenGroup = group;
		xResolution = xresolution;
		yResolution = yresolution;
	}
	
	public void OpenAccountSettings(UserType user){

		currentUser = user;

		setupGrid();
        setupInfo();
        SetupPassword();
        SetupProfileText();
        SetupButtons();

        screenGroup.getChildren().add(grid);
	}
	
	public void setupGrid(){
		grid.setHgap(gridCellWidth);
        grid.setVgap(gridCellHeight);
        grid.setPadding(new Insets(yResolution*yStart,xResolution*xStart,100,100));
	}
	
	public void setupInfo(){
		HBox hBoxDoB = new HBox(5);
		HBox hBoxAccountType = new HBox(30);
		ToggleGroup group = new ToggleGroup();
		Label labelFName = new Label("First Name");
        Label labelLName = new Label("Last Name");
        Label labelUsername = new Label("User Name");
        Label labelEmail = new Label("Email");
        Label labelDoB = new Label("Date of Birth");
        Label labelAccountType = new Label("Account Type");
        
        fieldFName = new TextField(currentUser.fName);
        fieldLName = new TextField(currentUser.lName);
        fieldUsername = new TextField(currentUser.username);
        fieldEmail = new TextField(currentUser.email);
        
        buttonStudent.setToggleGroup(group);
        buttonLandlord.setToggleGroup(group);
        
        if(currentUser.landlord)buttonLandlord.setSelected(true);
        else buttonStudent.setSelected(true);
        
        SetupDoB();
        hBoxDoB.getChildren().addAll(comboDoBDay,comboDoBMonth,comboDoBYear);
        hBoxAccountType.getChildren().addAll(buttonStudent,buttonLandlord);
        
        grid.addColumn(	0, labelFName, labelLName, labelUsername, labelEmail, 
        				labelDoB, labelAccountType);
        grid.addColumn(	1, fieldFName, fieldLName, fieldUsername, fieldEmail, 
        				hBoxDoB, hBoxAccountType);
	}
		
	public void SetupDoB(){
		
		for(int i = 1; i < 32; i++){
        	comboDoBDay.getItems().add(String.format("%02d", i));
        }
        for(int i = 1; i < 13; i++){
        	comboDoBMonth.getItems().add(String.format("%02d", i));
        }
        for(int i = 2015; i > 1919; i--){
        	comboDoBYear.getItems().add(String.format("%04d", i));
        }
        
    	int doBDay = Integer.valueOf(currentUser.doB.substring(0,2), 10).intValue();
    	int doBMonth = Integer.valueOf(currentUser.doB.substring(3,5), 10).intValue();
    	int doBYear = Integer.valueOf(currentUser.doB.substring(6,10), 10).intValue();
    	comboDoBDay.getSelectionModel().select(doBDay - 1);
    	comboDoBMonth.getSelectionModel().select(doBMonth-1);
    	comboDoBYear.getSelectionModel().select(2015 - doBYear);
	}
	
	public void SetupPassword(){
		Label labelCurrentPassword = new Label("Current Password");
        Label labelNewPassword = new Label("New Password");
        Label labelConfNewPassword = new Label("Confirm New Password");
        labelPasswordIncorrect = new Label("Incorrect Password");
        labelNewPasswordInvalid = new Label("New Password Invalid");
        
        labelPasswordIncorrect.setVisible(false);
        labelNewPasswordInvalid.setVisible(false);
        
        grid.addRow(6, labelCurrentPassword, fieldPassword, labelPasswordIncorrect);
        grid.addRow(7, labelNewPassword, fieldNewPassword, labelNewPasswordInvalid);
        grid.addRow(8, labelConfNewPassword, fieldConfNewPassword);
	}
	
	public void SetupProfileText(){
		Label labelProfileText = new Label("Profile");
		
		profileText.setText(currentUser.profileText);
		profileText.setMaxHeight(gridCellHeight*3);
		
		grid.add(labelProfileText, 0,9);
		grid.add(profileText, 0, 10);
        GridPane.setConstraints(profileText,0,10,2,1,HPos.CENTER,VPos.CENTER);
	}
	
	public void SetupButtons(){
		Button buttonApply = new Button("Apply Changes");
		
		buttonApply.setOnAction(new EventHandler<ActionEvent>(){
        	@Override public void handle(ActionEvent event){
                ApplyChanges();
            }
        });
		
		grid.add(buttonApply, 0, 11);
        GridPane.setConstraints(buttonApply,0,11,2,1,HPos.CENTER,VPos.CENTER);
	}
	
	private void ApplyChanges(){
		currentUser.fName = fieldFName.getText();
		currentUser.lName = fieldLName.getText();
		currentUser.username = fieldUsername.getText();
		currentUser.email = fieldEmail.getText();
		currentUser.doB = comboDoBDay.getValue() + "/" + comboDoBMonth.getValue() + "/" + comboDoBYear.getValue();
		currentUser.landlord = buttonLandlord.isSelected();
		currentUser.profileText = profileText.getText();
		
		if(fieldPassword.getText().equals("")){
			screenGroup.getChildren().clear();
			ProfileViewer profile = new ProfileViewer(screenGroup, xResolution, yResolution);
			profile.OpenProfile(currentUser);
		} else if(!fieldPassword.getText().equals(currentUser.password)){
			labelPasswordIncorrect.setVisible(true);
		} else if(fieldPassword.getText().equals(currentUser.password)){
			if((!fieldNewPassword.getText().equals(""))&&(fieldNewPassword.getText().equals(fieldConfNewPassword.getText()))){
				currentUser.password = fieldNewPassword.getText();
				screenGroup.getChildren().clear();
				ProfileViewer profile = new ProfileViewer(screenGroup, xResolution, yResolution);
				profile.OpenProfile(currentUser);
			} else{
				labelPasswordIncorrect.setVisible(false);
				labelNewPasswordInvalid.setVisible(true);
			}
		}
	}
}
