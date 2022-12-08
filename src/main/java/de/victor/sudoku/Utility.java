package de.victor.sudoku;

public class Utility {


    private static void printSudoku(int[] grid) {

        for (int i = 0; i < grid.length; i++) {

            if (i % 9 == 0) System.out.println();
            if (i % 27 == 0 && i != 0) System.out.print("-------------------------------\n");
            if (i % 3 == 0 && i % 9 != 0) System.out.print("|  ");
            if (grid[i] == 0)
                System.out.print(".  ");
            else
                System.out.print(grid[i] + "  ");
        }
        System.out.println();

    }
}
