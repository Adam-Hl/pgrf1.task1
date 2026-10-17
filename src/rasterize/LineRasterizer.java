package rasterize;

import raster.Raster;

// abstraktní třída pro rasterozery (třídy pro vykreslování tvarů)
public abstract class LineRasterizer {
    protected final Raster raster;
    protected final int color;

    public LineRasterizer(Raster raster, int color) {
        this.color = color;
        this.raster = raster;
    }

    public void rasterize(int x1, int y1, int x2, int y2) {
    }
}
