package controller;

import gui.Panel;
import gui.Tool;
import model.Point;
import model.line.Line;
import model.line.LineRasterizer;
import model.polygon.Polygon;
import model.polygon.PolygonRasterizer;
import clip.CohenSutherland;
import clip.SutherlandHodgman;
import fill.Scanline;
import fill.Seedfilter;

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
    private boolean scanlineEnabled = false;
    private boolean clipEnabled = false;
    private final Seedfilter seed;
    private final Scanline scanline;
    private final CohenSutherland clipper;
    private final Polygon clipPolygon;
    private final SutherlandHodgman polygonClipper;

    public Controller2D(Panel panel, Tool tool) {
        this.panel = panel;
        this.seed = new Seedfilter(panel.getRaster());
        this.scanline = new Scanline(panel.getRaster());
        this.clipper = new CohenSutherland(150, 100, 650, 500);
        this.clipPolygon = createClipPolygon();
        this.polygonClipper = new SutherlandHodgman(this.clipPolygon);
        this.lineRasterizer = new LineRasterizer(panel.getRaster());
        this.polygonRasterizer = new PolygonRasterizer(this.lineRasterizer);

        tool.getBtnLine().addActionListener(e -> mode = Mode.LINE);
        tool.getBtnPolygon().addActionListener(e -> mode = Mode.POLYGON);
        tool.getBtnScanline().addActionListener(e -> {
            scanlineEnabled = !scanlineEnabled;
            drawScene();
            panel.repaint();
        });
        tool.getBtnClip().addActionListener(e -> {
            clipEnabled = !clipEnabled;
            drawScene();
            panel.repaint();
        });
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
        actualP = null;
        mode = Mode.LINE;
        scanlineEnabled = false;
        clipEnabled = false;
        panel.getRaster().clear();
        panel.repaint();
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {}

    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            if (e.isShiftDown()) {
                seed.fillByBoundary(e.getX(), e.getY(), Color.BLUE.getRGB(), Color.RED.getRGB());
            } else {
                seed.fill(e.getX(), e.getY(), Color.BLUE.getRGB());
            }
            panel.repaint();
            return;
        }
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
        Polygon activePolygon = clipEnabled ? polygonClipper.clip(polygon) : polygon;

        if (scanlineEnabled) {
            scanline.fill(activePolygon, Color.GREEN.getRGB());
        }
        if (clipEnabled) {
            drawClipWindow();
            polygonRasterizer.rasterize(clipPolygon, Color.CYAN.getRGB());
        }
        for (Line line : lines) {
            Line toDraw = clipEnabled ? clipper.clip(line) : line;
            if (toDraw != null) {
                lineRasterizer.rasterize(toDraw, Color.RED.getRGB());
            }
        }
        polygonRasterizer.rasterize(activePolygon, Color.RED.getRGB());
    }

    private Polygon createClipPolygon() {
        Polygon p = new Polygon();
        p.addPoint(new Point(400, 80));
        p.addPoint(new Point(650, 230));
        p.addPoint(new Point(560, 500));
        p.addPoint(new Point(240, 500));
        p.addPoint(new Point(150, 230));
        return p;
    }

    private void drawClipWindow() {
        Point tl = new Point(clipper.getXMin(), clipper.getYMin());
        Point tr = new Point(clipper.getXMax(), clipper.getYMin());
        Point br = new Point(clipper.getXMax(), clipper.getYMax());
        Point bl = new Point(clipper.getXMin(), clipper.getYMax());
        int color = Color.YELLOW.getRGB();
        lineRasterizer.rasterize(new Line(tl, tr), color);
        lineRasterizer.rasterize(new Line(tr, br), color);
        lineRasterizer.rasterize(new Line(br, bl), color);
        lineRasterizer.rasterize(new Line(bl, tl), color);
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
