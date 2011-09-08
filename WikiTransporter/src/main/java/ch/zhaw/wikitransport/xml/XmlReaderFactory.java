package ch.zhaw.wikitransport.xml;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.xml.sax.SAXException;

import ch.zhaw.wikitransport.page.Page;


/**
 * Represents a factory to get the appropriate xml parser depending on the
 * current situation.
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 *
 */
public class XmlReaderFactory {
	
	/**
	 * Creates an instance of the chosen xml parser type.
	 * 
	 * @param parserType - An int representing the xmlParserType (1 = DOM), (2 = SAX).
	 * @return obj - The concrete instance of the xmlReader with the appropriate type.
	 * @throws XMLStreamException 
	 * @throws IOException 
	 * @throws TransformerException 
	 * @throws TransformerFactoryConfigurationError 
	 * @throws SAXException 
	 */
	public static XmlReadable<Page> getXmlParser(ParserTypeEnum parserType){
		switch(parserType){
			//It is a DOM parser
			case DOM_PARSER:
				return XmlReaderDOM.newDOMXmlReader();
			//It is a SAX parser;
			case SAX_PARSER:
				return XmlReaderSAX.newSAXXmlReader();
			//It is a StAX parser;
			case StAX_PARSER:
				return XmlReaderStAX.newStAXXmlReader();
		}
		throw new IllegalArgumentException("The parser type " + parserType + " is not recognized.");
	}
}