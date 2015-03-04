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
	float xResolution;
	float yResolution;
	float xPosition;
	float yPosition;
	float width;
	float height;
	float specifiedWidth;
	float scaleFactor;
	float scaledWidth;
	Group group;

	public ImageHandler(Group group, float xResolution, float yResolution) {
		
		this.group = group;
		this.xResolution = xResolution;
		this.yResolution = yResolution;
	}

	public void ImageElement(String sourcefile, float xstart, float ystart,
			float scale, float duration, float starttime, float specifiedWidth) {
		
		this.sourcefile = sourcefile;
		this.xstart = xstart;
		this.ystart = ystart;
		this.scale = scale;
		this.duration = duration;
		this.starttime = starttime;
		this.specifiedWidth = specifiedWidth;

		xPosition = xResolution * xstart;
		yPosition = yResolution * ystart;
		
		Image house = new Image(sourcefile);
		ImageView houseImage = new ImageView(house);
	
		width = (float)houseImage.getImage().getWidth();
		height = (float)houseImage.getImage().getHeight();
		
		System.out.println("the original width is: " + width);
		System.out.println("the original height is: " + height);
		
		scaleFactor = (specifiedWidth/width);
		scaledWidth = (scaleFactor*width);
			
		houseImage.setFitWidth(((scaledWidth)*scale)); 
		
		houseImage.setX(xPosition);
		houseImage.setY(yPosition);
		
		houseImage.setPreserveRatio(true);
		
		//Visual testing
		System.out.println("the scaled width is: " + scaledWidth);
		System.out.println("the scaled height is: " + (scaleFactor*height));
        
		group.getChildren().add(houseImage);
	}
	
	public float GetImageWidth(){
		
		return width;
	}
}
