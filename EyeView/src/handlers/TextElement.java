package handlers;

import java.util.Timer;
import java.util.TimerTask;

import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/* TODO: rewrite this with getters and setters rather than constructors */

/**
 * Single Text Element with configurable test, x position, y position, wrapping
 * size (all relative to pane size), color and font. 
 * 
 * @author Joel Fergusson
 *
 */
public class TextElement {
	Label label;
	double xpos;
	double ypos;
	double wrappingWidth;
	double timeout;
	
	/**
	 * Creates a text element with the specified parameters.
	 * 
	 * @param text				String to put on javafx pane
	 * @param xpos				x position of text (0-1 relative to pane width)
	 * @param ypos				y position of text (0-1 relative to pane height)
	 * @param wrappingWidth		wrapping width (0-1 relative to pane width, 0 
	 * disables wrapping.
	 * @param color				Color of text
	 * @param font				Font of text
	 * @param timeout				Time (seconds) for which the text should be 
	 * displayed. 0 will display indefinitely. 
	 */
	public TextElement(String text, double xpos, double ypos, 
			double wrappingWidth, Color color, Font font, double timeout) {
		this.label = new Label(text);
		this.xpos = xpos;
		this.ypos = ypos;
		this.wrappingWidth = wrappingWidth;
		this.label.setTextFill(color);
		this.label.setFont(font);
		this.timeout = timeout;
	}
	
	/**
	 * Creates a text element with the default parameters: xpos = 0, ypos = 0,
	 * wrappingWidth = 0 (i.e. no wrapping), duration = 0 (i.e. no timeout).
	 * 
	 * @param text				String to put on javafx pane
	 */
	public TextElement(String text) {
		this.label = new Label(text);
		this.xpos = 0;
		this.ypos = 0;
		this.wrappingWidth = 0;
		this.timeout = 0;
	}
	
	/**
	 * Places the text on the pane passed to it. 
	 * 
	 * @param pane	Pane to display the text on
	 */
	public void display(StackPane pane) {
		// Set wrapping width as a proportion of overall width
		if (wrappingWidth != 0) {
			label.setWrapText(true);
			label.setPrefWidth(pane.getWidth() * wrappingWidth);
		} else {
			label.setWrapText(false);
			label.setPrefWidth(pane.getWidth());
		}
		
		// Add text to pane
		pane.getChildren().add(label);
		
		// Position text in pane
		StackPane.setMargin(label, new Insets(
				pane.getHeight() * ypos, 0, 0, pane.getWidth() * xpos));
		
		// Remove text after duration has elapsed
		if (timeout > 0) {
			new Timer().schedule(new TimerTask() {          
			    @Override
			    public void run() {
			    	label.setVisible(false);
			    }
			}, (long) (timeout * 1000));
		}		
		
	}

	/**
	 * Set the x position of the text.
	 * 
	 * @param xpos 	X position in proportion of pixels from left of the pane.
	 */
	public void setXpos(double xpos) {
		this.xpos = xpos;
	}

	/**
	 * Set the y position of the text.
	 * 
	 * @param ypos 	Y position in proportion of pixels from top of the pane.
	 */
	public void setYpos(double ypos) {
		this.ypos = ypos;
	}
	
	/**
	 * Set the position of the text.
	 * 
	 * @param xpos 	x position in proportion of pixels from top of the pane.
	 * 
	 * @param ypos	y position in pixels from top of the pane.
	 */
	public void setPosition(double xpos, double ypos){
		this.xpos = xpos;
		this.ypos = ypos;
	}

	/**
	 * Sets the wrapping width of the text in pixels. 0 will disable wrapping.
	 * 
	 * @param wrappingWidth Wrapping width in pixels
	 */
	public void setWrappingWidth(double wrappingWidth) {
		this.wrappingWidth = wrappingWidth;
	}

	/**
	 * Sets the timeout for the text in seconds. Once display is called, the 
	 * text will disappear after this time has elapsed. 0 disables timeout.
	 * 
	 * @param timeout Time in seconds for the text to be visible.
	 */
	public void setTimeout(double timeout) {
		this.timeout = timeout;
	}
	
	/**
	 * Sets the colour of the text
	 * 
	 * @param color
	 */
	public void setColor(Color color) {
		this.label.setTextFill(color);
	}
	
	/**
	 * Sets the font of the text
	 * 
	 * @param font
	 */
	public void setFont(Font font) {
		this.label.setFont(font);
	}
}
