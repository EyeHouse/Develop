package parser;

/**
 * Description of the class goes here
 *
 * @version 1.6
 * @author EyeHouse Ltd.
 */
public class Graphic {

	private String type, graphiccolor, shadingcolor;
	private float xstart, ystart, xend, yend, duration;
	private boolean solid;

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setXstart(float xstart) {
		this.xstart = xstart;
	}

	public float getXstart() {
		return xstart;
	}

	public void setYstart(float ystart) {
		this.ystart = ystart;
	}

	public float getYstart() {
		return ystart;
	}

	public void setXend(float xend) {
		this.xend = xend;
	}

	public float getXend() {
		return xend;
	}

	public void setYend(float yend) {
		this.yend = yend;
	}

	public float getYend() {
		return yend;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	public boolean isSolid() {
		return solid;
	}

	public void setGraphicColor(String graphiccolor) {
		this.graphiccolor = graphiccolor;
	}

	public String getGraphicColor() {
		return graphiccolor;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public float getDuration() {
		return duration;
	}

	public void setShadingColor(String shadingcolor) {
		this.shadingcolor = shadingcolor;
	}

	public String getShadingColor() {
		return shadingcolor;
	}

}
