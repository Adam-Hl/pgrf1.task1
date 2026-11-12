package fill;

import raster.Raster;
import shapes.Edge;
import shapes.Point;
import shapes.Polygon;

import java.util.ArrayList;

public class ScanLine implements Filler {
    private int fillColor;
    private Raster raster;
    private Polygon polygon = null;
    private record FilledPixel(int x, int y, int color) {}
    private final ArrayList<FilledPixel> filledPixels = new ArrayList<>();

    public ScanLine(int fillColor, Raster raster) {
        this.fillColor = fillColor;
        this.raster = raster;
    }

    @Override
    public void fill(int x, int y) {

        // najít správný min a max z polygonu
        int yMin = Integer.MAX_VALUE;
        int yMax = Integer.MIN_VALUE;
        for (Point point : polygon.getPoints()) {
            if (point.getY() < yMin) {
                yMin = point.getY();
            }
            if (point.getY() > yMax) {
                yMax = point.getY();
            }
        }

        for (int yi = yMin; yi <= yMax; yi++) {
            ArrayList<Double> intersections = new ArrayList<>();

            // pro danou scanline se ptám jestli existuje průsečík s hranou.
            ArrayList<Edge> edges = polygon.getEdgesOriented();
            if (edges == null) {
                return;
            }
            for (Edge edge : edges) {
                // zkontrolovat průsečík s hranou
                if (!edge.isIntersection(yi)) continue;
                // výpočet pozice průsečíku
                double intersectionX = edge.getX1() + (double) ((edge.getX2() - edge.getX1()) * (yi - edge.getY1())) / (edge.getY2() - edge.getY1());
                // uložit průsečík do seznamu průsečíků pro dané y
                intersections.add(intersectionX);
            }
            // spojíme lichý se sudým průsečíkem
            intersections.sort(Double::compareTo);
            for (int i = 0; i < intersections.size(); i += 2) {
                if (i + 1 < intersections.size()) {
                    int xStart = (int) Math.round(intersections.get(i));
                    int xEnd = (int) Math.round(intersections.get(i + 1));
                    // vyplnit oblast mezi průsečíky
                    for (int xi = xStart; xi <= xEnd; xi++) {
                        filledPixels.add(new FilledPixel(xi, yi, fillColor));
                        raster.setPixel(xi, yi, fillColor);
                    }
                }
            }
        }
        // obtažení polygonu probíhá v controlleru, protože je potřeba použít rasterizátor a scene renderer,
        // aby se obtažení vykreslilo na raster.
        // ScanLine je jen algoritmus pro vyplnění polygonu, ne pro jeho vykreslení.
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    @Override
    public void reFill() {
        for (FilledPixel pixel : filledPixels) {
            raster.setPixel(pixel.x, pixel.y, pixel.color);
        }
    }

    @Override
    public void clearFilledPixels() {
        filledPixels.clear();
    }
}
