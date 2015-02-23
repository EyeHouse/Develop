/**
 * "XMLParser"
 * 
 * Instantiates the 
 *
 * @company EyeHouse Ltd.
 * @version 1.3, 22/02/15
 * @authors Peter
 */


package parser;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class XMLParser extends DefaultHandler {
	
    private Slideshow slideshow = null;
    private Slide currentSlide = null;
	private DocumentInfo info = null;
	private DefaultSettings defaults = null;
	private StringBuffer elementBuffer = null;
    private Image currentImage = null;
   
    public XMLParser()/* throws IOException*/ {
    	
        //loadSlideshow("Example PWS XML.xml");
        //printLists();
    }

    /**
     * This method gets the parser and then starts the parser reading
     * through the XML file.
     * 
     * As the parser reads through the XML file, it will call the 
     * appropriate methods within this class as it encounters new elements, 
     * end elements, characters in the main content of and element, etc.
     * 
     * This method is public so that a controlling class can build an instance
     * of this class and then call this method to read the XML file and return
     * the data.
     * 
     */
    public Slideshow loadSlideshow(String inputFile) {
    	
        try {
            // use the default parser
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            // parse the input
            saxParser.parse(inputFile, this);
        }
        catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
        catch (SAXException saxe) {
            saxe.printStackTrace();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return slideshow;
    }

    /**
     * Called by the parser when it encounters the start of the XML file.
     */
    public void startDocument() throws SAXException {
        System.out.println("Starting to process document.\n");
    }

    /**
     * Called by the parser when it encounters any start element tag.
     */
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        
    	// sort out element name if (no) namespace in use
        String elementName = localName;
        if ("".equals(elementName)) {
            elementName = qName;
        }
        
        if (elementName.equals("slideshow")) {
            if (slideshow == null) {
                slideshow = new Slideshow();
                slideshow.setTitle(attributes.getValue("title"));
                System.out.println("Found slideshow...");
            }
        }
        else if (elementName.equals("documentinfo")) {
        	info = new DocumentInfo();
            System.out.println("\tFound document info...");
        }
        else if (elementName.equals("defaultsettings")) {
            defaults = new DefaultSettings();
            System.out.println("\tFound default settings...");
        }
        else if (elementName.equals("slide")) {
        	if (currentSlide == null) {
        		currentSlide = new Slide(attributes.getValue("id"));
	            currentSlide.setTitle(attributes.getValue("title"));
	            try {
	            	currentSlide.setDuration(Integer.parseInt(attributes.getValue("duration")));
	            } catch (NumberFormatException e) {
	            	
	            }
	            System.out.println("\tFound a slide...");
        	}
        }
        else if (elementName.equals("image")) {
	        currentImage = new Image();
	    	currentImage.setSource(attributes.getValue("sourcefile"));
	    	currentImage.setXstart(attributes.getValue("xstart"));
	    	currentImage.setYstart(attributes.getValue("ystart"));
	    	currentImage.setScale(attributes.getValue("scale"));
	    	currentImage.setDuration(attributes.getValue("duration"));
	    	currentImage.setStarttime(attributes.getValue("starttime"));
	        System.out.println("\t\tFound an image...");
        }
    }

    /**
     * Called by the parser when it encounters characters in the main body of an element.
     */
    public void characters(char[] ch, int start, int length) throws SAXException {
    	
    	String elementString = new String(ch, start, length);
		if (elementBuffer == null) {
			elementBuffer = new StringBuffer(elementString);
		} else {
			elementBuffer.append(elementString);
		}
    }

    /**
     * Called by the parser when it encounters any end element tag.
     */
    public void endElement(String uri, String localName, String qName) throws SAXException {
        
    	// sort out element name if (no) namespace in use
        String elementName = localName;
        if ("".equals(elementName)) {
            elementName = qName;
        }
        
        if (elementName.equals("slide")) {
			slideshow.addSlide(currentSlide);
			currentSlide = null;
		} else if (elementName.equals("documentinfo")) {
			slideshow.setInfo(info);
		} else if (elementName.equals("defaults")) {
			slideshow.setDefaults(defaults);
		} else if (elementName.equals("author")) {
			info.setAuthor(elementBuffer.toString().trim());
			elementBuffer = null;
		} else if (elementName.equals("version")) {
			info.setVersion(elementBuffer.toString().trim());
			elementBuffer = null;
		} else if (elementName.equals("comment")) {
			info.setComment(elementBuffer.toString().trim());
			elementBuffer = null;
		} else if (elementName.equals("groupid")) {
			info.setGroupID(elementBuffer.toString().trim());
			elementBuffer = null;
		} else if (elementName.equals("backgroundcolor")) {
			defaults.setBackgroundColor(elementBuffer.toString().trim());
			elementBuffer = null;
		} else if (elementName.equals("font")) {
			defaults.setFont(elementBuffer.toString().trim());
			elementBuffer = null;
		} else if (elementName.equals("fontsize")) {
			defaults.setFontSize(Integer.parseInt(elementBuffer.toString().trim()));
			elementBuffer = null;
		} else if (elementName.equals("fontcolor")) {
			defaults.setFontColor(elementBuffer.toString().trim());
			elementBuffer = null;
		} else if (elementName.equals("image")) {
    		currentSlide.addImage(currentImage);
    		//currentImage = null;
		}
        //System.out.println(elementName);
    }

    /**
     * Called by the parser when it encounters the end of the XML file.
     */
    public void endDocument() throws SAXException {
        System.out.println("\nFinished processing document.");
    }

    /**
     * Utility method for this class, to output a quick check on the contents
     * that were read in from the XML file.
     */
    public void printLists() {
        System.out.println("\n\nSlideshow Title: " + slideshow.getTitle());
        System.out.println("\tDocument Information");
        System.out.println("\t\tAuthor: " + info.getAuthor());
        System.out.println("\t\tVersion: " + info.getVersion());
        System.out.println("\t\tComment: " + info.getComment());
        System.out.println("\t\tGroup ID: " + info.getGroupID());
        System.out.println("\tDefault Settings");
        System.out.println("\t\tBackground Colour: " + defaults.getBackgroundColor());
        System.out.println("\t\tFont: " + defaults.getFont());
        System.out.println("\t\tFont Size: " + defaults.getFontSize());
        System.out.println("\t\tFont Colour: " + defaults.getFontColor());
        List<Slide> slides = slideshow.getSlides();
        for (Slide slide : slides) {
        	List<Image> images = slide.getImageList();
            System.out.println("\tSlide: " + slide.getTitle());
            for (Image image : images) {
                System.out.println("\t\tImage");
            	System.out.println("\t\t\tSource: " + image.getSource());
            	System.out.println("\t\t\tX: " + image.getXstart());
            	System.out.println("\t\t\tY: " + image.getYstart());
            	System.out.println("\t\t\tScale: " + image.getScale());
            	System.out.println("\t\t\tDuration: " + image.getDuration());
            	System.out.println("\t\t\tStart Time: " + image.getStarttime());
            }
        }
    }

    /*public static void main(String[] args) {
    	
    	new XMLParser();
    }*/

}
