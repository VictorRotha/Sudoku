package de.victor.sudoku;

import java.util.*;
import java.util.stream.Collectors;

public class Classifier {

    private Puzzle puzzle;
    private int[] originalPuzzle;




    public Classifier() {


    }


    public void solve() {


        int[] mPuzzle = puzzle.puzzle;
        HashMap<Integer, List<Integer>> markers = updatePencilMarks(mPuzzle, null);

        boolean isSolved = false;
        boolean isSolvable = true;
        int difficulty = 0;
        int maxDifficulty = 9;
        int addedNumbersInStreak = 0;
        int [] solvedByDifficulty = new int[maxDifficulty + 1];

        boolean lastTry = true;

        while (isSolvable && !isSolved) {

            System.out.println("\ndifficulty " + difficulty);

            int addedNumbers = 0;

            switch (difficulty) {

                case 0:
                    addedNumbersInStreak = 0;
                    addedNumbers = collapsePencilMarks(puzzle.puzzle, markers);
//                    addedNumbers = soleCandidates(mPuzzle);

                    break;
                case 1:

                    addedNumbers = uniqueCandidates(mPuzzle);
                    updatePencilMarks(puzzle.puzzle, markers);

                    break;

                case 2:
                    addedNumbers = candidateLines(mPuzzle, markers);
                    break;

                case 3:
                    addedNumbers = doublePairs(mPuzzle, markers);
                    break;

                case 4:
                    addedNumbers = nakedDoubles(mPuzzle, markers);
                    break;
                case 5:
                    addedNumbers = hiddenDoubles(mPuzzle, markers);
                    break;

                case 6:
                    addedNumbers = nakedTriples(mPuzzle, markers);
                    break;
                case 7:
                    addedNumbers = hiddenTriples(mPuzzle, markers);
                    break;
                case 8:
                    addedNumbers = XWings(mPuzzle, markers);
                    break;

                case 9:
                    addedNumbers = swordfish(mPuzzle, markers);
                    break;
            }

            solvedByDifficulty[difficulty] += addedNumbers;

            isSolved = Arrays.equals(mPuzzle, puzzle.grid);

            addedNumbersInStreak += addedNumbers;

            if (isSolved) {
                System.out.println("Sudoku solved !");
                continue;
            }

            if (addedNumbers > 0 && difficulty > 0) {

                System.out.println("End of Streak, added " + addedNumbersInStreak);
                difficulty = 0;
                SudokuUtils.printSudoku(mPuzzle);


            } else {
                difficulty++;
                if (difficulty > maxDifficulty) {
                    System.out.println("End of Streak, added " + addedNumbersInStreak);
                    if (addedNumbersInStreak == 0) {

                        if (lastTry) {
                            lastTry = false;
                            difficulty = 0;
                            System.out.println("LAST TRY");
                        } else {
                            isSolvable = false;
                            System.out.println("Puzzle not solvable !");
                        }
                    } else {
//                        System.out.println("End of Streak, added " + addedNumbersInStreak);
                        difficulty = 0;
                    }

                }

            }

            System.out.printf("isSolved %s, isSolvable %s, addedNumbers %s, addedNumbersInStreak %s\ndifficulty set to %s \n",
                    isSolved, isSolvable, addedNumbers, addedNumbersInStreak, difficulty);

        }

        System.out.println("solvedByDifficulty " + Arrays.toString(solvedByDifficulty));

        if (!isSolved)
            SudokuUtils.printPencilMarks(mPuzzle, markers, false);

    }



    /**
     * finds candidates that appears only once in row, column or box
     * @param puzzle sudoku puzzle as int[]
     * @return number of solved cells
     */
    protected int uniqueCandidates(int[] puzzle) {

        int addedNumbers = 1;
        int totalAdded = 0;

        while (addedNumbers > 0) {

            addedNumbers = eliminateUniques(puzzle);
            totalAdded += addedNumbers;
//            System.out.println("unique Candidates added: " + addedNumbers + " " + totalAdded);

        }

        return totalAdded;

    }


    protected int eliminateUniques(int[] puzzle) {

        int addedNumbers = 0;

        for (int row = 0; row < 9; row ++) {
            var cells = SudokuUtils.getRowIndices(row * 9);
            addedNumbers += findUniqueCandidates(puzzle, cells);
        }


        for (int col = 0; col < 9; col++) {
            var cells = SudokuUtils.getColumnIndices(col);
            addedNumbers += findUniqueCandidates(puzzle, cells);

        }

        for (int row = 0; row < 9; row+=3) {
            for (int col = 0; col < 9; col+=3) {
                List<Integer> cells = SudokuUtils.getBoxIndices(row * 9 + col);
                addedNumbers += findUniqueCandidates(puzzle, cells);
            }
        }

        return addedNumbers;

    }


    protected int findUniqueCandidates(int[] puzzle, List<Integer> cells) {

        int addedNumbers = 0;

        Map<Integer, List<Integer>> indicesByNumbers = findIndicesByNumbers(puzzle, cells);

        for (int number : indicesByNumbers.keySet()) {

            if (indicesByNumbers.get(number).size() == 1) {
                int idx = indicesByNumbers.get(number).get(0);
                puzzle[idx] = number;
                System.out.println("--> unique number set idx " + idx  + " to " + number);
                addedNumbers++;
            }
        }


        return addedNumbers;


    }

    /**
     *
     * eliminate and collapse pencilmarks
     *
     * @param puzzle sudoku puzzle as int[]
     * @param markers pencilmarks
     * @return number of collapsed sudoku cells
     */
    public int candidateLines(int[] puzzle, HashMap<Integer, List<Integer>> markers) {
        if (markers == null) {
            throw new IllegalArgumentException("markers can not be be null");
        }

        boolean removedMarkers = true;
        int addedNumbers = 0;

        while (removedMarkers && addedNumbers == 0) {

            removedMarkers = false;

            for (int box = 0; box < 9; box++) {

                List<Integer> boxIndices = SudokuUtils.getIndicesFromAbbr("b" + box);
                HashMap<Integer, List<Integer>> indicesByNumber = getIndicesByNumber(boxIndices, markers);

                for (int number : indicesByNumber.keySet()) {
                    List<Integer> indices = indicesByNumber.get(number);

                    List<Integer> eliminateFrom = null;

                    int range = Collections.max(indices) - Collections.min(indices);

                    if (range <= 2)
                        eliminateFrom = SudokuUtils.getRowIndices(indices.get(0));
                    else if (areIndicesInSameColumn(indices))
                        eliminateFrom = SudokuUtils.getColumnIndices(indices.get(0));



                    if (eliminateFrom != null) {
                        eliminateFrom.removeAll(indices);
                        if (!eliminateFrom.isEmpty() && eliminateCandidateFromPositions(markers, number, eliminateFrom)) {
                            removedMarkers = true;
                            addedNumbers = collapsePencilMarks(puzzle, markers);
                            if (addedNumbers > 0) {
                                return addedNumbers;
                            }

                        }
                    }

                }

            }

        }

        return addedNumbers;

    }

    /**
     * removes and collapse pencilmarks
     *
     * @param puzzle sudoku puzzle
     * @param markers pencilmarks
     * @return number of collapsed cells
     */
    public int doublePairs(int[] puzzle, HashMap<Integer, List<Integer>> markers) {

        removeDoublePairsFromMarkers(markers);
        return collapsePencilMarks(puzzle, markers);

    }

    protected void removeDoublePairsFromMarkers(HashMap<Integer, List<Integer>> markers) {

        doublePairColumns(markers);

        doublePairRows(markers);

    }

    protected void doublePairRows(HashMap<Integer, List<Integer>> markers) {

        for (int boxLine = 0; boxLine < 3; boxLine++) {

            List<List<Integer>> boxIndices = new ArrayList<>(Arrays.asList(
                    SudokuUtils.getBoxIndices(boxLine * 27),
                    SudokuUtils.getBoxIndices(boxLine * 27 + 3),
                    SudokuUtils.getBoxIndices(boxLine * 27 + 6)));

            List<HashMap<Integer, HashSet<Integer>>> linesByNumbers = new ArrayList<>(Arrays.asList(
                    getRowsByNumber(boxIndices.get(0), markers),
                    getRowsByNumber(boxIndices.get(1), markers),
                    getRowsByNumber(boxIndices.get(2), markers)));


            for (int i = 0; i < 2; i++) {
                for (int j = i + 1; j < 3; j++) {

                    HashMap<Integer, HashSet<Integer>> currentLines = linesByNumbers.get(i);
                    HashMap<Integer, HashSet<Integer>> otherLines = linesByNumbers.get(j);

                    for (int n: currentLines.keySet()) {
                        if (doublesAreNotInLines(currentLines.get(n), otherLines.get(n)))
                            continue;

                        List<Integer> indices = new ArrayList<>();
                        indices.addAll(boxIndices.get(i));
                        indices.addAll(boxIndices.get(j));

                        HashSet<Integer> lines = new HashSet<>(currentLines.get(n));
                        lines.addAll(otherLines.get(n));

                        List<Integer> lineIndices = new ArrayList<>();
                        for (int line: lines) {
                            lineIndices.addAll(SudokuUtils.getRowIndices(line * 9));
                        }

                        lineIndices.removeAll(indices);

                        eliminateCandidateFromPositions(markers, n, lineIndices);

                    }
                }

            }


        }

    }

    protected void doublePairColumns(HashMap<Integer, List<Integer>> markers) {

        for (int boxLine = 0; boxLine < 3; boxLine++) {


            List<List<Integer>> boxIndices = new ArrayList<>(Arrays.asList(
                    SudokuUtils.getBoxIndices(boxLine * 3),
                    SudokuUtils.getBoxIndices((9 + boxLine) * 3),
                    SudokuUtils.getBoxIndices((18 + boxLine) * 3)));

            List<HashMap<Integer, HashSet<Integer>>> linesByNumbers = new ArrayList<>(Arrays.asList(
                    getColumnsByNumber(boxIndices.get(0), markers),
                    getColumnsByNumber(boxIndices.get(1), markers),
                    getColumnsByNumber(boxIndices.get(2), markers)));

            for (int i = 0; i < 2; i++) {
                for (int j = i + 1; j < 3; j++) {

                    HashMap<Integer, HashSet<Integer>> currentLines = linesByNumbers.get(i);
                    HashMap<Integer, HashSet<Integer>> otherLines = linesByNumbers.get(j);

                    for (int n: currentLines.keySet()) {

                        if (doublesAreNotInLines(currentLines.get(n), otherLines.get(n)))
                            continue;

                        List<Integer> indices = new ArrayList<>();
                        indices.addAll(boxIndices.get(i));
                        indices.addAll(boxIndices.get(j));


                        HashSet<Integer> lines = new HashSet<>(currentLines.get(n));
                        lines.addAll(otherLines.get(n));

                        List<Integer> lineIndices = new ArrayList<>();
                        for (int col: lines) {
                            lineIndices.addAll(SudokuUtils.getColumnIndices(col));
                        }

                        lineIndices.removeAll(indices);

                        eliminateCandidateFromPositions(markers, n, lineIndices);
                    }
                }

            }

        }

    }


    /**
     * Searching for cells, that contains only the same two numbers.
     * If both cells in the same row, removes the numbers from the other cells in the row.<br/>
     * If both cells in the same column, removes the numbers from the other cells in the column.<br/>
     * If both cells in the same box, removes the numbers from the other cells in the box.<br/>
     * Collapsing cells.
     *
     * @param puzzle sudoku puzzle
     * @param markers pencilmarks
     * @return number added after collapsing
     */
    public int nakedDoubles(int[] puzzle, HashMap<Integer, List<Integer>> markers) {

        List<String> queue = new ArrayList<>(Arrays.asList(
                "r0", "r1", "r2", "r3", "r4", "r5", "r6", "r7", "r8",
                "c0", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8",
                "b0", "b1", "b2", "b3", "b4", "b5", "b6", "b7", "b8" ));

        List<Integer> indices;
        int added = 0;

        while (!queue.isEmpty()) {

            String line = queue.remove(0);

            indices = SudokuUtils.getIndicesFromAbbr(line);

            Integer[] removed = findNakedDoubles(indices, markers);

            if (removed.length > 0) {

                boolean collapse = false;
                for (int idx: removed) {
                    if (markers.containsKey(idx) && markers.get(idx).size() == 1) {
                        puzzle[idx] = markers.get(idx).get(0);
                        collapse = true;
                    }
                }

                if (collapse) {
                    added = collapsePencilMarks(puzzle, markers);
                    break;

                } else {

                    for (int idx : removed) {
                        int col = idx % 9;
                        int row = idx / 9;
                        int box = row / 3  * 3 + col % 3;

                        String[] lines = new String[] {"c" + col, "r" + row, "b" + box};
                        for (String l : lines) {
                            if (!queue.contains(l))
                                queue.add(l);
                        }

                    }
                }
            }


        }

        return added;

    }


    /**
     * Searches for a pair of cells, that contains only the same two numbers.
     * Removes the numbers from the other cells and write to pencilmarks
     *
     * @param indices cells to look in
     * @param markers pencilmarks
     * @return int[] of indices, from which a number is being removed
     */
    public Integer[] findNakedDoubles(List<Integer> indices, HashMap<Integer, List<Integer>> markers) {
        System.out.println("find naked doubles in " + indices);

        //get mutable subset of markers
        HashMap<Integer, List<Integer>> map = new HashMap<>();
        for (Integer idx: indices) {
            if (markers.containsKey(idx))
                map.put(idx, new ArrayList<>(markers.get(idx)));
        }

        HashMap<Integer, List<Integer>> newMap = findDoublesInSubMap(map);

        //compare
        List<Integer> removedFrom = new ArrayList<>();
        for (int idx : map.keySet()) {
            if (map.get(idx).size() > newMap.get(idx).size()) {
                removedFrom.add(idx);
                markers.put(idx, newMap.get(idx));
            }
        }

        if (!removedFrom.isEmpty())
            System.out.println("find naked doubles removed markers from " + removedFrom);

        Integer[] result = new Integer[removedFrom.size()];
        removedFrom.toArray(result);

        return result;

    }


    public int hiddenDoubles(int[] puzzle, HashMap<Integer, List<Integer>> markers) {

        List<String> queue = new ArrayList<>(Arrays.asList(
                "r0", "r1", "r2", "r3", "r4", "r5", "r6", "r7", "r8",
                "c0", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8",
                "b0", "b1", "b2", "b3", "b4", "b5", "b6", "b7", "b8" ));

        List<Integer> indices;
        int added = 0;

        while (!queue.isEmpty()) {

            String line = queue.remove(0);

            indices = SudokuUtils.getIndicesFromAbbr(line);

            Integer[] removed = findHiddenDoubles(indices, markers);

            if (removed.length > 0) {

                boolean collapse = false;
                for (int idx: removed) {
                    if (markers.containsKey(idx) && markers.get(idx).size() == 1) {
                        puzzle[idx] = markers.get(idx).get(0);
                        collapse = true;
                    }
                }

                if (collapse) {
                    added = collapsePencilMarks(puzzle, markers);
                    break;

                } else {

                    for (int idx : removed) {
                        int col = idx % 9;
                        int row = idx / 9;
                        int box = row / 3  * 3 + col % 3;

                        String[] lines = new String[] {"c" + col, "r" + row, "b" + box};
                        for (String l : lines) {
                            if (!queue.contains(l))
                                queue.add(l);
                        }

                    }
                }
            }


        }

        return added;

    }


    public Integer[] findHiddenDoubles(List<Integer> indices, HashMap<Integer, List<Integer>> markers) {

        HashMap<Integer, List<Integer>> indicesByNumbers = getIndicesByNumber(indices, markers);

        HashMap<Integer, List<Integer>> newIndisByNumbers = findDoublesInSubMap(indicesByNumbers);

        HashMap<Integer, List<Integer>> newMap = new HashMap<>();

        for (Integer number : newIndisByNumbers.keySet()) {
            for (Integer idx : newIndisByNumbers.get(number)) {
                if (!newMap.containsKey(idx))
                    newMap.put(idx, new ArrayList<>());
                newMap.get(idx).add(number);
            }
        }

        List<Integer> removedFrom = new ArrayList<>();
        for (Integer idx : newMap.keySet()) {
            if (newMap.get(idx).size() < markers.get(idx).size()) {
                removedFrom.add(idx);
                markers.put(idx, newMap.get(idx));
            }
        }

        if (!removedFrom.isEmpty())
            System.out.println("removed markers from " + removedFrom);

        Integer[] result = new Integer[removedFrom.size()];
        removedFrom.toArray(result);

        return result;


    }


    public int nakedTriples(int[] puzzle, HashMap<Integer, List<Integer>> markers) {

        List<String> queue = new ArrayList<>(Arrays.asList(
                "r0", "r1", "r2", "r3", "r4", "r5", "r6", "r7", "r8",
                "c0", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8",
                "b0", "b1", "b2", "b3", "b4", "b5", "b6", "b7", "b8" ));

        List<Integer> indices;
        int added = 0;


        while (!queue.isEmpty()) {

            String line = queue.remove(0);

            indices = SudokuUtils.getIndicesFromAbbr(line);

            Integer[] removed = findNakedTriples(indices, markers);

            if (removed.length > 0) {

                boolean collapse = false;
                for (int idx: removed) {
                    if (markers.containsKey(idx) && markers.get(idx).size() == 1) {
                        puzzle[idx] = markers.get(idx).get(0);
                        collapse = true;
                    }
                }

                if (collapse) {
                    added = collapsePencilMarks(puzzle, markers);
                    break;

                } else {

                    for (int idx : removed) {
                        int col = idx % 9;
                        int row = idx / 9;
                        int box = row / 3  * 3 + col % 3;

                        String[] lines = new String[] {"c" + col, "r" + row, "b" + box};
                        for (String l : lines) {
                            if (!queue.contains(l))
                                queue.add(l);
                        }

                    }
                }
            }


        }

        return added;

    }


    public Integer[] findNakedTriples(List<Integer> indices, HashMap<Integer, List<Integer>> markers) {

        HashMap<Integer, List<Integer>> map = new HashMap<>();
        for (Integer idx: indices) {
            if (markers.containsKey(idx))
                map.put(idx, new ArrayList<>(markers.get(idx)));
        }

        HashMap<Integer, List<Integer>> newMap = findTriplesInMap(map);

        List<Integer> removedFrom = new ArrayList<>();
        for (int idx : map.keySet()) {
            if (map.get(idx).size() > newMap.get(idx).size()) {
                removedFrom.add(idx);
                markers.put(idx, newMap.get(idx));
            }

        }

        if (!removedFrom.isEmpty())
            System.out.println("removed markers from " + removedFrom);

        Integer[] result = new Integer[removedFrom.size()];
        removedFrom.toArray(result);

        return result;

    }


    public Integer[] findHiddenTriples(List<Integer> indices, HashMap<Integer, List<Integer>> markers) {

        HashMap<Integer, List<Integer>> indicesByNumbers = getIndicesByNumber(indices, markers);

        HashMap<Integer, List<Integer>> newIndisByNumbers = findTriplesInMap(indicesByNumbers);

        HashMap<Integer, List<Integer>> newMap = new HashMap<>();

        for (Integer number : newIndisByNumbers.keySet()) {
            for (Integer idx : newIndisByNumbers.get(number)) {
                if (!newMap.containsKey(idx))
                    newMap.put(idx, new ArrayList<>());
                newMap.get(idx).add(number);
            }
        }

        List<Integer> removedFrom = new ArrayList<>();
        for (Integer idx : newMap.keySet()) {
            if (newMap.get(idx).size() < markers.get(idx).size()) {
                removedFrom.add(idx);
                markers.put(idx, newMap.get(idx));
            }
        }

        if (!removedFrom.isEmpty())
            System.out.println("removed markers from " + removedFrom);

        Integer[] result = new Integer[removedFrom.size()];
        removedFrom.toArray(result);

        return result;


    }

    public HashMap<Integer, List<Integer>> findTriplesInMap(HashMap<Integer, List<Integer>> subMap) {

        List<Integer> candidates = new ArrayList<>(subMap.keySet());
        HashMap<Integer, List<Integer>> resultMap = new HashMap<>();
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

                        if (values.size() > 3)
                            continue;

//                        System.out.printf("Triple found for candidates %s, %s and %s; values %s\n", c1, c2, c3, values);

                        List<Integer> foundCandidates = new ArrayList<>(Arrays.asList(c1, c2, c3));
                        foundTotal.addAll(foundCandidates);

                        for (int idx: resultMap.keySet()) {
                            if (!foundCandidates.contains(idx))
                                if (resultMap.get(idx).removeAll(values)) {
                                    success = true;
                                }
                        }

                        if (success) {
                            System.out.println(resultMap);
                        }

                    }
                }
            }


        }

        return resultMap;

    }





    public int hiddenTriples(int[] puzzle, HashMap<Integer, List<Integer>> markers) {

        List<String> queue = new ArrayList<>(Arrays.asList(
                "r0", "r1", "r2", "r3", "r4", "r5", "r6", "r7", "r8",
                "c0", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8",
                "b0", "b1", "b2", "b3", "b4", "b5", "b6", "b7", "b8" ));

        List<Integer> indices;
        int added = 0;

        while (!queue.isEmpty()) {

            String line = queue.remove(0);

            indices = SudokuUtils.getIndicesFromAbbr(line);

            Integer[] removed = findHiddenTriples(indices, markers);

            if (removed.length > 0) {

                boolean collapse = false;
                for (int idx: removed) {
                    if (markers.containsKey(idx) && markers.get(idx).size() == 1) {
                        puzzle[idx] = markers.get(idx).get(0);
                        collapse = true;
                    }
                }

                if (collapse) {
                    added = collapsePencilMarks(puzzle, markers);
                    break;

                } else {

                    for (int idx : removed) {
                        int col = idx % 9;
                        int row = idx / 9;
                        int box = row / 3  * 3 + col % 3;

                        String[] lines = new String[] {"c" + col, "r" + row, "b" + box};
                        for (String l : lines) {
                            if (!queue.contains(l))
                                queue.add(l);
                        }

                    }
                }
            }


        }

        return added;

    }

    public HashMap<Integer, List<Integer>> getIndicesByNumber(List<Integer> indices, HashMap<Integer, List<Integer>> markers) {

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
     *
     * Searching for cells in a given part of pencilmarks, that contains only the same two numbers.
     * Removes these numbers from the other cells and repeat until no marker can be removed.<br/>
     * example: {1:[1,2], 2:[1,3,4], 3:[1,2]} -> {1:[1,2], 2:[3,4], 3:[1,2]}<br/>
     * example: {1:[1,2], 2:[1,2,3], 3:[1,2]} -> {1:[1,2], 2:[3], 3:[1,2]}
     *
     * @param subMap sub map of pencilmarks
     * @return an altered version of the subMap
     */
    public HashMap<Integer, List<Integer>> findDoublesInSubMap(HashMap<Integer, List<Integer>> subMap) {
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

                    System.out.printf("Double found for candidates %s and %s; values %s\n", c1, c2, values);

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


    protected int swordfish(int[] puzzle, HashMap<Integer, List<Integer>> markers) {

        var altered = swordfishRemoveMarkers(markers);

        var rotatedMarkers = rotatePencilmarksRight(markers);
        altered = (altered) ? altered :  swordfishRemoveMarkers(rotatedMarkers);
        rotatedMarkers = rotatePencilmarksLeft(rotatedMarkers);

        for (int i : markers.keySet())
            markers.put(i, rotatedMarkers.get(i));

        return collapsePencilMarks(puzzle, markers);


    }

    /**
     * Looks for SwordFish and removes markers from pencilmarks
     * @param markers pencilmarks
     * @return true if markers altered, else false
     */
    boolean swordfishRemoveMarkers(HashMap<Integer, List<Integer>> markers) {

        HashMap<Integer, HashMap<Integer, List<Integer>>> doubles = findCandidatePairInRow(markers);
        return swordfishCompareRows(doubles, markers);

    }

    /**
     * Compares rows and looks for candidate pairs in three different rows with three overlapping columns.
     * If found, the candidate will be removed from other pencilmark rows in all three columns
     *
     * @param doubles {row={candidate=(col1, col2}}
     * @param markers pencilmarks
     * @return true if any candidate was removed, else false
     */
    protected boolean swordfishCompareRows(HashMap<Integer, HashMap<Integer, List<Integer>>> doubles, HashMap<Integer, List<Integer>> markers) {
        boolean removed = false;
        for (int row = 0; row < 7; row++) {
            if (!doubles.containsKey(row))
                continue;
            for (int n : doubles.get(row).keySet()) {
                for (int row2 = row + 1; row2 < 8; row2++) {
                    if (!doubles.containsKey(row2) || !doubles.get(row2).containsKey(n))
                        continue;

                    var total = new HashSet<>(doubles.get(row).get(n));
                    total.addAll(doubles.get(row2).get(n));
                    if (total.size() == 3) {
                        //overlapp 2 rows
                        for (int row3 = row2 + 1; row3 < 9; row3++) {
                            if (!doubles.containsKey(row3) || !doubles.get(row3).containsKey(n))
                                continue;
                            if (total.contains(doubles.get(row3).get(n).get(0)) && total.contains(doubles.get(row3).get(n).get(1))) {

                                var indices = new ArrayList<Integer>();
                                for (int col : total)
                                    indices.addAll(SudokuUtils.getColumnIndices(col));

                                indices.removeAll(SudokuUtils.getRowIndices(row * 9));
                                indices.removeAll(SudokuUtils.getRowIndices(row2 * 9 ));
                                indices.removeAll(SudokuUtils.getRowIndices(row3 * 9 ));

                                if (eliminateCandidateFromPositions(markers, n, indices))
                                    removed = true;
                            }

                        }
                    }
                }
            }
        }

        return removed;
    }


    protected int XWings(int[] puzzle, HashMap<Integer, List<Integer>> markers) {

        var altered = xWingRemoveMarkers(markers);

        var rotatedMarkers = rotatePencilmarksRight(markers);
        altered = (altered) ? altered :  xWingRemoveMarkers(rotatedMarkers);
        rotatedMarkers = rotatePencilmarksLeft(rotatedMarkers);

        System.out.println("XWing markers removed " + altered);

        for (int i : markers.keySet())
            markers.put(i, rotatedMarkers.get(i));

        return collapsePencilMarks(puzzle, markers);


    }

    /**
     * Looks for XWings and removes markers from pencilmarks
     * @param markers pencilmarks
     * @return true if markers altered, else false
     */
    boolean xWingRemoveMarkers(HashMap<Integer, List<Integer>> markers) {

        HashMap<Integer, HashMap<Integer, List<Integer>>> doubles = findCandidatePairInRow(markers);
        return xWingCompareRows(doubles, markers);

    }

    /**
     * Compares rows and looks for candidates in different rows but same columns.
     * If found, the candidate will be removed from other pencilmark rows in both columns
     *
     * @param doubles {row={candidate=(col1, col2}}
     * @param markers pencilmarks
     * @return true if any candidate was removed, else false
     */
    protected boolean xWingCompareRows(HashMap<Integer, HashMap<Integer, List<Integer>>> doubles, HashMap<Integer, List<Integer>> markers) {

        boolean removed = false;
        for (int row = 0; row < 8; row++) {
            if (!doubles.containsKey(row))
                continue;
            for (int n : doubles.get(row).keySet()) {
                for (int row2 = row + 1; row2 < 9; row2++) {
                    if (!doubles.containsKey(row2))
                        continue;
                    if (!doubles.get(row2).containsKey(n))
                        continue;
                    if (doubles.get(row2).get(n).equals(doubles.get(row).get(n))) {

                        var cols = doubles.get(row).get(n);
                        var indices = SudokuUtils.getColumnIndices(doubles.get(row).get(n).get(0));
                        indices.addAll(SudokuUtils.getColumnIndices(doubles.get(row).get(n).get(1)));

                        indices.removeAll(SudokuUtils.getRowIndices(row * 9 + cols.get(0)));
                        indices.removeAll(SudokuUtils.getRowIndices(row2 * 9 + cols.get(0)));

                        if ( eliminateCandidateFromPositions(markers, n, indices))
                            removed = true;

                    }
                }
            }
        }
        return removed;
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
    protected HashMap<Integer, HashMap<Integer, List<Integer>>> findCandidatePairInRow(HashMap<Integer, List<Integer>> markers) {

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

//        for (int row : doubles.keySet())
//            System.out.println(row + " " + doubles.get(row));

        return doubles;
    }


    /**
     * rotates the pencilmark grid 90° to the right, so rows 0 to 8 becomes columns 8 to 0
     * and columns 0 to 8 becomes rows 0 to 8
     *
     * @param markers rotated instance of pencilmarks
     */
    protected HashMap<Integer, List<Integer>> rotatePencilmarksRight(HashMap<Integer, List<Integer>> markers) {

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
    protected HashMap<Integer, List<Integer>> rotatePencilmarksLeft(HashMap<Integer, List<Integer>> markers) {

        HashMap<Integer, List<Integer>> rotated = new HashMap<>();

        for (int i : markers.keySet()) {
            int j = (8 - (i % 9)) * 9 + (i / 9);
            rotated.put(j, markers.get(i));
        }

        return rotated;
    }



    protected HashMap<Integer, HashSet<Integer>> getColumnsByNumber(List<Integer> indices, HashMap<Integer, List<Integer>> markers) {

        HashMap<Integer, HashSet<Integer>> colsByNumber = new HashMap<>();
        for (int i : indices) {
            if (!markers.containsKey(i))
                continue;
            for (int n : markers.get(i)) {
                if (!colsByNumber.containsKey(n)) {
                    colsByNumber.put(n, new HashSet<>());
                }
                colsByNumber.get(n).add(i % 9);
            }

        }

        return colsByNumber;

    }

    protected HashMap<Integer, HashSet<Integer>> getRowsByNumber(List<Integer> indices, HashMap<Integer, List<Integer>> markers) {

        HashMap<Integer, HashSet<Integer>> rowsByNumber = new HashMap<>();
        for (int i : indices) {
            if (!markers.containsKey(i))
                continue;
            for (int n : markers.get(i)) {
                if (!rowsByNumber.containsKey(n)) {
                    rowsByNumber.put(n, new HashSet<>());
                }
                rowsByNumber.get(n).add(i / 9);
            }

        }

        return rowsByNumber;

    }

    protected boolean doublesAreNotInLines(HashSet<Integer> lines1, HashSet<Integer> lines2) {

//        System.out.println("compare lines1" + lines1 + " lines2 " + lines2);
        if (lines1 == null || lines2 == null ||lines1.size() > 2 || lines2.size() > 2 || lines1.isEmpty() || lines2.isEmpty())
            return true;

        HashSet<Integer> allColumns = new HashSet<>(lines1);
        allColumns.addAll(lines2);
        return allColumns.size() != 2;

    }


    /** Searches the pencilmarks for single entries and writes them to the puzzle. Collapsed entries were deleted from markers.
     * After that the markers are updated and the procedure repeats until no cell can be collapsed anymore.
     *
     * @param puzzle sudoku puzzle as int[]
     * @param markers pencilmarks
     * @return number of collapsed cells
     */
    protected int collapsePencilMarks(int[] puzzle, HashMap<Integer, List<Integer>> markers) {
        int addedNumbers = 0;

        boolean collapsing = true;

        while (collapsing) {
            collapsing = false;
            for (int i : markers.keySet()) {

                if (markers.get(i).size() == 1) {
                    puzzle[i] = markers.get(i).get(0);
                    addedNumbers++;
                    System.out.println("--> collapsing: set idx " + i + " to " + puzzle[i]);
                    eliminateNeighbourCandidates(puzzle, markers, i);
                    collapsing = true;
                }

            }
        }

        for (int i = 0; i < 81; i++) {
            if (markers.containsKey(i) && markers.get(i).size() <= 1)
                markers.remove(i);
        }

        return addedNumbers;
    }



    protected HashMap<Integer, List<Integer>> updatePencilMarks(int[] puzzle, HashMap<Integer, List<Integer>> markers) {

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


    protected void eliminateNeighbourCandidates(int[] puzzle, HashMap<Integer, List<Integer>> markers, int idx) {

        int number = puzzle[idx];
        if (number == 0) {
            return;
        }

        List<Integer> neighbours = findNeighbourPositions(idx);
        eliminateCandidateFromPositions(markers, number, neighbours);

    }

    /**
     * removes a candidate from all cells in pencilmarks specified in positions.
     *
     * @param markers pencilmarks
     * @param candidate candidate to remove from markers
     * @param positions indices whe candidate should be removed
     * @return true if candidate was removed at least once, else false
     */
    protected boolean eliminateCandidateFromPositions(HashMap<Integer, List<Integer>> markers, int candidate, List<Integer> positions) {

        boolean removed = false;
        for (Integer pos : positions) {
            if (markers.containsKey(pos) && markers.get(pos).remove((Integer) candidate)) {
                removed = true;
            }
        }

        return removed;

    }

    protected List<Integer> findNeighbourPositions(int idx) {

        HashSet<Integer> neighbours = new HashSet<>();

        neighbours.addAll(SudokuUtils.getBoxIndices(idx));
        neighbours.addAll(SudokuUtils.getRowIndices(idx));
        neighbours.addAll(SudokuUtils.getColumnIndices(idx));

//        System.out.println(" idx " + idx + " neighbours " + neighbours);

        return new ArrayList<>(neighbours);

    }


    protected boolean areIndicesInSameColumn(List<Integer> indices) {

        if (indices.size() == 1)
            return true;

        boolean sameCol = true;
        for (int i = 1; i < indices.size(); i++) {
            if ((indices.get(i) - indices.get(i-1)) % 9 != 0) {
                sameCol = false;
                break;
            }
        }

        return sameCol;
    }


    protected Map<Integer, List<Integer>> findIndicesByNumbers(int[] puzzle, List<Integer> indices) {

        HashMap<Integer, List<Integer>> indicesByNumbers = new HashMap<>();

        for (Integer cell : indices) {

            if (puzzle[cell] == 0) {
                List<Integer> numbers = eliminateNeighboursFromIdx(puzzle, cell);

                for (int number : numbers) {
                    if (!indicesByNumbers.containsKey(number)) {
                        indicesByNumbers.put(number, new ArrayList<>());
                    }
                    indicesByNumbers.get(number).add(cell);

                }
            }
        }
        return indicesByNumbers;

    }


    protected List<Integer> eliminateNeighboursFromIdx(int[] puzzle, int idx, HashMap<Integer, List<Integer>> markers) {

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


    protected List<Integer> eliminateNeighboursFromIdx(int[] puzzle, int idx) {

           return eliminateNeighboursFromIdx(puzzle, idx, null);
    }


    public Puzzle getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
        this.originalPuzzle = Arrays.copyOf(puzzle.puzzle, puzzle.puzzle.length);
    }

    public int[] getOriginalPuzzle() {
        return originalPuzzle;
    }
}
