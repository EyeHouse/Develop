package Profile;

import java.util.Arrays;
import java.util.ArrayList;

import Button.ButtonType;
import Button.SetupButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import presenter.SlideContent;

public class SavedProperties extends presenter.Window {

	GridPane grid = new GridPane();
	ArrayList<String> properties = new ArrayList<String>(Arrays.asList(
			"5 The Link", "4 Burnholme Grove"));
	ListView<HBox> propertyList = new ListView<HBox>();
	ObservableList<HBox> items = FXCollections.observableArrayList();

	public SavedProperties() {

		setupGrid();
		setupTitle();
		setupButtons();
		setupPropertyList();

		SlideContent.setupBackButton();

		root.getChildren().add(grid);
	}

	private void setupGrid() {

		ColumnConstraints col1 = new ColumnConstraints();
		ColumnConstraints col2 = new ColumnConstraints();
		col1.setMinWidth(500);
		col2.setMinWidth(100);
		grid.getColumnConstraints().addAll(col1, col2);

		grid.setVgap(30);
		grid.setHgap(30);
		grid.relocate(220, 80);
	}

	private void setupTitle() {

		Label labelTitle = new Label("Saved Properties");
		labelTitle.setTextFill(Color.web("#162252FF"));
		labelTitle.setFont(new Font(35));
		grid.add(labelTitle, 0, 0);
		GridPane.setConstraints(labelTitle, 0, 0, 2, 1, HPos.CENTER,
				VPos.CENTER);
	}

	private void setupButtons() {
		VBox buttons = new VBox(30);
		
		ButtonType button1 = new ButtonType("150,150,150",null,"View",100,30);
		ButtonType button2 = new ButtonType("150,150,150",null,"Remove",100,30);
		
		Button buttonView = new SetupButton().CreateButton(button1);
		Button buttonRemove = new SetupButton().CreateButton(button2);

		buttonRemove.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				int index = propertyList.getSelectionModel().getSelectedIndex();

				for (int i = index; i < items.size() - 1; i++) {
					items.set(i, items.get(i + 1));
				}
				items.remove(items.size() - 1);
				
				properties.remove(index); // update database of current user
			}
		});

		buttonView.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				// int index =
				// propertyList.getSelectionModel().getSelectedIndex();
			}
		});

		buttons.getChildren().addAll(buttonView, buttonRemove);
		buttons.setAlignment(Pos.CENTER);
		grid.add(buttons, 1, 1);
	}

	private void setupPropertyList() {

		propertyList.setPrefHeight(550);

		// Loop based on number of houses saved in profile.
		for (int i = 0; i < properties.size(); i++) {

			HBox listItem = new HBox(10);
			VBox propertyInfo = new VBox(10);
			Rectangle propertyImage = new Rectangle(100, 100, Color.BLUEVIOLET);
			Label propertyAddress = new Label(properties.get(i));
			Label propertyDetails = new Label(
					"No. of Bedrooms: 2\nPrice: �75pppw");

			propertyAddress.setFont(Font.font(null, FontWeight.BOLD, 20));

			propertyInfo.getChildren().addAll(propertyAddress, propertyDetails);
			listItem.getChildren().addAll(propertyImage, propertyInfo);

			items.add(listItem);
		}
		propertyList.setItems(items);

		grid.add(propertyList, 0, 1);
	}
}