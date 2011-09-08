package ch.zhaw.wikitransport;

import java.util.Map;
import java.util.Map.Entry;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.zhaw.wikitransport.transporter.jwbf.IndexEntry;
import ch.zhaw.wikitransport.transporter.jwbf.IndexPageParser;

public class TestMetaDataParser extends TestCase { 
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testWikiDocPageMetaDataParser(){
		String testMetaDataCorrectFormat = "Javadoc:ch.zhaw.wikidoclet.doclet,1b81fffa4d49caf4f768a22d3e892b3\nJavadoc:ch.zhaw.wikidoclet.doclet,1b81fffa4d49caf4f768a22d3e892b3";
		Map<String, IndexEntry> myMetaDataList = IndexPageParser.parseWikiDocMetaData(testMetaDataCorrectFormat);
		
		assertNotNull("Wrong index page format!", myMetaDataList);
		
		for(Entry<String, IndexEntry> metaData : myMetaDataList.entrySet()){
			assertNotNull("Key not found!", metaData.getKey());
			assertNotNull("Hash value not found on the current record!", metaData.getValue().getHash());
			assertNotNull("Title value not found on the current record!", metaData.getValue().getPageTitle());
		}
	}
	
	@Test
	public void testWikiDocPageMetaDataParserWithWrongInput(){
		String testMetaDataIncorrect = "Javadoc:ch.zhaw.wikidoclet.doclet,1b81fffa4d49caf4f768a22d3e892b3 Javadoc:ch.zhaw.wikidoclet.doclet,1b81fffa4d49caf4f768a22d3e892b3";
		Map<String, IndexEntry> myMetadataList = IndexPageParser.parseWikiDocMetaData(testMetaDataIncorrect);
		assertNull("Meta data list should be null, because the meta data string is in invalid formated.", myMetadataList);
	}
}
