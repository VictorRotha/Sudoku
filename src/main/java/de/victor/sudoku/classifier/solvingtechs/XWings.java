package de.victor.sudoku.classifier.solvingtechs;

import de.victor.sudoku.SudokuUtils;
import de.victor.sudoku.classifier.SolvingResult;
import de.victor.sudoku.classifier.SolvingTechnique;

import java.util.HashMap;
import java.util.List;

public class XWings implements SolvingTechnique {


    @Override
    public SolvingResult execute(int[] puzzle, HashMap<Integer, List<Integer>> markers) {

        var altered = xWingRemoveMarkers(markers);

        var rotatedMarkers = SudokuUtils.rotatePencilmarksRight(markers);
        altered = (altered) ? altered :  xWingRemoveMarkers(rotatedMarkers);
        rotatedMarkers = SudokuUtils.rotatePencilmarksLeft(rotatedMarkers);

        System.out.println("XWing markers removed " + altered);

        if (altered) {
            var collapseResult = new CollapsePencilMarks().execute(puzzle, rotatedMarkers);
            return new SolvingResult(
                    collapseResult.puzzle(),
                    collapseResult.pencilMarks(),
                    collapseResult.addedValues(),
                    altered);
           }

        return new SolvingResult(puzzle, markers, 0, false);


    }

    /**
     * Looks for XWings and removes markers from pencilMarks
     * @param markers pencilMarks
     * @return true if markers altered, else false
     */
    boolean xWingRemoveMarkers(HashMap<Integer, List<Integer>> markers) {

        HashMap<Integer, HashMap<Integer, List<Integer>>> doubles = SudokuUtils.findCandidatePairInRow(markers);
        return xWingCompareRows(doubles, markers);

    }

    /**
     * Compares rows and looks for two candidates in different rows but same columns.
     * If found, the candidate will be removed from other pencilmark rows in both columns
     *
     * @param doubles {row={candidate=(col1, col2}}
     * @param markers pencilMarks
     * @return true if any candidate was removed, else false
     */
    protected boolean xWingCompareRows(HashMap<Integer, HashMap<Integer, List<Integer>>> doubles, HashMap<Integer, List<Integer>> markers) {

        boolean removed = false;
        for (int row = 0; row < 8; row++) {
            if (!doubles.containsKey(row))
                continue;
            for (int n : doubles.get(row).keySet()) {
                for (int row2 = row + 1; row2 < 9; row2++) {
                    if (!doubles.containsKey(row2))
                        continue;
                    if (!doubles.get(row2).containsKey(n))
                        continue;
                    if (doubles.get(row2).get(n).equals(doubles.get(row).get(n))) {

                        var cols = doubles.get(row).get(n);
                        var indices = SudokuUtils.getColumnIndices(doubles.get(row).get(n).get(0));
                        indices.addAll(SudokuUtils.getColumnIndices(doubles.get(row).get(n).get(1)));

                        indices.removeAll(SudokuUtils.getRowIndices(row * 9 + cols.get(0)));
                        indices.removeAll(SudokuUtils.getRowIndices(row2 * 9 + cols.get(0)));

                        if ( SudokuUtils.eliminateCandidateFromPositions(markers, n, indices))
                            removed = true;

                    }
                }
            }
        }
        return removed;
    }

}
