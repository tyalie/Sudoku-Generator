package com.georg.GUI;

import com.georg.Sudoku;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by Georg on 05/06/16.
 */
class SudokuSpinner extends JSpinner {
    private int ID;

    int getID() {
        return ID;
    }

    SudokuSpinner(SpinnerModel model, int ID) {
        super(model);
        this.ID = ID;

        updateColor(false);

        JFormattedTextField textField = ((JSpinner.DefaultEditor)getEditor()).getTextField();
        textField.setOpaque(false);
        textField.setBackground(new Color(0,0,0,0));
        textField.setBorder(null);
        textField.setHorizontalAlignment(JTextField.CENTER);

        //                      t l b r
        int[] bordW = new int[]{0,0,0,0};
        if (ID% Sudoku.BLOCK_SIZE == Sudoku.BLOCK_SIZE-1)
            bordW[3] = 1;
        if (ID%Sudoku.BLOCK_SIZE == 0)
            bordW[1] = 1;
        if ((ID/Sudoku.FIELD_SIZE)%Sudoku.BLOCK_SIZE == Sudoku.BLOCK_SIZE-1)
            bordW[2] = 1;
        if ((ID/Sudoku.FIELD_SIZE)%Sudoku.BLOCK_SIZE == 0)
            bordW[0] = 1;

        Border border1 = BorderFactory.createMatteBorder(1-bordW[0],1-bordW[1],1-bordW[2],1-bordW[3], new Color(0x90, 0x90, 0x90));
        Border border2 = BorderFactory.createMatteBorder(bordW[0],bordW[1],bordW[2],bordW[3],Color.BLACK);

        int cx = (int)(300/18f - getFontMetrics(getFont()).charWidth('0')/1.7);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createCompoundBorder(border2, border1), BorderFactory.createEmptyBorder(0,cx,0,cx)));

        setUI(new javax.swing.plaf.basic.BasicSpinnerUI(){
            protected Component createNextButton(){
                Component c = new JButton();
                c.setPreferredSize(new Dimension(0,0));
                c.setFocusable(false);
                return c;
            }
            protected Component createPreviousButton(){
                Component c = new JButton();
                c.setPreferredSize(new Dimension(0,0));
                c.setFocusable(false);
                return c;
            }
        });
    }

    void updateColor(boolean inval) {
        if ((Integer) getValue() == 0)
            setBackground(Color.LIGHT_GRAY);
        else if (inval)
            setBackground(UserInterface.warnColor);
        else
            setBackground(new Color(0xee, 0xee, 0xee));

    }

    void setEditable(boolean value) {
        ((JSpinner.DefaultEditor)getEditor()).getTextField().setEditable(value);
    }
}
