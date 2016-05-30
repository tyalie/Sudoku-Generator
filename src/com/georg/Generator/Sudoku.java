package com.georg.Generator;

/**
 * Created by Georg on 30/05/16.
 */
public class Sudoku {
    private Level hardness;
    /* 0. row
       1. column
       2. row
       3. column
     */
    private byte[] field;

    public byte getIndex(int x, int y) {
        return field[x+9*y];
    }

    public byte getIndex(int c1, int r1, int c2, int r2) {
        return field[c1*3 + r1*27 + c2 + r2*9];
    }

    public Sudoku(Level l, byte[] field) throws RuntimeException{
        hardness = l;
        if(field.length != 81)
            throw new RuntimeException("Wrong field size. Should be 81 is"+field.length);
        this.field = field;
    }

    public Sudoku(Level l) {
        hardness = l;
        this.field = new byte[81];
    }

    public Level getHardness() {
        return hardness;
    }

    public byte[] getField() {
        return field;
    }

    @Override
    public String toString() {
        String out = "";
        for (int i=0; i<81; i++) {
            out+=field[i];
            if(i%9==0)
                out+='\n';
            if(i%3==0)
                out+="|";
            if(i%27==0)
                out+="---|---|---";
        }

        return out;
    }
}
