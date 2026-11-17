package fill;

import model.Point;
import model.polygon.Polygon;
import raster.Raster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scanline {

    private final Raster raster;

    public Scanline(Raster raster) {
        this.raster = raster;
    }

    public void fill(Polygon polygon, int fillColor) {
        int size = polygon.getSize();
        if (size < 3) {
            return;
        }

        List<Edge> edges = new ArrayList<>();
        int yMin = Integer.MAX_VALUE;
        int yMax = Integer.MIN_VALUE;

        for (int i = 0; i < size; i++) {
            Point p1 = polygon.getPoint(i);
            Point p2 = polygon.getPoint((i + 1) % size);

            yMin = Math.min(yMin, p1.getY());
            yMax = Math.max(yMax, p1.getY());

            if (p1.getY() != p2.getY()) {
                edges.add(new Edge(p1, p2));
            }
        }

        if (edges.isEmpty()) {
            return;
        }

        int width = raster.getWidth();
        int height = raster.getHeight();
        yMin = Math.max(0, yMin);
        yMax = Math.min(height - 1, yMax);

        List<Integer> intersections = new ArrayList<>();
        for (int y = yMin; y <= yMax; y++) {
            intersections.clear();
            for (Edge edge : edges) {
                if (edge.hasIntersection(y)) {
                    intersections.add(edge.getIntersectionX(y));
                }
            }
            Collections.sort(intersections);

            for (int i = 0; i + 1 < intersections.size(); i += 2) {
                int xStart = Math.max(0, intersections.get(i));
                int xEnd = Math.min(width - 1, intersections.get(i + 1));
                for (int x = xStart; x <= xEnd; x++) {
                    raster.setPixel(x, y, fillColor);
                }
            }
        }
    }

    private static class Edge {
        private final int x1;
        private final int y1;
        private final int x2;
        private final int y2;

        Edge(Point p1, Point p2) {
            if (p1.getY() < p2.getY()) {
                this.x1 = p1.getX();
                this.y1 = p1.getY();
                this.x2 = p2.getX();
                this.y2 = p2.getY();
            } else {
                this.x1 = p2.getX();
                this.y1 = p2.getY();
                this.x2 = p1.getX();
                this.y2 = p1.getY();
            }
        }

        boolean hasIntersection(int y) {
            return y >= y1 && y < y2;
        }

        int getIntersectionX(int y) {
            float t = (float) (y - y1) / (y2 - y1);
            return Math.round(x1 + t * (x2 - x1));
        }
    }
}
