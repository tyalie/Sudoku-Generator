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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Georg on 05/06/16.
 */
public class UserInterface extends JFrame implements PropertyChangeListener{
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
    private List<SudokuSpinner> sudokuTextAreas;
    private GUISudoku sudoku;

    private static String lastDir;

    public GUISudoku getSudoku() {
        return sudoku;
    }

    public UserInterface() {
        setTitle("Sudoku Generator");

        Thread.setDefaultUncaughtExceptionHandler((Thread t, Throwable e) -> {
            if (Thread.currentThread().getId() == 1)
                printErrorMessage(e);
        });

        setContentPane(rootPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        pack();
        setMinimumSize(getSize());
        setMaximumSize(getSize());
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

        generateButton.addActionListener((ActionEvent e) -> triggerSudokuGen());

        solveButton.addActionListener((ActionEvent e) -> solveSudoku());

        solveAllButton.addActionListener((ActionEvent e) -> solveAllSudoku());

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

        createTerminalPatternButton.addActionListener((ActionEvent e) -> createTerminalPattern());
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println(evt.toString());
    }

    private void triggerSudokuGen() {
        Thread thread = new Thread(() -> {
            try {
                sudoku = new GUISudoku(StaticGenerator.GenerateSudoku((Level)difficultyCombo.getSelectedItem()));
                GeneratingDialog.externalCancel();
            } catch (ValueFormatException exception) {
                printErrorMessage(exception);
            }
        });
        new GeneratingDialog(thread);
        sudokuUpdate();
    }

    private void solveSudoku() {
        Thread thread = new Thread(() -> {
            if (sudoku.freeFields() == 0) {
                JOptionPane.showMessageDialog(rootPanel, "The sudoku has no free fields!", "Nothing to solve", JOptionPane.INFORMATION_MESSAGE);
            } else {
                try {
                    long start = System.currentTimeMillis();
                    SudokuSolver.DFSLV(new CompSudoku(sudoku), System.currentTimeMillis() * 10, 0, 1);
                    long end = System.currentTimeMillis();

                    Sudoku su = SudokuSolver.getLastField();
                    if (su != null) {
                        sudoku = new GUISudoku(su);
                        JOptionPane.showMessageDialog(rootPanel, "Possible solution generated successfully.\n"+
                                        "The algorithm needed: "+ formatTime(end-start),
                                "Number of solutions", JOptionPane.INFORMATION_MESSAGE);
                    } else
                        JOptionPane.showMessageDialog(rootPanel, "The sudoku couldn't be solved!", "Sudoku Error", JOptionPane.ERROR_MESSAGE);
                } catch (ValueFormatException exception) {
                    printErrorMessage(exception);
                }
            }
            GeneratingDialog.externalCancel();
        });
        new GeneratingDialog(thread);
        sudokuUpdate();
    }

    private void solveAllSudoku() {
        Thread thread = new Thread(() -> {
            try {
                long start = System.currentTimeMillis();
                int i = SudokuSolver.solutions(sudoku);
                long end = System.currentTimeMillis();
                GeneratingDialog.externalCancel();

                String msg = "The sudoku has exactly: " + i + " solutions.\n";
                if (i<1)
                    msg = "The sudoku has no valid solution.\n";

                JOptionPane.showMessageDialog(rootPanel, msg+
                                "The algorithm needed: "+ formatTime(end-start),
                        "Number of solutions", JOptionPane.INFORMATION_MESSAGE);
            } catch (ValueFormatException e) {
                GeneratingDialog.externalCancel();
                printErrorMessage(e);
            }

        });
        new GeneratingDialog(thread);
    }

    private void createTerminalPattern() {
        Thread thread = new Thread(() -> {
            try {
                long start = System.currentTimeMillis();
                sudoku = new GUISudoku(LasVegasAlgorithm.LasVegas(Level.None));
                long end = System.currentTimeMillis();
                GeneratingDialog.externalCancel();

                JOptionPane.showMessageDialog(rootPanel, "Terminal pattern generated successfully.\n"+
                                "The algorithm needed: "+ formatTime(end-start),
                        "Number of solutions", JOptionPane.INFORMATION_MESSAGE);
            } catch (ValueFormatException e) {
                GeneratingDialog.externalCancel();
                printErrorMessage(e);
            } finally {
                sudokuUpdate();
            }

        });
        new GeneratingDialog(thread);
    }


    private void printErrorMessage(Throwable e) {
        JOptionPane.showMessageDialog(rootPanel, e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
    }

    private String formatTime(long ms) {
        long SSS = ms%1000;
        long hh  = ms/1000;
        long ss  = hh%60;
        hh/=60;
        long mm  = hh%60;
        hh/=60;

        return String.format(Locale.US, "%dh %02dm %02ds %03dms", hh, mm, ss, SSS);
    }

}
