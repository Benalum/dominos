/*
// Created by Alex Hartel for CS 351 project
// Player class to hold the player name and the player hand
// Created in February of 2024
// Finished Notes and Organization by number of inputs
*/


import java.util.ArrayList;
import java.util.List;


public class Player {
    private final List<DominoPiece> hand;

    /*
    // Get the length of the hand(number of dominoes)

     */

    public int getLengthOfHand(){ return hand.size(); }

    /*
    // Add domino to hand

     */

    public void addToHand(DominoPiece d){ this.hand.add(d); }

    /*
    // get domino at specific index

     */

    public DominoPiece getDominoAtIndex(int index){ return this.hand.get(index); }

    /*
    // Get hand
     */

    public List<DominoPiece> returnHand(){ return this.hand;}

    /*
    // Give player name and hand

     */

    public Player(String name) {
        this.hand = new ArrayList<>();
    }

    /*
    // Remove domino piece at specific index

     */

    public void removeDominoPieceAtIndex(int x){ this.hand.remove(x); }

    /*
    // Helper function, trying to keep things private

     */

    private int getDominoPieceLeftValueHelper(DominoPiece d){ return d.getLeftValue(d); }

    /*
    // Get left value of domino

     */

    public int getLeftValue(int index) { return getDominoPieceLeftValueHelper(getDominoAtIndex(index)); }

    /*
    // Helper function, trying to keep things private

     */

    private int getDominoPieceRightValueHelper(DominoPiece d){ return d.getRightValue(d); }

    /*
    // Get right value of domino

     */

    public int getRightValue(int index) { return getDominoPieceRightValueHelper(getDominoAtIndex(index)); }

    /*
    Check if player has valid move
     */



}
