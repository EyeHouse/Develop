package houseAdverts;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import language.Translate;
import presenter.SlideContent;
import database.Database;
import database.House;

public class MoreInfo extends presenter.Window {

	private static Label furnishedField = new Label("");
//	static TextArea description = new TextArea("");
	private static Label description = new Label("");
	private static House house = Database.getHouse(currentPropertyID);
	
	private static Label labelTitle = new Label(Translate.translateText(languageIndex, "More Information"));
	private static Label labelAddress = new Label(Translate.translateText(languageIndex, "Address: "));
	private static Label labelPostcode = new Label(Translate.translateText(languageIndex, "Postcode:"));
	private static Label labelPrice = new Label(Translate.translateText(languageIndex, "Price (£pppw):"));
	private static Label labelBeds = new Label(Translate.translateText(languageIndex, "Bedrooms:"));
	private static Label labelBaths = new Label(Translate.translateText(languageIndex, "Bathrooms:"));
	private static Label labelFurnished = new Label(Translate.translateText(languageIndex, "Furnished:"));
	private static Label labelDate = new Label(Translate.translateText(languageIndex, "Date Available:"));
	private static Label labelDeposit = new Label(Translate.translateText(languageIndex, "Deposit:"));
	private static Label labelDesc = new Label(Translate.translateText(languageIndex, "Description"));
		
	private GridPane grid = new GridPane();

	public MoreInfo(){
		setupGrid();
		VBox vBoxDesc = new VBox(10);
		
		root.getChildren().add(grid);
		SlideContent.setupBackButton();
		Label address = new Label(house.address);
		Label postcode = new Label(house.postcode);
		Label price = new Label("£" + Integer.toString(house.price));
		Label beds = new Label(Integer.toString(house.rooms));
		Label baths = new Label(Integer.toString(house.bathrooms));
		if(house.furnished){
			furnishedField = new Label(Translate.translateText(languageIndex, "Yes"));
		}
		else{
			furnishedField = new Label(Translate.translateText(languageIndex, "No"));
		}
		Label dateAvailable = new Label(house.dateAvailable.substring(8, 10) + "/"
				+ house.dateAvailable.substring(5, 7) + "/"
				+ house.dateAvailable.substring(0, 4));
		Label deposit = new Label("£" + Integer.toString(house.deposit));
		
//		description.setText(Translate.translateText(languageIndex, house.description));
		description.setText(house.description);
		description.setWrapText(true);
		description.setPrefWidth(500);
//		description.setEditable(false);

		labelTitle.setFont(new Font(32));
		labelAddress.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelPostcode.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelPrice.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelBeds.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelBaths.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelFurnished.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelDate.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelDeposit.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelDesc.setFont(Font.font(null, FontWeight.BOLD, 14));
		
		address.setFont(new Font(14));
		postcode.setFont(new Font(14));
		price.setFont(new Font(14));
		beds.setFont(new Font(14));
		baths.setFont(new Font(14));
		furnishedField.setFont(new Font(14));
		dateAvailable.setFont(new Font(14));
		deposit.setFont(new Font(14));
		
		vBoxDesc.getChildren().addAll(labelDesc, description);
		
		grid.addRow(0, labelTitle);
		grid.addRow(2, labelAddress, address);
		grid.addRow(3, labelPostcode, postcode);
		grid.addRow(4, labelPrice, price);
		grid.addRow(5, labelBeds, beds);
		grid.addRow(6, labelBaths, baths);
		grid.addRow(7, labelFurnished, furnishedField);
		grid.addRow(8, labelDate, dateAvailable);
		grid.addRow(9, labelDeposit, deposit);
		grid.addRow(10, vBoxDesc);
		
//		GridPane.setConstraints(address, 1, 2, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(labelTitle, 0, 0, 3, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(vBoxDesc, 0, 10, 3, 1, HPos.CENTER, VPos.CENTER);
	}

	private void setupGrid() {
		// Set grid size and spacing in group.
		grid.setHgap(40);
		grid.setVgap(20);
		grid.relocate(315, 80);

		grid.setMaxWidth(650);
		grid.setMinWidth(650);
	}
	
	
	/**
	 * Updates labels with translation based on selected language
	 */
	public static void UpdateLanguage() {
		labelTitle.setText(Translate.translateText(
				languageIndex, "More Information"));
		labelAddress.setText(Translate.translateText(
				languageIndex, "Address") + ": ");
		labelPostcode.setText(Translate.translateText(
				languageIndex, "Postcode") + ": ");
		labelPrice.setText(Translate.translateText(
				languageIndex, "Price") + ": ");
		labelBeds.setText(Translate.translateText(
				languageIndex, "Bedrooms") + ": ");
		labelBaths.setText(Translate.translateText(
				languageIndex, "Bathrooms") + ": ");
		labelFurnished.setText(Translate.translateText(
				languageIndex, "Furnished") + ": ");
		labelDate.setText(Translate.translateText(
				languageIndex, "Date Available") + ": ");
		labelDeposit.setText(Translate.translateText(
				languageIndex, "Deposit") + ": ");
		labelDesc.setText(Translate.translateText(
				languageIndex, "Description") + ": ");
		
		if(house.furnished){
			furnishedField.setText(Translate.translateText(languageIndex, "Yes"));
		}
		else{
			furnishedField.setText(Translate.translateText(languageIndex, "No"));
		}
		
//		description.setText(Translate.translateText(languageIndex, house.description));
		
	}
}
