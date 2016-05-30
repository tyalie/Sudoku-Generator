package com.georg.Generator;

/**
 * Created by Georg on 30/05/16.
 */
public class SudokuSolver {
    private static final int ERROR = -1;

    public static int solutions(Sudoku sudoku) {
        return solutions(sudoku, true);
    }

    public static boolean isSolvable(Sudoku sudoku) {
        return solutions(sudoku, false) != ERROR;

    }

    private static int solutions(Sudoku sudoku, boolean end) {

        return ERROR;
    }
}
