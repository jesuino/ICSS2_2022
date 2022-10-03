package org.unifesp.sjc;

import java.util.Scanner;

import org.unifesp.sjc.model.Board;
import org.unifesp.sjc.model.Board.Cell;

public class FreeGameMain {

    public static void main(String[] args) {
        var board = Board.generate();
        System.out.println("Welcome to the game! Here's your board");
        try (Scanner sc = new Scanner(System.in)) {
            do {
                System.out.println(board);
                System.out.println("\n\nEnter the cell number to move");
                var cell = Cell.EMPTY;
                var in = sc.nextLine();
                try {
                    cell = Cell.fromN(in);
                    board.move(cell);
                } catch (Exception e) {
                    System.out.println("**** Invalid input! ****");
                    continue;
                }

            } while (!board.goal());
        }

        System.out.println("Congratulations - you won!!!!");

    }

}
