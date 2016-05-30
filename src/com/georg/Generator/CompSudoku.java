package com.georg.Generator;

import com.georg.Sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Georg on 30/05/16.
 */
public class CompSudoku extends Sudoku {
    private int index = 0;

    public CompSudoku(Level l) {
        super(l);
    }

    private CompSudoku(Level l, byte[] field) throws RuntimeException {
        super(l, field);
    }

    public CompSudoku(Sudoku su) {
        this(su.getHardness(), su.getField());
        moveNext();
    }
    private CompSudoku(CompSudoku su) {
        this(su.getHardness(), su.getField().clone());
        index = su.index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int i) {
        index = i;
    }

    public void moveNext() {
        while (field[index]!=NAN && !isIndexLast())
            moveIndex();

    }

    public boolean isIndexLast() {
        for (int i = index; i < 81; i++) {
            if(field[i]==NAN)
                return false;
        }

        return true;
    }

    public void moveIndex() {
        if(index<(FIELD_SIZE*FIELD_SIZE-1))
            index++;
    }

    public void resetIndex() {
        index = 0;
    }

    public void moveIndexReverse() {
        if(index>0)
            index--;
    }

    public void addSecureByte(byte in) {
        if(field[index] != NAN)
            return;
        field[index] = in;
        moveNext();
    }

    public List<CompSudoku> expand() {
        Boolean[] possible = new Boolean[MAX_NUM];
        Arrays.fill(possible, true);
        possible = rule_3(rule_2(rule_1(possible)));

        List<CompSudoku> ret = new ArrayList<>();
        for (byte i = 1; i <= MAX_NUM; i++) {
            if(possible[i-1]) {
                CompSudoku c = new CompSudoku(this);
                c.addSecureByte(i);
                ret.add(c);
            }
        }
        return ret;
    }



    /**
     * Rule one is the first rule for solving sudokus named in
     * the paper. In this case it is that there cannot be
     * any double numbers in a row.
     * @param possible the number variable. From 1 to 9, where false
     *                 marks not available anymore and true marks the
     *                 opposite.
     * @return returns the edited, now valid number variable. See input.
     */
    private Boolean[] rule_1(Boolean[] possible) {
        for (int i = 0; i < FIELD_SIZE; i++) {
            byte f = field[i+index/FIELD_SIZE*9];
            if (f != NAN)
                possible[f-1] = false;
        }
        return possible;
    }

    /**
     * Rule one is the second rule for solving sudokus named in
     * the paper. In this case there cannot be a double number
     * in a column.
     * @param possible the number variable. From 1 to 9, where false
     *                 marks not available anymore and true marks the
     *                 opposite.
     * @return returns the edited, now valid number variable. See input.
     */
    private Boolean[] rule_2(Boolean[] possible) {
        for (int i = 0; i < FIELD_SIZE; i++) {
            byte f = field[i * FIELD_SIZE + index % FIELD_SIZE];
            if(f != NAN)
                possible[f-1] = false;
        }
        return possible;
    }

    /**
     * Rule one is the third rule for solving sudokus named in
     * the paper. In this case there cannot be a double number
     * in a block.
     * @param possible the number variable. From 1 to 9, where false
     *                 marks not available anymore and true marks the
     *                 opposite.
     * @return returns the edited, now valid number variable. See input.
     */
    private Boolean[] rule_3(Boolean[] possible) {
        for (int i = 0; i < FIELD_SIZE; i++) {
            byte f = field[i % BLOCK_SIZE + (i / BLOCK_SIZE) * FIELD_SIZE
                    + (index % FIELD_SIZE)/BLOCK_SIZE * BLOCK_SIZE  + index / (FIELD_SIZE*BLOCK_SIZE) * 27];
            if(f != NAN)
                possible[f-1] = false;
        }
        return possible;
    }
}
