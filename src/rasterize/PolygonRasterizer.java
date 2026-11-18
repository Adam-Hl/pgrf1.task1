package rasterize;

import shapes.Point2D;
import shapes.Polygon;

import java.util.ArrayList;

public class PolygonRasterizer{
    private final LineRasterizer lineRasterizer;
    private int color;

    public PolygonRasterizer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    public void rasterize(Polygon polygon) {
            ArrayList<Point2D> point2DS = polygon.getPoints();
            for (int i = 0; i < point2DS.size(); i++) {
                Point2D p1 = point2DS.get(i);
                Point2D p2;
                if (polygon.isClosed()) {
                    // modulo pro spojení posledního bodu s prvním pokud je polygon uzavřený
                    p2 = point2DS.get((i + 1) % point2DS.size());
                } else {
                    if (i + 1 < point2DS.size()) {
                        p2 = point2DS.get(i + 1);
                    } else {
                        break;
                    }
                }
                // vykreslení čáry mezi body p1 a p2
                if (p2 != null) {
                    lineRasterizer.setColor(color);
                    lineRasterizer.rasterize(p1.getX(), p1.getY(), p2.getX(), p2.getY());
                }
            }
    }

    public void setColor(int color) {
        this.color = color;
    }
}
