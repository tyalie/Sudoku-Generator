package com.georg;

import com.georg.Generator.CompSudoku;
import com.georg.Generator.LasVegasAlgorithm;
import com.georg.Generator.StaticGenerator;
import com.georg.Generator.SudokuSolver;

public class Main {

    private static Sudoku testCase1 = new Sudoku(Level.Easy, new byte[]{
            -1, 9, -1, 6, -1, 1, -1, -1, -1,
            -1, -1, -1, -1, 3, -1, 9, -1, 1,
            -1, 3, -1, 2, -1, 8, -1, -1, -1,
            7, -1, 9, -1, -1, -1, -1, -1, 4,
            -1, 4, -1, 3, -1, 7, -1, 9, -1,
            8, -1, 3, -1, 1, -1, 5, -1, 7,
            -1, 5, -1, 7, -1, 2, -1, 1, -1,
            9, -1, 4, -1, 5, -1, 7, -1, 6,
            -1, 1, -1, 9, -1, 6, -1, 5, 8
    });

    private static Sudoku testCase2 = new Sudoku(Level.Easy, new byte[]{
            5, 9, 8, 6, 7, 1, 2, 4, 3,
            6, 7, 2, 5, 3, 4, 9, 8, 1,
            4, 3, 1, 2, 9, 8, 6, 7, 5,
            7, -1, 9, -1, -1, -1, -1, -1, 4,
            -1, 4, -1, 3, -1, 7, -1, 9, -1,
            8, -1, 3, -1, 1, -1, 5, -1, 7,
            -1, 5, -1, 7, -1, 2, -1, 1, -1,
            9, -1, 4, -1, 5, -1, 7, -1, 6,
            -1, 1, -1, 9, -1, 6, -1, 5, 8
    });


    public static void main(String[] args) {
        // write your code here
        // testSolver();
        // testCompSudoku();
        // testLasVegasAlgorithm();
        testSudokuGen(Level.Easy);
    }


    private static void testSolver() {
        boolean ret = SudokuSolver.isSolvable(testCase1);
        System.out.println("Value is: " + ret);
        assert ret : "Value is: " + ret;
    }

    private static void testCompSudoku() {
        CompSudoku c = new CompSudoku(testCase2);
        c.moveNext();
        System.out.println(c.toString() + "\n\n");

        for (CompSudoku e : c.expand())
            System.out.println(e.toString() + "\n\n");
    }

    private static void testLasVegasAlgorithm() {
        Sudoku su = LasVegasAlgorithm.LasVegas(Level.Easy);
        assert su != null;
        System.out.println(su.toString());
    }

    private static void testSudokuGen(Level l) {
        Sudoku su = StaticGenerator.GenerateSudoku(l);
        System.out.println(su.toString());
    }
}
