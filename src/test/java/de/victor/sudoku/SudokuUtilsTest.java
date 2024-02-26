package de.victor.sudoku;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class SudokuUtilsTest {


    @Test
    void testGetIndicesFromAbbr_ThrowsExceptionIfAbbrUnknown() {

        String abbr = "x2";


        assertThrows(IllegalArgumentException.class, () -> SudokuUtils.getIndicesFromAbbr(abbr));


    }

    @Test
    void testGetIndicesFromAbbr_ReturnsRow() {

        String abbr = "r0";
        Integer[] expected = new Integer[] {0,1,2,3,4,5,6,7,8};

        List<Integer> indices = SudokuUtils.getIndicesFromAbbr(abbr);
        Collections.sort(indices);

        assertArrayEquals(expected, indices.toArray(new Integer[0]));

        abbr = "r4";
        expected = new Integer[] {36,37,38,39,40,41,42,43,44};

        indices = SudokuUtils.getIndicesFromAbbr(abbr);
        Collections.sort(indices);

        assertArrayEquals(expected, indices.toArray(new Integer[0]));

        abbr = "r8";
        expected = new Integer[] {72,73,74,75,76,77,78,79,80};

        indices = SudokuUtils.getIndicesFromAbbr(abbr);
        Collections.sort(indices);

        assertArrayEquals(expected, indices.toArray(new Integer[0]));

    }

    @Test
    void testGetIndicesFromAbbr_ReturnsColumn() {

        String abbr = "c8";
        Integer[] expected = new Integer[] {8, 17, 26, 35, 44, 53, 62, 71, 80};

        List<Integer> indices = SudokuUtils.getIndicesFromAbbr(abbr);
        Collections.sort(indices);


        assertArrayEquals(expected, indices.toArray(new Integer[0]));

        abbr = "c1";
        expected = new Integer[] {1, 10, 19, 28, 37, 46, 55, 64, 73};

        indices = SudokuUtils.getIndicesFromAbbr(abbr);
        Collections.sort(indices);

        assertArrayEquals(expected, indices.toArray(new Integer[0]));

        abbr = "c5";
        expected = new Integer[] {5, 14, 23, 32, 41, 50, 59, 68, 77};

        indices = SudokuUtils.getIndicesFromAbbr(abbr);
        Collections.sort(indices);

        assertArrayEquals(expected, indices.toArray(new Integer[0]));

    }

    @Test
    void testGetIndicesFromAbbr_ReturnsBox() {

        String abbr = "b0";
        Integer[] expected = new Integer[] {0,1,2,9,10,11,18,19,20};

        List<Integer> indices = SudokuUtils.getIndicesFromAbbr(abbr);
        Collections.sort(indices);

        assertArrayEquals(expected, indices.toArray(new Integer[0]));

        abbr = "b1";
        expected = new Integer[] {3,4,5,12,13,14,21, 22, 23};

        indices = SudokuUtils.getIndicesFromAbbr(abbr);
        Collections.sort(indices);

        assertArrayEquals(expected, indices.toArray(new Integer[0]));

        abbr = "b2";
        expected = new Integer[] {6,7,8,15,16,17,24,25,26};

        indices = SudokuUtils.getIndicesFromAbbr(abbr);
        Collections.sort(indices);

        assertArrayEquals(expected, indices.toArray(new Integer[0]));


        abbr = "b4";
        expected = new Integer[] {30, 31, 32, 39, 40, 41, 48, 49, 50};

        indices = SudokuUtils.getIndicesFromAbbr(abbr);
        Collections.sort(indices);

        assertArrayEquals(expected, indices.toArray(new Integer[0]));

        abbr = "b8";
        expected = new Integer[] {60, 61, 62, 69, 70, 71, 78, 79, 80};

        indices = SudokuUtils.getIndicesFromAbbr(abbr);
        Collections.sort(indices);

        assertArrayEquals(expected, indices.toArray(new Integer[0]));

    }


    @Test
    void testGetBoxIndices() {

        List<Integer> indices = SudokuUtils.getBoxIndices(5);
        Collections.sort(indices);

        Integer[] expected = new Integer[] {3,4,5,12,13,14,21,22,23};

        assertArrayEquals(expected, indices.toArray(new Integer[0]));

        indices = SudokuUtils.getBoxIndices(10);
        Collections.sort(indices);
        expected = new Integer[] {0, 1, 2, 9, 10, 11, 18, 19, 20};

        assertArrayEquals(expected, indices.toArray(new Integer[0]));


    }

    @Test
    void testGetRowIndices() {

        List<Integer> indices = SudokuUtils.getRowIndices(5);
        Collections.sort(indices);

        Integer[] expected = new Integer[] {0,1,2,3,4,5,6,7,8};

        assertArrayEquals(expected, indices.toArray(new Integer[0]));

        indices = SudokuUtils.getRowIndices(10);
        Collections.sort(indices);
        expected = new Integer[] {9, 10, 11, 12, 13, 14, 15, 16, 17};

        assertArrayEquals(expected, indices.toArray(new Integer[0]));

    }

    @Test
    void testGetColumnIndices() {

        List<Integer> indices = SudokuUtils.getColumnIndices(5);
        Collections.sort(indices);

        Integer[] expected = new Integer[] {5, 14, 23, 32, 41, 50, 59, 68, 77};

        assertArrayEquals(expected, indices.toArray(new Integer[0]));

        indices = SudokuUtils.getColumnIndices(10);
        Collections.sort(indices);
        expected = new Integer[] {1, 10, 19, 28, 37, 46, 55, 64, 73};

        assertArrayEquals(expected, indices.toArray(new Integer[0]));

        indices = SudokuUtils.getColumnIndices(78);
        Collections.sort(indices);
        expected = new Integer[] {6, 15, 24, 33, 42, 51, 60, 69, 78};
        assertArrayEquals(expected, indices.toArray(new Integer[0]));

    }

    @Test
    void testGetNeighbourPositions() {

        List<Integer> indices = SudokuUtils.getNeighbourPositions(5);
        Collections.sort(indices);

        List<Integer> expected = new ArrayList<>(
                Arrays.asList(
                        5, 14, 23, 32, 41, 50, 59, 68, 77,
                        0, 1, 2, 3, 4, 6, 7, 8,
                        12, 13, 21, 22));

        Collections.sort(expected);

        assertArrayEquals(expected.toArray(new Integer[0]), indices.toArray(new Integer[0]));

        indices = SudokuUtils.getNeighbourPositions(10);
        Collections.sort(indices);

        expected = new ArrayList<>(Arrays.asList(
                1, 10, 19, 28, 37, 46, 55, 64, 73,
                9, 11, 12, 13, 14, 15, 16, 17,
                0, 2, 18, 20
        ));

        Collections.sort(expected);

        assertArrayEquals(expected.toArray(new Integer[0]), indices.toArray(new Integer[0]));

    }


    @Test
    void testEliminateCandidateFromPosition_returnsFalseIfNothingRemovedAndMarkersNotChanged() {


        int[] puzzle = TestData.UNIQUE_SOLVABLE_PUZZLE_ONE;
        var markers = SudokuUtils.updatePencilMarks(puzzle, null);
        var expected = TestUtils.deepCopyPencilMarks(markers);

        List<Integer> positions = new ArrayList<>(Arrays.asList(0,1,2, 3,5,6,7,8));
        var result = SudokuUtils.eliminateCandidateFromPositions(markers, 2, positions);


        assertFalse(result);
        for (int i : expected.keySet())
            assertArrayEquals(expected.get(i).toArray(), markers.get(i).toArray());




    }

    @Test
    void testEliminateCandidateFromPosition_returnsTrueIfRemovedAndMarkersChanged() {

        int[] puzzle = TestData.UNIQUE_SOLVABLE_PUZZLE_ONE;
        var markers = SudokuUtils.updatePencilMarks(puzzle, null);

        var expected = TestUtils.deepCopyPencilMarks(markers);
        expected.put(4, expected.get(4).stream().filter(i -> i != 5).collect(Collectors.toList()));

        List<Integer> positions = new ArrayList<>(Arrays.asList(0,1,2, 4,5,6,7,8));
        var result = SudokuUtils.eliminateCandidateFromPositions(markers, 5, positions);



        assertTrue(result);
        for (int i : expected.keySet())
            assertArrayEquals(expected.get(i).toArray(), markers.get(i).toArray());

    }


    @Test
    void testGetIndicesByNumbers() {

        HashMap<Integer, List<Integer>> markers = new HashMap<>();
        markers.put(0, new ArrayList<>(Arrays.asList(2, 3)));
        markers.put(2, new ArrayList<>(Arrays.asList(4, 6, 9)));
        markers.put(3, new ArrayList<>(Arrays.asList(4, 6)));
        markers.put(4, new ArrayList<>(Arrays.asList(3, 7)));
        markers.put(7, new ArrayList<>(Arrays.asList(2, 3, 7)));
        markers.put(8, new ArrayList<>(List.of(9)));
        markers.put(10, new ArrayList<>(List.of(1)));
        markers.put(20, new ArrayList<>(List.of(1, 8)));

        List<Integer> indices = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));

        HashMap<Integer, List<Integer>> expected = new HashMap<>();
        expected.put(2, new ArrayList<>(Arrays.asList(0, 7)));
        expected.put(3, new ArrayList<>(Arrays.asList(0, 4, 7)));
        expected.put(4, new ArrayList<>(Arrays.asList(2, 3)));
        expected.put(6, new ArrayList<>(Arrays.asList(2, 3)));
        expected.put(7, new ArrayList<>(Arrays.asList(4, 7)));
        expected.put(9, new ArrayList<>(Arrays.asList(2, 8)));


        HashMap<Integer, List<Integer>> indicesByNumbers = SudokuUtils.getIndicesByNumber(indices, markers);

        assertEquals(expected.keySet().size(), indicesByNumbers.keySet().size());

        for (int i : expected.keySet()) {

            List<Integer> expectedCandidates = expected.get(i);
            List<Integer> indicesCandidates = indicesByNumbers.get(i);

            Collections.sort(expectedCandidates);
            Collections.sort(indicesCandidates);

            assertArrayEquals(expectedCandidates.toArray(), indicesCandidates.toArray());

        }


    }

    @Test
    void rotatePencilmarksRight() {

        HashMap<Integer, List<Integer>> markers = new HashMap<>();
        markers.put(0, new ArrayList<>(Arrays.asList(1,2)));
        markers.put(4, new ArrayList<>(Arrays.asList(1,3)));
        markers.put(8, new ArrayList<>(Arrays.asList(1,4)));
        markers.put(36, new ArrayList<>(Arrays.asList(2,3)));
        markers.put(40, new ArrayList<>(Arrays.asList(2,4)));
        markers.put(44, new ArrayList<>(Arrays.asList(2,5)));
        markers.put(72, new ArrayList<>(Arrays.asList(3,4)));
        markers.put(76, new ArrayList<>(Arrays.asList(3,5)));
        markers.put(80, new ArrayList<>(Arrays.asList(3,6)));

        HashMap<Integer, List<Integer>> expected = new HashMap<>();

        expected.put(0, new ArrayList<>(Arrays.asList(3,4)));
        expected.put(4, new ArrayList<>(Arrays.asList(2,3)));
        expected.put(8, new ArrayList<>(Arrays.asList(1,2)));
        expected.put(36, new ArrayList<>(Arrays.asList(3,5)));
        expected.put(40, new ArrayList<>(Arrays.asList(2,4)));
        expected.put(44, new ArrayList<>(Arrays.asList(1,3)));
        expected.put(72, new ArrayList<>(Arrays.asList(3,6)));
        expected.put(76, new ArrayList<>(Arrays.asList(2,5)));
        expected.put(80, new ArrayList<>(Arrays.asList(1,4)));



        var rotated = SudokuUtils.rotatePencilmarksRight(markers);

        var expectedArray = expected.keySet().toArray(new Integer[0]);
        var rotatedArray = rotated.keySet().toArray(new Integer[0]);

        Arrays.sort(expectedArray);
        Arrays.sort(rotatedArray);

        assertArrayEquals(expectedArray, rotatedArray);
        for (int i : rotated.keySet())
            assertArrayEquals(expected.get(i).toArray(), rotated.get(i).toArray());


    }

    @Test
    void rotatePencilmarksLeft() {

        HashMap<Integer, List<Integer>> markers = new HashMap<>();
        markers.put(0, new ArrayList<>(Arrays.asList(3,4)));
        markers.put(4, new ArrayList<>(Arrays.asList(2,3)));
        markers.put(8, new ArrayList<>(Arrays.asList(1,2)));
        markers.put(36, new ArrayList<>(Arrays.asList(3,5)));
        markers.put(40, new ArrayList<>(Arrays.asList(2,4)));
        markers.put(44, new ArrayList<>(Arrays.asList(1,3)));
        markers.put(72, new ArrayList<>(Arrays.asList(3,6)));
        markers.put(76, new ArrayList<>(Arrays.asList(2,5)));
        markers.put(80, new ArrayList<>(Arrays.asList(1,4)));

        HashMap<Integer, List<Integer>> expected = new HashMap<>();
        expected.put(0, new ArrayList<>(Arrays.asList(1,2)));
        expected.put(4, new ArrayList<>(Arrays.asList(1,3)));
        expected.put(8, new ArrayList<>(Arrays.asList(1,4)));
        expected.put(36, new ArrayList<>(Arrays.asList(2,3)));
        expected.put(40, new ArrayList<>(Arrays.asList(2,4)));
        expected.put(44, new ArrayList<>(Arrays.asList(2,5)));
        expected.put(72, new ArrayList<>(Arrays.asList(3,4)));
        expected.put(76, new ArrayList<>(Arrays.asList(3,5)));
        expected.put(80, new ArrayList<>(Arrays.asList(3,6)));


        var rotated =SudokuUtils.rotatePencilmarksLeft(markers);

        var expectedArray = expected.keySet().toArray(new Integer[0]);
        var rotatedArray = rotated.keySet().toArray(new Integer[0]);

        Arrays.sort(expectedArray);
        Arrays.sort(rotatedArray);

        assertArrayEquals(expectedArray, rotatedArray);
        for (int i : rotated.keySet())
            assertArrayEquals(expected.get(i).toArray(), rotated.get(i).toArray());


    }


    @Test
    void findDoublesInSubMap() {

        HashMap<Integer, List<Integer>> subMap = new HashMap<>();
        subMap.put(1, new ArrayList<>(Arrays.asList(2, 3)));
        subMap.put(2, new ArrayList<>(Arrays.asList(2, 3)));
        subMap.put(3, new ArrayList<>(Arrays.asList(1, 2)));
        subMap.put(9, new ArrayList<>(Arrays.asList(1, 2, 6)));
        subMap.put(10, new ArrayList<>(Arrays.asList(1, 3, 5, 7)));
        subMap.put(11, new ArrayList<>(Arrays.asList(1, 4, 5, 6, 7)));
        subMap.put(12, new ArrayList<>(Arrays.asList(2, 5, 6, 7)));

        HashMap<Integer, List<Integer>> expected = new HashMap<>();
        expected.put(1, new ArrayList<>(Arrays.asList(2, 3)));
        expected.put(2, new ArrayList<>(Arrays.asList(2, 3)));
        expected.put(3, new ArrayList<>(List.of(1)));
        expected.put(9, new ArrayList<>(Arrays.asList(1, 6)));
        expected.put(10, new ArrayList<>(Arrays.asList(1, 5, 7)));
        expected.put(11, new ArrayList<>(Arrays.asList(1, 4, 5, 6, 7)));
        expected.put(12, new ArrayList<>(Arrays.asList(5, 6, 7)));

        var result = SudokuUtils.findDoublesInSubMap(subMap);

        for (int i : expected.keySet()) {

            List<Integer> expectedCandidates = expected.get(i);
            List<Integer> resultCandidates = result.get(i);

            Collections.sort(expectedCandidates);
            Collections.sort(resultCandidates);

            assertArrayEquals(expectedCandidates.toArray(), resultCandidates.toArray());
        }


    }

    @Test
    void findDoublesInSubMap_TwoPairs() {

        HashMap<Integer, List<Integer>> subMap = new HashMap<>();
        subMap.put(1, new ArrayList<>(Arrays.asList(2, 3)));
        subMap.put(2, new ArrayList<>(Arrays.asList(2, 3)));
        subMap.put(3, new ArrayList<>(Arrays.asList(1, 3, 6)));
        subMap.put(9, new ArrayList<>(Arrays.asList(1, 2, 6)));
        subMap.put(10, new ArrayList<>(Arrays.asList(1, 3, 5, 7)));
        subMap.put(11, new ArrayList<>(Arrays.asList(1, 4, 5, 6, 7)));
        subMap.put(12, new ArrayList<>(Arrays.asList(2, 5, 7, 8)));

        HashMap<Integer, List<Integer>> expected = new HashMap<>();
        expected.put(1, new ArrayList<>(Arrays.asList(2, 3)));
        expected.put(2, new ArrayList<>(Arrays.asList(2, 3)));
        expected.put(3, new ArrayList<>(Arrays.asList(1, 6)));
        expected.put(9, new ArrayList<>(Arrays.asList(1, 6)));
        expected.put(10, new ArrayList<>(Arrays.asList(5, 7)));
        expected.put(11, new ArrayList<>(Arrays.asList(4, 5, 7)));
        expected.put(12, new ArrayList<>(Arrays.asList(5, 7, 8)));

        var result = SudokuUtils.findDoublesInSubMap(subMap);

        for (int i : expected.keySet()) {

            List<Integer> expectedCandidates = expected.get(i);
            List<Integer> resultCandidates = result.get(i);

            Collections.sort(expectedCandidates);
            Collections.sort(resultCandidates);

            assertArrayEquals(expectedCandidates.toArray(), resultCandidates.toArray());
        }


    }

    @Test
    void findDoublesInSubMap_NoPairs() {
        HashMap<Integer, List<Integer>> subMap = new HashMap<>();
        subMap.put(1, new ArrayList<>(Arrays.asList(2, 3)));
        subMap.put(2, new ArrayList<>(Arrays.asList(2, 3, 4)));
        subMap.put(3, new ArrayList<>(Arrays.asList(1, 3, 6)));
        subMap.put(9, new ArrayList<>(Arrays.asList(1, 2, 6)));
        subMap.put(10, new ArrayList<>(Arrays.asList(1, 3, 5, 7)));
        subMap.put(11, new ArrayList<>(Arrays.asList(1, 4, 5, 6, 7)));
        subMap.put(12, new ArrayList<>(Arrays.asList(2, 5, 7, 8)));

        HashMap<Integer, List<Integer>> expected = new HashMap<>();
        expected.put(1, new ArrayList<>(Arrays.asList(2, 3)));
        expected.put(2, new ArrayList<>(Arrays.asList(2, 3, 4)));
        expected.put(3, new ArrayList<>(Arrays.asList(1, 3, 6)));
        expected.put(9, new ArrayList<>(Arrays.asList(1, 2, 6)));
        expected.put(10, new ArrayList<>(Arrays.asList(1, 3, 5, 7)));
        expected.put(11, new ArrayList<>(Arrays.asList(1, 4, 5, 6, 7)));
        expected.put(12, new ArrayList<>(Arrays.asList(2, 5, 7, 8)));

        var result = SudokuUtils.findDoublesInSubMap(subMap);

        for (int i : expected.keySet()) {

            List<Integer> expectedCandidates = expected.get(i);
            List<Integer> resultCandidates = result.get(i);

            Collections.sort(expectedCandidates);
            Collections.sort(resultCandidates);

            assertArrayEquals(expectedCandidates.toArray(), resultCandidates.toArray());
        }


    }

    @Test
    void findTriplesInSubMap() {

        var subMap = new HashMap<Integer, List<Integer>>();

        subMap.put(0, Arrays.asList(1,2));
        subMap.put(1, Arrays.asList(1,2,3));
        subMap.put(2, Arrays.asList(1,3));
        subMap.put(3, Arrays.asList(1,2, 4 , 6));
        subMap.put(4, Arrays.asList(1, 4 ,5));
        subMap.put(5, Arrays.asList(3, 4 ,5, 6));
        subMap.put(6, Arrays.asList(1, 4, 8, 9));
        subMap.put(7, Arrays.asList(2, 5, 7, 9));
        subMap.put(8, Arrays.asList(6, 7, 9));

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

        var result = SudokuUtils.findTriplesInSubMap(subMap);

        assertArrayEquals(expected.keySet().toArray(new Integer[0]), result.keySet().toArray(new Integer[0]));

        for (int i : expected.keySet())
            assertArrayEquals(expected.get(i).toArray(), result.get(i).toArray());

    }

    @Test
    void findTriplesInSubMapSubsetSmallerThanThree() {

        var subMap = new HashMap<Integer, List<Integer>>();

//        subMap.put(0, Arrays.asList(1,2));
        subMap.put(0, Arrays.asList(1,2,6));

        var expected = new HashMap<Integer, List<Integer>>();

//        expected.put(0, Arrays.asList(1,2));
        expected.put(0, Arrays.asList(1,2,6));

        var result = SudokuUtils.findTriplesInSubMap(subMap);

        assertArrayEquals(expected.keySet().toArray(new Integer[0]), result.keySet().toArray(new Integer[0]));

        for (int i : expected.keySet())
            assertArrayEquals(expected.get(i).toArray(), result.get(i).toArray());


    }


    @Test
    void findTriplesInSubMapLesserThreeCandidates() {

        var subMap = new HashMap<Integer, List<Integer>>();

        subMap.put(0, List.of(1));
        subMap.put(1, List.of(1));
        subMap.put(2, List.of(1));

        var expected = new HashMap<Integer, List<Integer>>();

        expected.put(0, List.of(1));
        expected.put(1, List.of(1));
        expected.put(2, List.of(1));

        var result = SudokuUtils.findTriplesInSubMap(subMap);

        assertArrayEquals(expected.keySet().toArray(new Integer[0]), result.keySet().toArray(new Integer[0]));

        for (int i : expected.keySet())
            assertArrayEquals(expected.get(i).toArray(), result.get(i).toArray());


    }

    @Test
    void findCandidatePairInRow() {

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

        HashMap<Integer, HashMap<Integer, List<Integer>>> expectedDoubles = new HashMap<>();
        HashMap<Integer, List<Integer>> temp = new HashMap<>();

        temp.put(2, Arrays.asList(2,3));
        temp.put(4, Arrays.asList(2,3));
        temp.put(6, Arrays.asList(1,8));
        temp.put(8, Arrays.asList(1,8));
        expectedDoubles.put(0, temp);

        temp = new HashMap<>();
        temp.put(4, Arrays.asList(1,7));
        temp.put(6, Arrays.asList(1,7));
        expectedDoubles.put(1, temp);

        temp = new HashMap<>();
        temp.put(2, Arrays.asList(2,3));
        temp.put(3, Arrays.asList(1,2));
        temp.put(8, Arrays.asList(1,6));
        expectedDoubles.put(2, temp);

        temp = new HashMap<>();
        temp.put(6, Arrays.asList(2,8));
        temp.put(9, Arrays.asList(2,8));
        expectedDoubles.put(3, temp);

        temp = new HashMap<>();
        temp.put(3, Arrays.asList(1,2));
        temp.put(8, Arrays.asList(6,7));
        temp.put(9, Arrays.asList(2,6));
        expectedDoubles.put(4, temp);

        temp = new HashMap<>();
        temp.put(4, Arrays.asList(0,6));
        temp.put(6, Arrays.asList(0,6));
        expectedDoubles.put(5, temp);

        temp = new HashMap<>();
        temp.put(9, Arrays.asList(6,8));
        expectedDoubles.put(6, temp);

        temp = new HashMap<>();
        temp.put(4, Arrays.asList(2,8));
        temp.put(6, Arrays.asList(2,8));
        expectedDoubles.put(8, temp);

        var doubles = SudokuUtils.findCandidatePairInRow(markers);

        assertArrayEquals(expectedDoubles.keySet().toArray(new Integer[0]), doubles.keySet().toArray(new Integer[0]));
        for (int row : doubles.keySet()) {
            assertArrayEquals(expectedDoubles.get(row).keySet().toArray(new Integer[0]), doubles.get(row).keySet().toArray(new Integer[0]));
            for (int cand : doubles.get(row).keySet()) {
                assertArrayEquals(expectedDoubles.get(row).get(cand).toArray(), doubles.get(row).get(cand).toArray());
            }
        }


    }

    @Test
    void findCandidatePairInRow_ReturnsEmptyMapIfNorDoublesFound() {

        HashMap<Integer, List<Integer>> markers = new HashMap<>();
        markers.put(1, new ArrayList<>(Arrays.asList(6,8)));
        markers.put(2, new ArrayList<>(Arrays.asList(2,4,6)));
        markers.put(3, new ArrayList<>(Arrays.asList(2,8,4)));
        markers.put(8, new ArrayList<>(Arrays.asList(8,6,4,2)));

        markers.put(74, new ArrayList<>(Arrays.asList(4,6, 7)));
        markers.put(75, new ArrayList<>(Arrays.asList(4,6,7)));
        markers.put(80, new ArrayList<>(Arrays.asList(4,6, 7)));

        var doubles = SudokuUtils.findCandidatePairInRow(markers);

        assertTrue(doubles.isEmpty());
    }

}