package ch.zhaw.wikitransport.xml;

import java.util.Iterator;
import java.util.List;

/**
 * Defines the function that an Xml parser in this project
 * must implement.
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 *
 * @param <T>
 */
public interface XmlReadable<T> extends Iterator<T>, Iterable<T>{
	
	/**
	 * Parses through the different element of the xml file.
	 * 
	 * @return parsedElements - A list of all the parsed elements out of the xml file.
	 */
	public List<T> getParsedElements();
	
	/**
	 * Reads the index page identifier out of the xml file.
	 * 
	 * @return indexPageIdentifier - A string identifying the meta data index page on the wiki. 
	 */
	public String getIndexPageIdentifier();
}
