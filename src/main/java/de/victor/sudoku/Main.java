package de.victor.sudoku;



public class Main {

    public static void main(String[] args) {

        Sudoku sudoku = new Sudoku();

        int[] puzzle = sudoku.solvePuzzle(new int[81]);
        SudokuUtils.printSudoku(puzzle);
        System.out.println("is valid: " + SudokuUtils.isSudokuValid(puzzle));

        int[] level = sudoku.generateRandomLevel(puzzle, 25);
        SudokuUtils.printSudoku(level);

        PuzzleResult result = sudoku.solvePuzzleAnCheckForMultipleSolutions(level);

        System.out.println("number of solutions: " + result.timesSolved);

        SudokuUtils.printSudoku(result.firstResult);
        SudokuUtils.printSudoku(result.secondResult);


    }
}
