/*
// Created by Alex Hartel for CS 351 project
// Game class to pull the strings for the other classes
// Created in February of 2024
// Finished Notes and Organization by number of inputs
*/


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Game {
    private final Player humanPlayer;
    private final Player computerPlayer;
    private final Boneyard boneyard;
    private final List<DominoPiece> board;
    private boolean isFirstMove = true;

    /*
    // main function to run everything

     */

    public static void main(String[] args) {
        Game game = new Game("Human"); // Hard code name
        game.play(); // Start the game
    }

    /*
    // Print statement usage

     */

    private void terminalPrintStatementOne(){
        System.out.print("\nComputer has: " + computerPlayer.getLengthOfHand() + " dominos");
        System.out.print("\nBoneyard contains " + boneyard.getSizeOfBoneYardStartingAtOne() + " dominos");

    }

    /*
    // print statement usage

     */

    private void terminalPrintStatementTwo(){
        System.out.print("\nHuman's turn");
        System.out.print("\n[p] Play Domino");
        System.out.print("\n[d] Draw from boneyard");
        System.out.print("\n[q] Quit\n");
    }

    /*
    // netgate first move, can be used to negate when resetting game as well

     */

    private void setFirstMoveOpposite(){ isFirstMove = !isFirstMove;}


    /*
    // function to control the game
    // Computer turn or Player turn
    // Check for final 2 plays
    */

    public void play() {

        // Boolean for finished game
        boolean isGameFinished = false;

        Scanner Input = new Scanner(System.in);

        // Give 7 cards to human and computer hands
        getHand(this.humanPlayer, this.boneyard);
        getHand(this.computerPlayer, this.boneyard);

        while(!isGameFinished){
            // Check if end exists
            if(!board.isEmpty() && !checkIfPlayerHandHasMove(this.humanPlayer, board, isFirstMove)
                    && checkIfPlayerHandHasMove(this.computerPlayer, board, isFirstMove)){
                if(this.humanPlayer.getLengthOfHand() > this.computerPlayer.getLengthOfHand()){
                    System.out.print("\nGame over, Computer Won!");
                } else if (this.humanPlayer.getLengthOfHand() < this.computerPlayer.getLengthOfHand()) {
                    System.out.print("\nGame over, Human Won!");
                }
                else {
                    System.out.print("\nGame over, Tie!");
                }
            }
            else {
                //Player turn
                while (!playerMoveAssistant(Input, this.humanPlayer, board, isFirstMove, boneyard)) {
                    playerMoveAssistant(Input, this.humanPlayer, board, isFirstMove, boneyard);
                    if (this.humanPlayer.getLengthOfHand() == 0) {
                        System.out.print("\n congrats you won!");
                        System.exit(0);
                    }
                }


                System.out.print("\nComputer's turn");
                System.out.print("\nPrint Computer Move\n");

                // Put loop here as well

                // Computer turn
                while (!computerPlayerMove(this.computerPlayer, board, boneyard)) {
                    computerPlayerMove(this.computerPlayer, board, boneyard);
                }
            }

        }
    }


    /*
    // initialization of players, yard, and tray.

     */

    public Game(String humanName) {
        this.humanPlayer = new Player(humanName);
        this.computerPlayer = new Player("Computer");
        this.boneyard = new Boneyard();
        //this.tray = new ArrayList<>();
        this.board = new ArrayList<>();
    }

        /*
    // Check user input
    // Determine if user wants to rotate first or not
     */

    private boolean selectRotateFirstOrNot(Scanner Input){
        System.out.print("Rotate first? (y/n)");
        while(Input.hasNext()){
            String letter = Input.next();
            if(!letter.isEmpty()){
                if(letter.charAt(0) == 'y'){
                    return true;
                }else if (letter.charAt(0) == 'n'){
                    return false;
                }
            }
            System.out.print("Input required: y or n");
        }
        return false;
    }

        /*
    // Left is True right if False
    // Determine which side of the board to try Domino on
     */

    private boolean selectRightOrLeftSide(Scanner Input){
        System.out.print("Left or Right? (l/r)");
        while(Input.hasNext()){
            String letter = Input.next();
            if(!letter.isEmpty()){
                if(letter.charAt(0) == 'l'){
                    return true;
                }else if (letter.charAt(0) == 'r'){
                    return false;
                }
            }
            System.out.print("Input required: l or r");
        }
        return false;
    }

        /*
    // Print tray to terminal

     */

    private void terminalPrintStatementHand(Player player){
        System.out.print("\nTray: ");
        System.out.print("[");
        for (int i = 0; i < player.getLengthOfHand(); i++) {
            System.out.print(player.getDominoAtIndex(i).toStringDominoBoard(player.getDominoAtIndex(i)));
            if(i + 1 != player.getLengthOfHand()){
                System.out.print(", ");
            }
            if(i+1 == player.getLengthOfHand()){
                System.out.print("]");
            }
        }
    }

        /*
    // Print board to terminal

    */

    private void printBoard(List<DominoPiece> board){
        System.out.print("\n");
        for (int i = 0; i < board.size(); i++) {
            if(i%2 == 0){
                System.out.print(board.get(i).toStringDominoBoard(board.get(i)));
            }
        }
        System.out.print("\n   ");
        for (int i = 0; i < board.size(); i++) {
            if (i % 2 == 1) {
                System.out.print(board.get(i).toStringDominoBoard(board.get(i)));
            }
        }
    }

        /*
    // put domino in hand

     */

    private void grabAnotherDomino(Player player, Boneyard boneyard) {
        player.addToHand(boneyard.getPiece());
    }

    /*
    // Give 7 Dominoes to

     */

    private void getHand(Player player, Boneyard boneyard){
        for(int i = player.getLengthOfHand(); i < 7; i++){
           grabAnotherDomino(player, boneyard);
        }
    }

        /*
    // Compare Domino numbers

    */

    private boolean comparison(int a, int b){
        return a == 0 || b == 0 || a == b;
    }

        /*
    // Get input for which domino to select
     */

    private int getDominoInput(Scanner Input, Player player){
        System.out.print("Which domino: ");

        while(Input.hasNext()){
            if(Input.hasNextInt()){
                int temp = Input.nextInt();
                if(temp >= 0 && temp < player.getLengthOfHand()){
                    return temp;
                }
                System.out.println("Found int but not within bounds of hand");
            }else{
                System.out.println("That is not an int: " + Input.next());
            }
        }
        return -1;
    }

    /*
    // Flip the domino numbers

     */

    private void flipDomino(Player player, int indexOfDomino){
        DominoPiece x = player.getDominoAtIndex(indexOfDomino);
        int a = x.getLeftValue(x);
        int b = x.getRightValue(x);
        x.DominoPieceSwap(a, b);
    }

    /*
    // Check to see if player can draw a domino
    // Checks both end of the board for the piece sticking out.  If the player has a domino number that
    // will match either of them, it will return true.
     */

    boolean checkIfPlayerHandHasMove(Player player, List<DominoPiece> board, boolean isFirstMove){
        if(isFirstMove){
            return true;
        }
        DominoPiece left;
        DominoPiece right;
        DominoPiece current;

        for(int i = 0; i < player.getLengthOfHand(); i++) {
            current = player.getDominoAtIndex(i);
            int currentLeft = current.getLeftValue(current);
            int currentRight = current.getRightValue(current);
            left = board.getFirst();
            int leftLeft = left.getLeftValue(left);
            right = board.getLast();
            int rightRight = right.getRightValue(right);

            if (currentLeft == leftLeft || currentRight == leftLeft || currentLeft == rightRight
                    || currentRight == rightRight) {
                return true;
            }
        }
        return false;
    }

    /*
    // Computer player automatic move
    // Checks for valid move to make things easier, might be redundant I am still looking into it.
    // Checks dominos one at a time to see if there is a valid move
     */

    boolean computerPlayerMove(Player computerPlayer, List<DominoPiece> board, Boneyard boneyard){

        DominoPiece left;
        DominoPiece right;
        DominoPiece current;
        while(!checkIfPlayerHandHasMove(computerPlayer, board, isFirstMove)
                && boneyard.getSizeOfBoneyard() > 0){
            computerPlayer.addToHand(boneyard.getPiece());
        }
        // if valid move check hand
        if(checkIfPlayerHandHasMove(computerPlayer, board, isFirstMove)){
            for(int i = 0; i < computerPlayer.getLengthOfHand(); i++) {
                current = computerPlayer.getDominoAtIndex(i);
                int currentLeft = current.getLeftValue(current);
                int currentRight = current.getRightValue(current);
                left = board.getFirst();
                int leftLeft = left.getLeftValue(left);
                right = board.getLast();
                int rightRight = right.getRightValue(right);
                if (currentLeft == leftLeft || currentRight == leftLeft
                        || currentLeft == rightRight || currentRight == rightRight) {
                    if (comparison(leftLeft, currentLeft)) {
                        flipDomino(computerPlayer, i);
                        board.addFirst(computerPlayer.getDominoAtIndex(i));
                        computerPlayer.removeDominoPieceAtIndex(i);
                        return true;
                    } else if (comparison(leftLeft, currentRight)) {
                        board.addFirst(computerPlayer.getDominoAtIndex(i));
                        computerPlayer.removeDominoPieceAtIndex(i);
                        return true;
                    }
                    else if (comparison(rightRight, currentRight)) {
                        flipDomino(computerPlayer, i);
                        board.addLast(computerPlayer.getDominoAtIndex(i));
                        computerPlayer.removeDominoPieceAtIndex(i);
                        return true;
                    } else if (comparison(rightRight, currentLeft)) {
                        board.addLast(computerPlayer.getDominoAtIndex(i));
                        computerPlayer.removeDominoPieceAtIndex(i);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /*
    // Get user input and perform specific operations
    // Flows based on q,p, or d.
    // Check for move might be redundant looking into it
     */

    private void getPQDInput(Scanner Input, Player player, List<DominoPiece> board, Boneyard boneyard){
        int count = 0;
        String letter = Input.nextLine();
        while(letter.isEmpty() || letter.charAt(0) != 'q' || letter.charAt(0) !=  'd' || letter.charAt(0) !=  'p'){
            if (!letter.isEmpty()) {
                if (letter.charAt(0) == 'q') {
                    System.out.print("\nExit");
                    System.exit(0);
                }
                if (letter.charAt(0) == 'p'){
                    if(player.getLengthOfHand() > 0){
                        break;
                    }
                    else{
                        // Cant draw so ask everything again

                        // Set of Print Statements
                        terminalPrintStatementOne();

                        // Print hand
                        terminalPrintStatementHand(player);

                        // Set of Print Statements
                        terminalPrintStatementTwo();
                    }
                }
                if (letter.charAt(0) == 'd'){
                    if(!checkIfPlayerHandHasMove(player, board, isFirstMove)) {
                        player.addToHand(boneyard.getPiece());
                    }
                    else{

                        // Cant draw so ask everything again
                        // Set of Print Statements
                        terminalPrintStatementOne();

                        // Print hand
                        terminalPrintStatementHand(player);

                        // Set of Print Statements
                        terminalPrintStatementTwo();

                        if (count == 2){
                            System.out.print("END OF GAME YOU LOSE");
                        }
                        count += 1;

                    }
                }
            }
            letter = Input.nextLine();
        }
    }

        /*
    // Extension of play.  This has the order of operations when it is players move
    // Series of print statements and input grabbers to dictate the flow of the game
     */

    private boolean playerMoveAssistant(Scanner Input, Player player , List<DominoPiece> board,
                                        boolean isFirstMove, Boneyard boneyard){
        // Set of Print Statements
        terminalPrintStatementOne();

        // Print Board
        printBoard(board);

        // Print hand
        terminalPrintStatementHand(player);

        // Set of Print Statements
        terminalPrintStatementTwo();

        // Wait for input of p, d, or q
        getPQDInput(Input, player, board, boneyard);

        // Domino selection
        int indexOfDomino = getDominoInput(Input, player);

        // Select Left or Right side to place Domino
        boolean isLorR = selectRightOrLeftSide(Input);

        // Rotate first or not
        boolean isRotateFirst = selectRotateFirstOrNot(Input);

        // Check if valid move take in Domino, side, and rotate first
        playerMoveImplementation(player, board, isFirstMove, isLorR, isRotateFirst, indexOfDomino);
        return true;
    }

     /*
    // If valid move then it will find a placement with a little assistance.
    // Can simply remove while loop to get rid of option to auto place tile
     */

    private void playerMoveImplementation(Player player, List<DominoPiece> board, boolean isFirstMove,
                                          boolean isLorR, boolean isRotateFirst, int indexOfDomino){
        // Check if first domino, if so place
        DominoPiece left;
        DominoPiece right;
        DominoPiece current;
        boolean matchFound = false;
        int count;
        if(isFirstMove){
            if(isRotateFirst){
                flipDomino(player, indexOfDomino);
            }// put domino on board
            board.addFirst(player.getDominoAtIndex(indexOfDomino));
            // remove domino from hand
            player.removeDominoPieceAtIndex(indexOfDomino);
            // Negate first move
            setFirstMoveOpposite();

        }else{
            current = player.getDominoAtIndex(indexOfDomino);
            int currentLeft = current.getLeftValue(current);
            int currentRight = current.getRightValue(current);
            left = board.getFirst();
            int leftLeft = left.getLeftValue(left);
            right = board.getLast();
            int rightRight = right.getRightValue(right);

            if(currentLeft == leftLeft || currentRight == leftLeft || currentLeft == rightRight
                    || currentRight == rightRight){
                count = 0;
                // if True then they want left
                while(!matchFound){
                    if(isLorR){
                        // Left Rotate
                        if(isRotateFirst){
                            if(comparison(leftLeft, currentLeft)){
                                flipDomino(player, indexOfDomino);
                                board.addFirst(player.getDominoAtIndex(indexOfDomino));
                                player.removeDominoPieceAtIndex(indexOfDomino);
                                matchFound = true;
                            }
                        }
                        //Left not Rotate
                        else{
                            if(comparison(leftLeft, currentRight)){
                                board.addFirst(player.getDominoAtIndex(indexOfDomino));
                                player.removeDominoPieceAtIndex(indexOfDomino);
                                matchFound = true;
                            }
                        }
                    }
                    else {
                        // Right Rotate
                        if (isRotateFirst) {
                            if (comparison(rightRight, currentRight)) {
                                flipDomino(player, indexOfDomino);
                                board.add(player.getDominoAtIndex(indexOfDomino));
                                player.removeDominoPieceAtIndex(indexOfDomino);
                                matchFound = true;
                            }
                        }
                        // Right not Rotate
                        else {
                            if (comparison(rightRight, currentLeft)) {
                                board.add(player.getDominoAtIndex(indexOfDomino));
                                player.removeDominoPieceAtIndex(indexOfDomino);
                                matchFound = true;
                            }
                        }
                    }
                    if(count == 0){
                        isRotateFirst = !isRotateFirst;
                        count +=1;
                    }
                    else if(count == 1){
                        isLorR = !isLorR;
                        isRotateFirst = !isRotateFirst;
                        count += 1;
                    }else if(count == 2){
                        isRotateFirst = !isRotateFirst;
                        count += 1;
                    }else{
                        System.out.print("No move found and count is past 2");
                    }
                }
            }
            else{
                System.out.print("\n No valid move with that domino\n");

                // Set of Print Statements
                terminalPrintStatementOne();

                // Print hand
                terminalPrintStatementHand(player);

                // Set of Print Statements
                terminalPrintStatementTwo();

            }
        }
    }
}