package com.georg;

import java.util.Arrays;

/**
 * Created by Georg on 30/05/16.
 * <p>
 * Sudoku class for easy management
 * of a field. Includes important constant
 * variables and methods for field, printing
 * and level.
 */
public class Sudoku {
    public static final int FIELD_SIZE = 9;
    public static final int FIELD_COUNT = FIELD_SIZE * FIELD_SIZE;
    public static final int BLOCK_SIZE = 3;
    public static final int MAX_NUM = 9;

    public static final int NAN = -1;
    /* 0. row
       1. column
       2. row
       3. column
     */
    protected byte[] field;
    private Level difficulty;

    public Sudoku(Level l, byte[] field) throws RuntimeException {
        difficulty = l;
        if (field.length != FIELD_COUNT)
            throw new RuntimeException("Wrong field size. Should be 81 is" + field.length);
        this.field = field;
    }

    public Sudoku(Level l) {
        difficulty = l;
        this.field = new byte[FIELD_COUNT];
        Arrays.fill(this.field, (byte) -1);
    }

    public byte getAtIndex(int x, int y) {
        return field[x + FIELD_SIZE * y];
    }

    public byte getAtIndex(int i) {
        return field[i];
    }

    public Level getDifficulty() {
        return difficulty;
    }

    public byte[] getField() {
        return field;
    }

    @Override
    public String toString() {
        String out = "";
        for (int i = 0; i < FIELD_COUNT; i++) {
            if (i != 0) {
                if (i % (FIELD_SIZE * BLOCK_SIZE) == 0)
                    out += "\n-------|-------|-------\n";
                else if (i % FIELD_SIZE == 0)
                    out += "\n";
                else if (i % BLOCK_SIZE == 0)
                    out += " |";
            }
            out += " ";


            if (field[i] != NAN)
                out += field[i];
            else
                out += " ";
        }

        return out;
    }
}
