package raster;

import java.util.OptionalInt;

// interface pro různé rastery -- mřížky pro vykreslování pixelů
public interface Raster {
    void setPixel(int x, int y, int color);
    int getWidth();
    int getHeight();
    void clear();
    OptionalInt getPixel(int x, int y);

}
