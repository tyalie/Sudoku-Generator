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

package com.georg.GUI;

import com.georg.Generator.CanBeDugList;
import com.georg.Generator.CompSudoku;
import com.georg.Level;
import com.georg.Sudoku;
import com.georg.ValueFormatException;

import java.util.List;

/**
 * Created by Georg on 05/06/16.
 * <p>
 * Class for better access on the sudoku
 * in the GUI module.
 * Auto transforms 0 into NAN's and v.v.
 */
class GUISudoku extends Sudoku {
    @SuppressWarnings("unused")
    GUISudoku(Level level, byte[] field) throws ValueFormatException {
        super(level, field);
    }

    GUISudoku(Level level) {
        super(level);
    }

    /**
     * Transforms input sudoku into a GUISudoku.
     *
     * @param sudoku The input sudoku
     * @throws ValueFormatException If the input is wrong.
     */
    GUISudoku(Sudoku sudoku) throws ValueFormatException {
        super(sudoku.getDifficulty(), sudoku.getField());
    }

    /**
     * Sets the value on the specified index
     * in the sudoku field. Automatically
     * transforms any 0 as value input into
     * a NAN.
     *
     * @param index The index
     * @param value The value [0, {@link Sudoku#MAX_NUM MAX_NUM}]
     */
    void setAtIndex(int index, byte value) {
        if (value == 0)
            field[index] = NAN;
        else
            field[index] = value;
    }

    /**
     * Gets the value at the index index.
     * Returns zero at every NAN.
     *
     * @param index The index
     * @return The value at the index [0, {@link Sudoku#MAX_NUM MAX_NUM}]
     */
    byte getAtIndexN(int index) {
        return (field[index] == NAN) ? 0 : field[index];
    }

    /*
     * @return the amount of free fields
     *      / NAN values in the field
     */
    int freeFields() {
        int i = 0;
        for (byte v : field)
            if (v == NAN)
                i++;
        return i;
    }

    /**
     * Returns a list of all number on the field
     * that are breaking one of the following rules:
     * <ul>
     * <li>{@link CompSudoku#rule_1(Boolean[])}</li>
     * <li>{@link CompSudoku#rule_2(Boolean[])}</li>
     * <li>{@link CompSudoku#rule_3(Boolean[])}</li>
     * </ul>
     *
     * @return A {@link CanBeDugList} where all rule-braking
     * fields are marked false.
     * @throws ValueFormatException
     */
    CanBeDugList getInvalid() throws ValueFormatException {
        CompSudoku su = new CompSudoku(this);
        CanBeDugList ret = new CanBeDugList();

        for (int i = 0; i < Sudoku.FIELD_COUNT; i++) {
            CompSudoku dug = su.digClone(i);
            List<Byte> avail = dug.getAvailable();
            if (!avail.contains(su.getAtIndex(i)))
                ret.setAtIndex(i, false);
        }
        return ret;
    }
}
