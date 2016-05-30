package com.georg.Generator;

import com.georg.Sudoku;

/**
 * Created by Georg on 30/05/16.
 */
public class SudokuSolver {
    private static final int ERROR = -1;

    public static int solutions(Sudoku sudoku) {
        return solutions(new CompSudoku(sudoku), true);
    }

    public static boolean isSolvable(Sudoku sudoku) {
        return solutions(new CompSudoku(sudoku), false) != ERROR;
    }

    private static int solutions(CompSudoku sudoku, boolean end) {


        return ERROR;
    }


}
