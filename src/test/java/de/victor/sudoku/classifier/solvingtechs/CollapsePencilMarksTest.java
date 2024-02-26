package de.victor.sudoku.classifier.solvingtechs;

import de.victor.sudoku.SudokuUtils;
import de.victor.sudoku.TestData;
import de.victor.sudoku.classifier.solvingtechs.CollapsePencilMarks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CollapsePencilMarksTest {

    private CollapsePencilMarks collapsePencilMarks;

    @BeforeEach
    void setUp() {

        collapsePencilMarks = new CollapsePencilMarks();

    }

    @Test
    void execute() {

        var puzzle = TestData.UNIQUE_SOLVABLE_PUZZLE_TWO;
        var markers = SudokuUtils.updatePencilMarks(puzzle, null);

        var expectedPuzzle = Arrays.copyOf(puzzle, puzzle.length);

        expectedPuzzle[5] = 2;
        expectedPuzzle[79] = 5;

        var expectedMarkers =  SudokuUtils.updatePencilMarks(expectedPuzzle, null);

        var result = collapsePencilMarks.execute(puzzle, markers);

        assertEquals(2, result.addedValues());
        assertTrue(result.arePencilMarksAltered());

        assertArrayEquals(expectedPuzzle, result.puzzle());

        for (int i : result.pencilMarks().keySet())
            assertArrayEquals(result.pencilMarks().get(i).toArray(new Integer[0]), expectedMarkers.get(i).toArray(new Integer[0]));
    }

    @Test
    void eliminateNeighbourCandidates_MarkersChanged() {
        var number = 6;
        var idx = 5;

        var puzzle = new int[81];

        var markers = SudokuUtils.updatePencilMarks(puzzle, null);

        puzzle[idx] = number;
        var expected = SudokuUtils.updatePencilMarks(puzzle, null);

        var updated = collapsePencilMarks.eliminateNeighbourCandidates(idx, number, markers);

        assertTrue(updated);

        for (int i : expected.keySet())
            assertArrayEquals(expected.get(i).toArray(new Integer[0]), markers.get(i).toArray(new Integer[0]));
    }

    @Test
    void eliminateNeighbourCandidates_MarkersNotChanged() {
        var number = 6;
        var idx = 5;

        var puzzle = new int[81];
        puzzle[idx] = number;

        var markers = SudokuUtils.updatePencilMarks(puzzle, null);
        var expected = SudokuUtils.updatePencilMarks(puzzle, null);

        var updated = collapsePencilMarks.eliminateNeighbourCandidates(idx, number, markers);

        assertFalse(updated);

        for (int i : expected.keySet())
            assertArrayEquals(expected.get(i).toArray(new Integer[0]), markers.get(i).toArray(new Integer[0]));
    }




}