package ch.zhaw.wikitransport.xml;

import java.io.File;
import java.io.IOError;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import ch.zhaw.wikitransport.page.Page;
import ch.zhaw.wikitransport.util.WikiTransporterCfg;

/**
 * That is the class for parsing the xml file with
 * the different wiki doc pages as content.
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 *
 */
public class XmlReaderSAX extends DefaultHandler implements XmlReadable<Page>{
	private Logger LOGGER;
	private static final WikiTransporterCfg CFG = WikiTransporterCfg.getInstance();
	private static final String WIKI_DOCLET_XSD = "wikidoclet.xsd";
	
	private String indexPageIdentifier = null;
	private boolean isTitle = false;
	private boolean isContent = false;
	private boolean isHash = false;
	private boolean isTime = false;
	private List<Page> pageHolder = new LinkedList<Page>();
	private Page tmp;
	
	/**
	 * just for debugging.
	 * @param args
	 */
	public static void main(String[] args){
		XmlReaderSAX myReader = new XmlReaderSAX();
		
		System.out.println("INDEX PAGE ATTRIBUTE: " + myReader.getIndexPageIdentifier());
		
		for(Page p : myReader.getParsedElements()){
			System.out.println("PAGE_TITLE: " + p.getTitleValue()); 
			System.out.println("PAGE_HASH: " + p.getHashValue());
			System.out.println("PAGE_TIME:  " + p.getTimeValue() + "\n");
		}
	}
	
	/**
	 * Needed to control access on the class. To leave iterator always in a consistent state.
	 * 
	 * @return XmlReaderSAX - A XmlReadable<Page> an instance of an event-driven xml-parser.
	 */
	public static XmlReadable<Page> newSAXXmlReader() {
		return new XmlReaderSAX();
	}
	
	/**
	 * Default constructor starts the whole initalizing process of the parser.
	 */
	private XmlReaderSAX(){
		init();
	}

	/**
	 * Setter method to set the index page identifier.
	 * 
	 * @param indexPageIdentifier - A String representing the index page identifier.
	 */
	public void setIndexPageIdentifier(String indexPageIdentifier) {
		this.indexPageIdentifier = indexPageIdentifier;
	}

	/**
	 * Method for receiving the current index page identifier.
	 * 
	 * @return indexPageIdentifier
	 */
	public String getIndexPageIdentifier() {
		return indexPageIdentifier;
	}

	/**
	 * Called while parser begins to read the xml file.
	 * Called only once at the beginning.
	 */
	@Override
	public void startDocument() throws SAXParseException {
		LOGGER.trace("XML SAX parser started");
	}
	
	/**
	 * Called while parser ends to read the xml file.
	 * Called only once.
	 */
	@Override
	public void endDocument() throws SAXParseException {
		LOGGER.trace("XML SAX parser ended");
	}
	
	/**
	 * Called when a start tag is being recognised by the parser.
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		if(qName.equals(WikiTransporterCfg.WIKI_DOCLET_XML_TITLE)){
			tmp = new Page();
			pageHolder.add(tmp);
		}
		
		if(qName.equals(WikiTransporterCfg.WIKI_DOCLET_XML_ROOT)){
			this.setIndexPageIdentifier(attributes.getValue(WikiTransporterCfg.WIKI_DOCLET_INDEX_PAGE_NAME_ATTRIBUTE));
		}
		else if(qName.equals(WikiTransporterCfg.WIKI_DOCLET_XML_TITLE)){
			isTitle = true;
		}
		else if(qName.equals(WikiTransporterCfg.WIKI_DOCLET_XML_CONTENT)){
			isContent = true;
		}
		else if(qName.equals(WikiTransporterCfg.WIKI_DOCLET_XML_HASH)){
			isHash = true;
		}
		else if(qName.equals(WikiTransporterCfg.WIKI_DOCLET_XML_TIME)){
			isTime = true;
		}
	}
	
	/**
	 * Called for each chunk of character data between starting and end tag.
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		
		if(isTitle){
			tmp.setTitleValue(ch,start,length);
		}
		else if(isContent){
			tmp.setContentValue(ch,start, length);
		}
		else if(isHash){
			tmp.setHashValue(ch,start,length);
		}
		else if(isTime){
			tmp.setTimeValue(ch,start,length);
		}
	}
	
	/**
	 * Called when an end tag is being recognised by the parser.
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		isTitle = false;
		isContent = false;
		isHash = false;
		isTime = false;
	}

	/**
	 * Receive all parsed elements in a list.
	 * @return pageHolder - A List with page elements basically to upload to the MediaWiki.
	 */
	@Override
	public List<Page> getParsedElements() {
		return pageHolder;
	}
	
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
	
	private void init(){
		LOGGER = LoggerFactory.getLogger(XmlReaderSAX.class);

		SAXParserFactory saxFactory = SAXParserFactory.newInstance();
		SAXParser saxParser;
		saxFactory.setValidating(false);
		saxFactory.setNamespaceAware(true);
		
		//check xml against scheme
		SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		try {
			saxFactory.setSchema(schemaFactory.newSchema(new Source[] { new StreamSource(getClass().getResourceAsStream(WIKI_DOCLET_XSD))}));
			saxParser = saxFactory.newSAXParser();
			saxParser.parse(new File(CFG.getConfigValue(WikiTransporterCfg.CFG_PAR_XML)), this);
			//xmlReader = saxParser.getXMLReader();
			//xmlReader.setErrorHandler(new XmlReaderErrorHandler());
	        //xmlReader.parse(new InputSource(/*CFG.getConfigValue(Configurator.CONFIG_PARAMETER_PATH) + File.separator +*/ CFG.getConfigValue(Configurator.CONFIG_PARAMETER_FILE_NAME)));

		} catch (SAXException e) {
			LOGGER.error(e.getMessage());
			throw new Error(e);
		} catch (ParserConfigurationException e) {
			LOGGER.error(e.getMessage());
			throw new Error(e);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			throw new IOError(e);
		}
	}

	/**
	 * To parse the wikidoclet.xml file element by element the StAX parser has to be used.
	 */
	@Override
	public boolean hasNext() {
		throw new UnsupportedOperationException("This method is not supported by this parser!");
	}

	/**
	 * To parse the wikidoclet.xml file element by element the StAX parser has to be used.
	 */
	@Override
	public Page next() {
		throw new UnsupportedOperationException("This method is not supported by this parser!");
	}

	/**
	 * To parse the wikidoclet.xml file element by element the StAX parser has to be used.
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException("This method is not supported by this parser!");
	}

	/**
	 * To parse the wikidoclet.xml file element by element the StAX parser has to be used.
	 */	
	@Override
	public Iterator<Page> iterator() {
		throw new UnsupportedOperationException("This method is not supported by this parser!");
	}
}