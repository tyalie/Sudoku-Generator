package com.georg;

import com.georg.Generator.StaticGenerator;
import com.georg.Generator.SudokuSolver;

public class Main {

    public static void main(String[] args) {
        try {
            testSudokuGen(Level.Evil);
        } catch (ValueFormatException e) {
            e.printStackTrace();
        }
    }

    private static void testSudokuGen(Level l) throws ValueFormatException {
        Sudoku su = StaticGenerator.GenerateSudoku(l);
        System.out.println(su + "\n\n");
        int i = SudokuSolver.solutions(su);
        assert 1 == i : "The sudoku has " + i + " solution(s)";
    }


}
