package de.victor.sudoku;


public class Main {

    public static void main(String[] args) {

        Sudoku sudoku = new Sudoku();

        int[] puzzle = sudoku.solvePuzzle(new int[81]);
        SudokuUtils.printSudoku(puzzle);
        System.out.println("is valid: " + SudokuUtils.isSudokuValid(puzzle));

        int[] level = sudoku.generateRandomLevel(puzzle, 25);
        SudokuUtils.printSudoku(level);

        int[] solvedLevel = sudoku.solvePuzzle(level);
        SudokuUtils.printSudoku(solvedLevel, level);
        System.out.println("is valid: " + SudokuUtils.isSudokuValid(solvedLevel));

    }
}
