package landlord;

import Button.ButtonType;
import Button.SetupButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import database.Database;
import database.House;

public class EditProperty extends presenter.Window {

	private int page;
	private static final int INFO = 0;
	private static final int PICS = 1;
	private static final int VIDEO = 2;
	private GridPane grid = new GridPane();
	private House house;

	public EditProperty(int page, int houseID) {
		this.page = page;
		house = Database.getHouse(houseID);

		setupGrid();
		setupTabLabels(page);
		switch (page) {
		case INFO:
			setupHouseInfo();
			break;
		case PICS:
			break;
		case VIDEO:
			break;
		}

		root.getChildren().add(grid);
	}

	private void setupGrid() {
		// Set grid size and spacing in group.
		grid.setHgap(50);
		grid.setVgap(30);
		grid.relocate(220, 20);

		ColumnConstraints col1 = new ColumnConstraints();
		col1.setMinWidth(100);
		grid.getColumnConstraints().addAll(col1, col1,col1);
	}

	private void setupTabLabels(int page) {

		HBox hBoxTabs = new HBox(60);
		Label labelInfoTab = new Label("1. House Information");
		Label labelPictureTab = new Label("2. House Pictures");
		Label labelVideoTab = new Label("3. House Videos");
		labelInfoTab.setFont(new Font(16));
		labelPictureTab.setFont(new Font(16));
		labelVideoTab.setFont(new Font(16));

		switch (page) {
		case INFO:
			labelInfoTab.setFont(Font.font(null, FontWeight.BOLD, labelInfoTab
					.getFont().getSize()));
			break;
		case PICS:
			labelPictureTab.setFont(Font.font(null, FontWeight.BOLD,
					labelPictureTab.getFont().getSize()));
			break;
		case VIDEO:
			labelVideoTab.setFont(Font.font(null, FontWeight.BOLD,
					labelVideoTab.getFont().getSize()));
			break;
		}

		hBoxTabs.getChildren().addAll(labelInfoTab, labelPictureTab,
				labelVideoTab);

		grid.add(hBoxTabs, 0, 0);
		GridPane.setConstraints(hBoxTabs, 0, 0, 3, 1, HPos.CENTER, VPos.CENTER);
	}
	
	private void setupHouseInfo(){
		VBox vBoxDesc = new VBox(10);
		Label labelAddress = new Label("Address:");
		Label labelPostcode = new Label("Postcode:");
		Label labelPrice = new Label("Price:");
		Label labelBeds = new Label("Bedrooms:");
		Label labelBaths = new Label("Bathrooms:");
		Label labelDate = new Label("Date Available:");
		Label labelDeposit = new Label("Deposit:");
		Label labelDesc = new Label("Description");
		
		labelAddress.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelPostcode.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelPrice.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelBeds.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelBaths.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelDate.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelDeposit.setFont(Font.font(null, FontWeight.BOLD, 14));
		labelDesc.setFont(Font.font(null, FontWeight.BOLD, 14));
		
		TextField address = new TextField(house.address);
		TextField postcode = new TextField(house.postcode);
		TextField price = new TextField("£" + Integer.toString(house.price));
		TextField beds = new TextField(Integer.toString(house.rooms));
		TextField baths = new TextField(Integer.toString(house.bathrooms));
		TextField dateAvailable = new TextField(house.dateAvailable);
		TextField deposit = new TextField("£" + Integer.toString(house.deposit));
		TextArea description = new TextArea(house.description);
		
		description.setPrefSize(300,100);
		description.setWrapText(true);
		
		vBoxDesc.getChildren().addAll(labelDesc,description);
		
		ButtonType button1 = new ButtonType("150,150,150",null,"Save",150,30);
		Button buttonSave = new SetupButton().CreateButton(button1);
		buttonSave.setCursor(Cursor.HAND);
		buttonSave.setOnAction(new ApplyChanges());
		
		grid.addRow(2,labelAddress,address);
		grid.addRow(3,labelPostcode,postcode);
		grid.addRow(4,labelPrice,price);
		grid.addRow(5,labelBeds,beds);
		grid.addRow(6,labelBaths,baths);
		grid.addRow(7,labelDate,dateAvailable);
		grid.addRow(8,labelDeposit,deposit);
		grid.addRow(9,vBoxDesc);
		grid.add(buttonSave,2,10);
		GridPane.setConstraints(address, 1, 2, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(vBoxDesc, 0, 9, 3, 1, HPos.CENTER, VPos.CENTER);
		
	}
	
	public class ApplyChanges implements EventHandler<ActionEvent>{

		public void handle(ActionEvent arg0) {
			
		}
	}
}
