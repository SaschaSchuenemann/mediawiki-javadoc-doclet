package ch.zhaw.wikitransport.util;

import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.zhaw.wikitransport.util.SSLUtilities;

public class SSLUtilitiesTest extends TestCase {
	
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(SSLUtilitiesTest.class);
	
	
	/**
	 * Setup
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
	}

	/**
	 * trustAllHttpsCertificates
	 * @throws Exception 
	 */
	@Test
	public void testTrustAllHttpsCertificates() throws Exception {
		SSLUtilities.setTrustAllCerts();
		SSLUtilities.setTrustAllCerts();
	}
	
	/**
	 * trustAllHostnames
	 * @throws Exception 
	 */
	@Test
	public void testTrustAllHostnames() throws Exception {
		SSLUtilities.setTrustAllCerts();
		SSLUtilities.setTrustAllCerts();
	}
	
	

	/**
	 * Finish testing
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		
	}
}
