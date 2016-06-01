package com.georg.Generator;

import com.georg.Level;
import com.georg.Sudoku;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;

import java.util.List;

import static com.georg.Sudoku.FIELD_COUNT;

//@formatter:off
/**
 * Created by Georg on 31/05/16.
 *
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
    private CompSudoku sudoku;
    private XoRoShiRo128PlusRandom rand;
    private Level l;
    private CanBeDugList possible;

    public SudokuGen(Sudoku sudoku) {
        this.sudoku = new CompSudoku(sudoku);
        this.rand = new XoRoShiRo128PlusRandom();
        this.l = sudoku.getDifficulty();
    }

    public Sudoku getSudoku() {
        return sudoku;
    }

    CompSudoku getCompSudoku() {
        return sudoku;
    }

    /**
     * This is the main function of the
     * class. It digs the holes into the
     * sudoku. It is managing job 1-4.
     */
    public void digHoles() {
        possible = new CanBeDugList();
        final int minBound = rand.nextInt(l.getMaxTotalGiven() - l.getMinTotalGiven()) + l.getMinTotalGiven();

        while (possible.isFree()) {
            int i = -1;
            switch (l) {
                case ExtremelyEasy:
                case Easy:
                    i = seq4RandomizingGlobally();
                    break;
                default:
                    throw new ArithmeticException("Level" + l + "NOT IMPLEMENTED YET");
            }

            CompSudoku tmp = sudoku.digClone(i);
            if (tmp.getNumTotalFields() >= minBound && tmp.getLowerBoundRC() >= l.getMinGivenRC()
                    && isUnique(tmp, i, sudoku.getAtIndex(i)))
                sudoku = tmp;

            possible.setAtIndex(i, false);
        }
    }

    private int seq4RandomizingGlobally() {
        int i = 0;
        while (possible.isFree()) {
            i = rand.nextInt(FIELD_COUNT);
            if (possible.getAtIndex(i))
                return i;
        }
        return -1;
    }


    private boolean isUnique(CompSudoku tmp, int i, byte orig) {
        List<CompSudoku> nodes = tmp.expand();
        for (CompSudoku node : nodes) {
            if (node.getAtIndex(i) != orig) {
                if (SudokuSolver.isSolvable(node))
                    return false;
            }
        }
        return true;
    }

}
