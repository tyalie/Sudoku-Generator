package com.georg;

import com.georg.Generator.Level;
import com.georg.Generator.SudokuSolver;

public class Main {

    public static void main(String[] args) {
	// write your code here
        testSolver();
    }

    private static void testSolver() {
        Sudoku mSu = new Sudoku(Level.Easy, new byte[]{
                -1,  9, -1,  6, -1,  1, -1, -1, -1,
                -1, -1, -1, -1,  3, -1,  9, -1,  1,
                -1,  3, -1,  2, -1,  8, -1, -1, -1,
                7, -1,  9, -1, -1, -1, -1, -1,  4,
                -1,  4, -1,  3, -1,  7, -1,  9, -1,
                8, -1,  3, -1,  1, -1,  5, -1,  7,
                -1,  5, -1,  7, -1,  2, -1,  1, -1,
                9, -1,  4, -1,  5, -1,  7, -1,  6,
                -1,  1, -1,  9, -1,  6, -1,  5,  8
        });

        boolean ret = SudokuSolver.isSolvable(mSu);
        assert ret: "Value is: "+ret;
    }
}
