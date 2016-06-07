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

import com.georg.Sudoku;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by Georg on 05/06/16.
 * <p>
 * A custom spinner class used
 * for the GUI sudoku field.
 */
class SudokuSpinner extends JSpinner {
    /**
     * The ID is equivalent to the
     * corresponding index on the
     * sudoku field.
     */
    private int ID;

    /**
     * The initializer for the spinner
     * view.
     * <p>
     * Creates a spinner without buttons,
     * the normal color for the tiles and
     * the corresponding borders, that are
     * influenced by its position on the
     * field.
     *
     * @param model The SpinnerNumberModel
     *              for this spinner.
     * @param ID    The index that this tile
     *              should represent.
     */
    SudokuSpinner(SpinnerNumberModel model, int ID) {
        super(model);
        this.ID = ID;

        /* Updates the color to the original state.
         * Because all spinners are filled with zeros
         * at the start color will be a light gray. */
        updateColor(false);

        /* Deactivates the color of the text-field.
         * Expect the text nothing of it will be
         * visible. */
        JFormattedTextField textField = ((JSpinner.DefaultEditor) getEditor()).getTextField();
        textField.setOpaque(false);
        textField.setBackground(new Color(0, 0, 0, 0));
        textField.setBorder(null);
        textField.setHorizontalAlignment(JTextField.CENTER);

        //                      t l b r
        int[] bordW = new int[]{0, 0, 0, 0};
        if (ID % Sudoku.BLOCK_SIZE == Sudoku.BLOCK_SIZE - 1)
            bordW[3] = 1;
        if (ID % Sudoku.BLOCK_SIZE == 0)
            bordW[1] = 1;
        if ((ID / Sudoku.FIELD_SIZE) % Sudoku.BLOCK_SIZE == Sudoku.BLOCK_SIZE - 1)
            bordW[2] = 1;
        if ((ID / Sudoku.FIELD_SIZE) % Sudoku.BLOCK_SIZE == 0)
            bordW[0] = 1;

        Border border1 = BorderFactory.createMatteBorder(1 - bordW[0], 1 - bordW[1], 1 - bordW[2], 1 - bordW[3], new Color(0x90, 0x90, 0x90));
        Border border2 = BorderFactory.createMatteBorder(bordW[0], bordW[1], bordW[2], bordW[3], new Color(0, 0, 0));

        // To center the text-field perfectly in the center.
        int cx = (int) (300 / 18f - getFontMetrics(getFont()).stringWidth(Integer.toString(Sudoku.MAX_NUM)) / 1.7);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createCompoundBorder(border2, border1), BorderFactory.createEmptyBorder(0, cx, 0, cx)));

        // Hides the up and down buttons.
        setUI(new javax.swing.plaf.basic.BasicSpinnerUI() {
            protected Component createNextButton() {
                Component c = new JButton();
                c.setPreferredSize(new Dimension(0, 0));
                c.setFocusable(false);
                return c;
            }

            protected Component createPreviousButton() {
                Component c = new JButton();
                c.setPreferredSize(new Dimension(0, 0));
                c.setFocusable(false);
                return c;
            }
        });
    }

    /**
     * @return Gets the {@link #ID} of the spinner.
     */
    int getID() {
        return ID;
    }

    /**
     * This method updates the color of
     * the spinner with its own logic.
     * <table>
     * <tr>
     * <td>0</td>
     * <td>{@link Color#lightGray}</td>
     * </tr>
     * <tr>
     * <td>[1,{@link Sudoku#MAX_NUM MAX_NUM}]</td>
     * <td>0xEEEEEE</td>
     * </tr>
     * <tr>
     * <td>Rules<br>broken</td>
     * <td>{@link UserInterface#warnColor warnColor}</td>
     * </tr>
     * </table>
     *
     * @param inval True if this tile is
     *              brakes any rules.
     *              If not set it to false.
     */
    void updateColor(boolean inval) {
        if ((Integer) getValue() == 0)
            setBackground(Color.LIGHT_GRAY);
        else if (inval)
            setBackground(UserInterface.warnColor);
        else
            setBackground(new Color(0xee, 0xee, 0xee));

    }

    /**
     * Activates or deactivates the editability
     * of this spinner view.
     *
     * @param value True for editable, false for else
     */
    @SuppressWarnings("unused")
    void setEditable(boolean value) {
        ((JSpinner.DefaultEditor) getEditor()).getTextField().setEditable(value);
    }
}
