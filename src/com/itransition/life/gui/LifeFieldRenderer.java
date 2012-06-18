package com.itransition.life.gui;

import com.itransition.life.core.ToroidalLifeField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class LifeFieldRenderer extends JPanel implements MouseListener {
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color ALIVE_COLOR = Color.GREEN;
    private static final Color DEAD_COLOR = Color.GRAY;
    private static final Color LINE_COLOR = Color.BLACK;
    private ToroidalLifeField lifeField;

    public LifeFieldRenderer(ToroidalLifeField lifeField) {
        this.lifeField = lifeField;
        this.addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D)g;
        canvas.drawString("a string", 10.0f, 10.0f );
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
