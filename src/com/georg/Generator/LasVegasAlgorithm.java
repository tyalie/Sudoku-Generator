package com.georg.Generator;

import com.georg.Level;
import com.georg.Sudoku;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;

import java.util.List;
import java.util.Random;

import static com.georg.Sudoku.*;

/**
 * Created by Georg on 31/05/16.
 * <p>
 * This class defines the Las Vegas Algorithm
 * for generation of terminal patterns.
 */
public class LasVegasAlgorithm {
    /**
     * Las Vegas Algorithm for Sudoku generation.
     * With the findings from the paper named before,
     * this method generates a terminal pattern by
     * putting in eleven random numbers in a random
     * position on the field and will then solve and
     * return the solved state.
     * To have not repeating patterns in the first few
     * lines, the code will return the last sudoku found
     * in a given time. In this case 100ms.
     *
     * @param l The playing level.
     * @return Returns a terminal pattern.
     */
    public static Sudoku LasVegas(Level l) {
        XoRoShiRo128PlusRandom rand = new XoRoShiRo128PlusRandom();
        rand.setSeed(System.currentTimeMillis());
        while (true) {
            CompSudoku terminal = new CompSudoku(l);

            terminal.setField((byte) (rand.nextInt(MAX_NUM) + 1));
            for (int i = 0; i < 10; i++) {
                terminal.setIndex(rand.nextInt(FIELD_SIZE * FIELD_SIZE));
                List<Byte> pos = terminal.getAvailable();
                if (pos.size() > 0 && terminal.getAtIndex() == NAN)
                    terminal.setField(pos.get(rand.nextInt(pos.size())));
                else
                    i--;
            }
            terminal.resetIndex();

            SudokuSolver.DFSLV(terminal, System.currentTimeMillis(), 200, rand.nextInt(10000));
            Sudoku su = SudokuSolver.getLastField();
            if (su != null)
                return su;
        }
    }
}
