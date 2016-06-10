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

import javax.swing.*;
import java.io.Serializable;

/**
 * Created by Georg A. Friedrich on 09/06/16
 * <p>
 * Replace for {@link SpinnerNumberModel}, that shows
 * and evaluates a free field, if the current sudoku
 * field value is 0.
 * <p>
 * The input must be an integer with a
 * forced step-size of 1.
 */
class SudokuNumberSpinner extends AbstractSpinnerModel implements Serializable {
    /**
     * The current value
     */
    private int value = 0;
    /**
     * The maximum value of
     * the spinner (included)
     */
    private int max = 0;
    /**
     * The minimum value of
     * the spinner (included)
     */
    private int min = 0;

    /**
     * The initializer.
     *
     * @param value The current value
     * @param min   The {@link #min minimum} value
     * @param max   The {@link #max maximum} value
     */
    SudokuNumberSpinner(int value, int min, int max) {
        this.value = value;
        this.min = min;
        this.max = max;
    }

    /**
     * Returns the formatted value of
     * the current spinner. This action
     * is triggered to write on the screen,
     * which means that 0 must be displayed
     * here as an empty field.
     *
     * @return The formatted value.
     */
    @Override
    public Object getValue() {
        return getValueFormat(value);
    }

    /**
     * Sets the value of the current model. This
     * works by extracting the correct value out of
     * the {@link String} or the {@link Number} instance
     * and setting it, if it's valid, to the current {@link #value}.
     *
     * @param value The value object to set for this Spinner.
     *              Must be a {@link String} or {@link Number}.
     */
    @Override
    public void setValue(Object value) {
        if ((value == null))
            throw new IllegalArgumentException("input value is null." + "in SudokuNumberSpinner/setValue");
        if ((!(value instanceof String) && !(value instanceof Number)))
            throw new IllegalArgumentException("illegal value" + value.getClass() + "in SudokuNumberSpinner/setValue");

        int val = 0;
        if (value instanceof String) {
            String in = ((String) value).replaceAll("\\D", "");

            if (in.length() > 0)
                val = Integer.valueOf((String) value);
        } else
            val = ((Number) value).intValue();
        if (val != this.value) {
            if (val > max)
                this.value = max;
            else if (val < min)
                this.value = min;
            else
                this.value = val;
        }
        fireStateChanged();
    }

    /**
     * The formatted value, where every number
     * except 0 is normally printed and 0 is
     * displayed as an empty string.
     *
     * @param val The value to format.
     * @return The formatted string object.
     */
    private Object getValueFormat(int val) {
        if (val == 0)
            return "";
        return Integer.toString(val);
    }

    /**
     * @return The value integer.
     */
    int getNumberVal() {
        return value;
    }

    /**
     * Returns the next value, is the
     * same if next number would be out
     * of range.
     *
     * @return The truncated next number
     * of the spinner (i++)
     */
    @Override
    public Object getNextValue() {
        if (value >= max)
            return getValueFormat(value);
        return getValueFormat(value + 1);
    }

    /**
     * Returns the previous value, is the
     * same if previous number would be out
     * of range.
     *
     * @return The truncated previous number
     * of in the spinner (i--)
     */
    @Override
    public Object getPreviousValue() {
        if (value <= min)
            return getValueFormat(value);
        return getValueFormat(value - 1);
    }
}
