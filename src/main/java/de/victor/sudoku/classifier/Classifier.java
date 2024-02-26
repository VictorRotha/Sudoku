package de.victor.sudoku.classifier;

import de.victor.sudoku.Puzzle;
import de.victor.sudoku.SudokuUtils;
import de.victor.sudoku.classifier.solvingtechs.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Classifier {

    private final boolean debug;

    private final SolvingTechnique[] ranking = new SolvingTechnique[] {
            new CollapsePencilMarks(),
            new UniqueCandidates(),
            new CandidateLines(),
            new DoublePairs(),
            new NakedDoubles(),
            new HiddenDoubles(),
            new NakedTriples(),
            new HiddenTriples(),
            new XWings(),
            new SwordFish()
    } ;

    public Classifier(boolean debug) {
        this.debug = debug;
    }

    public Classifier() {
        this(false);
    }

    public ClassifierResult classify(Puzzle puzzle) {

        var result = solve(puzzle);
        var score = estimateSolvingDifficulty(result);

        return new ClassifierResult(
                result.solvedByDifficulty(),
                result.alteredByDifficulty(),
                result.solution(),
                result.solvableByHuman(),
                result.solutionConfirmed(),
                result.message(),
                score
        );
    }

    private int estimateSolvingDifficulty(ClassifierResult classifierResult) {

        if (!classifierResult.solvableByHuman())
            return -1;

        var  result = 0;

        for (int i = 0; i < classifierResult.solvedByDifficulty().length; i++ ) {
            result += classifierResult.solvedByDifficulty()[i] * (i+1) * 100;
        }

        for (int i = 0; i < classifierResult.alteredByDifficulty().length; i++) {
            if (classifierResult.alteredByDifficulty()[i])
                result += (i+1) * 1000;
        }

        return result;
    }


    private ClassifierResult solve(Puzzle puzzle) {

        int[] mPuzzle = Arrays.copyOf(puzzle.puzzle(), puzzle.puzzle().length);

        HashMap<Integer, List<Integer>> markers = SudokuUtils.updatePencilMarks(mPuzzle, null);

        boolean isSolved = false;

        int difficulty = 0;

        int[] solvedByDifficulty = new int[ranking.length];
        boolean[] alteredByDifficulty = new boolean[ranking.length];

        while (!isSolved) {

            printDebug("solve difficulty " + difficulty);

            var result = applyTechnique(mPuzzle, markers, difficulty);

            //handle result

            mPuzzle = result.puzzle();
            markers = new HashMap<>(result.pencilMarks());

            if (result.addedValues() > 0) {
                printDebug("UPDATED GRID");

                if (debug)
                    SudokuUtils.printSudoku(result.puzzle());

                printDebug(result.toString());
                printDebug("isConfirmed=" +( SudokuUtils.confirmPuzzleSolution(mPuzzle, puzzle.solvedPuzzle()) == null));
            }

            if (result.arePencilMarksAltered())
                printDebug("pencilMarks altered");

            solvedByDifficulty[difficulty] += result.addedValues();
            alteredByDifficulty[difficulty] = alteredByDifficulty[difficulty] || result.arePencilMarksAltered();

            isSolved = Arrays.equals(result.puzzle(), puzzle.solvedPuzzle());

            if (isSolved) {
                printDebug("Sudoku solved !");
                continue;
            }

            if (difficulty == 0) {

                difficulty++;

            } else if (result.addedValues() > 0 || result.arePencilMarksAltered()) {

                printDebug("End of Streak: difficulty " + difficulty + " added " + result.addedValues() + " markers altered " + result.arePencilMarksAltered());
                difficulty = 0;

            } else if (difficulty < ranking.length - 1) {
                difficulty ++;

            } else {
                printDebug("End of Streak: max difficulty; puzzle not solvable ");
                break;

            }

            printDebug(String.format("isSolved %s, addedNumbers %s, markers altered %s\ndifficulty set to %s \n",
                    isSolved, result.addedValues(), result.arePencilMarksAltered(), difficulty));

        }

        var confirmedMessage = SudokuUtils.confirmPuzzleSolution(mPuzzle, puzzle.solvedPuzzle());

        var result = new  ClassifierResult(
                solvedByDifficulty,
                alteredByDifficulty,
                mPuzzle,
                isSolved,
                confirmedMessage != null,
                confirmedMessage,
                -1
        );

        printDebug(result.toString());

        return  result;
    }


    protected SolvingResult applyTechnique(int[] mPuzzle, HashMap<Integer, List<Integer>> markers, int difficulty) {

        return ranking[difficulty].execute(mPuzzle, markers);

    }

    private void printDebug(String text) {
        if (debug) {
            System.out.println(text);
        }
    }

}
