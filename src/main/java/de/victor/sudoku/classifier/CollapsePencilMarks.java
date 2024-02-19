package de.victor.sudoku.classifier;

import de.victor.sudoku.SudokuUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class CollapsePencilMarks implements SolvingTechnique{


 /** Searches the pencilmarks for single entries and writes them to the puzzle. Collapsed entries were deleted from markers.
 * After that the markers are updated and the procedure repeats until no cell can be collapsed anymore.
 *
 * @param puzzle sudoku puzzle as int[]
 * @param markers pencilmarks
 * @return true if pencilmarks altered else false
 */
    @Override
    public SolvingResult execute(int[] puzzle, HashMap<Integer, List<Integer>> markers) {

            var removedOnce = false;
            int addedNumbers = 0;

            boolean collapsing = true;

            while (collapsing) {
                collapsing = false;
                for (int i : markers.keySet()) {

                    if (markers.get(i).size() == 1) {
                        puzzle[i] = markers.get(i).get(0);
                        addedNumbers++;
                        var removed = eliminateNeighbourCandidates(i, puzzle[i], markers);
                        removedOnce = removedOnce || removed;
                        collapsing = true;

                    }

                }
            }

        for (int i = 0; i < 81; i++)
            if (markers.containsKey(i) && markers.get(i).size() <= 1)
                markers.remove(i);

        return new SolvingResult(
                puzzle,
                markers,
                addedNumbers,
                true
        );
    }

    protected boolean eliminateNeighbourCandidates(int idx, int number, HashMap<Integer, List<Integer>> markers) {

        List<Integer> neighbours = SudokuUtils.getNeighbourPositions(idx);
        return SudokuUtils.eliminateCandidateFromPositions(markers, number, neighbours);

    }


}
