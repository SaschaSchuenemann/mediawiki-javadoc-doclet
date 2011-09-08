package ch.zhaw.wikitransport.transporter;

import java.util.List;

import ch.zhaw.wikitransport.page.Page;

/**
 * Defines the functionality a transporter has to provide.
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 *
 */
public interface Transporter {

	/**
	 * Establishes the communication channel
	 */
	public void connect();
	
	/**
	 * Set the index page name
	 * @param indexPageName
	 */
	public void buildIndex(String indexPageName);

	/**
	 * Upload a predefined set of pages
	 */
	public void transport(List<Page> pages);

	/**
	 * Removes old pages that are not used anymore
	 * 
	 */
	public void clean();

}
