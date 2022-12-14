package de.victor.sudoku;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Sudoku {

    private Random random;

    public Sudoku() {

        random = new Random();

    }

    /**
     * Solves a sudoku puzzle.
     *
     * @param puzzle the sudoku to be solved. Pass a new int[81] to generate a new sudoku grid.
     * @return a solved puzzle or null if the puzzle can't be solved.
     */
    public int[] solveSudoku(int[] puzzle) {

        int[] grid = Arrays.copyOf(puzzle, puzzle.length);

        boolean result = solve(0, grid, puzzle);

        return (result) ? grid : null;

    }

    public int[] generateSudoku() {

        return solveSudoku(new int[81]);

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


    private boolean solve(int idx, int[] grid, int[] level) {

        if (idx == grid.length) return true;

        ArrayList<Integer> candidates = new ArrayList<>();

        if (level[idx] != 0)
            candidates.add(level[idx]);
        else
            candidates.addAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

        while (candidates.size() > 0) {
            int candidate = candidates.get(random.nextInt(candidates.size()));
            candidates.remove((Integer) candidate);
            grid[idx] = candidate;
            if (SudokuUtils.isValidNumber(idx, candidate, grid) && solve(idx + 1, grid, level)) {
                return true;
            }
        }

        grid[idx] = level[idx];
        return false;
    }
}
