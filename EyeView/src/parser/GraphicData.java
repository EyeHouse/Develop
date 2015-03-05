package parser;

/**
 * This class creates a new <code>GraphicData</code> object. It contains the
 * information about the graphic, including the type of shape, the (x,y)
 * positions of the top-left and bottom-right corners of the graphic, whether
 * the graphic is solid or an outline, as well as the colour of the shape, the
 * colour of the cyclic shading, and the duration it is displayed on the screen
 * for.
 * 
 * @version 2.1
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class GraphicData {

	private String type, graphiccolor, shadingcolor;
	private float xstart, ystart, xend, yend, duration;
	private boolean solid;

	/**
	 * Sets the type of shape to be drawn.
	 * 
	 * @param type
	 *            Name of shape type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Returns the type of shape to be drawn.
	 * 
	 * @return Name of shape type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the horizontal position of the top-left corner of the graphic.
	 * 
	 * @param xstart
	 *            Position of the top-left corner
	 */
	public void setXstart(float xstart) {
		this.xstart = xstart;
	}

	/**
	 * Returns the horizontal position of the top-left corner of the graphic.
	 * 
	 * @return Position of the top-left corner
	 */
	public float getXstart() {
		return xstart;
	}

	/**
	 * Sets the vertical position of the top-left corner of the graphic.
	 * 
	 * @param ystart
	 *            Position of the top-left corner
	 */
	public void setYstart(float ystart) {
		this.ystart = ystart;
	}

	/**
	 * Returns the vertical position of the top-left corner of the graphic.
	 * 
	 * @return Position of the top-left corner
	 */
	public float getYstart() {
		return ystart;
	}

	/**
	 * Sets the horizontal position of the bottom-right corner of the graphic.
	 * 
	 * @param xstart
	 *            Position of the top-left corner
	 */
	public void setXend(float xend) {
		this.xend = xend;
	}

	/**
	 * Returns the horizontal position of the bottom-right corner of the
	 * graphic.
	 * 
	 * @return Position of the top-left corner
	 */
	public float getXend() {
		return xend;
	}

	/**
	 * Sets the vertical position of the bottom-right corner of the graphic.
	 * 
	 * @param xstart
	 *            Position of the top-left corner
	 */
	public void setYend(float yend) {
		this.yend = yend;
	}

	/**
	 * Returns the vertical position of the bottom-right corner of the graphic.
	 * 
	 * @return Position of the top-left corner
	 */
	public float getYend() {
		return yend;
	}

	/**
	 * Sets whether the shape is solid or not.
	 * 
	 * @param solid
	 *            True/False condition
	 */
	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	/**
	 * Returns whether the shape is solid or not.
	 * 
	 * @return True/False condition
	 */
	public boolean isSolid() {
		return solid;
	}

	/**
	 * Sets the colour of the graphic.
	 * 
	 * @param graphiccolor
	 *            Graphic colour (8-digit hex)
	 */
	public void setGraphicColor(String graphiccolor) {
		this.graphiccolor = graphiccolor;
	}

	/**
	 * Returns the colour of the graphic.
	 * 
	 * @return Graphic colour (8-digit hex)
	 */
	public String getGraphicColor() {
		return graphiccolor;
	}

	/**
	 * Sets the duration that the graphic stays visible for on the slide.
	 * (default 0)
	 * 
	 * @param duration
	 *            Duration in seconds
	 */
	public void setDuration(float duration) {
		this.duration = duration;
	}

	/**
	 * Returns the duration that the image stays visible for on the slide.
	 * (default 0)
	 * 
	 * @return Duration in seconds
	 */
	public float getDuration() {
		return duration;
	}

	/**
	 * Sets the colour of the graphic.
	 * 
	 * @param shadingcolor
	 *            Graphic's shading colour (8-digit hex)
	 */
	public void setShadingColor(String shadingcolor) {
		this.shadingcolor = shadingcolor;
	}

	/**
	 * Returns the colour of the graphic.
	 * 
	 * @return Graphic's shading colour (8-digit hex)
	 */
	public String getShadingColor() {
		return shadingcolor;
	}

}
