/** 
 *	File Name: OthelloListener.java
 *	Name: Ms. I. Lam
 *	Course: ICS3U1
 *	Date: December 6, 2025
 * Description: This class implement the listener used to detect all actions
 * on the game board - mouse clicks on game grid and button clicks
 */

import javax.swing.*;
import java.awt.event.*;
public class OthelloListener implements MouseListener
{
   private OthelloGUI gui;
   private Othello game;
   public OthelloListener (Othello game, OthelloGUI gui) {
      this.game = game;
      this.gui = gui;
      gui.addListener (this);
   }
   
   
   public void mouseClicked (MouseEvent event) {
      JLabel label = (JLabel) event.getComponent ();
      int row = gui.getRow(label);
      int column = gui.getColumn (label);
      game.play(row, column);   
   }
   
   public void mousePressed (MouseEvent event) {
   }
   
   public void mouseReleased (MouseEvent event) {
   }
   
   
   public void mouseEntered (MouseEvent event) {
   }
   
   public void mouseExited (MouseEvent event) {
   }
}

class ButtonListener implements ActionListener {
   final String SAVEGAMEBUTTON = "Save Game";
   final String LOADGAMEBUTTON = "Load Game";
   final String RESTARTGAMEBUTTON = "Restart Game";
   final String EXITBUTTON = "Exit";
   
   private OthelloGUI gui;
   private Othello game;
   
   public ButtonListener (Othello game, OthelloGUI gui) {
      this.game = game;
      this.gui = gui;
      gui.addListener (this);
   }

   public void actionPerformed(ActionEvent e) {
      // detect which button is clicked
      String button = e.getActionCommand();
      
      if (button.equals(EXITBUTTON)) {
         System.exit(0);
      } else if (button.equals(RESTARTGAMEBUTTON)) {
         game.newGame();
      } else {
         // if save or load game, prompt user for the file name
         String fileName = JOptionPane.showInputDialog(null, "File Name: ", "File Name", JOptionPane.QUESTION_MESSAGE);   
         if (fileName != null) {
            if (button.equals(SAVEGAMEBUTTON)) {
               if (!game.saveToFile(fileName)) {
                  JOptionPane.showMessageDialog(null, "Problem Saving Game!", "Error", JOptionPane.PLAIN_MESSAGE, null); 
               }
            } else if (button.equals(LOADGAMEBUTTON)) {
               if (!game.loadFromFile(fileName)) {
                  JOptionPane.showMessageDialog(null, "Problem Loading Game!", "Error", JOptionPane.PLAIN_MESSAGE, null);           
               }
            }
         }
      }
   }

}
