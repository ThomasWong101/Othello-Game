/**
* Othello.java
Name: Thomas Wong
Date last updated: January 16 2026
Purpose: To apply the logic of overthrowing, move valdiation, moving/playing,
score keeping, saving/loading files,and updating gui here.
*/

import java.io.*;

public class Othello {

// =========================================================================
// Constants and variables required by other classes
// Do not change the names nor delete them

   final String GAMEFILEFOLDER = "gamefiles"; 
   
   final int NUMPLAYER;   // number of players in the game
   final int NUMROW;		  // number of rows in the game board
   final int NUMCOL;	 	  // number of columns in the game board
	
   OthelloGUI gui;	// the object referring to the GUI
							// use it when calling methods to update the GUI

// ======================================================================

//=== *** Your "global" constants & variables can be added starting here *** ===//
   int [][] board;  //initalizes the gameboard
   final int P1 = 0; //sets player 1 to 0 on the gameboard
   final int P2 = 1; //sets player 2 to 1 on the gameboard
   final int EMPTY_SLOT = -1; //sets an empty slot to -1
   boolean player1Status = true; //player condition - whose turn it is 
   int player1Score; //player 1 score count
   int player2Score; //player 2 score count
   String fileName; //string for a user's file name
   final int COLUMN = 8; //# of columns on the array
   final int ROW = 8; //# of rows on the array
   
   
/**
 * Constructs Othello object.
 *
 * @param gui	The GUI object that will be used by this class.
 */ 
   public Othello(OthelloGUI gui) {
      this.gui = gui;
      NUMPLAYER = gui.NUMPLAYER;
      NUMROW = gui.NUMROW;
      NUMCOL = gui.NUMCOL;
      board = new int [ROW][COLUMN]; //creates a new gameboard
      
      // arrays used to implement the logic of the game should be created here
   }
   
    /*
      Method: newGame - non static
      Parameters:  none
      return: void
      description: this game resets the board to an inital state
   */
   
   public void newGame (){
      gui.resetGameBoard(); //resets in case of prior game
      for (int r = 0; r < ROW; r++){ //sets board up to empty value
         for (int c = 0; c < COLUMN; c++){
            board[r][c] = EMPTY_SLOT;
         }
      } 
   
      board[4][3] = P1; //sets up inital board and updates gui
      gui.setPiece(4,3,0);
      board[3][4] = P1;
      gui.setPiece(3,4,0);
      board[3][3] = P2;
      gui.setPiece(3,3,1);
      board[4][4] = P2;
      gui.setPiece(4,4,1);
      
      player1Score = 2;
      player2Score = 2;
      gui.setPlayerScore(0,player1Score); //sets player 1 and 2 score to 2 
      gui.setPlayerScore(1,player2Score);
      player1Status = true; //black or A's or player 1's turn to play
      gui.setNextPlayer(0);
            
   }
   /*
      Method: play - non static
      Parameters:  integer r and integer c
      return:void
      description: this method is the binder for all the other submethods which deal 
      logic of outflanking and move validity. 
   */  
   public void play (int r, int c){ //1 is (p2) and 0 is (p1) 
      boolean valid = validity(r,c);
      int piecesTotal = player1Score + player2Score; //if the total score is 64 - all grids are filled
      if (piecesTotal == 64){ 
         endGame(); //end the game with scores
      }else if (valid == false || board[r][c] != EMPTY_SLOT){ //clicks on a slot that is already filled or move is invalid
         gui.showInvalidMoveMessage();
      }else if (player1Status == true){  //if it is player(1) A's turn
         gui.setPiece(r,c,0);
         gui.setNextPlayer(1);
         board[r][c] = P1;
         overthrownController(r,c); //passses it to delve out overthrown and change score
         player1Score = scoreUpdate(P1); //updates player 1 score based on scoreUpdate method and readjusts gui
         gui.setPlayerScore(0, player1Score); 
         player2Score = scoreUpdate(P2); //updates player 2 score - might change if overthrown
         gui.setPlayerScore(1, player2Score);
         player1Status = false;
      }else{
         gui.setPiece(r,c,1); //player 2 turn
         gui.setNextPlayer(0);
         board[r][c] = P2; 
         overthrownController(r,c); //passes it into the controller to see who is in play
         player2Score = scoreUpdate(P2); //updates player 2 score based on scoreUpdate method and readjusts gui
         gui.setPlayerScore(1, player2Score);
         player1Score = scoreUpdate(P1); //updates player 1 score if overthrow occurs
         gui.setPlayerScore(0, player1Score);
         player1Status = true;
          
      }
   }
  /*
      Method: endGame - nonstatic
      Parameters: none
      return: void
      description: counts array board for # of pieces of a certain player type
   */
   
   public void endGame (){
      boolean nextGame = false;
      if (player1Score > player2Score){ //player1 wins
         gui.showWinnerMessage(0);
      }else if (player2Score > player1Score){ //player 2 has higher points
         gui.showWinnerMessage(1);
      }else{ //equal points 
         gui.showTieGameMessage();
      }
      nextGame = gui.showPlayAgain();
      if (nextGame){ //if true sends to new game
         newGame();
      }
   }
    /*
      Method: scoreUpdate - nonstatic
      Parameters: int currentPlayer
      return: int
      description: counts array board for # of pieces of a certain player type
   */
   public int scoreUpdate(int currentPlayer){
      int count = 0;
      for (int r = 0; r < ROW; r++){ //search for piece and update counter
         for (int c = 0; c < COLUMN; c++){
            if (board[r][c] == currentPlayer){
               count++;
            }
         }
      }
      return count;
   }
   
   /*
      Method:validity - non static 
      Parameters: int r, int c
      return: boolean
      description: This method checks to see
      if the move contacts a piece around it 
   */
   public boolean validity(int row, int column){
      boolean goodMove = false; //move placed is board[r][c]
      if (column == 0){
         if (row == 0 && (board[1][0] != EMPTY_SLOT) || (board[0][1] != EMPTY_SLOT) || (board[1][1] != EMPTY_SLOT)){//check in right, below, and bottom right for [0][0]
            //if (()){
            goodMove = true;
           // }
         }else if (row == 7 && (board[7][1] != EMPTY_SLOT) || (board[6][0] != EMPTY_SLOT) || (board[6][1] != EMPTY_SLOT)){ //check above, top right, and right for [7][0]
           // if ((board[7][1] != EMPTY_SLOT) || (board[6][0] != EMPTY_SLOT) || (board[6][1] != EMPTY_SLOT)){
            goodMove = true;
            //}
         }else{  //any other spot beside the corners in column 1
            for (int rowCheck = row - 1;rowCheck <= row + 1 && goodMove ==false; rowCheck++){ //checks in a rectangle to the right to see move validity
               for (int columnCheck = column; columnCheck <= column+1 && goodMove == false; columnCheck++){
                  if (board[rowCheck][columnCheck] != EMPTY_SLOT){
                     goodMove = true;
                  }
               }
            }
         
         }
      }else if (column == 7){
         if (row == 0 && (board[0][6] != EMPTY_SLOT) || (board[1][7] != EMPTY_SLOT) || (board[1][6] != EMPTY_SLOT) ){ //check in left, below, and bottom left
           // if ((board[0][6] != EMPTY_SLOT) || (board[1][7] != EMPTY_SLOT) || (board[1][6] != EMPTY_SLOT)){
            goodMove = true;
           // }
         }else if (row == 7 && (board[6][7] != EMPTY_SLOT) || (board[6][6] != EMPTY_SLOT) || (board[7][6] != EMPTY_SLOT)){ //check in left, above, and top left
            //if((board[6][7] != EMPTY_SLOT) || (board[6][6] != EMPTY_SLOT) || (board[7][6] != EMPTY_SLOT)){
            goodMove = true;
           // }
         }else{ //check above, top left, left, bottom left, and below
            for (int rowCheck = row-1;rowCheck <= row+1 && goodMove ==false; rowCheck++){ //checks in a box to see move validity
               for (int columnCheck = column; columnCheck >= column-1 && goodMove == false; columnCheck--){
                  if (board[rowCheck][columnCheck] != EMPTY_SLOT){
                     goodMove = true;
                  }
               }
            }
         } 
      
      }else if (row == 0){//check in left, bottom left, bottom, bottom right, right 
         for (int rowCheck = row;rowCheck <= row+1 && goodMove ==false; rowCheck++){ //checks in a box to see move validity
            for (int columnCheck = column-1; columnCheck <= column+1 && goodMove == false; columnCheck++){
               if (board[rowCheck][columnCheck] != EMPTY_SLOT){
                  goodMove = true;
               }
            }
         }
      
      
      }else if (row == 7){//check left, top left, above, top right, and right
         for (int rowCheck = row ;rowCheck >= row-1 && goodMove ==false; rowCheck--){ //checks in a box to see move validity
            for (int columnCheck = column-1; columnCheck <= column+1 && goodMove == false; columnCheck++){
               if (board[rowCheck][columnCheck] != EMPTY_SLOT){
                  goodMove = true;
               }
            }
         }
      }else{//checks in a box to see move validity 
         for (int rowCheck = row-1;rowCheck <= row+1 && goodMove ==false; rowCheck++){ 
            for (int columnCheck = column-1; columnCheck <= column+1 && goodMove == false; columnCheck++){
               if (board[rowCheck][columnCheck] != EMPTY_SLOT){
                  goodMove = true;
               }
            }
         }
      }
      return goodMove;
   }
        
   
  /*
   Method: overthrownController - non static
   Parameters: int row and int column
   returns:void
   Description: Takes a check in all directions to see if it has the ability to overthrow and returns the total overthrown. 
 */
   public void overthrownController(int r, int c){
      int totalOverthrown = 0;
      int currentScore;//use score finding method
      if (player1Status){
         currentScore = scoreUpdate(0);//find current score for player 1 
      }else{
         currentScore = scoreUpdate(1);//find current score for player 2
      }
      if(r == 0){ 
         if(c == 0){ //check right, bottom right, and below
            overthrownRight(r,c);
            overthrownBottomRight(r,c);
            overthrownBelow(r,c);
         }else if (c == 7){// check left, bottom left, and below
            overthrownLeft(r,c);
            overthrownBottomLeft(r,c);
            overthrownBelow(r,c);
            
         }else{ //check left, bottom left, below, bottom right, and right
            overthrownLeft(r,c);
            overthrownBelow(r,c);
            overthrownBottomLeft(r,c);
            overthrownBottomRight(r,c);
            overthrownRight(r,c);
         }
      }else if (r == 7){
         if (c == 0){
            overthrownAbove(r,c);
            overthrownTopRight(r,c);
            overthrownRight(r,c);
         }else if (c == 7){
            overthrownAbove(r,c);
            overthrownTopLeft(r,c);
            overthrownLeft(r,c); 
         }else{
            overthrownLeft(r,c);
            overthrownTopLeft(r,c);
            overthrownAbove(r,c);
            overthrownTopRight(r,c);
            overthrownRight(r,c);
         }
      
      }else if (c == 0){
         overthrownAbove(r,c);
         overthrownTopRight(r,c);
         overthrownRight(r,c);
         overthrownBottomRight(r,c);
         overthrownBelow(r,c);
      
      } else if (c == 7){
         overthrownAbove(r,c);
         overthrownTopLeft(r,c);
         overthrownLeft(r,c);
         overthrownBottomLeft(r,c);
         overthrownBelow(r,c);
      
      }else{ //check in all directions
         overthrownAbove(r,c);
         overthrownTopLeft(r,c);
         overthrownLeft(r,c);
         overthrownBottomLeft(r,c);
         overthrownBelow(r,c);
         overthrownBottomRight(r,c);
         overthrownRight(r,c);
         overthrownTopRight(r,c);
      }
      if (player1Status){//updates entire for person 1 
         totalOverthrown = scoreUpdate(0);
      }else{//updates entire score for person 2 
         totalOverthrown = scoreUpdate(1);
      }
      totalOverthrown = totalOverthrown - currentScore; //finds actual amount overthrown through subtraction
      if (player1Status && totalOverthrown > 0){ //displays gui message for player 1 or player 2 
         gui.showOutflankMessage(0,totalOverthrown);
      }else if (totalOverthrown > 0){
         gui.showOutflankMessage(1,totalOverthrown);
      }
   }
 

 /*
   Method: overthrownLeft - non static
   Parameters: int row, int column
   returns: void
   Description: Takes a check for left for tiles able to be overthrown
 */

   public void overthrownLeft(int row, int column){ //checks left for possible overthrown tiles
      boolean runThrough = true;
      boolean overthrow = true;
      int movesToEdge = column;
      int counter = 0;
      for (int c = 1; c <= movesToEdge && runThrough == true && player1Status == true; c++){
         if (board[row][column - c] == EMPTY_SLOT){ //if it sees an empty slot while moving left, exits and cancels overthrow
            counter = -1;
            runThrough = false;
         }else if(board[row][column - c] == P1){ //finds and locates where p1 is left 
            counter = c;
            runThrough = false;
         }
      }
      for(int c =1; c <= movesToEdge && runThrough == true && player1Status == false; c++){
         if (board[row][column - c] == EMPTY_SLOT){ //if empty slot while moving left - cancel overthrow
            counter = -1;
            runThrough = false;
         }else if(board[row][column - c] == P2){ //finds and locates where p2 is left
            counter = c;
            runThrough = false;
         }
      }
      for (int i = 1; i < counter; i++){ //while the number of slots left is greater than the counter 
         if (player1Status == true){ //p1 flip 
            board[row][column - i] = P1;
            gui.setPiece(row, column - i,0);
         }else{
            board[row][column-i] = P2; //p2 flip
            gui.setPiece(row, column - i, 1);
         }
      }
   }
/* 
   Method: overthrownRight - non static
   Parameters: int row and int column
   returns: void
   Description: Takes a check for right for tiles able to be overthrown.
*/
   public void overthrownRight(int row, int column){
      boolean runThrough = true;
      int movesToEdge = (COLUMN-1) - column; //ensures that maximum is 7 right 
      int counter = 0;
      for (int c = 1; c <= movesToEdge && runThrough == true && player1Status == true; c++){ //checks for pot. overthrow for player 2
         if(board[row][column + c] == EMPTY_SLOT){
            counter = -1;//if there is an empty slot changes it to impossible overthrow
            runThrough = false;
         } else if(board[row][column + c] == P1){ //finds same player piece - stops search for overthrow
            counter = c;
            runThrough = false;
         }
      }
      for(int c =1; c <= movesToEdge && runThrough == true && player1Status == false; c++){ //checks for pot. overthrow for player 1 
         if (board[row][column + c] == EMPTY_SLOT){ //if there is an empty slot changes it to impossible overthrow
            counter = -1;
            runThrough = false;
         }else if(board[row][column + c] == P2){ //finds same player piece - stop search for p2 overthrow
            counter = c;
            runThrough = false;
         }
      }
      for (int i = 1; i < counter; i++){ //place and overthrow on gui and board
         if (player1Status == true){
            board[row][column + i] = P1;
            gui.setPiece(row,column + i,0);
         }else{
            board[row][column + i] = P2;
            gui.setPiece(row,column + i,1);
         }
         
      }
      
   }
 
 /*
   Method: overthrownAbove - non static
   Parameters: int row, int column and boolean p1Status
   returns: void
   Description: Checks directly above for the ability to overthrow.
 */
   public void overthrownAbove(int row, int column){
      boolean runThrough = true;
      int movesToEdge = row;
      int counter = 0;
      for (int r = 1; r <= movesToEdge && runThrough == true && player1Status == true; r++){
         if(board[row - r][column] == EMPTY_SLOT){//if there is an empty slot changes it to impossible overthrow
            counter = -1;
            runThrough = false;
         }else if(board[row - r][column] == P1){//overthrow location for p1
            counter = r;
            runThrough = false;
         }
      }
      for(int r = 1; r <= movesToEdge && runThrough == true && player1Status == false; r++){
         if(board[row-r][column] == EMPTY_SLOT){ //if there is an empty slot changes it to impossible overthrow
            counter = -1;
            runThrough = false;
         }else if(board[row - r][column] == P2){//overthrow location for p2
            counter = r;
            runThrough = false;
         }
      }
      for (int i = 1; i < counter; i++){ //sets gui and board pieces to overthrow
         if(player1Status == true){
            board[row - i][column] = P1;
            gui.setPiece(row - i,column,0);
         }else{
            board[row - i][column] = P2;
            gui.setPiece(row - i,column,1);
         }
      }   
   }
   
 
  /*
   Method: overthrownBelow - non static
   Parameters: int row and int column
   returns: void
   Description: Checks directly below for the ability to overthrow. 
 */
   public void overthrownBelow(int row, int column){
      boolean runThrough = true;
      int movesToEdge = (ROW-1) - row; //max moves to edge is 7 
      int counter = 0;
      for (int r = 1; r <= movesToEdge && runThrough == true && player1Status == true; r++){ //player 1
         if(board[row+r][column] == EMPTY_SLOT){ //impossible overthrow
            counter = -1;
            runThrough = false;
         }else if(board[row + r][column] == P1){//overthrow location to for p1
            counter = r;
            runThrough = false;
         }
      }
      for(int r = 1; r <= movesToEdge && runThrough == true && player1Status == false; r++){
         if(board[row+r][column] == EMPTY_SLOT){ //impossible overthrow 
            counter = -1;
            runThrough = false;
         }else if(board[row + r][column] == P2){//overthrow location for p2
            counter = r;
            runThrough = false;
         }
      }
      for (int i = 1; i < counter; i++){ //applys overthrow to gui and board
         if (player1Status == true){
            board[row + i][column] = P1;
            gui.setPiece(row + i,column,0);
         }else{
            board[row +i][column] = P2;
            gui.setPiece(row + i,column,1);
         }
      }  
   }
 /*
   Method: overthrownTopLeft - non static
   Parameters: int row and int column and boolean player1Status
   returns: int
   Description: Takes a check for top left for tiles able to be overthrown. 
 */
   public void overthrownTopLeft(int r, int c){
      boolean runThrough = true;
      int movesToEdge = 0;
      int tempRow = r;
      int tempColumn = c;
      int counter = 0;
      while (tempRow > 0 && tempColumn > 0){ //figure out how many spaces to edge of board top left
         tempRow--;
         tempColumn--;
         movesToEdge++;
      }
      for (int i = 1; i <= movesToEdge && runThrough == true && player1Status == true; i++){
         if(board[r-i][c - i] == EMPTY_SLOT){ //no overthrow
            counter = -1;
            runThrough = false;
         }else if(board[r  -i][c - i] == P1){ //overthrow location for p1
            counter = i;
            runThrough = false;
         }
      }
      for(int i = 1; i <= movesToEdge && runThrough == true && player1Status == false; i++){
         if(board[r-i][c-i] == EMPTY_SLOT){ //no overthrow
            counter = -1;
            runThrough = false;
         }else if(board[r - i][c - i] == P2){ //overthrows location for p2
            counter = i;
            runThrough = false;
         }
      }
      for (int i = 1; i < counter; i++){ //applys overthrow to board and gui 
         if (player1Status == true){
            board[r - i][c - i] = P1;
            gui.setPiece(r - i,c - i,0);
         }else{
            board[r - i][c - i] = P2;
            gui.setPiece(r - i,c - i,1);
         }
      } 
      
   
   }
 
  /*
   Method: overthrownTopRight - non static
   Parameters: int row and int column
   returns: int
   Description: Takes a checks top right for the ability to overthrow. 
 */
   public void overthrownTopRight(int r, int c){
      boolean runThrough = true;
      int movesToEdge = 0;
      int tempRow = r;
      int tempColumn = c;
      int counter = 0;
      while (tempRow > 0 && tempColumn < 7){//figures out number of spaces to right corner
         tempRow--;
         tempColumn++;
         movesToEdge++;
      }
      for (int i = 1; i <= movesToEdge && runThrough == true && player1Status == true; i++){
         if(board[r-i][c + i] == EMPTY_SLOT){ //stops overthrow attempt
            counter = -1;
            runThrough = false;
         }else if(board[r  -i][c +i] == P1){//p1 recorded for overthrow to 
            counter = i;
            runThrough = false;
         }
      }
      for(int i = 1; i <= movesToEdge && runThrough == true && player1Status == false; i++){
         if(board[r-i][c+i] == EMPTY_SLOT){ //stops overthrow attempt
            counter = -1;
            runThrough = false;
         }else if(board[r - i][c + i] == P2){//finds own piece and records it 
            counter = i;
            runThrough = false;
         }
      }
      for (int i = 1; i < counter; i++){ //applies overthrow to gui and board
         if (player1Status == true){
            board[r - i][c +i] = P1;
            gui.setPiece(r - i,c + i,0);
         }else{
            board[r - i][c +i] = P2;
            gui.setPiece(r - i,c + i,1);
         }
      }  
   }

  /*
   Method: overthrownBottomLeft - non static
   Parameters: int row and int column
   returns: void
   Description: Checks bottom left for the ability to overthrow. 
 */
   public void overthrownBottomLeft(int r, int c){  
      boolean runThrough = true;
      int movesToEdge = 0;
      int tempRow = r;
      int tempColumn = c;
      int counter = 0;
      while(tempRow < 7 && tempColumn > 0){ //counts number of slots in bottom left fashion
         tempRow++;
         tempColumn--;
         movesToEdge++;
      }
      for (int i = 1; i <= movesToEdge && runThrough == true && player1Status == true; i++){
         if(board[r+i][c - i] == EMPTY_SLOT){ //no overthrow
            counter = -1;
            runThrough = false;
         }else if(board[r + i][c - i] == P1){ //finds P1 piece to overthrow to 
            counter = i;
            runThrough = false;
         }
      }
      for(int i = 1; i <= movesToEdge && runThrough == true && player1Status == false; i++){
         if(board[r + i][c - i] == EMPTY_SLOT){ //no overthrow
            counter = -1;
            runThrough = false;
         }else if(board[r + i][c - i] == P2){ //finds p2 piece to overthrow to 
            counter = i;
            runThrough = false;
         }
      }
      for (int i = 1; i < counter; i++){ //applies to gui and board
         if (player1Status == true){
            board[r + i][c - i] = P1;
            gui.setPiece(r + i,c - i,0);
         }else{
            board[r + i][c - i] = P2;
            gui.setPiece(r + i,c - i,1);
         }
      }   
   
   }
  /*
   Method: overthrownBottomRight - non static
   Parameters: int row and int column
   returns: void
   Description: Checks for rightward motion.
 */
   public void overthrownBottomRight(int r, int c){
      boolean runThrough = true;
      int movesToEdge = 0;
      int tempRow = r;
      int tempColumn = c;
      int counter = 0;
      while (tempRow < 7 && tempColumn <7){//counts to bottom right 
         tempRow++;
         tempColumn++;
         movesToEdge++;
      }
      for (int i = 1; i <= movesToEdge && runThrough == true && player1Status == true; i++){
         if(board[r+i][c + i] == EMPTY_SLOT){ //no overthrow 
            counter = -1;
            runThrough = false;
         }else if(board[r + i][c + i] == P1){ //records where to overthrow to 
            counter = i;
            runThrough = false;
         }
      }
      for(int i = 1; i <= movesToEdge && runThrough == true && player1Status == false; i++){
         if(board[r + i][c + i] == EMPTY_SLOT){//no overthrow
            counter = -1;
            runThrough = false;
         }else if(board[r + i][c + i] == P2){ //finds where to stop p2 overthrow
            counter = i;
            runThrough = false;
         }
      }
      for (int i = 1; i < counter; i++){ //applies to gui and board
         if (player1Status == true){
            board[r + i][c + i] = P1;
            gui.setPiece(r + i,c + i,0);
         }else{
            board[r + i][c + i] = P2;
            gui.setPiece(r + i,c + i,1);
         }
      }
   }

 /*
      Method: saveToFile - non static
      Parameters:  String fileName
      return: boolean
      description: This method takes the current state of the game and shoves 
      outputs a text save file
   */
      
   public boolean saveToFile (String fileName){
      boolean saveStatus = false;
      try { 
         BufferedWriter out = new BufferedWriter (new FileWriter (GAMEFILEFOLDER+"/" + fileName, true));
         if (player1Status == true){
            out.write("1\n");
         }else{
            out.write("2\n");
         }
         out.write(player1Score + "\n");
         out.write(player2Score + "\n");
         for (int r = 0; r < ROW; r++){
            for (int c = 0; c < COLUMN; c++){
               out.write(board[r][c] + " ");
            }
            out.newLine();
         }
         saveStatus = true;
         out.close();
      }catch (IOException iox){
         System.out.println("Problem writing " + fileName);
         saveStatus = false;
      }
      return saveStatus;
   }
   
   /*
      Method: loadFromFile - non static
      Parameters:  String fileName
      return: boolean
      description: This method takes the status from 
      a save file and inputs it into the game varriables
      such as the board and player status. 
   */
   public boolean loadFromFile (String fileName){
      boolean loadStatus = false;
      try{
         BufferedReader in = new BufferedReader (new FileReader (GAMEFILEFOLDER +"/" + fileName));
         int playerOneInt = Integer.parseInt(in.readLine());
         if (playerOneInt == 0){
            player1Status = true;
         }else{
            player1Status = false;
         }
         gui.setNextPlayer(playerOneInt);
         player1Score = Integer.parseInt(in.readLine());
         gui.setPlayerScore(0,player1Score);
         player2Score = Integer.parseInt(in.readLine());
         gui.setPlayerScore(1,player2Score);
         String[] input;
         for (int r = 0; r < ROW; r++){
            input = in.readLine().split(" ");
            for (int c = 0; c < COLUMN; c++){
               board[r][c] = Integer.parseInt(input[c]);
            } 
         }
         gui.resetGameBoard();
         for(int r = 0; r < ROW; r++){
            for(int c = 0; c < COLUMN; c++){
               if (board[r][c] == P1){
                  gui.setPiece(r,c,0);
               }else if (board[r][c] == P2){
                  gui.setPiece(r,c,1);
               }
            }
         }
         loadStatus = true;
         
      }catch(IOException iox){
         System.out.println("Problem Reading "+fileName);
         loadStatus = false;
      }
      return loadStatus;
      
   }
}