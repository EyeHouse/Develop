package Images;

public class ImageType {
	
	public float xstart;
	public float ystart;
	public float scale;
	public float duration;
	public float starttime;
	public float specifiedWidth;

	public ImageType(float xstart, float ystart, float scale,
			float duration, float starttime, float specifiedWidth) {
		
		this.xstart = xstart;
		this.ystart = ystart;
		this.scale = scale;
		this.duration = duration;
		this.starttime = starttime;
		this.specifiedWidth = specifiedWidth;
	}
}
