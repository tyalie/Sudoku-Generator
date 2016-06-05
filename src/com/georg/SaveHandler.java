package com.georg;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import de.erichseifert.vectorgraphics2d.PDFGraphics2D;
import de.erichseifert.vectorgraphics2d.VectorGraphics2D;

import java.awt.*;
import java.io.*;

/**
 * Created by Georg on 05/06/16.
 */
public class SaveHandler {
    public static void save(String name, Sudoku sudoku) throws IOException {
        if (name.endsWith(".pdf"))
            savePDF(name, sudoku);
        else
            saveTXT(name+".txt", sudoku);
    }

    private final static int offset = 5;
    private final static int fontW = 5;


    private static void savePDF(String name, Sudoku sudoku) throws IOException{
        PDFGraphics2D mPDF = new PDFGraphics2D(0, 0, Sudoku.FIELD_SIZE*10+offset*2, Sudoku.FIELD_SIZE*10+offset*2);

        mPDF.setColor(new Color(0x90, 0x90, 0x90));
        mPDF.setStroke(new BasicStroke(0.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{2.5f}, 1.25f));
        for (int i = 1; i < Sudoku.FIELD_SIZE; i++) {
            if(i%3==0)
                continue;
            mPDF.drawLine(offset+10*i,offset,offset+10*i,offset+90);
            mPDF.drawLine(offset, offset+10*i, offset+90, offset+10*i);
        }

        mPDF.setColor(new Color(0x00, 0x00, 0x00));
        mPDF.setStroke(new BasicStroke(0.5f));
        for (int i = 1; i < Sudoku.BLOCK_SIZE; i++) {
            mPDF.drawLine(offset+30*i,offset,offset+30*i,offset+90);
            mPDF.drawLine(offset, offset+30*i, offset+90, offset+30*i);
        }

        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        mPDF.setFont(new Font(env.getAllFonts()[0].getFontName(), Font.BOLD, fontW));

        int fOffset = offset+5;


        for (int i = 0; i < Sudoku.FIELD_COUNT; i++) {
            char c = (char)(sudoku.getAtIndex(i)+0x30);
            int cx = mPDF.getFontMetrics().stringWidth(c+"")/-2;
            int cy = fontW/2;

            if (c==(0x30+Sudoku.NAN))
                c = 0x20;
            mPDF.drawChars(new char[]{c}, 0, 1,cx+fOffset+(i%Sudoku.FIELD_SIZE)*10, cy+fOffset+(i/Sudoku.FIELD_SIZE)*10);
        }

        OutputStream stream = IOWriter(name);
        mPDF.writeTo(stream);
        stream.flush();


    }

    private static void saveTXT(String name, Sudoku sudoku) throws IOException {
        OutputStream file = IOWriter(name);
        file.write(sudoku.toString().getBytes());
        file.flush();
        file.close();
    }

    private static OutputStream IOWriter(String file) throws IOException {
        return new FileOutputStream(file, false);
    }
}
