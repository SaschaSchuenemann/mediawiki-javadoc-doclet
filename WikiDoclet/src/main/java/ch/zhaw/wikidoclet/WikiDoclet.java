package ch.zhaw.wikidoclet;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.zhaw.wikidoclet.util.WikiDocletCfg;

import com.sun.javadoc.Doclet;
import com.sun.javadoc.RootDoc;

/**
 * The WikiDoclet is used by Javadoc as a custom Doclet. It creates Wikimedia
 * Pages from Javadoc output and places them in a XML File specified by
 * parameter "-d" and "-f" while calling Javadoc. The WikiDoclet Class has a
 * dependency to the org.slf4j Package.
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 * 
 * @version 1.0
 * 
 */
public class WikiDoclet extends Doclet {

	/**
	 * Logger Variable
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(WikiDoclet.class);

	/**
	 * Standard doclet entry point. This method is called by Javadoc
	 * 
	 * @param root
	 *            A RootDoc object usually created by Javadoc utility
	 * @return True or False if the Doclet was successfull.
	 */
	public static boolean start(RootDoc root) {
		
		// logger = LoggerFactory.getLogger(WikiDoclet.class);
		LOGGER.info("WikiDoclet Start");

		// Load Doclet Properties
		WikiDocletCfg cfg = WikiDocletCfg.getInstance();
		cfg.loadDocletConfiguration(root.options());

		LOGGER.info("Analyse Root Doc");
		// Analyse the Rootdoc Object and create the PageSet
		AnalyseRootDoc analyser = new AnalyseRootDoc(root);

		LOGGER.info("Write XML File");
		// Write the XML File
		PageWriter xmlwriter = new PageWriter();
		xmlwriter.writePagesToXml(analyser.getPageList(), cfg.getConfigValue(WikiDocletCfg.CFG_PAR_XML));

		LOGGER.info("WikiDoclet Finished");
		return true;
	}

	/**
	 * Returns the valid amound of parameters to a option in the options string.
	 * This method is called by Javadoc to validate custom parameters.
	 * 
	 * @param option
	 *            Option key string eg: "-d"
	 * @return Amount of parameters as int
	 */
	public static int optionLength(String option) {
		LOGGER.trace("optionLength: Started with parameter {} ", option);
		// Check all Parameters
		for (Map.Entry<String, Integer> s : WikiDocletCfg.VALID_PARAMS.entrySet()) {
			if (option.equals(s.getKey())) {
				LOGGER.debug("optionLength: Option Found: Key: '{}' ValidLength: {}", option, s.getValue());
				return s.getValue();
			}
		}
		LOGGER.warn("optionLength: Unknown Parameter: '{}", option);
		return 0;
	}

}
