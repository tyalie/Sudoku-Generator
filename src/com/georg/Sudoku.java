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
    /**
     * The field size.
     * Because sudoku is squared in my
     * program, I just need to declare a.
     * <pre><code>
     *     a
     * +-------+
     * |       |
     * |       |a
     * |       |
     * +-------+
     * </pre></code>
     */
    public static final int FIELD_SIZE = 9;
    /**
     * That's just the square of the field size
     * that represents the amount of all values
     * on the bord.
     */
    public static final int FIELD_COUNT = FIELD_SIZE * FIELD_SIZE;
    //@formatter:off
    /**
     * The length or height of one subfield in
     * the sudoku puzzle.
     <pre><code>
     *             This
     *               |
     * +--+--+--+    |
     * |  |  |@@|<---+
     * +--+--+--+
     * |  |  |  |
     * +--+--+--+
     * |  |  |  |
     * +--+--+--+
     *</code></pre>
     */
    //@formatter:on
    public static final int BLOCK_SIZE = 3;
    /**
     * The maximum number occurring on the
     * field. With that information the
     * number range can be extracted.
     * It is:<br>
     * &nbsp&nbsp&nbsp[1,MAX_NUM]
     */
    public static final int MAX_NUM = 9;

    /**
     * General constant for NAN values on
     * the field. (Empty values)
     */
    public static final int NAN = -1;
    /**
     * The difficulty of the sudoku. See
     * {@link Level}.
     */
    private final Level difficulty;
    /**
     * The field variable. Free fields are
     * {@link #NAN}, any other number should
     * be in the range [1, {@link #MAX_NUM}].
     * <p>
     * The order of counting is as follows.
     * <pre><code>
     *   +1
     *  i->
     * +--+--+--+
     * |  |  |  | i
     * +--+--+--+ |+width
     * |  |  |  | v
     * +--+--+--+
     * |  |  |  |
     * +--+--+--+
     * </code></pre>
     */
    protected byte[] field;

    /**
     * This initializer generates an sudoku
     * with the field defined by field and
     * the difficulty defined by level.
     *
     * @param level The difficulty. See {@link Level}.
     * @param field The field.
     * @throws ValueFormatException If the input fieldsize is not matching the hardcoded one.
     */
    public Sudoku(Level level, byte[] field) throws ValueFormatException {
        difficulty = level;
        if (field.length != FIELD_COUNT) // ERROR field not possible.
            throw new ValueFormatException("Wrong field size. Should be 81 is" + field.length);
        this.field = field.clone();
    }

    /**
     * This initializer generates an empty
     * ({@link #NAN}  filled) field with and
     * sets it level to the input.
     *
     * @param level The difficulty. See {@link Level}.
     */
    public Sudoku(Level level) {
        difficulty = level;
        this.field = new byte[FIELD_COUNT];
        Arrays.fill(this.field, (byte) NAN);
    }

    /**
     * Get the value at the coordinate.
     * <pre><code>
     *   x->
     * +--+--+--+
     * |  |  |  | y
     * +--+--+--+ |
     * |  |  |  | v
     * +--+--+--+
     * |  |  |  |
     * +--+--+--+
     * </code></pre>
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return The value.
     */
    public byte getAtIndex(int x, int y) {
        return field[x + FIELD_SIZE * y];
    }

    /**
     * Get the value at the specified index.
     *
     * @param index The index.
     * @return The value at the index.
     */
    public byte getAtIndex(int index) {
        return field[index];
    }

    /**
     * @return The difficulty of this sudoku
     * instance.
     */
    public Level getDifficulty() {
        return difficulty;
    }

    /**
     * The whole field, nonadjustable.
     *
     * @return The field.
     */
    public byte[] getField() {
        return field.clone();
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
