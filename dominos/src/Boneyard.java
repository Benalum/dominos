/*
// Created by Alex Hartel for CS 351 project
// Boneyard class to hold all Dominoes not in use
// Created in February of 2024
// Finished Notes and Organization by number of inputs
*/


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Boneyard {
    private final List<DominoPiece> pieces;

    /*
    // Create the array with all 28 domino pieces
    // Returns a boneyard shuffled
     */

    public Boneyard() {
        this.pieces = new ArrayList<>();
        // Initialize boneyard with all 28 domino pieces
        for (int i = 0; i <= 6; i++) {
            for (int j = i; j <= 6; j++) {
                pieces.add(new DominoPiece(i, j));
            }
        }
        // Shuffle the pieces
        Collections.shuffle(pieces);
    }

    /*
    // Get how many pieces are left
    // returns the index of the last domino
     */

    public int getSizeOfBoneyard(){
        return pieces.indexOf(pieces.getLast());
    }

    /*
    // Get how many pieces are left with starting at 1
    // returns the index of the last domino plus 1
     */

    public int getSizeOfBoneYardStartingAtOne(){
        return pieces.indexOf(pieces.getLast()) + 1;
    }

    /*
    // Return 1 piece from Boneyard as well as remove it

     */

    public DominoPiece getPiece(){
        DominoPiece x = pieces.getFirst();
        pieces.removeFirst();
        return x;
    }

    /*
    // Check if boneyard is empty
    // Return true is boneyard is empty, false otherwise
     */

    public boolean isEmpty(){
        return pieces.isEmpty();
    }
}
