package com.itransition.life.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class encapsulates game of life logic: life generation and cycle detection.
 * It also wraps around DigestableToroidalLifeField by implementing its interface.
 * Life generation is done in a separate thread.
 * @see DigestableToroidalLifeField
 * @see DigestCycleDetector
 */
public class LifeGameController implements DigestableToroidalLifeField {
    private static final Log LOGGER = LogFactory.getLog(LifeGameController.class);
    private static final long MAXIMAL_NUMBER_OF_GENERATIONS = 1234567890l;
    private GameState gameState = GameState.UNDEFINED;
    private DigestableToroidalLifeField lifeField = null;
    private StackCycleDetector cycleDetector = null;
    private long currentGeneration = 0;
    private long cycleLength = 0;
    private Thread gameThread = null;

    private enum GameState {
        UNDEFINED,
        UPDATED,
        RUNNING,
        PAUSED,
        STOPPED
    };

    public long getCurrentGeneration() {
        return currentGeneration;
    }
    
    public long getCycleLength() {
        return cycleLength;
    }

    /**
     * @see DigestableToroidalLifeField#isAlive(int, int).
     */
    @Override
    public boolean isAlive(int x, int y) {
        return lifeField.isAlive(x, y);
    }

    /**
     * @see com.itransition.life.core.DigestableToroidalLifeField#getWidth().
     */
    @Override
    public int getWidth() {
        return lifeField.getWidth();
    }

    /**
     * @see com.itransition.life.core.DigestableToroidalLifeField#getHeight().
     */
    @Override
    public int getHeight() {
        return lifeField.getHeight();
    }

    /**
     * Synchronizes on internal DigestableToroidalLifeField and sets its state.
     * Does nothing if no game was set up or some game is running.
     * @see DigestableToroidalLifeField#setState(int, int, boolean).
     */
    @Override
    public void setState(int x, int y, boolean state) {
        if (getGameState() == GameState.UNDEFINED || getGameState() == GameState.RUNNING) {
            LOGGER.info("didn't set new state to a cell " + x + " " + y + ". state is " + getGameState());
            return;
        }
        synchronized (lifeField) {
            lifeField.setState(x, y, state);
        }
        resetFields();
        setGameState(GameState.UPDATED);
        LOGGER.info("set new state to a cell " + x + " " + y + ". state is " + getGameState());
    }

    /**
     * Does nothing. Use startOrResumeGame() instead.
     */
    @Override
    public void nextGeneration() {
        // Do nothing.
    }

    /**
     * Does nothing. Returns dummy digest.
     * @return dummy digest as new byte[0].
     */
    @Override
    public byte[] getDigest() {
        return new byte[0];
    }

    /**
     * Create new game of the specified width and height.
     * If some game is running it is stopped and reset.
     */
    public void createNewGame(int width, int height) {
        pauseGame();
        lifeField = new MapLifeField(width, height);
        resetFields();
        setGameState(GameState.UPDATED);
    }

    /**
     * Pause running game.
     * Does nothing if game isn't running.
     */
    public void pauseGame() {
        if (getGameState() == GameState.RUNNING) {
            setGameState(GameState.PAUSED);
        }
    }

    /**
     * Start updated game or resume paused game.
     * Game is started/resumed in a separate thread.
     */
    public void startOrResumeGame() {
        switch (getGameState()) {
            case PAUSED: {
                setGameState(GameState.RUNNING);
                resumeGame();
            }
            case UPDATED: {
                setGameState(GameState.RUNNING);
                startGame();
            }
        }
    }

    private void startGame() {
        gameThread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        startGenerating();
                    }
                }
        );
        gameThread.start();
    }

    private void resumeGame() {
        gameThread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        resumeGenerating();
                    }
                }
        );
        gameThread.start();
    }

    private void startGenerating() {
        cycleDetector = new StackCycleDetector();
        byte[] digest;
        synchronized (lifeField) {
            digest = lifeField.getDigest();
        }
        cycleDetector.addDigest(digest);
        currentGeneration = 0;
        generateStates();
    }

    private void resumeGenerating() {
        generateStates();
    }

    private void generateStates() {
        while (getGameState() == GameState.RUNNING && currentGeneration <= MAXIMAL_NUMBER_OF_GENERATIONS) {
            byte[] digest;
            synchronized (lifeField) {
                currentGeneration++;
                lifeField.nextGeneration();
                digest = lifeField.getDigest();
            }
            LOGGER.info("created new generation.");
            cycleDetector.addDigest(digest);
            cycleLength = cycleDetector.getCycleLength();
            if (cycleLength > 0) {
                setGameState(GameState.STOPPED);
                LOGGER.info("calculated cycle length = " + cycleLength);
                return;
            }
        }
        LOGGER.info("failed to find cycle length. maximal number of generations was reached: "
                + MAXIMAL_NUMBER_OF_GENERATIONS);
        setGameState(GameState.STOPPED);
    }

    private GameState getGameState() {
        return gameState;
    }

    private void setGameState(GameState newState) {
        gameState = newState;
    }

    private void resetFields() {
        gameThread = null;
        cycleDetector = null;
        currentGeneration = 0;
        cycleLength = 0;
    }

}
