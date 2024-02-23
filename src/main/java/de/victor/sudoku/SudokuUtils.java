package de.victor.sudoku;

import java.util.*;
import java.util.stream.Collectors;

public class SudokuUtils {


    public static boolean isValidNumber(int idx, int number, int[] grid) {

        boolean inRow = isInRow(idx, number, grid);
        boolean inCol = isInColumn(idx, number, grid);
        boolean inSquare = isInSquare(idx, number, grid);

        return !inRow && !inCol && !inSquare;
    }


    private static boolean isInRow(int idx, int number, int[] grid) {

        int row = idx / 9;

        int firstIdx = row * 9;
        int lastIdx = firstIdx + 8;

        for ( int i = firstIdx; i <= lastIdx; i++) {
            if (i == idx)
                continue;
            if (grid[i] == number)
                return true;

        }
        return false;

    }

    private static boolean isInColumn(int idx, int number, int[] grid) {

        int col = idx % 9;

        for (int row = 0; row < 9; row++) {
            int currentIdx = row * 9 + col;
            if (currentIdx == idx)
                continue;
            if (grid[currentIdx] == number)
                return true;

        }

        return false;

    }

    private static boolean isInSquare(int idx, int number, int[] grid) {

        int row = idx / 9;
        int col = idx % 9;

        int firstRow = row / 3 * 3;
        int firstCol = col / 3 * 3;


        for (int r = firstRow; r < firstRow + 3; r++) {

            for (int c = firstCol; c < firstCol + 3; c++) {

                int currentIdx = r * 9 + c;
                if (currentIdx == idx)
                    continue;
                if (grid[currentIdx] == number)
                    return true;

            }
        }

        return false;


    }

    public static boolean isSudokuValid(int[] grid) {
        for (int i = 0; i < grid.length; i++) {
            if (grid[i] == 0 || !isValidNumber(i, grid[i], grid))
                return false;
        }
        return true;

    }

    public static boolean isGridMatchesLevel(int[] grid, int[] level) {

        if (grid == null || level == null)
            return false;

        for (int i = 0; i < level.length; i++) {
            if (level[i] != 0 && grid[i] != level[i]) {
                return false;
            }
        }

        return true;

    }


    public static List<Integer> getRowIndices(int idx) {

        List<Integer> indices = new ArrayList<>();

        int row = idx / 9;

        for (int i = row * 9; i < row * 9 + 9; i++) {
            indices.add(i);
        }

        return indices;

    }

    public static List<Integer> getColumnIndices(int idx) {
        List<Integer> indices = new ArrayList<>();

        int col = idx % 9;

        for (int i = col; i < 81; i+=9) {
            indices.add(i);
        }

        return indices;


    }

    public static List<Integer> getBoxIndices(int idx) {
        int row = idx / 9;
        int col = idx % 9;
        int firstRow = row / 3 * 3;
        int firstCol = col / 3 * 3;

        ArrayList<Integer> indices = new ArrayList<>();

        for (int r = firstRow; r < firstRow + 3; r++) {
            for (int c = firstCol; c < firstCol + 3; c++) {
                indices.add( r * 9 + c);
            }
        }
        return indices;
    }

    public static List<Integer> getNeighbourPositions(int idx) {

        HashSet<Integer> neighbours = new HashSet<>();

        neighbours.addAll(SudokuUtils.getBoxIndices(idx));
        neighbours.addAll(SudokuUtils.getRowIndices(idx));
        neighbours.addAll(SudokuUtils.getColumnIndices(idx));

//        System.out.println(" idx " + idx + " neighbours " + neighbours);

        return new ArrayList<>(neighbours);

    }

    public static List<Integer> getIndicesFromAbbr(String abbr) {

        String message = abbr + " is not a valid abbreviation";

        List<Integer> indices;
        int n;
        try {
            n = Integer.parseInt(abbr.substring(1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(message);
        }

        if (abbr.toLowerCase().startsWith("r"))
            indices = SudokuUtils.getRowIndices(n * 9);
        else if (abbr.toLowerCase().startsWith("c"))
            indices = SudokuUtils.getColumnIndices(n);
        else if (abbr.toLowerCase().startsWith("b")) {
            int idx = n / 3 * 3 * 9 + (n % 3) * 3;
            indices = SudokuUtils.getBoxIndices(idx);
        }
        else
            throw new IllegalArgumentException(message);

        return indices;
    }



    /**
     * removes a candidate from all cells in pencilmarks specified in positions.
     *
     * @param markers pencilmarks
     * @param candidate candidate to remove from markers
     * @param positions indices whe candidate should be removed
     * @return true if a candidate was removed at least once, else false
     */
    public static boolean eliminateCandidateFromPositions(HashMap<Integer, List<Integer>> markers, int candidate, List<Integer> positions) {

        boolean removed = false;
        for (Integer pos : positions) {
            if (markers.containsKey(pos) && markers.get(pos).contains(candidate)) {
                markers.put(pos, markers.get(pos).stream().filter(n -> n != candidate).collect(Collectors.toList()));
                removed = true;
            }
        }

        return removed;

    }

    public static HashMap<Integer, List<Integer>> updatePencilMarks(int[] puzzle, HashMap<Integer, List<Integer>> markers) {

        if (markers == null)
            markers = new HashMap<>();

        for (int i = 0; i < puzzle.length; i++) {
            if (puzzle[i] == 0) {
                List<Integer> candidates = eliminateNeighboursFromIdx(puzzle, i, markers);
                markers.put(i, candidates);
            } else if (markers.get(i) != null) {
                markers.remove(i);
            }

        }

        return markers;

    }


    public static List<Integer> eliminateNeighboursFromIdx(int[] puzzle, int idx, HashMap<Integer, List<Integer>> markers) {

        ArrayList<Integer> candidates;

        if (markers == null || (markers.get(idx) == null && puzzle[idx] == 0))
            candidates = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
        else
            candidates = new ArrayList<>(markers.get(idx));

        int row = idx / 9;
        int col = idx % 9;

        for (int c = row * 9; c < (row+1) * 9; c++) {
            candidates.remove((Integer) puzzle[c]);
        }

        for (int c = col; c < puzzle.length; c += 9) {
            candidates.remove((Integer) puzzle[c]);
        }

        int firstRow = row / 3 * 3;
        int firstCol = col / 3 * 3;

        for (int r = firstRow; r < firstRow + 3; r++) {
            for (int c = firstCol; c < firstCol + 3; c++) {
                int currentIdx = r * 9 + c;;
                candidates.remove((Integer) puzzle[currentIdx]);
            }
        }

        return candidates;

    }



    public static void printSudoku(int[] grid) {
        printSudoku(grid, new int[81]);
    }

    public static void printSudoku(int[] grid, int[] highlight) {

        if (grid == null || highlight == null) return;

        for (int i = 0; i < grid.length; i++) {

            if (i % 9 == 0) System.out.println();
            if (i % 27 == 0 && i != 0) System.out.print("-------------------------------\n");
            if (i % 3 == 0 && i % 9 != 0) System.out.print("|  ");
            if (grid[i] == 0)
                System.out.print(".  ");
            else if (highlight[i] == 0)
                System.out.print(grid[i] + "  ");
            else
                System.out.print(grid[i] + "* ");

        }
        System.out.println();

    }

    public static void printPencilMarks(int[] puzzle, HashMap<Integer, List<Integer>> markers, boolean showSolved) {

        if (markers == null) {
            System.err.println("markers are null");
            return;
        }

        if (puzzle == null)
            puzzle = new int[81];

        for (int i = 0; i < puzzle.length; i++) {
            if (markers.containsKey(i))
                System.out.printf("%2d : %s\n", i, markers.get(i));
            else if (showSolved)
                System.out.printf("%2d : %s\n", i, puzzle[i]);

        }



    }

    public static HashMap<Integer, List<Integer>> getIndicesByNumber(List<Integer> indices, HashMap<Integer, List<Integer>> markers) {

        HashMap<Integer, List<Integer>> result = new HashMap<>();

        for (Integer idx: indices) {
            if (!markers.containsKey(idx))
                continue;
            List<Integer> numbers = markers.get(idx);
            for (Integer n: numbers) {
                if (!result.containsKey(n))
                    result.put(n, new ArrayList<>());
                result.get(n).add(idx);
            }

        }

        return result;
    }

    /**
     * rotates the pencilmark grid 90° to the right, so rows 0 to 8 becomes columns 8 to 0
     * and columns 0 to 8 becomes rows 0 to 8
     *
     * @param markers rotated instance of pencilmarks
     */
    public static HashMap<Integer, List<Integer>> rotatePencilmarksRight(HashMap<Integer, List<Integer>> markers) {

        HashMap<Integer, List<Integer>> rotated = new HashMap<>();

        for (int i : markers.keySet()) {
            int j = (i % 9) * 9 + 8 - (i/9);
            rotated.put(j, markers.get(i));
        }

        return rotated;
    }

    /**
     * rotates the pencilmark grid 90° to the left, so rows 0 to 8 becomes columns 0 to 8
     * and columns 0 to 8 becomes rows 8 to 0
     *
     * @param markers rotated instance of pencilmarks
     */
    public static HashMap<Integer, List<Integer>> rotatePencilmarksLeft(HashMap<Integer, List<Integer>> markers) {

        HashMap<Integer, List<Integer>> rotated = new HashMap<>();

        for (int i : markers.keySet()) {
            int j = (8 - (i % 9)) * 9 + (i / 9);
            rotated.put(j, markers.get(i));
        }

        return rotated;
    }


    /**
     *
     * Searching for two cells in a given part of pencilmarks, that contains only the same two numbers.
     * Removes these numbers from the other cells and repeat until no marker can be removed.<br/>
     * example: {1:[1,2], 2:[1,3,4], 3:[1,2]} -> {1:[1,2], 2:[3,4], 3:[1,2]}<br/>
     * example: {1:[1,2], 2:[1,2,3], 3:[1,2]} -> {1:[1,2], 2:[3], 3:[1,2]}
     *
     * @param subMap sub map of pencilmarks
     * @return an altered version of the subMap
     */
    public static HashMap<Integer, List<Integer>> findDoublesInSubMap(HashMap<Integer, List<Integer>> subMap) {
        List<Integer> candidates = new ArrayList<>(subMap.keySet());
        HashMap<Integer, List<Integer>> resultMap = new HashMap<>();
        for (Integer idx : subMap.keySet())
            resultMap.put(idx, new ArrayList<>(subMap.get(idx)));
        List<Integer> foundTotal = new ArrayList<>();


        HashSet<Integer> values;
        boolean success = true;

        while (success) {

            success = false;

            for (int i = 0; i < candidates.size() - 1; i++) {

                int c1 = candidates.get(i);

                if (foundTotal.contains(c1))
                    continue;

                if (resultMap.get(c1).size() != 2)
                    continue;

                for (int j = i + 1; j < candidates.size(); j++) {
                    int c2 = candidates.get(j);
                    if (foundTotal.contains(c2))
                        continue;
                    if (resultMap.get(c2).size() != 2)
                        continue;
                    values = new HashSet<>(resultMap.get(c1));
                    values.addAll(resultMap.get(c2));
                    if (values.size() > 2)
                        continue;

//                    System.out.printf("Double found for candidates %s and %s; values %s\n", c1, c2, values);

                    List<Integer> foundCandidates = new ArrayList<>(Arrays.asList(c1, c2));
                    foundTotal.addAll(foundCandidates);

                    for (int idx: resultMap.keySet()) {
                        if (!foundCandidates.contains(idx))
                            if (resultMap.get(idx).removeAll(values)) {
                                success = true;
                            }
                    }
                }
            }
        }

        return resultMap;

    }

    /**
     *
     * Searching for three cells in a given part of pencilmarks, that contains only one or more of the same three numbers.
     * Removes these numbers from the other cells and repeat until no marker can be removed.<br/>
     * example: {1:[1,2,3], 2:[1,3], 3:[1,2,4], 4:[1,2]} -> {1:[1,2,3], 2:[1,3], 3:[4], 4:[1,2]}<br/>
     *
     * @param subMap sub map of pencilmarks
     * @return an altered version of the subMap
     */
    public static HashMap<Integer, List<Integer>> findTriplesInSubMap(HashMap<Integer, List<Integer>> subMap) {

        HashMap<Integer, List<Integer>> resultMap = new HashMap<>();

        List<Integer> candidates = new ArrayList<>(subMap.keySet());

        for (Integer idx : subMap.keySet())
            resultMap.put(idx, new ArrayList<>(subMap.get(idx)));
        List<Integer> foundTotal = new ArrayList<>();

        HashSet<Integer> values;
        boolean success = true;

        while (success) {

            success = false;

            for (int i = 0; i < candidates.size() - 2; i++) {


                int c1 = candidates.get(i);

                if (foundTotal.contains(c1))
                    continue;

                if (resultMap.get(c1).size() > 3)
                    continue;

                for (int j = i + 1; j < candidates.size() - 1; j++) {
                    int c2 = candidates.get(j);
                    if (foundTotal.contains(c2))
                        continue;
                    if (resultMap.get(c2).size() > 3)
                        continue;
                    values = new HashSet<>(resultMap.get(c1));
                    values.addAll(resultMap.get(c2));
                    if (values.size() > 3)
                        continue;
                    for (int k = j + 1; k < candidates.size(); k++) {
                        int c3 = candidates.get(k);

                        if (foundTotal.contains(c3))
                            continue;
                        if (resultMap.get(c3).size() > 3)
                            continue;
                        values = new HashSet<>(resultMap.get(c1));
                        values.addAll(resultMap.get(c2));
                        values.addAll(resultMap.get(c3));

                        if (values.size() != 3)
                            continue;

                        List<Integer> foundCandidates = new ArrayList<>(Arrays.asList(c1, c2, c3));
                        foundTotal.addAll(foundCandidates);

                        for (int idx: resultMap.keySet()) {
                            if (!foundCandidates.contains(idx))
                                if (resultMap.get(idx).removeAll(values)) {
                                    success = true;
                                }
                        }
                    }
                }
            }
        }

        return resultMap;

    }

    /**
     * Searches for candidates, who only occurs twice in a row.
     * Returns the columns who contains the candidate, mapped to the candidate, mapped to the row.<br/>
     * example output:<br/>
     * {<br/>
     * 1 = {7 = (1, 3), 8 = (4, 5)},<br/>
     * 3 = {7 = (1, 3)},<br/>
     * }<br/>
     * candidate 7 in row 1 lies only in column 1 and 3<br/>
     * candidate 8 in row 1 lies only in column 4 and 5<br/>
     * candidate 7 in row 2 lies only in column 1 and 3<br/>
     *
     *
     *
     * @param markers pencilmarks
     * @return map of row -> map of candidate -> list of (col1, col2)
     */
    public static HashMap<Integer, HashMap<Integer, List<Integer>>> findCandidatePairInRow(HashMap<Integer, List<Integer>> markers) {

        HashMap<Integer, HashMap<Integer, List<Integer>>> doubles = new HashMap<>();
        // get columns that contains the only two n in row
        for (int row = 0; row < 9; row++) {
            var indices = SudokuUtils.getRowIndices(row * 9);
            var indicesByNumber = getIndicesByNumber(indices, markers);
            if (indicesByNumber.isEmpty())
                continue;
            for (int n : indicesByNumber.keySet()) {
                if (indicesByNumber.get(n).size() != 2)
                    continue;
                doubles.computeIfAbsent(row, k -> new HashMap<>());
                var cols = indicesByNumber.get(n).stream().map(i -> i % 9).collect(Collectors.toList());
                doubles.get(row).put(n, cols);
            }
        }

        return doubles;
    }



}
