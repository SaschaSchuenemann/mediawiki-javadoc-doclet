package ch.zhaw.wikitransport.transporter.jwbf.threading;

import net.sourceforge.jwbf.core.actions.util.ActionException;
import net.sourceforge.jwbf.core.actions.util.ProcessException;
import net.sourceforge.jwbf.core.contentRep.Article;
import net.sourceforge.jwbf.core.contentRep.SimpleArticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.zhaw.wikitransport.page.Page;
import ch.zhaw.wikitransport.transporter.jwbf.bot.MediaWikiDocletBot;
import ch.zhaw.wikitransport.transporter.jwbf.bot.PageToBigException;
import ch.zhaw.wikitransport.util.WikiTransporterCfg;

/**
 * This class represents a page writer used to finally write the WikiDoc pages
 * into the MediaWiki.
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 *
 */
public class PageWriter {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PageWriter.class);

	private MediaWikiDocletBot bot;
	private Page page = null;

	/**
	 * 
	 * 
	 * @param bot
	 */
	public PageWriter(MediaWikiDocletBot bot) {

		this.bot = bot;
	}

	/**
	 * 
	 * 
	 * @param page
	 */
	public void setPage(Page page) {
		this.page = page;
	}

	/**
	 * 
	 * 
	 */
	public void writeArticleIfPageExists() {
		if (page != null) {
			// Write the Page
			writeArticle();
		}
		page = null;
	}

	private void writeArticle() {
		SimpleArticle simpleArticle;
		Article article;
		simpleArticle = new SimpleArticle();
		article = new Article(bot, simpleArticle);

		article.setTitle(page.getTitleValue());
		simpleArticle.addText(page.getContentValue());
		LOGGER.debug("Try to write {}", article.getTitle());
		int retry = 1;
		while (retry <= WikiTransporterCfg.MAX_NUMBER_OF_RETRIES) {
			LOGGER.debug("TRY: {}", retry);
			try {
				LOGGER.debug("saving..");
				article.save("WikiDoclet: Page has been updated");
				LOGGER.debug("save complete");
				break;
			} catch (ActionException e) {

				retry++;
				LOGGER.warn("ActionException ", e);
			} catch (PageToBigException e) {
				//Successfully Saved, but with too big Warning
				LOGGER.warn("PageToBigException: {} ", e.getMessage());
				break;
				
			} catch (ProcessException e) {

				retry++;
				LOGGER.warn("ProcessException ", e);
			}
		}
		if (retry > WikiTransporterCfg.MAX_NUMBER_OF_RETRIES) {
			LOGGER.error("Article could not be written after {} retrys: {}",
					WikiTransporterCfg.MAX_NUMBER_OF_RETRIES,
					article.getTitle());
		} else {
			LOGGER.debug("Success: {} (Try: {})", article.getTitle(), retry);
		}

	}
}