package model.polygon;

import java.util.ArrayList;
import java.util.List;

import model.Point;

public class Polygon {
    private final List<Point> points = new ArrayList<>();

    public void addPoint(Point p) {
        points.add(p);
    }

    public int getSize() {
        return points.size();
    }

    public void removePoint(Point p) {
        points.remove(p);
    }

    public Point getPoint(int index) {
        return points.get(index);
    }

    public void clear() {
        points.clear();
    }

}
