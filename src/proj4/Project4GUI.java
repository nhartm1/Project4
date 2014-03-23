/**
 * @version 5/ 13/ 12
 * @author Nathan Hartmann <nhartm1@umbc.edu>
 * CMSC 341 - Spring 2012 - Project #4
 * Section 01
 */
package proj4;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Project4GUI
{
	// Initialize all swing objects.
    /** The f. */
	private JFrame f = new JFrame("Tic Tac Toe GUI"); //create Frame
    
    /** The num games. */
    private JLabel numGames;
    
    /** The num wins. */
    private JLabel numWins;
    
    /** The num losses. */
    private JLabel numLosses;
    
    /** The num draws. */
    private JLabel numDraws;
    
    /** The board item. */
    private JLabel[] boardItem;
    
    /** The game. */
	private JPanel game;

	/** The game panel. */
	private JPanel gamePanel;

	/** The Constant LABEL_COLOR. */
	private static final Color LABEL_COLOR = Color.BLACK;

	/** The Constant MED_LABEL_FONT. */
	private static final Font MED_LABEL_FONT = new Font("Sans Serif", Font.BOLD, 14);

	// Menu
    /** The mb. */
	private JMenuBar mb = new JMenuBar(); // Menubar
    
    /** The mnu file. */
    private JMenu mnuFile = new JMenu("File"); // File Entry on Menu bar
    
    /** The mnu item quit. */
    private JMenuItem mnuItemQuit = new JMenuItem("Quit"); // Quit sub item
 
    /** The other player. */
	private TicTacToeValues otherPlayer = TicTacToeValues.O_VALUE;

	/** The player. */
	private TicTacToeValues player = TicTacToeValues.X_VALUE;

	/** The ran. */
	private SmartPlayer ran = new SmartPlayer(otherPlayer, true);

	/** The gamettt. */
	private Tx3Board gamettt = null;

	/** The num games won. */
	private int numGamesWon = 0;

	/** The num plays. */
	private int numPlays = 0;

	/**
     * Constructor for the GUI.
     *
     * @throws TicTacToePlayException the tic tac toe play exception
     */
    public Project4GUI() throws TicTacToePlayException{
		
		gamettt = new Tx3Board();
		
    	// Set menubar
        f.setJMenuBar(mb);
        
		//Build Menus
        mnuFile.add(mnuItemQuit);  // Create Quit line
        mb.add(mnuFile);        // Add Menu items to form
        
        numGames = new JLabel();
        numWins = new JLabel();
        numLosses = new JLabel();
        numDraws = new JLabel();
        
        gamePanel = new JPanel();
        boardItem = new JLabel[9];
        game = new JPanel();

        resetBoard();

		// Allows the Swing App to be closed
        f.addWindowListener(new ListenCloseWdw());
		
		//Add Menu listener
        mnuItemQuit.addActionListener(new ListenMenuQuit());
    }
	
    /** The index. */
    private static int index = 0;
    
    /**
     * Reset board.
     */
    private void resetBoard()
    {
        // Add Buttons
    	
    	gamettt = new Tx3Board();		
    	
        numGames.setText("Game Plays: " + numPlays + "  ");
        numGames.setFont(MED_LABEL_FONT);
        numGames.setForeground(LABEL_COLOR);
        
        numWins.setText("Wins: " + numGamesWon+ "  ");
        numWins.setFont(MED_LABEL_FONT);
        numWins.setForeground(LABEL_COLOR);
        
        numLosses.setText("Losses: " + ran.getNumWins()+ "  ");
        numLosses.setFont(MED_LABEL_FONT);
        numLosses.setForeground(LABEL_COLOR);      
        
        numDraws.setText("Draws: " + (numPlays - (ran.getNumWins() + numGamesWon))+ "  ");
        numDraws.setFont(MED_LABEL_FONT);
        numDraws.setForeground(LABEL_COLOR);

        //panels

        gamePanel.add(numGames);
        gamePanel.add(numWins);
        gamePanel.add(numLosses);
        gamePanel.add(numDraws);
        gamePanel.setOpaque(false);

        boardItem[0] = tictactoe();
        boardItem[1] = tictactoe();
        boardItem[2] = tictactoe();
        boardItem[3] = tictactoe();
        boardItem[4] = tictactoe();
        boardItem[5] = tictactoe();
        boardItem[6] = tictactoe();
        boardItem[7] = tictactoe();
        boardItem[8] = tictactoe();  
        
        game.removeAll();
        game.setLayout(new GridLayout(3, 3));
        for(JLabel p : boardItem)
        	game.add(p);
        
        game.setVisible(true);
        game.setOpaque(true);
        game.setMaximumSize(new Dimension(325, 325));
        game.setAlignmentX(f.getWidth());
        
        f.getContentPane().removeAll();
        f.getContentPane().add(gamePanel, BorderLayout.BEFORE_FIRST_LINE); 
        f.getContentPane().add(game, BorderLayout.AFTER_LAST_LINE);

    }
    
    
    /**
     * Tictactoe.
     *
     * @return the j label
     */
    private JLabel tictactoe()
    {    	
    	BufferedImage img = null;
    	try {
    	    img = ImageIO.read(getClass().getResource("images/blank.jpg"));
    	} catch (IOException e) {
    	}
    	
    	JLabel ttt = new JLabel();
        ttt.setIcon(new ImageIcon(img));
        ttt.setOpaque(true);
        ttt.setBackground(Color.WHITE);
        ttt.setMaximumSize(new Dimension(100, 100));
        ttt.setBorder(LineBorder.createBlackLineBorder());
        ttt.setVisible(true);
        
        ttt.addMouseListener(new MouseListener(){
        	private int inde = index++;
        	
			public void mouseReleased(MouseEvent e) {
				try {
					gamettt.move(player, inde);
					boardItem[inde].setIcon(new ImageIcon(player.getImage()));
					
					if(gamettt.whoWon() != player && gamettt.numMovesLeft() > 0)
					{
						int i = ran.makeIndexedMove(gamettt);
						gamettt.move(ran.getPlayer(), i);
						boardItem[i].setIcon(new ImageIcon(ran.getPlayer().getImage()));
						
						if(gamettt.whoWon() == ran.getPlayer() || gamettt.numMovesLeft() == 0)
						{
							ran.gameOver(gamettt.whoWon());
							numPlays++;
							index = 0;
							resetBoard();
						}	
					}
					
					else
					{
						ran.gameOver(gamettt.whoWon());	
						numPlays++;
						index = 0;
						resetBoard();
					}
					
				} catch (Exception r){ ;}
			}
			public void mouseExited(MouseEvent e) {
				
			}
			public void mouseEntered(MouseEvent e) {
				
			}
			public void mousePressed(MouseEvent e) { }
			public void mouseClicked(MouseEvent e) { }
		});
        
        
        return ttt;
    }    
    
    /**
     * The Class ListenMenuQuit.
     */
    public class ListenMenuQuit implements ActionListener{
        
        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e){
            System.out.println(ran.getFinalReport());
        	System.exit(0);         
        }
    }
	
    /**
     * The Class ListenCloseWdw.
     */
    public class ListenCloseWdw extends WindowAdapter{
        
        /* (non-Javadoc)
         * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
         */
        public void windowClosing(WindowEvent e){
            System.out.println(ran.getFinalReport());
        	System.exit(0);         
        }
    }
	
    /**
     * Launch frame.
     */
    public void launchFrame(){
        // Display Frame
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack(); //Adjusts panel to components for display
        f.setVisible(true);
    }
    
    /**
     * The main method.
     *
     * @param args the arguments
     * @throws TicTacToePlayException the tic tac toe play exception
     */
    public static void main(String args[]) throws TicTacToePlayException{
    	Project4GUI gui = new Project4GUI();   	
        gui.launchFrame();
    }
}
