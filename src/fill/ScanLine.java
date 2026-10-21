package fill;

import shapes.Edge;
import shapes.Polygon;
import shapes.Point;

import java.awt.image.Raster;
import java.util.ArrayList;

public class ScanLine implements Filler {
    private int fillColor;
    private Polygon polygon;
    private Raster raster;

    public ScanLine(int fillColor, Raster raster, Polygon polygon) {
        this.fillColor = fillColor;
        this.polygon = polygon;
        this.raster = raster;
    }

    @Override
    public void fill(int x, int y) {

        // TODO: najít správný min a max z polygonu
        int yMin = 0;
        int yMax = 600;

        for (int y = yMin; y <= yMax; y++) {
            ArrayList<Integer> intersections = new ArrayList<>();

            // TODO: pro danou scanline se ptáme jestli existuje průsečík s hranou.
            ArrayList<Edge> edges = getEdges(polygon);
            for (Edge edge : edges) {
                // TODO: zkontrolovat průsečík s hranou
                if (!edge.isIntersection(y)) continue;
                    // TODO: výpočet pozice průsečíku
                    // TODO: uložit průsečík do seznamu průsečíků pro dané y
            }
            // TODO: spojíme lichý se sudým průsečíkem
        }
        // TODO: obtáhnout polygon
    }

    private ArrayList<Edge> getEdges(Polygon polygon) {
        // vytvářím seznam hran z bodů v polygonu
        ArrayList<Edge> edges = new ArrayList<>();
        for (int i = 0; i < polygon.getPoints().size(); i++) {
            Point p1 = polygon.getPoints().get(i);
            Point p2 = polygon.getPoints().get((i + 1) % polygon.getPoints().size());
            Edge edge = new Edge(p1, p2);
            if (!edge.isHorizontal()) {
                edge.orientate();
            }
        }
        return edges;
    };
}
