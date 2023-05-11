package de.victor.sudoku;

import java.util.*;

public class Classifier {

    private Puzzle puzzle;
    private int[] originalPuzzle;




    public Classifier() {


    }


    public void solve(Sudoku sudoku) {


        int[] mPuzzle = puzzle.puzzle;
        HashMap<Integer, List<Integer>> markers = null;

        boolean isSolved = false;
        boolean isSolvable = true;
        int difficulty = 0;
        int maxDifficulty = 7;
        int addedNumbersInStreak = 0;
        int [] solvedByDifficulty = new int[maxDifficulty + 1];

        boolean lastTry = true;

        while (isSolvable && !isSolved) {

            System.out.println("\ndifficulty " + difficulty);

            int addedNumbers = 0;

            switch (difficulty) {

                case 0:
                    addedNumbersInStreak = 0;

                    addedNumbers = soleCandidates(mPuzzle);

                    break;
                case 1:

                    addedNumbers = uniqueCandidates(mPuzzle);
                    break;

                case 2:
                    if (markers == null)
                        markers = generatePencilMarks(mPuzzle);
                    addedNumbers = candidateLines(mPuzzle, markers);
                    break;

                case 3:
                    if (markers == null)
                        markers = generatePencilMarks(mPuzzle);
                    addedNumbers = doublePairs(mPuzzle, markers);
                    break;

                case 4:
                    if (markers == null)
                        markers = generatePencilMarks(mPuzzle);
                    addedNumbers = nakedDoubles(mPuzzle, markers);
                    break;
                case 5:
                    if (markers == null)
                        markers = generatePencilMarks(mPuzzle);
                    addedNumbers = hiddenDoubles(mPuzzle, markers);
                    break;

                case 6:
                    if (markers == null)
                        markers = generatePencilMarks(mPuzzle);
                    addedNumbers = nakedTriples(mPuzzle, markers);
                    break;
                case 7:
                    if (markers == null)
                        markers = generatePencilMarks(mPuzzle);
                    addedNumbers = hiddenTriples(mPuzzle, markers);
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


    private int soleCandidates(int[] puzzle) {

        int addedTotal = 0;

        int numbersAdded = 1;
        while (numbersAdded > 0) {
            numbersAdded = 0;
            for (int i = 0; i < 81; i++) {
                if (puzzle[i] != 0)
                    continue;

                List<Integer> candidates = eliminateNeighboursFromIdx(puzzle, i);
                if (candidates.size() == 1) {
                    System.out.println("--> sole candidates idx " + i + " set to " + candidates.get(0));
                    numbersAdded++;
                    puzzle[i] = candidates.get(0);
                }
            }

            addedTotal += numbersAdded;

        }

        return addedTotal;
    }

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

            ArrayList<Integer> cells = new ArrayList<>();
            for (int i = row * 9; i < (row+1) * 9;  i ++ ) {
                cells.add(i);
            }
            addedNumbers += findUniqueCandidates(puzzle, cells);
        }


        for (int col = 0; col < 9; col++) {
            ArrayList<Integer> cells = new ArrayList<>();
            for (int i = col; i < 81; i += 9) {
                cells.add(i);
            }
            addedNumbers += findUniqueCandidates(puzzle, cells);

        }


        for (int row = 0; row < 9; row+=3) {
            for (int col = 0; col < 9; col+=3) {
                ArrayList<Integer> cells = new ArrayList<>();
                for (int i = col; i < col + 3; i++) {
                    for (int j = row; j < row + 3; j++) {
                        cells.add(j * 9 + i);
                    }
                }
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
                System.out.println("--> unique number idx " + idx  + " set to " + number);
                addedNumbers++;
            }
        }


        return addedNumbers;


    }


    public int candidateLines(int[] puzzle, HashMap<Integer, List<Integer>> markers) {
        System.out.println("candidate lines");
        if (markers == null) {
            throw new IllegalArgumentException("markers can not be be null");
        }

        boolean removedMarkers = true;
        int addedNumbers = 0;

        while (removedMarkers && addedNumbers == 0) {

            removedMarkers = false;

            for (int box = 0; box < 9; box++) {

                System.out.println("box " + box);

                List<Integer> boxIndices = SudokuUtils.getIndicesFromAbbr("b" + box);
                HashMap<Integer, List<Integer>> indicesByNumber = getIndicesByNumber(boxIndices, markers);

                System.out.println("boxIndices " + boxIndices);

                System.out.println("IndisByNumbers " + indicesByNumber);

                for (int number : indicesByNumber.keySet()) {
                    List<Integer> indices = indicesByNumber.get(number);

                    List<Integer> eliminateFrom = null;

                    int range = Collections.max(indices) - Collections.min(indices);

                    System.out.println(number + " range " + range + " " + indices);

                    if (range <= 2)
                        eliminateFrom = SudokuUtils.getRowIndices(indices.get(0));
                    else if (areIndicesInSameColumn(indices))
                        eliminateFrom = SudokuUtils.getColumnIndices(indices.get(0));



                    if (eliminateFrom != null) {
                        System.out.println(number + " eli from " + eliminateFrom);
                        eliminateFrom.removeAll(indices);
                        if (!eliminateFrom.isEmpty() && eliminateCandidateFromPositions(markers, number, eliminateFrom)) {
                            removedMarkers = true;
                            System.out.println("removed markers, from " + eliminateFrom);
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
                        if (!areDoublesInLines(currentLines.get(n), otherLines.get(n)))
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

                        if (!areDoublesInLines(currentLines.get(n), otherLines.get(n)))
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


    public Integer[] findNakedDoubles(List<Integer> indices, HashMap<Integer, List<Integer>> markers) {

        HashMap<Integer, List<Integer>> map = new HashMap<>();
        for (Integer idx: indices) {
            if (markers.containsKey(idx))
                map.put(idx, new ArrayList<>(markers.get(idx)));
        }

        HashMap<Integer, List<Integer>> newMap = findDoublesInMap(map);

        List<Integer> removedFrom = new ArrayList<>();
        for (int idx : map.keySet()) {
            if (map.get(idx).size() > newMap.get(idx).size()) {
                removedFrom.add(idx);
                markers.put(idx, newMap.get(idx));
            }

        }

        if (removedFrom.size() > 0)
            System.out.println("removed markers from " + removedFrom);

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

        HashMap<Integer, List<Integer>> newIndisByNumbers = findDoublesInMap(indicesByNumbers);

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

        if (removedFrom.size() > 0)
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

        if (removedFrom.size() > 0)
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

        if (removedFrom.size() > 0)
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








    public HashMap<Integer, List<Integer>> findDoublesInMap(HashMap<Integer, List<Integer>> subMap) {
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

                if (resultMap.get(c1).size() > 2)
                    continue;

                for (int j = i + 1; j < candidates.size(); j++) {
                    int c2 = candidates.get(j);
                    if (foundTotal.contains(c2))
                        continue;
                    if (resultMap.get(c2).size() > 2)
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

                        if (success) {
                            System.out.println("Doubles: success " + resultMap);
                        }

                    }
                }
            }


        return resultMap;

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

    protected boolean areDoublesInLines(HashSet<Integer> lines1, HashSet<Integer> lines2) {

//        System.out.println("compare lines1" + lines1 + " lines2 " + lines2);
        if (lines1 == null || lines2 == null ||lines1.size() > 2 || lines2.size() > 2 || lines1.isEmpty() || lines2.isEmpty())
            return false;

        HashSet<Integer> allColumns = new HashSet<>(lines1);
        allColumns.addAll(lines2);
        return allColumns.size() == 2;

    }




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

    protected HashMap<Integer, List<Integer>> generatePencilMarks(int[] puzzle) {

        HashMap<Integer, List<Integer>> markers = new HashMap<>();

        for (int i = 0; i < puzzle.length; i++) {
            if (puzzle[i] == 0) {
                List<Integer> candidates = eliminateNeighboursFromIdx(puzzle, i);
                markers.put(i, candidates);
            }

        }

        SudokuUtils.printPencilMarks(puzzle, markers, false);

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


    protected List<Integer> eliminateNeighboursFromIdx(int[] puzzle, int idx) {

            ArrayList<Integer> candidates = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));

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
                    int currentIdx = r * 9 + c;
                    candidates.remove((Integer) puzzle[currentIdx]);
                }
            }

            return candidates;

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
