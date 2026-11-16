package controller;

import gui.Panel;
import model.Point;
import model.Line.LineRasterizer;
import model.Line.Line;

import java.awt.Color;
import java.awt.event.*;

public class Controller2D implements KeyListener {
    private final Panel panel;
    private final LineRasterizer lineRasterizer;

    public Controller2D(Panel panel) {
        this.panel = panel;
        this.lineRasterizer = new LineRasterizer(panel.getRaster());

        Line line = new Line(new Point(50, 50), new Point(300, 200));
        lineRasterizer.rasterize(line, Color.RED.getRGB());
        panel.repaint();

        panel.addKeyListener(this);
        panel.setFocusable(true);
        panel.requestFocusInWindow();
    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_C) {
            panel.getRaster().clear();
            panel.repaint();
        }
    }

    public void keyTyped(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {}
}
