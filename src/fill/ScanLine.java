package fill;

import raster.Raster;
import shapes.Edge;
import shapes.Polygon;
import shapes.Point;
import utils.IsInside;

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

        // TODO: najít správný min a max z polygonu
        int yMin = 0;
        int yMax = raster.getHeight() - 1;

        for (int yi = yMin; yi <= yMax; yi++) {
            ArrayList<Integer> intersections = new ArrayList<>();

            // pro danou scanline se ptám jestli existuje průsečík s hranou.
            ArrayList<Edge> edges = new IsInside().getEdges(polygon);
            if (edges == null) {
                return;
            }
            for (Edge edge : edges) {
                // zkontrolovat průsečík s hranou
                if (!edge.isIntersection(yi)) continue;
                // výpočet pozice průsečíku
                double intersectionX = edge.getX1() + (double) ((edge.getX2() - edge.getX1()) * (yi - edge.getY1())) / (edge.getY2() - edge.getY1());
                // uložit průsečík do seznamu průsečíků pro dané y
                intersections.add((int) intersectionX);
            }
            // spojíme lichý se sudým průsečíkem
            intersections.sort(Integer::compareTo);
            for (int i = 0; i < intersections.size(); i += 2) {
                if (i + 1 < intersections.size()) {
                    int xStart = intersections.get(i);
                    int xEnd = intersections.get(i + 1);
                    // vyplnit oblast mezi průsečíky
                    for (int xi = xStart; xi <= xEnd; xi++) {
                        filledPixels.add(new FilledPixel(xi, yi, fillColor));
                        raster.setPixel(xi, yi, fillColor);
                    }
                }
            }
        }
        // TODO: obtáhnout polygon
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
