package com.itransition.life.gui;

import com.itransition.life.core.ArrayLifeField;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainForm {
    private static final Log logger = LogFactory.getLog(MainForm.class);
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

    public MainForm() {
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
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

    private static void createAndShowGui() {
        JFrame frame = new JFrame("Game Of Life");
        frame.setContentPane(new MainForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        ArrayLifeField field = new ArrayLifeField(20,20);
        field.setState(0, 0, true);
        field.setState(0, 5, true);
        field.setState(5, 0, true);
        field.setState(2, 7, true);
        this.lifeFieldRenderer = new ToroidalLifeFieldRenderer( field );
    }
}
