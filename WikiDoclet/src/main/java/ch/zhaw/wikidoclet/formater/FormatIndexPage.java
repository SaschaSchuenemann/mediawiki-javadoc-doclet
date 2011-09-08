package ch.zhaw.wikidoclet.formater;

import java.util.List;

import ch.zhaw.wikidoclet.page.Page;

/**
 * That is the formatter class that yields the page design
 * for the indexing page that is part of our performance optimizer.
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 *
 */
public class FormatIndexPage extends FormatPage {

	/**
	 * Creates the indexing list.
	 * @param pageList page list that yields all pages to be parsed.
	 * @return output a delimited string that yields all indexing information
	 * 				  (page title, page hash value) for every page.
	 */
	public String createIndex(List<Page> pageList) {
		String output = "";
		if (pageList != null) {
			for (Page s : pageList) {
				output += s.getPageTitle() + DELIMITER + s.getHash() + FormatPage.NEWLINE;
			}
		} else {
			output += "No indexing information";
		}
		return output;
	}

}