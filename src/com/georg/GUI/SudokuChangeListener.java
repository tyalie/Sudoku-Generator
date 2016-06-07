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

package com.georg.GUI;

import com.georg.Generator.CanBeDugList;
import com.georg.Sudoku;
import com.georg.ValueFormatException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created by Georg A. Friedrich on 05/06/16
 */
class SudokuChangeListener implements ChangeListener {
    private UserInterface orig;

    SudokuChangeListener(UserInterface orig) {
        this.orig = orig;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() instanceof SudokuSpinner) {
            SudokuSpinner spinner = (SudokuSpinner)e.getSource();
            if (orig.getSudoku()!= null)
                orig.getSudoku().setAtIndex(spinner.getID(), ((Integer)spinner.getValue()).byteValue() );

            try {
                CanBeDugList inval = orig.getSudoku().getInvalid();
                if (inval.getCount() < Sudoku.FIELD_COUNT || spinner.getBackground() == UserInterface.warnColor) {
                    for (int i = 0; i < Sudoku.FIELD_COUNT; i++)
                        orig.getSudokuTextAreas().get(i).updateColor(!inval.getAtIndex(i));
                } else
                    spinner.updateColor(false);
            } catch (ValueFormatException err) {
                orig.printErrorMessage(err);
            }
        }
    }
}
