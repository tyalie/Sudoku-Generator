package com.georg.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Created by Georg on 05/06/16.
 * <p>
 * Automatically selectes the whole text
 * when the JFormattedTextField is focused.
 */
class mFocusListener implements FocusListener {
    @Override
    public void focusGained(FocusEvent e) {
        dumpInfo(e);
    }

    @Override
    public void focusLost(FocusEvent e) {
        dumpInfo(e);
    }

    private void dumpInfo(FocusEvent e) {
        final Component c = e.getComponent();
        if(c instanceof JFormattedTextField) {
            SwingUtilities.invokeLater(() -> {
                ((JFormattedTextField)c).setText(((JFormattedTextField)c).getText());
                ((JFormattedTextField)c).selectAll();
            });
        }
    }

}
