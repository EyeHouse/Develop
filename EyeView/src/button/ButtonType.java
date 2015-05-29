package button;

public class ButtonType {
	public String colourRGB;
	public String borderColourRGB;
	public String text;
	public double xSize;
	public double ySize;

	/**
	 * Button variable container
	 * 
	 * @param colourRGB
	 *            Button fill colour (RR,GG,BB).
	 * @param borderColourRGB
	 *            Button border colour (RR,GG,BB).
	 * @param text
	 *            Button text.
	 * @param xSize
	 *            Button width (Pixels).
	 * @param ySize
	 *            Button height (Pixels).
	 */
	public ButtonType(String colourRGB, String borderColourRGB, String text,
			double xSize, double ySize) {
		this.colourRGB = colourRGB;
		this.borderColourRGB = borderColourRGB;
		this.text = text;
		this.xSize = xSize;
		this.ySize = ySize;
	}
}