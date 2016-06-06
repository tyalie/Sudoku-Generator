package com.georg.GUI;

import javax.swing.*;
import java.awt.event.*;

public class GeneratingDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonCancel;

    private static GeneratingDialog lastM;

    GeneratingDialog(final Thread sudokuGen) {
        lastM = this;
        sudokuGen.start();
        setContentPane(contentPane);
        setModal(true);

        buttonCancel.addActionListener(e -> onCancel(sudokuGen));

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel(sudokuGen);
            }
        });

        pack();
        setMinimumSize(getSize());
        setMaximumSize(getSize());
        setVisible(true);
    }

    @SuppressWarnings("deprecation")
    private void onCancel(Thread sudokuGen) {
        if (sudokuGen!=null)
            sudokuGen.stop();
        dispose();
    }

    static void externalCancel() {
        if (lastM!=null)
            lastM.onCancel(null);
        lastM=null;
    }

}
