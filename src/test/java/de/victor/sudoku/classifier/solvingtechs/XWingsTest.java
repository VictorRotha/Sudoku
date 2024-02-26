package de.victor.sudoku.classifier.solvingtechs;

import de.victor.sudoku.Sudoku;
import de.victor.sudoku.SudokuUtils;
import de.victor.sudoku.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class XWingsTest {

    private XWings xWings;

    @BeforeEach
    void setUp() {
        xWings = new XWings();
    }


    @Test
    void xWingInRows() {


        int[] puzzle = new int[] {
                9,0,0,0,5,1,7,3,0,
                1,0,7,3,9,8,2,0,5,
                5,0,0,0,7,6,0,9,1,
                8,1,0,7,2,4,3,5,0,
                2,0,0,1,6,5,0,0,7,
                0,7,5,9,8,3,0,1,2,
                0,2,1,5,3,7,0,0,0,
                7,5,8,6,4,9,1,2,3,
                3,9,0,8,1,2,5,7,0
        };

        HashMap<Integer, List<Integer>> markers = SudokuUtils.updatePencilMarks(puzzle, null);
        markers.put(8, markers.get(8).stream().filter(i -> i != 4).collect(Collectors.toList()));

        var result = xWings.execute(puzzle, markers);

        assertTrue(result.arePencilMarksAltered());
        assertEquals(24, result.addedValues());
        assertTrue(SudokuUtils.isSudokuValid(puzzle));


    }


    @Test
    void xWingInColsRemovedMarkersNothingCollapsed() {


        int[] puzzle = new int[] {
                7,0,3,8,0,6,0,9,0,
                6,1,4,9,2,3,7,0,0,
                9,8,0,0,7,4,0,6,3,
                0,3,0,0,0,0,0,7,0,
                1,7,9,2,0,5,6,3,0,
                0,4,0,0,3,0,0,1,0,
                8,0,1,0,9,0,3,0,6,
                3,9,7,0,6,0,0,0,1,
                4,6,0,3,0,1,9,0,7
        };

        HashMap<Integer, List<Integer>> markers = SudokuUtils.updatePencilMarks(puzzle, null);
        markers.put(30, markers.get(30).stream().filter(i -> i != 4).collect(Collectors.toList()));

        var expectedMarkers = TestUtils.deepCopyPencilMarks(markers);
        expectedMarkers.put(69, Arrays.asList(2, 5, 8));
        expectedMarkers.put(57, Arrays.asList(4, 5, 7));
        expectedMarkers.put(66, Arrays.asList(4, 5));
        expectedMarkers.put(70, Arrays.asList(2, 4, 5, 8));
        expectedMarkers.put(61, Arrays.asList(2,4, 5));

        var result = xWings.execute(puzzle, markers);

        assertTrue(result.arePencilMarksAltered());
        assertEquals(0, result.addedValues());

        for (int i :expectedMarkers.keySet())
            assertArrayEquals(expectedMarkers.get(i).toArray(), result.pencilMarks().get(i).toArray());

    }


    @Test
    void testExecuteNothingRemoved() {


        int[] puzzle = new int[] {
                9,0,0,0,5,1,7,3,0,
                1,0,7,3,9,8,2,0,5,
                5,0,0,0,7,6,0,9,1,
                8,1,6,7,2,4,3,5,9,
                2,0,0,1,6,5,0,0,7,
                0,7,5,9,8,3,0,1,2,
                0,2,1,5,3,7,0,0,0,
                7,5,8,6,4,9,1,2,3,
                3,9,4,8,1,2,5,7,6
        };

        var markers = SudokuUtils.updatePencilMarks(puzzle, null);


        var expectedPuzzle = Arrays.copyOf(puzzle, puzzle.length);
        var expectedMarkers = SudokuUtils.updatePencilMarks(puzzle, null);

        SudokuUtils.printSudoku(new Sudoku().solvePuzzle(puzzle));



        var result = xWings.execute(puzzle, markers);

        assertFalse(result.arePencilMarksAltered());
        assertEquals(0, result.addedValues());
        assertArrayEquals(expectedPuzzle, result.puzzle());
        for (int i : expectedMarkers.keySet())
            assertArrayEquals(expectedMarkers.get(i).toArray(), markers.get(i).toArray());


    }

    @Test
    void xWingRemoveMarkers() {

        int[] puzzle = new int[] {
                9,0,0,0,5,1,7,3,0,
                1,0,7,3,9,8,2,0,5,
                5,0,0,0,7,6,0,9,1,
                8,1,0,7,2,4,3,5,0,
                2,0,0,1,6,5,0,0,7,
                0,7,5,9,8,3,0,1,2,
                0,2,1,5,3,7,0,0,0,
                7,5,8,6,4,9,1,2,3,
                3,9,0,8,1,2,5,7,0
        };


        HashMap<Integer, List<Integer>> markers = SudokuUtils.updatePencilMarks(puzzle, null);


        var markercopy = TestUtils.deepCopyPencilMarks(markers);


        var altered = markercopy.get(8).stream().filter(i -> i != 6).collect(Collectors.toList());
        markercopy.put(8, altered);

        altered = markercopy.get(62).stream().filter(i -> i != 6).collect(Collectors.toList());
        markercopy.put(62, altered);

        altered = markercopy.get(2).stream().filter(i -> i != 6).collect(Collectors.toList());
        markercopy.put(2, altered);

        var removed = xWings.xWingRemoveMarkers(markers);

        var e = markercopy.keySet().stream().sorted().toArray();
        var m = markers.keySet().stream().sorted().toArray();

        assertTrue(removed);
        assertArrayEquals(e, m);
        for (int i: markercopy.keySet())
            assertArrayEquals(markercopy.get(i).toArray(), markers.get(i).toArray());



    }

    @Test
    void xWingRemoveMarkers_returnsFalseIfPencilmarksNotAltered() {

        int[] puzzle = new int[] {
                9,0,0,0,5,1,7,3,0,
                1,0,7,3,9,8,2,0,5,
                5,0,0,0,7,6,0,9,1,
                8,1,0,7,2,4,3,5,0,
                2,0,0,1,6,5,0,0,7,
                0,7,5,9,8,3,0,1,2,
                0,2,1,5,3,7,0,0,0,
                7,5,8,6,4,9,1,2,3,
                3,9,0,8,1,2,5,7,0
        };


        HashMap<Integer, List<Integer>> markers = SudokuUtils.updatePencilMarks(puzzle, null);

        markers.put(8, markers.get(8).stream().filter(i -> i != 6).collect(Collectors.toList()));
        markers.put(62, markers.get(62).stream().filter(i -> i != 6).collect(Collectors.toList()));
        markers.put(2, markers.get(2).stream().filter(i -> i != 6).collect(Collectors.toList()));

        var expected = TestUtils.deepCopyPencilMarks(markers);

        var removed = xWings.xWingRemoveMarkers(markers);

        var e = expected.keySet().stream().sorted().toArray();
        var m = markers.keySet().stream().sorted().toArray();

        assertFalse(removed);
        assertArrayEquals(e, m);
        for (int i: expected.keySet())
            assertArrayEquals(expected.get(i).toArray(), markers.get(i).toArray());

    }


    @Test
    void xWingCompareRows() {

        HashMap<Integer, List<Integer>> markers = new HashMap<>();
        markers.put(1, new ArrayList<>(Arrays.asList(6,8)));
        markers.put(2, new ArrayList<>(Arrays.asList(2,4)));
        markers.put(3, new ArrayList<>(Arrays.asList(2,4)));
        markers.put(8, new ArrayList<>(Arrays.asList(8,6)));
        markers.put(10, new ArrayList<>(Arrays.asList(4,6)));
        markers.put(16, new ArrayList<>(Arrays.asList(4,6)));
        markers.put(19, new ArrayList<>(Arrays.asList(3,4,8)));
        markers.put(20, new ArrayList<>(Arrays.asList(2,3,4)));
        markers.put(21, new ArrayList<>(Arrays.asList(2,4)));
        markers.put(24, new ArrayList<>(Arrays.asList(4,8)));
        markers.put(29, new ArrayList<>(Arrays.asList(6,9)));
        markers.put(35, new ArrayList<>(Arrays.asList(6,9)));
        markers.put(37, new ArrayList<>(Arrays.asList(3,4)));
        markers.put(38, new ArrayList<>(Arrays.asList(3,4,9)));
        markers.put(42, new ArrayList<>(Arrays.asList(4,8,9)));
        markers.put(43, new ArrayList<>(Arrays.asList(4,8)));
        markers.put(45, new ArrayList<>(Arrays.asList(4,6)));
        markers.put(51, new ArrayList<>(Arrays.asList(4,6)));
        markers.put(54, new ArrayList<>(Arrays.asList(4,6)));
        markers.put(60, new ArrayList<>(Arrays.asList(4,6,8,9)));
        markers.put(61, new ArrayList<>(Arrays.asList(4,6,8)));
        markers.put(62, new ArrayList<>(Arrays.asList(4,6,8,9)));
        markers.put(74, new ArrayList<>(Arrays.asList(4,6)));
        markers.put(80, new ArrayList<>(Arrays.asList(4,6)));

        HashMap<Integer, List<Integer>> expected = new HashMap<>();
        expected.put(1, new ArrayList<>(Arrays.asList(6,8)));
        expected.put(2, new ArrayList<>(Arrays.asList(2,4)));
        expected.put(3, new ArrayList<>(Arrays.asList(2,4)));
        expected.put(8, new ArrayList<>(Arrays.asList(8)));
        expected.put(10, new ArrayList<>(Arrays.asList(4,6)));
        expected.put(16, new ArrayList<>(Arrays.asList(4,6)));
        expected.put(19, new ArrayList<>(Arrays.asList(3,4,8)));
        expected.put(20, new ArrayList<>(Arrays.asList(2,3,4)));
        expected.put(21, new ArrayList<>(Arrays.asList(2,4)));
        expected.put(24, new ArrayList<>(Arrays.asList(4,8)));
        expected.put(29, new ArrayList<>(Arrays.asList(6,9)));
        expected.put(35, new ArrayList<>(Arrays.asList(6,9)));
        expected.put(37, new ArrayList<>(Arrays.asList(3,4)));
        expected.put(38, new ArrayList<>(Arrays.asList(3,4,9)));
        expected.put(42, new ArrayList<>(Arrays.asList(4,8,9)));
        expected.put(43, new ArrayList<>(Arrays.asList(4,8)));
        expected.put(45, new ArrayList<>(Arrays.asList(4,6)));
        expected.put(51, new ArrayList<>(Arrays.asList(4,6)));
        expected.put(54, new ArrayList<>(Arrays.asList(4,6)));
        expected.put(60, new ArrayList<>(Arrays.asList(4,6,8,9)));
        expected.put(61, new ArrayList<>(Arrays.asList(4,6,8)));
        expected.put(62, new ArrayList<>(Arrays.asList(4,8,9)));
        expected.put(74, new ArrayList<>(Arrays.asList(4,6)));
        expected.put(80, new ArrayList<>(Arrays.asList(4,6)));

        var doubles = SudokuUtils.findCandidatePairInRow(markers);

        var removed = xWings.xWingCompareRows(doubles, markers);

        assertTrue(removed);
        assertArrayEquals(markers.keySet().toArray(new Integer[0]), expected.keySet().toArray(new Integer[0]));

        for (int row : markers.keySet())
            assertArrayEquals(expected.get(row).toArray(),markers.get(row).toArray());

    }

    @Test
    void xWingCompareRows_NothingRemoved() {
        HashMap<Integer, List<Integer>> markers = new HashMap<>();
        markers.put(1, new ArrayList<>(Arrays.asList(8,9)));
        markers.put(2, new ArrayList<>(Arrays.asList(2,4)));
        markers.put(3, new ArrayList<>(Arrays.asList(2,8,4,6)));
        markers.put(8, new ArrayList<>(Arrays.asList(8,6,4,2)));
        markers.put(74, new ArrayList<>(Arrays.asList(4,7)));
        markers.put(75, new ArrayList<>(Arrays.asList(4,6,7)));
        markers.put(80, new ArrayList<>(Arrays.asList(4,6, 7)));

        HashMap<Integer, List<Integer>> expected = new HashMap<>();
        expected.put(1, new ArrayList<>(Arrays.asList(8,9)));
        expected.put(2, new ArrayList<>(Arrays.asList(2,4)));
        expected.put(3, new ArrayList<>(Arrays.asList(2,8,4,6)));
        expected.put(8, new ArrayList<>(Arrays.asList(8,6,4,2)));
        expected.put(74, new ArrayList<>(Arrays.asList(4,7)));
        expected.put(75, new ArrayList<>(Arrays.asList(4,6,7)));
        expected.put(80, new ArrayList<>(Arrays.asList(4,6, 7)));

        var doubles = SudokuUtils.findCandidatePairInRow(markers);

        var removed = xWings.xWingCompareRows(doubles, markers);

        assertFalse(removed);
        assertArrayEquals(markers.keySet().toArray(new Integer[0]), expected.keySet().toArray(new Integer[0]));

        for (int row : markers.keySet()) {
            assertArrayEquals(expected.get(row).toArray(),markers.get(row).toArray());
        }

    }
}