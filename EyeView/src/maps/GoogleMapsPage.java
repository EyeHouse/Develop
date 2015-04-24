package maps;

import java.net.URL;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
 

public class GoogleMapsPage extends Application {
 
  Scene scene;
  MyBrowser myBrowser;
  
  GridPane grid = new GridPane();
  final Button backButton = new Button("Back");
  
  

  public static void main(String[] args) {
      launch(args);
  }
 
  @Override
  public void start(Stage primaryStage) {
      primaryStage.setTitle("Maps Page");
     
      
      myBrowser = new MyBrowser();
      scene = new Scene(myBrowser, 960, 800);
      setupGrid();
      setupButtons();
      setupTitle();
      primaryStage.setScene(scene);
      primaryStage.show();
  }
  
  
  public void setupGrid() {

		grid.setVgap(10);
		grid.setHgap(10);
		grid.setPadding(new Insets(5, 5, 5, 200));
	}
  
  public void setupTitle() {

		final Label topTitle = new Label("Maps");
		topTitle.setTextFill(Color.web("#162252FF"));
		topTitle.setFont(new Font(35));
		grid.add(topTitle, 1, 1);
		//GridPane.setConstraints(topTitle, 0, 0, 2, 1, HPos.CENTER, VPos.CENTER);

	}
  
  public void setupButtons() {
	  
	  grid.add(backButton, 1, 0);
	  backButton.setCursor(Cursor.HAND);
  }
 
  class MyBrowser extends Region{
     
	  WebView webView = new WebView();
	  WebEngine webEngine = webView.getEngine();
	 
	   
	  /*public void getHouseLocation() {
		  String house = "36, Eastfield Crescent, YO10 5JB";
		  webEngine.executeScript("document.zoomIn()");
		  
	  }*/
      
     
      public MyBrowser(){
         
          final URL urlGoogleMaps = getClass().getResource("GoogleMaps.html");
          webEngine.load(urlGoogleMaps.toExternalForm());
 
          getChildren().add(webView);
          grid.add(webView, 1, 2);
          getChildren().add(grid);
          
          
          backButton.setOnAction(new EventHandler<ActionEvent>() {
  			public void handle(ActionEvent event) {
  				
  				getChildren().clear();  				  				
				//slideID = HOUSES;
				//SlideContent sc = new SlideContent();
				//sc.createSlide();
  				
  			}
  		});
         
      }
     
  }
  
  


}