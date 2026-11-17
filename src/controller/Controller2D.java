package controller;

import gui.Panel;
import model.Point;
import model.Line.LineRasterizer;
import model.Line.Line;

import java.awt.Color;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Controller2D implements KeyListener, MouseListener, MouseMotionListener {
    private final Panel panel;
    private final LineRasterizer lineRasterizer;
    private Point actualP;
    private final List<Line> lines = new ArrayList<>();


    public Controller2D(Panel panel) {
        this.panel = panel;
        this.lineRasterizer = new LineRasterizer(panel.getRaster());

        Line line = new Line(new Point(50, 50), new Point(300, 200));
        lineRasterizer.rasterize(line, Color.RED.getRGB());
        panel.repaint();

        panel.addKeyListener(this);
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
        panel.setFocusable(true);
        panel.requestFocusInWindow();
    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_C) {
            lines.clear();
            panel.getRaster().clear();
            panel.repaint();
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {}

    public void mousePressed(MouseEvent e) {
        this.actualP = new Point(e.getX(), e.getY());
    }

    public void mouseDragged(MouseEvent e) {
        drawScene();
        Line preview = new Line(this.actualP, getEndPoint(e));
        lineRasterizer.rasterize(preview, Color.RED.getRGB());
        panel.repaint();
    }

    public void mouseReleased(MouseEvent e) {
        lines.add(new Line(this.actualP, getEndPoint(e)));
        drawScene();
        panel.repaint();
    }

    private void drawScene() {
        panel.getRaster().clear();
        for (Line line : lines){
            lineRasterizer.rasterize(line, Color.RED.getRGB());
        }
    }

    private Point getEndPoint(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if (e.isShiftDown()) {
            int dx = x - this.actualP.getX();
            int dy = y - this.actualP.getY();

            int absDx = Math.abs(dx);
            int absDy = Math.abs(dy);

            if (absDy < absDx * 0.5) {
                y = this.actualP.getY();
            }
            else if (absDx < absDy * 0.5) {
                x = this.actualP.getX();
            }
            else {
                int d = Math.max(absDx, absDy);
                if (dx >= 0) {
                    x = this.actualP.getX() + d;
                }
                else {
                    x = this.actualP.getX() - d;
                }
                if (dy >= 0) {
                    y = this.actualP.getY() + d;
                }
                else {
                    y = this.actualP.getY() - d;
                }
            }
        }
        return new Point(x, y);
    }

    public void mouseClicked(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mouseMoved(MouseEvent e) {}
}
