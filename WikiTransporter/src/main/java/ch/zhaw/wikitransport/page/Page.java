package ch.zhaw.wikitransport.page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Class that encapsulates a page record read from the XML-File generated
 * with the help of the WikiDoclet class. Within the XML-File there are
 * several page records...
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 *
 */
public class Page {

	private static final Logger LOGGER = LoggerFactory.getLogger(Page.class);
	private StringBuilder titleValue;
	private StringBuilder contentValue;
	private StringBuilder hashValue;
	private StringBuilder timeValue;
	
	/**
	 * Default constructor sets all the needed instance variables. 
	 */
	public Page(){
		this.titleValue = new StringBuilder(75);
		this.contentValue = new StringBuilder(5000);
		this.hashValue = new StringBuilder(32);
		this.timeValue = new StringBuilder(15);
	}
	
	/**
	 * Method to return the title value of a current page record.
	 * @return titleValue the title of the current page record.
	 */
	public String getTitleValue() {
		return titleValue.toString();
	}
	
	/**
	 * Method to set the title value of a certain page record.
	 * 
	 * @param ch  char array containing current chars
	 * @param start  integer containing the start position to read from.
	 * @param length  integer read till this position
	 */
	public void setTitleValue(char[] ch, int start, int length) {
		LOGGER.trace("Title Appending {}", length);
		this.titleValue.append(ch, start, length);
	}
	
	/**
	 * Method to set the title value of a certain page record.
	 * 
	 * @param titleValue  a String representing the title value.
	 */
	public void setTitleValue(String titleValue){
		this.titleValue.append(titleValue);
	}
	
	/**
	 * Method to get the value of the content. The content is basically wiki syntax 
	 * stored in a string. 
	 * @return contentValue  the content of the current page record.
	 */
	public String getContentValue() {
		return contentValue.toString();
	}
	
	/**
	 * Method to set the content value of a current page record.
	 *
	 * @param ch  char array containing current chars
	 * @param start  integer containing the start position to read from.
	 * @param length  integer read till this position
	 */
	public void setContentValue(char[] ch, int start, int length) {
		LOGGER.trace("Content Appending {}", length);
		this.contentValue.append(ch, start, length);
	}
	
	/**
	 * Method to set the content value.
	 * 
	 * @param contentValue  a String representing the content value.
	 */
	public void setContentValue(String contentValue){
		this.contentValue.append(contentValue);
	}
	
	/**
	 * Method to get the hash value of a current page record.
	 * @return hashValue - the hash value of the current page record.
	 */
	public String getHashValue() {
		return hashValue.toString();
	}
	
	/**
	 * Method to set the hash value of a current page record.
	 * 
	 * @param ch  char array containing current chars
	 * @param start  integer containing the start position to read from.
	 * @param length  integer read till this position
	 */
	public void setHashValue(char[] ch, int start, int length) {
		LOGGER.trace("Hash Appending {}", length);
		this.hashValue.append(ch, start, length);
	}
	
	/**
	 * Method to set the hash value.
	 * 
	 * @param hashValue  a String representing the hash value.
	 */
	public void setHashValue(String hashValue){
		this.hashValue.append(hashValue);
	}
	
	/**
	 * Method to get the time value of a current page record.
	 * @return timeValue  the time value of the current page record.
	 */
	public String getTimeValue() {
		return timeValue.toString();
	}
	
	/**
	 * Method to set the time value of a current page record.
	 * 
	 * @param ch  char array containing current chars
	 * @param start  integer containing the start position to read from.
	 * @param length  integer read till this position
	 */
	public void setTimeValue(char[] ch, int start, int length) {
		LOGGER.trace("Time Appending {}", timeValue.length());
		this.timeValue.append(ch, start, length);
	}
	
	/**
	 * Method to set the time value
	 * 
	 * @param timeValue a String representing the timeValue.
	 */
	public void setTimeValue(String timeValue){
		this.timeValue.append(timeValue);
	}
}