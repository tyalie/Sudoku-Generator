/*
 * MIT License
 *
 * Copyright (c) 2016 Georg A. Friedrich
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
