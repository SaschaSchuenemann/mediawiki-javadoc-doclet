package ch.zhaw.wikitransport.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stores and handles all command line parameters and the config file values. 
 * It validates all parameters from all sources, determines, which source to use 
 * and if no valid config source is available, it contains the default configuration 
 * settings for the application.
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 *
 */
public class WikiTransporterCfg {

	// Default Config File
	private static final String CFGFILE = "wikitransporter.properties";

	// --------- Static vars for XML file content -------------
	public static final String WIKI_DOCLET_XML_ROOT = "wikiDocletPages";
	public static final String WIKI_DOCLET_XML_PAGE = "page";
	public static final String WIKI_DOCLET_XML_TITLE = "title";
	public static final String WIKI_DOCLET_XML_CONTENT = "content";
	public static final String WIKI_DOCLET_XML_HASH = "hash";
	public static final String WIKI_DOCLET_XML_TIME = "time";
	public static final String WIKI_DOCLET_INDEX_PAGE_NAME_ATTRIBUTE = "indexpagename";
	
	// ----------- Static vars for ThreadCoordinator class ------------
	public static final int NUMBER_OF_THREADS = 1;
	
	// ----------- Static vars for Pagewriter class ------------
	public static final int MAX_NUMBER_OF_RETRIES = 3;

	// ---------------- WIKITransporter PARAMETER NAMES ------------

	public static final String CFG_PAR_XML = "XML";
	public static final String CFG_PAR_XML_DESC = "Path to your Wiki Doclet XML File";
	private static final String CFG_PAR_XML_DEF = "wikidoclet.xml";
	private static final String CFG_PAR_XML_MATCH = "^(.*?/|.*?\\\\)?([^\\./|^\\.\\\\]+)(?:\\.([^\\\\]*)|)+(\\.[^\\/\\\\:*?\"<>|]+)$";

	public static final String CFG_PAR_URL = "WIKIURL";
	public static final String CFG_PAR_URL_DESC = "URL to your Mediawiki api.php";
	public static final String CFG_PAR_URL_DEF = "";
	public static final String CFG_PAR_URL_MATCH = "^(http|https)\\://[a-zA-Z0-9\\-\\.]+(\\.[a-zA-Z]{2,3})?(:[a-zA-Z0-9]*)?/?([a-zA-Z0-9\\-\\._\\?\\,\\'/\\\\\\+&amp;%\\$#\\=~])*[^\\.\\,\\)\\(\\s]api.php$";

	public static final String CFG_PAR_UNAME = "USERNAME";
	public static final String CFG_PAR_UNAME_DESC = "Wiki Bot Username";
	private static final String CFG_PAR_UNAME_DEF = "";
	private static final String CFG_PAR_UNAME_MATCH = "^.*$";

	public static final String CFG_PAR_PASSWD = "PASSWORD";
	public static final String CFG_PAR_PASSWD_DESC = "Wiki Bot Password";
	private static final String CFG_PAR_PASSWD_DEF = "";
	private static final String CFG_PAR_PASSWD_MATCH = "^.*$";
	// ----------------------------------------------------
	public static final String[][] OPTIONSET = { 
		{ CFG_PAR_XML, CFG_PAR_XML_DESC, CFG_PAR_XML_DEF, CFG_PAR_XML_MATCH },
		{ CFG_PAR_URL, CFG_PAR_URL_DESC, CFG_PAR_URL_DEF, CFG_PAR_URL_MATCH }, 
		{ CFG_PAR_UNAME, CFG_PAR_UNAME_DESC, CFG_PAR_UNAME_DEF, CFG_PAR_UNAME_MATCH },
		{ CFG_PAR_PASSWD, CFG_PAR_PASSWD_DESC, CFG_PAR_PASSWD_DEF, CFG_PAR_PASSWD_MATCH } 
	};

	// ----------------------------------------------------

	// LOCAL STATIC VARS
	private static Logger logger = LoggerFactory.getLogger(WikiTransporterCfg.class);

	/**
	 * Current Instance from Singleton
	 */
	private static WikiTransporterCfg instance;

	// INSTANCE VARIABLES
	private Properties properties;

	/**
	 * Singleton: Create Properties with Default Settings
	 */
	protected WikiTransporterCfg() {
		properties = new Properties();
	}

	public static void reset() {
		instance = null;
	}
	
	/**
	 * Singleton Get Instance
	 * 
	 * @return Instance of the configurator
	 */
	public static WikiTransporterCfg getInstance() {
		if (instance == null) {
			instance = new WikiTransporterCfg();
		}
		return instance;
	}

	/**
	 * Get a specified parameter value
	 * 
	 * @param param
	 *            Parameter Name as String (Use the Static Variables)
	 * @return Parameter Value or null if property not found.
	 */
	public String getConfigValue(String param) {
		return properties.getProperty(param);
	}

	/**
	 * Get all Properties stored in the Configurator
	 * 
	 * @return Properties Object
	 */
	public Properties getProperties() {
		return properties;
	}

	private void parseArgs(Options options, String[] args) {
		CommandLineParser parser = new PosixParser();
		CommandLine cmd;
		try {
			cmd = parser.parse(options, args);

			// Put all Arguments in the Properties Objects
			for (String[] ss : OPTIONSET) {
				// If Param does not exist, use default Value in ss[2]
				String entry = cmd.getOptionValue(ss[0].toLowerCase(), ss[2]);
				if (entry.matches(ss[3])) {
					logger.trace("Argument: " + ss[0] + " Value: " + entry);
					properties.put(ss[0], entry);
				} else if(entry.equals("")) {
					logger.error("Missing Argument: " + ss[0]);
					throw new MissingArgumentException("Missing argument " + ss[0] + ", this argument is required");
				} else {
					logger.error("Wrong Argument Value: " + ss[0]);
					throw new IllegalArgumentException("Parameter " + ss[0] + " has an invalid value");
				}
			}
			if (cmd.hasOption("?")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("DataTransporter.class ", options);
			}
		} catch (ParseException e) {
			logger.error("Error parsing arguments: {}", e.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("DataTransporter.class ", options);
		}

	}

	public void loadDataTransporterConfig(String[] args) {

		// first Load Config File (Priority 2 Settings)
		loadCfgFile();

		// Set Options for DataTransporter
		Options options = new Options();

		options.addOption("?", "help", false, "Show available parameters.");

		OptionBuilder.withArgName(CFG_PAR_XML.toLowerCase());
		OptionBuilder.hasArg();
		OptionBuilder.withDescription(CFG_PAR_XML_DESC);
		OptionBuilder.withLongOpt(CFG_PAR_XML.toLowerCase());
		options.addOption(OptionBuilder.create(CFG_PAR_XML.toLowerCase().substring(0, 1)));

		OptionBuilder.withArgName(CFG_PAR_URL.toLowerCase());
		OptionBuilder.hasArg();
		OptionBuilder.withDescription(CFG_PAR_URL_DESC);
		OptionBuilder.withLongOpt(CFG_PAR_URL.toLowerCase());
		options.addOption(OptionBuilder.create(CFG_PAR_URL.toLowerCase().substring(0, 1)));

		OptionBuilder.withArgName(CFG_PAR_UNAME.toLowerCase());
		OptionBuilder.hasArg();
		OptionBuilder.withDescription(CFG_PAR_UNAME_DESC);
		OptionBuilder.withLongOpt(CFG_PAR_UNAME.toLowerCase());
		options.addOption(OptionBuilder.create(CFG_PAR_UNAME.toLowerCase().substring(0, 1)));

		OptionBuilder.withArgName(CFG_PAR_PASSWD.toLowerCase());
		OptionBuilder.hasArg();
		OptionBuilder.withDescription(CFG_PAR_PASSWD_DESC);
		OptionBuilder.withLongOpt(CFG_PAR_PASSWD.toLowerCase());
		options.addOption(OptionBuilder.create(CFG_PAR_PASSWD.toLowerCase().substring(0, 1)));

		// Parse Args
		parseArgs(options, args);
		// Write CFg File
		//writeCfgFile();
	}

	private void loadCfgFile() {
		// first Load Config File (Priority 2 Settings)
		try {
			this.properties.load(new FileInputStream(WikiTransporterCfg.CFGFILE));
		} catch (FileNotFoundException e1) {
			// No error since this file may not exist
			logger.trace("Uncritical FileNotFoundException: {}", e1.getMessage());
		} catch (IOException e1) {
			// No Error since an IOException may happen if the application is
			// used in a readonly environment
			logger.trace("Uncritical IOException: {}", e1.getMessage());
		}
	}

	@SuppressWarnings("unused")
	private void writeCfgFile() {
		// Store the File if Trace is enabled
		if (logger.isTraceEnabled()) {
			try {
				logger.trace(properties.toString());
				properties.store(new FileWriter(new File(WikiTransporterCfg.CFGFILE)), "Wiki Doclet");
			} catch (IOException e) {
				logger.trace("Error Writing the Config File", e);
			}
		}
	}
}
