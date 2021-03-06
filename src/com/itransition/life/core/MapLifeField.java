package com.itransition.life.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.*;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Life field implementation in which map is used to store alive cells.
 */
public class MapLifeField implements DigestableToroidalLifeField {
    /**
     * Minimal width of the field in cells.
     */
    public static final int MINIMAL_WIDTH = 3;
    /**
     * Minimal height of the field in cells.
     */
    public static final int MINIMAL_HEIGHT = 3;
    private static final Log LOGGER = LogFactory.getLog(MapLifeField.class);
    private static final String HASHING_ALGORITHM = "SHA-256";
    private static final Comparator<Point> POINT_COMPARATOR = new Comparator<Point>() {
        @Override
        public int compare(Point a, Point b) {
            if (a.x < b.x) {
                return -1;
            }
            if (a.x > b.x) {
                return +1;
            }
            if (a.y < b.y) {
                return -1;
            }
            if (a.y > b.y) {
                return +1;
            }
            return 0;
        }
    };
    private static MessageDigest md;
    private final int width;
    private final int height;
    private Set<Point> aliveCells;
    private Set<Point> survivedAndNewCells;

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
     * Width should be at least MINIMAL_WIDTH and height should be at least MINIMAL_HEIGHT.
     * @param width width of the field in cells.
     * @param height height of the field in cells.
     * @throws IllegalAccessException if either width or height is too small.
     */
    public MapLifeField(int width, int height) {
        if (width < MINIMAL_WIDTH || height < MINIMAL_HEIGHT) {
            String errorMessage = "wrong field size! width = " + width + "   height = " + height
                    + ". minimal width is " + MINIMAL_WIDTH
                    + " and minimal height is " + MINIMAL_HEIGHT + ".";
            LOGGER.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        this.width = width;
        this.height = height;
        this.aliveCells = new HashSet<Point>(width * height / 2);
    }

    /**
     * @see com.itransition.life.core.DigestableToroidalLifeField#getDigest().
     */
    @Override
    public byte[] getDigest() {
        if (aliveCells.size() == 0) {
            return md.digest(new byte[] {0});
        }
        return md.digest(getByteRepresentation());
    }

    private byte[] getByteRepresentation() {
        Point[] aliveCellsArray = aliveCells.toArray(new Point[0]);
        Arrays.sort(aliveCellsArray, POINT_COMPARATOR);
        byte[] coordinatesStackedTogether = new byte[2 * aliveCells.size()];
        int coordinateIndex = 0;
        for (Point point : aliveCellsArray) {
            coordinatesStackedTogether[coordinateIndex] = (byte)point.x;
            coordinateIndex++;
            coordinatesStackedTogether[coordinateIndex] = (byte)point.y;
            coordinateIndex++;
        }
        LOGGER.info("calculated byte representation: " + Arrays.toString(coordinatesStackedTogether));
        return coordinatesStackedTogether;
    }

    /**
     * @see DigestableToroidalLifeField#isAlive(int, int).
     */
    @Override
    public boolean isAlive(int x, int y) {
        return aliveCells.contains(new Point(x, y));
    }

    private boolean isAlive(Point point) {
        return aliveCells.contains(point);
    }

    private int getNumberOfAliveCells() {
        return aliveCells.size();
    }

    /**
     * @see DigestableToroidalLifeField#setState(int, int, boolean).
     */
    @Override
    public void setState(int x, int y, boolean state) {
        Point point = new Point(x, y);
        if (state) {
            aliveCells.add(point);
            return;
        }
        aliveCells.remove(point);
    }

    /**
     * @see com.itransition.life.core.DigestableToroidalLifeField#getWidth().
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * @see com.itransition.life.core.DigestableToroidalLifeField#getHeight().
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * @see com.itransition.life.core.DigestableToroidalLifeField#nextGeneration().
     */
    @Override
    public void nextGeneration() {
        if (getNumberOfAliveCells() == 0) {
            LOGGER.info("tried to compute next generation but no cells are alive!");
            return;
        }
        survivedAndNewCells = new HashSet<Point>(getWidth() * getHeight() / 2);
        selectSurvivedCells();
        selectNewBornCells();
        aliveCells = survivedAndNewCells;
    }

    private void selectSurvivedCells() {
        Iterator<Point> iterator = aliveCells.iterator();
        while (iterator.hasNext()) {
            Point point = iterator.next();
            int numberOfAliveNeighbours = getNumberOfAliveNeighbours(point);
            if (numberOfAliveNeighbours == 2 || numberOfAliveNeighbours == 3) {
                survivedAndNewCells.add(point);
            }
        }
    }

    private void selectNewBornCells() {
        Iterator<Point> iterator = aliveCells.iterator();
        while (iterator.hasNext()) {
            isNewBorn(iterator.next());
        }
    }

    private void isNewBorn(Point point) {
        List<Point> neighbours = getNeighbouringCells(point);
        Iterator<Point> iterator = neighbours.iterator();
        while (iterator.hasNext()) {
            Point neighbour = iterator.next();
            if (!isAlive(neighbour) && getNumberOfAliveNeighbours(neighbour) == 3) {
                survivedAndNewCells.add(neighbour);
            }
        }
    }

    private int getNumberOfAliveNeighbours(Point point) {
        final int[] MOVE_X = { -1, -1, -1, +0, +0, +1, +1, +1 };
        final int[] MOVE_Y = { -1, +0, +1, -1, +1, -1, +0, +1 };
        int aliveNeighboursCount = 0;
        List<Point> neighbours = getNeighbouringCells(point);
        Iterator<Point> iterator = neighbours.iterator();
        while (iterator.hasNext()) {
            if (isAlive(iterator.next())) {
                aliveNeighboursCount++;
            }
        }
        return aliveNeighboursCount;
    }

    private List<Point> getNeighbouringCells(Point point) {
        final int[] MOVE_X = { -1, -1, -1, +0, +0, +1, +1, +1 };
        final int[] MOVE_Y = { -1, +0, +1, -1, +1, -1, +0, +1 };
        List<Point> neighbours = new ArrayList<Point>(MOVE_X.length + 2);
        for (int move = 0; move < MOVE_X.length; move++) {
            int neighbourX = (getWidth() + point.x + MOVE_X[move]) % getWidth();
            int neighbourY = (getHeight() + point.y + MOVE_Y[move]) % getHeight();
            neighbours.add(new Point(neighbourX, neighbourY));
        }
        return neighbours;
    }
}
