package de.victor.sudoku.classifier.solvingtechs;

import de.victor.sudoku.SudokuUtils;
import de.victor.sudoku.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NakedTriplesTest {

    private NakedTriples nakedTriples;

    @BeforeEach
    void setUp() {

        nakedTriples = new NakedTriples();


    }


    @Test
    void execute() {

        var puzzle = new int[] {

                6,0,0,8,0,2,7,3,5,
                7,0,2,3,5,6,9,4,0,
                3,0,0,4,0,7,0,6,2,
                1,0,0,9,7,5,0,2,4,
                2,0,0,1,8,3,0,7,9,
                0,7,9,6,2,4,0,0,3,
                4,0,0,5,6,0,2,0,7,
                0,6,7,2,4,0,3,0,0,
                9,2,0,7,3,8,4,0,6

        };

        var markers = SudokuUtils.updatePencilMarks(puzzle, null);

        var expectedMarkers = TestUtils.deepCopyPencilMarks(markers);
        expectedMarkers.put(1, Arrays.asList(4,9));
        expectedMarkers.put(19, Arrays.asList(5,9));

        var expectedPuzzle = Arrays.copyOf(puzzle, puzzle.length);

        var result = nakedTriples.execute(puzzle, markers);

        assertTrue(result.arePencilMarksAltered());
        assertEquals(0, result.addedValues());
        assertArrayEquals(expectedPuzzle, result.puzzle());
        for (int i : expectedMarkers.keySet()) {
            assertArrayEquals(expectedMarkers.get(i).toArray(), result.pencilMarks().get(i).toArray());
        }

    }

    @Test
    void findNakedTriples() {

        var markers = new HashMap<Integer, List<Integer>>();

        markers.put(0, Arrays.asList(1,2));
        markers.put(1, Arrays.asList(1,2,3));
        markers.put(2, Arrays.asList(1,3));
        markers.put(3, Arrays.asList(1,2, 4 , 6));
        markers.put(4, Arrays.asList(1, 4 ,5));
        markers.put(5, Arrays.asList(3, 4 ,5, 6));
        markers.put(6, Arrays.asList(1, 4, 8, 9));
        markers.put(7, Arrays.asList(2, 5, 7, 9));
        markers.put(8, Arrays.asList(6, 7, 9));

        markers.put(9, Arrays.asList(1,4,9));
        markers.put(10, Arrays.asList(2,5,8));
        markers.put(11, Arrays.asList(3,6,7));


        var expected = new HashMap<Integer, List<Integer>>();

        expected.put(0, Arrays.asList(1,2));
        expected.put(1, Arrays.asList(1,2,3));
        expected.put(2, Arrays.asList(1,3));
        expected.put(3, Arrays.asList(4,6));
        expected.put(4, Arrays.asList(4,5));
        expected.put(5, Arrays.asList(4,5,6));
        expected.put(6, Arrays.asList(8, 9));
        expected.put(7, Arrays.asList(7, 9));
        expected.put(8, Arrays.asList(7, 9));

        expected.put(9, Arrays.asList(1,4,9));
        expected.put(10, Arrays.asList(2,5,8));
        expected.put(11, Arrays.asList(3,6,7));


        var expectedIndices = new Integer[] {3,4,5,6,7,8};

        var result = nakedTriples.findNakedTriples(Arrays.asList(0,1,2,3,4,5,6,7,8), markers);

        assertArrayEquals(expectedIndices, result );
        assertArrayEquals(expected.keySet().toArray(new Integer[0]), markers.keySet().toArray(new Integer[0]));


    }
}