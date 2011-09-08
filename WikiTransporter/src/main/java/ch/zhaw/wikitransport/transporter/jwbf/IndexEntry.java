package ch.zhaw.wikitransport.transporter.jwbf;

/**
 * This class represents a WikiDocMetaData entry; that is an entry
 * on a hidden (not  linked) wiki page, where information about the existing wiki 
 * pages are stored in a cvs (comma and newline separated) style.
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 *
 */
public class IndexEntry {
	
	private String pageTitle;
	private String hash;

	/**
	 * That is the constructor that is being called while initializing a new object.
	 * This constructor basically sets all the needed information about a WikiDocMetaData entry that
	 * have to be stored.
	 * @param pageTitle -A string representing the page title of an existing WikiDoc page.
	 * @param hash -A string representing the hash value of the existing WikiDoc page.
	 */
	public IndexEntry(String pageTitle, String hash){
		this.pageTitle = pageTitle;
		this.hash = hash;
	}
	
	/**
	 * Method to get the pageTitle of an existing WikiDoc page.
	 * @return pageTitle - The string representing the pageTitle of an existing WikiDoc page.
	 */
	public String getPageTitle() {
		return pageTitle;
	}
	
	/**
	 * Method to get the hash value of an existing WikiDoc page.
	 * @return hash - The string representing the hash value of an existing WikiDoc page.
	 */
	public String getHash() {
		return hash;
	}
}