package de.victor.sudoku.classifier.solvingtechs;

import de.victor.sudoku.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class HiddenTriplesTest {

    private HiddenTriples hiddenTriples;

    @BeforeEach
    void setUp() {

        hiddenTriples = new HiddenTriples();
    }

    @Test
    void executeMarkersRemovedNothingCollapsed() {

        var markers = new HashMap<Integer, List<Integer>>();
        markers.put(0, Arrays.asList(1,2,5,6,8));
        markers.put(1, Arrays.asList(4,5,7,8));
        markers.put(2, Arrays.asList(5,7,8));
        markers.put(3, Arrays.asList(5,8));
        markers.put(5, Arrays.asList(1,2,5,6));
        markers.put(7, Arrays.asList(1,2,6));
        markers.put(8, Arrays.asList(4,7));
        markers.put(10, Arrays.asList(1,2, 5,6,8));
        markers.put(11, Arrays.asList(1,2, 5,6,8));
        markers.put(18, Arrays.asList(1,2, 5,6,8));

        var expectedMarkers = TestUtils.deepCopyPencilMarks(markers);
        expectedMarkers.put(0, Arrays.asList(1,2,6));
        expectedMarkers.put(5, Arrays.asList(1,2,6));
        expectedMarkers.put(7, Arrays.asList(1,2,6));

        var result = hiddenTriples.execute(new int[81], markers);

        assertTrue(result.arePencilMarksAltered());
        assertEquals(0, result.addedValues());
        for (int i : expectedMarkers.keySet()) {
            assertArrayEquals(expectedMarkers.get(i).toArray(), result.pencilMarks().get(i).toArray());
        }


    }


    @Test
    void executeMarkersNothingRemoved() {

        var markers = new HashMap<Integer, List<Integer>>();
        markers.put(0, Arrays.asList(1,2,5,6,8));
        markers.put(1, Arrays.asList(4,5,7,8));
        markers.put(2, Arrays.asList(5,7,8));
        markers.put(3, Arrays.asList(5,8));
        markers.put(5, Arrays.asList(1,2,5,6));
        markers.put(7, Arrays.asList(1,2,6));
        markers.put(8, Arrays.asList(4,6,7));
        markers.put(10, Arrays.asList(1,2, 5,6,8));
        markers.put(11, Arrays.asList(1,2, 5,6,8));
        markers.put(18, Arrays.asList(1,2, 5,6,8));

        var expectedMarkers = TestUtils.deepCopyPencilMarks(markers);

        var result = hiddenTriples.execute(new int[81], markers);

        assertFalse(result.arePencilMarksAltered());
        assertEquals(0, result.addedValues());
        for (int i : expectedMarkers.keySet()) {
            assertArrayEquals(expectedMarkers.get(i).toArray(), result.pencilMarks().get(i).toArray());
        }


    }

    @Test
    void findHiddenTriplesInRow() {

        HashMap<Integer, List<Integer>> markers = new HashMap<>();
        markers.put(1, new ArrayList<>(Arrays.asList(1, 2, 5)));
        markers.put(2, new ArrayList<>(Arrays.asList(5, 6)));
        markers.put(3, new ArrayList<>(Arrays.asList(9, 8)));
        markers.put(4, new ArrayList<>(Arrays.asList(1, 3, 6)));
        markers.put(5, new ArrayList<>(Arrays.asList(8, 6)));
        markers.put(6, new ArrayList<>(Arrays.asList(2, 3, 6)));
        markers.put(8, new ArrayList<>(Arrays.asList(9, 5)));

        List<Integer> indices = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));

        Integer[] result = hiddenTriples.findHiddenTriples(indices, markers);

        HashMap<Integer, List<Integer>> expected = new HashMap<>();
        expected.put(1, new ArrayList<>(Arrays.asList(1, 2)));
        expected.put(2, new ArrayList<>(Arrays.asList(5, 6)));
        expected.put(3, new ArrayList<>(Arrays.asList(9, 8)));
        expected.put(4, new ArrayList<>(Arrays.asList(1, 3)));
        expected.put(5, new ArrayList<>(Arrays.asList(8, 6)));
        expected.put(6, new ArrayList<>(Arrays.asList(2, 3)));
        expected.put(8, new ArrayList<>(Arrays.asList(9, 5)));

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
    void findHiddenTriplesInRowSubsetSmallerThanThree() {

        HashMap<Integer, List<Integer>> markers = new HashMap<>();

        markers.put(4, new ArrayList<>(Arrays.asList(1, 3, 6)));
        markers.put(6, new ArrayList<>(Arrays.asList(1, 3, 6)));

        List<Integer> indices = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));

        Integer[] result = hiddenTriples.findHiddenTriples(indices, markers);

        HashMap<Integer, List<Integer>> expected = new HashMap<>();
        expected.put(4, new ArrayList<>(Arrays.asList(1,3,6)));
        expected.put(6, new ArrayList<>(Arrays.asList(1,3,6)));

        assertEquals(0, result.length);

        for (int i : expected.keySet()) {

            List<Integer> expectedCandidates = expected.get(i);
            List<Integer> markerCandidates = markers.get(i);

            Collections.sort(expectedCandidates);
            Collections.sort(markerCandidates);

            assertArrayEquals(expectedCandidates.toArray(), markerCandidates.toArray());

        }

    }



    @Test
    void findHiddenTriplesInCol() {

        HashMap<Integer, List<Integer>> markers = new HashMap<>();
        markers.put(11, new ArrayList<>(Arrays.asList(1, 2, 7)));
        markers.put(20, new ArrayList<>(Arrays.asList(1,3,8)));
        markers.put(29, new ArrayList<>(Arrays.asList(4,5,8)));
        markers.put(38, new ArrayList<>(Arrays.asList(4,5,6)));
        markers.put(47, new ArrayList<>(Arrays.asList(1,2,3)));
        markers.put(56, new ArrayList<>(Arrays.asList(4,6,7)));
        markers.put(65, new ArrayList<>(Arrays.asList(7,8)));
        markers.put(74, new ArrayList<>(Arrays.asList(7,8)));

        List<Integer> indices = new ArrayList<>(Arrays.asList(2, 11, 20,29, 38, 47, 56, 65, 74));

        Integer[] result = hiddenTriples.findHiddenTriples(indices, markers);

        HashMap<Integer, List<Integer>> expected = new HashMap<>();
        expected.put(11, new ArrayList<>(Arrays.asList(1, 2)));
        expected.put(20, new ArrayList<>(Arrays.asList(1,3)));
        expected.put(29, new ArrayList<>(Arrays.asList(4,5)));
        expected.put(38, new ArrayList<>(Arrays.asList(4,5,6)));
        expected.put(47, new ArrayList<>(Arrays.asList(1,2,3)));
        expected.put(56, new ArrayList<>(Arrays.asList(4,6)));
        expected.put(65, new ArrayList<>(Arrays.asList(7,8)));
        expected.put(74, new ArrayList<>(Arrays.asList(7,8)));

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
    void findHiddenTriplesInBox() {

        HashMap<Integer, List<Integer>> markers = new HashMap<>();
        markers.put(60, new ArrayList<>(Arrays.asList(5, 7, 8)));
        markers.put(61, new ArrayList<>(Arrays.asList(5, 6)));
        markers.put(70, new ArrayList<>(Arrays.asList(6,7,8,9)));
        markers.put(78, new ArrayList<>(Arrays.asList(5, 6)));
        markers.put(80, new ArrayList<>(Arrays.asList(5, 8, 9)));

        List<Integer> indices = new ArrayList<>(Arrays.asList(60,61,62,69,70,71,78,79,80));

        Integer[] result = hiddenTriples.findHiddenTriples(indices, markers);

        HashMap<Integer, List<Integer>> expected = new HashMap<>();
        expected.put(60, new ArrayList<>(Arrays.asList(7, 8)));
        expected.put(61, new ArrayList<>(Arrays.asList(5, 6)));
        expected.put(70, new ArrayList<>(Arrays.asList(7,8,9)));
        expected.put(78, new ArrayList<>(Arrays.asList(5, 6)));
        expected.put(80, new ArrayList<>(Arrays.asList(8, 9)));

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
    void findHiddenTriples_ReturnsEmptyListNoMarkersRemoved() {

        HashMap<Integer, List<Integer>> markers = new HashMap<>();
        markers.put(1, new ArrayList<>(Arrays.asList(1, 2, 5)));
        markers.put(2, new ArrayList<>(Arrays.asList(5, 6)));
        markers.put(3, new ArrayList<>(Arrays.asList(9, 8)));
        markers.put(4, new ArrayList<>(Arrays.asList(1, 3, 6)));
        markers.put(5, new ArrayList<>(Arrays.asList(8, 6)));
        markers.put(6, new ArrayList<>(Arrays.asList(2, 3, 6)));
        markers.put(8, new ArrayList<>(Arrays.asList(9, 5, 3)));

        List<Integer> indices = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));

        Integer[] result = hiddenTriples.findHiddenTriples(indices, markers);

        HashMap<Integer, List<Integer>> expected = new HashMap<>();
        expected.put(1, new ArrayList<>(Arrays.asList(1, 2, 5)));
        expected.put(2, new ArrayList<>(Arrays.asList(5, 6)));
        expected.put(3, new ArrayList<>(Arrays.asList(9, 8)));
        expected.put(4, new ArrayList<>(Arrays.asList(1, 3, 6)));
        expected.put(5, new ArrayList<>(Arrays.asList(8, 6)));
        expected.put(6, new ArrayList<>(Arrays.asList(2, 3, 6)));
        expected.put(8, new ArrayList<>(Arrays.asList(9, 5, 3)));

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