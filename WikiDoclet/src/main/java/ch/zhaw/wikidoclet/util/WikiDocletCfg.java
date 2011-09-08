package ch.zhaw.wikidoclet.util;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;
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

import ch.zhaw.wikidoclet.WikiDoclet;

/**
 * Stores and handles all command line parameters and the config file values. 
 * It validates all parameters from all sources, determines, which source to use 
 * and if no valid config source is available, it contains the default configuration 
 * settings for the application.
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 *
 */
public class WikiDocletCfg {

	// Default Config File
	private static final String CFGFILE = "wikidoclet.properties";

	// --------- Static vars for XML file content -------------
	public static final String WIKI_DOCLET_XML_ROOT = "wikiDocletPages";
	public static final String WIKI_DOCLET_XML_PAGE = "page";
	public static final String WIKI_DOCLET_XML_TITLE = "title";
	public static final String WIKI_DOCLET_XML_CONTENT = "content";
	public static final String WIKI_DOCLET_XML_HASH = "hash";
	public static final String WIKI_DOCLET_XML_TIME = "time";
	public static final String WIKI_DOCLET_INDEX_PAGE_NAME_ATTRIBUTE = "indexpagename";
	

	// ---------------- WIKIDOCLET PARAMETER NAMES ------------
	// -x -p -n -i -w -u -p
	/**
	 * Properties Parameter: XML Path for Output
	 */	  
	public static final String CFG_PAR_XML = "XML";
	public static final String CFG_PAR_XML_DESC = "Path to your Wiki Doclet XML File";
	private static final String CFG_PAR_XML_DEF = "wikidoclet.xml";
	private static final String CFG_PAR_XML_MATCH = "^(.*?/|.*?\\\\)?([^\\./|^\\.\\\\]+)(?:\\.([^\\\\]*)|)+(\\.[^\\/\\\\:*?\"<>|]+)$";
	/**
	 * Properties Parameter: Name for the Project (Optional)
	 */
	public static final String CFG_PAR_PROJ = "PROJECT";
	public static final String CFG_PAR_PROJ_DESC = "Custom Project Description Token (Will be Printed as Prefix of the Wiki Page Title)";
	private static final String CFG_PAR_PROJ_DEF = "";
	private static final String CFG_PAR_PROJ_MATCH = "^\\w*$";
	/**
	 * Properties Parameter Name for the Wiki Namespace
	 */
	public static final String CFG_PAR_NSPACE = "NAMESPACE";
	public static final String CFG_PAR_NSPACE_DESC = "Namespace of the Pages in the Wiki";
	private static final String CFG_PAR_NSPACE_DEF = "Javadoc:";
	private static final String CFG_PAR_NSPACE_MATCH = "^\\w*:$";
	/**
	 * Properties Parameter Name for the Project Name
	 */
	public static final String CFG_PAR_IPAGE = "INDEXPAGE";
	public static final String CFG_PAR_IPAGE_DESC = "Name of the Index Page in the Wiki";
	private static final String CFG_PAR_IPAGE_DEF = "Index";
	private static final String CFG_PAR_IPAGE_MATCH ="^\\w*$";

	
	// ----------------------------------------------------
	public static final String[][] OPTIONSET = { 
		{ CFG_PAR_XML, CFG_PAR_XML_DESC, CFG_PAR_XML_DEF, CFG_PAR_XML_MATCH }, 
		{ CFG_PAR_PROJ, CFG_PAR_PROJ_DESC, CFG_PAR_PROJ_DEF, CFG_PAR_PROJ_MATCH },
		{ CFG_PAR_NSPACE, CFG_PAR_NSPACE_DESC, CFG_PAR_NSPACE_DEF, CFG_PAR_NSPACE_MATCH }, 
		{ CFG_PAR_IPAGE, CFG_PAR_IPAGE_DESC, CFG_PAR_IPAGE_DEF, CFG_PAR_IPAGE_MATCH },
	};

	public static final Hashtable<String, Integer> VALID_PARAMS = new Hashtable<String, Integer>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8889666916920651018L;

		{
			put("--" + CFG_PAR_XML.toLowerCase(), 2);
			put("--" + CFG_PAR_PROJ.toLowerCase(), 2);
			put("--" + CFG_PAR_NSPACE.toLowerCase(), 2);
			put("--" + CFG_PAR_IPAGE.toLowerCase(), 2);
			put("-" + CFG_PAR_XML.substring(0, 1).toLowerCase(), 2);
			put("-" + CFG_PAR_PROJ.substring(0, 1).toLowerCase(), 2);
			put("-" + CFG_PAR_NSPACE.substring(0, 1).toLowerCase(), 2);
			put("-" + CFG_PAR_IPAGE.substring(0, 1).toLowerCase(), 2);
		}
	};
	// ----------------------------------------------------

	// LOCAL STATIC VARS
	private static Logger logger = LoggerFactory.getLogger(WikiDoclet.class);

	/**
	 * Current Instance from Singleton
	 */
	private static WikiDocletCfg instance;

	// INSTANCE VARIABLES
	private Properties properties;

	/**
	 * Singleton: Create Properties with Default Settings
	 */
	protected WikiDocletCfg() {
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
	public static WikiDocletCfg getInstance() {
		if (instance == null) {
			instance = new WikiDocletCfg();
		}
		return instance;
	}

	/**
	 * Get a specified parameter value
	 * 
	 * @param param
	 *            Parameter Name as String (Use the Static Variables)
	 * @return Parameter Value
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

	/**
	 * <p>
	 * Loads <b>all</b> <i>the</i> <code>Config Parameters</code> according <font bla="test">to</font> their
	 * importance.
	 * </p>
	 * 
	 * <p>
	 * Nothing <b>is</b> <i>more</i> <b><i>important</i></b> than </p>
	 * <p>text
	 * <ul>
	 * <li>Prio 1: Commandline Parameters Prio</li>
	 * <li>2: Config File (Relativ)</li>
	 * <li>((((Prio 3: Config File (in Package) ))))</li>
	 * <li>Prio 4: Default Values</li>
	 * </ul></p>
	 * 
	 * @param javaDocArgs
	 *            String Multi Array from Javadoc
	 */
	public void loadDocletConfiguration(String[][] javaDocArgs) {

		// Create Args from javaDocArgs
		loadCfgFile();

		// Calculate Args Size
		int argssize = 0;

		for (String[] ss : javaDocArgs) {
			if (VALID_PARAMS.containsKey(ss[0])) {
				argssize += ss.length;
			}
		}

		String[] args = new String[argssize];
		int i = 0;
		if (argssize > i) {
			for (String[] ss : javaDocArgs) {
				if (i ==  argssize)
					//throw new IllegalArgumentException("To many arguments at " + ss[0] + " got " + argssize + " but expected " + i + " arguments");
					logger.debug("Ignoring Argument: " +  ss[0]);
				if (VALID_PARAMS.containsKey(ss[0])) {
					for (String s : ss) {

						args[i++] = s;
					}
				}
			}
		}
		logger.trace("JavaDocArgumentsToParse: " + Arrays.toString(args));

		// Set Options for Wiki Doclet
		Options options = new Options();

		options.addOption("?", "help", false, "Show available parameters.");

		OptionBuilder.withArgName(CFG_PAR_XML.toLowerCase());
		OptionBuilder.hasArg();
		OptionBuilder.withDescription(CFG_PAR_XML_DESC);
		OptionBuilder.withLongOpt(CFG_PAR_XML.toLowerCase());
		options.addOption(OptionBuilder.create(CFG_PAR_XML.toLowerCase().substring(0, 1)));

		OptionBuilder.withArgName(CFG_PAR_NSPACE.toLowerCase());
		OptionBuilder.hasArg();
		OptionBuilder.withDescription(CFG_PAR_NSPACE_DESC);
		OptionBuilder.withLongOpt(CFG_PAR_NSPACE.toLowerCase());
		options.addOption(OptionBuilder.create(CFG_PAR_NSPACE.toLowerCase().substring(0, 1)));

		OptionBuilder.withArgName(CFG_PAR_PROJ.toLowerCase());
		OptionBuilder.hasArg();
		OptionBuilder.withDescription(CFG_PAR_PROJ_DESC);
		OptionBuilder.withLongOpt(CFG_PAR_PROJ.toLowerCase());
		options.addOption(OptionBuilder.create(CFG_PAR_PROJ.toLowerCase().substring(0, 1)));

		OptionBuilder.withArgName(CFG_PAR_IPAGE.toLowerCase());
		OptionBuilder.hasArg();
		OptionBuilder.withDescription(CFG_PAR_IPAGE_DESC);
		OptionBuilder.withLongOpt(CFG_PAR_IPAGE.toLowerCase());
		options.addOption(OptionBuilder.create(CFG_PAR_IPAGE.toLowerCase().substring(0, 1)));

		// Parse Args
		parseArgs(options, args);


		// Write CFG File
		//writeCfgFile();
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
					logger.trace("Argument: " + ss[0] + " value: " + entry);
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

	private void loadCfgFile() {
		// first Load Config File (Priority 2 Settings)
		try {
			this.properties.load(new FileInputStream(WikiDocletCfg.CFGFILE));
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
				properties.store(new FileWriter(new File(WikiDocletCfg.CFGFILE)), "Wiki Doclet");
			} catch (IOException e) {
				logger.trace("Error Writing the Config File", e);
			}
		}
	}
}
