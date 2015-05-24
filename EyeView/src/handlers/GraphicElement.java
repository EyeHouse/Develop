package handlers;

/* Graphic Element class */
public class GraphicElement {

	public String type;
	public float xstart, ystart, xend, yend, duration;
	public boolean solid;
	public String graphiccolor;
	public String shadingcolor;

	public GraphicElement(String type, float xstart, float ystart,
			float xend, float yend, float duration, boolean solid,
			String graphiccolor, String shadingcolor) {
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

