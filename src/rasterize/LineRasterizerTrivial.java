package rasterize;

import raster.Raster;

// resterizer pro přímku, který využívá vlastní algoritmus
public class LineRasterizerTrivial extends LineRasterizer{
    public LineRasterizerTrivial(Raster raster) {
        super(raster);
    }

    @Override
    public void rasterize(int x1, int y1, int x2, int y2) {
        // TODO: při vysokém k se vynachávají pixely -> prohození primární osy
        // TODO: samotný bod
        //for cyklus pro x (od x1 po x2) a dopočítám y
        // y = kx + q -- k je směrnice, q je posun po ose y, x je x
        // k = (y2-y1)/(x2-x1)
        // q = y1 - kx1
        if (x1 > x2) {
            int temp = x1;
            x1 = x2;
            x2 = temp;

            temp = y1;
            y1 = y2;
            y2 = temp;
        }
        for (int i = x1; i <= x2; i++) {
            float k = (float) (y2 - y1) / (x2 - x1);
            float q = y1 - k * x1;
            int y = (int) (k * i + q);
            raster.setPixel(i, y, 0xffff00);
        }
    }
}
