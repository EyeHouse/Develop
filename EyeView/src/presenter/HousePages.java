package presenter;

import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

public class HousePages extends Window {

	private ArrayList<Image> galleryList1, galleryList2, galleryList3;
	private ImageGallery gallery1, gallery2, gallery3;
	
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
		
	    Pane housePane = new Pane();
	    housePane.setPrefSize(750, 550);
	    housePane.getChildren().clear();
	    
	    switch (pageIndex) {
	    case 0:
		    gallery1 = new ImageGallery(galleryList1, 40, 80);
	    	housePane.getChildren().add(gallery1.getGallery());
    		break;
	    case 1:
			gallery2 = new ImageGallery(galleryList2, 40, 80);
	    	housePane.getChildren().add(gallery2.getGallery());
    		break;
	    case 2:
			gallery3 = new ImageGallery(galleryList3, 40, 80);
	    	housePane.getChildren().add(gallery3.getGallery());
    		break;
    	default:
    		break;
	    }
	    
        Label desc = new Label("Random Text Goes Here!!!");
        desc.setContentDisplay(ContentDisplay.CENTER);
        
        housePane.getChildren().add(desc);
        
        return housePane;
	}

}
