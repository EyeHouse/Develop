/**
 * Description of the class goes here
 *
 * @company EyeHouse Ltd.
 * @version 1.3, 21/02/15
 * @authors Peter
 */


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
