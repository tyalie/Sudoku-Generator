package com.georg.Generator;

import com.georg.Sudoku;
import com.georg.ValueFormatException;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;

import static com.georg.Sudoku.*;

/**
 * Created by Georg on 03/06/16.
 * <p></p>
 * This class has methods for all possible
 * preparations described by the paper under
 * "Operator ○5 : Equivalent propagation"
 */
@SuppressWarnings("WeakerAccess")
public class SudokuPropagation {

    /**
     * The sudoku class instance.
     */
    private CompSudoku sudoku;
    /**
     * The solved sudoku.
     * For performance this is solved only once.
     */
    private Sudoku solvedSudoku;

    /**
     * Initialises the class with the input
     * sudoku and solves it.
     * @param sudoku The sudoku to be propagated.
     */
    public SudokuPropagation(Sudoku sudoku) throws ValueFormatException{
        this.sudoku = new CompSudoku(sudoku);
        SudokuSolver.DFSLV(this.sudoku, System.currentTimeMillis()*2, 0, 1);
        solvedSudoku = SudokuSolver.getLastField();
    }

    //@formatter:off
    /**
     * This flips two numbers in the sudoku for every
     * block on its own where it gets the position
     * from the solved field.
     * Due to the nature of sudokus this will in a not
     * terminal pattern pretty much destroy it.
     * Just kept here for the sake of completeness.
     *
     * <pre><code>
     * ┌───┬───┬───┐      ┌───┬───┬───┐
     * │ 1 │@2 │ 3 │      │ 1 │@5 │ 3 │
     * ├───┼───┼───┤ 2&5  ├───┼───┼───┤
     * │ 4 │@5 │ 6 │ ━━━▶ │ 4 │@2 │ 6 │
     * ├───┼───┼───┤      ├───┼───┼───┤
     * │ 7 │ 8 │ 9 │      │ 7 │ 8 │ 9 │
     * └───┴───┴───┘      └───┴───┴───┘
     *     Mutual exchange of two
     *            numbers.
     *
     * </code></pre>
     * @param d1 The first digit.
     * @param d2 The second digit.
     * @return Always true.
     * @throws ValueFormatException If d1 or d2 is out of range.
     * @throws ArithmeticException If one of the two numbers
     * were not found. Should be unreachable in complete sudoku.
     */
    //@formatter:on
    @SuppressWarnings("unused")
    public boolean prop1_TwoDigits(byte d1, byte d2) throws ValueFormatException{
        if(d1<=0 || d1 > MAX_NUM)
            throw new ValueFormatException("d1 is out of range");
        if(d2<=0 || d2 > MAX_NUM)
            throw new ValueFormatException("d2 is out of range");

        for (int i = 0; i < BLOCK_SIZE*BLOCK_SIZE; i++) {
            int i_d1=-1, i_d2=-1;
            // Find digits.
            for (int j = 0; j < BLOCK_SIZE*BLOCK_SIZE; j++) {
                int x = i%3*BLOCK_SIZE+j%3+(j/3+i/3*3)*FIELD_SIZE;
                if(solvedSudoku.getAtIndex(x)==d1)
                    i_d1 = x;
                else if(solvedSudoku.getAtIndex(x)==d2)
                    i_d2 = x;
            }

            if(i_d1==-1 || i_d2==-1)
                throw new ArithmeticException("Unreachable state reached");

            // Swap them.
            sudoku.swapIndex(i_d1, i_d2);
        }
        return true;
    }

    //@formatter:off
    /**
     * Swaps two columns in a specified block.
     * <pre><code>
     * ┌───┬───┬───┐      ┌───┬───┬───┐
     * │ 1 │ 2 │ 3 │      │ 3 │ 1 │ 3 │
     * ├───┼───┼───┤ 1&2  ├───┼───┼───┤
     * │ 4 │ 5 │ 6 │ ━━━▶ │ 6 │ 4 │ 6 │
     * ├───┼───┼───┤      ├───┼───┼───┤
     * │ 7 │ 8 │ 9 │      │ 9 │ 7 │ 9 │
     * └───┴───┴───┘      └───┴───┴───┘
     *     mutual exchange of two
     *   columns in the same column
     * </code></pre>
     * @param block The block.
     * @param col1 The first column.
     * @param col2 The second column.
     * @return True if the action was useful.
     * False if the action had no effect (f.E:col1==col2)
     * @throws ValueFormatException If either of the above
     * values is out of range.
     */
    //@formatter:on
    public boolean prop2_TwoColumnInBlock(int block, int col1, int col2) throws ValueFormatException {
        if(col1<0 || col1>=BLOCK_SIZE || col2<0 || col2>=BLOCK_SIZE)
            throw new ValueFormatException("Col1 or col2 are not in range");
        if(block<0 || block>=BLOCK_SIZE)
            throw new ValueFormatException("Bock is out of range");

        if(col1==col2)
            return false;

        for (int i = 0; i < FIELD_SIZE; i++)
            sudoku.swapIndex(CompSudoku.getStaticIndex(block * BLOCK_SIZE + col1, i),
                    CompSudoku.getStaticIndex(block * BLOCK_SIZE + col2, i));
        return true;
    }

     //@formatter:off
    /**
     * Swaps two block columns.
     * <pre><code>
     * ┌───┬───┬───┐      ┌───┬───┬───┐
     * │ 1 │ 2 │ 3 │      │ 3 │ 1 │ 3 │
     * ├───┼───┼───┤ 1&2  ├───┼───┼───┤
     * │ 4 │ 5 │ 6 │ ━━━▶ │ 6 │ 4 │ 6 │
     * ├───┼───┼───┤      ├───┼───┼───┤
     * │ 7 │ 8 │ 9 │      │ 9 │ 7 │ 9 │
     * └───┴───┴───┘      └───┴───┴───┘
     *      mutual exchange of two
     *        columns of blocks
     * </code></pre>
     * @param col1 The first column.
     * @param col2 The second column.
     * @return True if the action was useful.
     * False if the action had no effect (f.E:col1==col2)
     * @throws ValueFormatException If either of the above
     * values is out of range.
     */
    //@formatter:on
    public boolean prop3_TwoColumnOfBlock(int col1, int col2) throws ValueFormatException {
        if(col1<0 || col1>=BLOCK_SIZE || col2<0 || col2>=BLOCK_SIZE)
            throw new ValueFormatException("Col1 or col2 are not in range");
        for(int i = 0; i< FIELD_SIZE; i++) {
            for (int j = 0; j < BLOCK_SIZE; j++)
                sudoku.swapIndex(CompSudoku.getStaticIndex(col1*BLOCK_SIZE+j,i),
                        CompSudoku.getStaticIndex(col2*BLOCK_SIZE+j, i));
        }
        return true;
    }

    /**
     * Rolls the grid by the specified
     * degree. The degree is defined as:<br>
     * int(degree°/90°) = <b>deg</b> (degree is clockwise)<br>
     * <pre><code>
     * ┌───┬───┬───┐      ┌───┬───┬───┐
     * │ 1 │ 2 │ 3 │      │ 7 │ 4 │ 1 │
     * ├───┼───┼───┤  1   ├───┼───┼───┤
     * │ 4 │ 5 │ 6 │ ━━━▶ │ 8 │ 5 │ 2 │
     * ├───┼───┼───┤      ├───┼───┼───┤
     * │ 7 │ 8 │ 9 │      │ 9 │ 6 │ 3 │
     * └───┴───┴───┘      └───┴───┴───┘
     *          grid rolling
     * </code></pre>
     *
     * @param deg The amount of degrees (/90°) to rotate.
     * @return True if the action had an effect, if else false. <br>
     * (f.E: deg=(360°*n)/90°; n is int)
     */
    @SuppressWarnings("SuspiciousNameCombination")
    public boolean prop4_GridRolling(int deg) {
        deg = deg%4;
        if(deg == 0)
            return false;
        if(deg<0)
            deg = 4+deg;

        CompSudoku su = new CompSudoku(sudoku.getDifficulty());
        CompSudoku solvedSu = new CompSudoku(sudoku.getDifficulty());

        for (int x = 0; x < FIELD_SIZE; x++) {
            for (int y = 0; y < FIELD_SIZE; y++) {
                int i = -1;
                // Everybody with a beautiful, non-switch idea is welcome.
                switch (deg) {
                    case 1:
                        i = CompSudoku.getStaticIndex(FIELD_SIZE-y-1, x);
                        break;
                    case 2:
                        i = CompSudoku.getStaticIndex(FIELD_SIZE-x-1, FIELD_SIZE-y-1);
                        break;
                    case 3:
                        i = CompSudoku.getStaticIndex(y, FIELD_SIZE-x-1);
                        break;
                }
                // Need to flip solved one and current one.
                su.setAtField(i, sudoku.getAtIndex(x,y));
                solvedSu.setAtField(i, solvedSudoku.getAtIndex(x,y));
            }
        }

        sudoku = su;
        solvedSudoku = solvedSu;
        return true;
    }

    /**
     * Samples randomly three of the four methods
     * from above. Excludes {@link #prop1_TwoDigits(byte, byte)}.
     * @param steps The amount of valid steps of sampling.
     * @throws ValueFormatException If an error was thrown. (Heavily unlikely)
     */
    public void randomSampling(int steps) throws ValueFormatException{
        XoRoShiRo128PlusRandom rand = new XoRoShiRo128PlusRandom();
        for (int i = 0; i < steps; i++) {
            boolean ret = true;

            switch (rand.nextInt(3)) {
                case 0:
                    ret = prop2_TwoColumnInBlock(rand.nextInt(BLOCK_SIZE), rand.nextInt(BLOCK_SIZE), rand.nextInt(BLOCK_SIZE));
                    break;
                case 1:
                    ret = prop3_TwoColumnOfBlock(rand.nextInt(BLOCK_SIZE), rand.nextInt(BLOCK_SIZE));
                    break;
                case 2:
                    ret = prop4_GridRolling(rand.nextInt(3)+1);
                    break;
            }

            // If action was useless, reuse this step.
            if(!ret)
                i--;
        }
    }

    /**
     * @return The sudoku of this instance.
     */
    public Sudoku getSudoku() {
        return sudoku;
    }
}
