package ch.zhaw.wikitransport.transporter.sql;

import java.util.List;

import ch.zhaw.wikitransport.page.Page;
import ch.zhaw.wikitransport.transporter.Transporter;

/**
 * This class could be a transporter to an sql database of any kind. 
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 *
 */
public class TransporterSQL implements Transporter  {

	/**
	 * Yields the steps to establish the communication channel
	 * between the concrete tranporter and the Wiki.
	 */
	@Override
	public void connect() {
		// TODO Auto-generated method stub
	}

	@Override
	public void buildIndex(String indexPageName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void transport(List<Page> pages) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clean() {
		// TODO Auto-generated method stub
		
	}

}
