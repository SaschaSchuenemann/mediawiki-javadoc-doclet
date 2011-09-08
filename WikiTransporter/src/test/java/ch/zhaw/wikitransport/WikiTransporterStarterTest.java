package ch.zhaw.wikitransport;

import java.net.URL;

import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.zhaw.wikitransport.WikiTransporterStarter;
import ch.zhaw.wikitransport.transporter.Transporter;

/**
 * This Test is only working with a real configured Wiki.
 * To simulate a Wiki via Mock would be to complicated therefore
 * we ignore this test at current stage in project.
 * @author Christian Dubs
 */

@Ignore
public class WikiTransporterStarterTest {
	private static final Logger logger = LoggerFactory.getLogger(WikiTransporterStarterTest.class);

	@Test
	public void testMain() {
		//Not really testing anything.. Just checking if its working
		URL xmlInputFile = this.getClass().getResource("wikiTransporterTestMain.xml");
		try {
			WikiTransporterStarter.main(new String[]{ "-x", xmlInputFile.getPath()});
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test(expected=Error.class)
	public void testwrongXml() {
		//Not really testing anything.. Just checking if its not working
		URL xmlInputFile = this.getClass().getResource("wikiTransporterTestWrongXml.xml");
		WikiTransporterStarter.main(new String[]{ "-x", xmlInputFile.getPath()});
	}
	
	@Ignore
	public void testTransport() {
		logger.trace("testRun");
		
		Transporter t = EasyMock.createMock(Transporter.class);
		t.connect();
		EasyMock.expectLastCall();
		
		t.buildIndex(null);
		EasyMock.expectLastCall();
		
		t.transport(null);
		EasyMock.expectLastCall();
		
		t.clean();
		EasyMock.expectLastCall();
		
		EasyMock.replay(t);

		WikiTransporterStarter wt = new WikiTransporterStarter();
		wt.startTransporting();
		
		EasyMock.verify(t);
	}
}
