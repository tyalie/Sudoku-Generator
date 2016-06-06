package com.georg.GUI;

import com.georg.Level;
import com.georg.Sudoku;
import com.georg.ValueFormatException;

/**
 * Created by Georg on 05/06/16.
 */
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
}
