package ch.zhaw.wikitransport;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ch.zhaw.wikitransport.page.Page;
import ch.zhaw.wikitransport.xml.ParserTypeEnum;
import ch.zhaw.wikitransport.xml.XmlReadable;
import ch.zhaw.wikitransport.xml.XmlReaderFactory;

@Ignore
public class TestXmlReadersAndFactory extends TestCase {
	private XmlReadable<Page> myParser;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		myParser = null;
	}
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
		myParser = null;
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFactory(){
		createDOMParser();
		//createSomeTestParser();
		createSAXParser();
	}

	private void createSAXParser() {
		myParser=null;
		myParser = XmlReaderFactory.getXmlParser(ParserTypeEnum.SAX_PARSER);
		assertNotNull("Sax-Parser instance could not be created!", myParser);
	}
	
	/*@Test(expected=IllegalArgumentException.class)
	private void createSomeTestParser(){
		//should throw an exception normally! --> SOME_TEST_PARSER does not exist as parser.
		myParser=null;
		myParser = XmlReaderFactory.getXmlParser(ParserTypeEnum.SOME_TEST_PARSER);
	}*/

	private void createDOMParser() {
		myParser = XmlReaderFactory.getXmlParser(ParserTypeEnum.DOM_PARSER);
		assertNotNull("DOM-Parser instance could not be created!", myParser);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testRetrievingDataFromXml(){
		
		//test it with sax parser
		createSAXParser();
		getDataThroughParser();
		
		//test it with dom parser
		createDOMParser();
		getDataThroughParser();
	}

	private void getDataThroughParser() {
		String indexPageIdentifier = null;
		indexPageIdentifier = myParser.getIndexPageIdentifier();
		
		//Assert that an index page identifier exists.
		assertNotNull("Index page identifier could not be read!", indexPageIdentifier);
		
		assertTrue("No elements found while parsing!", myParser.getParsedElements().size() > 0);
		
		for(Page p : myParser.getParsedElements()){
			assertNotNull("Content could not be read!", p.getContentValue());
			assertNotNull("Hash could not be read!", p.getHashValue());
			assertNotNull("Time could not be read!", p.getTimeValue());
			assertNotNull("Title could not be read!", p.getTitleValue());
		}
	}
}