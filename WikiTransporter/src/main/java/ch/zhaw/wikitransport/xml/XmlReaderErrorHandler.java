package ch.zhaw.wikitransport.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * That class just represents a simple example error handler that could be easily extended, if there is need for a complex
 * error handler.
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 *
 */
public class XmlReaderErrorHandler implements ErrorHandler {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(XmlReaderErrorHandler.class.getName());
	
	/**
	 * A method to catch a parse error.
	 * @param saxParseError - A SAXParseException representing the current parse error.
	 */
	@Override
	public void error(SAXParseException saxParseError) throws SAXException {
		LOGGER.warn("There was an error while parsing the XML-file at line: {}. The error message is: {} ", saxParseError.getLineNumber(), saxParseError.getMessage());
		throw saxParseError;
	}
	
	/**
	 * A method to catch a fatal parse error.
	 * @param saxParseError - A SAXParseException representing the current fatal parse error.
	 */
	@Override
	public void fatalError(SAXParseException saxParseError) throws SAXException {
		LOGGER.error("There was an error while parsing the XML-file at line: {}. The error message is: {} ", saxParseError.getLineNumber(), saxParseError.getMessage());
		throw saxParseError;
	}
	/**
	 * A method to catch a warning.
	 * @param saxParseException - A SAXParseException representing the current parse warning.
	 */
	@Override
	public void warning(SAXParseException saxParseError) throws SAXException {
		LOGGER.info("There was an error while parsing the XML-file at line: {}. The error message is: {} ", saxParseError.getLineNumber(), saxParseError.getMessage());
		throw saxParseError;
	}
}
