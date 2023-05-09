package de.victor.sudoku;



import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Sudoku {

    private final Random random;



    //grid: valid, fully solved puzzle
    //puzzle: an actual sudoku puzzle (which contains empty fields)
    //valid puzzle have only one solution

    public Sudoku() {

        random = new Random();

    }

    /**
     * Checks if a given level has more than one solution.
     *
     * @param puzzle the sudoku to be solved.
     * @return true if there is only one solution, false otherwise
     */
    public boolean checkForSingleSolution(int[] puzzle) {

        int[] grid1 = Arrays.copyOf(puzzle, puzzle.length);
        boolean result1 = solveOrdered(grid1, false);

        int[] grid2 = Arrays.copyOf(puzzle, puzzle.length);
        boolean result2 = solveOrdered(grid2, true);

        return (result1 && result2 && Arrays.equals(grid1, grid2));

    }


    /**
     * Generates a new, fully solved puzzle grid
     *
     * @return int[81]
     */
    public int[] generatePuzzleGrid() {

        int[] grid = new int[81];
        boolean result = solveRandom(grid);
        return (result) ? grid : null;

    }

    /**
     * Solves a sudoku puzzle.
     *
     * @param puzzle the sudoku to be solved.
     * @return a solved puzzle or null if the puzzle can't be solved.
     */
    public int[] solvePuzzle(int[] puzzle) {

        int[] grid = Arrays.copyOf(puzzle, puzzle.length);
        boolean result = solveOrdered(grid, false);
        return (result) ? grid : null;

    }

    /**
     * Solves a sudoku puzzle and return a PuzzleResult that contains one or two possible solutions.
     * If there are more than one solution, the algorithm stops after the second one.
     *
     * @param puzzle the sudoku to be solved.
     * @return a PuzzleResult object.
     */
    public PuzzleResult solvePuzzleAndCheckForMultipleSolutions(int[] puzzle) {
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

    /**
     * Generates a sudoku puzzle from a given, fully solved grid.
     * The generated puzzle has only one solution.
     * @param grid a fully solved sudoku grid
     * @return a puzzle with only one solution
     */
    public int[] generateValidLevel(int[] grid) {

        int[] mGrid = Arrays.copyOf(grid, grid.length);

        ArrayList<Integer> visited = new ArrayList<>();

        int zeros = 0;
        while (zeros < 64 && visited.size() + zeros < mGrid.length ) {

            int currentIdx = random.nextInt(mGrid.length);

            int currentValue = mGrid[currentIdx];
            if (visited.contains(currentIdx) || currentValue == 0)
                continue;

            mGrid[currentIdx] = 0;
            PuzzleResult puzzleResult = solvePuzzleAndCheckForMultipleSolutions(mGrid);
            if (!puzzleResult.hasSingleSolution()) {
                mGrid[currentIdx] = currentValue;
                visited.add(currentIdx);
            } else
                zeros++;

        }

        return mGrid;

    }

    /**
     * Generates a sudoku puzzle from a given, fully solved grid.
     * The generated puzzle has only one solution.
     * Alternative to generateValidLevel, but slower;
     * @param grid a fully solved sudoku grid
     * @return a puzzle with only one solution
     */
    public int[] generateValidLevel2(int[] grid) {

        int[] mGrid = Arrays.copyOf(grid, grid.length);

        List<Integer> indices = IntStream.rangeClosed(0, 80).boxed().collect(Collectors.toList());
        Collections.shuffle(indices);

        boolean hasSingleSolution;

        for (int currentIdx : indices) {

            int currentValue = mGrid[currentIdx];

            mGrid[currentIdx] = 0;

            hasSingleSolution = checkForSingleSolution(mGrid);

            if (!hasSingleSolution) {
                mGrid[currentIdx] = currentValue;
            }

        }

        return mGrid;

    }


    private boolean solveRandom(int[] puzzle) {
        
        int idx = 0;
        while (puzzle[idx] != 0) {
            if (++idx == puzzle.length)
                return true;
        }

        ArrayList<Integer> candidates = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

        while (candidates.size() > 0) {
            int candidate = candidates.get(random.nextInt(candidates.size()));
            candidates.remove((Integer) candidate);
            puzzle[idx] = candidate;
            if (SudokuUtils.isValidNumber(idx, candidate, puzzle) && solveRandom(puzzle)) {
                return true;
            }
        }

        puzzle[idx] = 0;
        return false;

    }

    private boolean solveOrdered(int[] puzzle, boolean invert) {

        int idx = 0;
        while (puzzle[idx] != 0) {
            if (++idx == puzzle.length)
                return true;
        }

        int candidate = (invert) ? 9 : 1;

        while (candidate > 0 && candidate < 10) {

            puzzle[idx] = candidate;
            if (SudokuUtils.isValidNumber(idx, candidate, puzzle) && solveOrdered(puzzle, invert)) {
                return true;
            }

            candidate = (invert) ? candidate - 1 : candidate + 1;

        }

        puzzle[idx] = 0;
        return false;

    }


    private boolean solveWithUniqueCheck(int[] puzzle, PuzzleResult result) {

        int idx = 0;
        while (puzzle[idx] != 0) {
            if (++idx == puzzle.length) {
                result.timesSolved++;
                break;
            }
        }

        if (idx < puzzle.length) {

            int candidate = 1;

            while (candidate < 10) {
                puzzle[idx] = candidate;
                if (SudokuUtils.isValidNumber(idx, candidate, puzzle) && solveWithUniqueCheck(puzzle, result)) {
                    return true;
                }

                candidate++;
            }

            puzzle[idx] = 0;
            return false;

        } else {

            if (result.timesSolved == 1) {
                result.firstResult = Arrays.copyOf(puzzle, puzzle.length);
                return false;
            } else {
                result.secondResult = puzzle;
                return true;
            }


        }


    }


}
