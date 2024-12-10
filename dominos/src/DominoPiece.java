/*
// Created by Alex Hartel for CS 351 project
// Player class for Domino Pieces, left and right values
// Created in February of 2024
// Finished Notes and Organization by number of inputs
*/

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.scene.input.MouseEvent;


public class DominoPiece {
    private int leftValue;
    private int rightValue;

    /*
    return left value of domino piece

     */

    public int getLeftValue(DominoPiece x){
        return x.leftValue;
    }

    /*
    return right value of domino piece

     */

    public int getRightValue(DominoPiece x){
        return  x.rightValue;
    }

    /*
    take in domino piece and print them

     */

    public String toStringDominoBoard(DominoPiece x) {
        return "[" + getLeftValue(x) + " " + getRightValue(x) + "]";
    }

    /*
    // takes in an index, creates a Pane for the dots to use with the domino
     */

    private Pane createDots(int value) {
        Pane dotsPane = new Pane();
        double radius = 5; // Radius of each dot

        // Coordinates for dots on a domino piece
        int[][][] positions = new int[][][]{
                {}, // 0 dots
                {{25, 50}}, // 1 dot
                {{15, 35}, {35, 65}}, // 2 dots
                {{15, 35}, {25, 50}, {35, 65}}, // 3 dots
                {{15, 35}, {15, 65}, {35, 35}, {35, 65}}, // 4 dots
                {{15, 35}, {15, 65}, {25, 50}, {35, 35}, {35, 65}}, // 5 dots
                {{15, 30}, {15, 50}, {15, 70}, {35, 30}, {35, 50}, {35, 70}} // 6 dots
        };

        // Ensure the value is within the range of the positions array
        if (value >= 0 && value < positions.length) {
            for (int[] pos : positions[value]) {
                Circle dot = new Circle(pos[0], pos[1], radius);
                dot.setFill(Color.BLACK); // Set the color of the dot
                dotsPane.getChildren().add(dot);
            }

        }

        return dotsPane;
    }

    /*
    returns a pane, used to create the rectangular shapes, the separator, and event handler.
     */

    public Pane drawDomino(int index) {
        Pane dominoPane = new Pane();
        Rectangle leftRect = new Rectangle(50, 100, Color.WHITE); // size and color
        Rectangle rightRect = new Rectangle(50, 100, Color.WHITE);
        rightRect.setX(50); // position the right rectangle

        Pane leftDots = createDots(leftValue);
        Pane rightDots = createDots(rightValue);

        // Add rectangles first so they are below the dots
        dominoPane.getChildren().addAll(leftRect, rightRect);

        Line separator = new Line(50, 0, 50, 100); // StartX, StartY, EndX, EndY
        separator.setStroke(Color.BLACK); // Set the color of the line

        // Event handler for domino click
        dominoPane.setOnMouseClicked((MouseEvent event) -> {
            if (DominoGameGUI.selectedDomino != null) {
                DominoGameGUI.selectedDomino.setStyle(""); // Remove border from previously selected domino
            }
            dominoPane.setStyle("-fx-border-color: black; -fx-border-width: 2;"); // Add border to this domino
            DominoGameGUI.selectedDomino = dominoPane;
            DominoGameGUI.intSelectedDomino = index;
        });

        // Add the dots on top of the rectangles
        leftDots.setLayoutX(0);
        rightDots.setLayoutX(50);
        dominoPane.getChildren().addAll(leftDots, rightDots, separator);

        return dominoPane;
    }

    /*
    Domino piece takes in two ints and returns a domino

     */

    public DominoPiece(int leftValue, int rightValue) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    /*
    takes in two ints and swaps them for the domino piece

     */

    public void DominoPieceSwap(int leftValue, int rightValue){
        this.leftValue = rightValue;
        this.rightValue = leftValue;
    }


}

