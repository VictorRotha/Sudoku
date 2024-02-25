package de.victor.sudoku.classifier.solvingtechs;

import de.victor.sudoku.SudokuUtils;
import de.victor.sudoku.classifier.SolvingResult;
import de.victor.sudoku.classifier.SolvingTechnique;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class NakedDoubles implements SolvingTechnique {


    /**
     * Searching for cells, that contains only the same two numbers.
     * If both cells in the same row, removes the numbers from the other cells in the row.<br/>
     * If both cells in the same column, removes the numbers from the other cells in the column.<br/>
     * If both cells in the same box, removes the numbers from the other cells in the box.<br/>
     *
     * @param puzzle sudoku puzzle
     * @param markers pencilMarks
     * @return SolvingResult
     */
    @Override
    public SolvingResult execute(int[] puzzle, HashMap<Integer, List<Integer>> markers) {

        List<String> queue = new ArrayList<>(Arrays.asList(
                "r0", "r1", "r2", "r3", "r4", "r5", "r6", "r7", "r8",
                "c0", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8",
                "b0", "b1", "b2", "b3", "b4", "b5", "b6", "b7", "b8" ));

        List<Integer> indices;
        int added = 0;

        var removedTotal = false;

        while (!queue.isEmpty()) {

            String line = queue.remove(0);

            indices = SudokuUtils.getIndicesFromAbbr(line);

            Integer[] removed = findNakedDoubles(indices, markers);
            removedTotal = removedTotal || removed.length > 0;

            if (removed.length > 0) {

                boolean collapse = false;
                for (int idx: removed) {
                    if (markers.containsKey(idx) && markers.get(idx).size() == 1) {
                        puzzle[idx] = markers.get(idx).get(0);
                        collapse = true;
                    }
                }

                if (collapse) {
                    added = new CollapsePencilMarks().execute(puzzle, markers).addedValues();
                    break;

                } else {

                    for (int idx : removed) {
                        int col = idx % 9;
                        int row = idx / 9;
                        int box = row / 3  * 3 + col % 3;

                        String[] lines = new String[] {"c" + col, "r" + row, "b" + box};
                        for (String l : lines) {
                            if (!queue.contains(l))
                                queue.add(l);
                        }

                    }
                }
            }


        }

        return new SolvingResult(puzzle, markers, added, removedTotal);

    }


    /**
     * Searches for a pair of cells, that contains only the same two numbers.
     * Removes the numbers from the other cells and write to pencilMarks
     *
     * @param indices cells to look in
     * @param markers pencilMarks
     * @return int[] of indices, from which a number is being removed
     */
    public Integer[] findNakedDoubles(List<Integer> indices, HashMap<Integer, List<Integer>> markers) {

        //get mutable subset of markers
        HashMap<Integer, List<Integer>> map = new HashMap<>();
        for (Integer idx: indices) {
            if (markers.containsKey(idx))
                map.put(idx, new ArrayList<>(markers.get(idx)));
        }

        HashMap<Integer, List<Integer>> newMap = SudokuUtils.findDoublesInSubMap(map);

        //compare
        List<Integer> removedFrom = new ArrayList<>();
        for (int idx : map.keySet()) {
            if (map.get(idx).size() > newMap.get(idx).size()) {
                removedFrom.add(idx);
                markers.put(idx, newMap.get(idx));
            }
        }

        Integer[] result = new Integer[removedFrom.size()];
        removedFrom.toArray(result);

        return result;

    }
}
