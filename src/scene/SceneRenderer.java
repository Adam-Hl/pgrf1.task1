package scene;

import raster.Raster;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerTrivial;
import rasterize.PolygonRasterizer;
import shapes.Point;
import shapes.Polygon;
import shapes.Segment;

import java.util.ArrayList;
import java.util.Iterator;

public class SceneRenderer {
    // objekt zodpovědný za vykreslování scény pomocí rasterizátorů
    private final Scene scene;
    private final Raster raster;

    public SceneRenderer(Scene scene, Raster raster) {
        this.scene = scene;
        this.raster = raster;
    }

    // metoda pro překreslení všech tvarů
    public void repaintShapes() {
        raster.clear();
        LineRasterizer lineRasterizer = new LineRasterizerTrivial(raster);
        PolygonRasterizer polygonRasterizer = new PolygonRasterizer(lineRasterizer);
        // použití iteratoru pro bezpečné odstranění polygonů během iterace, aby se předešlo ConcurrentModificationException
        // iterátor je objekt, který umožňuje bezpečně procházet kolekci a odstraňovat prvky během iterace
        Iterator<Polygon> polygonIterator = scene.getPolygons().iterator();
        while (polygonIterator.hasNext()) {
            Polygon polygon = polygonIterator.next();
            ArrayList<Point> points = polygon.getPoints();
            if (points.isEmpty()) {
                // odstranění polygonu, pokud má méně než 1 bod
                polygonIterator.remove();
                continue;
            }
            polygonRasterizer.rasterize(polygon);
        }
        Iterator<Segment> iterator = scene.getSegments().iterator();
        while (iterator.hasNext()) {
            Segment segment = iterator.next();
            if (segment.getPoints().size() < 2) {
                // odstranění segmentu, pokud má méně než 2 body
                iterator.remove();
                continue;
            }
            lineRasterizer.rasterize(segment.getStart().getX(), segment.getStart().getY(), segment.getEnd().getX(), segment.getEnd().getY());
        }
    }
}
