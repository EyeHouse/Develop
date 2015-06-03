package parser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * This test checks that all the correct information is 
 * retrieved from the example xml file 
 * 
 * @version 3.8 (02.06.15)
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class XMLParserTest {

	private XMLParser parser;
	private SlideshowData slideshow;
	
	/**
	 * This method sets up the xml parser for the tests.
	 */
	@Before
	public void setUp() throws Exception {
		parser = new XMLParser();
		slideshow = parser.loadSlideshow("Example PWS XML.xml");
	}
	
	/**
	 * This test checks that there is an instance of every type and that
	 * each type can be retrieved from the xml correctly.
	 */
	@Test
	public void PaserShouldFindCollectReleventInformationAndStoreInCorrectList() {		
		assertTrue(slideshow.getSlides().get(0) instanceof SlideData);
		assertTrue(slideshow.getSlides().get(2).getGraphicList().get(0) instanceof GraphicData);
		assertTrue(slideshow.getSlides().get(1).getImageList().get(0) instanceof ImageData);
		assertTrue(slideshow.getSlides().get(0).getVideoList().get(0) instanceof VideoData);
		assertTrue(slideshow.getSlides().get(2).getAudioList().get(0) instanceof AudioData);
		assertTrue(slideshow.getSlides().get(0).getTextList().get(0) instanceof TextData);
	}
	
	/**
	 * This test checks that information about each type can be read correctly from
	 * the xml file.
	 */
	@Test
	public void ListsShouldContainCorrectInformation() {
		assertEquals(slideshow.getInfo().getAuthor(), "EyeHouse");
		
		assertEquals(slideshow.getTitle(), "EyeView");
		
		//assertEquals(slideshow.getSlides().get(0).getDuration(), "5");
		assertEquals(slideshow.getSlides().get(1).getImageList().get(0).getSource(), 
				"http://2.bp.blogspot.com/-wXSkXwW06FI/UGCr1wbLWSI/AAAAAAAAAsU/RVSgLdrL-QI/s1600/Ensemble.png");
		assertEquals(slideshow.getSlides().get(2).getAudioList().get(0).getSource(), 
				"http://playthegame.co.nf/audio/bottle.wav");
		
		assertEquals(slideshow.getSlides().get(0).getVideoList().get(0).getSource(), 
				"http://download.oracle.com/otndocs/javafx/JavaRap_ProRes_H264_768kbit_Widescreen.mp4");
		assertEquals(slideshow.getSlides().get(2).getGraphicList().get(0).getType(), "rectangle");
		assertEquals(slideshow.getSlides().get(3).getTextList().get(0).getSource(), "Fourth Slide");
	}
}
