package org.unifesp.sjc;

import org.unifesp.sjc.algorithms.Algorithms;
import org.unifesp.sjc.model.Board;

public class AlgororithmRunMain {
    
    public static void main(String[] args) {
        var b = Board.generate();
        
        
        Algorithms algo = new Algorithms(10_000);
        
        
        algo.greedy(b.cloneBoard(), Board::h1).ifPresent(i -> System.out.println("h1: " + i));
        
        
    }

}
