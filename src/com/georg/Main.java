package com.georg;

import com.georg.Generator.StaticGenerator;
import com.georg.Generator.SudokuSolver;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            //testSudokuGen(Level.Evil);
            testSaveHandler("/Users/Georg/Desktop/out.pdf");
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

    private static void testSaveHandler(String name) throws ValueFormatException{
        Sudoku su = StaticGenerator.GenerateSudoku(Level.Evil);


        try {
            SaveHandler.save(name, su);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int i = SudokuSolver.solutions(su);
        assert 1 == i : "The sudoku has " + i + " solution(s)";
    }


}
