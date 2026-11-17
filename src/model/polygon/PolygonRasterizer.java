package model.polygon;

import model.Point;
import model.line.Line;
import model.line.LineRasterizer;

public class PolygonRasterizer {
    private final LineRasterizer lineRasterizer;

    public PolygonRasterizer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    public void rasterize(Polygon polygon, int color) {
        int size = polygon.getSize();
        if (size == 0) {
            return;
        }
        if (size == 1) {
            Point p = polygon.getPoint(0);
            lineRasterizer.rasterize(new Line(p, p), color);
            return;
        }
        for (int i = 0; i < size; i++) {
            Point p1 = polygon.getPoint(i);
            Point p2 = polygon.getPoint((i + 1) % size);
            lineRasterizer.rasterize(new Line(p1, p2), color);
        }
    }
}
