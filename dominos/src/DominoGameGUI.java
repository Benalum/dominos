/*
// Created by Alex Hartel for CS 351 project
// DominoGameGUI brings it all together
// Created in February of 2024
// Finished Notes and Organization by number of inputs
*/

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class DominoGameGUI extends Application {
    private Player humanPlayer;
    private Player computerPlayer;
    private Boneyard boneyard;
    private List<DominoPiece> board;
    private boolean isFirstMove = true;
    public static Pane selectedDomino = null;
    private boolean isRotate = false;
    private boolean isLeftSide = false;

        /*
    // Used to communicate in the DominoPiece file
     */

    public static int intSelectedDomino;

    public static void main(String[] args) {
        launch(args);

    }

    /*
    Negate first move
    */
    private void setFirstMoveOpposite(){ isFirstMove = !isFirstMove;}


    /*
    Initialize human, computer, boneyard, and board.  Then give 7 dominos to each
     */

    private void initializeGame() {
        this.humanPlayer = new Player("Human");
        this.computerPlayer = new Player("Computer");
        this.boneyard = new Boneyard();
        this.board = new ArrayList<>();

        getHand(this.humanPlayer, this.boneyard);
        getHand(this.computerPlayer, this.boneyard);
    }



    @Override
    public void start(Stage primaryStage) {
        initializeGame();

        // Game board
        Pane gameBoard = new Pane();
        gameBoard.setPrefSize(1000, 400); // Example size


        // Player's hand
        HBox playerHand = new HBox();
        playerHand.setSpacing(10); // Spacing between dominos

        // Example: Add dominos to the player's hand
        int index = 0;
        for (DominoPiece domino : this.humanPlayer.returnHand()) {
            Pane dominoGraphic = domino.drawDomino(index);
            final int dominoIndex = index;  // Capture the current index for use in the event handler
            dominoGraphic.setOnMouseClicked(event -> {
                if (selectedDomino != null) {
                    selectedDomino.setStyle(""); // Remove border from previously selected domino
                }
                dominoGraphic.setStyle("-fx-border-color: black; -fx-border-width: 2;"); // Add border to this domino
                selectedDomino = dominoGraphic;
                intSelectedDomino = dominoIndex;  // Update intSelectedDomino with the index of the clicked domino
            });
            playerHand.getChildren().add(dominoGraphic);
            index++;
        }

        // Boneyard display
        VBox boneyardDisplay = new VBox();

        // Layout setup
        BorderPane root = new BorderPane();
        root.setCenter(gameBoard);
        root.setBottom(playerHand);
        root.setRight(boneyardDisplay);

        buttonEvents(this.humanPlayer, boneyard, playerHand, gameBoard, root);

        // Scene setup
        Scene scene = new Scene(root, 800, 600); // Example size
        primaryStage.setTitle("Domino Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /*
    Pops up an alert window informing you of the winner
     */

    private void showWinner(String winnerMessage) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(winnerMessage);

        alert.showAndWait();
    }
    /*
    puts the board on the window using gameBoard/Pane
     */

    private void updateGameBoard(Pane gameBoard) {
        gameBoard.getChildren().clear(); // Clear previous dominos

        double firstRowY = 100; // Y-coordinate for the first row
        double secondRowY = 205; // Y-coordinate for the second row
        double xOffset = 5; // Horizontal position of the first domino

        // Iterate through each domino and position them
        for (int i = 0; i < board.size(); i++) {
            DominoPiece domino = board.get(i);
            Pane dominoGraphic = domino.drawDomino(i);

            // Set the horizontal position for the domino
            dominoGraphic.setLayoutX(xOffset + 5);

            // Place domino in the first or second row based on its index
            if (i % 2 == 0) {
                dominoGraphic.setLayoutY(firstRowY);
            } else {
                dominoGraphic.setLayoutY(secondRowY);
            }

            gameBoard.getChildren().add(dominoGraphic); // Add domino to the board
            xOffset += 51; // Increment the offset for the next domino
        }
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
    Update your current hand to the window/HBOX in the window whichever you prefer to use.
     */

    private void updateHandGUI(Player player, HBox playerHand) {
        int index = 0;
        playerHand.getChildren().clear();
        for (DominoPiece domino : player.returnHand()) {
            Pane dominoGraphic = domino.drawDomino(index ++);
            playerHand.getChildren().add(dominoGraphic);
        }
    }


    /*
    // Compare Domino numbers

    */

    private boolean comparison(int a, int b){
        return a == 0 || b == 0 || a == b;
    }

    /*
    // Give 7 Dominoes to player from boneyard

     */

    private void getHand(Player player, Boneyard boneyard){
        for(int i = player.getLengthOfHand(); i < 7; i++){
            grabAnotherDomino(player, boneyard);
        }
    }

    /*
    // put domino in hand

     */

    private void grabAnotherDomino(Player player, Boneyard boneyard) {
        player.addToHand(boneyard.getPiece());
    }

    /*
    Check who wins
     */

    private void checkForWin(Player player,Player computer, Boneyard boneyard,
                             List<DominoPiece> board, boolean isFirstMove){  //////////////////////////////////5
        if(player.getLengthOfHand() == 0){
            // Player wins
            showWinner("Player Wins!!");
        } else if (computer.getLengthOfHand() == 0) {
            // Computer wins
            showWinner("Computer Wins!");
        } else if(boneyard.getSizeOfBoneyard() == 0 && !checkIfPlayerHandHasMove(player,board, isFirstMove)
                && !checkIfPlayerHandHasMove(computerPlayer, board, isFirstMove)){
            if (player.getLengthOfHand() > computer.getLengthOfHand()){
                // Player wins
                showWinner("Player Wins!");
            } else if (player.getLengthOfHand() < computer.getLengthOfHand()) {
                //Computer wins
                showWinner("Computer Wins!");
            }else{
                // Its a tie
                showWinner("Its a tie!");
            }
        }
    }
    /*
    Updates the board and the hand to the window
     */

    private void updateBoard(Player player, HBox playerHand, Pane gameBoard){
        updateGameBoard(gameBoard);
        updateHandGUI(player, playerHand);
    }

    /*
    computer player moves automatically based on first to last dominos in hand searching for any valid move.
     */

    void computerPlayerMove(Player computerPlayer, List<DominoPiece> board, Boneyard boneyard){

        DominoPiece left;
        DominoPiece right;
        DominoPiece current;
        // While no move keep picking new dominos
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
                if (currentLeft == 0 || currentRight == 0 ||currentLeft == leftLeft || currentRight == leftLeft
                        || currentLeft == rightRight || currentRight == rightRight) {
                    if (comparison(leftLeft, currentLeft)) {
                        flipDomino(computerPlayer, i);
                        board.addFirst(computerPlayer.getDominoAtIndex(i));
                        computerPlayer.removeDominoPieceAtIndex(i);
                        break;
                        //return true;
                    } else if (comparison(leftLeft, currentRight)) {
                        board.addFirst(computerPlayer.getDominoAtIndex(i));
                        computerPlayer.removeDominoPieceAtIndex(i);
                        break;
                        //return true;
                    }
                    else if (comparison(rightRight, currentRight)) {
                        flipDomino(computerPlayer, i);
                        board.addLast(computerPlayer.getDominoAtIndex(i));
                        computerPlayer.removeDominoPieceAtIndex(i);
                        break;
                        //return true;
                    } else if (comparison(rightRight, currentLeft)) {
                        board.addLast(computerPlayer.getDominoAtIndex(i));
                        computerPlayer.removeDominoPieceAtIndex(i);
                        break;
                        //return true;
                    }
                }
            }
        }
        //return false;
    }

    /*
    Check if player has a move in their hand, else false
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

            if (currentLeft == 0 || currentRight == 0 || currentLeft == leftLeft || currentRight == leftLeft || currentLeft == rightRight
                    || currentRight == rightRight) {
                return true;
            }
        }
        return false;
    }

    /*
    Holds button information to keep code clean
     */

    private void buttonEvents(Player player, Boneyard boneyard , HBox playerHand, Pane gameBoard, BorderPane root){
        Button drawButton = new Button("Draw from Boneyard");
        Button rotateButton = new Button("Rotate Domino");
        Button sideButton = new Button("Blue(Left) Red(Right) Side");
        Button playButton = new Button("Play Domino");

        //Start button red
        sideButton.setStyle("-fx-background-color: red;");

        // If no move exists draw domino
        drawButton.setOnAction(event -> {
            if(!checkIfPlayerHandHasMove(player,board, this.isFirstMove)){
                grabAnotherDomino(player, boneyard);
                updateHandGUI(player, playerHand);
            }
        });

        sideButton.setOnAction(event -> {
            isLeftSide = !isLeftSide;
            // Right side
            if(!isLeftSide){
                sideButton.setStyle("-fx-background-color: red;");
            }
            // Left side
            else{
                sideButton.setStyle("-fx-background-color: blue;");
            }
        });

        rotateButton.setOnAction(event -> {
            isRotate = !isRotate; // Toggle the isRotate state

            // Change the button color based on isRotate state
            if (isRotate) {
                rotateButton.setStyle("-fx-background-color: blue;");
            } else {
                rotateButton.setStyle(""); // Reset to default style
            }
        });

        playButton.setOnAction(event -> {
            // Send check for domino piece
            if(playerMoveImplementation(player, board, this.isFirstMove, isLeftSide, isRotate, intSelectedDomino)){
                computerPlayerMove(this.computerPlayer, board, boneyard);
                updateBoard(this.humanPlayer, playerHand, gameBoard);
                System.out.print("INT SEL: " + intSelectedDomino);
                checkForWin(this.humanPlayer,this.computerPlayer, boneyard,
                        board, isFirstMove);
            }

        });
        root.setTop(new HBox(drawButton,rotateButton,sideButton,playButton)); // Buttons at the top

    }

    /*
    // If valid move then it will find a placement with a little assistance.
    // Can simply remove while loop to get rid of option to auto place tile
     */

    private boolean playerMoveImplementation(Player player, List<DominoPiece> board, boolean isFirstMove,
                                          boolean isLorR, boolean isRotateFirst, int indexOfDomino){
        // Check if first domino, if so place
        DominoPiece left;
        DominoPiece right;
        DominoPiece current;
        if(isFirstMove){
            if(isRotateFirst){
                flipDomino(player, indexOfDomino);
            }// put domino on board
            board.addFirst(player.getDominoAtIndex(indexOfDomino));
            // remove domino from hand
            player.removeDominoPieceAtIndex(indexOfDomino);
            // Negate first move
            setFirstMoveOpposite();
            return true;

        }else{
            current = player.getDominoAtIndex(indexOfDomino);
            int currentLeft = current.getLeftValue(current);
            int currentRight = current.getRightValue(current);
            left = board.getFirst();
            int leftLeft = left.getLeftValue(left);
            right = board.getLast();
            int rightRight = right.getRightValue(right);

            if(checkIfPlayerHandHasMove(this.humanPlayer,board, isFirstMove)){
                // if True then they want left
                if(isLorR){
                    // Left Rotate
                    if(isRotateFirst){
                        if(comparison(leftLeft, currentLeft)){
                            flipDomino(player, indexOfDomino);
                            board.addFirst(player.getDominoAtIndex(indexOfDomino));
                            player.removeDominoPieceAtIndex(indexOfDomino);
                            return true;
                        }
                    }
                    //Left not Rotate
                    else{
                        if(comparison(leftLeft, currentRight)){
                            board.addFirst(player.getDominoAtIndex(indexOfDomino));
                            player.removeDominoPieceAtIndex(indexOfDomino);
                            return true;
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
                            return true;
                        }
                    }
                    // Right not Rotate
                    else {
                        if (comparison(rightRight, currentLeft)) {
                            board.add(player.getDominoAtIndex(indexOfDomino));
                            player.removeDominoPieceAtIndex(indexOfDomino);
                            return true;
                        }
                    }
                }
            }
            else { System.out.print("\n No valid move with that domino\n"); }
        }
        return false;
    }
}
