/**
 * Description of the class goes here
 *
 * @company EyeHouse Ltd.
 * @version <version>, <date>
 * @authors <name> & <name>
 */


package parser;

import java.io.IOException;
//import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class XMLParser extends DefaultHandler {
	
    private Slideshow slideshow;
    private Slide currentSlide;
	private DocumentInfo info;
	private DefaultSettings defaults;
	private StringBuffer elementBuffer;
    //private Image currentImage;
   
    public XMLParser() throws IOException {
    	
        readXMLFile("Example PWS XML.xml");
        writeSlides();
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
    public Slideshow readXMLFile(String inputFile) {
    	
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
            defaults.setBackgroundColor(attributes.getValue("backgroundcolor"));
            defaults.setFont(attributes.getValue("font"));
            try {
            	defaults.setFontSize(Integer.parseInt(attributes.getValue("fontsize")));
            } catch (NumberFormatException e) {
            	
            }
            defaults.setFontColor(attributes.getValue("fontcolor"));
            System.out.println("\tFound default settings...");
        }
        else if (elementName.equals("slide")) {
            currentSlide = new Slide(attributes.getValue("id"));
            currentSlide.setTitle(attributes.getValue("title"));
            try {
            	currentSlide.setDuration(Integer.parseInt(attributes.getValue("duration")));
            } catch (NumberFormatException e) {
            	
            }
            System.out.println("\tFound a slide...");
        }
        /*else if (elementName.equals("image")) {
        	currentImage = new Image();
            String attributeName = attributes.getLocalName(0);
            System.out.println("\t\tFound an image...");
            if ("".equals(attributeName)) {
                attributeName = attributes.getQName(0);
            }
            
            for (int i=0; i<attributes.getLength(); i++) {
            	//currentImage.
                attributeName = attributes.getLocalName(i);
	        	if (attributeName.equals("urlname"))
	        		currentImage.addProperty(0, attributes.getValue(i));
	        	if (attributeName.equals("xstart"))
	        		currentImage.addProperty(1, attributes.getValue(i));
	        	if (attributeName.equals("ystart"))
	        		currentImage.addProperty(2, attributes.getValue(i));
	        	if (attributeName.equals("width"))
	        		currentImage.addProperty(3, attributes.getValue(i));
	        	if (attributeName.equals("height"))
	        		currentImage.addProperty(4, attributes.getValue(i));
	        	if (attributeName.equals("starttime"))
	        		currentImage.addProperty(5, attributes.getValue(i));
	        	if (attributeName.equals("endtime"))
	        		currentImage.addProperty(6, attributes.getValue(i));
	        	
	        	System.out.println("\t\t\t" + attributeName + ": " + attributes.getValue(i));
        		
            }
        }*/
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
			info = null;
		} else if (elementName.equals("defaults")) {
			slideshow.setDefaults(defaults);
			defaults = null;
		} else if (elementName.equals("author")) {
			info.setAuthor(elementBuffer.toString().trim());
			elementBuffer = null;
		} else if (elementName.equals("version")) {
			info.setVersion(elementBuffer.toString().trim());
			elementBuffer = null;
		} else if (elementName.equals("comment")) {
			info.setComment(elementBuffer.toString().trim());
			elementBuffer = null;
		} else if (elementName.equals("backgroundcolor")) {
			defaults.setBackgroundColor(elementBuffer.toString().trim());
			elementBuffer = null;
		} else if (elementName.equals("font")) {
			defaults.setFont(elementBuffer.toString());
			elementBuffer = null;
		} else if (elementName.equals("fontsize")) {
			defaults.setFontSize(Integer.parseInt(elementBuffer.toString().trim()));
			elementBuffer = null;
		} else if (elementName.equals("fontcolor")) {
			defaults.setFontColor(elementBuffer.toString().trim());
			elementBuffer = null;
		}
        System.out.println(elementName);
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
    private void writeSlides() {
        /*System.out.println("\n\nSlideshow Title: " + slideshow.getTitle());
        List<Slide> slides = slideshow.getSlides();
        List<Image> images = currentSlide.getImages();
        for (Slide slide : slides) {
        	images = slide.getImages();
            System.out.println("\tSlide: " + slide.getID());
            for (Image image : images) {
            	System.out.println("\t\tImage: " + image.getProperties());
            }
        }*/
    }

    public static void main(String[] args) {
    	try {
			new XMLParser();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}
