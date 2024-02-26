package de.victor.sudoku.classifier;

import java.util.Arrays;

public record ClassifierResult(

        int[] solvedByDifficulty,
        boolean[] alteredByDifficulty,

        int[] solution,

        boolean solvableByHuman,

        boolean solutionConfirmed,

        String message,

        int score
){


    @Override
    public String toString() {
        return String.format(
                "ClassifierResult[solvedByDifficulty=%s, alteredByDifficulty=%s, solutionConfirmed=%s, solvableByHuman=%s, message=%s, score=%s, solution=%s]",
                Arrays.toString(solvedByDifficulty), Arrays.toString(alteredByDifficulty), solutionConfirmed, solvableByHuman, message, score, Arrays.toString(solution));
    }

}
