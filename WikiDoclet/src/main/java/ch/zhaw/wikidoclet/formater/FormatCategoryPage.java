package ch.zhaw.wikidoclet.formater;

/**
 * That is the formatter class for the category page.
 * It only prints a place holder to the heading section
 * of the Wiki page. The reason, why to do this, is that the page
 * is displayed as existing with content (no red link in MediaWiki).
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 *
 */
public class FormatCategoryPage extends FormatPage{
	
	public FormatCategoryPage() {

	}
	/**
	 * Prints the Head of the Wiki Category Page
	 * 
	 */
	public StringBuilder printHead() {
		StringBuilder output = new StringBuilder();
		output.append("Javadoc Category");
		return  output;

	}
}
