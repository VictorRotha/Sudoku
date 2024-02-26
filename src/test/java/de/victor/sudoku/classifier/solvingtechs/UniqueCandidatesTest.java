package de.victor.sudoku.classifier.solvingtechs;

import de.victor.sudoku.SudokuUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class UniqueCandidatesTest {

    private UniqueCandidates uniqueCandidates;

    @BeforeEach
    void setup() {
        uniqueCandidates = new UniqueCandidates();
    }


    @Test
    void testExecute() {

        var puzzle = new int[81];
        var markers = SudokuUtils.updatePencilMarks(puzzle, null);

        var cells = SudokuUtils.getRowIndices(0);
        cells.addAll(SudokuUtils.getColumnIndices(8));

        cells = cells.stream().filter(i -> i != 8 && i != 17).collect(Collectors.toList());
        for (int i : cells)
            markers.put(i, markers.get(i).stream().filter(n -> n != 1).collect(Collectors.toList()));

        cells = SudokuUtils.getColumnIndices(9);
        cells.addAll(SudokuUtils.getBoxIndices(9));
        cells = cells.stream().filter(i -> i != 9 && i != 19).collect(Collectors.toList());
        for (int i : cells)
            markers.put(i, markers.get(i).stream().filter(n -> n != 2).collect(Collectors.toList()));

        cells = SudokuUtils.getBoxIndices(30);
        cells.addAll(SudokuUtils.getRowIndices(30));
        cells = cells.stream().filter(i -> i != 30 && i != 33).collect(Collectors.toList());
        for (int i : cells)
            markers.put(i, markers.get(i).stream().filter(n -> n != 3).collect(Collectors.toList()));


        var result = uniqueCandidates.execute(puzzle, markers);
        assertEquals(3, result.addedValues());
        assertTrue(result.arePencilMarksAltered());

        assertEquals(1, result.puzzle()[8]);
        assertEquals(2, result.puzzle()[9]);
        assertEquals(3, result.puzzle()[30]);

        for (int i : new int[] {8, 9, 30}) {
            assertFalse(result.pencilMarks().containsKey(i));

        }




    }

    @Test
    void testEliminateUniques() {

        var puzzle = new int[81];
        var markers = SudokuUtils.updatePencilMarks(puzzle, null);
        for (int i = 0; i < 8; i++)
            markers.put(i, markers.get(i).stream().filter(n -> n != 1).collect(Collectors.toList()));
        var column = SudokuUtils.getColumnIndices(0);
        column.remove((Integer) 9);
        for (int i : column)
            markers.put(i, markers.get(i).stream().filter(n -> n != 2).collect(Collectors.toList()));
        var box = SudokuUtils.getBoxIndices(30);
        box.remove((Integer) 30);
        for (int i : box)
            markers.put(i, markers.get(i).stream().filter(n -> n != 3).collect(Collectors.toList()));


        var result = uniqueCandidates.eliminateUniques(puzzle, markers);

        assertEquals(3, result);

        assertEquals(1, puzzle[8]);
        assertEquals(2, puzzle[9]);
        assertEquals(3, puzzle[30]);

        assertFalse(markers.containsKey(8));
        assertFalse(markers.containsKey(9));
        assertFalse(markers.containsKey(30));


    }

    @Test
    void testExecuteTwoMarkersInSubset() {

        var puzzle = new int[81];
        var markers = SudokuUtils.updatePencilMarks(puzzle, null);
        for (int i = 0; i < 8; i++)
            markers.put(i, markers.get(i).stream().filter(n -> n != 1).collect(Collectors.toList()));

        var column = Arrays.asList(0,18,27,36,45,54,63,72,10,11,12,13,14,15,16);
         for (int i : column)
            markers.put(i, markers.get(i).stream().filter(n -> n != 2).collect(Collectors.toList()));

        var box = SudokuUtils.getBoxIndices(30);
        box.remove((Integer) 30);
        for (int i : box)
            markers.put(i, markers.get(i).stream().filter(n -> n != 3).collect(Collectors.toList()));


        var result = uniqueCandidates.execute(puzzle, markers);

        assertEquals(3, result.addedValues());

        assertEquals(1, result.puzzle()[8]);
        assertEquals(2, result.puzzle()[9]);
        assertEquals(3, result.puzzle()[30]);
        assertEquals(0, result.puzzle()[17]);

        assertFalse(result.pencilMarks().containsKey(8));
        assertFalse(result.pencilMarks().containsKey(9));
        assertFalse(result.pencilMarks().containsKey(30));
        assertTrue(result.pencilMarks().containsKey(17));


    }

    @Test
    void testFindUniqueCandidates() {

       var puzzle = new int[81];
       var markers = SudokuUtils.updatePencilMarks(puzzle, null);

       for (int i = 0; i < 8; i++)
           markers.put(i, markers.get(i).stream().filter(n -> n != 1).collect(Collectors.toList()));

       var cells = Arrays.asList(0,1,2,3,4,5,6,7,8);

       var result = uniqueCandidates.findUniqueCandidates(cells, markers);

       assertEquals(1, result.size());
       assertTrue(result.containsKey(8));
       assertEquals(1, result.get(8));

    }
}