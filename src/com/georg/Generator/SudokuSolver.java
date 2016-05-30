package com.georg.Generator;

import com.georg.Sudoku;

import java.util.List;

/**
 * Created by Georg on 30/05/16.
 */
public class SudokuSolver {
    private static final int ERROR = -1;

    public static int solutions(Sudoku sudoku) {
        return DFS(new CompSudoku(sudoku), true,0);
    }

    public static boolean isSolvable(Sudoku sudoku) {
        return DFS(new CompSudoku(sudoku), false,0 ) > 0;
    }

    private static int DFS(CompSudoku sudoku, boolean end, int i) {
        if(sudoku.isIndexLast())
            return 1;

        //if(i>3)
        //    return 0;


        List<CompSudoku> compSudokus = sudoku.expand();
        if(compSudokus.size()==0)
            return 0;

        int sum = 0;
        for(CompSudoku comp : compSudokus) {
            //System.out.println(comp.toString()+"\n\n");
            sum += DFS(comp, end, i+1);
            if(!end && sum!=0)
                break;
        }

        return sum;
    }


}
