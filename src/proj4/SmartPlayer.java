/**
 * @version 5/ 10/ 12
 * @author Nathan Hartmann <nhartm1@umbc.edu>
 * CMSC 341 - Spring 2012 - Project #4
 * Section 01
 */
package proj4;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * The Class SmartPlayer.
 */
public class SmartPlayer extends RandomPlayer {

	/** The game states ever reached. */
	private QuadraticProbingHashTable<HashNode> gameStatesEverReached; 
	
	/** The current game path. */
	private ArrayList<HashNode> currentGamePath;
	
	/** The save. */
	private boolean save;
	
	/** The file name. */
	private final String fileName = "configs.txt";
	
	
	/**
	 * Instantiates a new smart player.
	 *
	 * @param mark the mark
	 * @param save the save
	 * @throws TicTacToePlayException the tic tac toe play exception
	 */
	public SmartPlayer(TicTacToeValues mark, boolean save) throws TicTacToePlayException 
	{
		super(mark);
		currentGamePath = new ArrayList<HashNode>();
		this.save = save;		
		
		if(save)
		{
			try 
			{
				loadFile();
			} 
			catch (Exception e)
			{
				gameStatesEverReached = new QuadraticProbingHashTable<HashNode>();
			}
		}
		
		else
			gameStatesEverReached = new QuadraticProbingHashTable<HashNode>();
	}

	/**
	 * Instantiates a new smart player.
	 *
	 * @param mark the mark
	 * @throws TicTacToePlayException the tic tac toe play exception
	 */
	public SmartPlayer(TicTacToeValues mark) throws TicTacToePlayException 
	{
		this(mark, false);
	}
	
	/* (non-Javadoc)
	 * @see proj4.RandomPlayer#makeMove(proj4.Tx3Board)
	 */
	public Tx3Board makeMove(Tx3Board currentBoard) throws TicTacToeBoardException, TicTacToePlayException
	{
		if(currentBoard == null)
			throw new TicTacToeBoardException("Invalid Board-ish");
	
		int[] validMoves = currentBoard.getValidMoves();
		ArrayList<HashDictionary> kids = new ArrayList<HashDictionary>();
		
		for(int i : validMoves)
		{
			kids.add(new HashDictionary(currentBoard, i));
		}
		
		int move = getMove(kids); 		
		currentGamePath.add(kids.get(move).hashedNode);

		return kids.get(move).board.state.clone();
	}

	/**
	 * Make indexed move.
	 *
	 * @param currentBoard the current board
	 * @return the int
	 * @throws TicTacToeBoardException the tic tac toe board exception
	 * @throws TicTacToePlayException the tic tac toe play exception
	 */
	public int makeIndexedMove(Tx3Board currentBoard) throws TicTacToeBoardException, TicTacToePlayException
	{
		if(currentBoard == null)
			throw new TicTacToeBoardException("Invalid Board-ish");
	
		int[] validMoves = currentBoard.getValidMoves();
		ArrayList<HashDictionary> kids = new ArrayList<HashDictionary>();
		
		for(int i : validMoves)
		{
			kids.add(new HashDictionary(currentBoard, i));
		}
		
		int move = getMove(kids); 		
		currentGamePath.add(kids.get(move).hashedNode);

		return validMoves[move];
	}	
	
	
	/**
	 * Gets the next best move, even if the next best move is a random move.
	 *
	 * @param hashedKids the hashed kids
	 * @return the move
	 * @throws TicTacToePlayException the tic tac toe play exception
	 */
	private int getMove(ArrayList<HashDictionary> hashedKids) throws TicTacToePlayException
	{
		double lossThreshold = 0.25;
		double winThreshold = 0.5;
		
		HashDictionary move = null;
		
		for(int i = 0; i < hashedKids.size(); i++)
		{
			if(hashedKids.get(i).numPlays() == 0 )
				move = hashedKids.get(i);
			
		}
		
		if(move == null)
		{
			for(int i = 0; i < hashedKids.size(); i++)
			{
				if(hashedKids.get(i).percentOfWins() > winThreshold )
				{
					move = hashedKids.get(i);
					winThreshold = hashedKids.get(i).percentOfWins();
				}
				
				else if(move == null && hashedKids.get(i).percentOfLosses() < lossThreshold)
				{
					move = hashedKids.get(i);
					lossThreshold = hashedKids.get(i).percentOfLosses();
				}
				
			}
		}		
	
		if(move == null)
		{
			generator = new Random();
			move = hashedKids.get(generator.nextInt(hashedKids.size()));
		}
			
		return hashedKids.indexOf(move);
	}
	
	/**
	 * Gets the favorite move.
	 * Finds the highest winning move given a particular state.
	 *
	 * @param initailMove the initail move
	 * @return the favorite move
	 * @throws TicTacToeBoardException the tic tac toe board exception
	 * @throws TicTacToePlayException the tic tac toe play exception
	 */
	public HashDictionary getFavoriteMove(Tx3Board initailMove) throws TicTacToeBoardException, TicTacToePlayException
	{
		if(initailMove == null)
			throw new TicTacToeBoardException("Invalid Board-ish");
	
		int[] validMoves = initailMove.getValidMoves();
		ArrayList<HashDictionary> kids = new ArrayList<HashDictionary>();
		
		for(int i : validMoves)
		{
			kids.add(new HashDictionary(initailMove, i));
		}	
		
		HashDictionary temp = null;
		
		for(HashDictionary h : kids)
			if(temp == null || h.percentOfWins() > temp.percentOfWins())
				temp = h;
		
		return temp;		
	}
	
	/* (non-Javadoc)
	 * @see proj4.RandomPlayer#gameOver(boolean)
	 */
	public void gameOver(TicTacToeValues value)
	{
		super.gameOver(value);
		
		while(!currentGamePath.isEmpty())
		{
			HashNode tmp = currentGamePath.remove(currentGamePath.size() - 1);
			tmp.didIwin(value);
		}
		
		currentGamePath.clear();
	}
	
	/* (non-Javadoc)
	 * @see proj4.RandomPlayer#getFinalReport()
	 */
	public String getFinalReport()
	{
		String str = "";
		str += "\nSmart player has won " + getNumWins() + " times which is " +String.format("%.2f percent", (getWinningPercentile()));
		str += "\nThe number of slots is: " + gameStatesEverReached.capacity();
		str += "\nThe number of entries is: " + gameStatesEverReached.currentSize();
		str += String.format("\nThe percent full is: %.2f", (((double) gameStatesEverReached.currentSize()  / gameStatesEverReached.capacity() ))  * 100);
		str += "\nThe number of collisions is: " + gameStatesEverReached.collisions();
		
		try {
			HashDictionary temp = getFavoriteMove(new Tx3Board("....X...."));
			str += "\n\nFavorite move is:\n" + temp.hashedNode.state + "\nwhich won " + String.format("%.2f", (temp.percentOfWins() * 100)) + "% of the " + temp.numPlays() + " times is was played.";

		} catch (Exception e){ ;}
		
		
		if(save)
			fileDumpHashTable();
		
		return str;
	}	
	
	/**
	 * File dump hash table.
	 */
	private void fileDumpHashTable() {
		
		BufferedWriter writer = null;
		
		try 
		{
			writer = new BufferedWriter(new FileWriter(fileName));
			writer.write(gameStatesEverReached.exportToFile());
			writer.newLine();
			writer.close();
		} 
		catch (IOException e) {
			System.out.println("Unable to dump hash table to file.");
		}	
		
	}

	/**
	 * Load file, imports file into hash table.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void loadFile() throws IOException
	{
		BufferedReader read = null;
	
		read = new BufferedReader(new FileReader(fileName));
		
		String line = read.readLine();
		String[] chars = line.split(" ");
		
		int length = Integer.parseInt(chars[0]);
		int collisions = Integer.parseInt(chars[1]);

		HashNode[] array = new HashNode[length];
		boolean[] bool = new boolean[length];

		for(int i = 0; i < length - 1; i++)
		{
			line = read.readLine();
			
			if(line != null &&!line.isEmpty() && !line.equalsIgnoreCase(" "))
			{
				String[] str = line.split(" ");
				
				try {
					array[i] = new HashNode(str[1], str[2], str[3], str[4]);
				} catch (TicTacToeBoardException e) {
					System.out.println("File was tampered with : " + e.getMessage());
				}
				
				if(str[0].equalsIgnoreCase("true"))
					bool[i] = true;
				
				else
					bool[i] = false;					
			}
			
			else
				array[i] = null;
			
		}

		gameStatesEverReached = new QuadraticProbingHashTable<HashNode>( length, collisions, array, bool);
		read.close();
	}
	
	/**
	 * The Class HashNode.
	 * HashNodes are stored in the hash table.
	 * They contain all of the stats of a particular move.
	 * All moves are reflected / rotated to take advantage of symmetry.
	 */
	private class HashNode
	{
		/** The state. */
		private Tx3Board state;
		
		/** The num wins. */
		private int numLosses;
		
		/** The num wins. */
		private int numWins;
		
		/** The num times played. */
		private int numTimesPlayed;

		/**
		 * Instantiates a new hash node.
		 *
		 * @param currentBoard the current board
		 * @param index the index
		 * @throws TicTacToePlayException the tic tac toe play exception
		 * @throws TicTacToeBoardException the tic tac toe board exception
		 */
		private HashNode(Tx3Board currentBoard, int index) throws TicTacToePlayException, TicTacToeBoardException
		{
			this.state = currentBoard.clone();
			state.move(getPlayer(), index);
			numTimesPlayed = 0;
			numWins = 0;
			numLosses = 0;
		}


		/**
		 * Instantiates a new hash node.
		 *
		 * @param board the board
		 * @param numPlays the num plays
		 * @param numWins the num wins
		 * @param numLosses the num losses
		 * @throws TicTacToeBoardException the tic tac toe board exception
		 */
		private HashNode(String board, String numPlays, String numWins, String numLosses) throws TicTacToeBoardException 
		{
			this.state = new Tx3Board(board);
			this.numTimesPlayed = Integer.parseInt(numPlays);
			this.numWins = Integer.parseInt(numWins);
			this.numLosses = Integer.parseInt(numLosses);
		}


		/**
		 * Did I win?
		 *
		 * @param value the value
		 */
		private void didIwin(TicTacToeValues value)
		{
			numTimesPlayed++;

			if(getPlayer() == value)
				this.numWins++;
			
			else if(value != TicTacToeValues.NO_VALUE)
				this.numLosses++;
		}
		
		/**
		 * Percent of losses.
		 *
		 * @return the double
		 */
		private double percentOfLosses()
		{
			if(numTimesPlayed == 0)
				return 1.0;
			
			return ((double) numLosses) / ((double) numTimesPlayed);
		}
		
		/**
		 * Percent of wins.
		 *
		 * @return the double
		 */
		private double percentOfWins()
		{
			if(numTimesPlayed == 0)
				return 0.0;
			
			return ((double) numWins) / ((double) numTimesPlayed);
		}
		
		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode()
		{
			return state.hashCode();
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString()
		{
			return state.oneLineBoard() + " " + numTimesPlayed + " " + numWins + " " + numLosses;
		}
	}
	
	/**
	 * The Class HashDictionary.
	 * Pairs the current board's children with the corresponding hash nodes in the table.
	 * If the child is not found in the hash table, then it is added.
	 * 
	 */
	private class HashDictionary
	{
		
		/** The hashed node. */
		HashNode hashedNode;
		
		/** The board. */
		HashNode board;
		
		/**
		 * Instantiates a new hash dictionary.
		 *
		 * @param board the board
		 * @param index the index
		 * @throws TicTacToePlayException the tic tac toe play exception
		 * @throws TicTacToeBoardException the tic tac toe board exception
		 */
		private HashDictionary( Tx3Board board, int index ) throws TicTacToePlayException, TicTacToeBoardException
		{
			this.board = new HashNode(board, index);
			
			if(!gameStatesEverReached.contains(this.board))
				gameStatesEverReached.insert(this.board);
			
			hashedNode = gameStatesEverReached.get(this.board);	
		}
		
		/**
		 * Percent of win.
		 *
		 * @return the double
		 */
		private double percentOfLosses()
		{
			return hashedNode.percentOfLosses();
		}
		
		/**
		 * Percent of wins.
		 *
		 * @return the double
		 */
		private double percentOfWins()
		{
			return hashedNode.percentOfWins();
		}
		
		/**
		 * Num plays.
		 *
		 * @return the int
		 */
		private int numPlays()
		{
			return hashedNode.numTimesPlayed;
		}
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
		RandomPlayer playerOne = new RandomPlayer( TicTacToeValues.X_VALUE );
		SmartPlayer playerTwo = new SmartPlayer( TicTacToeValues.O_VALUE, true );
		Tx3Board game;
		boolean playerOneTurn;
		TicTacToeValues winner;
		
		BufferedWriter writer = null;
		
		try {
			writer = new BufferedWriter(new FileWriter("src/proj4/Smart_Hash.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		int numPlays = 1000;
		
		while(numPlays > 0)
		{	
			game = new Tx3Board();
			playerOneTurn = true;
			winner = TicTacToeValues.NO_VALUE;
			
			while(winner.equals(TicTacToeValues.NO_VALUE) && game.numMovesLeft() > 0)
			{				
				if(playerOneTurn)
					game = playerOne.makeMove(game);

				else
					game = playerTwo.makeMove(game);

				playerOneTurn = !playerOneTurn;
				winner = game.whoWon();
				
			/*	try {
					writer.write(game.toString());
					writer.newLine();
					writer.newLine();
				} catch (IOException e) {	e.printStackTrace();  } */
			}
			
			playerOne.gameOver(winner);
			playerTwo.gameOver(winner);
			
			numPlays--;
			
		/*	try {
				writer.write(winner.toString() + " <-- WON | GameNum: " + numPlays);
				writer.newLine();
				writer.write("\n\n----------" );
				writer.newLine();
			} catch (IOException e) {	e.printStackTrace();  } */
			
		}
		
		System.out.println(playerOne);
		System.out.println(playerTwo);
		
		//System.out.println(playerTwo.gameStatesEverReached);
		System.out.println(playerTwo.gameStatesEverReached.capacity());
		System.out.println(playerTwo.gameStatesEverReached.currentSize());
		
	 /*  try {
			writer.write(playerOne.toString());
			writer.newLine();
			writer.write(playerTwo.toString());
			writer.newLine();
		} catch (IOException e) {	e.printStackTrace();  } */
		
		try {
			writer.write(playerTwo.gameStatesEverReached.exportToFile());
			writer.close();
		} catch (IOException e) {	e.printStackTrace();  }		
		
		System.out.println("------");
		System.out.println(playerTwo.getFinalReport());
	}
	
}