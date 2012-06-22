package com.itransition.life.gui;

import com.itransition.life.core.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main form of the application.
 */
public class MainForm implements ActionListener {
    private static final Log LOGGER = LogFactory.getLog(MainForm.class);
    private static final int MINIMAL_FIELD_WIDTH = 5;
    private static final int MINIMAL_FIELD_HEIGHT = 5;
    private static final int DEFAULT_FIELD_WIDTH = 20;
    private static final int DEFAULT_FIELD_HEIGHT = 20;
    private static final int REPAINT_TIMER_DELAY_SEC = 1;
    private JPanel mainPanel;
    private JPanel interactionPanel;
    private JPanel createPanel;
    private JPanel controlsPanel;
    private JPanel informationPanel;
    private JButton clearButton;
    private JButton goButton;
    private JButton pauseButton;
    private JLabel generationTextLabel;
    private JLabel populationTextLabel;
    private JLabel periodTextLabel;
    private JLabel periodLabel;
    private JLabel populationLabel;
    private JLabel generationLabel;
    private JButton createButton;
    private JTextField widthTextField;
    private JTextField heightTextField;
    private JLabel widthLabel;
    private JLabel heightLabel;
    private JPanel lifeGameFieldPanel;
    private JPanel lifeFieldRenderer;
    private Timer timer = new Timer(REPAINT_TIMER_DELAY_SEC*1000, this);
    private LifeGameController gameController;

    /**
     * ActionListeners are attached here.
     */
    public MainForm() {
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewField();
            }
        });
        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseGame();
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearField();
            }
        });
        LOGGER.info("main form has been constructed.");
    }

    private void clearField() {
        final int WIDTH = gameController.getWidth();
        final int HEIGHT = gameController.getHeight();
        gameController.pauseGame();
        gameController.createNewGame(WIDTH, HEIGHT);
        lifeFieldRenderer.repaint();
        generationLabel.setText("");
        periodLabel.setText("");
        populationLabel.setText("");
    }

    private void pauseGame() {
        gameController.pauseGame();
        timer.stop();
        pauseButton.setEnabled(false);
    }

    private void startGame() {
        gameController.startOrResumeGame();
        timer.start();
        pauseButton.setEnabled(true);
    }

    /**
     * Entry point of the application.
     * Creates GUI and shows it.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        createAndShowGui();
                    }
                }
        );
    }

    private static void createAndShowGui() {
        JFrame frame = new JFrame("Game Of Life");
        frame.setContentPane(new MainForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        gameController = new LifeGameController();
        gameController.createNewGame(DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT);
        lifeFieldRenderer = new ToroidalLifeFieldRenderer(gameController);
    }

    private void createNewField() {
        gameController.pauseGame();
        timer.stop();
        int width = getUsefSelectedFieldWidthOrCorrentIt();
        int height = getUsefSelectedFieldHeightOrCorrentIt();
        gameController.createNewGame(width, height);
        lifeFieldRenderer.repaint();
        generationLabel.setText("");
        periodLabel.setText("");
        populationLabel.setText("");
    }

    private int getUsefSelectedFieldHeightOrCorrentIt() {
        int height = DEFAULT_FIELD_HEIGHT;
        try {
            height = Integer.parseInt(heightTextField.getText());
            if (height < MINIMAL_FIELD_HEIGHT) {
                height = MINIMAL_FIELD_HEIGHT;
            }
        }
        catch (NumberFormatException trouble) {
            if (gameController != null) {
                height = gameController.getHeight();
            }
        }
        heightTextField.setText(Integer.toString(height));
        return height;
    }

    private int getUsefSelectedFieldWidthOrCorrentIt() {
        int width = DEFAULT_FIELD_WIDTH;
        try {
            width = Integer.parseInt(widthTextField.getText());
            if (width < MINIMAL_FIELD_WIDTH) {
                width = MINIMAL_FIELD_WIDTH;
            }
        }
        catch (NumberFormatException trouble) {
            if (gameController != null) {
                width = gameController.getWidth();
            }
        }
        widthTextField.setText(Integer.toString(width));
        return width;
    }

    /**
     * When timer goes off life field is repainted here.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        lifeFieldRenderer.repaint();
        periodLabel.setText(getCycleLength());
        generationLabel.setText(getCurrentGeneration());
        populationLabel.setText(getCurrentPopulation());
    }

    private String getCurrentPopulation() {
        return Integer.toString(((ToroidalLifeFieldRenderer)lifeFieldRenderer).getNumberOfAliveCells());
    }

    private String getCurrentGeneration() {
        return Long.toString(gameController.getCurrentGeneration());
    }

    private String getCycleLength() {
        long cycleLength = gameController.getCycleLength();
        if (cycleLength == 0) {
            return "";
        }
        timer.stop();
        return Long.toString(cycleLength);
    }
}
