package controller;

import gui.Panel;
import gui.Tool;
import model.Point;
import model.line.Line;
import model.line.LineRasterizer;
import model.polygon.Polygon;
import model.polygon.PolygonRasterizer;

import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Controller2D implements KeyListener, MouseListener, MouseMotionListener {
    private enum Mode {
        LINE,
        POLYGON
    }

    private final Panel panel;
    private final LineRasterizer lineRasterizer;
    private final PolygonRasterizer polygonRasterizer;
    private Point actualP;
    private final List<Line> lines = new ArrayList<>();
    private final Polygon polygon = new Polygon();
    private Mode mode = Mode.LINE;

    public Controller2D(Panel panel, Tool tool) {
        this.panel = panel;
        this.lineRasterizer = new LineRasterizer(panel.getRaster());
        this.polygonRasterizer = new PolygonRasterizer(this.lineRasterizer);

        tool.getBtnLine().addActionListener(e -> mode = Mode.LINE);
        tool.getBtnPolygon().addActionListener(e -> mode = Mode.POLYGON);
        tool.getBtnClear().addActionListener(e -> clearScene());

        panel.addKeyListener(this);
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
        panel.setFocusable(true);
        panel.requestFocusInWindow();
    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_C) {
            clearScene();
        }
    }

    private void clearScene() {
        lines.clear();
        polygon.clear();
        panel.getRaster().clear();
        panel.repaint();
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {}

    public void mousePressed(MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) {
            return;
        }
        this.actualP = new Point(e.getX(), e.getY());
    }

    public void mouseDragged(MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) {
            return;
        }
        drawScene();
        if (mode == Mode.LINE) {
            Line preview = new Line(this.actualP, getEndPoint(e));
            lineRasterizer.rasterize(preview, Color.RED.getRGB());
        } else {
            Point cur = new Point(e.getX(), e.getY());
            int size = polygon.getSize();
            if (size >= 1) {
                lineRasterizer.rasterize(new Line(polygon.getPoint(0), cur), Color.RED.getRGB());
            }
            if (size >= 2) {
                lineRasterizer.rasterize(new Line(polygon.getPoint(size - 1), cur), Color.RED.getRGB());
            }
        }
        panel.repaint();
    }

    public void mouseReleased(MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) {
            return;
        }
        if (mode == Mode.LINE) {
            lines.add(new Line(this.actualP, getEndPoint(e)));
        } else {
            polygon.addPoint(new Point(e.getX(), e.getY()));
        }
        drawScene();
        panel.repaint();
    }

    private void drawScene() {
        panel.getRaster().clear();
        for (Line line : lines) {
            lineRasterizer.rasterize(line, Color.RED.getRGB());
        }
        polygonRasterizer.rasterize(polygon, Color.RED.getRGB());
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
