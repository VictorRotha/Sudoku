package de.victor.sudoku.classifier;

import java.util.HashMap;
import java.util.List;

public interface SolvingTechnique {

    SolvingResult execute(int[] puzzle, HashMap<Integer, List<Integer>> markers);


}
