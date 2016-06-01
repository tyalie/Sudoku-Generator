package com.georg.Generator;

import java.util.Arrays;

import static com.georg.Sudoku.FIELD_COUNT;

/**
 * Created by Georg on 31/05/16.
 * <p>
 * Class to manage the "Can-Be-Dug" list.
 * This list is just a boolean that holds
 * all field that can be dugged.
 * For performance reasons does this class
 * use an extra counter to know the amount
 * of free fields.
 */
class CanBeDugList {

    private boolean[] field;
    private int count;

    public CanBeDugList() {
        field = new boolean[FIELD_COUNT];
        Arrays.fill(field, true);
        count = FIELD_COUNT;
    }

    public int getFree() {
        return count;
    }

    public boolean isFree() {
        return count > 0;
    }

    public boolean getAtIndex(int i) {
        return field[i];
    }

    public void setAtIndex(final int i, final boolean v) {
        if (field[i] != v) {
            if (v)
                count++;
            else
                count--;
            field[i] = v;
        }
    }
}
