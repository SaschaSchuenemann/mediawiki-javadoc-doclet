package ch.zhaw.wikidoclet.page;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.zhaw.wikidoclet.WikiDoclet;

/**
 * An Abstract Class which represents a neutral Wiki Page. The class has some
 * Wiki Syntax functions to filter or modify Strings to have all Wiki Syntax
 * values in one place. This Class has to be extended by all possible Page Types
 * and must implement the toString() Method
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 * @version 1.0
 */
public abstract class Page {
	

	/**
	 * Contains the Wiki Page Title String
	 */
	private StringBuilder pageTitle;
	/**
	 * Default Logger
	 */
	protected static Logger logger = LoggerFactory.getLogger(WikiDoclet.class);

	/**
	 * Abstract toString Method to print the Content of the Page
	 */
	public abstract String toString();

	/**
	 * Getter for the Page Title
	 * @return Page Title
	 */
	public String getPageTitle() {
		return this.pageTitle.toString();
	}

	/**
	 * Used to set the Page Title from the Constructors of the Pages
	 * @param pageTitle
	 */
	protected void setPageTitle(StringBuilder pageTitle) {
		this.pageTitle = pageTitle;
	}

	/**
	 * Calculates an MD5 Hash of the toString method
	 * @return MD5 Hash of the content of the Page
	 */
	public String getHash() {
		String output = "";
		try {
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");

			digest.update(this.toString().getBytes());
			byte[] hash = digest.digest();

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < hash.length; i++) {
				hexString.append(Integer.toHexString(0xFF & hash[i]));
			}
			output += hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			logger.error("Error while searching algorithmus. The error was: {}", e);
			// FALLBACK to standard hashcode:
			output += this.toString().hashCode();
		}
		return output;
	}
}