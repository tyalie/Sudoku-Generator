package com.georg.Generator;

import com.georg.Level;
import com.georg.Sudoku;

/**
 * Created by Georg on 31/05/16.
 */
public class StaticGenerator {
    public static Sudoku GenerateSudoku(Level l) {
        while (true) {
            Sudoku su = LasVegasAlgorithm.LasVegas(l);
            SudokuGen gen = new SudokuGen(su);
            gen.digHoles();
            if (gen.getCompSudoku().getNumTotalFields() <= l.getMaxTotalGiven())
                return gen.getSudoku();
        }
    }
}
