package handlers;

/**
 * This class contains the data values required to place a graphic element on
 * the screen from specified x and y start and end positions, with a specified
 * time, solid property, and specified shape and shading color.
 * 
 * @version 1.2 (05.03.15)
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class GraphicElement {

	// Data variables for the graphic
	public String type;
	public float xstart, ystart, xend, yend, duration;
	public boolean solid;
	public String graphiccolor;
	public String shadingcolor;

	/**
	 * Constructor method for GraphicElement object.
	 * 
	 * @param type
	 *            The shape to be drawn on the canvas ( rectangle/oval/line )
	 * @param xstart
	 *            Horizontal position of the top-left of the graphic, relative
	 *            to the resolution of the screen
	 * @param ystart
	 *            Vertical position of the top-left of the graphic, relative to
	 *            the resolution of the screen
	 * @param xend
	 *            Horizontal position of the bottom-right of the graphic,
	 *            relative to the resolution of the screen
	 * @param yend
	 *            Vertical position of the bottom-right of the graphic, relative
	 *            to the resolution of the screen
	 * @param duration
	 *            Length of time that the graphic is displayed on the screen
	 * @param solid
	 *            True if the shape is a solid fill colour, false if it is a
	 *            stroke outline
	 * @param graphiccolor
	 *            Fill colour of the graphic object
	 * @param shadingcolor
	 *            Cyclic shading colour of the graphic object
	 */
	public GraphicElement(String type, float xstart, float ystart, float xend,
			float yend, float duration, boolean solid, String graphiccolor,
			String shadingcolor) {

		this.type = type;
		this.xstart = xstart;
		this.ystart = ystart;
		this.xend = xend;
		this.yend = yend;
		this.duration = duration;
		this.solid = solid;
		this.graphiccolor = graphiccolor;
		this.shadingcolor = shadingcolor;
	}
}
