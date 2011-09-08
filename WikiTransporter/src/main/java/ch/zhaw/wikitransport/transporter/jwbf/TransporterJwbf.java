package ch.zhaw.wikitransport.transporter.jwbf;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.jwbf.core.actions.util.ActionException;
import net.sourceforge.jwbf.core.actions.util.ProcessException;
import net.sourceforge.jwbf.core.contentRep.Article;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.zhaw.wikitransport.page.Page;
import ch.zhaw.wikitransport.transporter.TransportError;
import ch.zhaw.wikitransport.transporter.Transporter;
import ch.zhaw.wikitransport.transporter.jwbf.bot.MediaWikiDocletBot;
import ch.zhaw.wikitransport.transporter.jwbf.threading.PageWriter;
import ch.zhaw.wikitransport.util.SSLUtilities;
import ch.zhaw.wikitransport.util.WikiTransporterCfg;

/**
 * That is the class that takes care of the data transport from the xml file to
 * the chosen MediaWiki.
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 * 
 */
public class TransporterJwbf implements Transporter {

	// Logger
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TransporterJwbf.class.getName());

	// Bot
	private MediaWikiDocletBot bot;

	// Index list
	private Map<String, IndexEntry> index;

	/**
	 * Default Constructor
	 */
	public TransporterJwbf() {
		this.index = new HashMap<String, IndexEntry>();
	}

	/**
	 * Connects to the Wiki using the Bot
	 */
	@Override
	public void connect() {
		
		WikiTransporterCfg CFG = WikiTransporterCfg.getInstance();

		try {
			
			//Set all trusting trust manager.
			//Can be changed easily in SSLUtilities class.
			SSLUtilities.setTrustAllCerts();

			LOGGER.debug("connecting wiki");
			bot = new MediaWikiDocletBot(
					CFG.getConfigValue(WikiTransporterCfg.CFG_PAR_URL));
			LOGGER.debug("logging in to wiki");
			bot.login(CFG.getConfigValue(WikiTransporterCfg.CFG_PAR_UNAME),
					CFG.getConfigValue(WikiTransporterCfg.CFG_PAR_PASSWD));

			LOGGER.debug("logged in");

			//Check the kill switch page
			try {
				String killSwitchPageTitle = "User:"
						+ CFG.getConfigValue(WikiTransporterCfg.CFG_PAR_UNAME)
						+ "/wikidocletbot";
				Article killSwitch = bot.readContent(killSwitchPageTitle);
				LOGGER.debug("Killswitch Text: {} {}", killSwitch.getTitle(),
						killSwitch.getText());
				if (killSwitch.getText().toUpperCase().contains("'''ON'''")) {
					// Bot OK
					LOGGER.info("WikiTransporter Functionality is turned ON for this Wiki User");
				} else {
					// Wiki is not configured to use the WikiTransporter
					LOGGER.error("This Wiki is not configured to use this Transporter. Please read the README File on how to configure your Wiki to support the WikiTransporter");
					throw new TransportError(
							"This Wiki is not configured to use this Transporter. Please read the README File on how to configure your Wiki to support the WikiTransporter");
				}
			} catch (ProcessException e) {
				LOGGER.error("Could not check the KillFlag");
				throw new TransportError("Could not check the KillFlag");
			}
		} catch (MalformedURLException e) {
			LOGGER.error(e.getMessage());
			throw new TransportError(e);
		} catch (ActionException e) {
			LOGGER.error(e.getMessage());
			throw new TransportError(e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}

	/**
	 * Builds the index list according to the indexPageIdentifyer.
	 * 
	 * @param indexPageIdentifer - The index page identifier to search
	 * 							   for on the Wiki.
	 */
	@Override
	public void buildIndex(String indexPageIdentifier) {
		try {
			if (!bot.isLoggedInDoclet()) {
				connect();
			}
			index = IndexPageParser.parseWikiDocMetaData(bot.readContent(
					indexPageIdentifier).getText());
		} catch (ActionException e) {
			LOGGER.error(e.getMessage());
			throw new TransportError(e);
		} catch (ProcessException e) {
			LOGGER.error(e.getMessage());
			throw new TransportError(e);
		}
	}

	/**
	 * Transports a set of pages - stored in a list - to the MediaWiki. Pages are only transported if they changed
	 * since last time. To keep track of changes the index list is used.
	 * 
	 * @param pages - A list of pages to be uploaded.
	 * 
	 */
	@Override
	public void transport(List<Page> pages) {
		// Build pageset to update
		Iterator<Page> pageIterator = pages.iterator();
		while (pageIterator.hasNext()) {
			Page page = pageIterator.next();
			// Check if the Page needs to be updated
			if (index != null
					&& index.containsKey(page.getTitleValue())
					&& index.get(page.getTitleValue()).getHash()
							.equals(page.getHashValue())) {
				LOGGER.debug("SAME {} {}", page.getTitleValue(),
						page.getHashValue());
				pageIterator.remove();
			} else {
				LOGGER.debug("NEW  {} {}", page.getTitleValue(),
						page.getHashValue());

			}

			// Remove it from the index
			if (index != null) {
				index.remove(page.getTitleValue());
			}

		}

		LOGGER.debug("Pages to upload: {}", pages.size());

		PageWriter pw = new PageWriter(bot);
		for(Page page : pages) {
			pw.setPage(page);
			pw.writeArticleIfPageExists();
		}
		
		// Use multiple Threads to upload.
		/*ThreadCoordinator threadcoordinator = new ThreadCoordinator(pages, bot);
		threadcoordinator.startCoordinating();*/

	}

	/**
	 * Deletes all old pages not required anymore.
	 */
	@Override
	public void clean() {

		// Delete all remaining Pages from the Index list
		Article articleToDel;
		if (index != null) {
			for (Map.Entry<String, IndexEntry> page : index.entrySet()) {
				try {
					articleToDel = bot.readContent(page.getValue()
							.getPageTitle());
					articleToDel.delete();
					LOGGER.debug("DELE {}", articleToDel.getTitle());
				} catch (RuntimeException e) {
					LOGGER.warn(e.getMessage());
				} catch (ActionException e) {
					LOGGER.error(e.getMessage());
					throw new TransportError(e);
				} catch (ProcessException e) {
					LOGGER.error(e.getMessage());
					throw new TransportError(e);
				}
			}
		}
	}
}