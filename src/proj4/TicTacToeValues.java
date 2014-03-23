/**
 * @version 5/ 10/ 12
 * @author Nathan Hartmann <nhartm1@umbc.edu>
 * CMSC 341 - Spring 2012 - Project #4
 * Section 01
 */
package proj4;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * The Enum TicTacToeValues.
 */
public enum TicTacToeValues 
{
	
	/** The X_ value. */
	X_VALUE('X', 2, "images/X.jpg"), 
	/** The O_ value. */
	O_VALUE('O', 1, "images/O.jpg"), 
	/** The N o_ value. */
	NO_VALUE('.', 0, "images/blank.jpg"), 
	/** The NUL l_ value. */
	NULL_VALUE(' ', -1, "images/blank.jpg");
	
	/** The char value. */
	private char charValue;
	
	/** The num value. */
	private int numValue;
	
	/** The image. */
	private BufferedImage image;
	
	/** The Constant values. */
	private static final TicTacToeValues[] values = { X_VALUE, O_VALUE, NO_VALUE, NULL_VALUE};
	
	/**
	 * Instantiates a new tic tac toe values.
	 *
	 * @param c the c
	 * @param n the n
	 * @param icon the icon
	 */
	private TicTacToeValues(char c, int n, String icon)
	{
		this.charValue = c;
		this.numValue = n;
		loadImage(icon);
	}

	/**
	 * Load image.
	 *
	 * @param icon the icon
	 */
	private void loadImage(String icon){
		image = null;
		try{
			image = ImageIO.read(getClass().getResource(icon));
		}catch(Exception e){
			System.out.println("Error: Could not load image: " + icon + "\n\tDoes not load image while on GL servers.");
		}
	}
	/**
	 * Gets the char value.
	 *
	 * @return the value
	 */
	public char getValue()
	{
		return this.charValue;
	}
	
	/**
	 * Gets the num value.
	 *
	 * @return the num value
	 */
	public int getNumValue()
	{
		return this.numValue;
	}
		
	/**
	 * Gets the image.
	 *
	 * @return the image
	 */
	public BufferedImage getImage()
	{
		return image;
	}	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString()
	{
		return String.valueOf(this.getValue()); 
	}

	/**
	 * Gets the value of.
	 *
	 * @param cmd the cmd
	 * @return the value of
	 */
	public static TicTacToeValues getValueOf(char cmd)
	{			
		for(TicTacToeValues val : values)
			if(cmd == val.getValue())
				return val;
		
		return NULL_VALUE;
	}
	
	/**
	 * Gets the value of.
	 *
	 * @param i the i
	 * @return the value of
	 */
	public static TicTacToeValues getValueOf(int i)
	{			
		for(TicTacToeValues val : values)
			if(i == val.getNumValue())
				return val;
		
		return NULL_VALUE;
	}
		
	
	/**
	 * Equals.
	 *
	 * @param other the other
	 * @return true, if successful
	 */
	public boolean equals(TicTacToeValues other)
	{
		return this.charValue == other.charValue;
	}

}
