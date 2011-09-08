package ch.zhaw.wikitransport.xml;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ch.zhaw.wikitransport.page.Page;
import ch.zhaw.wikitransport.util.WikiTransporterCfg;

/**
 * THIS CLASS DOES NOT WORK BECAUSE OF A MAJOR BUG IN THE StAX-PARSER NOT FIXED
 * SO FAR: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6440214
 * 
 * BUG DOES CURRENTLY NOT OCCUR ON MAC OSX BUT ON DEBIAN AND WINDOWS ENVIRONMENT
 * 
 * That is the class for parsing the xml file with the different wiki doc pages
 * as content.
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 * 
 */
class XmlReaderStAX extends DefaultHandler implements XmlReadable<Page>{
	private static final Logger LOGGER = LoggerFactory.getLogger(XmlReaderStAX.class.getName());
	private static final WikiTransporterCfg CFG = WikiTransporterCfg.getInstance();
	private static final String WIKI_DOCLET_XSD = "wikidoclet.xsd";
	private XMLEventReader parser;
	private XMLEvent event;
	
	private String indexPageIdentifier = null;
	private boolean isTitle = false;
	private boolean isContent = false;
	private boolean isHash = false;
	private boolean isTime = false;
	private Page nextPage = null;
	

	/**
	 * Needed to control access on the class. To leave iterator always in a consistent state.
	 * 
	 * @return XmlReaderStAX - A XmlReadable<Page> an instance of an event-driven xml-parser.
	 */
	public static XmlReadable<Page> newStAXXmlReader() {
		return new XmlReaderStAX();
	}
	
	/**
	 * Default constructor starts the whole initalizing process of the parser.
	 * 
	 */
	private XmlReaderStAX(){
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
	 * It is received by the first run through.
	 * 
	 * @return indexPageIdentifier
	 */
	public String getIndexPageIdentifier() {
		return indexPageIdentifier;
	}

	private void findIndexPageIdentifierInXml() throws XMLStreamException {
		StartElement startEl;
		String startElName;
		Attribute attribute;
		
		if(this.indexPageIdentifier == null){
			while(parser.hasNext()){
				event = parser.nextEvent();
				if(event.getEventType() == XMLStreamConstants.START_ELEMENT){
						startEl = event.asStartElement();
						startElName = startEl.getName().toString();
						
						if(startElName.equals(WikiTransporterCfg.WIKI_DOCLET_XML_ROOT)){
						
						
						for(Iterator<?> attributes = startEl.getAttributes(); attributes.hasNext();){
							attribute =(Attribute) attributes.next();
							
							if(WikiTransporterCfg.WIKI_DOCLET_INDEX_PAGE_NAME_ATTRIBUTE.equals(attribute.getName().toString())){
								this.setIndexPageIdentifier(attribute.getValue());
								return;
							}
						}
						
					}
				}
			}
		}
	}

	private void init(){
		//Read in wikidoclet xml as stream.
		StreamSource in = new StreamSource(CFG.getConfigValue(WikiTransporterCfg.CFG_PAR_XML));
		
		//Create XMLInputFactory and set the necessary properties.
		XMLInputFactory factory = XMLInputFactory.newInstance();
		
		factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
		
		//Needed to support multilined content betweent tags. --> As content value!
		factory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);
		
		//Get a Schema factory to be able to validate xml against current scheme.
		SchemaFactory schemaFactory = SchemaFactory.newInstance( "http://www.w3.org/2001/XMLSchema" );
		
		try {
			
			//Link current scheme
			Schema schemaGrammar = schemaFactory.newSchema( new Source[] { new StreamSource(getClass().getResourceAsStream(WIKI_DOCLET_XSD))});
			
			//Create validator that is used for validating...
			Validator schemaValidator = schemaGrammar.newValidator();
			
			//Set the error handler
			schemaValidator.setErrorHandler(new XmlReaderErrorHandler());
			
			//Validate with current error handler set.
			schemaValidator.validate(in);
			
			//If there were no errors while validating create the parser.
			parser =factory.createXMLEventReader(in);
			
			//Firstly, find the IndexPageIdentifer. -->It is an attribute on the root start tag.
			findIndexPageIdentifierInXml();
		
		} catch (SAXException e) {
			LOGGER.error("There was a SAXException. The error message was {}", e);
			throw new Error(e);
		} catch (IOException e) {
			LOGGER.error("There was a IOException. The error message was {}", e);
			throw new Error(e);
		} catch (XMLStreamException e) {
			LOGGER.error("There was a XMLStreamException. The error message was {}", e);
			throw new Error(e);
		}
		
	}
	
	/**
	 * Checks whether there is a next event or not
	 * 
	 * @return hasNext - true, if there is a next event, otherwise false.
	 */
	@Override
	public boolean hasNext() {
		try {
			if(parser.hasNext()){
				 XMLEvent checkElementEvent = parser.peek();
				if (checkElementEvent.getEventType() == XMLStreamConstants.END_ELEMENT
						&& checkElementEvent
								.asEndElement()
								.getName()
								.toString()
								.equals(WikiTransporterCfg.WIKI_DOCLET_XML_ROOT)) {

					LOGGER.trace("XML StAX parser ended");

					// close parser properly
					parser.close();
					return false;

				}
				 return true;
			}
		} catch (XMLStreamException e) {
			LOGGER.error("There was a XMLStreamException. The error message was {}", e);
		}
		return false;
	}

	/**
	 * Parses through the next element that is a whole page, if possible. If it is not possible
	 * throughs an error.
	 * 
	 * @return nextPage - the next page in the xml-file.
	 */
	@Override
	public Page next() {
		try {
			if(parser.hasNext()){
				createNextElement();
			}
			else{
				throw new NoSuchElementException(
				"There is no such element in the list. Please make sure that you access the iterator in a correct manner within its boundary!");
			}
		} catch (XMLStreamException e) {
			LOGGER.error("XML-Reader parser stream exception" , e);
		}
		return nextPage;
	}

	private void createNextElement() throws XMLStreamException {
		
		StartElement startEl;
		String startElName;
		EndElement endEl;
		String endElName;
		Characters chars;
		
		while(parser.hasNext()){
			
			//consume next event
			event = parser.nextEvent();
			
			//divide events from each other
			switch(event.getEventType()){
				
				case XMLStreamConstants.START_DOCUMENT:
					LOGGER.trace("XML StAX parser started");
					break;
				
				case XMLStreamConstants.START_ELEMENT:
					
					startEl = event.asStartElement();
					startElName = startEl.getName().toString();
					
					if(startElName.equals(WikiTransporterCfg.WIKI_DOCLET_XML_TITLE)){
						nextPage = new Page();
						isTitle = true;
					}
					else if(startElName.equals(WikiTransporterCfg.WIKI_DOCLET_XML_CONTENT)){
						isContent = true;
					}
					else if(startElName.equals(WikiTransporterCfg.WIKI_DOCLET_XML_HASH)){
						isHash = true;
					}
					else if(startElName.equals(WikiTransporterCfg.WIKI_DOCLET_XML_TIME)){
						isTime = true;
					}
					break;
					
				case XMLStreamConstants.CHARACTERS:
					
					chars = event.asCharacters();
					
					if(isTitle){
						nextPage.setTitleValue(chars.getData());
					}
					else if(isContent){
						nextPage.setContentValue(chars.getData());
					}
					else if(isHash){
						nextPage.setHashValue(chars.getData());
					}
					else if(isTime){
						nextPage.setTimeValue(chars.getData());
					}
					break;
					
				case XMLStreamConstants.END_ELEMENT:
					endEl = event.asEndElement();
					endElName = endEl.getName().toString();
					
					unsetControlVars();
					if(endElName.equals(WikiTransporterCfg.WIKI_DOCLET_XML_PAGE)){
						return;
					}
					break;
					
				default:
					break;
			}
		}
	}

	private void unsetControlVars() {
		isTitle = false;
		isContent = false;
		isHash = false;
		isTime = false;
	}

	/**
	 * Not supported!
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException("This operation is not supported!");
	}

	/**
	 * Returns this class as instance. You are able to go through the different pages by iterator.
	 * For consistency reason of the iterator. You can instantiate this class only over a factory pattern method.
	 * 
	 * @return this - XmlReadable<Page> 
	 */
	@Override
	public Iterator<Page> iterator() {
		return this;
	}

	/**
	 * Convenience method to get all elements at once.
	 * 
	 * @return currentPageList - A list of pages
	 */
	@Override
	public List<Page> getParsedElements() {
		List<Page> currentPagesList = new LinkedList<Page>();
		
		for(Page nextPage : this){
			currentPagesList.add(nextPage);
		}
		return currentPagesList;
	}
}