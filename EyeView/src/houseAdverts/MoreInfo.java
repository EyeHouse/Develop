package houseAdverts;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import presenter.SlideContent;
import database.Database;
import database.House;

public class MoreInfo extends presenter.Window {

	GridPane grid = new GridPane();


	public MoreInfo(){
		setupGrid();
		VBox vBoxDesc = new VBox(10);
		House house = Database.getHouse(currentPropertyID);
		root.getChildren().add(grid);
		//add BackButton
		SlideContent.setupBackButton();
		
		Label address = new Label(house.address);
		Label postcode = new Label(house.postcode);
		Label price = new Label("£" + Integer.toString(house.price));
		Label beds = new Label(Integer.toString(house.rooms));
		Label baths = new Label(Integer.toString(house.bathrooms));
		Label furnished;
		if(house.furnished){
			furnished = new Label("Yes");
		}
		else{
			furnished = new Label("No");
		}
		Label dateAvailable = new Label(house.dateAvailable.substring(8, 10) + "/"
				+ house.dateAvailable.substring(5, 7) + "/"
				+ house.dateAvailable.substring(0, 4));
		Label deposit = new Label("£" + Integer.toString(house.deposit));
		TextArea description = new TextArea(house.description);
		description.setWrapText(true);
		description.setEditable(false);
		
		Label labelAddress = new Label("Address: ");
		Label labelPostcode = new Label("Postcode:");
		Label labelPrice = new Label("Price (£pppw):");
		Label labelBeds = new Label("Bedrooms:");
		Label labelBaths = new Label("Bathrooms:");
		Label labelFurnished = new Label("Furnished:");
		Label labelDate = new Label("Date Available:");
		Label labelDeposit = new Label("Deposit:");
		Label labelDesc = new Label("Description");

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
		furnished.setFont(new Font(14));
		deposit.setFont(new Font(14));
		
		vBoxDesc.getChildren().addAll(labelDesc, description);
		
		grid.addRow(2, labelAddress, address);
		grid.addRow(3, labelPostcode, postcode);
		grid.addRow(4, labelPrice, price);
		grid.addRow(5, labelBeds, beds);
		grid.addRow(6, labelBaths, baths);
		grid.addRow(7, labelFurnished, furnished);
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
}
