package de.victor.sudoku;



public class PuzzleResult {

    public int[] firstResult;
    public int[] secondResult;
    public int timesSolved = 0;

    public boolean hasSolution() {
        return timesSolved > 0;
    }

    public boolean hasSingleSolution() {
        return timesSolved == 1;
    }
}
