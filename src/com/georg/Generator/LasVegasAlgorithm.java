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
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;

import java.util.List;

import static com.georg.Sudoku.*;

/**
 * Created by Georg on 31/05/16.
 * <p>
 * This class defines the Las Vegas Algorithm
 * for generation of terminal patterns.
 */
@SuppressWarnings("WeakerAccess")
public class LasVegasAlgorithm {
    /**
     * Las Vegas Algorithm for Sudoku generation.
     * With the findings from the paper named before,
     * this method generates a terminal pattern by
     * putting in eleven random numbers in a random
     * position on the field and will then solve and
     * return the solved state.
     * To have not repeating patterns in the first few
     * lines, the code will return the last sudoku found
     * in a given time. In this case 100ms.
     *
     * @param l The playing level.
     * @return Returns a terminal pattern.
     */
    public static Sudoku LasVegas(Level l) throws ValueFormatException {
        // Faster random generator.
        XoRoShiRo128PlusRandom rand = new XoRoShiRo128PlusRandom();
        rand.setSeed(System.currentTimeMillis());
        while (true) {
            // Creates an empty sudoku field.
            CompSudoku terminal = new CompSudoku(l);

            /* Assigns a random number at the start
             * This blocks annoying 123... sequences
             * in the first rows. */
            terminal.setField((byte) (rand.nextInt(MAX_NUM) + 1));
            for (int i = 0; i < 10; i++) {
                // Randomly sets items on the field.
                terminal.setIndex(rand.nextInt(FIELD_SIZE * FIELD_SIZE));
                List<Byte> pos = terminal.getAvailable();
                if (pos.size() > 0 && terminal.getAtIndex() == NAN)
                    terminal.setField(pos.get(rand.nextInt(pos.size())));
                else
                    i--;
            }
            // Reset index to next free one, important for the solver to work.
            terminal.resetIndex();

            /* Trigger the solving and limit the calculation
             * time, but also choose randomly at which amount
             * of found solutions it should stop. Gives the
             * whole generated terminal pattern some randomness.
             *
             * The time limit is there so that the algorithm here
             * will produce results in an relative short amount of time.
             */
            SudokuSolver.DFSLV(terminal, System.currentTimeMillis(), 200, rand.nextInt(10000));
            Sudoku su = SudokuSolver.getLastField();
            // If Sudoku correctly finish the process.
            if (su != null)
                return su;
        }
    }
}
