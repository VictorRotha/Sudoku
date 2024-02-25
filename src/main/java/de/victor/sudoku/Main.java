package de.victor.sudoku;


import de.victor.sudoku.classifier.Classifier;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class Main {

    public static void main(String[] args) {

        Sudoku sudoku = new Sudoku();

//        int[] puzzle = sudoku.generatePuzzleGrid();

//        int[] level1 = sudoku.generateValidLevel(puzzle);


//
//        UNIQUE_SOLVABLE_PUZZLE_ONE
//        int[] puzzle = new int[]{
//                9, 8, 1, 0, 0, 3, 0, 4, 0,
//                0, 0, 0, 0, 7, 9, 2, 5, 0,
//                0, 7, 0, 1, 0, 6, 0, 8, 3,
//                0, 9, 0, 4, 0, 7, 5, 0, 2,
//                0, 0, 8, 0, 1, 0, 7, 0, 0,
//                7, 0, 3, 6, 0, 5, 0, 1, 0,
//                3, 1, 0, 7, 0, 4, 0, 9, 0,
//                0, 6, 9, 2, 3, 0, 0, 0, 0,
//                0, 5, 0, 9, 0, 0, 3, 2, 4
//        };

//        UNIQUE_SOLVABLE_PUZZLE_TWO
//        int[] puzzle = new int[]{
//                6, 0, 7, 0, 4, 0, 0, 3, 1,
//                0, 0, 0, 0, 6, 0, 0, 0, 0,
//                0, 2, 3, 5, 0, 0, 0, 0, 0,
//                0, 3, 0, 0, 0, 9, 0, 4, 0,
//                0, 0, 8, 0, 0, 0, 3, 0, 0,
//                0, 5, 0, 6, 0, 0, 0, 8, 0,
//                0, 0, 0, 0, 0, 8, 1, 6, 0,
//                0, 0, 0, 0, 1, 0, 0, 0, 0,
//                1, 7, 0, 0, 2, 0, 9, 0, 8
//        };


        int[] puzzle = new int[]{
                6,0,0,0,5,0,0,9,0,
                0,0,0,0,0,0,0,4,7,
                3,0,0,4,7,0,0,0,0,
                0,0,0,0,0,6,5,0,4,
                0,1,0,5,2,9,0,6,0,
                7,0,5,3,0,0,0,0,0,
                0,0,0,0,3,4,0,0,2,
                8,3,0,0,0,0,0,0,0,
                0,7,0,0,8,0,0,0,9
        };


        System.out.println("PUZZLE");
        SudokuUtils.printSudoku(puzzle);

        int[] solvedPuzzle  = sudoku.solvePuzzle(puzzle);
        System.out.println("\nSOLVED PUZZLE");
        SudokuUtils.printSudoku(solvedPuzzle, puzzle);
        System.out.println("\nis valid: " + SudokuUtils.isSudokuValid(solvedPuzzle));

        checkForMultipleResults(sudoku, solvedPuzzle, puzzle);

        System.out.println();
        System.out.println(puzzleInfo(puzzle));


        Classifier classifier = new Classifier();
        var result = classifier.solve(new Puzzle(puzzle, solvedPuzzle));

        System.out.println("\nCLASSIFIER PUZZLE RESULT");
        System.out.println(result);

    }

    public static String puzzleInfo(int[] puzzle) {
        int solved;

        List<Integer> cells = Arrays.stream(puzzle).filter(i -> i != 0).boxed().collect(Collectors.toList());
        solved = cells.size();

        return "The puzzle contains " + solved  + " solved cells and " + (81 - solved) + " zeros.";

    }



    public static void checkForMultipleResults(Sudoku sudoku, int[] solvedPuzzle, int[] puzzle) {

//        SudokuUtils.printSudoku(puzzle);

        PuzzleResult result = sudoku.solvePuzzleAndCheckForMultipleSolutions(puzzle);

        if (!result.hasSolution()) {
            System.out.println("not solvable !");
            return;
        }

        System.out.println((result.hasSingleSolution()) ? "one solution" : "multiple solutions");

//        SudokuUtils.printSudoku(result.firstResult);
        System.out.println("SOLUTION 1:");
        System.out.println("    same as origin: " + Arrays.equals(solvedPuzzle, result.firstResult));
        System.out.println("    is valid      : " + SudokuUtils.isSudokuValid(result.firstResult));


        if (result.secondResult != null) {
//            SudokuUtils.printSudoku(result.secondResult);
            System.out.println("SOLUTION 2:");
            System.out.println("    same as origin: " + Arrays.equals(solvedPuzzle, result.secondResult));
            System.out.println("    is valid      : " + SudokuUtils.isSudokuValid(result.secondResult));

        }

    }
}
