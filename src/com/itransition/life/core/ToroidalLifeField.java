package com.itransition.life.core;

/**
 * Interface of the toroidal field, i.e. the bottom of the field is linked with
 * its top and its left side is linked with its right side.
 */
public interface ToroidalLifeField {
    /**
     * Test whether the cell (x;y) has life in it.
     * Coordinate limits:
     * 0 <= x < width of the field
     * 0 <= y < height of the field
     * Throws java.lang.IllegalArgumentException if either x or y is out of limits.
     * Returns true if the cell is alive and false otherwise.
     */
    public boolean isAlive(int x, int y);

    /**
     * Get number of cells that are still alive.
     * If there is no alive cells zero will be returned.
     * @return number of alive cells.
     */
    public int getNumberOfAliveCells();

    /**
     * Set state to the cell (x;y).
     * Coordinate limits:
     * 0 <= x < width of the field
     * 0 <= y < height of the field
     * Should throw java.lang.IllegalArgumentException if either x or y is out of limits.
     * @param state true for alive cells and false for dead ones.
     */
    public void setState(int x, int y, boolean state);

    /**
     * Get the field's width in cells.
     */
    public int getWidth();

    /**
     * Get the field's height in cells.
     */
    public int getHeight();

    /**
     * Transform to the next state according to the original Conway's rules.
     * @see{http://en.wikipedia.org/wiki/Conway%27s_Game_of_Life#Rules}
     */
    public void nextGeneration();
}
