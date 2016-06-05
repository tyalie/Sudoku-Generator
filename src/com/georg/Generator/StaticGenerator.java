package com.georg.Generator;

import com.georg.Level;
import com.georg.Sudoku;
import com.georg.ValueFormatException;

/**
 * Created by Georg on 31/05/16.
 * <p>
 * Should be used class to generate algorithms.
 * Internal method includes all important steps
 * to generate a satisfying sudoku puzzle at the
 * given difficulty.
 */
public class StaticGenerator {
    /**
     * The method that allows to generate a
     * sudoku in the given difficulty.
     *
     * @param l The difficultly.
     * @return The final sudoku puzzle.
     */
    public static Sudoku GenerateSudoku(Level l) throws ValueFormatException {
        while (true) {
            // Generates a terminal pattern.
            Sudoku su = LasVegasAlgorithm.LasVegas(l);
            // Inits the Generator.
            SudokuGen gen = new SudokuGen(su);
            // Digs the holes.
            gen.digHoles();

            /* If my sudoku is to in the given difficulty
             * process further.
             *
             * In this case the difficulty can be checked
             * by only asking for the number of given fields,
             * because all other properties are guarantied
             * to be satisfied in the SudokuGen class.
             */
            if (((CompSudoku) gen.getSudoku()).getNumTotalFields() <= l.getMaxTotalGiven()) {
                // Mixes the sudoku a bit without changing the difficulty.
                SudokuPropagation sudokuPropagation = new SudokuPropagation(gen.getSudoku());
                sudokuPropagation.randomSampling(1000);

                // Return the freshly sampled sudoku puzzle.
                return sudokuPropagation.getSudoku();
            }
        }
    }
}
