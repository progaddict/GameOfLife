package com.itransition.life.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Observable;

/**
 * Life field implementation in which array is used to store the state.
 * It extends Observable and therefore informs its registered observers
 * that its state has changed (after setAlive and setDead operations).
 */
public class ArrayLifeField extends Observable implements ToroidalLifeField, Digestable {
    private static final Log logger = LogFactory.getLog(ArrayLifeField.class);
    private static MessageDigest md;
    private static final byte ALIVE = 1;
    private static final byte DEAD = 0;
    private final int width;
    private final int height;
    private final byte[] field;
    private int numberOfAliveCells;

    static {
        initializeStaticFields();
    }

    private static void initializeStaticFields() {
        try {
            md = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException trouble) {
            // Do nothing because there is SHA256 algorithm.
        }
    }

    /**
     * Create new life field of the specified size.
     * Size of the field is immutable, but state of the cells can be changed.
     * @param width width of the field in cells.
     * @param height height of the field in cells.
     */
    public ArrayLifeField(int width, int height) {
        if (width <= 0) {
            String errorMessage = "wrong width! width = " + width + ".";
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        if (height <= 0) {
            String errorMessage = "wrong height! height = " + height + ".";
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        this.width = width;
        this.height = height;
        this.field = new byte[width*height];
        this.numberOfAliveCells = 0;
    }

    @Override
    public byte[] getDigest() {
        return md.digest(field);
    }

    @Override
    public boolean isAlive(int x, int y) {
        checkIfCellIsWithinBounds(x,y);
        int linearIndex = getLinearIndex(x,y);
        return field[linearIndex] == ALIVE;
    }

    @Override
    public void setAlive(int x, int y) {
        checkIfCellIsWithinBounds(x,y);
        int linearIndex = getLinearIndex(x,y);
        if (field[linearIndex] == ALIVE) {
            logger.info("cell (" + x + ";" + y + ") is already alive.");
            return;
        }
        field[linearIndex] = ALIVE;
        numberOfAliveCells++;
        notifyObservers();
        logger.info("cell (" + x + ";" + y + ") is now alive. notified observers.");
    }

    @Override
    public void setDead(int x, int y) {
        checkIfCellIsWithinBounds(x,y);
        int linearIndex = getLinearIndex(x,y);
        if (field[linearIndex] == DEAD) {
            logger.info("cell (" + x + ";" + y + ") is already dead.");
            return;
        }
        field[linearIndex] = DEAD;
        numberOfAliveCells--;
        notifyObservers();
        logger.info("cell (" + x + ";" + y + ") is now dead. notified observers.");
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void nextGeneration() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private int getLinearIndex(int x, int y) {
        return y*getWidth() + x;
    }

    private void checkIfCellIsWithinBounds(int x, int y) {
        if(!isCellWithinBounds(x, y)) {
            String errorMessage = "wrong cell coordinates: x = " + x + "   y = " + y;
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private boolean isCellWithinBounds(int x, int y) {
        return isXWithinBounds(x) && isYWithinBounds(y);
    }

    private boolean isXWithinBounds(int x) {
        return 0 <= x && x < getWidth();
    }

    private boolean isYWithinBounds(int y) {
        return 0 <= y && y < getHeight();
    }
}
