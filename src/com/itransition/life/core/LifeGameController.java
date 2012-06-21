package com.itransition.life.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LifeGameController implements DigestableToroidalLifeField {
    private static final Log LOGGER = LogFactory.getLog(LifeGameController.class);
    private static final long MAXIMAL_NUMBER_OF_GENERATIONS = 1234567890l;
    private static final String GAME_THREAD_NAME = "LifeThread";
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

    @Override
    public boolean isAlive(int x, int y) {
        return lifeField.isAlive(x, y);
    }

    @Override
    public int getWidth() {
        return lifeField.getWidth();
    }

    @Override
    public int getHeight() {
        return lifeField.getHeight();
    }

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

    @Override
    public void nextGeneration() {
        // Do nothing.
    }

    @Override
    public byte[] getDigest() {
        // Return dummy digest.
        return new byte[0];
    }

    /**
     * Create new game of the specified width and height.
     */
    public void createNewGame(int width, int height) {
        pauseGame();
        lifeField = new MapLifeField(width, height);
        resetFields();
        setGameState(GameState.UPDATED);
    }

    /**
     * Pause running game.
     */
    public void pauseGame() {
        if (getGameState() == GameState.RUNNING) {
            setGameState(GameState.PAUSED);
        }
    }

    /**
     * Start updated game or resume paused game.
     * Game is started/resumed in a new thread.
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
                },
                GAME_THREAD_NAME
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
                },
                GAME_THREAD_NAME
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
