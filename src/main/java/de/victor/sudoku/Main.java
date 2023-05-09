package de.victor.sudoku;


import java.util.Arrays;


public class Main {

    public static void main(String[] args) {

        Sudoku sudoku = new Sudoku();

        int[] puzzle = sudoku.generatePuzzleGrid();

        SudokuUtils.printSudoku(puzzle);
        System.out.println("is valid: " + SudokuUtils.isSudokuValid(puzzle));


        int[] level1 = sudoku.generateValidLevel(puzzle);

        System.out.println("\nLevel 1");
        checkLevel(sudoku, puzzle, level1);

        int[] level2 = sudoku.generateRandomLevel(puzzle, 25);

//        int[] level2 = sudoku.generateValidLevel(puzzle);

        System.out.println("\nLevel 2");
        checkLevel(sudoku, puzzle, level2);

    }



    public static void checkLevel(Sudoku sudoku, int[] puzzle, int[] level) {

        SudokuUtils.printSudoku(level);

        PuzzleResult result = sudoku.solvePuzzleAndCheckForMultipleSolutions(level);

        if (!result.hasSolution()) {
            System.out.println("not solvable !");
            return;
        }

        System.out.println((result.hasSingleSolution()) ? "one solution" : "multiple solutions");

        SudokuUtils.printSudoku(result.firstResult);
        System.out.println("same as origin: " + Arrays.equals(puzzle, result.firstResult));
        System.out.println("is valid      : " + SudokuUtils.isSudokuValid(result.firstResult));


        if (result.secondResult != null) {
            SudokuUtils.printSudoku(result.secondResult);
            System.out.println("same as origin: " + Arrays.equals(puzzle, result.secondResult));
            System.out.println("is valid      : " + SudokuUtils.isSudokuValid(result.secondResult));

        }

    }
}
