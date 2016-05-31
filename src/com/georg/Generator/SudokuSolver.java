package com.georg.Generator;

import com.georg.Sudoku;

import java.util.List;

/**
 * Created by Georg on 30/05/16.
 *
 * A class to solve sudokus in various ways.
 */
public class SudokuSolver {
    /**
     * Find number of solutions for given sudoku.
     * @param sudoku The sudoku to solve.
     * @return Returns number of solutions.
     */
    static int solutions(Sudoku sudoku) {
        return DFS(new CompSudoku(sudoku), true);
    }

    /**
     * Gives an answer to the question:
     * "Is the sudoku solvable."
     * This is a faster variant of {@link #solutions(Sudoku)}
     * if the sudoku has a solution.
     * @param sudoku The sudoku to evaluate.
     * @return True if the sudoku is solvable.
     */
    public static boolean isSolvable(Sudoku sudoku) {
        return DFS(new CompSudoku(sudoku), false) > 0;
    }

    /**
     * Depth first search algorithm to find all
     * possible solutions for the sudoku using
     * a brut force like algorithm that tries
     * out every valid combination of on the field.
     * @param sudoku The sudoku to solve.
     * @param end False if just one solution should
     *            be found, true if the total number
     *            of all possible solution should returned.
     * @return Returns the number of solutions found.
     */
    private static int DFS(CompSudoku sudoku, boolean end) {
        if(sudoku.isIndexLast())
            return 1;

        List<CompSudoku> compSudokus = sudoku.expand();
        if(compSudokus.size()==0)
            return 0;

        int sum = 0;
        for(CompSudoku comp : compSudokus) {
            sum += DFS(comp, end);
            if(!end && sum!=0)
                break;
        }

        return sum;
    }


    static Sudoku lastField = null;
    static int cSol = 0;
    /**
     * Tries to find a solution for a given field
     * and returns exactly this solution.
     * @param sudoku
     * @param start
     * @param maxTime
     * @return
     */
    static Sudoku DFSLV(CompSudoku sudoku, final long start, final int maxTime, final int maxSol) {
        if(sudoku.isIndexLast())
            return sudoku;

        if(System.currentTimeMillis()-start > maxTime)
            return null;

        List<CompSudoku> compSudokus = sudoku.expand();
        if(compSudokus.size()==0)
            return null;

        for(CompSudoku comp : compSudokus) {
            Sudoku ret = DFSLV(comp, start, maxTime, maxSol);
            if(ret!=null) {
                if(cSol<=maxSol)
                    lastField = ret;
                if(cSol==maxSol)
                    return null;
                cSol++;
            }

        }
        return null;
    }


}
