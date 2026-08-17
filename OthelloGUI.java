/** 
 *	File Name: OthelloGUI.java
 *	Name: Ms. I. Lam
 *	Course: ICS3U1
 *	Date: December 6, 2025
 * Description: This class creates the graphical user interface (GUI) for the
 * Othello game.  It contains methods to be called to update the graphical 
 * game board.
 * NOTE: 
 * All graphics files should be in a folder called "images"
 * logo.jpg, player1.jpg, player2.jpg
 */
import javax.swing.*;
import javax.swing.JComponent;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class OthelloGUI {
   private JLabel[][] slots;
   private JFrame mainFrame;
   private JTextField[] playerScore;
   private ImageIcon[] playerIcon;
   private JLabel nextPlayerIcon;
   private JButton saveGameButton;
   private JButton loadGameButton;
   private JButton restartGameButton;
   private JButton exitButton;

   private Color background = new Color(100, 100, 100);

   private String logoIcon;
   private String[] iconFile;

/**
* Number of players
*/
   public final int NUMPLAYER = 2;

/**
* Number of rows on the game board
*/
   public final int NUMROW = 8;

/**
* Number of colums on the game board
*/
   public final int NUMCOL = 8;

/**
* Name of the graphics file for the logo at top of game board
*/
   private final String LOGOICON = "images/logo.jpg";
   
/**
* Name of the graphics file for player 1 piece
*/
   private final String PLAYER1ICON = "images/player1.jpg";
   
/**
* Name of the graphics file for player 2 piece
*/
   private final String PLAYER2ICON = "images/player2.jpg";

/**
* Constants defining the demensions of the different components
* on the GUI
*/
   private final int PIECESIZE = 70;
   private final int PLAYPANEWIDTH = NUMCOL * PIECESIZE;
   private final int PLAYPANEHEIGHT = NUMROW * PIECESIZE;

   private final int INFOPANEWIDTH = 2 * PIECESIZE;
   private final int INFOPANEHEIGHT = PLAYPANEHEIGHT;

   private final int LOGOHEIGHT = 2 * PIECESIZE;
   private final int LOGOWIDTH = PLAYPANEWIDTH + INFOPANEWIDTH;

   private final int FRAMEWIDTH = (int)(LOGOWIDTH * 1.03);
   private final int FRAMEHEIGHT = (int)((LOGOHEIGHT + PLAYPANEHEIGHT) * 1.1);

// Constructor:  OthelloGUI
// - intialize variables from config files
// - initialize the imageIcon array
// - initialize the slots array
// - create the main frame
   public OthelloGUI () {
      initConfig();
      initImageIcon();
      initSlots();
      createMainFrame();
   }

   private void initConfig() {
      logoIcon = LOGOICON;
      iconFile = new String[NUMPLAYER];
      iconFile[0] = PLAYER1ICON;
      iconFile[1] = PLAYER2ICON;          
   }

// initImageIcon
// Initialize playerIcon arrays with graphic files
   private void initImageIcon() {
      playerIcon = new ImageIcon[NUMPLAYER];
      for (int i = 0; i < NUMPLAYER; i++) {
         playerIcon[i] = new ImageIcon(iconFile[i]);
      }
   }

// initSlots
// initialize the array of JLabels
   private void initSlots() {
      slots = new JLabel[NUMROW][NUMCOL];
      for (int i = 0; i < NUMROW; i++) {
         for (int j = 0; j < NUMCOL; j++) {
            slots [i] [j] = new JLabel ();
            slots[i][j].setPreferredSize(new Dimension(PIECESIZE, PIECESIZE));
            slots [i] [j].setHorizontalAlignment (SwingConstants.CENTER);
            slots [i] [j].setBorder (new LineBorder (Color.white));        
         }
      }
   }

// createPlayPanel
   private JPanel createPlayPanel() {
      JPanel panel = new JPanel(); 
      panel.setPreferredSize(new Dimension(PLAYPANEWIDTH, PLAYPANEHEIGHT));
      panel.setBackground(background);
      panel.setLayout(new GridLayout(NUMROW, NUMCOL));
      for (int i = 0; i < NUMROW; i++) {
         for (int j = 0; j < NUMCOL; j++) {
            panel.add(slots[i][j]);
         }
      }
      return panel;    
   }

// createInfoPanel
   private JPanel createInfoPanel() {
      JPanel panel = new JPanel();
      panel.setPreferredSize(new Dimension(INFOPANEWIDTH, INFOPANEHEIGHT));
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
      panel.setBackground (background);
   
      Font headingFont = new Font ("Arial", Font.BOLD, 18);
      Font regularFont = new Font ("Arial", Font.BOLD, 16);
   
   // Create a panel for the scoreboard
      JPanel scorePanel = new JPanel();
      scorePanel.setBackground(background);
   
   // Create the label to display "SCOREBOARD" heading
      JLabel scoreLabel = new JLabel ("SCOREBOARD", JLabel.CENTER);
      scoreLabel.setFont(headingFont);
      scoreLabel.setAlignmentX (Component.CENTER_ALIGNMENT);
   
   // Create JLabels for players
      JLabel[] playerLabel = new JLabel[NUMPLAYER];
      for (int i = 0; i < NUMPLAYER; i++) {
         playerLabel[i] = new JLabel(playerIcon[i]);
      }
   
   // Create the array of textfield for players' score
      playerScore = new JTextField[NUMPLAYER];
      for (int i = 0; i < NUMPLAYER; i++) {
         playerScore[i] = new JTextField();
         playerScore[i].setFont(regularFont);
         playerScore[i].setText("0");
         playerScore[i].setEditable(false);
         playerScore[i].setHorizontalAlignment (JTextField.CENTER);
         playerScore[i].setPreferredSize (new Dimension (INFOPANEWIDTH - PIECESIZE - 10, 30));
         playerScore[i].setBackground(background);
      }
   
      scorePanel.add(scoreLabel);
      for (int i = 0; i < NUMPLAYER; i++) {
         scorePanel.add(playerLabel[i]);
         scorePanel.add(playerScore[i]);
      }
     
      JPanel nextPanel = new JPanel();
      nextPanel.setBackground(background);
   
   // Create the label to display "NEXT TURN" heading
      JLabel nextLabel = new JLabel ("NEXT TURN", JLabel.CENTER);
      nextLabel.setFont(headingFont);
      nextLabel.setAlignmentX (Component.CENTER_ALIGNMENT);
   
      // Create the JLabel for the nextPlayer
      nextPlayerIcon = new JLabel();
      System.out.println(nextPlayerIcon.getAlignmentX());
      nextPlayerIcon.setAlignmentX(JLabel.CENTER_ALIGNMENT);
      nextPlayerIcon.setIcon(playerIcon[0]);
   
      nextPanel.add(nextLabel);
      nextPanel.add(nextPlayerIcon);
   
      panel.add(scorePanel);
      panel.add(nextPanel);
   
   // panel for the buttons
      JPanel buttonPanel = new JPanel();
      buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
      buttonPanel.setBackground(background);
      
   // button for save game
      saveGameButton = new JButton("Save Game");
      saveGameButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
      saveGameButton.setFont(regularFont);
                              
   // button for load game
      loadGameButton = new JButton("Load Game");
      loadGameButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
      loadGameButton.setFont(regularFont);
     
   // button for restart game
      restartGameButton = new JButton("Restart Game");
      restartGameButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
      restartGameButton.setFont(regularFont);
   
   // button for exit
      exitButton = new JButton("Exit");
      exitButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
      exitButton.setFont(regularFont);        
   
      buttonPanel.add(saveGameButton);
      buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
      buttonPanel.add(loadGameButton);
      buttonPanel.add(Box.createRigidArea(new Dimension(0, 50)));
      buttonPanel.add(restartGameButton);
      buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
      buttonPanel.add(exitButton);
      buttonPanel.add(Box.createRigidArea(new Dimension(0, 25)));
      panel.add(buttonPanel);      
      
      return panel;
   }

// createMainFrame
   private void createMainFrame() {
   
   // Create the main Frame
      mainFrame = new JFrame ("Othello");
      JPanel panel = (JPanel)mainFrame.getContentPane();
      panel.setLayout (new BoxLayout(panel,BoxLayout.Y_AXIS));
   
   // Create the panel for the logo
      JPanel logoPane = new JPanel();
      logoPane.setPreferredSize(new Dimension (LOGOWIDTH, LOGOHEIGHT));
      JLabel logo = new JLabel();
      logo.setIcon(new ImageIcon(logoIcon));
      logoPane.add(logo);
   
   // Create the bottom Panel which contains the play panel and info Panel
      JPanel bottomPane = new JPanel();
      bottomPane.setLayout(new BoxLayout(bottomPane,BoxLayout.X_AXIS));
      bottomPane.setPreferredSize(new Dimension(PLAYPANEWIDTH + INFOPANEWIDTH, PLAYPANEHEIGHT));
      bottomPane.add(createPlayPanel());
      bottomPane.add(createInfoPanel());
   
   // Add the logo and bottom panel to the main frame
      panel.add(logoPane);
      panel.add(bottomPane);
   
      mainFrame.setContentPane(panel);
      mainFrame.setSize(FRAMEWIDTH, FRAMEHEIGHT);
      mainFrame.setVisible(true);
   }

// getRow
   public int getRow(JLabel label) {
      int result = -1;
      for (int i = 0; i < NUMROW && result == -1; i++) {
         for (int j = 0; j < NUMCOL && result == -1; j++) {
            if (slots[i][j] == label) {
               result = i;
            }
         }
      }
      return result;
   }

// getColumn
   public int getColumn(JLabel label) {
      int result = -1;
      for (int i = 0; i < NUMROW && result == -1; i++) {
         for (int j = 0; j < NUMCOL && result == -1; j++) {
            if (slots[i][j] == label) {
               result = j;
            }
         }
      }
      return result;
   }

   public void addListener (OthelloListener listener) {
      for (int i = 0; i < NUMROW; i++) {
         for (int j = 0; j < NUMCOL; j++) {
            slots [i] [j].addMouseListener (listener);
         }
      }
   }

// add listener to all the buttons
   public void addListener(ButtonListener listener) {
      saveGameButton.addActionListener(listener);
      loadGameButton.addActionListener(listener);
      restartGameButton.addActionListener(listener);
      exitButton.addActionListener(listener);
   }

/**
* Display the specified player icon on the specified slot
* 
* @param row row of the slot
* @param col column of the slot
* @param player player to be displayed
*/
   public void setPiece(int row, int col, int player) {
      slots[row][col].setIcon(playerIcon[player]);
   }

/**
* Display the score on the textfield of the corresponding player
* 
* @param player the player whose score to be displayed
* @param score the score to be displayed
*/
   public void setPlayerScore(int player, int score) {
      playerScore[player].setText(score+"");
   }

/**
* Display the appropriate player icon under"Next Turn"
* 
* @param player the player number of the next player; its corresponding icon will be displayed under "Next Turn"
*/
   public void setNextPlayer(int player) {
      nextPlayerIcon.setIcon(playerIcon[player]);
   }

/**
* Reset the game board (clear all the pieces on the game board)
* 
*/
   public void resetGameBoard() {
      for (int i = 0; i < NUMROW; i++) {
         for (int j = 0; j < NUMCOL; j++) {
            slots[i][j].setIcon(null);
         }
      }
   }

/**
* Display a pop up window displaying the message about invalid move
* 
*/
   public void showInvalidMoveMessage(){
      JOptionPane.showMessageDialog(null, " This move is invalid", "Invalid Move", JOptionPane.PLAIN_MESSAGE, null); 
   }

/**
* Display a pop up window specifying the number of opponents that was outflanked
* 
* @param player the player number who has outflanked opponents
* @param outflank the number of opponents that were outflanked
*/
   public void showOutflankMessage(int player, int outflank){
      JOptionPane.showMessageDialog(null, " outflanked " + outflank + " opponents.", "OutFlank!", JOptionPane.PLAIN_MESSAGE, playerIcon[player]); 
   }
   
/**
* Display a pop up window displaying the message about a tie game
* 
*/
   public void showTieGameMessage(){
      JOptionPane.showMessageDialog(null, " This game is tie.", "Tie Game", JOptionPane.PLAIN_MESSAGE, null); 
   }

/**
* Display a pop up window specifying the winner of this game
* 
* @param player the player number of the winner of the game
*/
   public void showWinnerMessage(int player){
      JOptionPane.showMessageDialog(null, " won this game!", "This game has a winner!", JOptionPane.PLAIN_MESSAGE, playerIcon[player]); 
   }

/**
 * Displays a dialog box asking the user whether they want to play a new game.
 *
 *  @return			<code>true</code> if the select "Yes"; the program exits otherwise.
 */
    public boolean showPlayAgain() {
      int choice = JOptionPane.showConfirmDialog(null, "Play Again" , "Play New Game?", JOptionPane.YES_NO_OPTION); 

      if (choice == JOptionPane.YES_OPTION) {
         return true;
      } else {
         // choice == JOptionPane.NO_OPTION or dialog is closed without selection
         System.exit (0);
      }
      return false;
   }

   public static void main (String[] args) {
      OthelloGUI gui = new OthelloGUI ();
      Othello game = new Othello (gui);
      OthelloListener listener = new OthelloListener (game, gui);
      ButtonListener butListener = new ButtonListener(game, gui); 
      game.newGame();
   }

}