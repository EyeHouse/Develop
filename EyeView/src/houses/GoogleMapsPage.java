package houses;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import database.Database;
import database.House;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import language.Translator;
import presenter.Window;
import profile.SavedProperties;

/**
 * This class puts an embedded browser in the scene, which can render HTML
 * content from local and remote URLs. This helps to load the JavaScript version
 * of the Google Maps API, which displays the house location and route from the
 * university.
 * 
 * @version 2.1
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class GoogleMapsPage extends Window {

	MyBrowser myBrowser;

	private GridPane grid = new GridPane();
	private Label topTitle;

	/**
	 * Constructor method
	 */
	public GoogleMapsPage() {

		myBrowser = new MyBrowser();

		setupGrid();
		setupButtons();
		setupTitle();
		Pane pane = new Pane();

		pane.getChildren().add(myBrowser);

		pane.resize(600, 600);
		pane.relocate(200, 120);

		root.getChildren().add(pane);
	}

	/**
	 * Sets up the Grid's horizontal and vertical spacing.
	 */
	public void setupGrid() {

		grid.setVgap(10);
		grid.setHgap(10);
	}

	/**
	 * Sets up the the title of the page.
	 */
	public void setupTitle() {

		topTitle = new Label(Translator.translateText(languageIndex,
				"Map and Route"));
		topTitle.setFont(new Font(32));
		topTitle.setTextFill(Color.web("#162252"));
		topTitle.setPrefWidth(550);
		topTitle.setAlignment(Pos.CENTER);
		topTitle.relocate(275, 80);
		root.getChildren().add(topTitle);
	}

	/**
	 * Makes sure that the title is translated.
	 */
	public void updateLanguage() {

		topTitle.setText(Translator.translateText(languageIndex,
				"Map and Route"));
	}

	/**
	 * Sets up the back button for the page.
	 */
	public void setupButtons() {

		SavedProperties.setupPropertyBackButton();
	}

	/**
	 * This class utilises WebEngine, which helps navigating links and loading
	 * HTML content. And it contains WebView, which encapsulates a WebEngine
	 * object and incorporates HTML content into an application's scene.
	 */
	class MyBrowser extends Region {

		WebView webView;
		WebEngine webEngine;

		/**
		 * Constructor method
		 */
		public MyBrowser() {

			webView = new WebView();
			webEngine = webView.getEngine();

			// Retrieval of house location from the database.
			House destination = Database.getHouse(currentPropertyID);
			changeDestination(destination.address, destination.postcode);

			// Accessing the HTML file.
			File file = new File("src/houses/PropertyMapData.html");
			try {
				URL urlGoogleMaps = file.toURI().toURL();
				webEngine.load(urlGoogleMaps.toExternalForm());
			} catch (MalformedURLException e) {
				System.out.println("Property map data not found.");
			}

			// Adding the browser to the grid.
			getChildren().add(webView);
			grid.add(webView, 1, 2);
			getChildren().add(grid);

		}

		/**
		 * Sends in a scanner into the HTML file, finds the line which set the
		 * destination location and changes it to the house location retrived
		 * from the database.
		 * 
		 * @param address
		 *            The street address of the house from the database.
		 * @param postcode
		 *            The postcode of the house from the database.
		 */
		private void changeDestination(String address, String postcode) {

			URL urlGoogleMaps = getClass().getResource("GoogleMaps.html");
			try {
				URLConnection inputConnection = urlGoogleMaps.openConnection();

				Scanner in = new Scanner(new InputStreamReader(
						inputConnection.getInputStream()));

				PrintWriter output = new PrintWriter(
						new OutputStreamWriter(new FileOutputStream(
								"src/houses/PropertyMapData.html")));
				
				// Setting the destination line to the house location.
				while (in.hasNext()) {
					String inputLine = in.nextLine();
					if (inputLine.contains("var destinationA")) {
						inputLine = "	var destinationA = '" + address + ", "
								+ postcode + "';";
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