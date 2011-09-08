package ch.zhaw.logtester;

import org.slf4j.Logger; 
import org.slf4j.LoggerFactory;
	
/**
 * A testclass for slf4j Logging in combination with any logging library. To test this example, 
 * i used log4j with success. The only dependency to this Class is the log4j.properties file in 
 * the CLASSPATH Root
 * 
 * @author Rolf
 * @version 1.0
 *
 */
public final class LogTester {
 
	/** 
	 * Creates a simple Debug output for each logging purpos in the location configured in log4j.properties
	 * If INFO Debugging is enabled an additional log entry will be created.
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		logTest();
	}
	 
	/**
	 * Optional Constructur (Empty)
	 */
	private LogTester() {
	}
 
	public static void logTest() {
		final Logger logger = LoggerFactory.getLogger(LogTester.class);
			
		//Simple trace 
		logger.trace("Trace Text");
		
		//Simple debug
		logger.debug("Debug text");
		
		//Simple INFO Logging
		logger.info("Hello World!");
		
		//Simple warn
		logger.warn("Warn text");
		
		//Simple error
		logger.error("Error text");
		 
		    
		//Check if INFO Logging is enabled here (This can be done for all logging purposes above
		if(logger.isInfoEnabled()) {
			logger.info("Dieser Text wird nur generiert wenn INFO Debugging sowieso aktiv ist. Eignet sich vorallem dann wenn Parameter extra fürs Logging generiert werden m�ssen.");
		}
	}
}