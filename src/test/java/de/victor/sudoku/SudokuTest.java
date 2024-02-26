package de.victor.sudoku;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SudokuTest {

    private Sudoku sudoku;

    @BeforeEach
    void setUp() {
        sudoku = new Sudoku();
    }

    @Test
    void checkForSingleSolution_TrueIfOnlyOneSolution() {

        int[] level = TestData.UNIQUE_SOLVABLE_PUZZLE_ONE;
        boolean result = sudoku.checkForSingleSolution(level);

        assertTrue(result);

        level = TestData.UNIQUE_SOLVABLE_PUZZLE_TWO;
        result = sudoku.checkForSingleSolution(level);

        assertTrue(result);

    }

    @Test
    void checkForSingleSolution_FalseIfMultipleSolutions() {
        int[] level = TestData.PUZZLE_WITH_MULTIPLE_SOLUTIONS_ONE;
        boolean result = sudoku.checkForSingleSolution(level);

        assertFalse(result);

    }

    @Test
    void checkForSingleSolution_FalseIfNoSolution() {
        int[] level = TestData.UNSOLVABLE_PUZZLE_ONE;
        boolean result = sudoku.checkForSingleSolution(level);

        assertFalse(result);

    }

    @Test
    void solvePuzzleAnCheckForMultipleSolutions_PUZZLE_WITH_MULT_SOLUTIONS() {

        int[] level = TestData.PUZZLE_WITH_MULTIPLE_SOLUTIONS_ONE;
        PuzzleResult result = sudoku.solvePuzzleAndCheckForMultipleSolutions(level);

        assertEquals(2, result.timesSolved);
        assertNotNull(result.firstResult);
        assertNotNull(result.secondResult);


    }

    @Test
    void solvePuzzleAnCheckForMultipleSolutions_PUZZLE_WITH_SINGLE_SOLUTION() {

        int[] level = TestData.UNIQUE_SOLVABLE_PUZZLE_ONE;
        PuzzleResult result = sudoku.solvePuzzleAndCheckForMultipleSolutions(level);

        assertEquals(1, result.timesSolved);
        assertNotNull(result.firstResult);
        assertNull(result.secondResult);


    }

    @Test
    void solvePuzzleAnCheckForMultipleSolutions_PUZZLE_NOT_SOLVABLE() {

        int[] level = TestData.UNSOLVABLE_PUZZLE_ONE;
        PuzzleResult result = sudoku.solvePuzzleAndCheckForMultipleSolutions(level);

        assertEquals(0, result.timesSolved);
        assertNull(result.firstResult);
        assertNull(result.secondResult);


    }

    @Test
    void solvePuzzle_WithEmptyGridReturnsNewValidMap() {

        int[] grid = sudoku.solvePuzzle(new int[81]);
        assertNotNull(grid);
        assertTrue(SudokuUtils.isSudokuValid(grid));


    }

    @Test
    void solvePuzzle_SolvableLevelReturnsValidSolution() {

        int[] level = TestData.UNIQUE_SOLVABLE_PUZZLE_ONE;
        int[] grid = sudoku.solvePuzzle(level);

        assertNotNull(grid);
        assertTrue(SudokuUtils.isSudokuValid(grid));
        assertTrue(SudokuUtils.isGridMatchesLevel(grid, level));

    }

    @Test
    void solvePuzzle_UnSolvableLevelReturnsNull() {

        int[] level = TestData.UNSOLVABLE_PUZZLE_ONE;
        int[] grid = sudoku.solvePuzzle(level);

        assertNull(grid);

    }

    @Test
    void generatePuzzleGrid_returnsValidPuzzleGrid() {

        int[] grid = sudoku.generatePuzzleGrid();

        assertTrue(SudokuUtils.isSudokuValid(grid));

    }

    @Test
    void generateValidLevel_puzzleHasSingleSolution() {

        int[] grid = TestData.VALID_GRID_ONE;

        int[] puzzle = sudoku.generateValidLevel(grid);

        PuzzleResult result = sudoku.solvePuzzleAndCheckForMultipleSolutions(puzzle);

        assertTrue(result.hasSolution());
        assertTrue(result.hasSingleSolution());

        puzzle = sudoku.generateValidLevel(grid);
        result = sudoku.solvePuzzleAndCheckForMultipleSolutions(puzzle);

        assertTrue(result.hasSolution());
        assertTrue(result.hasSingleSolution());


    }

    @Test
    void generateValidLevel2_puzzleHasSingleSolution() {

        int[] grid = TestData.VALID_GRID_ONE;

        int[] puzzle = sudoku.generateValidLevel2(grid);

        PuzzleResult result = sudoku.solvePuzzleAndCheckForMultipleSolutions(puzzle);

        assertTrue(result.hasSolution());
        assertTrue(result.hasSingleSolution());

        puzzle = sudoku.generateValidLevel2(grid);
        result = sudoku.solvePuzzleAndCheckForMultipleSolutions(puzzle);

        assertTrue(result.hasSolution());
        assertTrue(result.hasSingleSolution());

    }
}