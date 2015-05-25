package houseAdverts;

import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import language.Translate;
import presenter.SlideContent;
import database.Database;
import database.House;

public class MoreInfo extends presenter.Window {

	private static Label labelAddress;
	private static Label labelPostcode;
	private static Label labelPrice;
	private static Label labelBeds;
	private static Label labelBaths;
	private static Label labelFurnished;
	private static Label labelDate;
	private static Label labelDeposit;
	private static Label labelDesc;
	private static Label furnishedField;
	private static House house = Database.getHouse(currentPropertyID);
		
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
		TextArea description = new TextArea(house.description);
		description.setWrapText(true);
		description.setEditable(false);
		
//		Label labelAddress = new Label("Address: ");
		labelAddress = new Label(Translate.translateText(languageIndex, "Address: "));
		labelPostcode = new Label(Translate.translateText(languageIndex, "Postcode:"));
		labelPrice = new Label(Translate.translateText(languageIndex, "Price (£pppw):"));
		labelBeds = new Label(Translate.translateText(languageIndex, "Bedrooms:"));
		labelBaths = new Label(Translate.translateText(languageIndex, "Bathrooms:"));
		labelFurnished = new Label(Translate.translateText(languageIndex, "Furnished:"));
		labelDate = new Label(Translate.translateText(languageIndex, "Date Available:"));
		labelDeposit = new Label(Translate.translateText(languageIndex, "Deposit:"));
		labelDesc = new Label(Translate.translateText(languageIndex, "Description"));

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
		deposit.setFont(new Font(14));
		
		vBoxDesc.getChildren().addAll(labelDesc, description);
		
		grid.addRow(2, labelAddress, address);
		grid.addRow(3, labelPostcode, postcode);
		grid.addRow(4, labelPrice, price);
		grid.addRow(5, labelBeds, beds);
		grid.addRow(6, labelBaths, baths);
		grid.addRow(7, labelFurnished, furnishedField);
		grid.addRow(8, labelDate, dateAvailable);
		grid.addRow(9, labelDeposit, deposit);
		grid.addRow(10, vBoxDesc);
		
		GridPane.setConstraints(address, 1, 2, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(vBoxDesc, 0, 10, 3, 1, HPos.CENTER, VPos.CENTER);
	}

	private void setupGrid() {
		// Set grid size and spacing in group.
		grid.setHgap(50);
		grid.setVgap(30);
		grid.relocate(250, 80);

		grid.setMaxWidth(650);
		grid.setMinWidth(650);
	}
	
	public static void UpdateLanguage() {
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
		
	}
}
