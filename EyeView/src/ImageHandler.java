import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageHandler {

	String sourcefile;
	float xstart;
	float ystart;
	float scale;
	float duration;
	float starttime;
	Group group;
	float xResolution;
	float yResolution;

	public ImageHandler(Group group, float xResolution, float yResolution) {
		
		this.group = group;
		this.xResolution = xResolution;
		this.yResolution = yResolution;
	}

	public void ImageElement(String sourcefile, float xstart, float ystart,
			float scale, float duration, float starttime) {

		float xPosition;
		float yPosition;
		float width;
		float height;
		
		this.sourcefile = sourcefile;
		this.xstart = xstart;
		this.ystart = ystart;
		this.scale = scale;
		this.duration = duration;
		this.starttime = starttime;

		xPosition = xResolution * xstart;
		yPosition = yResolution * ystart;
		
		Image house = new Image(sourcefile);
		ImageView houseImage = new ImageView(house);
	
		houseImage.setX(xPosition);
		houseImage.setY(yPosition);
				
		group.getChildren().add(houseImage);
	}

}
