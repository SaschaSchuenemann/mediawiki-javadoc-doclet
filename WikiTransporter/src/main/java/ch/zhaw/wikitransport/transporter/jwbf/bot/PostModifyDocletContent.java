package ch.zhaw.wikitransport.transporter.jwbf.bot;

import net.sourceforge.jwbf.core.actions.util.ActionException;
import net.sourceforge.jwbf.core.actions.util.HttpAction;
import net.sourceforge.jwbf.core.actions.util.ProcessException;
import net.sourceforge.jwbf.core.contentRep.SimpleArticle;
import net.sourceforge.jwbf.mediawiki.actions.editing.PostModifyContent;
import net.sourceforge.jwbf.mediawiki.bots.MediaWikiBot;

import org.apache.log4j.Logger;

/**
 * PostModifyContent wrapper to enable bigger Error Reporting
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 */

public class PostModifyDocletContent extends PostModifyContent {
	private static final Logger LOGGER = Logger.getLogger(PostModifyDocletContent.class);

	public PostModifyDocletContent(MediaWikiDocletBot bot, SimpleArticle a)
			throws ActionException, ProcessException {
		super((MediaWikiBot) bot, a);
	}

	/**
	 * Wrapping the returning function to show the complete error message
	 * 
	 * @param s The string returned by the api.php of the MediaWiki
	 * @param hm The current HttpAction
	 */
	@Override
	public String processReturningText(String s, HttpAction hm)
			throws ProcessException {
		if (s.contains("error")) {
			if(s.contains("WARNING: This")) {
				//WARNING: This page is 65 kilobytes long; some browsers may have problems editing pages approaching or longer than 32kb.
				//Please consider breaking the page into smaller sections.
				LOGGER.warn("Page is bigger than 32 kilobytes.");
				throw new PageToBigException("Page is bigger than 32 kilobytes.");
			} else if(s.contains("Editing Javadoc")) {
				LOGGER.warn("Edit Page shown.");
				throw new PageToBigException("Edit Page Shown.");
			} else {
				throw new ProcessException(s);
			}
		}

		return super.processReturningText(s, hm);
	}
}