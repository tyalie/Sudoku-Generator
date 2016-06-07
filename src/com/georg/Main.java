/*
 * MIT License
 *
 * Copyright (c) 2016 Georg A. Friedrich
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.georg;

import com.georg.GUI.UserInterface;
import com.georg.Generator.CompSudoku;
import com.georg.Generator.StaticGenerator;
import com.georg.Generator.SudokuSolver;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new UserInterface();
    }

    private static void testSolver() throws ValueFormatException {
        Sudoku m = new Sudoku(Level.Easy, new byte[]{
                -1, -1, -1, -1, -1, -1, -1, -1, -1,
                8, 7, 3, -1, -1, -1, -1, -1, 1,
                -1, -1, -1, -1, -1, -1, -1, 6, -1,
                -1, -1, 4, 7, -1, 3, 5, -1, -1,
                -1, 6, -1, -1, -1, 1, 7, -1, 4,
                -1, -1, -1, 9, -1, 5, -1, -1, -1,
                5, 8, -1, -1, -1, 7, 1, 2, -1,
                2, 1, -1, 3, -1, -1, 6, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1});
        SudokuSolver.DFSLV(new CompSudoku(m), System.currentTimeMillis() * 2, 0, 1);
        try {
            SaveHandler.save("/Users/Georg/Desktop/out.pdf", SudokuSolver.getLastField());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void testSudokuGen(Level l) throws ValueFormatException {
        Sudoku su = StaticGenerator.GenerateSudoku(l);
        System.out.println(su + "\n\n");
        int i = SudokuSolver.solutions(su);
        assert 1 == i : "The sudoku has " + i + " solution(s)";
    }

    private static void testSaveHandler(String name) throws ValueFormatException {
        Sudoku su = StaticGenerator.GenerateSudoku(Level.Evil);


        try {
            SaveHandler.save(name, su);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int i = SudokuSolver.solutions(su);
        assert 1 == i : "The sudoku has " + i + " solution(s)";
    }
}
