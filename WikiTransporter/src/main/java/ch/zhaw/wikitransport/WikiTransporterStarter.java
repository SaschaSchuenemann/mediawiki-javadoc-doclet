package ch.zhaw.wikitransport;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.zhaw.wikitransport.page.Page;
import ch.zhaw.wikitransport.transporter.Transporter;
import ch.zhaw.wikitransport.transporter.jwbf.TransporterJwbf;
import ch.zhaw.wikitransport.util.WikiTransporterCfg;
import ch.zhaw.wikitransport.xml.ParserTypeEnum;
import ch.zhaw.wikitransport.xml.XmlReadable;
import ch.zhaw.wikitransport.xml.XmlReaderFactory;

/**
 * This class is the starting point of the whole WikiTransporter functionality.
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 * 
 */
public class WikiTransporterStarter {

	private static final Logger LOGGER = LoggerFactory.getLogger(WikiTransporterStarter.class);

	private List<Page> pages;
	private String indexPageName;
	private Transporter jwbfTransporter;
	private XmlReadable<Page> myXmlParser;
	
	/**
	 * Main method that starts the whole WikiTransporter application.
	 * 
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		
		//Parse command line parameters
		parseCommandLineParameters(args);
		printDebugInfos(args);
		
		//Create new transporter instance and run the transporter.
		WikiTransporterStarter wikiTransporter = new WikiTransporterStarter();
		wikiTransporter.run();
	
	}
	
	/**
	 * Default constructor sets the needed instance variables.
	 * 
	 */
	public WikiTransporterStarter(){
		
		//Get new parser
		myXmlParser = XmlReaderFactory.getXmlParser(ParserTypeEnum.SAX_PARSER);
		
		//Get the transporter
		jwbfTransporter = new TransporterJwbf();
	}
	
	/**
	 * Run the Transporter
	 */
	private void run() {
		//Load the Pages
		loadXmlData();
		
		//Transport them
		startTransporting();
	}
	
	/**
	 * Load XML data
	 * 
	 */
	private void loadXmlData() {
		
		// Get the pages and put them in a list.
		pages =  myXmlParser.getParsedElements();

		// Get the IndexPageIdentifier
		indexPageName = myXmlParser.getIndexPageIdentifier();
	}

	/**
	 * Does run the Transporter and uploads all pages.
	 */
	protected void startTransporting() {

		//connect to the Wiki
		jwbfTransporter.connect();
		
		//Build the index by reading the index page
		jwbfTransporter.buildIndex(indexPageName);
		
		//Transport the new pages
		jwbfTransporter.transport(pages);
		
		//Clean the old pages. That are the pages that are not used anymore. (delete them)
		jwbfTransporter.clean();
	}
	
	/**
	 * Loads the command line parameters into the WikiDocletCfg config handler
	 * 
	 * @param args arguments to parse
	 */
	private static void parseCommandLineParameters(String[] args) {
		WikiTransporterCfg.getInstance().loadDataTransporterConfig(args);
	}

	/**
	 * Print some debug infos if trace or debug is enabled
	 * 
	 * @param args arguments to print as debug information.
	 */
	private static void printDebugInfos(String[] args) {
		if (LOGGER.isTraceEnabled()) {
			WikiTransporterCfg cfg = WikiTransporterCfg.getInstance();

			StringBuilder tmpArgHolder = new StringBuilder(10);
			for (String s : args) {
				tmpArgHolder.append(" \"").append(s).append("\"");
			}
			LOGGER.trace("parameters " + tmpArgHolder);

			cfg.loadDataTransporterConfig(args);

			LOGGER.trace("Arguments:");
			LOGGER.trace(WikiTransporterCfg.getInstance().getProperties().toString());
		}
	}
}