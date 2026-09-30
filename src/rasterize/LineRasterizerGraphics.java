package rasterize;

import raster.Raster;
import raster.RasterBufferedImage;

import java.awt.*;

// rasterizer využívající instanci Graphic, která již má algoritmus na čáru
public class LineRasterizerGraphics extends LineRasterizer {

    public LineRasterizerGraphics(Raster raster) {
        super(raster);
    }

    @Override
    public void rasterize(int x1, int y1, int x2, int y2) {
        Graphics g = ((RasterBufferedImage)raster).getImage().getGraphics();
        g.drawLine(x1, y1, x2, y2);
    }
}
