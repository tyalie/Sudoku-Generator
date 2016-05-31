package com.georg.Generator;

import com.georg.Level;
import com.georg.Sudoku;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;

import java.util.List;

import static com.georg.Sudoku.FIELD_SIZE;
import static com.georg.Sudoku.MAX_NUM;
import static com.georg.Sudoku.NAN;

/**
 * Created by Georg on 31/05/16.
 *
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
     * @param l The playing level.
     * @return Returns a terminal pattern.
     */
    public static Sudoku LasVegas(Level l) {
        XoRoShiRo128PlusRandom rand = new XoRoShiRo128PlusRandom();
        while(true) {
            CompSudoku terminal = new CompSudoku(l);

            terminal.setField((byte)(rand.nextInt(MAX_NUM)+1));
            for (int i = 0; i < 11; i++) {
                rand.setSeed(System.currentTimeMillis());
                terminal.setIndex(rand.nextInt(FIELD_SIZE*FIELD_SIZE));
                List<Byte> pos = terminal.getAvailable();
                if(pos.size()>0 && terminal.getAtIndex()==NAN)
                    terminal.setField(pos.get(rand.nextInt(pos.size())));
                else
                    i--;
            }
            terminal.resetIndex();

            SudokuSolver.DFSLV(terminal, System.currentTimeMillis(), 100, rand.nextInt(10000));
            if(SudokuSolver.lastField != null)
                return SudokuSolver.lastField;
            SudokuSolver.lastField = null;
        }
    }
}
