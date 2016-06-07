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

import com.georg.Generator.CompSudoku;
import com.georg.Generator.LasVegasAlgorithm;
import com.georg.Generator.StaticGenerator;
import com.georg.Generator.SudokuSolver;
import com.georg.Level;
import com.georg.SaveHandler;
import com.georg.Sudoku;
import com.georg.ValueFormatException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.georg.Sudoku.NAN;

public class UserInterface extends JFrame {
    static final Color warnColor = new Color(254, 114, 85);

    private JPanel panel;
    private JPanel rootPanel;
    private JPanel sudokuField;
    private JButton clearButton;
    private JTabbedPane tabbedPane1;
    private JPanel createTab;
    private JPanel solveTab;
    private JComboBox difficultyCombo;
    private JButton generateButton;
    private JButton solveButton;
    private JButton solveAllButton;
    private JButton saveButton;
    private JButton createTerminalPatternButton;
    private JButton quitButton;
    private List<SudokuSpinner> sudokuTextAreas;
    private GUISudoku sudoku;

    private static String lastDir;

    public GUISudoku getSudoku() {
        return sudoku;
    }

    public List<SudokuSpinner> getSudokuTextAreas() {
        return sudokuTextAreas;
    }

    public UserInterface() {
        setTitle("Sudoku Generator");

        Thread.setDefaultUncaughtExceptionHandler((Thread t, Throwable e) -> {
            if (!(e instanceof  java.lang.ThreadDeath))
                printErrorMessage(e);
        });

        setContentPane(rootPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        pack();
        setResizable(false);
        setVisible(true);

        // Clear button
        clearButton.addActionListener((ActionEvent e) -> {
            sudoku = new GUISudoku(Level.None);
            sudokuUpdate();
        });

        for (Level l : Level.values()) {
            if (l != Level.None)
                difficultyCombo.addItem(l);
        }
        difficultyCombo.setSelectedItem(Level.Easy);

        generateButton.addActionListener((ActionEvent e) -> sudokuButtonManagement(3));

        solveButton.addActionListener((ActionEvent e) -> sudokuButtonManagement(2));

        solveAllButton.addActionListener((ActionEvent e) -> sudokuButtonManagement(0));

        saveButton.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser(lastDir);

            fileChooser.setDialogTitle("Specify location to save.");
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text file", "txt"));
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PDF File", "pdf"));

            int userSelection = fileChooser.showSaveDialog(rootPanel);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                try {
                    String name = fileChooser.getSelectedFile().getAbsolutePath();
                    String prefix = "";
                    if (fileChooser.getFileFilter() instanceof FileNameExtensionFilter)
                        prefix = ((FileNameExtensionFilter)fileChooser.getFileFilter()).getExtensions()[0];

                    if (!name.toLowerCase().endsWith("."+prefix.toLowerCase()) && prefix.length()>0)
                        name+="."+prefix;

                    lastDir = fileChooser.getSelectedFile().getParent();

                    SaveHandler.save(name, sudoku);

                } catch (Exception exception) {
                    printErrorMessage(exception);
                }
            }
        });

        createTerminalPatternButton.addActionListener((ActionEvent e) -> sudokuButtonManagement(1));

        quitButton.addActionListener(e -> {
            dispose();
            System.exit(0);
        });
    }

    private void createUIComponents() {
        sudokuField = new JPanel(new GridLayout(Sudoku.FIELD_SIZE, Sudoku.FIELD_SIZE));

        sudokuTextAreas = new ArrayList<>();
        mFocusListener fcsListener = new mFocusListener();
        sudoku = new GUISudoku(Level.None);

        for (int i = 0; i < Sudoku.FIELD_COUNT; i++) {
            SpinnerNumberModel model = new SpinnerNumberModel(0,sudoku.getAtIndexN(i),9,1);
            SudokuSpinner spinner = new SudokuSpinner(model, i);
            ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().addFocusListener(fcsListener);
            spinner.addChangeListener(new SudokuChangeListener(this));

            sudokuTextAreas.add(spinner);
            sudokuField.add(sudokuTextAreas.get(i));
        }
        JFormattedTextField test = ((JSpinner.DefaultEditor)sudokuTextAreas.get(0).getEditor()).getTextField();
        test.setText(test.getText());
        test.selectAll();
    }

    private void sudokuUpdate() {
        if (sudoku==null)
            sudoku = new GUISudoku(Level.None);
        for (int i = 0; i < Sudoku.FIELD_COUNT; i++) {
            sudokuTextAreas.get(i).setValue((int)sudoku.getAtIndexN(i));
        }
    }

    private void sudokuButtonManagement(int choice) {
        Thread thread = new Thread(() -> {
            try {
                if (sudoku.freeFields() == 0 && choice!=1 && choice!=3)
                    JOptionPane.showMessageDialog(rootPanel, "The sudoku has no free fields!", "Nothing to do", JOptionPane.INFORMATION_MESSAGE);
                else {

                    int i = -1;

                    long start = System.currentTimeMillis();
                    switch (choice) {
                        case 0:
                            i = SudokuSolver.solutions(sudoku);
                            break;
                        case 1:
                            sudoku = new GUISudoku(LasVegasAlgorithm.LasVegas(Level.None));
                            break;
                        case 2:
                            SudokuSolver.DFSLV(new CompSudoku(sudoku), System.currentTimeMillis() * 10, 0, 1);
                            break;
                        case 3:
                            sudoku = new GUISudoku(StaticGenerator.GenerateSudoku((Level)difficultyCombo.getSelectedItem()));
                            break;
                    }

                    long end = System.currentTimeMillis();
                    GeneratingDialog.externalCancel();

                    String win = "";
                    String msg = "";
                    boolean show = true;
                    switch (choice) {
                        case 0:
                            msg = "The sudoku has exactly: " + i + " solutions.";
                            win = "Number of solutions";
                            break;
                        case 1:
                            msg = "Terminal pattern generated successfully.";
                            win = "Terminal Pattern created";
                            break;
                        case 2:
                            Sudoku su = SudokuSolver.getLastField();
                            if (su!=null) {
                                sudoku = new GUISudoku(su);
                                msg = "Possible solution generated successfully.";
                                win = "Solution generated";
                            } else {
                                msg = "The sudoku couldn't be solved!";
                                win = "Sudoku Error";
                            }
                            break;
                        case 3:
                            show=false;
                            break;
                    }

                    if (show)
                        JOptionPane.showMessageDialog(rootPanel, msg +
                                        "\nThe algorithm needed: " + formatTime(end - start),
                                win, win.toLowerCase().contains("error")?JOptionPane.ERROR_MESSAGE:JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (ValueFormatException e) {
                GeneratingDialog.externalCancel();
                printErrorMessage(e);
            }
        });
        new GeneratingDialog(thread);
        sudokuUpdate();
    }

    void printErrorMessage(Throwable e) {
        JOptionPane.showMessageDialog(rootPanel, e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
    }

    private String formatTime(long ms) {
        long SSS = ms%1000;
        long hh  = ms/1000;
        long ss  = hh%60;
        hh/=60;
        long mm  = hh%60;
        hh/=60;

        String format;
        Object[] in;
        if(hh!=0) {
            format = "%dh %02dm %02ds %03dms";
            in = new Object[]{hh,mm,ss,SSS};
        } else if(mm!=0) {
            format = "%02dm %02ds %03dms";
            in = new Object[]{mm,ss,SSS};
        } else if(ss!=0) {
            format = "%02ds %03dms";
            in = new Object[]{ss,SSS};
        } else {
            format = "%03dms";
            in = new Object[]{SSS};
        }

        return String.format(Locale.US, format, in);
    }
}
