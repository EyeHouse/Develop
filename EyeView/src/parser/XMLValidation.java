package parser;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

/**
 * Description of the class goes here
 *
 * @version 1.2
 * @author EyeHouse Ltd.
 */
public class XMLValidation {

	static String xsdFile = new String("PWS Schema.xsd");
	static String xmlFile = new String("Example PWS XML.xml");
	
    public static void main(String[] args) {
    	
    	validateXML(xsdFile, xmlFile);
    }
     
    public static void validateXML(String xsdPath, String xmlPath) {
         
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
        } catch (IOException | SAXException e) {
            System.out.println("Exception: " + e.getMessage() + "\n");
            System.out.println(xmlFile + " does not validate against " + xsdFile);
            System.exit(-1);
        }
        System.out.println(xmlFile + " validates against " + xsdFile);
    }
}