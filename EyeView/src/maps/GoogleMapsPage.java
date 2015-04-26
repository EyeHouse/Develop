package maps;

import java.io.*;
import java.net.*;
import java.util.Scanner;

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

	public GoogleMapsPage() {

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
		// GridPane.setConstraints(topTitle, 0, 0, 2, 1, HPos.CENTER,
		// VPos.CENTER);

	}

	public void setupButtons() {

		SlideContent.setupBackButton();
	}

	class MyBrowser extends Region {

		WebView webView;
		WebEngine webEngine;

		/*
		 * public void getHouseLocation() { String house =
		 * "36, Eastfield Crescent, YO10 5JB";
		 * webEngine.executeScript("document.zoomIn()");
		 * 
		 * }
		 */

		public MyBrowser() {

			webView = new WebView();
			webEngine = webView.getEngine();
			
			changeDestination("5 The Link","YO10 4LB");
			
			File file = new File("src/maps/PropertyMapData.html");
			try {
				URL urlGoogleMaps = file.toURI().toURL();
				webEngine.load(urlGoogleMaps.toExternalForm());
			} catch (MalformedURLException e) {
				System.out.println("Property map data not found.");
			}
			
			getChildren().add(webView);
			grid.add(webView, 1, 2);
			getChildren().add(grid);

		}

		private void changeDestination(String address, String postcode) {

			URL urlGoogleMaps = getClass().getResource("GoogleMaps.html");
			try {
				URLConnection inputConnection = urlGoogleMaps.openConnection();

				Scanner in = new Scanner(new InputStreamReader(
						inputConnection.getInputStream()));

				PrintWriter output = new PrintWriter(new OutputStreamWriter(
					       new FileOutputStream("src/maps/PropertyMapData.html")));
				while (in.hasNext()) {
					String inputLine = in.nextLine();
					if(inputLine.contains("var destinationA")){
						inputLine = "	var destinationA = '"+ address +", " + postcode + "';";
					}
					output.println(inputLine);
				}
				in.close();
				output.close();
			} catch (IOException e) {
				System.out.println("Failed to connect to HTML document");
			}
		}

	}

}