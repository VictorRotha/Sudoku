package de.victor.sudoku.classifier.solvingtechs;

import de.victor.sudoku.SudokuUtils;
import de.victor.sudoku.classifier.SolvingResult;
import de.victor.sudoku.classifier.SolvingTechnique;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static de.victor.sudoku.SudokuUtils.eliminateCandidateFromPositions;

public class CandidateLines implements SolvingTechnique {

    /**
     * Looks for candidates in the same box who lies on the same line (row oe columns).
     * Removes the candidate from all other cell in that line.
     *
     * @param puzzle sudoku puzzle
     * @param markers pencilMarks
     * @return SolvingResult
     */
    @Override
    public SolvingResult execute(int[] puzzle, HashMap<Integer, List<Integer>> markers) {

        var removed = removeMarkers(markers);
        var added = 0;
        if (removed)
            added = new CollapsePencilMarks().execute(puzzle, markers).addedValues();


        return new SolvingResult(
                puzzle,
                markers,
                added,
                removed
        );
    }




    public boolean removeMarkers(HashMap<Integer, List<Integer>> markers) {

        boolean removed = true;
        boolean removedTotal = false;

        while (removed) {

            removed = false;

            for (int box = 0; box < 9; box++) {

                List<Integer> boxIndices = SudokuUtils.getIndicesFromAbbr("b" + box);
                HashMap<Integer, List<Integer>> indicesByNumber = SudokuUtils.getIndicesByNumber(boxIndices, markers);

                for (int number : indicesByNumber.keySet()) {
                    List<Integer> indices = indicesByNumber.get(number);

                    List<Integer> eliminateFrom = null;

                    int range = Collections.max(indices) - Collections.min(indices);

                    if (range <= 2)
                        eliminateFrom = SudokuUtils.getRowIndices(indices.get(0));
                    else if (areIndicesInSameColumn(indices))
                        eliminateFrom = SudokuUtils.getColumnIndices(indices.get(0));


                    if (eliminateFrom != null) {
                        eliminateFrom.removeAll(indices);
                        if (!eliminateFrom.isEmpty() && eliminateCandidateFromPositions(markers, number, eliminateFrom)) {
                            removed = true;
                            removedTotal = true;

                        }
                    }

                }

            }

        }

        return removedTotal;

    }

    protected boolean areIndicesInSameColumn(List<Integer> indices) {

        if (indices.size() == 1)
            return true;

        boolean sameCol = true;
        for (int i = 1; i < indices.size(); i++) {
            if ((indices.get(i) - indices.get(i-1)) % 9 != 0) {
                sameCol = false;
                break;
            }
        }

        return sameCol;
    }


}
