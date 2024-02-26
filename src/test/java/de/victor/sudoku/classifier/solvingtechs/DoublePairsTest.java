package de.victor.sudoku.classifier.solvingtechs;

import de.victor.sudoku.SudokuUtils;
import de.victor.sudoku.TestData;
import de.victor.sudoku.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class DoublePairsTest {

    private DoublePairs doublePairs;


    @BeforeEach
    void setup() {
        doublePairs = new DoublePairs();

    }


    @Test
    void execute() {

        int[] puzzle = new int[] {
                9,3,4,0,6,0,0,5,0,
                0,0,6,0,0,4,9,2,3,
                0,0,8,9,0,0,0,4,6,
                8,0,0,5,4,6,0,0,7,
                6,0,0,0,1,0,0,0,5,
                5,0,0,3,9,0,0,6,2,
                3,6,0,4,0,1,2,7,0,
                4,7,0,6,0,0,5,0,0,
                0,8,0,0,0,0,6,3,4,
        };

        var markers = SudokuUtils.updatePencilMarks(puzzle, null);

        markers.put(3, markers.get(3).stream().filter(n -> n != 8).collect(Collectors.toList()));
        markers.put(5, markers.get(5).stream().filter(n -> n != 8).collect(Collectors.toList()));
        markers.put(22, markers.get(22).stream().filter(n -> n != 2).collect(Collectors.toList()));
        markers.put(23, markers.get(23).stream().filter(n -> n != 2).collect(Collectors.toList()));
        markers.put(37, markers.get(37).stream().filter(n -> n != 2).collect(Collectors.toList()));
        markers.put(38, markers.get(38).stream().filter(n -> n != 2).collect(Collectors.toList()));
        markers.put(65, markers.get(65).stream().filter(n -> n != 1).collect(Collectors.toList()));
        markers.put(70, markers.get(70).stream().filter(n -> n != 9).collect(Collectors.toList()));

        var expectedMarkers = TestUtils.deepCopyPencilMarks(markers);
        expectedMarkers.put(3, Arrays.asList(1,2));
        expectedMarkers.put(12, Arrays.asList(1,8));
        expectedMarkers.put(39, Arrays.asList(2,8));
        expectedMarkers.put(68, Arrays.asList(3,8,9));
        expectedMarkers.put(76, Arrays.asList(2,5));
        expectedMarkers.put(77, Arrays.asList(5,9));
        expectedMarkers.remove(75);

        var expectedGrid = Arrays.copyOf(puzzle, puzzle.length);
        expectedGrid[75] = 7;

        var result = doublePairs.execute(puzzle, markers);

        assertTrue(result.arePencilMarksAltered());
        assertEquals(1, result.addedValues());
        assertArrayEquals(expectedGrid, result.puzzle());
        for (int i : expectedMarkers.keySet())
            assertArrayEquals(expectedMarkers.get(i).toArray(), result.pencilMarks().get(i).toArray());

    }

    @Test
    void execute_RemovedButNothingCollapsed() {

        int[] puzzle = new int[] {
                9,3,4,0,6,0,0,5,0,
                0,0,6,0,0,4,9,2,3,
                0,0,8,9,0,0,0,4,6,
                8,0,0,5,4,6,0,0,7,
                6,0,0,0,1,0,0,0,5,
                5,0,0,3,9,0,0,6,2,
                3,6,0,4,0,1,2,7,0,
                4,7,0,6,0,0,5,0,0,
                0,0,0,0,0,0,6,3,4,
        };

        var markers = SudokuUtils.updatePencilMarks(puzzle, null);

        markers.put(3, markers.get(3).stream().filter(n -> n != 8).collect(Collectors.toList()));
        markers.put(5, markers.get(5).stream().filter(n -> n != 8).collect(Collectors.toList()));
        markers.put(22, markers.get(22).stream().filter(n -> n != 2).collect(Collectors.toList()));
        markers.put(23, markers.get(23).stream().filter(n -> n != 2).collect(Collectors.toList()));
        markers.put(37, markers.get(37).stream().filter(n -> n != 2).collect(Collectors.toList()));
        markers.put(38, markers.get(38).stream().filter(n -> n != 2).collect(Collectors.toList()));
        markers.put(65, markers.get(65).stream().filter(n -> n != 1).collect(Collectors.toList()));
        markers.put(70, markers.get(70).stream().filter(n -> n != 9).collect(Collectors.toList()));

        var expectedMarkers = TestUtils.deepCopyPencilMarks(markers);

        expectedMarkers.put(68, Arrays.asList(3,8,9));
        expectedMarkers.put(77, Arrays.asList(5,7,8,9));
        expectedMarkers.put(75, Arrays.asList(7,8));

        var expectedGrid = Arrays.copyOf(puzzle, puzzle.length);

        var result = doublePairs.execute(puzzle, markers);

        assertTrue(result.arePencilMarksAltered());
        assertEquals(0, result.addedValues());
        assertArrayEquals(expectedGrid, result.puzzle());
        for (int i : expectedMarkers.keySet())
            assertArrayEquals(expectedMarkers.get(i).toArray(), result.pencilMarks().get(i).toArray());

    }

    @Test
    void executeNothingRemoved() {

        int[] puzzle = new int[] {
                9,3,4,0,6,0,0,5,0,
                0,0,6,0,0,4,9,2,3,
                0,0,8,9,0,0,0,4,6,
                8,0,0,5,4,6,0,0,7,
                6,0,0,0,1,0,0,0,5,
                5,0,0,3,9,0,0,6,2,
                3,6,0,4,0,1,2,7,0,
                4,7,0,6,0,0,5,0,0,
                0,8,0,0,0,0,6,3,4,
        };

        var markers = SudokuUtils.updatePencilMarks(puzzle, null);

        markers.put(3, markers.get(3).stream().filter(n -> n != 8).collect(Collectors.toList()));
        markers.put(5, markers.get(5).stream().filter(n -> n != 8).collect(Collectors.toList()));
        markers.put(37, markers.get(37).stream().filter(n -> n != 2).collect(Collectors.toList()));
        markers.put(38, markers.get(38).stream().filter(n -> n != 2).collect(Collectors.toList()));
        markers.put(65, markers.get(65).stream().filter(n -> n != 1).collect(Collectors.toList()));
        markers.put(70, markers.get(70).stream().filter(n -> n != 9).collect(Collectors.toList()));

        var expectedMarkers = TestUtils.deepCopyPencilMarks(markers);

        var expectedGrid = Arrays.copyOf(puzzle, puzzle.length);

        var result = doublePairs.execute(puzzle, markers);

        assertFalse(result.arePencilMarksAltered());
        assertEquals(0, result.addedValues());
        assertArrayEquals(expectedGrid, result.puzzle());
        for (int i : expectedMarkers.keySet())
            assertArrayEquals(expectedMarkers.get(i).toArray(), result.pencilMarks().get(i).toArray());

    }




    @Test
    void removeCandidatesFromIndices() {

        var puzzle = TestData.UNIQUE_SOLVABLE_PUZZLE_TWO;
        var markers = SudokuUtils.updatePencilMarks(puzzle, null);

        var indicesToRemove = new HashMap<Integer, List<Integer>>();
        var indicesOne = Arrays.asList(6, 9, 12, 14);
        var indicesTwo = Arrays.asList(2, 9, 10, 12, 15);
        indicesToRemove.put(2, indicesOne);
        indicesToRemove.put(4, indicesTwo);

        var expected = TestUtils.deepCopyPencilMarks(markers);
        for (int i : indicesOne)
            if (markers.containsKey(i))
                expected.put(i, expected.get(i).stream().filter(n -> n != 2).collect(Collectors.toList()));

        for (int i : indicesTwo)
            if (markers.containsKey(i))
                expected.put(i, expected.get(i).stream().filter(n -> n != 4).collect(Collectors.toList()));

        var removed = doublePairs.removeCandidatesFromIndices(indicesToRemove, markers);

        assertArrayEquals(markers.keySet().toArray(new Integer[0]), expected.keySet().toArray(new Integer[0]));
        assertTrue(removed);
        for (int i : markers.keySet())
            assertArrayEquals(expected.get(i).toArray(), markers.get(i).toArray());

    }

    @Test
    void compareColumns() {

        HashMap<Integer, HashMap<Integer, List<Integer>>> doubles = new HashMap<>();

        var columnsByBoxes = new HashMap<Integer, List<Integer>>();
        columnsByBoxes.put(0, Arrays.asList(0, 2));
        columnsByBoxes.put(3, Arrays.asList(0, 2));

        doubles.put(1, columnsByBoxes);

        columnsByBoxes = new HashMap<>();
        columnsByBoxes.put(0, Arrays.asList(1, 2));
        columnsByBoxes.put(6, Arrays.asList(0, 2));

        doubles.put(2, columnsByBoxes);

        columnsByBoxes = new HashMap<>();
        columnsByBoxes.put(4, Arrays.asList(3,5));
        columnsByBoxes.put(5, Arrays.asList(7,8));
        columnsByBoxes.put(8, Arrays.asList(7,8));

        doubles.put(3, columnsByBoxes);

        var expected = new HashMap<Integer, List<Integer>>();

        expected.put(1, Arrays.asList(54,63,72,56,65,74));
        expected.put(3, Arrays.asList(7,8,16,17,25,26));

        var result = doublePairs.compareColumns(doubles);

        assertArrayEquals(expected.keySet().toArray(new Integer[0]), result.keySet().toArray(new Integer[0]));

        for (int i : result.keySet()) {
            var e = expected.get(i).stream().sorted().mapToInt(in -> in).toArray();
            var r = result.get(i).stream().sorted().mapToInt(in -> in).toArray();
            assertArrayEquals(e, r);

        }

    }

    @Test
    void findCandidateDoublesInBoxes() {


        HashMap<Integer, List<Integer>> markers = new HashMap<>();
        markers.put(0, Arrays.asList(1,2));
        markers.put(2, Arrays.asList(1,5));
        markers.put(9, Arrays.asList(2,4));
        markers.put(10, Arrays.asList(2,4,6));

        markers.put(27, Arrays.asList(1,3));
        markers.put(28, Arrays.asList(1,3, 6));
        markers.put(36, Arrays.asList(3, 7));

        markers.put(80, Arrays.asList(7,8,9));
        markers.put(70, Arrays.asList(6,8,9));
        markers.put(62, Arrays.asList(4,5,9));

        var expected = new HashMap<Integer, HashMap<Integer, List<Integer>>>();

//        HashMap<Integer, List<Integer>> colsByNumber = new HashMap<>();
//        colsByNumber.put(1, Arrays.asList(0,2));
//        colsByNumber.put(4, Arrays.asList(0,1));
//        expected.put(0, colsByNumber);
//
//        colsByNumber = new HashMap<>();
//        colsByNumber.put(1, Arrays.asList(0,1));
//        expected.put(3, colsByNumber);
//
//        colsByNumber = new HashMap<>();
//        colsByNumber.put(8, Arrays.asList(7,8));
//        expected.put(8, colsByNumber);

        HashMap<Integer, List<Integer>> colsByBoxes = new HashMap<>();
        colsByBoxes.put(0, Arrays.asList(0,2));
        colsByBoxes.put(3, Arrays.asList(0,1));
        expected.put(1, colsByBoxes);

        colsByBoxes = new HashMap<>();
        colsByBoxes.put(0, Arrays.asList(0,1));
        expected.put(4, colsByBoxes);

        colsByBoxes = new HashMap<>();
        colsByBoxes.put(8, Arrays.asList(7,8));
        expected.put(8, colsByBoxes);

        var doubles = doublePairs.findCandidateDoublesInBoxes(markers);


        assertArrayEquals(expected.keySet().toArray(new Integer[0]), doubles.keySet().toArray(new Integer[0]));

        for (int box : expected.keySet()) {

            assertArrayEquals(expected.get(box).keySet().toArray(new Integer[0]), doubles.get(box).keySet().toArray(new Integer[0]));
            for (int number : expected.get(box).keySet())
                assertArrayEquals(expected.get(box).get(number).toArray(), doubles.get(box).get(number).toArray());

        }


    }

    @Test
    void testRemoveMarkersInRows() {

        var markers = SudokuUtils.updatePencilMarks(new int[81], null);

        var indices = new ArrayList<Integer>();
        indices.addAll(SudokuUtils.getBoxIndices(0));
        indices.addAll(SudokuUtils.getBoxIndices(27));
        indices.removeAll(Arrays.asList(0, 2, 27, 38));
        for (int i : indices)
            markers.put(i, markers.get(i).stream().filter(n -> n != 1).collect(Collectors.toList()));

        indices = new ArrayList<>();
        indices.addAll(SudokuUtils.getBoxIndices(3));
        indices.addAll(SudokuUtils.getBoxIndices(57));
        indices.addAll(SudokuUtils.getBoxIndices(30));
        indices.removeAll(Arrays.asList(3, 13, 57, 76, 30, 31, 32));
        for (int i : indices)
            markers.put(i, markers.get(i).stream().filter(n -> n != 2).collect(Collectors.toList()));

        var expected = TestUtils.deepCopyPencilMarks(markers);

        SudokuUtils.eliminateCandidateFromPositions(expected, 1, Arrays.asList(54, 63, 72, 56, 65, 74));
        SudokuUtils.eliminateCandidateFromPositions(expected, 2, Arrays.asList(30, 39, 48, 31, 40, 49));

        var removed = doublePairs.removeMarkers(markers);

        assertTrue(removed);
        assertArrayEquals(expected.keySet().toArray(new Integer[0]), markers.keySet().toArray(new Integer[0]));

        for (int i : expected.keySet())
            assertArrayEquals(expected.get(i).toArray(), markers.get(i).toArray());


    }


    @Test
    void testRemoveMarkersInRowsAndCols() {

        var markers = SudokuUtils.updatePencilMarks(new int[81], null);

        var indices = new ArrayList<Integer>();
        indices.addAll(SudokuUtils.getBoxIndices(0));
        indices.addAll(SudokuUtils.getBoxIndices(27));
        indices.removeAll(Arrays.asList(0, 2, 27, 38));
        for (int i : indices)
            markers.put(i, markers.get(i).stream().filter(n -> n != 1).collect(Collectors.toList()));

        indices = new ArrayList<>();
        indices.addAll(SudokuUtils.getBoxIndices(3));
        indices.addAll(SudokuUtils.getBoxIndices(57));
        indices.addAll(SudokuUtils.getBoxIndices(30));
        indices.removeAll(Arrays.asList(3, 13, 57, 76, 30, 31, 32));
        for (int i : indices)
            markers.put(i, markers.get(i).stream().filter(n -> n != 2).collect(Collectors.toList()));

        indices = new ArrayList<>();
        indices.addAll(SudokuUtils.getBoxIndices(1));
        indices.addAll(SudokuUtils.getBoxIndices(2));
        indices.removeAll(Arrays.asList(14, 23, 16,26));
        for (int i : indices)
            markers.put(i, markers.get(i).stream().filter(n -> n != 4).collect(Collectors.toList()));


        var expected = TestUtils.deepCopyPencilMarks(markers);

        SudokuUtils.eliminateCandidateFromPositions(expected, 1, Arrays.asList(54, 63, 72, 56, 65, 74));
        SudokuUtils.eliminateCandidateFromPositions(expected, 2, Arrays.asList(30, 39, 48, 31, 40, 49));
        SudokuUtils.eliminateCandidateFromPositions(expected, 4, Arrays.asList(9,10,11,18,19,20));

        var removed = doublePairs.removeMarkers(markers);

        assertTrue(removed);
        assertArrayEquals(expected.keySet().toArray(new Integer[0]), markers.keySet().toArray(new Integer[0]));

        for (int i : expected.keySet())
            assertArrayEquals(expected.get(i).toArray(), markers.get(i).toArray());


    }


    @Test
    void testRemoveMarkersReturnsFalseIfNothingRemoved() {

        var markers = SudokuUtils.updatePencilMarks(new int[81], null);

        var indices = new ArrayList<Integer>();
        indices.addAll(SudokuUtils.getBoxIndices(0));
        indices.addAll(SudokuUtils.getBoxIndices(27));
        indices.removeAll(Arrays.asList(0, 2, 27, 37));
        for (int i : indices)
            markers.put(i, markers.get(i).stream().filter(n -> n != 1).collect(Collectors.toList()));

        indices = new ArrayList<>();
        indices.addAll(SudokuUtils.getBoxIndices(3));
        indices.addAll(SudokuUtils.getBoxIndices(30));
        indices.removeAll(Arrays.asList(3,13,30,31,32));
        for (int i : indices)
            markers.put(i, markers.get(i).stream().filter(n -> n != 2).collect(Collectors.toList()));

        var expected = TestUtils.deepCopyPencilMarks(markers);

        var removed = doublePairs.removeMarkers(markers);

        assertFalse(removed);
        assertArrayEquals(expected.keySet().toArray(new Integer[0]), markers.keySet().toArray(new Integer[0]));

        for (int i : expected.keySet())
            assertArrayEquals(expected.get(i).toArray(), markers.get(i).toArray());


    }


}