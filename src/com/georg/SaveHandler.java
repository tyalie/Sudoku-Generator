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

package com.georg;

import de.erichseifert.vectorgraphics2d.PDFGraphics2D;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.georg.Sudoku.FIELD_SIZE;

/**
 * Created by Georg on 05/06/16.
 * <p>
 * All the saving methods united in
 * one place.
 */
public class SaveHandler {
    /**
     * The offset of the non-text version from
     * the image screen.
     */
    private final static int stdOffset = 5;
    /**
     * The font size.
     */
    private final static int stdFontW = 5;

    /**
     * Main save method.
     *
     * @param name   The name of the file
     * @param sudoku The sudoku
     * @throws IOException
     */
    public static void save(String name, Sudoku sudoku) throws IOException {
        if (name.toLowerCase().endsWith(".pdf"))
            savePDF(name, sudoku);
        else // all non pdf files
            saveTXT(name + (name.toLowerCase().endsWith(".txt") ? "" : ".txt"), sudoku);
    }

    /**
     * Saves the sudoku as a pdf file
     *
     * @param name   The filename
     * @param sudoku The sudoku
     * @throws IOException
     */
    private static void savePDF(String name, Sudoku sudoku) throws IOException {
        PDFGraphics2D mPDF = new PDFGraphics2D(0, 0, getSizeXY(20), getSizeXY(20));
        drawSudoku(mPDF, sudoku, 20);
        OutputStream stream = IOOpen(name);
        mPDF.writeTo(stream);
        IOClose(stream);
    }

    /**
     * Helper method for figuring out the
     * correct image width and height
     * (it's a square) under a specific
     * tileSize.
     *
     * @param tileSize The size of one tile
     * @return The width or height in pixels
     */
    private static int getSizeXY(int tileSize) {
        return FIELD_SIZE * tileSize + (int) (stdOffset / 10f * tileSize) * 2;
    }

    /**
     * General method that allows the generation
     * of all kinds of images, as long as the canvas
     * is a subclass of {@link Graphics2D}.
     *
     * @param graphics2D The canvas
     * @param sudoku     The sudoku
     * @param tileSize   The size in px of one tile
     */
    private static void drawSudoku(Graphics2D graphics2D, Sudoku sudoku, int tileSize) {
        int offset = (int) (stdOffset / 10f * tileSize);
        int fontW = (int) (stdFontW / 10f * tileSize);

        graphics2D.setColor(new Color(0x90, 0x90, 0x90));
        graphics2D.setStroke(new BasicStroke(tileSize / 50f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, tileSize, new float[]{tileSize / 4f}, tileSize / 8f));
        for (int i = 1; i < FIELD_SIZE; i++) {
            if (i % 3 == 0)
                continue;
            graphics2D.drawLine(offset + tileSize * i, offset, offset + tileSize * i, offset + tileSize * FIELD_SIZE);
            graphics2D.drawLine(offset, offset + tileSize * i, offset + tileSize * FIELD_SIZE, offset + tileSize * i);
        }

        graphics2D.setColor(new Color(0x00, 0x00, 0x00));
        graphics2D.setStroke(new BasicStroke(tileSize / 20f));
        for (int i = 1; i < Sudoku.BLOCK_SIZE; i++) {
            graphics2D.drawLine(offset + tileSize * 3 * i, offset, offset + tileSize * 3 * i, offset + tileSize * FIELD_SIZE);
            graphics2D.drawLine(offset, offset + tileSize * 3 * i, offset + tileSize * FIELD_SIZE, offset + tileSize * 3 * i);
        }

        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        graphics2D.setFont(new Font(env.getAllFonts()[0].getFontName(), Font.BOLD, fontW));

        int fOffset = offset + tileSize / 2;


        for (int i = 0; i < Sudoku.FIELD_COUNT; i++) {
            char c = (char) (sudoku.getAtIndex(i) + 0x30);
            int cx = graphics2D.getFontMetrics().stringWidth(c + "") / -2;
            int cy = fontW / 2;

            if (c == (0x30 + Sudoku.NAN))
                c = 0x20;
            graphics2D.drawChars(new char[]{c}, 0, 1, cx + fOffset + (i % FIELD_SIZE) * tileSize, cy + fOffset + (i / FIELD_SIZE) * tileSize);
        }
    }

    /**
     * Save the image as a txt.
     *
     * @param name   The filename
     * @param sudoku The sudoku
     * @throws IOException
     */
    private static void saveTXT(String name, Sudoku sudoku) throws IOException {
        OutputStream file = IOOpen(name);
        // Sudokus toString method is good enough.
        file.write(sudoku.toString().getBytes());
        IOClose(file);
    }

    /**
     * Opens an OutputStream instance.
     *
     * @param file The filename
     * @return The OutputStream to the file
     * @throws IOException
     */
    private static OutputStream IOOpen(String file) throws IOException {
        return new FileOutputStream(file, false);
    }

    private static void IOClose(OutputStream file) throws IOException {
        file.flush();
        file.close();
    }
}
