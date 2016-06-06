package com.georg.GUI;

import com.georg.Generator.CanBeDugList;
import com.georg.Sudoku;
import com.georg.ValueFormatException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created by Georg on 05/06/16.
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
