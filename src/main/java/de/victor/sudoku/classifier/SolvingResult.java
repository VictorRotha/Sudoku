package de.victor.sudoku.classifier;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public record SolvingResult (
        int[] puzzle,
        Map<Integer, List<Integer>> pencilMarks,
        int addedValues,
        boolean arePencilMarksAltered
) {

    @Override
    public String toString() {
        return String.format("puzzle=%s, pencilMarks=%s, addedValues=%s, arePencilMarksAltered=%s",
                Arrays.toString(puzzle), pencilMarks, addedValues, arePencilMarksAltered);
    }
}
