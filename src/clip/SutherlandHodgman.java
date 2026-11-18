package clip;

import model.Point;
import model.polygon.Polygon;

import java.util.ArrayList;
import java.util.List;

public class SutherlandHodgman {

    private final Polygon clipPolygon;
    private final boolean positiveOrientation;

    public SutherlandHodgman(Polygon clipPolygon) {
        this.clipPolygon = clipPolygon;
        this.positiveOrientation = detectOrientation(clipPolygon);
    }

    private boolean detectOrientation(Polygon poly) {
        int size = poly.getSize();
        for (int i = 0; i < size; i++) {
            Point a = poly.getPoint(i);
            Point b = poly.getPoint((i + 1) % size);
            Point c = poly.getPoint((i + 2) % size);
            double cross = crossProduct(a, b, c);
            if (cross != 0) {
                return cross > 0;
            }
        }
        return true;
    }

    private double crossProduct(Point a, Point b, Point p) {
        return (double) (b.getX() - a.getX()) * (p.getY() - a.getY())
                - (double) (b.getY() - a.getY()) * (p.getX() - a.getX());
    }

    private boolean isInside(Point a, Point b, Point p) {
        double cross = crossProduct(a, b, p);
        return positiveOrientation ? (cross >= 0) : (cross <= 0);
    }

    private Point getIntersection(Point a, Point b, Point s, Point e) {
        double dx1 = b.getX() - a.getX();
        double dy1 = b.getY() - a.getY();
        double dx2 = e.getX() - s.getX();
        double dy2 = e.getY() - s.getY();

        double denom = dx1 * dy2 - dy1 * dx2;
        if (denom == 0) {
            return e;
        }

        double t = (dx1 * (a.getY() - s.getY()) - dy1 * (a.getX() - s.getX())) / denom;
        int x = (int) Math.round(s.getX() + t * dx2);
        int y = (int) Math.round(s.getY() + t * dy2);
        return new Point(x, y);
    }

    public Polygon clip(Polygon subject) {
        Polygon result = new Polygon();
        if (subject.getSize() < 3 || clipPolygon.getSize() < 3) {
            return result;
        }

        List<Point> output = new ArrayList<>();
        for (int i = 0; i < subject.getSize(); i++) {
            output.add(subject.getPoint(i));
        }

        int clipSize = clipPolygon.getSize();
        for (int i = 0; i < clipSize; i++) {
            Point a = clipPolygon.getPoint(i);
            Point b = clipPolygon.getPoint((i + 1) % clipSize);

            List<Point> input = new ArrayList<>(output);
            output.clear();
            if (input.isEmpty()) {
                break;
            }

            Point s = input.get(input.size() - 1);
            for (Point e : input) {
                if (isInside(a, b, e)) {
                    if (!isInside(a, b, s)) {
                        output.add(getIntersection(a, b, s, e));
                    }
                    output.add(e);
                } else if (isInside(a, b, s)) {
                    output.add(getIntersection(a, b, s, e));
                }
                s = e;
            }
        }

        for (Point p : output) {
            result.addPoint(p);
        }
        return result;
    }
}
