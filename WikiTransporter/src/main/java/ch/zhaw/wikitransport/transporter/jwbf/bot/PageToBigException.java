package ch.zhaw.wikitransport.transporter.jwbf.bot;

import net.sourceforge.jwbf.core.actions.util.ProcessException;

/**
 * Represents the error thrown, if the page is too big that is
 * if the page is bigger than 32 kilobytes. Exception does not
 * finish the application.
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 *
 */

public class PageToBigException extends ProcessException {
	private static final long serialVersionUID = 5215489887642306125L;

	/**
	 * Constructor to pass an error message as a string to the
	 * current PageToBigException.
	 * 
	 * @param msg
	 */
	public PageToBigException(String msg) {
		super(msg);
	}
}
