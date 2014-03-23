/**
 * @version 5/ 10/ 12
 * @author Nathan Hartmann <nhartm1@umbc.edu>
 * CMSC 341 - Spring 2012 - Project #4
 * Section 01
 */
package proj4;

/**
 * The Class TicTacToeBoardException.
 */
public class TicTacToeBoardException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Instantiates a new tic tac toe board exception.
	 *
	 * @param message the message
	 */
	public TicTacToeBoardException(String message)
	{
		super(message);
	}

}
