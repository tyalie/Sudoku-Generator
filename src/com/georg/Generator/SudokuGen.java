package com.georg.Generator;

import com.georg.Level;
import com.georg.Sudoku;
import com.georg.ValueFormatException;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;

import java.util.List;

import static com.georg.Sudoku.FIELD_COUNT;
import static com.georg.Sudoku.FIELD_SIZE;

//@formatter:off
/**
 * Created by Georg on 31/05/16.
 * <p>
 * This diagram describes what the whole class should do.
 * Look into the JavaDocs of the class {@link SudokuGen}
 * to see the correctly formatted diagram.
 *
 * The numbers reference to the functions in this class.
 *
 *<pre><code>
                                    -----
                                   |Begin|
                                    --+--
                                      |
                                      v
                      +------------------------------+
                      | Enter a desirable difficulty |
                      +---------------+--------------+
                                      v
                    +-----------------------------------+1
                    | Determine the sequence of digging |
                    +-----------------+-----------------+
                                      v
                 +----------------------------------------+2
                 | Setup the restriction of digging holes |
                 +--------------------+-------------------+
                                      v
                     +---------------------------------+
                     | Set all the cells "can-be-dug"  |
                     +----------------+----------------+
                                      |
                                      v
                            /-------------------\
                            |  Do "can-be-dug"  |
+----------+--------------->|   cells exist?    +-No--+
|          |                \---------+---------/     |
|          |                          v Yes           |
|          |            +--------------------------+  |
|          |            | Select next "can-be-dug" |  |
|          |            |     cell in sequence     |  |
|          |            +-------------+------------+  |
|          |                          v               |
|+---------+--------+       /-------------------\     |
||  Forbidden from  |       |  Does it violate  |     |
|| digging the cell |<-Yes--+   restriction?    |     |
|+------------------+       \---------+---------/     |
|          ^                          v No            |
|          |4           /--------------------------\3 |
|          |            |  Does it yield a unique  |  |
|          +-No---------+ solution if cell diged?  |  |
|                       \-------------+------------/  |
|                                     v Yes           |
|                             +--------------+        |
+-----------------------------| Dig the cell |        |
                              +--------------+        |
                                                      |
                               +-------------+5       |
                               | Propagating |<-------+
                               +------+------+
                                      v
                                 +---------+
                                 | Output  |
                                 +----+----+
                                      |
                                      v
                                     ---
                                    |End|
                                     ---
 *</pre></code>
 */
//@formatter:on
public class SudokuGen {
    /**
     * The sudoku to work with.
     */
    private CompSudoku sudoku;
    /**
     * A instance of a good performance,
     * and precise pseudorandom class.
     */
    private XoRoShiRo128PlusRandom rand;
    /**
     * The difficulty.
     */
    private Level level;
    /**
     * The list of all fields that can be dug.
     *
     * @see CanBeDugList
     */
    private CanBeDugList possible;

    /**
     * Creates an instance of this class.
     * Defines inits {@link #rand}, {@link #level},
     * and {@link #sudoku}. Where the latter two
     * are influenced by the input.
     * The input instance will not be edited.
     *
     * @param sudoku The input sudoku.
     */
    SudokuGen(Sudoku sudoku) throws ValueFormatException {
        this.sudoku = new CompSudoku(sudoku);
        this.rand = new XoRoShiRo128PlusRandom();
        this.level = sudoku.getDifficulty();
    }

    /**
     * @return The currently saved sudoku.
     */
    Sudoku getSudoku() {
        return sudoku;
    }

    /**
     * This is the main function of the
     * class. It digs the holes into the
     * sudoku. It is managing job 1-4.
     *
     * @throws ArithmeticException If a
     *                             non-included difficultly is used.
     */
    void digHoles() throws ValueFormatException {
        possible = new CanBeDugList();
        // The minimum amount of total givens. Randomly sampled.
        final int minBound = rand.nextInt(level.getMaxTotalGiven() - level.getMinTotalGiven()) + level.getMinTotalGiven();

        int i = -1;
        // Repeat as long as there are free fields.
        while (possible.isFree()) {
            /* This switcher decides for the
             * current difficulty which method
             * should be used.
             * Important is that all algorithms
             * in this switcher are all returning
             * just the index to work with, because
             * work on digging the hole is the same
             * for all.
             */
            switch (level) {
                case ExtremelyEasy: // Both are using the same case.
                case Easy:
                    i = seq4RandomizingGlobally();
                    break;
                case Medium:
                    i = seq3JumpingOneCell(i);
                    break;
                case Hard:
                    i = seq2WanderingAlongS(i);
                    break;
                case Evil:
                    i = seq1Left2RightAndTop2Bottom(i);
                    break;
                default: // If because for whatever reason I implement a new difficulty.
                    throw new ArithmeticException("Level" + level + "NOT IMPLEMENTED YET");
            }

            // Generates temporary digged sudoku.
            CompSudoku tmp = sudoku.digClone(i);
            /* If the temporary sudoku is acceptable for all
             * rules the temporary gets the current one.
             */
            if (isUnique(tmp, sudoku.getAtIndex(i)) && tmp.getNumTotalFields() >= minBound
                    && tmp.getLowerBoundRC() >= level.getMinGivenRC())
                sudoku = tmp;

            // Declare that this index was in fact worked on.
            possible.setAtIndex(i, false);
        }
    }

    //@formatter:off
    /**
     * See Sequence 1 on the paper.
     * It is described like this:
     * <pre><code>
     * ┌───────────────┐
     * │ ┌───┬───┬───┐ │
     * │ │ i─┼─▶─┼─▶─┼─┘
     * │ ├───┼───┼───┤
     * └─┼─▶─┼─▶─┼─▶ │
     *   ├───┼───┼───┤
     *   │       │
     * </code></pre>
     * @param lastI The previous index.
     * @return The next index in the series.
     */
    //@formatter:on
    private int seq1Left2RightAndTop2Bottom(int lastI) {
        return (lastI + 1) % FIELD_COUNT;
    }

    //@formatter:off
    /**
     * See Sequence 2 on the paper.
     * It is drawn like this:
     * <pre><code>
     * ┌───┬───┬───┐
     * │ i─┼─▶─┼─▶││
     * ├───┼───┼──┼┤
     * │ │◀┼──◀┼──▼│
     * ├─┼─┼───┼───┤
     * │ ▼─┼─▶ │
     *     │
     * </code></pre>
     * @param lastI The previous index.
     * @return The next index in the series.
     */
    //@formatter:on
    private int seq2WanderingAlongS(int lastI) {
        if (lastI < 0)
            return 0;
        int i;
        if ((lastI / FIELD_SIZE) % 2 == 0) {
            if ((lastI + 1) % FIELD_SIZE == 0)
                i = lastI + FIELD_SIZE;
            else
                i = lastI + 1;
        } else {
            if (lastI % FIELD_SIZE == 0)
                i = lastI + FIELD_SIZE;
            else
                i = lastI - 1;
        }

        return i % FIELD_COUNT;
    }

    //@formatter:off
    /**
     * See Sequence 3 on the paper.
     * It is drawn like this:
     * <pre><code>
     * ┌───┬───┬───┐
     * │ i─┼───┼─▶││
     * ├───┼───┼──┼┤
     * │ ┌─┼─ ◀┼──┘│
     * ├─┼─┼───┼───┤
     * │ ▼─┼───┴─▶
     *     │
     * </code></pre>
     * @param lastI The previous index.
     * @return The next index in the series.
     */
    //@formatter:on
    private int seq3JumpingOneCell(int lastI) {
        if (lastI < 0)
            return 0;
        int i;
        /* If somebody knows a better looking
         * solution. PLEASE implement it. I don't
         * like this decision code here.
         */
        if ((lastI / FIELD_SIZE) % 2 == 0) {
            if ((lastI + 1) % FIELD_SIZE == 0)
                i = lastI + FIELD_SIZE - 1;
            else if ((lastI + 2) % FIELD_SIZE == 0)
                i = lastI + FIELD_SIZE + 1;
            else
                i = lastI + 2;
        } else {
            if (lastI % FIELD_SIZE == 1)
                i = lastI + FIELD_SIZE - 1;
            else if (lastI % FIELD_SIZE == 0)
                i = lastI + FIELD_SIZE + 1;
            else
                i = lastI - 2;
        }
        if (i % FIELD_COUNT != i)
            i = (i + 1) % 2;
        return i;
    }


    /**
     * There's no sequence here.
     * All this one does is just simply
     * choosing a free title by random.
     *
     * This is sequence 4 on the paper.
     * @return The next index in the series.
     */
    private int seq4RandomizingGlobally() {
        int i;
        while (possible.isFree()) {
            i = rand.nextInt(FIELD_COUNT);
            if (possible.getAtIndex(i))
                return i;
        }
        return -1;
    }


    /**
     * Test if the current sudoku replace will
     * still create a unique solution. This is
     * done by trying to solve a sudoku that
     * replaces the to be duged number with any
     * other allowed number at this index.
     * In the paper it is called: Reduction to
     * absurdity.
     *
     * @param tmp  The digged sudoku.
     * @param orig The original character on the
     *             currently free hole.
     * @return True if it is unique.
     */
    private boolean isUnique(CompSudoku tmp, byte orig) throws ValueFormatException {
        List<CompSudoku> nodes = tmp.expand();
        for (CompSudoku node : nodes) {
            if (node.getAtIndex(tmp.getIndex()) != orig) {
                if (SudokuSolver.isSolvable(node))
                    return false;
            }
        }
        return true;
    }

}
