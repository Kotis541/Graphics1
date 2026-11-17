package fill;

import model.Point;
import raster.Raster;

import java.util.ArrayDeque;

public class Seedfilter {

    private final Raster raster;

    public Seedfilter(Raster raster) {
        this.raster = raster;
    }
    public void fill(int x, int y, int fillColor) {
        int width = this.raster.getWidth();
        int height = this.raster.getHeight();
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return;
        }

        int backgroundColor = this.raster.getPixel(x, y);
        if (backgroundColor == fillColor) {
            return;
        }

        ArrayDeque<Point> stack = new ArrayDeque<>();
        stack.push(new Point(x, y));

        while (!stack.isEmpty()) {
            Point p = stack.pop();
            int px = p.getX();
            int py = p.getY();
            if (px >= 0 && px < width && py >= 0 && py < height
                    && this.raster.getPixel(px, py) == backgroundColor) {
                this.raster.setPixel(px, py, fillColor);
                pushNeighbors(stack, px, py);
            }
        }
    }
    public void fillByBoundary(int x, int y, int fillColor, int boundaryColor) {
        int width = this.raster.getWidth();
        int height = this.raster.getHeight();
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return;
        }

        ArrayDeque<Point> stack = new ArrayDeque<>();
        stack.push(new Point(x, y));

        while (!stack.isEmpty()) {
            Point p = stack.pop();
            int px = p.getX();
            int py = p.getY();
            if (px >= 0 && px < width && py >= 0 && py < height) {
                int currentColor = this.raster.getPixel(px, py);
                if (currentColor != boundaryColor && currentColor != fillColor) {
                    this.raster.setPixel(px, py, fillColor);
                    pushNeighbors(stack, px, py);
                }
            }
        }
    }

    private void pushNeighbors(ArrayDeque<Point> stack, int px, int py) {
        stack.push(new Point(px + 1, py));
        stack.push(new Point(px - 1, py));
        stack.push(new Point(px, py + 1));
        stack.push(new Point(px, py - 1));
    }
}
