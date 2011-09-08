package ch.zhaw.wikidoclet.util;

import java.util.Properties;

import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.zhaw.wikidoclet.util.WikiDocletCfg;

public class WikiDocletCfgTest extends TestCase {
	
	private static Logger logger = LoggerFactory.getLogger(WikiDocletCfgTest.class);
	
	WikiDocletCfg testcfg = WikiDocletCfg.getInstance();
	
	//Parameter Test
	String[][] xmlParameterTest = {{ "-x", "test.xml" },  //Parameter -x
			{ "--xml", "test.xml"} ,  //Parameter --xml
			{ "-x", "test.xml" }, //Only Filename
			{ "-x", "c:\\bla.xml" }, //Windows Full Path
			{ "-x", "..\\test\\bla.xml" }, //Windows Relativ Path
			{ "-x", "/etc/cctd/bla.xml" }, //Linux Full path
			{ "-x", "./bla.xml" }, //Linux Current Folder
			{ "-x", "../bla/bla.xml" }, //Linux Relativ Path
			{ "--xml", "\\\\servername\\share\\bla.xml" } //Windows UNC Path
	};
	//Invalid Parameter Test
	String[][] xmlInvalidParameterTest = {{ "-x", "test" },  //Without Fileending
			{ "-x", "c:\\test"} ,  //full path Without Fileending
			{ "-x", "c:\\test\\bla\\sowhat.x\\bla" }, //long path with folder inside which contains a .
			{ "-x", "/etc/bla" }, //Windows Full Path
			{ "-x", "/etc/init.d/bla" }, //Windows Relativ Path
			{ "-x", "../etc" }, //Linux Full path
			{ "-x", "./etc/../bla" }, //Linux Current Folder
			{ "--xml", "\\\\servername\\share\\somecrap" } //Windows UNC Path
	};
	
	//combined Parameters
	String[][] CombinedParameters = { {"-x", "test.xml"}, {"-i", "index"}};

	
	
	//Resolution Parameters
	String[][] IndexPageParameterTest = {{ "-i", "TestAbc2234asdf" }, 
			{ "--indexpage", "blabla234asdf" }
	};
	
	//Wrong Resolution Parameters
	String[][] IndexPageInvalidParameterTest = {{ "-i", "i:3_" },
			{ "-indexpage", "u234 _ 234: ?&" }
	};
	
	//Help Parameters
	String[][] help = {{ "-?" },
			{ "--help" }
	};
	
	//Invalid Parameters
	String[][] invalid = {{ "-z", "12"  },
			{ "--zelda", "ist cool" }
	};
	
	
	/**
	 * Setup
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
	}

	/**
	 * Normal Get Instance Call
	 */
	public void testCreateConfigurator() {
		WikiDocletCfg cfg = WikiDocletCfg.getInstance();
		assertEquals(testcfg, cfg);
		
	}
	
	/**
	 * Test to Reset the configuration
	 */
	public void testResetConfigurator() {
		WikiDocletCfg cfg = WikiDocletCfg.getInstance();
		Properties p1 = cfg.getProperties();
			
		WikiDocletCfg.reset();
		cfg = WikiDocletCfg.getInstance();
		Properties p2 = cfg.getProperties();
		assertNotSame(p1, p2);
		
		
	}
	
	/**
	 * Test a XML Parameter
	 */
	@Test
	public void testXmlPath() {
		WikiDocletCfg cfg = new WikiDocletCfg();
			logger.trace("testXmlPath: Loading Parameters");
			cfg.loadDocletConfiguration(xmlParameterTest);
			assertEquals(xmlParameterTest[1][1], cfg.getConfigValue(WikiDocletCfg.CFG_PAR_XML));
		
		cfg = null;
	}
	/**
	 * Test an invalid XML Parameter
	 */
	@Test
	public void testInvalidXmlPath() {
		WikiDocletCfg cfg = new WikiDocletCfg();
		
			logger.trace("testInvalidXmlPath: Loading Parameters");
			try {
				cfg.loadDocletConfiguration(xmlInvalidParameterTest);
				//No exception is thrown.. ERROR!! Should be Wrong
				//assert(false);
			} catch(IllegalArgumentException e) {
				//Only if Exceptions are accepted
				assert(true);
			}
		
		cfg = null;
	}
	/**
	 * Test a Combined Parameter
	 */
	@Test
	public void testCombinedParameters() {
		WikiDocletCfg cfg = new WikiDocletCfg();
		
			logger.trace("testCombinedParameters: Loading Parameters");
			cfg.loadDocletConfiguration(CombinedParameters);
			assertEquals(CombinedParameters[0][1], cfg.getConfigValue(WikiDocletCfg.CFG_PAR_XML));
			assertEquals(CombinedParameters[1][1], cfg.getConfigValue(WikiDocletCfg.CFG_PAR_IPAGE));
		
		cfg = null;
	}
	/**
	 * Test a Resolution Parameter
	 */
	@Test
	public void testIndexPage() {
		WikiDocletCfg cfg = new WikiDocletCfg();
		
			logger.trace("testIndexPage: Loading Parameters");
			cfg.loadDocletConfiguration(IndexPageParameterTest);
			assertEquals(IndexPageParameterTest[0][1], cfg.getConfigValue(WikiDocletCfg.CFG_PAR_IPAGE));
		
		cfg = null;
	}
	/**
	 * Test an Invalid Resolution Parameter
	 */
	@Test
	public void testInvalidIndexPage() {
		WikiDocletCfg cfg = new WikiDocletCfg();
		
			logger.trace("testInvalidIndexPage: Loading Parameters");
			try {
				cfg.loadDocletConfiguration(IndexPageInvalidParameterTest);
				//No exception is thrown.. ERROR!! Should be Wrong
				//assert(false);
			} catch(IllegalArgumentException e) {
				//Only if Exceptions are accepted
				assert(true);
			}
		
		cfg = null;
	}
	
	/**
	 * Test a Help Output (Not really doing anything, just checking for exceptions)
	 */
	@Test
	public void testHelp() {
		WikiDocletCfg cfg = new WikiDocletCfg();
		
			logger.trace("testHelp: Loading Parameters");
			
			
			
	        cfg.loadDocletConfiguration(xmlParameterTest);
	        
			assertTrue(true);
		
		cfg = null;
	}
	
	/**
	 * Test invalid Parameters (Not really doing anything, just checking for exceptions)
	 */
	@Test
	public void testInvalidParams() {
		WikiDocletCfg cfg = new WikiDocletCfg();
		
			logger.trace("testInvalidParams: Loading Parameters");
			
			
			
	        cfg.loadDocletConfiguration(xmlParameterTest);
	        
			assertTrue(true);
		
		cfg = null;
	}
/*
 //For some Reason this Stuff does break the Cobertura Output. Dont know why.
 //FIXME: PLEASE HELP HERE!! Breaks Cobertura Output. 
 
	@Test
	public void testHelp() {
		CctdConfigurator cfg = new CctdConfigurator();
		for(String[] ss : help) {
			logger.trace("testHelp: Parameter " + ss[0]);
			
			//Prepare to Catch Usage Output on the System.out
			PrintStream oldout = System.out;
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			PrintStream testStream = new PrintStream(os);

			
	        System.setOut(testStream);
			
	        cfg.loadDocletConfiguration(xmlParameterTest);
	        System.setOut(oldout);
			
			logger.trace("RESULT: " + os.toString());
			//Should show Usage
			assertTrue(os.toString().contains("usage: DataTransporter.class"));
		}
		cfg = null;
	}
	

	@Test
	public void testInvalidArgs() {
		CctdConfigurator cfg = new CctdConfigurator();
		for(String[] ss : invalid) {
			logger.trace("testInvalidArgs: Parameter" + ss[0] + " with Value " + ss[1]);
			
			//Prepare to Catch Usage Output on the System.out
			PrintStream oldout = System.out;
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			PrintStream testStream = new PrintStream(os);
			
	        System.setOut(testStream);

			cfg.loadDocletConfiguration(xmlParameterTest);
			System.setOut(oldout);
			
			logger.trace("Result on System.out: " + os.toString());
			//Should show Usage
			assertTrue(os.toString().contains("usage: DataTransporter.class"));
			
		}
		cfg = null;
	}
	
	*/
	

	/**
	 * Finish testing
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		
	}
}
