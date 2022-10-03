package org.unifesp.sjc.model;

import java.util.HashSet;
import java.util.Set;

public class BoardStateTree {
    

    private Node root;

    public static BoardStateTree of(Board rootBoard) {
        var tree = new BoardStateTree();
        tree.root = Node.create(null, rootBoard);
        return tree;
    }

    public static void openState(Node node) {
        for (var board : node.board.possibleStates()) {
            var n = Node.create(node, board);
            node.children.add(n);
        }
    }

    public static class Node {

        private Board board;
        private Node parent;
        private Set<Node> children;

        static Node create(Node parent, Board board) {
            var n = new Node();
            n.board = board;
            n.parent = parent;
            n.children = new HashSet<>();
            return n;
        }

        public Board getBoard() {
            return board;
        }

        public Set<Node> getChildren() {
            return children;
        }
    }

    public Node getRoot() {
        return root;
    }
}
