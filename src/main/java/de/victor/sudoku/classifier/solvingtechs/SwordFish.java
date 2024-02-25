package de.victor.sudoku.classifier.solvingtechs;

import de.victor.sudoku.SudokuUtils;
import de.victor.sudoku.classifier.SolvingResult;
import de.victor.sudoku.classifier.SolvingTechnique;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class SwordFish implements SolvingTechnique {

     @Override
     public SolvingResult execute(int[] puzzle, HashMap<Integer, List<Integer>> markers) {

        var altered = swordfishRemoveMarkers(markers);

        var rotatedMarkers = SudokuUtils.rotatePencilmarksRight(markers);
        altered = (altered) ? altered :  swordfishRemoveMarkers(rotatedMarkers);
        rotatedMarkers = SudokuUtils.rotatePencilmarksLeft(rotatedMarkers);

        for (int i : markers.keySet())
            markers.put(i, rotatedMarkers.get(i));

        if (altered) {
            var collapseResult = new CollapsePencilMarks().execute(puzzle, markers);
            return new SolvingResult(
                    collapseResult.puzzle(),
                    collapseResult.pencilMarks(),
                    collapseResult.addedValues(),
                    altered
            );
        }

        return new SolvingResult(puzzle, markers, 0, false);

    }

    /**
     * Looks for SwordFish and removes markers from pencilMarks
     * @param markers pencilMarks
     * @return true if markers altered, else false
     */
    boolean swordfishRemoveMarkers(HashMap<Integer, List<Integer>> markers) {

        HashMap<Integer, HashMap<Integer, List<Integer>>> doubles = SudokuUtils.findCandidatePairInRow(markers);
        return swordfishCompareRows(doubles, markers);

    }

    /**
     * Compares rows and looks for candidate double in three different rows with three overlapping columns.
     * If found, the candidate will be removed from other pencilmark rows in all three columns
     *
     * @param doubles {row={candidate=(col1, col2}}
     * @param markers pencilMarks
     * @return true if any candidate was removed, else false
     */
    protected boolean swordfishCompareRows(HashMap<Integer, HashMap<Integer, List<Integer>>> doubles, HashMap<Integer, List<Integer>> markers) {
        boolean removed = false;
        for (int row = 0; row < 7; row++) {
            if (!doubles.containsKey(row))
                continue;
            for (int n : doubles.get(row).keySet()) {
                for (int row2 = row + 1; row2 < 8; row2++) {
                    if (!doubles.containsKey(row2) || !doubles.get(row2).containsKey(n))
                        continue;

                    var total = new HashSet<>(doubles.get(row).get(n));
                    total.addAll(doubles.get(row2).get(n));
                    if (total.size() == 3) {
                        //overlapp 2 rows
                        for (int row3 = row2 + 1; row3 < 9; row3++) {
                            if (!doubles.containsKey(row3) || !doubles.get(row3).containsKey(n))
                                continue;
                            if (total.contains(doubles.get(row3).get(n).get(0)) && total.contains(doubles.get(row3).get(n).get(1))) {

                                var indices = new ArrayList<Integer>();
                                for (int col : total)
                                    indices.addAll(SudokuUtils.getColumnIndices(col));

                                indices.removeAll(SudokuUtils.getRowIndices(row * 9));
                                indices.removeAll(SudokuUtils.getRowIndices(row2 * 9 ));
                                indices.removeAll(SudokuUtils.getRowIndices(row3 * 9 ));

                                if (SudokuUtils.eliminateCandidateFromPositions(markers, n, indices))
                                    removed = true;
                            }

                        }
                    }
                }
            }
        }

        return removed;
    }


}
