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
 * This class calls the SAX parser to process the input XML file and parse the
 * information into objects of their respective appropriate types.
 * 
 * @version 2.1
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class XMLParser extends DefaultHandler {

	private SlideshowData slideshow;
	private SlideData currentSlide;
	private DocumentInfo info;
	private DefaultSettings defaults;
	private StringBuffer elementBuffer;
	private TextData currentText;
	private GraphicData currentGraphic;
	private ImageData currentImage;
	private AudioData currentAudio;
	private VideoData currentVideo;

	/**
	 * This method gets the parser and then starts the parser reading through
	 * the XML file.
	 * 
	 * As the parser reads through the XML file, it will call the appropriate
	 * methods within this class as it encounters new elements, end elements,
	 * characters in the main content of and element, etc.
	 * 
	 * This method is public so that a controlling class can build an instance
	 * of this class and then call this method to read the XML file and return
	 * the data.
	 * 
	 * @param inputFile
	 *            The file path of the input XML
	 * @return <code>SlideshowData</code> object
	 * 
	 */
	public SlideshowData loadSlideshow(String inputFile) {

		try {
			// Use the default parser
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			// Parse the input
			saxParser.parse(inputFile, this);
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException saxe) {
			saxe.printStackTrace();
		} catch (IOException ioe) {
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
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		// Sort out element name if namespace in use
		String elementName = localName;
		if ("".equals(elementName)) {
			elementName = qName;
		}

		// Create new data objects for storing parsed data
		switch (elementName) {

		case "slideshow":
			slideshow = new SlideshowData();
			slideshow.setTitle(attributes.getValue("title"));
			System.out.println("Found slideshow...");
			break;

		case "documentinfo":
			info = new DocumentInfo();
			System.out.println("\tFound document info...");
			break;

		case "defaultsettings":
			defaults = new DefaultSettings();
			System.out.println("\tFound default settings...");
			break;

		case "slide":
			currentSlide = new SlideData();
			currentSlide.setTitle(attributes.getValue("title"));
			if (attributes.getValue("duration") == null) {
				currentSlide.setDuration(0);
			} else {
				currentSlide.setDuration(Float.parseFloat(attributes
						.getValue("duration")));
			}
			System.out.println("\tFound a slide...");
			break;

		case "text":
			currentText = new TextData();
			currentText.setSource(attributes.getValue("sourcefile"));
			currentText.setXstart(Float.parseFloat(attributes
					.getValue("xstart")));
			currentText.setYstart(Float.parseFloat(attributes
					.getValue("ystart")));
			if (attributes.getValue("font") == null) {
				currentText.setFont(defaults.getFont());
			} else {
				currentText.setFont(attributes.getValue("font"));
			}
			if (attributes.getValue("fontsize") == null) {
				currentText.setFontSize(defaults.getFontSize());
			} else {
				currentText.setFontSize(Integer.parseInt(attributes
						.getValue("fontsize")));
			}
			if (attributes.getValue("fontcolor") == null) {
				currentText.setFontColor(defaults.getFontColor());
			} else {
				currentText.setFontColor(attributes.getValue("fontcolor"));
			}
			if (attributes.getValue("duration") == null) {
				currentText.setDuration(0);
			} else {
				currentText.setDuration(Float.parseFloat(attributes
						.getValue("duration")));
			}
			System.out.println("\t\tFound some text...");
			break;

		case "image":
			currentImage = new ImageData();
			currentImage.setSource(attributes.getValue("sourcefile"));
			currentImage.setXstart(Float.parseFloat(attributes
					.getValue("xstart")));
			currentImage.setYstart(Float.parseFloat(attributes
					.getValue("ystart")));
			if (attributes.getValue("scale") == null) {
				currentImage.setScale(1);
			} else {
				currentImage.setScale(Float.parseFloat(attributes
						.getValue("scale")));
			}
			if (attributes.getValue("duration") == null) {
				currentImage.setDuration(0);
			} else {
				currentImage.setDuration(Float.parseFloat(attributes
						.getValue("duration")));
			}
			if (attributes.getValue("starttime") == null) {
				currentImage.setStarttime(0);
			} else {
				currentImage.setStarttime(Float.parseFloat(attributes
						.getValue("starttime")));
			}
			System.out.println("\t\tFound an image...");
			break;

		case "audio":
			currentAudio = new AudioData();
			currentAudio.setSource(attributes.getValue("sourcefile"));
			if (attributes.getValue("scale") == null) {
				currentAudio.setStarttime(0);
			} else {
				currentAudio.setStarttime(Float.parseFloat(attributes
						.getValue("starttime")));
			}
			System.out.println("\t\tFound a sound...");
			break;

		case "video":
			currentVideo = new VideoData();
			currentVideo.setSource(attributes.getValue("sourcefile"));
			currentVideo.setXstart(Float.parseFloat(attributes
					.getValue("xstart")));
			currentVideo.setYstart(Float.parseFloat(attributes
					.getValue("ystart")));
			System.out.println("\t\tFound a video...");
			break;

		case "graphic":
			currentGraphic = new GraphicData();
			currentGraphic.setType(attributes.getValue("type"));
			currentGraphic.setXstart(Float.parseFloat(attributes
					.getValue("xstart")));
			currentGraphic.setYstart(Float.parseFloat(attributes
					.getValue("ystart")));
			currentGraphic
					.setXend(Float.parseFloat(attributes.getValue("xend")));
			currentGraphic
					.setYend(Float.parseFloat(attributes.getValue("yend")));
			currentGraphic.setSolid(Boolean.parseBoolean(attributes
					.getValue("solid")));
			currentGraphic.setGraphicColor(attributes.getValue("graphiccolor"));
			if (attributes.getValue("duration") == null) {
				currentGraphic.setDuration(0);
			} else {
				currentGraphic.setDuration(Float.parseFloat(attributes
						.getValue("duration")));
			}
			System.out.println("\t\tFound a graphic...");
			break;

		case "cyclicshading":
			currentGraphic.setShadingColor(attributes.getValue("shadingcolor"));
			System.out.println("\t\t\tGraphic contains shading...");
			break;

		default:
			break;
		}
	}

	/**
	 * Called by the parser when it encounters characters in the main body of an
	 * element.
	 */
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		// Creates a string buffer and stores characters found in an element
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
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		// Sort out element name if namespace in use
		String elementName = localName;
		if ("".equals(elementName)) {
			elementName = qName;
		}

		// Stores parsed data in the relevant data object
		switch (elementName) {

		case "slide":
			slideshow.addSlide(currentSlide);
			break;

		case "documentinfo":
			slideshow.setInfo(info);
			break;

		case "defaultsettings":
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
			defaults.setFontSize(Integer.parseInt(elementBuffer.toString()
					.trim()));
			elementBuffer = null;
			break;

		case "fontcolor":
			defaults.setFontColor(elementBuffer.toString().trim());
			elementBuffer = null;
			break;

		case "text":
			if (currentText.getSource() == "") {
				currentText.setSource(elementBuffer.toString().trim());
			}
			elementBuffer = null;
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

		System.out.println("\nFinished processing document.\n");
	}

	/**
	 * Provides a quick check on the contents that were read in from the XML
	 * file by printing out all the values to the console window.
	 */
	public void printLists() {

		System.out.println("\n\nSlideshow Title: " + slideshow.getTitle());
		System.out.println("\tDocument Information");
		System.out.println("\t\tAuthor: " + info.getAuthor());
		System.out.println("\t\tVersion: " + info.getVersion());
		System.out.println("\t\tComment: " + info.getComment());
		System.out.println("\t\tGroup ID: " + info.getGroupID());
		System.out.println("\tDefault Settings");
		System.out.println("\t\tBackground Colour: "
				+ defaults.getBackgroundColor());
		System.out.println("\t\tFont: " + defaults.getFont());
		System.out.println("\t\tFont Size: " + defaults.getFontSize());
		System.out.println("\t\tFont Colour: " + defaults.getFontColor());
		List<SlideData> slides = slideshow.getSlides();
		for (SlideData slide : slides) {
			List<TextData> texts = slide.getTextList();
			List<ImageData> images = slide.getImageList();
			List<AudioData> audios = slide.getAudioList();
			List<VideoData> videos = slide.getVideoList();
			List<GraphicData> graphics = slide.getGraphicList();
			System.out.println("\tSlide: " + slide.getTitle());
			for (TextData text : texts) {
				System.out.println("\t\tText");
				System.out.println("\t\t\tSource: " + text.getSource());
				System.out.println("\t\t\tX: " + text.getXstart());
				System.out.println("\t\t\tY: " + text.getYstart());
				System.out.println("\t\t\tFont: " + text.getFont());
				System.out.println("\t\t\tFontSize: " + text.getFontSize());
				System.out.println("\t\t\tFontColor: " + text.getFontColor());
				System.out.println("\t\t\tDuration: " + text.getDuration());
			}
			for (ImageData image : images) {
				System.out.println("\t\tImage");
				System.out.println("\t\t\tSource: " + image.getSource());
				System.out.println("\t\t\tX: " + image.getXstart());
				System.out.println("\t\t\tY: " + image.getYstart());
				System.out.println("\t\t\tScale: " + image.getScale());
				System.out.println("\t\t\tDuration: " + image.getDuration());
				System.out.println("\t\t\tStart Time: " + image.getStarttime());
			}
			for (AudioData audio : audios) {
				System.out.println("\t\tAudio");
				System.out.println("\t\t\tSource: " + audio.getSource());
				System.out.println("\t\t\tStart Time: " + audio.getStarttime());
			}
			for (VideoData video : videos) {
				System.out.println("\t\tVideo");
				System.out.println("\t\t\tSource: " + video.getSource());
				System.out.println("\t\t\tX: " + video.getXstart());
				System.out.println("\t\t\tY: " + video.getYstart());
			}
			for (GraphicData graphic : graphics) {
				System.out.println("\t\tGraphic");
				System.out.println("\t\t\tType: " + graphic.getType());
				System.out.println("\t\t\tStart X: " + graphic.getXstart());
				System.out.println("\t\t\tStart Y: " + graphic.getYstart());
				System.out.println("\t\t\tEnd X: " + graphic.getXend());
				System.out.println("\t\t\tEnd Y: " + graphic.getYend());
				System.out.println("\t\t\tSolid? " + graphic.isSolid());
				System.out.println("\t\t\tGraphic Colour: "
						+ graphic.getGraphicColor());
				System.out.println("\t\t\tDuration: " + graphic.getDuration());
				System.out.println("\t\t\t\tShading Colour: "
						+ graphic.getShadingColor());
			}
		}
	}

}