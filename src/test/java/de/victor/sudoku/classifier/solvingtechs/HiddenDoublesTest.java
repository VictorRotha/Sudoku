package de.victor.sudoku.classifier.solvingtechs;

import de.victor.sudoku.SudokuUtils;
import de.victor.sudoku.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class HiddenDoublesTest {

    private HiddenDoubles hiddenDoubles;

    @BeforeEach
    void setUp() {
        hiddenDoubles = new HiddenDoubles();
    }

    @Test
    void execute() {

        int[] puzzle = new int[] {
                8,0,1,0,0,6,0,9,4,
                3,0,0,0,0,9,0,8,0,
                9,7,0,0,8,0,5,0,0,
                5,4,7,0,6,2,0,3,0,
                6,3,2,0,0,0,0,5,0,
                1,9,8,3,7,5,2,4,6,
                0,8,3,6,2,0,9,1,5,
                0,6,5,1,9,8,0,0,0,
                2,1,9,5,0,0,0,0,8
        };
        var markers = SudokuUtils.updatePencilMarks(puzzle, null);

        var expectedMarkers = TestUtils.deepCopyPencilMarks(markers);
        expectedMarkers.put(23, Arrays.asList(1,3));
        expectedMarkers.put(26, Arrays.asList(1,3));
        expectedMarkers.put(39, Arrays.asList(8,9));

        var expectedPuzzle = Arrays.copyOf(puzzle, puzzle.length);

        var result = hiddenDoubles.execute(puzzle, markers);

        assertTrue(result.arePencilMarksAltered());
        assertEquals(0, result.addedValues());
        assertArrayEquals(expectedPuzzle, result.puzzle());
        assertArrayEquals(expectedMarkers.keySet().toArray(new Integer[0]), result.pencilMarks().keySet().toArray(new Integer[0]));
        for (int i : expectedMarkers.keySet())
            assertArrayEquals(expectedMarkers.get(i).toArray(), result.pencilMarks().get(i).toArray());

    }

    @Test
    void executeNothingRemoved() {

        int[] puzzle = new int[] {
                8,0,1,0,0,6,0,9,4,
                3,0,0,0,0,9,0,8,0,
                9,7,0,0,8,0,5,0,0,
                5,4,7,0,6,2,0,3,0,
                6,3,2,0,0,0,0,5,0,
                1,9,8,3,7,5,2,4,6,
                0,8,3,6,2,0,9,1,5,
                0,6,5,1,9,8,0,0,0,
                2,1,9,5,0,0,0,0,8
        };
        var markers = SudokuUtils.updatePencilMarks(puzzle, null);
        markers.put(23, Arrays.asList(1,3));
        markers.put(26, Arrays.asList(1,3));
        markers.put(39, Arrays.asList(8,9));

        var expectedMarkers = TestUtils.deepCopyPencilMarks(markers);

        var expectedPuzzle = Arrays.copyOf(puzzle, puzzle.length);

        var result = hiddenDoubles.execute(puzzle, markers);

        assertFalse(result.arePencilMarksAltered());
        assertEquals(0, result.addedValues());
        assertArrayEquals(expectedPuzzle, result.puzzle());
        assertArrayEquals(expectedMarkers.keySet().toArray(new Integer[0]), result.pencilMarks().keySet().toArray(new Integer[0]));
        for (int i : expectedMarkers.keySet())
            assertArrayEquals(expectedMarkers.get(i).toArray(), result.pencilMarks().get(i).toArray());

    }


    @Test
    void findHiddenDoubles() {

        HashMap<Integer, List<Integer>> markers = new HashMap<>();
        markers.put(0, new ArrayList<>(Arrays.asList(4, 7)));
        markers.put(1, new ArrayList<>(Arrays.asList(1,2,3)));
        markers.put(2, new ArrayList<>(Arrays.asList(3,5,6)));
        markers.put(3, new ArrayList<>(Arrays.asList(4, 5, 7)));
        markers.put(4, new ArrayList<>(Arrays.asList(3, 6, 7)));
        markers.put(5, new ArrayList<>(Arrays.asList(1, 2, 4, 6)));
        markers.put(8, new ArrayList<>(Arrays.asList(5, 7)));


        List<Integer> indices = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));

        Integer[] result = hiddenDoubles.findHiddenDoubles(indices, markers);

        HashMap<Integer, List<Integer>> expected = new HashMap<>();
        expected.put(0, new ArrayList<>(Arrays.asList(4, 7)));
        expected.put(1, new ArrayList<>(Arrays.asList(1,2)));
        expected.put(2, new ArrayList<>(Arrays.asList(3,6)));
        expected.put(3, new ArrayList<>(Arrays.asList(4, 5, 7)));
        expected.put(4, new ArrayList<>(Arrays.asList(3, 6)));
        expected.put(5, new ArrayList<>(Arrays.asList(1, 2)));
        expected.put(8, new ArrayList<>(Arrays.asList(5, 7)));

        assertTrue(result.length > 0);

        for (int i : expected.keySet()) {

            List<Integer> expectedCandidates = expected.get(i);
            List<Integer> markerCandidates = markers.get(i);

            Collections.sort(expectedCandidates);
            Collections.sort(markerCandidates);

            assertArrayEquals(expectedCandidates.toArray(), markerCandidates.toArray());

        }

    }

    @Test
    void findHiddenDoubles_ReturnsEmptyListIfNoMarkersRemoved() {

        HashMap<Integer, List<Integer>> markers = new HashMap<>();
        markers.put(0, new ArrayList<>(Arrays.asList(4, 7)));
        markers.put(1, new ArrayList<>(Arrays.asList(1,2)));
        markers.put(2, new ArrayList<>(Arrays.asList(3,6)));
        markers.put(3, new ArrayList<>(Arrays.asList(4, 5, 7)));
        markers.put(4, new ArrayList<>(Arrays.asList(3, 6)));
        markers.put(5, new ArrayList<>(Arrays.asList(1, 2)));
        markers.put(8, new ArrayList<>(Arrays.asList(5, 7)));


        List<Integer> indices = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));

        Integer[] result = hiddenDoubles.findHiddenDoubles(indices, markers);

        HashMap<Integer, List<Integer>> expected = new HashMap<>();
        expected.put(0, new ArrayList<>(Arrays.asList(4, 7)));
        expected.put(1, new ArrayList<>(Arrays.asList(1,2)));
        expected.put(2, new ArrayList<>(Arrays.asList(3,6)));
        expected.put(3, new ArrayList<>(Arrays.asList(4, 5, 7)));
        expected.put(4, new ArrayList<>(Arrays.asList(3, 6)));
        expected.put(5, new ArrayList<>(Arrays.asList(1, 2)));
        expected.put(8, new ArrayList<>(Arrays.asList(5, 7)));

        assertEquals(0, result.length);

        for (int i : expected.keySet()) {

            List<Integer> expectedCandidates = expected.get(i);
            List<Integer> markerCandidates = markers.get(i);

            Collections.sort(expectedCandidates);
            Collections.sort(markerCandidates);

            assertArrayEquals(expectedCandidates.toArray(), markerCandidates.toArray());

        }

    }
}