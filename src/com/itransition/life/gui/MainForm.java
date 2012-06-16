package com.itransition.life.gui;

import javax.swing.*;

public class MainForm {

    private JPanel mainPanel;

    private static void createAndShowGui() {
        JFrame frame = new JFrame("Game Of Life");
        frame.setContentPane(new MainForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

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
}
