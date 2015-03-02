package parser;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class calls the SAX parser to process the input XML file and parse
 * the information into objects of their respective appropriate types.
 *
 * @version 2.0
 * @author  EyeHouse Ltd.
 */
public class XMLParser extends DefaultHandler {
	
    private Slideshow slideshow;
    private Slide currentSlide;
	private DocumentInfo info;
	private DefaultSettings defaults;
	private StringBuffer elementBuffer;
	private Text currentText;
	private Graphic currentGraphic;
    private Image currentImage;
	private Audio currentAudio;
	private Video currentVideo;

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
     * @param inputFile The file path of the input XML
     * @return <code>Slideshow</code> object
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
             slideshow = new Slideshow();
             slideshow.setTitle(attributes.getValue("title"));
             System.out.println("Found slideshow...");
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
        	currentSlide = new Slide();
	        currentSlide.setTitle(attributes.getValue("title"));
	        if (attributes.getValue("duration") == null) {
	        	currentSlide.setDuration(0);
	        } else {
		        currentSlide.setDuration(Float.parseFloat(attributes.getValue("duration")));
	        }
	        System.out.println("\tFound a slide...");
        }
        else if (elementName.equals("text")) {
	        currentText = new Text();
	    	currentText.setSource(attributes.getValue("sourcefile"));
	    	currentText.setXstart(Float.parseFloat(attributes.getValue("xstart")));
	    	currentText.setYstart(Float.parseFloat(attributes.getValue("ystart")));
	    	if (attributes.getValue("font") == null) {
	    		currentText.setFont(defaults.getFont());
	    	} else {
	    		currentText.setFont(attributes.getValue("font"));
	    	}
	    	if (attributes.getValue("fontsize") == null) {
	    		currentText.setFontSize(defaults.getFontSize());
	    	} else {
	    		currentText.setFontSize(Integer.parseInt(attributes.getValue("fontsize")));
	    	}
	    	if (attributes.getValue("fontcolor") == null) {
	    		currentText.setFontColor(defaults.getFontColor());
	    	} else {
	    		currentText.setFontColor(attributes.getValue("fontcolor"));
	    	}
	    	if (attributes.getValue("duration") == null) {
	    		currentText.setDuration(0);
	    	} else {
	    		currentText.setDuration(Float.parseFloat(attributes.getValue("duration")));
	    	}
	    	System.out.println("\t\tFound some text...");
        }
        else if (elementName.equals("image")) {
	        currentImage = new Image();
	    	currentImage.setSource(attributes.getValue("sourcefile"));
	    	currentImage.setXstart(Float.parseFloat(attributes.getValue("xstart")));
	    	currentImage.setYstart(Float.parseFloat(attributes.getValue("ystart")));
	    	if (attributes.getValue("scale") == null) {
	    		currentImage.setScale(1);
	    	} else {
	    		currentImage.setScale(Float.parseFloat(attributes.getValue("scale")));
	    	}
	    	if (attributes.getValue("duration") == null) {
	    		currentImage.setDuration(0);
	    	} else {
	    		currentImage.setDuration(Float.parseFloat(attributes.getValue("duration")));
	    	}
	    	if (attributes.getValue("starttime") == null) {
	    		currentImage.setStarttime(0);
	    	} else {
	    		currentImage.setStarttime(Float.parseFloat(attributes.getValue("starttime")));
	    	}
	        System.out.println("\t\tFound an image...");
        }
        else if (elementName.equals("audio")) {
	        currentAudio = new Audio();
	        currentAudio.setSource(attributes.getValue("sourcefile"));
	        if (attributes.getValue("scale") == null) {
	        	currentAudio.setStarttime(0);
	    	} else {
	    		currentAudio.setStarttime(Float.parseFloat(attributes.getValue("starttime")));
	    	}
	    	System.out.println("\t\tFound a sound...");
        }
        else if (elementName.equals("video")) {
	        currentVideo = new Video();
	        currentVideo.setSource(attributes.getValue("sourcefile"));
	        currentVideo.setXstart(Float.parseFloat(attributes.getValue("xstart")));
	        currentVideo.setYstart(Float.parseFloat(attributes.getValue("ystart")));
	        System.out.println("\t\tFound a video...");
        }
        else if (elementName.equals("graphic")) {
	        currentGraphic = new Graphic();
	        currentGraphic.setType(attributes.getValue("type"));
	        currentGraphic.setXstart(Float.parseFloat(attributes.getValue("xstart")));
	        currentGraphic.setYstart(Float.parseFloat(attributes.getValue("ystart")));
	        currentGraphic.setXend(Float.parseFloat(attributes.getValue("xend")));
	        currentGraphic.setYend(Float.parseFloat(attributes.getValue("yend")));
	        currentGraphic.setSolid(Boolean.parseBoolean(attributes.getValue("solid")));
	        currentGraphic.setGraphicColor(attributes.getValue("graphiccolor"));
	    	if (attributes.getValue("duration") == null) {
	    		currentGraphic.setDuration(0);
	    	} else {
	    		currentGraphic.setDuration(Float.parseFloat(attributes.getValue("duration")));
	    	}
	    	System.out.println("\t\tFound a graphic...");
        }
        else if (elementName.equals("cyclicshading")) {
	        currentGraphic.setShadingColor(attributes.getValue("shadingcolor"));
	    	System.out.println("\t\t\tGraphic contains shading...");
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
        
        switch (elementName) {
	        case "slide":
	        	slideshow.addSlide(currentSlide);
	        	break;
	        case "documentinfo":
	        	slideshow.setInfo(info);
	        	break;
	    	case "defaults":
				slideshow.setDefaults(defaults);
				break;
			case "author":
				info.setAuthor(elementBuffer.toString().trim());
				elementBuffer = null;
				break;
			case "version":
				info.setVersion(elementBuffer.toString().trim());
				elementBuffer = null;
				break;
			case "comment":
				info.setComment(elementBuffer.toString().trim());
				elementBuffer = null;
				break;
			case "groupid":
				info.setGroupID(elementBuffer.toString().trim());
				elementBuffer = null;
				break;
			case "backgroundcolor":
				defaults.setBackgroundColor(elementBuffer.toString().trim());
				elementBuffer = null;
				break;
			case "font":
				defaults.setFont(elementBuffer.toString().trim());
				elementBuffer = null;
				break;
			case "fontsize":
				defaults.setFontSize(Integer.parseInt(elementBuffer.toString().trim()));
				elementBuffer = null;
				break;
			case "fontcolor":
				defaults.setFontColor(elementBuffer.toString().trim());
				elementBuffer = null;
				break;
			case "text":
				if (currentText.getSource() == "") {
					currentText.setSource(elementBuffer.toString().trim());
					elementBuffer = null;
				}
	    		currentSlide.addText(currentText);
				break;
			case "image":
	    		currentSlide.addImage(currentImage);
				break;
			case "audio":
	    		currentSlide.addAudio(currentAudio);
				break;
			case "video":
	    		currentSlide.addVideo(currentVideo);
				break;
			case "graphic":
	    		currentSlide.addGraphic(currentGraphic);
				break;
			default:
				break;
		}
    }

    /**
     * Called by the parser when it encounters the end of the XML file.
     */
    public void endDocument() throws SAXException {
        System.out.println("\nFinished processing document.");
    }

    /**
     * Provides a quick check on the contents that were read in from the XML file
     * by printing out all the values to the console window.
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
        	List<Text> texts = slide.getTextList();
        	List<Image> images = slide.getImageList();
        	List<Audio> audios = slide.getAudioList();
        	List<Video> videos = slide.getVideoList();
        	List<Graphic> graphics = slide.getGraphicList();
            System.out.println("\tSlide: " + slide.getTitle());
            for (Text text : texts) {
                System.out.println("\t\tText");
            	System.out.println("\t\t\tSource: " + text.getSource());
            	System.out.println("\t\t\tX: " + text.getXstart());
            	System.out.println("\t\t\tY: " + text.getYstart());
            	System.out.println("\t\t\tFont: " + text.getFont());
            	System.out.println("\t\t\tFontSize: " + text.getFontSize());
            	System.out.println("\t\t\tFontColor: " + text.getFontColor());
            	System.out.println("\t\t\tDuration: " + text.getDuration());
            }
            for (Image image : images) {
                System.out.println("\t\tImage");
            	System.out.println("\t\t\tSource: " + image.getSource());
            	System.out.println("\t\t\tX: " + image.getXstart());
            	System.out.println("\t\t\tY: " + image.getYstart());
            	System.out.println("\t\t\tScale: " + image.getScale());
            	System.out.println("\t\t\tDuration: " + image.getDuration());
            	System.out.println("\t\t\tStart Time: " + image.getStarttime());
            }
            for (Audio audio : audios) {
                System.out.println("\t\tAudio");
            	System.out.println("\t\t\tSource: " + audio.getSource());
            	System.out.println("\t\t\tStart Time: " + audio.getStarttime());
            }
            for (Video video : videos) {
                System.out.println("\t\tVideo");
            	System.out.println("\t\t\tSource: " + video.getSource());
            	System.out.println("\t\t\tX: " + video.getXstart());
            	System.out.println("\t\t\tY: " + video.getYstart());
            }
            for (Graphic graphic : graphics) {
                System.out.println("\t\tGraphic");
            	System.out.println("\t\t\tType: " + graphic.getType());
            	System.out.println("\t\t\tStart X: " + graphic.getXstart());
            	System.out.println("\t\t\tStart Y: " + graphic.getYstart());
            	System.out.println("\t\t\tEnd X: " + graphic.getXend());
            	System.out.println("\t\t\tEnd Y: " + graphic.getYend());
            	System.out.println("\t\t\tSolid? " + graphic.isSolid());
            	System.out.println("\t\t\tGraphic Colour: " + graphic.getGraphicColor());
            	System.out.println("\t\t\tDuration: " + graphic.getDuration());
            	System.out.println("\t\t\t\tShading Colour: " + graphic.getShadingColor());
            }
        }
    }

}
