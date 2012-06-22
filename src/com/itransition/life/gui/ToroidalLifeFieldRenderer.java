package com.itransition.life.gui;

import com.itransition.life.core.DigestableToroidalLifeField;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Canvas on which life field is rendered.
 * @see DigestableToroidalLifeField
 */
public class ToroidalLifeFieldRenderer extends JPanel implements MouseListener {
    private static final Log LOGGER = LogFactory.getLog(ToroidalLifeFieldRenderer.class);
    private static final Color ALIVE_COLOR = Color.GREEN;
    private static final Color DEAD_COLOR = Color.GRAY;
    private static final Color LINE_COLOR = Color.BLACK;
    private boolean isLifeFieldLocked = false;
    private DigestableToroidalLifeField lifeField;
    private Graphics2D canvas;
    private int numberOfAliveCells;

    /**
     * Create new renderer for a life field.
     * @param lifeField life field to render.
     */
    public ToroidalLifeFieldRenderer(DigestableToroidalLifeField lifeField) {
        this.lifeField = lifeField;
        this.addMouseListener(this);
    }

    /**
     * Get number of alive cells (current population).
     * @return number of alive cells on the field.
     */
    public int getNumberOfAliveCells() {
        return numberOfAliveCells;
    }

    /**
     * Overriding this method is a standard way to create
     * you own canvas with black jack and hookers.
     * First of all component is filled with the color of dead cells.
     * Then alive cells are rendered.
     * And finally lines are drawn.
     * @see JPanel#paintComponent(java.awt.Graphics).
     */
    @Override
    protected void paintComponent(Graphics g) {
        setBackground(DEAD_COLOR);
        super.paintComponent(g);
        canvas = (Graphics2D)g;
        paintAliveCells();
        paintLines();
        LOGGER.info("painted life field.");
    }

    private void paintLines() {
        canvas.setColor(LINE_COLOR);
        paintHorizontalLines();
        paintVerticalLines();
        LOGGER.info("painted lines.");
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
        numberOfAliveCells = 0;
        for (int x = 0; x < lifeField.getWidth(); x++) {
            for (int y = 0; y < lifeField.getHeight(); y++) {
                if (lifeField.isAlive(x, y)) {
                    paintAliveCell(x, y);
                    numberOfAliveCells++;
                }
            }
        }
        LOGGER.info("painted alive cells.");
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

    /**
     * When user clicks a mouse the cell should change its state.
     * @param e mouse event. contains pointer coordinates used to calculate cell to change.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (!(lifeField instanceof DigestableToroidalLifeField)) {
            return;
        }
        DigestableToroidalLifeField editableLifeField = (DigestableToroidalLifeField) lifeField;
        Point cursor = e.getPoint();
        int x = convertCanvasXToFieldX(cursor.x);
        int y = convertCanvasYToFieldY(cursor.y);
        boolean currentState = lifeField.isAlive(x, y);
        editableLifeField.setState(x, y, !currentState);
        repaint();
    }

    /**
     * Does nothing.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        // Do nothing.
    }

    /**
     * Does nothing.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        // Do nothing.
    }

    /**
     * Does nothing.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        // Do nothing.
    }

    /**
     * Does nothing.
     */
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
