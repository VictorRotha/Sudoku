package de.victor.sudoku;

public class Puzzle {

    public int[] grid;
    public int[] puzzle;

    public Puzzle(int[] grid, int[] puzzle) {
        this.grid = grid;
        this.puzzle = puzzle;
    }

    public Puzzle(PuzzleResult result) {
        this(result.firstResult, result.secondResult);
    }




}
