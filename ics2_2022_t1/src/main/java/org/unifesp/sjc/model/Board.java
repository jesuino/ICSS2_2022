package org.unifesp.sjc.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Stream;

public class Board {

    private static int ROWS = 3;
    private static int COLS = 3;

    record Position(int x, int y) {

        Position left() {
            return new Position(x - 1, y);
        }

        Position right() {
            return new Position(x + 1, y);
        }

        Position top() {
            return new Position(x, y + 1);
        }

        Position bottom() {
            return new Position(x, y - 1);
        }

    }

    public static enum Cell {

        EMPTY(0, 0),
        N1(0, 1),
        N2(0, 2),
        N3(1, 0),
        N4(1, 1),
        N5(1, 2),
        N6(2, 0),
        N7(2, 1),
        N8(2, 2);

        int goalX, goalY;

        private Cell(int goalX, int goalY) {
            this.goalX = goalX;
            this.goalY = goalY;
        }

        @Override
        public String toString() {
            if (this == EMPTY) {
                return " ";
            } else {
                return this.name().replace("N", "");
            }

        }

        public static Cell fromN(String n) {
            return valueOf("N" + n);
        }

        public Position getGoalPosition() {
            return new Position(goalX, goalY);
        }

    }

    public Cell boardState[][];

    private Board(Cell boardState[][]) {
        this.boardState = boardState;

    }

    public static Board generate() {
        var cellStates = new Stack<Cell>();
        Arrays.stream(Cell.values()).forEach(cellStates::add);
        Collections.shuffle(cellStates);
        var boardState = new Cell[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            boardState[i] = new Cell[ROWS];
            for (int j = 0; j < COLS; j++) {
                boardState[i][j] = cellStates.pop();
            }
        }
        return new Board(boardState);
    }

    public static Board _withState(Cell[][] state) {
        return new Board(state);
    }

    public Cell[][] getBoardState() {
        return boardState;
    }

    public void move(Cell cell) {
        var currentPos = findPosition(cell);
        var futurePos = checkMove(cell).orElseThrow(() -> new IllegalArgumentException("Can't move to this position"));

        boardState[currentPos.x()][currentPos.y()] = Cell.EMPTY;
        boardState[futurePos.x()][futurePos.y()] = cell;

        // TODO: Check consistency?
    }

    public Board previewMove(Cell cell) {
        var clone = cloneBoard();
        clone.move(cell);
        return clone;
    }

    public List<Board> possibleStates() {
        var result = new ArrayList<Board>();
        var blankPos = findPosition(Cell.EMPTY);
        getCell(blankPos.bottom()).ifPresent(c -> result.add(previewMove(c)));
        getCell(blankPos.left()).ifPresent(c -> result.add(previewMove(c)));
        getCell(blankPos.right()).ifPresent(c -> result.add(previewMove(c)));
        getCell(blankPos.top()).ifPresent(c -> result.add(previewMove(c)));
        return result;
    }

    public Optional<Cell> getCell(int x, int y) {
        if (x < ROWS && x >= 0 && y < COLS && y >= 0) {
            return Optional.of(boardState[x][y]);
        }
        return Optional.empty();
    }

    public Optional<Cell> getCell(Position pos) {
        return getCell(pos.x, pos.y);
    }

    public Optional<Position> checkMove(Cell cell) {
        var pos = findPosition(cell);
        var blankPos = findPosition(Cell.EMPTY);
        if (pos.bottom().equals(blankPos)) {
            return Optional.of(pos.bottom());
        }

        if (pos.top().equals(blankPos)) {
            return Optional.of(pos.top());
        }

        if (pos.left().equals(blankPos)) {
            return Optional.of(pos.left());
        }

        if (pos.right().equals(blankPos)) {
            return Optional.of(pos.right());
        }

        return Optional.empty();
    }

    public Position findPosition(Cell cell) {
        for (int i = 0; i < boardState.length; i++) {
            for (int j = 0; j < boardState.length; j++) {
                if (boardState[i][j] == cell) {
                    return new Position(i, j);
                }
            }
        }
        throw new IllegalArgumentException("Something is definitively wrong");
    }

    public String toString() {
        var sb = new StringBuffer();
        sb.append(" --------- \n");
        for (int i = 0; i < boardState.length; i++) {
            sb.append("|");
            for (int j = 0; j < boardState[i].length; j++) {
                sb.append(" " + boardState[i][j] + " ");
            }
            sb.append("|\n");
        }
        sb.append(" --------- \n");
        return sb.toString();
    }

    public boolean goal() {
        return Stream.of(Cell.values()).allMatch(this::isOnGoal);
    }

    public boolean isOnGoal(Cell cell) {
        return boardState[cell.goalX][cell.goalY] == cell;
    }

    public Board cloneBoard() {
        var stateClone = new Cell[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                stateClone[i][j] = boardState[i][j];
            }
        }
        return _withState(stateClone);
    }

    public int stepsToTarget(Cell cell) {
        var pos = findPosition(cell);
        return Math.abs(pos.x() - cell.goalX) + Math.abs(pos.y() - cell.goalY);
    }

    /**
     * Calculates Heuristic h1 (number of nodes not in their position)
     * @return
     */
    public int h1() {
        Long l = Arrays.stream(Cell.values())
                .filter(c -> !findPosition(c).equals(c.getGoalPosition()))
                .count();
        return l.intValue();
    }

    /**
     * Calculates Heuristic h2 (sum of all nodes steps until right position)
     * @return
     */
    public int h2() {
        return Arrays.stream(Cell.values())
                .mapToInt(this::stepsToTarget)
                .sum();
    }

    /**
     * Calculates Heuristic h3 (TBD)
     * @return
     */
    public int h3() {
        return h1() + h2();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode(boardState);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Board other = (Board) obj;
        return Arrays.deepEquals(boardState, other.boardState);
    }

}
