package com.itransition.life.gui;

import com.itransition.life.core.ToroidalLifeField;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Canvas on which life field is rendered.
 */
public class ToroidalLifeFieldRenderer extends JPanel implements MouseListener {
    private static final Log logger = LogFactory.getLog(ToroidalLifeFieldRenderer.class);
    private static final Color ALIVE_COLOR = Color.GREEN;
    private static final Color DEAD_COLOR = Color.GRAY;
    private static final Color LINE_COLOR = Color.BLACK;
    private ToroidalLifeField lifeField;
    private Graphics2D canvas;

    public ToroidalLifeFieldRenderer(ToroidalLifeField lifeField) {
        if( lifeField.getWidth() < 2 || lifeField.getHeight() < 2 ) {
            String errorMessage = "wrong field size! width = " + lifeField.getWidth() + "   height = " + lifeField.getHeight() + ".";
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        this.lifeField = lifeField;
        this.addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        setBackground(DEAD_COLOR);
        super.paintComponent(g);
        canvas = (Graphics2D)g;
        paintAliveCells();
        paintLines();
        logger.info("painted life field.");
    }

    private void paintLines() {
        canvas.setColor(LINE_COLOR);
        paintHorizontalLines();
        paintVerticalLines();
        logger.info("painted lines.");
    }


    private void paintVerticalLines() {
        for (int x = 0; x < lifeField.getWidth(); x++) {
            int x1 = convertFieldXToCanvasX(x);
            int y1 = 0;
            int x2 = x1;
            int y2 = getHeight();
            canvas.drawLine(x1, y1, x2, y2);
        }
    }

    private void paintHorizontalLines() {
        for (int y = 0; y < lifeField.getHeight(); y++) {
            int x1 = 0;
            int y1 = convertFieldYToCanvasY(y);
            int x2 = getWidth();
            int y2 = y1;
            canvas.drawLine(x1, y1, x2, y2);
        }
    }

    private void paintAliveCells() {
        canvas.setColor(ALIVE_COLOR);
        for (int x = 0; x < lifeField.getWidth(); x++) {
            for (int y = 0; y < lifeField.getHeight(); y++) {
                if (lifeField.isAlive(x, y)) {
                    paintAliveCell(x, y);
                }
            }
        }
        logger.info("painted alive cells.");
    }

    private void paintAliveCell(int x, int y) {
        int x1 = convertFieldXToCanvasX(x);
        int y1 = convertFieldYToCanvasY(y);
        int x2 = convertFieldXToCanvasX(x + 1);
        int y2 = convertFieldYToCanvasY(y + 1);
        canvas.fillRect(x1, y1, x2 - x1, y2 - y1);
    }

    private int convertFieldXToCanvasX(int fieldX) {
        double canvasX = ((double) fieldX * this.getWidth()) / lifeField.getWidth();
        return (int) Math.round(canvasX);
    }

    private int convertFieldYToCanvasY(int fieldY) {
        double canvasY = ((double) fieldY * this.getHeight()) / lifeField.getHeight();
        return (int) Math.round(canvasY);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point cursor = e.getPoint();
        int x = convertCanvasXToFieldX(cursor.x);
        int y = convertCanvasYToFieldY(cursor.y);
        boolean currentState = lifeField.isAlive(x, y);
        lifeField.setState(x, y, !currentState);
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Do nothing.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Do nothing.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Do nothing.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Do nothing.
    }

    private int convertCanvasXToFieldX(int canvasX) {
        return (canvasX * lifeField.getWidth()) / getWidth();
    }

    private int convertCanvasYToFieldY(int canvasY) {
        return (canvasY * lifeField.getHeight()) / getHeight();
    }
}
