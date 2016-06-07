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

import java.util.Arrays;
import java.util.List;

public class GUISudoku extends Sudoku {
    public GUISudoku(Level level, byte[] field) throws ValueFormatException {
        super(level, field);
    }

    public GUISudoku(Level level) {
        super(level);
    }

    public void setAtIndex(int index, byte value) {
        if (value==0)
            field[index] = NAN;
        else
            field[index] = value;
    }

    public GUISudoku(Sudoku sudoku) throws ValueFormatException{
        super(sudoku.getDifficulty(), sudoku.getField());
    }

    public byte getAtIndexN(int index) {
        return (field[index]==NAN)?0:field[index];
    }

    public int freeFields() {
        int i=0;
        for (byte v : field)
            if (v==NAN)
                i++;
        return i;
    }

    public CanBeDugList getInvalid() throws ValueFormatException{
        CompSudoku su = new CompSudoku(this);
        CanBeDugList ret = new CanBeDugList();

        for (int i = 0; i < Sudoku.FIELD_COUNT; i++) {
            CompSudoku dug = su.digClone(i);
            List<Byte> avail = dug.getAvailable();
            if ( !avail.contains(su.getAtIndex(i)) )
                ret.setAtIndex(i, false);
        }
        return ret;
    }
}
