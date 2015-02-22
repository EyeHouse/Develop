
package presenter;

import parser.Slideshow;
import parser.XMLParser;

public class Presenter {
	
	public static Slideshow slideshow;
	
	public static void runSlideshow() {
		
		XMLParser parser = new XMLParser();
		slideshow = parser.loadSlideshow("Example PWS XML.xml");
		parser.printLists();
	}
	
	public static void main(String[] args) {
		
		runSlideshow();
	}
}
