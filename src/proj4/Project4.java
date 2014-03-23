/**
 * @version 5/ 10/ 12
 * @author Nathan Hartmann <nhartm1@umbc.edu>
 * CMSC 341 - Spring 2012 - Project #4
 * Section 01
 */
package proj4;

/**
 * The Class Project4.
 * Uses Command line arguments to play tic tac toe games.
 */
public class Project4 
{
	
	/**
	 * Instantiates a new project4.
	 *
	 * @param history the history
	 * @param display the display
	 * @param save the save
	 * @param numGames the num games
	 * @throws TicTacToePlayException the tic tac toe play exception
	 * @throws TicTacToeBoardException the tic tac toe board exception
	 */
	public Project4(boolean history, boolean display, boolean save, int numGames) throws TicTacToePlayException, TicTacToeBoardException
	{
		if(display)
	    	new Project4GUI().launchFrame(); 
		
		else
		{
			GameManager game = new GameManager(new RandomPlayer(TicTacToeValues.X_VALUE), new SmartPlayer(TicTacToeValues.O_VALUE, save), history);
			System.out.println(game.play(numGames));
		}
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String args[])
	{
		boolean history = false;
		boolean display = false; 
		boolean save = false; 
		int numGames = 0;
		
		for(String str : args)
		{
			if(str.equalsIgnoreCase("-h"))
				history = true;
			
			else if(str.equalsIgnoreCase("-d"))
				display = true;
			
			else if(str.equalsIgnoreCase("-s"))
				save = true;
			
			else
			{
				try{
					numGames = Math.abs(Integer.parseInt(str));
				}catch(Exception e){ System.out.println("Invalid ARGS");}
			}
		}
		
		try {
			new Project4(history, display, save, numGames);
		} catch (Exception e){ System.out.println("Invalid Arguements."); }                  
	}
	
}
