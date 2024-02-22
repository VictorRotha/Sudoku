package de.victor.sudoku.classifier.solvingtechs;

import de.victor.sudoku.SudokuUtils;
import de.victor.sudoku.classifier.SolvingResult;
import de.victor.sudoku.classifier.SolvingTechnique;

import java.util.*;

public class DoublePairs implements SolvingTechnique {


    @Override
    public SolvingResult execute(int[] puzzle, HashMap<Integer, List<Integer>> markers) {

        var markersRemoved = removeMarkers(markers);

        var added = 0;
        if (markersRemoved) {
            var collapseResult = new CollapsePencilMarks().execute(puzzle, markers);
            added = collapseResult.addedValues;
        }


        return new SolvingResult(puzzle, markers, added, markersRemoved);
    }

    protected boolean removeMarkers(HashMap<Integer, List<Integer>> markers) {

        Map<Integer, HashMap<Integer, List<Integer>>> doubles = findCandidateDoublesInBoxes(markers);
        Map<Integer, List<Integer>> indicesToRemove = compareColumns(doubles);
        boolean removed = removeCandidatesFromIndices(indicesToRemove, markers);

        var rotatedMarkers = SudokuUtils.rotatePencilmarksRight(markers);

        doubles = findCandidateDoublesInBoxes(rotatedMarkers);
        indicesToRemove = compareColumns(doubles);
        boolean removedRotated = removeCandidatesFromIndices(indicesToRemove, markers);
        rotatedMarkers = SudokuUtils.rotatePencilmarksLeft(rotatedMarkers);

        for (int i : markers.keySet())
            markers.put(i, rotatedMarkers.get(i));

        return removed || removedRotated;
    }


    protected boolean removeCandidatesFromIndices(Map<Integer, List<Integer>> indicesToRemove, HashMap<Integer, List<Integer>> markers) {

        var result = false;
        for (int number : indicesToRemove.keySet()) {
          var removed = SudokuUtils.eliminateCandidateFromPositions(markers, number, indicesToRemove.get(number));
          result = result || removed;
        }

        return result;
    }

    /**
     * Searches for candidates, who only occurs twice in a box.
     * Returns the columns who contains the candidate, mapped to the box, mapped to the candidate.<br/>
     * example output:<br/>
     * {<br/>
     * 1 = {0 = (0, 2), 3 = (1, 2)},<br/>
     * 3 = {8 = (7,8 )},<br/>
     * }<br/>
     * candidate 1 in box 0 in column 0 and 2 and in box 3 in column 1 and 2<br/>
     * candidate 3 in box 1 in column 7 and 8<br/>
     *
     * @param markers pencilmarks
     * @return Map{number : {box : (col1, col2)}}
     */
    protected Map<Integer, HashMap<Integer, List<Integer>>> findCandidateDoublesInBoxes(HashMap<Integer, List<Integer>> markers) {

        HashMap<Integer, HashMap<Integer, List<Integer>>> doubles = new HashMap<>();

         for (int box = 0; box < 9; box++) {

             var indices = SudokuUtils.getIndicesFromAbbr("b" + box);

             var indicesByNumber = SudokuUtils.getIndicesByNumber(indices, markers);

             for (int n : indicesByNumber.keySet()) {
                 if (indicesByNumber.get(n).size() == 2) {
                     if (!doubles.containsKey(n))
                         doubles.put(n, new HashMap<>());
                     var c0 = indicesByNumber.get(n).get(0) % 9;
                     var c1 = indicesByNumber.get(n).get(1) % 9;
                     doubles.get(n).put(box, Arrays.asList(c0, c1));

                 }
             }
         }

         return doubles;
    }


    /**
     * Looks for candidates pairs in different boxes but same columns.
     * Returns the indices, where the number should be removed.
     *
     * @param doubles {candidate={box=(col1, col2}}

     * @return {number=(indices)}
     */
    protected Map<Integer, List<Integer>> compareColumns(Map<Integer, HashMap<Integer, List<Integer>>> doubles) {

        var result = new HashMap<Integer, List<Integer>>();

        for (int number : doubles.keySet()) {

            for (int i = 0; i < 8; i++) {
                if (!doubles.get(number).containsKey(i))
                    continue;

                for (int j = i + 1; j < 9; j++) {
                    if (!doubles.get(number).containsKey(j))
                        continue;

                    if ((j - i) % 3 == 0) {
                        var cols1 = doubles.get(number).get(i);
                        var cols2 = doubles.get(number).get(j);


                        if (cols1.equals(cols2)) {
                            var indices = new ArrayList<Integer>();
                            indices.addAll(SudokuUtils.getColumnIndices(cols1.get(0)));
                            indices.addAll(SudokuUtils.getColumnIndices(cols1.get(1)));
                            indices.removeAll(SudokuUtils.getIndicesFromAbbr("b" + i));
                            indices.removeAll(SudokuUtils.getIndicesFromAbbr("b" + j));

                            if (!result.containsKey(number))
                                result.put(number, new ArrayList<>());
                            result.get(number).addAll(indices);

                        }
                    }
                }
            }
        }

        return result;
    }
}
