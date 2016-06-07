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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Georg on 05/06/16.
 * <p>
 * This dialog can manage automatically
 * a thread and should always be displayed
 * if long calculations are done asynchronously
 * in the foreground.
 * <p>
 * Uses the deprecated {@link Thread#stop()},
 * because its easier to implement and doesn't
 * display a risk in this case.
 */
public class GeneratingDialog extends JDialog {
    /**
     * Holds the last instance. Used to
     * close this window when the thread
     * finishes its task, by triggering a
     * corresponding method in the thread class.
     */
    private static GeneratingDialog lastM;
    /* By IntelliJ's GUI Designer
     * generated variables.*/
    private JPanel contentPane;
    private JButton buttonCancel;

    /**
     * Create an instance of this class will
     * open an all-blocking dialog window that
     * informs the user about current current
     * calculations.
     * <p>
     * In the background it will start the
     * thread and init the action listeners for
     * close and "cancel" actions. Those will force
     * stop the thread by using the {@link Thread#stop()}
     * method.
     *
     * @param sudokuGen A not started thread instance.
     */
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
        setResizable(false);
        setVisible(true);
    }

    /**
     * Should be triggered by the
     * calculation thread, if the
     * task is done. This will close
     * window and set the underlying
     * GUI free again.
     */
    static void externalCancel() {
        if (lastM != null)
            lastM.onCancel(null);
        lastM = null;
    }

    /**
     * This method is triggered to
     * cancel the window and the thread.
     * <p>
     * Uses deprecated
     * {@link Thread#stop()}.
     *
     * @param sudokuGen The thread instance.
     */
    @SuppressWarnings("deprecation")
    private void onCancel(Thread sudokuGen) {
        if (sudokuGen != null)
            sudokuGen.stop();
        dispose();
    }

}
