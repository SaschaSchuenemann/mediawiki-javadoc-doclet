package ch.zhaw.wikidoclet.page;

import java.util.Date;
import java.util.List;

import ch.zhaw.wikidoclet.formater.FormatIndexPage;
import ch.zhaw.wikidoclet.util.WikiDocletCfg;


/**
 * Represents an index page.
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 * @version 1.0
 * @see ch.zhaw.wikidoclet.page.Page
 * 
 */
public class IndexPage extends Page {


	
	private List<Page> pageList;

	private FormatIndexPage ipf;
	/**
	 * Default Constructor which only sets the Page Title. index(pageset) needs
	 * to be called before the toString method is called
	 */
	public IndexPage() {
		ipf = new FormatIndexPage();
		WikiDocletCfg cfg = WikiDocletCfg.getInstance();
		
		StringBuilder pageTitle = new StringBuilder();
		pageTitle.append(cfg.getConfigValue(WikiDocletCfg.CFG_PAR_NSPACE)).append(cfg.getConfigValue(WikiDocletCfg.CFG_PAR_PROJ)).append(cfg.getConfigValue(WikiDocletCfg.CFG_PAR_IPAGE));
		setPageTitle(pageTitle);
		
	}

	/**
	 * Returns the index page content
	 * 
	 * @see ch.zhaw.wikidoclet.page.Page#toString()
	 */
	@Override
	public String toString() {
		return ipf.createIndex(pageList);
	}

	/**
	 * This Method has to be executed before the toString can be done to add all
	 * the information inside it.
	 * 
	 * @param pageList current page list
	 */
	public void index(List<Page> pageList) {
		this.pageList = pageList;
	}

	/**
	 * getHash from Page had to be overwritten because it would loop endless.
	 * Replaced the MD5 Hash of the content with the current TimeStamp
	 * 
	 * @return TimeStamp of the IndexPage
	 */
	public String getHash() {
		return "" + new Date().getTime();
	}
}
