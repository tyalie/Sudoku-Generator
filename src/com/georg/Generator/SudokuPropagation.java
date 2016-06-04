package com.georg.Generator;

import com.georg.Sudoku;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;

import static com.georg.Sudoku.*;

/**
 * Created by Georg on 03/06/16.
 */
public class SudokuPropagation {

    private CompSudoku sudoku;
    private Sudoku solvedSudoku;


    public SudokuPropagation(Sudoku su) {
        sudoku = new CompSudoku(su);
        SudokuSolver.DFSLV(sudoku, System.currentTimeMillis()*2, 0, 1);
        solvedSudoku = SudokuSolver.getLastField();
    }


    private boolean prop1_TwoDigits(byte d1, byte d2) {
        if(d1<=0 || d1 > MAX_NUM)
            throw new ArithmeticException("d1 is out of range");
        if(d2<=0 || d2 > MAX_NUM)
            throw new ArithmeticException("d2 is out of range");

        for (int i = 0; i < BLOCK_SIZE*BLOCK_SIZE; i++) {
            int i_d1=-1, i_d2=-1;
            for (int j = 0; j < BLOCK_SIZE*BLOCK_SIZE; j++) {
                int x = i%3*BLOCK_SIZE+j%3+(j/3+i/3*3)*FIELD_SIZE;
                if(solvedSudoku.getAtIndex(x)==d1)
                    i_d1 = x;
                else if(solvedSudoku.getAtIndex(x)==d2)
                    i_d2 = x;
            }

            if(i_d1==-1 || i_d2==-1)
                throw new ArithmeticException("Unreachable state reached");

            sudoku.swapIndex(i_d1, i_d2);
        }
        return true;
    }

    private boolean prop2_TwoColumnInBlock(int block, int col1, int col2) {
        if(col1<0 || col1>=BLOCK_SIZE || col2<0 || col2>=BLOCK_SIZE)
            throw new ArithmeticException("Col1 or col2 are not in range");
        if(block<0 || block>=BLOCK_SIZE)
            throw new ArithmeticException("Bock is out of range");

        if(col1==col2)
            return false;

        for (int i = 0; i < FIELD_SIZE; i++)
            sudoku.swapIndex(CompSudoku.getStaticIndex(block * BLOCK_SIZE + col1, i),
                    CompSudoku.getStaticIndex(block * BLOCK_SIZE + col2, i));
        return true;
    }

    private boolean prop3_TwoColumnOfBlock(int col1, int col2) {
        if(col1<0 || col1>=BLOCK_SIZE || col2<0 || col2>=BLOCK_SIZE)
            throw new ArithmeticException("Col1 or col2 are not in range");
        for(int i = 0; i< FIELD_SIZE; i++) {
            for (int j = 0; j < BLOCK_SIZE; j++)
                sudoku.swapIndex(CompSudoku.getStaticIndex(col1*BLOCK_SIZE+j,i),
                        CompSudoku.getStaticIndex(col2*BLOCK_SIZE+j, i));
        }
        return true;
    }

    private boolean prop4_GridRolling(int deg) {
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
                su.setAtField(i, sudoku.getAtIndex(x,y));
                solvedSu.setAtField(i, solvedSudoku.getAtIndex(x,y));
            }
        }

        sudoku = su;
        solvedSudoku = solvedSu;
        return true;
    }

    public void randomSampling(int steps) {
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

            if(!ret)
                i--;
        }
    }

    public Sudoku getSudoku() {
        return sudoku;
    }
}
