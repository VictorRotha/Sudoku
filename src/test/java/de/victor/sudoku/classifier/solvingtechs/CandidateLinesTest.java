package de.victor.sudoku.classifier.solvingtechs;

import de.victor.sudoku.SudokuUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CandidateLinesTest {

    private CandidateLines candidateLines;

    @BeforeEach
    void setup() {
        candidateLines = new CandidateLines();

    }

    @Test
    void testExecute() {

        var puzzle = new int[81];
        var markers = SudokuUtils.updatePencilMarks(puzzle, null);

        for (int i : new int[] {9,10,11,18,19,20})
            markers.put(i, markers.get(i).stream().filter(n -> n != 1).collect(Collectors.toList()));

        for (int i : new int[] {1,2,10,11,19,20, 37,38,46,47})
            markers.put(i, markers.get(i).stream().filter(n -> n != 2).collect(Collectors.toList()));

        markers.put(30, Arrays.asList(2, 3));
        markers.put(54, Arrays.asList(2, 4));

        var result = candidateLines.execute(puzzle, markers);

        assertEquals(2, result.addedValues());
        assertTrue(result.arePencilMarksAltered());
        assertEquals(3, puzzle[30]);
        assertEquals(4, puzzle[54]);

    }

    @Test
    void testExecute_NothingCollapsedButMarkersRemoved() {

        var puzzle = new int[81];
        var markers = SudokuUtils.updatePencilMarks(puzzle, null);

        for (int i : new int[] {9,10,11,18,19,20})
            markers.put(i, markers.get(i).stream().filter(n -> n != 1).collect(Collectors.toList()));

        for (int i : new int[] {1,2,10,11,19,20, 37,38,46,47})
            markers.put(i, markers.get(i).stream().filter(n -> n != 2).collect(Collectors.toList()));

        var result = candidateLines.execute(puzzle, markers);

        assertEquals(0, result.addedValues());
        assertTrue(result.arePencilMarksAltered());
        assertEquals(81, markers.size());

    }

    @Test
    void testExecute_NothingCollapsedANdNoMarkerRemoved() {

        var puzzle = new int[81];
        var markers = SudokuUtils.updatePencilMarks(puzzle, null);

        var result = candidateLines.execute(puzzle, markers);

        assertEquals(0, result.addedValues());
        assertFalse(result.arePencilMarksAltered());
        assertEquals(81, markers.size());

    }

    @Test
    void testRemoveMarkers() {

        var puzzle = new int[81];
        var markers = SudokuUtils.updatePencilMarks(puzzle, null);

        for (int i : new int[] {9,10,11,18,19,20})
            markers.put(i, markers.get(i).stream().filter(n -> n != 1).collect(Collectors.toList()));

        for (int i : new int[] {1,2,10,11,19,20, 37,38,46,47})
            markers.put(i, markers.get(i).stream().filter(n -> n != 2).collect(Collectors.toList()));

        var removed = candidateLines.removeMarkers(markers);

        assertTrue(removed);

        for (int i : new int[] {3,4,5,6,7,8})
            assertFalse(markers.get(i).contains(1));

        for (int i : new int[] {27,36,45,54,63,72, 30,31,32,33,34,35})
            assertFalse(markers.get(i).contains(2));



    }

    @Test
    void testAreInSameColumn_returnsTrueForSingleIndex() {

        List<Integer> indices = new ArrayList<>();
        indices.add(6);

        assertTrue(candidateLines.areIndicesInSameColumn(indices));

        indices.clear();
        indices.add(80);

        assertTrue(candidateLines.areIndicesInSameColumn(indices));


    }

    @Test
    void testAreInSameColumn_returnsTrueForTwoIndicesInSameColumn() {

        List<Integer> indices = new ArrayList<>(Arrays.asList(4, 13));

        assertTrue(candidateLines.areIndicesInSameColumn(indices));

        indices = new ArrayList<>(Arrays.asList(6, 33));

        assertTrue(candidateLines.areIndicesInSameColumn(indices));


    }

    @Test
    void testAreInSameColumn_returnsTrueForThreeIndicesInSameColumn() {

        List<Integer> indices = new ArrayList<>(Arrays.asList(1, 10, 19));

        assertTrue(candidateLines.areIndicesInSameColumn(indices));

        indices = new ArrayList<>(Arrays.asList(5, 14, 41));

        assertTrue(candidateLines.areIndicesInSameColumn(indices));

    }

    @Test
    void testAreInSameColumn_returnsFalseForTwoIndicesInDifferentColumns() {

        List<Integer> indices = new ArrayList<>(Arrays.asList(1, 12));

        assertFalse(candidateLines.areIndicesInSameColumn(indices));

        indices = new ArrayList<>(Arrays.asList(5, 15));

        assertFalse(candidateLines.areIndicesInSameColumn(indices));

    }

    @Test
    void testAreInSameColumn_returnsFalseForThreeIndicesInDifferentColumns() {

        List<Integer> indices = new ArrayList<>(Arrays.asList(1, 11, 21));

        assertFalse(candidateLines.areIndicesInSameColumn(indices));

        indices = new ArrayList<>(Arrays.asList(5, 15, 39));

        assertFalse(candidateLines.areIndicesInSameColumn(indices));

    }
}