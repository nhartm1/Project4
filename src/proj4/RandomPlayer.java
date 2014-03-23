/**
 * @version 5/ 10/ 12
 * @author Nathan Hartmann <nhartm1@umbc.edu>
 * CMSC 341 - Spring 2012 - Project #4
 * Section 01
 */
package proj4;

import java.util.Random;

/**
 * The Class RandomPlayer.
 */
public class RandomPlayer extends Player
{	
	/** The generator. */
	protected Random generator;
	
	/**
	 * Instantiates a new random player.
	 *
	 * @param mark the mark
	 * @throws TicTacToePlayException the tic tac toe play exception
	 */
	public RandomPlayer(TicTacToeValues mark) throws TicTacToePlayException
	{
		super(mark);
		generator = new Random();
	}
	
	/**
	 * Random move, returns a random index that is a valid move.
	 *
	 * @param currentBoard the current board
	 * @return the int
	 */
	public int randomMove(Tx3Board currentBoard)
	{
		if(currentBoard.numMovesLeft() == 0)
			return -1;
		
		try
		{
			int[] validMoves = currentBoard.getValidMoves();
			return validMoves[generator.nextInt(validMoves.length)];
		}
		catch(TicTacToePlayException b)
		{
			System.out.println(b.getMessage());
		}
		
		return -1;
	}
	
	/**
	 * Make move, returns a game board after a move was made.
	 *
	 * @param currentBoard the current board
	 * @return the int
	 * @throws TicTacToeBoardException the tic tac toe board exception
	 * @throws TicTacToePlayException the tic tac toe play exception
	 */
	public Tx3Board makeMove(Tx3Board currentBoard) throws TicTacToeBoardException, TicTacToePlayException
	{
		if(currentBoard == null)
			throw new TicTacToeBoardException("Invalid Board-ish");
		
		currentBoard.move(getPlayer(), randomMove(currentBoard));
		return currentBoard;
	}	
	
	/* (non-Javadoc)
	 * @see proj4.Player#makeIndexedMove(proj4.Tx3Board)
	 */
	@Override
	public int makeIndexedMove(Tx3Board currentBoard)throws TicTacToeBoardException, TicTacToePlayException 
	{
		return randomMove(currentBoard);
	}

	/* (non-Javadoc)
	 * @see proj4.Player#getFinalReport()
	 */
	public String getFinalReport()
	{
		return "Random has won " + getNumWins() + " times which is " +String.format("%.2f percent", getWinningPercentile());
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
		Tx3Board game = null;
		RandomPlayer tim = null;
		RandomPlayer tom = null;
		int numGames = 18000;
		
		try {
			tim = new RandomPlayer(TicTacToeValues.X_VALUE);
			tom = new RandomPlayer(TicTacToeValues.O_VALUE);
		} catch (TicTacToePlayException e) {
			e.printStackTrace();
		}		
		
		while(numGames-- > 0)
		{

			try 
			{
				game = new Tx3Board(".........");
			}
			catch (TicTacToeBoardException e) {

				e.printStackTrace();
			}
			
			while(game.numMovesLeft() > 0 && game.whoWon() == TicTacToeValues.NO_VALUE)
			{
				if((game.numMovesLeft() + 1) % 2 == 0)
					game.move(tim.getPlayer(), tim.randomMove(game));
				else
					game.move(tom.getPlayer(), tom.randomMove(game));	
				System.out.println(game);
				System.out.println("----------");
			}
			System.out.println("===============");
			tom.gameOver(game.whoWon());			
			tim.gameOver(game.whoWon());
		}
		
		System.out.println(tim.getPlayer() + " won " + tim.getWinningPercentile() + " games.");
		System.out.println(tom.getPlayer() + " won " + tom.getWinningPercentile() + " games.");		
	}
	
}
