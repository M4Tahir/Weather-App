package com.main;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundPanels extends JPanel {
    private int arcWidth, archHeight;
    private Color color1, color2;

    RoundPanels(int arcWidth, int archHeight, Color color1, Color color2) {
        this.arcWidth = arcWidth;
        this.archHeight = archHeight;
        this.color1 = color1;
        this.color2 = color2;
        this.setOpaque(false); // Make the panel transparent so that custom painting is visible
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int height = getHeight();
        int width = getWidth();  // Yes, in the RoundPanels class, the getWidth() and getHeight() methods are used to retrieve the dimensions of the panel where the RoundPanels object is added (in your MyFrame class). These methods return the actual width and height of the panel at runtime.
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
        g2d.setPaint(gp);
        RoundRectangle2D roundRectangle2D = new RoundRectangle2D.Double(0, 0, width, height, arcWidth, archHeight); // create rect with the precision of double data type  value
        /*
        The meaning of the double
        The RoundRectangle2D class in Java's AWT (Abstract Window Toolkit) and Swing library provides a way to represent a rounded rectangle shape. It's a geometric shape that can have rounded corners,
        and it's specified by its x and y coordinates, width, height, and the radii of the corners (arcWidth and arcHeight). These coordinates and dimensions are specified using double precision values
        to allow for high precision and smoother rendering of graphics.
         */
        g2d.fill(roundRectangle2D);
    }
}
