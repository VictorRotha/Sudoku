package de.victor.sudoku;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class TestUtils {

    public static HashMap<Integer, List<Integer>> deepCopyPencilMarks(HashMap<Integer, List<Integer>> markers) {

        return new HashMap<>(markers.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> List.copyOf(e.getValue()))));

    }


    @Test
    void testDeepCopyPencilMarks_returnsNewInstance() {
        var markers = SudokuUtils.updatePencilMarks(TestData.UNIQUE_SOLVABLE_PUZZLE_ONE, null);

        assertNotSame(markers, deepCopyPencilMarks(markers));

    }

    @Test
    void testDeepCopyPencilMarks_isDeepCopy() {

        var markers = SudokuUtils.updatePencilMarks(TestData.UNIQUE_SOLVABLE_PUZZLE_ONE, null);
        var dpcopy = deepCopyPencilMarks(markers);

        for (int i: markers.keySet())
            assertNotSame(markers.get(i), dpcopy.get(i));

    }

    @Test
    void testDeepCopyPencilMarks_containsSameValues() {
        var markers = SudokuUtils.updatePencilMarks(TestData.UNIQUE_SOLVABLE_PUZZLE_ONE, null);
        assertEquals(markers, deepCopyPencilMarks(markers));

    }


}
