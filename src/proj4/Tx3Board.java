/**
 * @version 5/ 10/ 12
 * @author Nathan Hartmann <nhartm1@umbc.edu>
 * CMSC 341 - Spring 2012 - Project #4
 * Section 01
 */
package proj4;

/**
 * The Class Tx3Board.
 */
public class Tx3Board {

	/** The board. */
	private TicTacToeValues[] board;
	
	/** The num moves left. */
	private int numMovesLeft;
	
	/** The board id. */
	private Integer boardID;
	
	/**
	 * Instantiates a new tx3 board.
	 */
	public Tx3Board()
	{
		numMovesLeft = 9;
		board = new TicTacToeValues[9];
		
		for(int x = 0; x < board.length; x++)
			board[x] = TicTacToeValues.NO_VALUE;
		
		setBoardId();
	}
	
	/**
	 * Instantiates a new tx3 board.
	 *
	 * @param other the other
	 */
	public Tx3Board(TicTacToeValues[] other)
	{
		numMovesLeft = 0;
		board = new TicTacToeValues[9];

		for(int x = 0; x < board.length; x++)
		{
			if(other[x] == TicTacToeValues.NO_VALUE)
				numMovesLeft++;	
			
			board[x] = other[x];
		}
		
		setBoardId();
	}
	
	/**
	 * Instantiates a new tx3 board.
	 *
	 * @param reloadedBoard the reloaded board
	 * @throws TicTacToeBoardException the tic tac toe board exception
	 */
	public Tx3Board(String reloadedBoard) throws TicTacToeBoardException
	{
		numMovesLeft = 0;
		board = new TicTacToeValues[9];
		
		for(int x = 0; x < board.length; x++)
		{
			TicTacToeValues temp = TicTacToeValues.getValueOf(reloadedBoard.charAt(x));
			
			if(temp == null) 
				throw new TicTacToeBoardException("Invalid Board");
			
			board[x] = temp;
			
			if(temp == TicTacToeValues.NO_VALUE)
				numMovesLeft++;			
		}
		
		setBoardId();
	}

	/**
	 * Instantiates a new tx3 board.
	 *
	 * @param boardId the board id
	 * @throws TicTacToeBoardException the tic tac toe board exception
	 */
	public Tx3Board(Integer boardId) throws TicTacToeBoardException
	{
		String str = String.valueOf(boardId);
		
		while(str.length() < 9)
			str = "0" + str;
		
		for(int x = 0; x < board.length; x++)
		{
			TicTacToeValues temp = TicTacToeValues.getValueOf(Integer.parseInt(str.charAt(x) + ""));
			
			if(temp == null) 
				throw new TicTacToeBoardException("Invalid Board");
			
			board[x] = temp;
			
			if(temp == TicTacToeValues.NO_VALUE)
				numMovesLeft++;			
		}
		
		setBoardId();
	}	
	
	/**
	 * Num moves left.
	 *
	 * @return the int
	 */
	public int numMovesLeft()
	{
		return this.numMovesLeft;
	}

	/**
	 * Move.
	 *
	 * @param player the player
	 * @param index the index
	 * @throws TicTacToePlayException the tic tac toe play exception
	 * @throws TicTacToeBoardException the tic tac toe board exception
	 */
	public void move(TicTacToeValues player, int index) throws TicTacToePlayException, TicTacToeBoardException
	{		
		makeMove(player, index);
		numMovesLeft--;
	}
	
	/**
	 * Gets the valid moves.
	 *
	 * @return the valid moves
	 * @throws TicTacToePlayException the tic tac toe play exception
	 */
	public int[] getValidMoves() throws TicTacToePlayException
	{
		if(numMovesLeft() == 0)
			throw new TicTacToePlayException("No moves can be made, game is over.");
		
		int[] temp = new int[numMovesLeft];
		int indexCounter = 0;
		for(int x = 0; x < board.length; x++)
		{
			if(board[x] == TicTacToeValues.NO_VALUE)
				temp[indexCounter++] = x;				
		}
		return temp;
	}

	/**
	 * Gets the board id num.
	 *
	 * @return the board id num
	 */
	public int getBoardIdNum()
	{
		
		return this.boardID;
	}
	
	/**
	 * Who won, if anyone.
	 *
	 * @return the tic tac toe values
	 */
	public TicTacToeValues whoWon()
	{
		if(!board[0].equals(TicTacToeValues.NO_VALUE))
		{
			if(board[0].equals(board[3]) && board[3].equals(board[6]) )//V check
				return board[0];
			
			if(board[0].equals(board[1]) && board[1].equals(board[2]))//H check
		  		return board[0];	
		}
		
		if(!board[4].equals(TicTacToeValues.NO_VALUE))
		{
			if(board[0].equals(board[4]) && board[4].equals(board[8]))
					return board[4];
			
			if(board[1].equals(board[4]) && board[4].equals(board[7]))
				return board[4];	
			
			if(board[2].equals(board[4]) && board[4].equals(board[6]))
				return board[4];
			
			if(board[3].equals(board[4]) && board[4].equals(board[5]))
				return board[4];
		}
		
		if(!board[8].equals(TicTacToeValues.NO_VALUE))
		{
			if(board[8].equals(board[5]) && board[5].equals(board[2]) )//V check
				return board[0];
			
			if(board[8].equals(board[7]) && board[7].equals(board[6]))//H check
		  		return board[0];	
		}	
		
		return TicTacToeValues.NO_VALUE;
	}

	/**
	 * Maps the index of choice for this board to that of another board.
	 * Both boards must be of similar configurations.
	 *
	 * @param other the other
	 * @param index the index
	 * @return the int
	 */
	public int mapIndex(Tx3Board other, int index)
	{
		if(other.numMovesLeft() != numMovesLeft())
			return -1;

		return MapTransformation.sameConfig(this.board, other.board).mapTransfer(index);		
	}	
	
	/**
	 * Sets the board id.
	 * Rotates the board until the lowest ID number is found.
	 */
	private void setBoardId()
	{
		Integer value;
		boardID = null;
		
		for(MapTransformation trans : MapTransformation.transformations)
		{
			value = trans.transformID(board);

			//try's to find the smallest valued boardID for all of the equivalent configurations
			if(boardID == null || value < this.boardID)
				this.boardID = value; 
		}
	}

	/**
	 * Make move.
	 *
	 * @param player the player
	 * @param index the index
	 * @throws TicTacToeBoardException the tic tac toe board exception
	 */
	private void makeMove(TicTacToeValues player, int index) throws TicTacToeBoardException
	{				
		if(index < 0 || index > board.length || board[index] != TicTacToeValues.NO_VALUE)
			throw new TicTacToeBoardException("Invalid Move " + index + " <---> " + player + "\n\n" + this);
		
		board[index] = player;
		setBoardId();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Tx3Board clone()
	{
		return new Tx3Board(this.board);
	}
	
	/**
	 * Equals, compares the IDs of two boards.
	 *
	 * @param other the other
	 * @return true, if successful
	 */
	public boolean equals(Tx3Board other)
	{
		return boardID.equals(other.boardID);
	}

	/**
	 * One line board.
	 *
	 * @return the string
	 */
	public String oneLineBoard()
	{
		String str = "";
		
		for(int x = 0; x < board.length; x++)
			str += board[x].getValue();
		
		return str;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return this.getBoardIdNum();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		String str = "";
		for(int x = 0; x < board.length; x++)
		{
			str += board[x].getValue();
			
			if((x + 1) % 3 == 0 && x + 1 != board.length)
				str += '\n';
		}
		return str;
	}
	
	/**
	 * The Enum MapTransformation.
	 */
	public enum MapTransformation
	{
		
		/** The NORMAL. */
		NORMAL( new int[]{ 0, 1, 2, 3, 4, 5, 6, 7, 8}, "Normal" ),
		
		/** The CLOCKWIS ex1. */
		CLOCKWISEx1( new int[]{ 6, 3, 0, 7, 4, 1, 8, 5, 2 }, "Clockwise Rotation x1"  ),
		
		/** The CLOCKWIS ex2. */
		CLOCKWISEx2( new int[]{8, 7, 6, 5, 4, 3, 2, 1, 0}, "Clockwise Rotation x2"  ),
		
		/** The CLOCKWIS ex3. */
		CLOCKWISEx3( new int[]{ 2, 5, 8, 1, 4, 7, 0, 3, 6 }, "Clockwise Rotation x3"  ),
		
		/** The ABOU t_ x_ axis. */
		ABOUT_X_AXIS( new int[]{ 6, 7, 8, 3, 4, 5, 0, 1, 2 }, "Rotate about X-axis" ), 
		
		/** The ABOU t_ x_ axi s_ clockwis ex1. */
		ABOUT_X_AXIS_CLOCKWISEx1( new int[]{ 0, 3, 6, 1, 4, 7, 2, 5, 8 }, "Rotate about X-axis + Clockwise Rotation x1"  ),
		
		/** The ABOU t_ y_ axis. */
		ABOUT_Y_AXIS( new int[]{ 2, 1, 0, 5, 4, 3, 8, 7, 6 }, "Rotate about Y-axis"  ),
		
		/** The ABOU t_ y_ axi s_ clockwis ex1. */
		ABOUT_Y_AXIS_CLOCKWISEx1( new int[]{ 8, 5, 2, 7, 4, 1, 6, 3, 0 }, "Rotate about Y-axis + Clockwise Rotation x1"  );
		
		/** The Constant transformations. */
		private static final MapTransformation[] transformations = { NORMAL, CLOCKWISEx1, CLOCKWISEx2, CLOCKWISEx3,
			ABOUT_X_AXIS, ABOUT_X_AXIS_CLOCKWISEx1,	ABOUT_Y_AXIS, ABOUT_Y_AXIS_CLOCKWISEx1 };

		/** The new index of value. */
		private int[] newIndexOfValue;
		
		/** The transfermation description. */
		private String transfermationDescription;
		
		/**
		 * Instantiates a new map transformation.
		 *
		 * @param index the index
		 * @param description the description
		 */
		private MapTransformation(int[] index, String description)
		{
			newIndexOfValue = new int[index.length];
			transfermationDescription = description;
			
			for(int i = 0; i < index.length; i++)
				newIndexOfValue[i] = index[i];
		}
		
		/**
		 * Maps the transfer of one index to another.
		 *
		 * @param index the index
		 * @return the int
		 */
		public int mapTransfer(int index)
		{
			if(index >= newIndexOfValue.length || index < 0)
				return -1;

			return newIndexOfValue[index];
		}
		
		/**
		 * Transform id.
		 *
		 * @param board the board
		 * @return the string
		 */
		public int transformID(TicTacToeValues[] board)
		{
			String str = "";
			
			for(int i = 0; i < board.length; i++)
				str += String.valueOf(board[ newIndexOfValue[i] ].getNumValue());	

			return Integer.parseInt(str);		
		}	
		
		/**
		 * Same config.
		 *
		 * @param one the one
		 * @param two the two
		 * @return the map transformation
		 */
		public static MapTransformation sameConfig(TicTacToeValues[] one, TicTacToeValues[] two)
		{
			boolean isConfig;
			
			for(MapTransformation trans : MapTransformation.transformations)
			{
				isConfig = true;
				
				for(int i = 0; i < one.length; i++)
				{
					if(one[i] != two[trans.mapTransfer(i)] )
						isConfig = false;
				}
				
				if(isConfig == true)
					return trans;
			}			
			
			return null;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		public String toString()
		{
			String str = transfermationDescription + ": [ ";
			
			for(int i : newIndexOfValue)
			{
				str += i + ", ";
			}
			str += "]";
			
			return str;			
		}
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws TicTacToeBoardException the tic tac toe board exception
	 * @throws TicTacToePlayException the tic tac toe play exception
	 */
	public static void main(String args[]) throws TicTacToeBoardException, TicTacToePlayException
	{
		Tx3Board test = new Tx3Board();
		System.out.println(test);
		
		test.move(TicTacToeValues.X_VALUE, 8);
		
		System.out.println(test);
		
		test.move(TicTacToeValues.O_VALUE, 5);
		test.move(TicTacToeValues.X_VALUE, 0);
		
		System.out.println(test + "\n\n_____NEXT TEST_____\n\n"); 
		
		Tx3Board testTwo = new Tx3Board();		
		testTwo.move(TicTacToeValues.X_VALUE, 0);		
		System.out.println(testTwo);
		
		Tx3Board testThree = new Tx3Board();	
		testThree.move(TicTacToeValues.X_VALUE, 8);
		System.out.println(testThree);
		
		System.out.println(testTwo.equals(testThree));
		System.out.println(testTwo.boardID);
		
		System.out.println(test + "\n\n_____NEXT TEST_____\n\n");
		
		System.out.println(testTwo);
		for(int i : testTwo.getValidMoves())
			System.out.print(i + " ");

		System.out.println(test + "\n\n_____NEXT TEST_____\n\n");

		test = new Tx3Board("X.OXOXOXO");
		System.out.println(test + "\n");

		testTwo = new Tx3Board("OXOXOXO.X");
		System.out.println(testTwo);

		System.out.println(test.equals(testTwo));
		System.out.println(test.mapIndex(testTwo, 1));
	
		System.out.println(test + "\n\n_____NEXT TEST_____\n\n");

		test = new Tx3Board("XOX.XOOXX");
		System.out.println(test + "\n");
		System.out.println(test.board[0]);
		System.out.println(test.board[4]);
		System.out.println(test.board[8]);
		System.out.println(test.whoWon());

		System.out.println(test + "\n\n_____NEXT TEST_____\n\n");

		test = new Tx3Board("XXOO.X..O");
		
		for(MapTransformation trans : MapTransformation.transformations)
			System.out.println(trans);
	}
}
