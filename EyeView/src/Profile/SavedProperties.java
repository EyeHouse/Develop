package Profile;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import presenter.SlideContent;

public class SavedProperties extends presenter.Window{
	
	public SavedProperties(){
		GridPane grid = new GridPane();
		grid.relocate(195, 80);
		Label label = new Label("Test");
		grid.add(label, 0, 0);
		
		SlideContent.setupBackButton();
		
		root.getChildren().add(grid);
	}
}
