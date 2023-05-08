package de.victor.sudoku;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Sudoku {

    private final Random random;


    public Sudoku() {

        random = new Random();

    }

    /**
     * Checks if a given level has more than one solution.
     *
     * @param level the sudoku to be solved.
     * @return true if there is only one solution, false otherwise
     */
    public boolean checkForSingleSolution(int[] level) {

        int[] grid1 = Arrays.copyOf(level, level.length);
        boolean result1 = solveOrdered(grid1, false);

        int[] grid2 = Arrays.copyOf(level, level.length);
        boolean result2 = solveOrdered(grid2, true);

        return (result1 && result2 && Arrays.equals(grid1, grid2));

    }


    /**
     * Solves a sudoku puzzle.
     *
     * @param puzzle the sudoku to be solved. Pass a new int[81] to generate a new sudoku grid.
     * @return a solved puzzle or null if the puzzle can't be solved.
     */
    public int[] solvePuzzle(int[] puzzle) {

        int[] grid = Arrays.copyOf(puzzle, puzzle.length);
        boolean result = solve(grid);
        return (result) ? grid : null;

    }

    /**
     * Solves a sudoku puzzle and return a PuzzleResult that contains one or two possible solutions.
     * If there are more than one solution, the algorithm stops after the second.
     *
     * @param puzzle the sudoku to be solved.
     * @return a PuzzleResult object.
     */
    public PuzzleResult solvePuzzleAnCheckForMultipleSolutions(int[] puzzle) {
        int[] grid = Arrays.copyOf(puzzle, puzzle.length);
        PuzzleResult result = new PuzzleResult();
        solveWithUniqueCheck(grid, result);

        return result;

    }


    /**
     * Generates a random sudoku puzzle from a given, fully solved grid.
     * The generated puzzle can have more than one possible solutions
     * @param grid a fully solved sudoku grid
     * @param n number of cells that should be revealed.
     * @return a puzzle with n revealed cells
     */
    public int[] generateRandomLevel(int[] grid, int n) {

        int[] level = new int[81];

        while (n > 0) {

            int idx = random.nextInt(grid.length);
            if (level[idx] == 0) {
                n--;
                level[idx] = grid[idx];
            }
        }

        return level;

    }


    private boolean solve(int[] grid) {
        
        int idx = 0;
        while (grid[idx] != 0) {
            if (++idx == grid.length)
                return true;
        }

        ArrayList<Integer> candidates = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

        while (candidates.size() > 0) {
            int candidate = candidates.get(random.nextInt(candidates.size()));
            candidates.remove((Integer) candidate);
            grid[idx] = candidate;
            if (SudokuUtils.isValidNumber(idx, candidate, grid) && solve(grid)) {
                return true;
            }
        }

        grid[idx] = 0;
        return false;

    }

    private boolean solveOrdered(int[] grid, boolean invert) {

        int idx = 0;
        while (grid[idx] != 0) {
            if (++idx == grid.length)
                return true;
        }

        ArrayList<Integer> candidates = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

        while (candidates.size() > 0) {
            int candidate = (invert) ? candidates.get(candidates.size()-1) : candidates.get(0);
            candidates.remove((Integer) candidate);
            grid[idx] = candidate;
            if (SudokuUtils.isValidNumber(idx, candidate, grid) && solveOrdered(grid, invert)) {
                return true;
            }
        }

        grid[idx] = 0;
        return false;

    }


    private boolean solveWithUniqueCheck(int[] grid, PuzzleResult result) {

        int idx = 0;
        while (grid[idx] != 0) {
            if (++idx == grid.length) {
                result.timesSolved++;
                break;
            }
        }

        if (idx < grid.length) {

            ArrayList<Integer> candidates = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

            while (candidates.size() > 0) {
                int candidate = candidates.get(random.nextInt(candidates.size()));
                candidates.remove((Integer) candidate);
                grid[idx] = candidate;
                if (SudokuUtils.isValidNumber(idx, candidate, grid) && solveWithUniqueCheck(grid, result)) {
                    return true;
                }
            }

            grid[idx] = 0;
            return false;

        } else {

            if (result.timesSolved == 1) {
                result.firstResult = Arrays.copyOf(grid, grid.length);
                return false;
            } else {
                result.secondResult = grid;
                return true;
            }


        }




    }


}
