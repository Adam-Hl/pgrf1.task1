package fill;

import shapes.Polygon;

public interface Filler {

    void fill(int x, int y);

    void reFill();

    void clearFilledPixels();
}
