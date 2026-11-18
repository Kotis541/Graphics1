package model.line;

import raster.Raster;

public class LineRasterizer {

    private final Raster raster;

    public LineRasterizer(Raster raster) {
        this.raster = raster;
    }

    public void rasterize(Line line, int color) {
        rasterize(
            line.getP1().getX(),
            line.getP1().getY(),
            line.getP2().getX(),
            line.getP2().getY(),
            color
        );
    }

    // Rasterizace úsečky pomocí algoritmu DDA (Digital Differential Analyzer)
    public void rasterize(int x1, int y1, int x2, int y2, int color) {
        int dx = x2 - x1;
        int dy = y2 - y1;

        int steps = Math.max(Math.abs(dx), Math.abs(dy));
        if (steps == 0) {
            raster.setPixel(x1, y1, color);
            return ;
        }

        float xInc = (float) dx / steps;
        float yInc = (float) dy / steps;

        float x = x1;
        float y = y1;

        for(int i = 0; i <= steps; i++) {
            raster.setPixel(Math.round(x), Math.round(y), color);
            x += xInc;
            y += yInc;
        }
        
        
    }
}
