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

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Created by Georg on 05/06/16.
 * <p>
 * Class to create trigger, that is
 * activated if one number field was edited,
 * that includes user change, but also
 * change of the content by the system.
 */
class SudokuChangeListener implements ChangeListener {
    private UserInterface orig;

    /**
     * @param orig The triggering class
     */
    SudokuChangeListener(UserInterface orig) {
        this.orig = orig;
    }

    /**
     * The state of one of the sudoku fields changed.
     * This method marks automatically every invalid
     * number field red and will change the sudoku field.
     *
     * @param e The change event.
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() instanceof SudokuSpinner) {
            SudokuSpinner spinner = (SudokuSpinner) e.getSource();
            if (orig.getSudoku() != null) {
                // Change the sudoku field to the current value.
                orig.getSudoku().setAtIndex(spinner.getID(), ((Integer) spinner.getValue()).byteValue());

                try {
                    // Generates the list of all invalid fields
                    CanBeDugList inval = orig.getSudoku().getInvalid();
                    /* IF there are problems it will loop through
                     * every single spinner view and set the color
                     * correspondingly.
                     *
                     * This action will also be triggered when the
                     * current was a warning tile before. This
                     * erases every color on the last field on the sudoku.
                     */
                    if (inval.getCount() < Sudoku.FIELD_COUNT || spinner.getBackground() == UserInterface.warnColor) {
                        for (int i = 0; i < Sudoku.FIELD_COUNT; i++)
                            orig.getSudokuTextAreas().get(i).updateColor(!inval.getAtIndex(i));
                    } else // change own color if not.
                        spinner.updateColor(false);
                } catch (ValueFormatException err) {
                    orig.printErrorMessage(err);
                }
            }
        }
    }
}
