package de.victor.sudoku.classifier;

import java.util.HashMap;
import java.util.List;

public class SolvingResult {

    public int[] updatedGrid;
    public HashMap<Integer, List<Integer>> updatedPencilMarks;

    public int addedValues;
    public boolean arePencilMarksAltered;


    public SolvingResult(int[] updatedGrid, HashMap<Integer, List<Integer>> updatedPencilMarks, int addedValues, boolean arePencilMarksAltered) {
        this.updatedGrid = updatedGrid;
        this.updatedPencilMarks = updatedPencilMarks;
        this.addedValues = addedValues;
        this.arePencilMarksAltered = arePencilMarksAltered;
    }

}
