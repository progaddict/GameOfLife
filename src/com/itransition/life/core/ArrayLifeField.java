package com.itransition.life.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Observable;
import java.util.Properties;

/**
 * Life field implementation in which array is used to store the state.
 * It informs its registered observers that its state has changed
 * (after setState operation).
 * @see{java.util.Observer}.
 */
public class ArrayLifeField extends Observable implements ToroidalLifeField, Digestable {
    private static final Log LOGGER = LogFactory.getLog(ArrayLifeField.class);
    private static final byte ALIVE = 1;
    private static final byte DEAD = 0;
    private static final String HASHING_ALGORITHM = "SHA-256";
    private static MessageDigest md;
    private final int width;
    private final int height;
    private final byte[] field;
    private int numberOfAliveCells;

    static {
        initializeStaticFields();
    }

    private static void initializeStaticFields() {
        try {
            md = MessageDigest.getInstance(HASHING_ALGORITHM);
        }
        catch (NoSuchAlgorithmException trouble) {
            LOGGER.error("can't find hashing algorithm " + HASHING_ALGORITHM, trouble);
            // Do nothing: algorithm should exist.
        }
    }

    /**
     * Create new life field of the specified size.
     * Size of the field is immutable, but state of the cells can be changed.
     * @param width width of the field in cells.
     * @param height height of the field in cells.
     */
    public ArrayLifeField(int width, int height) {
        if (width <= 0 || height <= 0) {
            String errorMessage = "wrong field size! width = " + width + "   height = " + height + ".";
            LOGGER.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        this.width = width;
        this.height = height;
        this.field = new byte[width * height];
        this.numberOfAliveCells = 0;
    }

    @Override
    public byte[] getDigest() {
        return md.digest(field);
    }

    @Override
    public boolean isAlive(int x, int y) {
        int linearIndex = getLinearIndex(x, y);
        return isAlive(linearIndex);
    }

    private boolean isAlive(int linearIndex) {
        return field[linearIndex] == ALIVE;
    }

    @Override
    public int getNumberOfAliveCells() {
        return numberOfAliveCells;
    }

    @Override
    public void setState(int x, int y, boolean state) {
        final byte NEW_STATE = state ? ALIVE : DEAD;
        int linearIndex = getLinearIndex(x, y);
        if (field[linearIndex] == NEW_STATE) {
            LOGGER.info("cell (" + x + ";" + y + ") is already in the right state.");
            return;
        }
        field[linearIndex] = NEW_STATE;
        if (state) {
            numberOfAliveCells++;
        }
        else {
            numberOfAliveCells--;
        }
        Properties changes = getOneCellChanges(x, y);
        notifyObservers(changes);
        LOGGER.info("cell (" + x + ";" + y + ") has changed to new state. notified observers.");
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
        if (getNumberOfAliveCells() == 0) {
            LOGGER.info("tried to compute next generation but no cells are alive!");
            return;
        }
        byte[] neighboursCount = getNeighboursCount();
        numberOfAliveCells = 0;
        for (int i = 0; i < neighboursCount.length; i++) {
            field[i] = getNextState(field[i], neighboursCount[i]);
            if (isAlive(i)) {
                numberOfAliveCells++;
            }
        }
        Properties changes = getWholeFieldChanges();
        notifyObservers(changes);
        LOGGER.info("next generation has been computed. observers were notified.");
    }

    private byte getNextState(byte currentState, byte neighboursCount) {
        switch (currentState) {
            case ALIVE: {
                return handleAliveCell(neighboursCount);
            }
            case DEAD: {
                return handleDeadCell(neighboursCount);
            }
        }
        LOGGER.error("wrong state! current state = " + currentState);
        return DEAD;
    }

    private byte handleAliveCell(int neighboursCount) {
        if(neighboursCount < 2) {
            return DEAD;
        }
        if(neighboursCount == 2 || neighboursCount == 3) {
            return ALIVE;
        }
        return DEAD;
    }

    private byte handleDeadCell(int neighboursCount) {
        if(neighboursCount == 3) {
            return ALIVE;
        }
        return DEAD;
    }

    private byte[] getNeighboursCount() {
        byte[] neighbours = new byte[getWidth() * getHeight()];
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                byte aliveNeighboursCount = getNumberOfAliveNeighbours(x, y);
                int linearIndex = getLinearIndex(x, y);
                neighbours[linearIndex] = aliveNeighboursCount;
            }
        }
        return neighbours;
    }

    private byte getNumberOfAliveNeighbours(int x, int y) {
        final int[] MOVE_X = { -1, -1, -1, +0, +0, +1, +1, +1 };
        final int[] MOVE_Y = { -1, +0, +1, -1, +1, -1, +0, +1 };
        byte aliveNeighboursCount = 0;
        for (int move = 0; move < MOVE_X.length; move++) {
            int neighbourX = (getWidth() + x + MOVE_X[move]) % getWidth();
            int neighbourY = (getHeight() + y + MOVE_Y[move]) % getHeight();
            if (isAlive(neighbourX, neighbourY)) {
                aliveNeighboursCount++;
            }
        }
        return aliveNeighboursCount;
    }

    private int getLinearIndex(int x, int y) {
        return y * getWidth() + x;
    }

    private static Properties getOneCellChanges(int x, int y) {
        Properties changes = new Properties();
        changes.setProperty("whatChanged", "cell");
        changes.setProperty("x", Integer.toString(x));
        changes.setProperty("y", Integer.toString(y));
        return changes;
    }

    private static Properties getWholeFieldChanges() {
        Properties changes = new Properties();
        changes.setProperty("whatChanged", "field");
        return changes;
    }
}
