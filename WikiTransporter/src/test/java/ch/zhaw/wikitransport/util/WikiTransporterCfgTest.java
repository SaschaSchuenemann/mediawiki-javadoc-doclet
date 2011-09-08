package ch.zhaw.wikitransport.util;

import java.util.Properties;

import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.zhaw.wikitransport.util.WikiTransporterCfg;

public class WikiTransporterCfgTest extends TestCase {
	
	private static Logger logger = LoggerFactory.getLogger(WikiTransporterCfgTest.class);
	
	WikiTransporterCfg testcfg = WikiTransporterCfg.getInstance();
	
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
	String[][] CombinedParameters = {{ "-w", "http://test.ch/mediawiki/api.php", "-u", "admin", "-p", "passxy" },  //smallparameter
			{ "--wikiurl", "http://test.ch/mediawiki/api.php", "--username", "admin", "--password", "passxy"}   //full parameter
	};

	
	
	//Resolution Parameters
	String[][] usernameParameterTest = {{ "-u", "javadoc" }, 
			{ "--username", "javadoc" }
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
		WikiTransporterCfg cfg = WikiTransporterCfg.getInstance();
		assertEquals(testcfg, cfg);
		
	}
	
	/**
	 * Test to Reset the configuration
	 */
	public void testResetConfigurator() {
		WikiTransporterCfg cfg = WikiTransporterCfg.getInstance();
		Properties p1 = cfg.getProperties();
			
		WikiTransporterCfg.reset();
		cfg = WikiTransporterCfg.getInstance();
		Properties p2 = cfg.getProperties();
		assertNotSame(p1, p2);
		
		
	}
	
	/**
	 * Test a XML Parameter
	 */
	@Test
	public void testXmlPath() {
		WikiTransporterCfg cfg = new WikiTransporterCfg();
		for(String[] ss : xmlParameterTest) {
			logger.trace("testXmlPath: Parameter" + ss[0] + " with Value " + ss[1]);
			cfg.loadDataTransporterConfig(ss);
			assertEquals(ss[1], cfg.getConfigValue(WikiTransporterCfg.CFG_PAR_XML));
		}
		cfg = null;
	}
	/**
	 * Test an invalid XML Parameter
	 */
	@Test
	public void testInvalidXmlPath() {
		WikiTransporterCfg cfg = new WikiTransporterCfg();
		for(String[] ss : xmlInvalidParameterTest) {
			logger.trace("testInvalidXmlPath: Parameter" + ss[0] + " with Value " + ss[1]);
			try {
				cfg.loadDataTransporterConfig(ss);
				//No exception is thrown.. ERROR!! Should be Wrong
				assert(false);
			} catch(IllegalArgumentException e) {
				//Only if Exceptions are accepted
				assert(true);
			}
		}
		cfg = null;
	}
	/**
	 * Test a Combined Parameter
	 */
	@Test
	public void testCombinedParameters() {
		WikiTransporterCfg cfg = new WikiTransporterCfg();
		for(String[] ss : CombinedParameters) {
			logger.trace("testCombinedParameters: Parameter" + ss[0] + " with Value " + ss[1] + " and Parameter" + ss[2] + " with Value " + ss[3]);
			cfg.loadDataTransporterConfig(ss);
			assertEquals(ss[1], cfg.getConfigValue(WikiTransporterCfg.CFG_PAR_URL));
			assertEquals(ss[3], cfg.getConfigValue(WikiTransporterCfg.CFG_PAR_UNAME));
			assertEquals(ss[5], cfg.getConfigValue(WikiTransporterCfg.CFG_PAR_PASSWD));
		}
		cfg = null;
	}
	
	/**
	 * Test a Help Output (Not really doing anything, just checking for exceptions)
	 */
	@Test
	public void testHelp() {
		WikiTransporterCfg cfg = new WikiTransporterCfg();
		for(String[] ss : help) {
			logger.trace("testHelp: Parameter " + ss[0]);
			
			
			
	        cfg.loadDataTransporterConfig(ss);
	        
			assertTrue(true);
		}
		cfg = null;
	}
	
	/**
	 * Test invalid Parameters (Not really doing anything, just checking for exceptions)
	 */
	@Test
	public void testInvalidParams() {
		WikiTransporterCfg cfg = new WikiTransporterCfg();
		for(String[] ss : invalid) {
			logger.trace("testInvalidParams: Parameter " + ss[0]);
			
			
			
	        cfg.loadDataTransporterConfig(ss);
	        
			assertTrue(true);
		}
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
			
	        cfg.loadDataTransporterConfig(ss);
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

			cfg.loadDataTransporterConfig(ss);
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
