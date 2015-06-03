package button;

/**
 * This class contains the necessary information to instantiate a new JavaFX
 * Button object.
 * 
 * @version 1.3 (12.03.15)
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class ButtonType {

	public String colourRGB;
	public String borderColourRGB;
	public String text;
	public double xSize;
	public double ySize;

	/**
	 * Constructor method for new ButtonType class.
	 * 
	 * @param colourRGB
	 *            Button fill colour (RR,GG,BB)
	 * @param borderColourRGB
	 *            Button border colour (RR,GG,BB)
	 * @param text
	 *            Button text
	 * @param xSize
	 *            Button width (Pixels)
	 * @param ySize
	 *            Button height (Pixels)
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