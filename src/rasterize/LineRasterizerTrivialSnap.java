package rasterize;

import raster.Raster;

public class LineRasterizerTrivialSnap extends LineRasterizer{
    // speciální line rasterizer pro vykreslování v násobcích 45°
    public LineRasterizerTrivialSnap(Raster raster, int color) {
        super(raster, color);
    }

    @Override
    public void rasterize(int x1, int y1, int x2, int y2) {
        //for cyklus pro x (od x1 po x2) a dopočítám y
        // y = kx + q -- k je směrnice, q je posun po ose y, x je x
        // k = (y2-y1)/(x2-x1)
        // q = y1 - kx1

        // samostatný bod
        if (x2 - x1 == 0 && y2 - y1 == 0) {
            raster.setPixel(x1, y1, color);
            return;
        }

        // vykreslování podle primární osy X
        if (Math.abs(x2 - x1) >= Math.abs(y2 - y1)) {
            // výpočet směrnice
            float k = (float) (y2 - y1) / (x2 - x1);

            // snap po 45°
            if (k > -0.4 && k < 0.4) {
                k = 0;
            } else if (k >= 0.4 && k <= 1.0) {
                k = 1;
            } else if (k <= -0.4 && k >= -1.0) {
                k = -1;
            }

            // for cyklus pro vykreslení
            if (x1 > x2) {
                // vykreslování "pozpátku"
                for (int x = x1; x >= x2; x--) {
                    int y = Math.round(y1 + k * (x - x1));
                    raster.setPixel(x, y, color);
                }
            } else {
                for (int x = x1; x <= x2; x++) {
                    int y = Math.round(y1 + k * (x - x1));
                    raster.setPixel(x, y, color);
                }
            }


        }
        // vykreslování podle primární osy Y – pro vysoké k
        else {

            float k = (float) (x2 - x1) / (y2 - y1);

            if (k > -0.4 && k < 0.4) {
                k = 0;
            } else if (k >= 0.4 && k <= 1.0) {
                k = 1;
            } else if (k <= -0.4 && k >= -1.0) {
                k = -1;
            }

            if (y1 > y2) {
                for (int y = y1; y >= y2; y--) {
                    int x = Math.round(x1 + k * (y - y1));
                    raster.setPixel(x, y, color);
                }
            } else {
                for (int y = y1; y <= y2; y++) {
                    int x = Math.round(x1 + k * (y - y1));
                    raster.setPixel(x, y, color);
                }
            }

        }
    }
}
