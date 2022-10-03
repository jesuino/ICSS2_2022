package org.unifesp.sjc.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Stack;
import java.util.function.Function;

import org.unifesp.sjc.model.Board;
import org.unifesp.sjc.model.BoardStateTree;
import org.unifesp.sjc.model.BoardStateTree.Node;

public class Algorithms {

    private int max = 10_000;

    public Algorithms(int max) {
        this.max = max;
    }

    public Optional<Integer> depthFirst(Board board) {
        var boardStateTree = BoardStateTree.of(board);
        var stack = new Stack<Node>();
        var visitedBoards = new ArrayList<Board>();
        stack.push(boardStateTree.getRoot());
        int i = 0;
        while (i++ < max) {
            Node n = stack.pop();
            var _board = n.getBoard();
            if (_board.goal()) {
                return Optional.of(i);
            }
            visitedBoards.add(_board);
            BoardStateTree.openState(n);
            n.getChildren()
                    .stream()
                    .filter(_n -> !visitedBoards.contains(_n.getBoard()))
                    .forEach(stack::push);
        }
        return Optional.empty();
    }

    public Optional<Integer> breadthFirst(Board board) {
        var boardStateTree = BoardStateTree.of(board);
        var queue = new LinkedList<Node>();
        var visitedBoards = new ArrayList<Board>();
        queue.addFirst(boardStateTree.getRoot());
        int i = 0;
        do {
            var n = queue.remove();
            BoardStateTree.openState(n);
            n.getChildren()
                    .stream()
                    .filter(_n -> !visitedBoards.contains(_n.getBoard()))
                    .forEach(queue::add);
            var _board = n.getBoard();
            if (_board.goal()) {
                return Optional.of(i);
            }
            visitedBoards.add(_board);
        } while (i++ < max);
        return Optional.empty();
    }

    public Optional<Integer> greedy(Board board, Function<Board, Integer> heuristic) {
        var boardStateTree = BoardStateTree.of(board);
        var visitedBoards = new ArrayList<Board>();
        var n = boardStateTree.getRoot();
        var i = 0;
        var costTable = new HashMap<Node, Integer>();
        costTable.put(n, heuristic.apply(n.getBoard()));
        do {
            var _board = n.getBoard();
            if (_board.goal()) {
                return Optional.of(i);
            }
            BoardStateTree.openState(n);
            for (var _n : n.getChildren()) {
                if (!visitedBoards.contains(_n.getBoard())) {
                    var nh = heuristic.apply(_n.getBoard());
                    costTable.put(_n, nh);
                }
            }
            costTable.remove(n);
            n = costTable.entrySet()
                    .stream()
                    .reduce((e1, e2) -> e1.getValue() >= e2.getValue() ? e2 : e1)
                    .get()
                    .getKey();

        } while (i++ < max);
        return Optional.empty();
    }

}
