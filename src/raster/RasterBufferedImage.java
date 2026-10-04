package raster;

import shapes.Point;
import shapes.Polygon;
import rasterize.LineRasterizerTrivial;
import shapes.Segment;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

// nový raster pro projekt
public class RasterBufferedImage implements Raster{
    // custom raster, který využívá BufferedImage pro vykreslování pixelů
    private final BufferedImage image;

    // array pro ukládání tvarů, které se mají vykreslit
    private final ArrayList<Polygon> polygons = new ArrayList<>();
    private final ArrayList<Segment> segments = new ArrayList<>();

    public RasterBufferedImage(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    // nový "setRGB" s kontrolou kreslení mimo raster
    public void setPixel(int x, int y, int color) {
        if (x >= 0 && 0 <= y && x < getWidth() && y < getHeight()) {
            image.setRGB(x, y, color);
        }
    }

    @Override
    public void clear() {
        image.getGraphics().clearRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

    public BufferedImage getImage() {
        return image;
    }

    public void addSegment(Segment segment) {
        segments.add(segment);
    }

    public ArrayList<Segment> getSegments() {
        return segments;
    }

    public void addPolygon(Polygon polygon) {
        polygons.add(polygon);
    }

    public ArrayList<Polygon> getPolygons() {
        return polygons;
    }

    // metoda pro překreslení všech tvarů
    public void repaintShapes() {
        clear();
        // použití iteratoru pro bezpečné odstranění polygonů a segmentů během iterace, aby se předešlo ConcurrentModificationException
        // iterátor je objekt, který umožňuje bezpečně procházet kolekci a odstraňovat prvky během iterace
        Iterator<Polygon> polygonIterator = polygons.iterator();
        while (polygonIterator.hasNext()) {
            Polygon polygon = polygonIterator.next();
            ArrayList<Point> points = polygon.getPoints();
            if (points.isEmpty()) {
                // odstranění polygonu, pokud má méně než 1 bod
                polygonIterator.remove();
                continue;
            }
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
                    new LineRasterizerTrivial(this).rasterize(p1.getX(), p1.getY(), p2.getX(), p2.getY());
                }
            }
        }
        Iterator<Segment> iterator = segments.iterator();
        while (iterator.hasNext()) {
            Segment segment = iterator.next();
            if (segment.getPoints().size() < 2) {
                // odstranění segmentu, pokud má méně než 2 body
                iterator.remove();
                continue;
            }
            new LineRasterizerTrivial(this).rasterize(segment.getStart().getX(), segment.getStart().getY(), segment.getEnd().getX(), segment.getEnd().getY());
        }
    }
}
