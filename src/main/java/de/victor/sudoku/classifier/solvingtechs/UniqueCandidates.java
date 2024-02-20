package de.victor.sudoku.classifier.solvingtechs;

import de.victor.sudoku.SudokuUtils;
import de.victor.sudoku.classifier.SolvingResult;
import de.victor.sudoku.classifier.SolvingTechnique;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UniqueCandidates implements SolvingTechnique {

    /**
     * finds candidates that appears only once in row, column or box
     * @param puzzle sudoku puzzle as int[]
     * @return SolvingResult
     */
    @Override
    public SolvingResult execute(int[] puzzle, HashMap<Integer, List<Integer>> markers) {

        int addedNumbers = 1;
        int totalAdded = 0;

        while (addedNumbers > 0) {

            addedNumbers = eliminateUniques(puzzle, markers);
            totalAdded += addedNumbers;

        }

        return new SolvingResult(puzzle, markers, totalAdded, totalAdded > 0);
    }


    /**
     * Looks in all rows, columns and boxes for candidates who appear only once in this subset.
     *
     * @param puzzle sudoku puzzle
     * @param markers pencilmarks
     * @return count of numbers added to the puzzle
     */
    protected int eliminateUniques(int[] puzzle, HashMap<Integer, List<Integer>> markers) {

        int addedNumbers = 0;

        for (int row = 0; row < 9; row ++) {
            var cells = SudokuUtils.getRowIndices(row * 9);
            addedNumbers += findUniqueCandidates(puzzle, cells, markers);
        }


        for (int col = 0; col < 9; col++) {
            var cells = SudokuUtils.getColumnIndices(col);
            addedNumbers += findUniqueCandidates(puzzle, cells, markers);

        }

        for (int row = 0; row < 9; row+=3) {
            for (int col = 0; col < 9; col+=3) {
                List<Integer> cells = SudokuUtils.getBoxIndices(row * 9 + col);
                addedNumbers += findUniqueCandidates(puzzle, cells, markers);
            }
        }

        return addedNumbers;

    }

    /**
     * Looks for candidates who appear only once in a given subset of puzzle and updates the puzzle and pencilmarks.
     *
     * @param puzzle sudoku puzzle
     * @param cells subset as list of indices
     * @param markers pencilmarks
     * @return count of numbers added to the puzzle
     */
    protected int findUniqueCandidates(int[] puzzle, List<Integer> cells, HashMap<Integer, List<Integer>> markers) {

        int addedNumbers = 0;

        Map<Integer, List<Integer>> indicesByNumbers = SudokuUtils.getIndicesByNumber(cells, markers);

        for (int number : indicesByNumbers.keySet()) {

            if (indicesByNumbers.get(number).size() == 1) {
                int idx = indicesByNumbers.get(number).get(0);
                puzzle[idx] = number;
                markers.remove(idx);
                addedNumbers++;
            }
        }

        return addedNumbers;


    }



}
