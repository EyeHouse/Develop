package houses;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import language.Translator;
import presenter.Window;
import profile.SavedProperties;
import database.Database;
import database.House;

/**
 * This class creates the information page for a specific house advert.
 * 
 * @version 2.5
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class MoreInfo extends Window {

	// Global Variables
	private Label furnishedField = new Label("");
	private Label description = new Label("");
	private House house = Database.getHouse(currentPropertyID);

	private Label labelTitle = new Label(Translator.translateText(
			languageIndex, "More Information"));
	private Label labelAddress = new Label(Translator.translateText(
			languageIndex, "Address: "));
	private Label labelPostcode = new Label(Translator.translateText(
			languageIndex, "Postcode:"));
	private Label labelPrice = new Label(Translator.translateText(
			languageIndex, "Price (£pppw):"));
	private Label labelBeds = new Label(Translator.translateText(languageIndex,
			"Bedrooms:"));
	private Label labelBaths = new Label(Translator.translateText(
			languageIndex, "Bathrooms:"));
	private Label labelFurnished = new Label(Translator.translateText(
			languageIndex, "Furnished:"));
	private Label labelDate = new Label(Translator.translateText(languageIndex,
			"Date Available:"));
	private Label labelDeposit = new Label(Translator.translateText(
			languageIndex, "Deposit:"));
	private Label labelDesc = new Label(Translator.translateText(languageIndex,
			"Description"));

	private GridPane grid = new GridPane();

	/**
	 * Constructor method
	 */
	public MoreInfo() {

		setupGrid();
		
		VBox vBoxDesc = new VBox(10);
		// Set up back button
		SavedProperties.setupPropertyBackButton();
		// Add labels
		Label address = new Label(house.address);
		Label postcode = new Label(house.postcode);
		Label price = new Label("£" + Integer.toString(house.price));
		Label beds = new Label(Integer.toString(house.rooms));
		Label baths = new Label(Integer.toString(house.bathrooms));

		// Set up if the property is furnished
		if (house.furnished) {
			furnishedField = new Label(Translator.translateText(languageIndex,
					"Yes"));
		} else {
			furnishedField = new Label(Translator.translateText(languageIndex,
					"No"));
		}
		// Add Labels
		Label dateAvailable = new Label(house.dateAvailable.substring(8, 10)
				+ "/" + house.dateAvailable.substring(5, 7) + "/"
				+ house.dateAvailable.substring(0, 4));
		Label deposit = new Label("£" + Integer.toString(house.deposit));

		// Add descriptions of the property
		description.setText(Translator.translateText(languageIndex,
				house.description));
		description.setWrapText(true);
		description.setPrefWidth(500);

		// Set label titles
		labelTitle.setFont(new Font(32));
		labelTitle.setTextFill(Color.web("#162252"));
		labelTitle.setPrefWidth(550);
		labelTitle.setAlignment(Pos.CENTER);
		labelTitle.relocate(275, 80);

		// Set font of labels
		labelAddress.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelPostcode.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelPrice.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelBeds.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelBaths.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelFurnished.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelDate.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelDeposit.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelDesc.setFont(Font.font(null, FontWeight.BOLD, 14));

		// Set font of text
		address.setFont(new Font(14));
		postcode.setFont(new Font(14));
		price.setFont(new Font(14));
		beds.setFont(new Font(14));
		baths.setFont(new Font(14));
		furnishedField.setFont(new Font(14));
		dateAvailable.setFont(new Font(14));
		deposit.setFont(new Font(14));

		vBoxDesc.getChildren().addAll(labelDesc, description);

		grid.addRow(1, labelAddress, address);
		grid.addRow(2, labelPostcode, postcode);
		grid.addRow(3, labelPrice, price);
		grid.addRow(4, labelBeds, beds);
		grid.addRow(5, labelBaths, baths);
		grid.addRow(6, labelFurnished, furnishedField);
		grid.addRow(7, labelDate, dateAvailable);
		grid.addRow(8, labelDeposit, deposit);
		grid.addRow(10, vBoxDesc);

		GridPane.setConstraints(labelTitle, 0, 0, 3, 1, HPos.CENTER,
				VPos.CENTER);
		GridPane.setConstraints(vBoxDesc, 0, 10, 3, 1, HPos.CENTER, VPos.CENTER);

		root.getChildren().addAll(labelTitle, grid);
	}

	/**
	 * Sets up properties for the grid layout.
	 */
	public void setupGrid() {

		// Set grid size and spacing in group.
		grid.setHgap(40);
		grid.setVgap(20);
		grid.relocate(315, 150);

		grid.setMaxWidth(650);
		grid.setMinWidth(650);
	}

	/**
	 * Updates labels with translation based on selected language.
	 */
	public void updateLanguage() {

		labelTitle.setText(Translator.translateText(languageIndex,
				"More Information"));
		labelAddress.setText(Translator.translateText(languageIndex, "Address")
				+ ": ");
		labelPostcode.setText(Translator.translateText(languageIndex,
				"Postcode") + ": ");
		labelPrice.setText(Translator.translateText(languageIndex, "Price")
				+ ": ");
		labelBeds.setText(Translator.translateText(languageIndex, "Bedrooms")
				+ ": ");
		labelBaths.setText(Translator.translateText(languageIndex, "Bathrooms")
				+ ": ");
		labelFurnished.setText(Translator.translateText(languageIndex,
				"Furnished") + ": ");
		labelDate.setText(Translator.translateText(languageIndex,
				"Date Available") + ": ");
		labelDeposit.setText(Translator.translateText(languageIndex, "Deposit")
				+ ": ");
		labelDesc.setText(Translator
				.translateText(languageIndex, "Description") + ": ");
		description.setText(Translator.translateText(languageIndex,
				house.description));

		if (house.furnished) {
			furnishedField.setText(Translator.translateText(languageIndex,
					"Yes"));
		} else {
			furnishedField.setText(Translator
					.translateText(languageIndex, "No"));
		}
	}
}
