package ch.zhaw.wikitransport;

import junit.framework.TestCase;

import org.junit.Test;

import ch.zhaw.wikitransport.page.Page;

public class TestWikiTransporterPage extends TestCase {
	
	private Page myTestPage = null;
	
	/**
	 * Setup
	 */
	protected void setUp() throws Exception {
		super.setUp();
		myTestPage = new Page();
	}
	
	@Test
	public void testTitle(){
		String titleTestString = "That is a test!!!";

		myTestPage.setTitleValue(titleTestString);
		assertEquals(titleTestString, myTestPage.getTitleValue());
	}
	
	@Test
	public void testContent(){
		String contentTestString = "That is a test content!!! [TOC]!!!!";
		
		myTestPage.setContentValue(contentTestString);
		assertEquals(contentTestString, myTestPage.getContentValue());
	}
	
	@Test
	public void testHash(){
		String hashTestString = "That is a test hash!!!";
		
		myTestPage.setHashValue(hashTestString);
		assertEquals(hashTestString, myTestPage.getHashValue());
		
	}
	
	@Test
	public void testTime(){
		String timeTestString = "That is a test!!!";
		
		myTestPage.setTimeValue(timeTestString);
		assertEquals(timeTestString, myTestPage.getTimeValue());
	}
	
	
	/**
	 * Finish testing
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		myTestPage = null;
	}
}
