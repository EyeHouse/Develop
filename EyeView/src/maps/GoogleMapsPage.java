package maps;

import java.net.URL;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import presenter.SlideContent;
 

public class GoogleMapsPage extends presenter.Window {
 
  MyBrowser myBrowser;
  
  GridPane grid = new GridPane();
  
  public GoogleMapsPage(){
	  
	  myBrowser = new MyBrowser();
	  
      setupGrid();
      setupButtons();
      setupTitle();
      Pane pane = new Pane();
      
      pane.getChildren().add(myBrowser);
      
      pane.resize(600, 600);
      pane.relocate(200, 100);
      
      root.getChildren().add(pane);
  }
  
  
  public void setupGrid() {

		grid.setVgap(10);
		grid.setHgap(10);
	}
  
  public void setupTitle() {

		final Label topTitle = new Label("Property Map");
		topTitle.setTextFill(Color.web("#162252FF"));
		topTitle.setFont(new Font(35));
		grid.add(topTitle, 1, 1);
		//GridPane.setConstraints(topTitle, 0, 0, 2, 1, HPos.CENTER, VPos.CENTER);

	}
  
  public void setupButtons() {
	  
	  SlideContent.setupBackButton();
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
         
      }
     
  }
  
  


}