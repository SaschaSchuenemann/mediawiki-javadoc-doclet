package ch.zhaw.wikidoclet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import ch.zhaw.wikidoclet.page.IndexPage;
import ch.zhaw.wikidoclet.page.Page;
import ch.zhaw.wikidoclet.util.WikiDocletCfg;


/**
 * The PageWriter class is used to write the Pages to an XML File. The Class
 * uses the Properties loaded in the WikiDocletCfg to find the location to write
 * the File to.
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 * @version 1.0
 * 
 */
class PageWriter {

	private static final Logger LOGGER = LoggerFactory.getLogger(WikiDoclet.class);

	/**
	 * Write a list of pages to the xml file
	 * 
	 * @param pageList
	 *            A List<Page> object
	 * @param XmlFilePath
	 *            Full path to an xml file
	 */
	public void writePagesToXml(List<Page> pageList, String XmlFilePath) {
		File f = new File(XmlFilePath);
		// Create folders if required
		if (f.getParent() != null) {
			new File(f.getParent()).mkdirs();
		}
		
		// Create all IndexPage entries
		AttributesImpl a = new AttributesImpl();
		for (Page s : pageList) {
			if (s instanceof IndexPage) {
				((IndexPage) s).index(pageList);
				
				a.addAttribute("", WikiDocletCfg.WIKI_DOCLET_INDEX_PAGE_NAME_ATTRIBUTE, "", "", s.getPageTitle());
			}
		}
		
		try {
			// Create file writer
			
			FileWriter f0 = new FileWriter(f);
			// Create new XML
			XMLWriterHandler xmlwriter = new XMLWriterHandler(f0);
			// Start xml document
			xmlwriter.startDocument();
			
			// Root node
			xmlwriter.startElement("", WikiDocletCfg.WIKI_DOCLET_XML_ROOT, "", a);

			// Create page entry for each page
			for (Page s : pageList) {
				LOGGER.debug("Writing page " + s.getPageTitle());
				xmlwriter.startElement(WikiDocletCfg.WIKI_DOCLET_XML_PAGE);
				xmlwriter.dataElement(WikiDocletCfg.WIKI_DOCLET_XML_TITLE, s.getPageTitle());
				xmlwriter.dataElement(WikiDocletCfg.WIKI_DOCLET_XML_CONTENT, stripNonValidXMLCharacters(s.toString()));
				xmlwriter.dataElement(WikiDocletCfg.WIKI_DOCLET_XML_HASH, s.getHash());
				xmlwriter.dataElement(WikiDocletCfg.WIKI_DOCLET_XML_TIME, Long.toString(new Date().getTime()));
			
				xmlwriter.endElement(WikiDocletCfg.WIKI_DOCLET_XML_PAGE);
			}

			// Close root node
			xmlwriter.endElement(WikiDocletCfg.WIKI_DOCLET_XML_ROOT);
			// Close xml document
			xmlwriter.endDocument();
		} catch (IOException e) {
			LOGGER.error("XML file could not be written.", e);
		} catch (SAXException e) {
			LOGGER.error("XML file could not be written.", e);
		}
	}
	
	/**
     * This method ensures that the output String has only
     * valid XML unicode characters as specified by the
     * XML 1.0 standard. For reference, please see
     * <a href="http://www.w3.org/TR/2000/REC-xml-20001006#NT-Char">the
     * standard</a>. This method will return an empty
     * String if the input is null or empty.
     *
     * @param in The String whose non-valid characters we want to remove.
     * @return The in String, stripped of non-valid characters.
     */
    public String stripNonValidXMLCharacters(String in) {
    	// Used to hold the output.
    	StringBuffer out = new StringBuffer(); 
        // Used to reference the current character.
        char current; 

        if (in == null || ("".equals(in))){
        	// vacancy test.
        	return ""; 
        }
        for (int i = 0; i < in.length(); i++) {
        	// NOTE: No IndexOutOfBoundsException caught here; it should not happen.
        	current = in.charAt(i); 
            if ((current == 0x9) ||
                (current == 0xA) ||
                (current == 0xD) ||
                ((current >= 0x20) && (current <= 0xD7FF)) ||
                ((current >= 0xE000) && (current <= 0xFFFD)) ||
                ((current >= 0x10000) && (current <= 0x10FFFF))){
                
            	out.append(current);
            }
            //else
            	//System.out.println("Found invalid char: " + current);
        }
        return out.toString();
    }    
}