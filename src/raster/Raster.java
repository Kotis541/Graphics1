package raster;

public interface Raster {
    void setPixel(int x, int y, int color);
    void clear();
    int getWidth();
    int getHeight();
    int getPixel(int x, int y);
}
