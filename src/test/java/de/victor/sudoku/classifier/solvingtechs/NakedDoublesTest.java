package de.victor.sudoku.classifier.solvingtechs;

import de.victor.sudoku.SudokuUtils;
import de.victor.sudoku.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class NakedDoublesTest {

    private NakedDoubles nakedDoubles;


    @BeforeEach
    void setup() {

        nakedDoubles = new NakedDoubles();

    }

    @Test
    void execute() {

        int[] puzzle = {
                4,0,0,2,7,0,6,0,0,
                7,9,8,1,5,6,2,3,4,
                0,2,0,8,4,0,0,0,7,
                2,3,7,4,6,8,9,5,1,
                8,4,9,5,3,1,7,2,6,
                5,6,1,7,9,2,8,4,3,
                0,8,2,0,1,5,4,7,9,
                0,7,0,0,2,4,3,0,0,
                0,0,4,0,8,7,0,0,2
        };


        var markers = SudokuUtils.updatePencilMarks(puzzle, null);

        var expectedMarkers = TestUtils.deepCopyPencilMarks(markers);
        expectedMarkers.put(72, Arrays.asList(3,9));
        expectedMarkers.put(75, Arrays.asList(3,9));
        expectedMarkers.remove(79);
        expectedMarkers.put(70, Arrays.asList(1,8));

        var expectedPuzzle = Arrays.copyOf(puzzle, puzzle.length);
        expectedPuzzle[79] = 6;

        var result = nakedDoubles.execute(puzzle, markers);

        assertTrue(result.arePencilMarksAltered());
        assertEquals(1, result.addedValues());
        assertArrayEquals(expectedPuzzle, puzzle);
        assertArrayEquals(expectedMarkers.keySet().toArray(new Integer[0]), result.pencilMarks().keySet().toArray(new Integer[0]));
        for (int i : expectedMarkers.keySet())
            assertArrayEquals(expectedMarkers.get(i).toArray(), result.pencilMarks().get(i).toArray());

    }

    @Test
    void executeRemovedMarkerButNothingCollapsed() {

        int[] puzzle = {
                4,0,0,2,7,0,6,0,0,
                7,9,8,1,5,6,2,3,4,
                0,2,0,8,4,0,0,0,7,
                2,3,7,4,6,8,9,5,1,
                8,4,9,5,3,1,7,2,6,
                5,6,1,7,9,2,8,4,3,
                0,8,2,0,1,5,4,7,9,
                0,7,0,0,2,4,3,0,0,
                0,0,4,0,8,7,0,0,2
        };


        var markers = SudokuUtils.updatePencilMarks(puzzle, null);
        markers.put(79, Arrays.asList(1,6,9));

        var expectedMarkers = TestUtils.deepCopyPencilMarks(markers);
        expectedMarkers.put(72, Arrays.asList(3,6,9));
        expectedMarkers.put(79, Arrays.asList(6,9));

        var expectedPuzzle = Arrays.copyOf(puzzle, puzzle.length);

        var result = nakedDoubles.execute(puzzle, markers);

        assertTrue(result.arePencilMarksAltered());
        assertEquals(0, result.addedValues());
        assertArrayEquals(expectedPuzzle, puzzle);
        assertArrayEquals(expectedMarkers.keySet().toArray(new Integer[0]), result.pencilMarks().keySet().toArray(new Integer[0]));
        for (int i : expectedMarkers.keySet())
            assertArrayEquals(expectedMarkers.get(i).toArray(), result.pencilMarks().get(i).toArray());

    }

    @Test
    void executeRemovedMarkerNothingRemoved() {

        int[] puzzle = {
                4,0,0,2,7,0,6,0,0,
                7,9,8,1,5,6,2,3,4,
                0,2,0,8,4,0,0,0,7,
                2,3,7,4,6,8,9,5,1,
                8,4,9,5,3,1,7,2,6,
                5,6,1,7,9,2,8,4,3,
                0,8,2,0,1,5,4,7,9,
                0,7,0,0,2,4,3,0,0,
                0,0,4,0,8,7,0,0,2
        };


        var markers = SudokuUtils.updatePencilMarks(puzzle, null);
        markers.put(79, Arrays.asList(6,9));
        markers.put(72, Arrays.asList(3,6,9));

        var expectedMarkers = TestUtils.deepCopyPencilMarks(markers);

        var expectedPuzzle = Arrays.copyOf(puzzle, puzzle.length);

        var result = nakedDoubles.execute(puzzle, markers);

//        assertFalse(result.arePencilMarksAltered);
        assertEquals(0, result.addedValues());
        assertArrayEquals(expectedPuzzle, puzzle);
        assertArrayEquals(expectedMarkers.keySet().toArray(new Integer[0]), result.pencilMarks().keySet().toArray(new Integer[0]));
        for (int i : expectedMarkers.keySet())
            assertArrayEquals(expectedMarkers.get(i).toArray(), result.pencilMarks().get(i).toArray());

    }


    @Test
    void findNakedDoubles() {

        HashMap<Integer, List<Integer>> markers = new HashMap<>();
        markers.put(1, new ArrayList<>(Arrays.asList(1, 3, 4)));
        markers.put(2, new ArrayList<>(Arrays.asList(2, 3, 4)));
        markers.put(3, new ArrayList<>(Arrays.asList(1, 2)));
        markers.put(4, new ArrayList<>(Arrays.asList(1, 2)));
        markers.put(6, new ArrayList<>(Arrays.asList(1, 3, 5, 7)));
        markers.put(7, new ArrayList<>(Arrays.asList(1, 4, 5, 6, 7)));
        markers.put(8, new ArrayList<>(Arrays.asList(2, 5, 6, 7)));

        List<Integer> indices = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));

        Integer[] result = nakedDoubles.findNakedDoubles(indices, markers);

        HashMap<Integer, List<Integer>> expected = new HashMap<>();

        expected.put(1, new ArrayList<>(Arrays.asList(3, 4)));
        expected.put(2, new ArrayList<>(Arrays.asList(3, 4)));
        expected.put(3, new ArrayList<>(Arrays.asList(1, 2)));
        expected.put(4, new ArrayList<>(Arrays.asList(1, 2)));
        expected.put(6, new ArrayList<>(Arrays.asList(5, 7)));
        expected.put(7, new ArrayList<>(Arrays.asList(5, 6, 7)));
        expected.put(8, new ArrayList<>(Arrays.asList(5, 6, 7)));


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
    void findNakedDoublesIn_ReturnEmptyListIfNoMarkersRemoved() {

        HashMap<Integer, List<Integer>> markers = new HashMap<>();
        markers.put(1, new ArrayList<>(Arrays.asList(3, 4)));
        markers.put(2, new ArrayList<>(Arrays.asList(3, 4)));
        markers.put(3, new ArrayList<>(Arrays.asList(1, 2)));
        markers.put(4, new ArrayList<>(Arrays.asList(1, 2)));
        markers.put(6, new ArrayList<>(Arrays.asList(5, 7)));
        markers.put(7, new ArrayList<>(Arrays.asList(5, 6, 7)));
        markers.put(8, new ArrayList<>(Arrays.asList(5, 6, 7)));

        List<Integer> indices = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));

        Integer[] result = nakedDoubles.findNakedDoubles(indices, markers);

        HashMap<Integer, List<Integer>> expected = new HashMap<>();
        expected.put(1, new ArrayList<>(Arrays.asList(3, 4)));
        expected.put(2, new ArrayList<>(Arrays.asList(3, 4)));
        expected.put(3, new ArrayList<>(Arrays.asList(1, 2)));
        expected.put(4, new ArrayList<>(Arrays.asList(1, 2)));
        expected.put(6, new ArrayList<>(Arrays.asList(5, 7)));
        expected.put(7, new ArrayList<>(Arrays.asList(5, 6, 7)));
        expected.put(8, new ArrayList<>(Arrays.asList(5, 6, 7)));

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
