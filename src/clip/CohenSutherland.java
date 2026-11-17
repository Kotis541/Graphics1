package clip;

import model.Point;
import model.line.Line;

public class CohenSutherland {
    private static final int INSIDE = 0;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int BOTTOM = 4;
    private static final int TOP = 8;

    private final int xMin;
    private final int yMin;
    private final int xMax;
    private final int yMax;

    public CohenSutherland(int x1, int y1, int x2, int y2) {
        this.xMin = Math.min(x1, x2);
        this.yMin = Math.min(y1, y2);
        this.xMax = Math.max(x1, x2);
        this.yMax = Math.max(y1, y2);
    }

    public int getXMin() {
        return xMin;
    }

    public int getYMin() {
        return yMin;
    }

    public int getXMax() {
        return xMax;
    }

    public int getYMax() {
        return yMax;
    }

    private int computeCode(double x, double y) {
        int code = INSIDE;
        if (x < xMin) {
            code |= LEFT;
        } else if (x > xMax) {
            code |= RIGHT;
        }
        if (y < yMin) {
            code |= TOP;
        } else if (y > yMax) {
            code |= BOTTOM;
        }
        return code;
    }

    public Line clip(Line line) {
        double x1 = line.getP1().getX();
        double y1 = line.getP1().getY();
        double x2 = line.getP2().getX();
        double y2 = line.getP2().getY();

        int code1 = computeCode(x1, y1);
        int code2 = computeCode(x2, y2);

        while (true) {
            if ((code1 | code2) == 0) {
                return new Line(
                        new Point((int) Math.round(x1), (int) Math.round(y1)),
                        new Point((int) Math.round(x2), (int) Math.round(y2))
                );
            }
            if ((code1 & code2) != 0) {
                return null;
            }

            int outCode = (code1 != 0) ? code1 : code2;
            double x = 0;
            double y = 0;

            if ((outCode & TOP) != 0) {
                x = x1 + (x2 - x1) * (yMin - y1) / (y2 - y1);
                y = yMin;
            } else if ((outCode & BOTTOM) != 0) {
                x = x1 + (x2 - x1) * (yMax - y1) / (y2 - y1);
                y = yMax;
            } else if ((outCode & RIGHT) != 0) {
                y = y1 + (y2 - y1) * (xMax - x1) / (x2 - x1);
                x = xMax;
            } else if ((outCode & LEFT) != 0) {
                y = y1 + (y2 - y1) * (xMin - x1) / (x2 - x1);
                x = xMin;
            }

            if (outCode == code1) {
                x1 = x;
                y1 = y;
                code1 = computeCode(x1, y1);
            } else {
                x2 = x;
                y2 = y;
                code2 = computeCode(x2, y2);
            }
        }
    }
}
