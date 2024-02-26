package de.victor.sudoku.classifier.solvingtechs;

import de.victor.sudoku.SudokuUtils;
import de.victor.sudoku.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class SwordFishTest {

    private SwordFish swordFish;

    @BeforeEach
    void setUp() {

        swordFish = new SwordFish();
    }


    @Test
    void executeInRows() {


        int[] puzzle = new int[] {
                5,6,3,0,0,0,9,8,0,
                1,8,4,9,5,3,2,6,7,
                0,0,7,0,6,8,0,0,3,
                0,0,0,6,0,0,4,3,8,
                4,0,6,8,3,7,0,9,2,
                3,0,8,0,0,4,6,7,0,
                6,4,0,3,8,0,7,2,9,
                8,0,9,0,2,6,3,0,0,
                0,3,0,0,0,9,8,1,6
        };

        HashMap<Integer, List<Integer>> markers = SudokuUtils.updatePencilMarks(puzzle, null);
        markers.put(48, markers.get(48).stream().filter(i -> i != 5).collect(Collectors.toList()));

        var expectedPuzzle = Arrays.copyOf(puzzle, puzzle.length);
        expectedPuzzle[48] = 2;
        var expectedMarkers = TestUtils.deepCopyPencilMarks(markers);
        expectedMarkers.remove(48);
        expectedMarkers.put(3, Arrays.asList(4,7));
        expectedMarkers.put(21, Arrays.asList(1,4));
        expectedMarkers.put(28, Arrays.asList(2,5,7,9));
        expectedMarkers.put(32, Arrays.asList(1,5));
        expectedMarkers.put(46, Arrays.asList(5,9));


        var result = swordFish.execute(puzzle, markers);

        assertTrue(result.arePencilMarksAltered());
        assertEquals(1, result.addedValues());
        assertArrayEquals(expectedPuzzle, result.puzzle());
        for (int i : expectedMarkers.keySet()) {
            assertArrayEquals(expectedMarkers.get(i).toArray(), result.pencilMarks().get(i).toArray());
        }


    }

    @Test
    void executeInCols() {

        int[] puzzle = new int[] {
                0,8,6,3,4,0,0,1,5,
                3,0,4,0,0,0,0,8,6,
                0,9,0,8,6,0,7,4,3,
                0,0,3,0,8,6,0,9,0,
                0,2,8,0,3,0,6,5,0,
                9,6,0,4,7,0,8,3,0,
                8,3,7,6,0,4,0,2,9,
                1,0,2,7,9,3,0,6,8,
                6,0,9,0,2,8,3,7,0
        };

        HashMap<Integer, List<Integer>> markers = SudokuUtils.updatePencilMarks(puzzle, null);
        markers.put(30, markers.get(30).stream().filter(i -> i != 5).collect(Collectors.toList()));

        var expectedPuzzle = Arrays.copyOf(puzzle, puzzle.length);
        expectedPuzzle[30] = 2;

        var expectedMarkers = TestUtils.deepCopyPencilMarks(markers);
        expectedMarkers.remove(30);

        expectedMarkers.put(12, Arrays.asList(5,9));
        expectedMarkers.put(14, Arrays.asList(2,5,7,9));
        expectedMarkers.put(33, Arrays.asList(1,4));
        expectedMarkers.put(35, Arrays.asList(4,7));
        expectedMarkers.put(50, Arrays.asList(1,5));

        var result = swordFish.execute(puzzle, markers);


        assertTrue(result.arePencilMarksAltered());
        assertEquals(1, result.addedValues());
        assertArrayEquals(expectedPuzzle, result.puzzle());
        for (int i : expectedMarkers.keySet()) {
            assertArrayEquals(expectedMarkers.get(i).toArray(), result.pencilMarks().get(i).toArray());
        }

    }


    @Test
    void executeNothingRemoved() {


        int[] puzzle = new int[] {
                5,6,3,0,0,0,9,8,0,
                1,8,4,9,5,3,2,6,7,
                0,0,7,0,6,8,0,0,3,
                0,0,0,6,0,0,4,3,8,
                4,0,6,8,3,7,0,9,2,
                3,0,8,0,0,4,6,7,0,
                6,4,0,3,8,0,7,2,9,
                8,0,9,0,2,6,3,0,0,
                0,3,0,0,0,9,8,1,6
        };

        HashMap<Integer, List<Integer>> markers = SudokuUtils.updatePencilMarks(puzzle, null);
        markers.put(3, Arrays.asList(2,4,7));
        markers.put(21, Arrays.asList(1,2,4));
        markers.put(28, Arrays.asList(2,5,7,9));
        markers.put(32, Arrays.asList(1,2,5));
        markers.put(46, Arrays.asList(2,5,9));
        markers.put(48, Arrays.asList(2, 5));


        var expectedPuzzle = Arrays.copyOf(puzzle, puzzle.length);
        var expectedMarkers = TestUtils.deepCopyPencilMarks(markers);

        var result = swordFish.execute(puzzle, markers);

        assertFalse(result.arePencilMarksAltered());
        assertEquals(0, result.addedValues());
        assertArrayEquals(expectedPuzzle, result.puzzle());
        for (int i : expectedMarkers.keySet())
            assertArrayEquals(expectedMarkers.get(i).toArray(), result.pencilMarks().get(i).toArray());


    }





    @Test
    void swordfishRemoveMarkers() {

        int[] puzzle = new int[] {
                5,6,3,0,0,0,9,8,0,
                1,8,4,9,5,3,2,6,7,
                0,0,7,0,6,8,0,0,3,
                0,0,0,6,0,0,4,3,8,
                4,0,6,8,3,7,0,9,2,
                3,0,8,0,0,4,6,7,0,
                6,4,0,3,8,0,7,2,9,
                8,0,9,0,2,6,3,0,0,
                0,3,0,0,0,9,8,1,6
        };


        HashMap<Integer, List<Integer>> markers = SudokuUtils.updatePencilMarks(puzzle, null);

        var excepted = TestUtils.deepCopyPencilMarks(markers);

        var altered = excepted.get(3).stream().filter(i -> i != 1).collect(Collectors.toList());
        excepted.put(3, altered);

        altered = excepted.get(28).stream().filter(i -> i != 1).collect(Collectors.toList());
        excepted.put(28, altered);

        altered = excepted.get(46).stream().filter(i -> i != 1).collect(Collectors.toList());
        excepted.put(46, altered);

        altered = excepted.get(48).stream().filter(i -> i != 1).collect(Collectors.toList());
        excepted.put(48, altered);

        var removed = swordFish.swordfishRemoveMarkers(markers);

        var e = excepted.keySet().stream().sorted().toArray();
        var m = markers.keySet().stream().sorted().toArray();

        assertTrue(removed);
        assertArrayEquals(e, m);
        for (int i: excepted.keySet()) {
            assertArrayEquals(excepted.get(i).toArray(), markers.get(i).toArray());
        }


    }

    @Test
    void swordfishRemoveMarkers_returnsFalseIfPencilmarksNotAltered() {

        int[] puzzle = new int[] {
                5,6,3,0,0,0,9,8,0,
                1,8,4,9,5,3,2,6,7,
                0,0,7,0,6,8,0,0,3,
                0,0,0,6,0,0,4,3,8,
                4,0,6,8,3,7,0,9,2,
                3,0,8,0,0,4,6,7,0,
                6,4,0,3,8,0,7,2,9,
                8,0,9,0,2,6,3,0,0,
                0,3,0,0,0,9,8,1,6
        };


        HashMap<Integer, List<Integer>> markers = SudokuUtils.updatePencilMarks(puzzle, null);

        var altered = markers.get(3).stream().filter(i -> i != 1).collect(Collectors.toList());
        markers.put(3, altered);
        altered = markers.get(28).stream().filter(i -> i != 1).collect(Collectors.toList());
        markers.put(28, altered);
        altered = markers.get(46).stream().filter(i -> i != 1).collect(Collectors.toList());
        markers.put(46, altered);
        altered = markers.get(48).stream().filter(i -> i != 1).collect(Collectors.toList());
        markers.put(48, altered);

        var excepted = TestUtils.deepCopyPencilMarks(markers);

        var removed = swordFish.swordfishRemoveMarkers(markers);

        var e = excepted.keySet().stream().sorted().toArray();
        var m = markers.keySet().stream().sorted().toArray();

        assertFalse(removed);
        assertArrayEquals(e, m);
        for (int i: excepted.keySet()) {
            assertArrayEquals(excepted.get(i).toArray(), markers.get(i).toArray());
        }

    }

    @Test
    void swordfishCompareRows() {

        var markers = new HashMap<>(Map.ofEntries(
                new AbstractMap.SimpleEntry<>(1, Arrays.asList(1,2)),
                new AbstractMap.SimpleEntry<>(2, Arrays.asList(1,2)),
                new AbstractMap.SimpleEntry<>(3, List.of(2)),
                new AbstractMap.SimpleEntry<>(10, Arrays.asList(1,2)),
                new AbstractMap.SimpleEntry<>(20, Arrays.asList(1,2)),
                new AbstractMap.SimpleEntry<>(21, Arrays.asList(1,2)),
                new AbstractMap.SimpleEntry<>(29, Arrays.asList(1,2)),
                new AbstractMap.SimpleEntry<>(30, Arrays.asList(1,2)),
                new AbstractMap.SimpleEntry<>(31, Arrays.asList(1,2)),
                new AbstractMap.SimpleEntry<>(37, Arrays.asList(1,2)),
                new AbstractMap.SimpleEntry<>(39, Arrays.asList(1,2)),
                new AbstractMap.SimpleEntry<>(48, Arrays.asList(1,2))
        ));

        var expectedMarkers = TestUtils.deepCopyPencilMarks(markers);
        expectedMarkers.put(10, Arrays.asList(2));
        expectedMarkers.put(29, Arrays.asList(2));
        expectedMarkers.put(30, Arrays.asList(2));
        expectedMarkers.put(48, Arrays.asList(2));

        var doubles = SudokuUtils.findCandidatePairInRow(markers);

        var result = swordFish.swordfishCompareRows(doubles, markers);

        assertTrue(result);

        for (int i : expectedMarkers.keySet())
            assertArrayEquals(expectedMarkers.get(i).toArray(), markers.get(i).toArray());


    }

    @Test
    void swordfishCompareRowsNothingRemoved() {

        var markers = new HashMap<>(Map.ofEntries(
                new AbstractMap.SimpleEntry<>(1, Arrays.asList(1,2)),
                new AbstractMap.SimpleEntry<>(2, Arrays.asList(1,2)),
                new AbstractMap.SimpleEntry<>(3, List.of(2)),
                new AbstractMap.SimpleEntry<>(10, Arrays.asList(2)),
                new AbstractMap.SimpleEntry<>(20, Arrays.asList(1,2)),
                new AbstractMap.SimpleEntry<>(21, Arrays.asList(1,2)),
                new AbstractMap.SimpleEntry<>(29, Arrays.asList(2)),
                new AbstractMap.SimpleEntry<>(30, Arrays.asList(2)),
                new AbstractMap.SimpleEntry<>(31, Arrays.asList(1,2)),
                new AbstractMap.SimpleEntry<>(37, Arrays.asList(1,2)),
                new AbstractMap.SimpleEntry<>(39, Arrays.asList(1,2))
        ));

        var expectedMarkers = TestUtils.deepCopyPencilMarks(markers);

        var doubles = SudokuUtils.findCandidatePairInRow(markers);

        var result = swordFish.swordfishCompareRows(doubles, markers);

        assertFalse(result);

        for (int i : expectedMarkers.keySet())
            assertArrayEquals(expectedMarkers.get(i).toArray(), markers.get(i).toArray());


    }
}