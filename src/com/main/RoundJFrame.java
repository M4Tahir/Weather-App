package com.main;

import javax.swing.*;
import java.awt.*;

public class RoundJFrame extends JFrame {
    Color color;

    RoundJFrame(Color color) {

        this.color = color;
        this.setUndecorated(true);
        this.getContentPane().setBackground(color);
        this.setLayout(null);
    }

    @Override
    public void paint(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        g2d.fillRoundRect(0, 0, 1280, 720, 20, 20);
        g2d.dispose();
    }
}
