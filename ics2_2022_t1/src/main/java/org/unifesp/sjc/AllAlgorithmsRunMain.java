package org.unifesp.sjc;

import org.unifesp.sjc.algorithms.Algorithms;
import org.unifesp.sjc.model.Board;

public class AllAlgorithmsRunMain {

    private static final int TOTAL_ATTEMPTS = 10_000;
    private static final int TOTAL_RUNS = 100;
    

    public static void main(String[] args) {
        int i = 0;
        
        var algorithms = new Algorithms(TOTAL_ATTEMPTS);

        var out = new StringBuffer("n,algorithm,time,states\n");
        while (i++ < TOTAL_RUNS) {            
            var board = Board.generate();
            var time = System.currentTimeMillis();
            int steps = 0;
            
            System.out.println("Run number " + i);

            System.out.println("Solving Board");
            System.out.println(board);

            System.out.println("Solving with depth search");
            time = System.currentTimeMillis();
            steps = 0;
            var depth = algorithms.depthFirst(board.cloneBoard());
            time = depth.isPresent() ? System.currentTimeMillis() - time : -1;
            steps = depth.isPresent() ? depth.get() : -1;
            out.append(String.format("%d,depth,%d,%d\n", System.currentTimeMillis(), time, steps));

            System.out.println("Solving with breadth search");
            time = System.currentTimeMillis();
            steps = 0;
            var breadth = algorithms.breadthFirst(board.cloneBoard());
            time = breadth.isPresent() ? System.currentTimeMillis() - time : -1;
            steps = breadth.isPresent() ? breadth.get() : -1;
            out.append(String.format("%d,breadth,%d,%d\n", System.currentTimeMillis(), time, steps));

            System.out.println("Solving with h1 search");
            time = System.currentTimeMillis();
            steps = 0;
            var h1 = algorithms.greedy(board.cloneBoard(), b -> board.h1());
            time = h1.isPresent() ? System.currentTimeMillis() - time : -1;
            steps = h1.isPresent() ? h1.get() : -1;
            out.append(String.format("%d,h1,%d,%d\n", System.currentTimeMillis(), time, steps));

            System.out.println("Solving with h2 search");
            time = System.currentTimeMillis();
            steps = 0;
            var h2 = algorithms.greedy(board.cloneBoard(), b -> board.h2());
            time = h2.isPresent() ? System.currentTimeMillis() - time : -1;
            steps = h2.isPresent() ? h2.get() : -1;
            out.append(String.format("%d,h2,%d,%d\n", System.currentTimeMillis(), time, steps));
            
//            System.out.println("Solving with h3 search");
//            time = System.currentTimeMillis();
//            steps = 0;
//            var h3 = algorithms.greedy(board.cloneBoard(), b -> board.h3());
//            time = h3.isPresent() ? System.currentTimeMillis() - time : -1;
//            steps = h3.isPresent() ? h3.get() : -1;
//            out.append(String.format("%d,h3,%d,%d\n", System.currentTimeMillis(), time, steps));
        }
        System.out.println(out.toString());
    }

}
