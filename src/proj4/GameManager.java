/**
 * @version 5/ 10/ 12
 * @author Nathan Hartmann <nhartm1@umbc.edu>
 * CMSC 341 - Spring 2012 - Project #4
 * Section 01
 */
package proj4;

/**
 * The Class GameManager.
 */
public class GameManager 
{	
	/** The player one. */
	private Player playerOne;
	
	/** The player two. */
	private Player playerTwo;

	/** The display info. */
	private boolean displayInfo;
	
	/**
	 * Instantiates a new game manager.
	 * 
	 * @param one the one
	 * @param two the two
	 * @param history the history
	 * @throws TicTacToePlayException the tic tac toe play exception
	 */
	public GameManager(Player one, Player two, boolean history) throws TicTacToePlayException
	{
		if(one.getPlayer() == two.getPlayer() || one.getPlayer() != TicTacToeValues.X_VALUE 
				|| two.getPlayer() != TicTacToeValues.O_VALUE)
			throw new TicTacToePlayException("Invalid players!");
		
		this.playerOne = one;
		this.playerTwo = two;
		displayInfo = history;
	}
	
	/**
	 * Plays One game.
	 *
	 * @return the string
	 * @throws TicTacToePlayException the tic tac toe play exception
	 * @throws TicTacToeBoardException the tic tac toe board exception
	 */
	public String play() throws TicTacToePlayException, TicTacToeBoardException
	{
		return play(1);
	}
	
	
	/**
	 * Plays a particular number of games, returns final report.
	 *
	 * @param numPlays the num plays
	 * @return the string
	 * @throws TicTacToePlayException the tic tac toe play exception
	 * @throws TicTacToeBoardException the tic tac toe board exception
	 */
	public String play(int numPlays) throws TicTacToePlayException, TicTacToeBoardException
	{
		Tx3Board game;
		boolean playerOneTurn;
		TicTacToeValues winner;
		
		while(numPlays > 0)
		{	
			game = new Tx3Board();
			playerOneTurn = true;
			winner = TicTacToeValues.NO_VALUE;
			
			while(winner == TicTacToeValues.NO_VALUE && game.numMovesLeft() > 0)
			{		
				if(displayInfo == true)
					System.out.println(game + "\n");
					
				if(playerOneTurn)
					game = playerOne.makeMove(game);

				else
					game = playerTwo.makeMove(game);

				playerOneTurn = !playerOneTurn;
				winner = game.whoWon();
			}	
			
			playerOne.gameOver(winner);
			playerTwo.gameOver(winner);
			
			numPlays--;
			
			if(displayInfo == true)
				System.out.println(game + "\n" + this);			
		}
		
		return finalReport();
	}
	
	/**
	 * Final report of games from both players.
	 *
	 * @return the string
	 */
	private String finalReport()
	{
		String str = "FINAL REPORT:\n\n"; 
		str += playerOne.getFinalReport() + "\n";
		str += playerTwo.getFinalReport();
		return str;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		String str = "Stats After " +  playerOne.getNumGamesPlayed() + " Games Played:\n";
		str += "\n\t" + "Number of Draws: " + playerOne.getNumDraws() + "\n";
		str += "\n" + playerOne.toString();
		str += "\n" + playerTwo.toString();
		return str;
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws TicTacToePlayException the tic tac toe play exception
	 * @throws TicTacToeBoardException the tic tac toe board exception
	 */
	public static void main(String args[]) throws TicTacToePlayException, TicTacToeBoardException
	{
		GameManager game = new GameManager( new RandomPlayer(TicTacToeValues.X_VALUE),
				new SmartPlayer(TicTacToeValues.O_VALUE, false), true);
		
		game.play(18000);
	}
}
