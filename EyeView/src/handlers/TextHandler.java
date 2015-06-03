package handlers;

import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

/**
 * A Text Handler for JavaFX. This class can be used to display text of
 * user-specified placement, size, wrapping, font and color to a
 * javafx.scene.lauput.StackPane. The text can be passed directly or read from a
 * file.
 * 
 * @author Joel Fergusson
 *
 */
public class TextHandler {
	
	private ArrayList<TextElement> textElements;
	private Color defaultColor;
	private Font defaultFont;

	/**
	 * Creates a text handler with the default black, opaque text of size 10,
	 * family times new roman.
	 */
	public TextHandler() {
		
		// Set up default font
		defaultFont = new Font("times new roman", 10);

		// Set up default color
		defaultColor = new Color(0, 0, 0, 1);

		// Initialise array list
		textElements = new ArrayList<TextElement>();
	}

	/**
	 * Creates a TextHandler using the specified default color and font.
	 * 
	 * @param defaultColor
	 *            Default color for text
	 * @param defaultFont
	 *            Default font for text
	 */
	public TextHandler(Color defaultColor, Font defaultFont) {
		
		// Set up default font
		this.defaultFont = defaultFont;

		// Set up default color
		this.defaultColor = defaultColor;

		// Initialise array list
		textElements = new ArrayList<TextElement>();
	}

	/**
	 * Sets the default color for all text elements added after this function is
	 * executed.
	 * 
	 * @param defaultColor
	 */
	public void setDefaultColor(Color defaultColor) {
		
		this.defaultColor = defaultColor;
	}

	/**
	 * Sets the default color for all text elements added after this function is
	 * executed.
	 * 
	 * @param defaultFont
	 */
	public void setDefaultFont(Font defaultFont) {
		
		this.defaultFont = defaultFont;
	}

	/**
	 * Adds a text element with text from the specified file. Will display it at
	 * the specified points, wrapped (0-1 relative to the size of the pane). If
	 * the file cannot be found, an IOException will be thrown. Uses the
	 * object's defaults for font and color, with no timeout.
	 * 
	 * @param filename
	 *            Name of file containing text to display.
	 * @param xpos
	 *            x position of text (0-1 relative to pane width)
	 * @param ypos
	 *            y position of text (0-1 relative to pane height)
	 * @param wrappingWidth
	 *            wrapping width (0-1 relative to pane width, 0 disables
	 *            wrapping.
	 * @throws IOException
	 *             If the file cannot be found, or another IO error occurs when
	 *             reading the file, this exception will be thrown.
	 */
	public void addTextElementByFile(String filename, double xpos, double ypos,
			double wrappingWidth) throws IOException {

		// Pass through with default font and color
		addTextElementByFile(filename, xpos, ypos, wrappingWidth, defaultColor,
				defaultFont, 0);
	}

	/**
	 * Adds a text element with text from the specified file. Will display it at
	 * the specified points, wrapped (0-1 relative to the size of the pane).
	 * Uses the specified font and color. If the file cannot be found, an
	 * IOException will be thrown.
	 * 
	 * @param filename
	 *            Name of file containing text to display.
	 * @param xpos
	 *            x position of text (0-1 relative to pane width)
	 * @param ypos
	 *            y position of text (0-1 relative to pane height)
	 * @param wrappingWidth
	 *            wrapping width (0-1 relative to pane width, 0 disables
	 *            wrapping.
	 * @param color
	 *            Color of text
	 * @param font
	 *            Font of text
	 * @param timeout
	 *            Time for which text will be displayed
	 * 
	 * @throws IOException
	 *             If the file cannot be found, or another IO error occurs when
	 *             reading the file, this exception will be thrown.
	 */
	public void addTextElementByFile(String filename, double xpos, double ypos,
			double wrappingWidth, Color color, Font font, double timeout)
			throws IOException {

		String content;

		// Read file and then pass it to add text element
		content = new String(readAllBytes(get(filename)));

		addTextElement(content, xpos, ypos, wrappingWidth, color, font, timeout);
	}

	/**
	 * Adds a text element that will be displayed at the specified points,
	 * wrapped (0-1 relative to the size of the pane). Uses the object's
	 * defaults for font and color, with no timeout.
	 * 
	 * @param text
	 *            String to be displayed
	 * @param xpos
	 *            x position of text (0-1 relative to pane width)
	 * @param ypos
	 *            y position of text (0-1 relative to pane height)
	 * @param wrappingWidth
	 *            wrapping width (0-1 relative to pane width, 0
	 */
	public void addTextElement(String text, double xpos, double ypos,
			double wrappingWidth) {
		
		addTextElement(text, xpos, ypos, wrappingWidth, defaultColor,
				defaultFont, 0);
	}

	/**
	 * @param text
	 *            String to be displayed
	 * @param xpos
	 *            x position of text (0-1 relative to pane width)
	 * @param ypos
	 *            y position of text (0-1 relative to pane height)
	 * @param wrappingWidth
	 *            wrapping width (0-1 relative to pane width, 0
	 * @param color
	 *            Color of text
	 * @param font
	 *            Font of text
	 * @param timeout
	 *            Time for which text will be displayed
	 */
	public void addTextElement(String text, double xpos, double ypos,
			double wrappingWidth, Color color, Font font, double timeout) {

		// Add text element
		textElements.add(new TextElement(text, xpos, ypos, wrappingWidth,
				color, font, timeout));
	}

	/**
	 * Adds a pre-constructed text element to the handler to be displayed when
	 * display() is called.
	 * 
	 * @param element
	 */
	public void addTextElement(TextElement element) {
		
		textElements.add(element);
	}

	/**
	 * Displays all the text elements that have been added to the TextHandler on
	 * the specified pane.
	 * 
	 * @param pane
	 *            StackPane to display objects on.
	 */
	public void display(StackPane pane) {
		
		// Iterate through text elements, displaying them
		for (TextElement element : textElements) {
			element.display(pane);
		}
	}
}
