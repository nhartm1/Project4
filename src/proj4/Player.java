/**
 * @version 5/ 10/ 12
 * @author Nathan Hartmann <nhartm1@umbc.edu>
 * CMSC 341 - Spring 2012 - Project #4
 * Section 01
 */
package proj4;

/**
 * The Class Player.
 * Every Tic Tac Toe player extends this class.
 */
public abstract class Player 
{
	
	/** The marker. */
	private TicTacToeValues marker;

	/** The wins. */
	private int wins;
	
	/** The losses. */
	private int losses;
	
	/** The draws. */
	private int draws;
	
	/** The num games played. */
	private int numGamesPlayed;
	
	/**
	 * Instantiates a new random player.
	 *
	 * @param mark the mark
	 * @throws TicTacToePlayException the tic tac toe play exception
	 */
	public Player(TicTacToeValues mark) throws TicTacToePlayException
	{
		if(mark == TicTacToeValues.O_VALUE || mark == TicTacToeValues.X_VALUE)
		{
			this.marker = mark; 
			wins = 0;
			losses = 0;
			draws = 0;
			numGamesPlayed = 0;
		}
		
		else
			throw new TicTacToePlayException("Invalid Player");
	}
	
	/**
	 * Gets the player.
	 *
	 * @return the player
	 */
	public TicTacToeValues getPlayer()
	{
		return this.marker;
	}

	/**
	 * Gets the numbe of games played.
	 *
	 * @return the num games played
	 */
	public int getNumGamesPlayed()
	{
		return this.numGamesPlayed;
	}
	
	/**
	 * Gets the winning percentile.
	 * ( Wins / Number of games Played )
	 *
	 * @return the winning percentile
	 */
	public double getWinningPercentile()
	{
		if(numGamesPlayed > 0)
			return ((double) wins / (double)numGamesPlayed ) * 100;
		
		return 0;
	}
	
	/**
	 * Gets the number of wins.
	 *
	 * @return the num wins
	 */
	public int getNumWins()
	{
		return this.wins;
	}

	/**
	 * Gets the number of draws.
	 *
	 * @return the num draws
	 */
	public int getNumDraws()
	{
		return this.draws;
	}
	
	
	/**
	 * Make move, returns the tic tac toe board after a valid move has been made.
	 *
	 * @param currentBoard the current board
	 * @return the int
	 * @throws TicTacToeBoardException the tic tac toe board exception
	 * @throws TicTacToePlayException the tic tac toe play exception
	 */
	public abstract Tx3Board makeMove(Tx3Board currentBoard) throws TicTacToeBoardException, TicTacToePlayException; 

	/**
	 * Make indexed move, returns an index to move at instead of the board.
	 *
	 * @param currentBoard the current board
	 * @return the int
	 * @throws TicTacToeBoardException the tic tac toe board exception
	 * @throws TicTacToePlayException the tic tac toe play exception
	 */
	public abstract int makeIndexedMove(Tx3Board currentBoard) throws TicTacToeBoardException, TicTacToePlayException; 

	
	/**
	 * Gets the final report.
	 *
	 * @return the final report
	 */
	public abstract String getFinalReport();
	
	/**
	 * Game over.
	 *
	 * @param value the value
	 */
	public void gameOver(TicTacToeValues value)
	{
		this.numGamesPlayed++;
		
		if(this.getPlayer() == value)
			this.wins++;
		
		else if(value == TicTacToeValues.NO_VALUE)
			this.draws++;
		
		else
			this.losses++;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		String str = "\tPlayer " + marker.getValue() + ":\n";
		str += String.format("\t\tNum of Wins: %d", wins) + "\n";
		str += String.format("\t\tPercentile of wins : %.2f", getWinningPercentile()) + "%\n";
		
		return str;
	}
}
