package rasterize;

import raster.Raster;

public class LineRasterizerTrivial extends LineRasterizer{
    public LineRasterizerTrivial(Raster raster) {
        super(raster);
    }

    @Override
    public void rasterize(int x1, int y1, int x2, int y2) {
        // TODO: uvařit ten algoritmus
        // TODO: ošetřit záporné čísla typu x2 < x1
        // TODO: při vysokém k se vynachávají pixely -> prohození primární osy
        // TODO: dělení nulou při k = "vertikální"
        // TODO: samotný bod
        //for cyklus pro x (od x1 po x2) a dopočítám y
        // y = kx + q -- k je směrnice, q je posun po ose y, x je x
        // k = (y2-y1)/(x2-x1)
        // q = y1 - kx1
    }
}
