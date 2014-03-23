/**
 * @version 5/ 10/ 12
 * @author Nathan Hartmann <nhartm1@umbc.edu>
 * CMSC 341 - Spring 2012 - Project #4
 * Section 01
 */
package proj4;

import java.util.Scanner;

/**
 * The Class HumanPlayer.
 * Use the terminal and input the index at which you would like to play.
 */
public class HumanPlayer extends Player 
{
	
	/**
	 * Instantiates a new human player.
	 *
	 * @param mark the mark
	 * @throws TicTacToePlayException the tic tac toe play exception
	 */
	public HumanPlayer(TicTacToeValues mark) throws TicTacToePlayException
	{
		super(mark);
	}

	/* (non-Javadoc)
	 * @see proj4.Player#makeMove(proj4.Tx3Board)
	 */
	public Tx3Board makeMove(Tx3Board currentBoard)	throws TicTacToeBoardException, TicTacToePlayException 
	{
		currentBoard.move(this.getPlayer(), askForMove(currentBoard));
		return currentBoard;
	}

	/* (non-Javadoc)
	 * @see proj4.Player#getFinalReport()
	 */
	public String getFinalReport() 
	{
		return "Human has won " + getNumWins() + " times which is " +String.format("%.0f percent", (getWinningPercentile()) * 100);
	}
	
	/**
	 * Ask for move from user.
	 *
	 * @param currentBoard the current board
	 * @return the int
	 * @throws TicTacToePlayException the tic tac toe play exception
	 */
	private int askForMove(Tx3Board currentBoard) throws TicTacToePlayException
	{
		int[] validMoves = currentBoard.getValidMoves();
		
		Scanner input = new Scanner(System.in);
		System.out.println(currentBoard);
		System.out.print("Move: ");
		int index = -1;
		boolean flag = false;
		
		do
		{
			String str = input.nextLine();
			try
			{
				index = Integer.parseInt(str);
			}
			catch (Exception e){ ; }
			
			for(int i : validMoves)
				if(index == i)
					flag = true;
			if(!flag)
				System.out.print("Not valid move, try again: ");
			
		}while(flag == false);
		
		return index;
	}
	
	/* (non-Javadoc)
	 * @see proj4.Player#makeIndexedMove(proj4.Tx3Board)
	 */
	@Override
	public int makeIndexedMove(Tx3Board currentBoard)throws TicTacToeBoardException, TicTacToePlayException 
	{
		return askForMove(currentBoard);
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
		RandomPlayer playerOne = new RandomPlayer(TicTacToeValues.X_VALUE );
		HumanPlayer playerTwo = new HumanPlayer(TicTacToeValues.O_VALUE );
		Scanner input = new Scanner(System.in);
		int numGames = -1;

		do
		{
			System.out.print("Number of games to play : ");
			String str = input.nextLine();
			
			try
			{
				numGames = Integer.parseInt(str);
			}
			catch (Exception e){; }
			
			if(numGames < 0)
				System.out.println("Not a valid Number.");

		}while(numGames < 0);
		
		while(numGames > 0)
		{
			Tx3Board game = new Tx3Board();
			boolean playerOneTurn = true;
			TicTacToeValues winner = TicTacToeValues.NO_VALUE;
			
			while(winner.equals(TicTacToeValues.NO_VALUE) && game.numMovesLeft() > 0)
			{				
				if(playerOneTurn)
					game = playerOne.makeMove(game);
	
				else
					game = playerTwo.makeMove(game);
	
				playerOneTurn = !playerOneTurn;
				winner = game.whoWon();
				
			}
			System.out.println(winner + " wins!");
			playerOne.gameOver(winner);
			playerTwo.gameOver(winner);
			numGames--;
		}
		System.out.println(playerOne);
		System.out.println(playerTwo);
		
	}
}
