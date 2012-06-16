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
     * Should throw java.lang.IllegalArgumentException
     * if either x or y is out of limits.
     * Should return false if the cell is dead.
     * @return true if cell has life in it.
     */
    public boolean isAlive(int x, int y);

    /**
     * Set cell (x;y) to alive state.
     * Coordinate limits:
     * 0 <= x < width of the field
     * 0 <= y < height of the field
     * Should throw java.lang.IllegalArgumentException
     * if either x or y is out of limits.
     */
    public void setAlive(int x, int y);

    /**
     * Set cell (x;y) to dead state.
     * Coordinate limits:
     * 0 <= x < width of the field
     * 0 <= y < height of the field
     * Should throw java.lang.IllegalArgumentException
     * if either x or y is out of limits.
     */
    public void setDead(int x, int y);

    /**
     * Get the field's width.
     * @return width of the field in cells.
     */
    public int getWidth();

    /**
     * Get the field's height.
     * @return height of the field in cells.
     */
    public int getHeight();

    /**
     * Transform to the next state according to the original Conway's rules.
     * @see http://en.wikipedia.org/wiki/Conway%27s_Game_of_Life#Rules
     */
    public void nextGeneration();
}
