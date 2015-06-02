package handlers;

/* Graphic Element class */
public class GraphicElement {

	// Data variables for the graphic
	public String type;
	public float xstart, ystart, xend, yend, duration;
	public boolean solid;
	public String graphiccolor;
	public String shadingcolor;

	/**
	 * A container containing the data values required to place an garphic element on the
	 * screen at a specified x and y position, with a specified time, solid or hollow and
	 * specified graphic and shading color
	 * 
	 * @param type
	 * @param xstart
	 * @param ystart
	 * @param xend
	 * @param yend
	 * @param duration
	 * @param solid
	 * @param graphiccolor
	 * @param shadingcolor
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
