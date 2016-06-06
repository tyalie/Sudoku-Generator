package com.georg;

import com.georg.GUI.UserInterface;
import com.georg.Generator.CompSudoku;
import com.georg.Generator.StaticGenerator;
import com.georg.Generator.SudokuSolver;

import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException{
        new UserInterface();
    }

    private static void testSolver() throws ValueFormatException{
        Sudoku m = new Sudoku(Level.Easy, new byte[]{
                -1, -1, -1, -1, -1, -1, -1, -1, -1,
                 8,  7,  3, -1, -1, -1, -1, -1,  1,
                -1, -1, -1, -1, -1, -1, -1,  6, -1,
                -1, -1,  4,  7, -1,  3,  5, -1, -1,
                -1,  6, -1, -1, -1,  1,  7, -1,  4,
                -1, -1, -1,  9, -1,  5, -1, -1, -1,
                 5,  8, -1, -1, -1,  7,  1,  2, -1,
                 2,  1, -1,  3, -1, -1,  6, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1});
        SudokuSolver.DFSLV(new CompSudoku(m), System.currentTimeMillis()*2, 0, 1);
        try {
            SaveHandler.save("/Users/Georg/Desktop/out.pdf", SudokuSolver.getLastField());
        } catch (IOException e) {
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
