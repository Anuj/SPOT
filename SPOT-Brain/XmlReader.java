import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.*;

/**
 * Class used for reading the xml file for the items.
 * @author tbrown126
 *
 */

public class XmlReader {

    /**
     * This method is used to find the other item from one of the pairings that occur in the game.
     * @param item, the item you are trying to find the partner of
     * @return the other item from the pair in the syllabus.xml file
     */
    public static String getOtherItem(String item){
	try {
	    File file = new File("/Users/anuj/Documents/Adobe Flash Builder 4.6/Puppybox/bin-debug/assets/syllabus.xml");
	    DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	    Document doc = dBuilder.parse(file);
	    NodeList first = doc.getElementsByTagName("item1");
	    NodeList second = doc.getElementsByTagName("item2");
	    
	    if (first.getLength() == second.getLength()){
		for (int i =0; i<first.getLength(); i++){
		    if (first.item(i).getTextContent().trim().equals(item)){
			return second.item(i).getTextContent().trim();
		    } else if (second.item(i).getTextContent().trim().equals(item)) {
			return first.item(i).getTextContent().trim();
		    }
		}
		return "error";
	    } else {
		return "error";
	    }
	    
	} catch (Exception e) {
	    System.out.println(e.getMessage());
	    return "error";
	}
    }
	
	/**
	 * Method is used to get all the data for an item. 
	 * At this time this consists of two fields, properties and locations.
	 * @param s, the item questions are going to be asked about
	 * @return a list that consists of two lists, the first is the properties and the second is the locations
	 */
	public static ArrayList<ArrayList<String>> getData(String s){
		ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>(2);
		try {
			File file = new File("./test.xml");
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			NodeList nList = doc.getElementsByTagName(s);
			
			Node nNode = nList.item(0);
			//NodeList attributes = nNode.getChildNodes();
			
			Element t = (Element) nNode;
			NodeList properties = t.getElementsByTagName("property");
			
			ArrayList<String> propertyList = new ArrayList<String>(properties.getLength());
			for (int i=0; i<properties.getLength(); i++){
				Node temp = properties.item(i);
				if (temp.getNodeType() == Node.ELEMENT_NODE){
					propertyList.add(temp.getTextContent().trim());
				}
			}
			
			NodeList locations = t.getElementsByTagName("location");
			
			ArrayList<String> locationList = new ArrayList<String>(locations.getLength());
			for (int i=0; i<locations.getLength(); i++){
				Node temp = locations.item(i);
				if (temp.getNodeType() == Node.ELEMENT_NODE){
					locationList.add(temp.getTextContent().trim());
				}
			}
			ret.add(0, propertyList);
			ret.add(1, locationList);
			
			//ret = new ArrayList<String>(attributes.getLength());
			/**
			for (int i = 0; i < attributes.getLength(); i++){
				Node temp = attributes.item(i);
				if (temp.getNodeType() == Node.ELEMENT_NODE){
					ret.add(temp.getTextContent().trim());
				}
			}
			*/
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			ret.add(new ArrayList<String>());
			ret.add(new ArrayList<String>());
			
		}
		return ret;
	}

}
