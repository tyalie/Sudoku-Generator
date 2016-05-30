package com.georg.Generator;

import com.georg.Sudoku;

import java.util.Arrays;

/**
 * Created by Georg on 30/05/16.
 */
public class CompSudoku extends Sudoku {
    private int index = 0;

    public CompSudoku(Level l) {
        super(l);
    }

    private CompSudoku(Level l, byte[] field) throws RuntimeException {
        super(l, field);
    }

    private CompSudoku(Sudoku su) {
        this(su.getHardness(), su.getField());
    }

    public int getIndex() {
        return index;
    }

    public void moveIndex() {
        if(index<(81-1))
            index++;
    }

    public void resetIndex() {
        index = 0;
    }

    public void moveIndexReverse() {
        if(index>0)
            index--;
    }


    private static CompSudoku[] expand() {
        Boolean[] possible = new Boolean[9];
        Arrays.fill(possible, true);
    }

    private static Boolean[] rule_1(Boolean[] possible) {

    }
}
