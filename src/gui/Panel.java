package gui;

import javax.swing.*;

import raster.RasterBufferedImage;

import java.awt.*;

public class Panel extends JPanel{

    private final RasterBufferedImage raster;
    
    public Panel(int width, int height) {
        raster = new RasterBufferedImage(width, height);
        setPreferredSize(new Dimension(width, height));
    }

    public RasterBufferedImage getRaster() {
        return raster;
    }

    @Override 
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(raster.getImage(), 0, 0, null);
    }
}
