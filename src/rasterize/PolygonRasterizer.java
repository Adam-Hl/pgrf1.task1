package rasterize;

import shapes.Point;
import shapes.Polygon;

import java.util.ArrayList;

public class PolygonRasterizer{
    private final LineRasterizer lineRasterizer;

    public PolygonRasterizer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    public void rasterize(Polygon polygon) {
            ArrayList<Point> points = polygon.getPoints();
            for (int i = 0; i < points.size(); i++) {
                Point p1 = points.get(i);
                Point p2;
                if (polygon.isClosed()) {
                    // modulo pro spojení posledního bodu s prvním pokud je polygon uzavřený
                    p2 = points.get((i + 1) % points.size());
                } else {
                    if (i + 1 < points.size()) {
                        p2 = points.get(i + 1);
                    } else {
                        break;
                    }
                }
                // vykreslení čáry mezi body p1 a p2
                if (p2 != null) {
                    lineRasterizer.rasterize(p1.getX(), p1.getY(), p2.getX(), p2.getY());
                }
            }
    }
}
