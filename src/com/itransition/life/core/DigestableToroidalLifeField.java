package com.itransition.life.core;

/**
 * Interface of the toroidal life field which can compute digest of its internal state.
 * The bottom of the field is linked with its top and its left side is linked with its right side.
 * Fields with equal states should have the same digests.
 */
public interface DigestableToroidalLifeField {
    /**
     * Test whether the cell (x;y) has life in it.
     * Coordinate limits:
     * 0 <= x < width of the field
     * 0 <= y < height of the field
     * @return true if the cell is alive and false otherwise.
     */
    public boolean isAlive(int x, int y);

    /**
     * Get the field's width in cells.
     * @return width of the field in cells.
     */
    public int getWidth();

    /**
     * Get the field's height in cells.
     * @return height of the field in cells.
     */
    public int getHeight();

    /**
     * Set state to the cell (x;y).
     * Coordinate limits:
     * 0 <= x < width of the field
     * 0 <= y < height of the field
     * @param state true for alive cells and false for dead ones.
     */
    public void setState(int x, int y, boolean state);

    /**
     * Transform to the next state according to the original Conway's rules.
     * @see{http://en.wikipedia.org/wiki/Conway%27s_Game_of_Life#Rules}
     */
    public void nextGeneration();

    /**
     * Compute digest according to the field's internal state. Fields with equal states should have
     * the same digest. Length of the digest depends on the digest algorithm used in computation.
     * Field of the same class should have the same fixed digest length.
     * @return digest of some fixed length (depending on the algorithm).
     */
    public byte[] getDigest();
}
