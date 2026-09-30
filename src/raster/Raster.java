package raster;

import java.awt.image.BufferedImage;
// interface pro různé rastery
public interface Raster {
    public void setPixel(int x, int y, int color);
    public int getWidth();
    public int getHeight();
    public void clear();
}
