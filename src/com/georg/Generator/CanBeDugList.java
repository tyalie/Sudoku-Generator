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
 * <p>
 * See number 4: "Pruning optimization"
 * in the paper.
 */
class CanBeDugList {

    /**
     * A representation of my field with
     * values of True or False. This is
     * used to declare fields that where
     * evaluated before as not possible
     * to evaluate. With that the algorithm
     * will not revisit this field.
     */
    private boolean[] field;
    /**
     * The number of unevaluated fields.
     * (Or simply fields which are True).
     */
    private int count;

    /**
     * Initialises list and fills
     * all positions with True.
     */
    CanBeDugList() {
        field = new boolean[FIELD_COUNT];
        Arrays.fill(field, true);
        count = FIELD_COUNT;
    }

    /**
     * Used to get fastly an overview
     * of the amount of free fields.
     *
     * @return True if there are free fields,
     * if else than False.
     */
    boolean isFree() {
        return count > 0;
    }

    /**
     * Returns the current boolean value
     * in the CanBeDugList at the index i.
     *
     * @param i The index.
     * @return Returns the value of the field
     * at i.
     */
    boolean getAtIndex(int i) {
        return field[i];
    }

    /**
     * Sets the value v at position i. If
     * automatically handles {@link #count}.
     *
     * @param i The index
     * @param v The value to write on index i.
     */
    void setAtIndex(final int i, final boolean v) {
        if (field[i] != v) {
            count += v ? 1 : -1;
            field[i] = v;
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(field);
    }
}
