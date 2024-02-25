package de.victor.sudoku.classifier.solvingtechs;

import de.victor.sudoku.SudokuUtils;
import de.victor.sudoku.classifier.SolvingResult;
import de.victor.sudoku.classifier.SolvingTechnique;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HiddenDoubles implements SolvingTechnique {


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

            Integer[] removed = findHiddenDoubles(indices, markers);
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


    public Integer[] findHiddenDoubles(List<Integer> indices, HashMap<Integer, List<Integer>> markers) {

        HashMap<Integer, List<Integer>> indicesByNumbers = SudokuUtils.getIndicesByNumber(indices, markers);

        HashMap<Integer, List<Integer>> newIndisByNumbers = SudokuUtils.findDoublesInSubMap(indicesByNumbers);

        HashMap<Integer, List<Integer>> newMap = new HashMap<>();

        for (Integer number : newIndisByNumbers.keySet()) {
            for (Integer idx : newIndisByNumbers.get(number)) {
                if (!newMap.containsKey(idx))
                    newMap.put(idx, new ArrayList<>());
                newMap.get(idx).add(number);
            }
        }

        List<Integer> removedFrom = new ArrayList<>();
        for (Integer idx : newMap.keySet()) {
            if (newMap.get(idx).size() < markers.get(idx).size()) {
                removedFrom.add(idx);
                markers.put(idx, newMap.get(idx));
            }
        }

        if (!removedFrom.isEmpty())
            System.out.println("removed markers from " + removedFrom);

        Integer[] result = new Integer[removedFrom.size()];
        removedFrom.toArray(result);

        return result;


    }


}
