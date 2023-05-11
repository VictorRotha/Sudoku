package de.victor.sudoku;


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








        int[] grid = sudoku.solvePuzzle(puzzle);

//        System.out.println(Arrays.toString(grid));

        SudokuUtils.printSudoku(grid);
        System.out.println("is valid: " + SudokuUtils.isSudokuValid(grid));

        checkLevel(sudoku, grid, puzzle);
        System.out.println();

        System.out.println(puzzleInfo(puzzle));

        Classifier classifier = new Classifier();
        classifier.setPuzzle(new Puzzle(grid, puzzle));

//        classifier.candidateLines(puzzle, null);

        classifier.solve(sudoku);
//
////        classifier.candidateLines(puzzle, null);


        System.out.println(puzzleInfo(puzzle));
        SudokuUtils.printSudoku(classifier.getPuzzle().puzzle);

        System.out.println("is valid " + SudokuUtils.isSudokuValid(puzzle));

        int[] solvedPuzzle = sudoku.solvePuzzle(puzzle);

        if (solvedPuzzle == null) {
            System.out.println("not solvable, original was \n");
            SudokuUtils.printSudoku(grid, classifier.getOriginalPuzzle());
        }

        else {
            System.out.println("solvable");
            SudokuUtils.printSudoku(solvedPuzzle, classifier.getOriginalPuzzle());
            System.out.println("is valid " + SudokuUtils.isSudokuValid(solvedPuzzle));
        }


    }

    public static String puzzleInfo(int[] puzzle) {
        int solved;

        List<Integer> cells = Arrays.stream(puzzle).filter(i -> i != 0).boxed().collect(Collectors.toList());
        solved = cells.size();

        return "The puzzle contains " + solved  + " solved cells and " + (81 - solved) + " zeros.";

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
