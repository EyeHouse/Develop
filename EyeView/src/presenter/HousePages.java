package presenter;

import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;

public class HousePages extends Window {

	private ArrayList<Image> galleryList1, galleryList2, galleryList3;
	private ImageGallery gallery;
	
	public HousePages() {
		
		createHousePagination();
	}
	
	private void createHousePagination() {
		
		galleryList1 = new ArrayList<Image>();
		galleryList2 = new ArrayList<Image>();
		galleryList3 = new ArrayList<Image>();
		
		for (int i = 1; i < 16; i++) {
			galleryList1.add(new Image("file:./resources/houses/Modern-A-House-" + i + ".jpg"));
		}
		
		for (int i = 1; i < 8; i++) {
			galleryList2.add(new Image("file:./resources/houses/Empty-Nester-" + i + ".jpg"));
		}
		
		for (int i = 1; i < 9; i++) {
			galleryList3.add(new Image("file:./resources/houses/modern-apartment-" + i + ".jpg"));
		}
		
        Pagination pagination = new Pagination(3, 0);
        pagination.setPageFactory(new Callback<Integer, Node>() {
            public Node call(Integer pageIndex) {
            	return createHousePage(pageIndex);
            }
        });
        pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
        pagination.relocate(195, 80);
        root.getChildren().add(pagination);
	}

	protected Pane createHousePage(Integer pageIndex) {
		
	    Pane galleryPane = new Pane();
	    galleryPane.setPrefSize(750, 550);
	    galleryPane.getChildren().clear();
	    
	    switch (pageIndex) {
	    case 0:
		    gallery = new ImageGallery(galleryList1, 20, 80);
    		break;
	    case 1:
			gallery = new ImageGallery(galleryList2, 20, 80);
    		break;
	    case 2:
	    	gallery = new ImageGallery(galleryList3, 20, 80);
    		break;
    	default:
    		break;
	    }
		galleryPane.getChildren().add(gallery.getGallery());
	    
	    VBox infoColumn = new VBox();
	    //infoColumn.setPrefWidth(200);
	    infoColumn.setSpacing(20);
	    infoColumn.relocate(450, 150);
	    
        Label price = new Label("£135 pcm");
        price.setFont(Font.font("Calibri", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 36));
        Label desc = new Label("What a lovely house!");
        desc.setFont(Font.font("Calibri", 28));
        
		infoColumn.getChildren().addAll(price, desc);
		
		galleryPane.getChildren().add(infoColumn);
        
        return galleryPane;
	}

}
