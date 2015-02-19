/**
 * Description of the class goes here
 *
 * @company EyeHouse Ltd.
 * @version <version>, <date>
 * @authors <name> & <name>
 */


package parser;

import java.util.ArrayList;
import java.util.List;

public class Image {

    public List<Object> properties;
    private static int maxAttributes = 7;
		
	public Image() {
        properties = new ArrayList<Object>();
        for (int i=0; i<maxAttributes; i++)
        	properties.add(null);
	}

	public void addProperty(int index, Object property) {
		properties.set(index, property);
	}

	public List<Object> getProperties() {
		return properties;
	}
}
