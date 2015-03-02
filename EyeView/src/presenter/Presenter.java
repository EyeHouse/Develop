package presenter;

import parser.XMLParser;

/**
 * Description of the class goes here
 *
 * @version 1.3
 * @author EyeHouse Ltd.
 */
public class Presenter {
	
	public static void runSlideshow() {
		
		XMLParser parser = new XMLParser();
		parser.loadSlideshow("Example PWS XML.xml");
		parser.printLists();
	}
	
	public static void main(String[] args) {
		
		runSlideshow();
	}
}
