package ch.zhaw.wikitransport.transporter.jwbf;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to read in the additional information (MetaData)
 * for every existing page (WikiDocPage) on the MediaWiki. The additional information
 * are pageTitle and hash of every's page content.
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 *
 */
public class IndexPageParser{
	
	public static final Logger LOGGER = LoggerFactory.getLogger(IndexPageParser.class.getName());
	private static final Pattern PATTERN_NEW_LINE = Pattern.compile("\\n");
	private static final Pattern  PATTERN_COMMA = Pattern.compile(",");
	
	/**
	 * just for testing reasons.
	 * 
	 * */
	public static void main(String[] args){
		Map<String, IndexEntry> myList = IndexPageParser.parseWikiDocMetaData("hallo,du\nwie,geht");
		Iterator<Map.Entry<String,IndexEntry>> it = myList.entrySet().iterator();
		IndexEntry tmp;
		while(it.hasNext()){
			tmp = it.next().getValue();
			LOGGER.debug("PAGE_TITLE: " + tmp.getPageTitle() + " HASH : " + tmp.getHash() + "\n");
		}
		
	}
	
	/**
	 * This method is used to generate a hash map that is generated out of a MediaWiki page's 
	 * content that is styled as cvs.
	 * 
	 * @param pagesMetaData - The data in cvs style received from the MediaWiki.
	 * @return pageTitleHashRel - A HashMap with pageTitle as key and complete WikiDocMetaData obj as value.
	 */
	public static Map<String, IndexEntry> parseWikiDocMetaData(String pagesMetaData){
		LOGGER.trace("MetaData to parse: " + pagesMetaData);
		if(pagesMetaData.trim().length() > 0){
			String[] tmpPageMetaData = PATTERN_NEW_LINE.split(pagesMetaData);
			LOGGER.trace("line to parse: " + tmpPageMetaData[0]);
			String[] tmpPageFields;
			HashMap<String, IndexEntry> pageTitleHashRel = new HashMap<String, IndexEntry>();
			
			for(int i = 0; i < tmpPageMetaData.length; i++){
				tmpPageFields = PATTERN_COMMA.split(tmpPageMetaData[i]);
				
				if(tmpPageFields.length==2) {
					pageTitleHashRel.put(tmpPageFields[0], new IndexEntry(tmpPageFields[0], tmpPageFields[1]));
				} else {
					LOGGER.warn("Wrong index page format, skipping index page.");
					return null;
				}
			}
			return pageTitleHashRel;
		}
		return null;
	}

}
