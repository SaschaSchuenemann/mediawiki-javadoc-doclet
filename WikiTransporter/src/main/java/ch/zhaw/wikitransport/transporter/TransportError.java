package ch.zhaw.wikitransport.transporter;

/**
 * Represents an error that occurs while exporting the pages
 * to the Wiki.
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 *
 */
public class TransportError extends Error {

	private static final long serialVersionUID = -3720653783549900250L;

	/**
	 * Constructor to set the current error message.
	 * 
	 * @param msg The error message as a String.
	 */
	public TransportError(String msg) {
		super(msg);
	}
	
	/**
	 * Constructor that sets the current TransportError message with the help
	 * of the message of a current exception.
	 * 
	 * @param e An existing exception.
	 */
	public TransportError(Exception e) {
		this(e.getMessage());
	}
}
