package scene;

import croppers.PolygonCrop;
import croppers.SimpleFrameCrop;
import fill.Filler;
import fill.SeedFill;
import raster.Raster;
import rasterize.LineRasterizer;
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
    private final LineRasterizer lineRasterizer;
    private final Filler filler;
    private final SimpleFrameCrop simpleFrameCrop = new SimpleFrameCrop();
    private final PolygonCrop polygonCrop = new PolygonCrop();

    public SceneRenderer(Scene scene, Raster raster, LineRasterizer lineRasterizer, Filler filler) {
        this.scene = scene;
        this.raster = raster;
        this.lineRasterizer = lineRasterizer;
        this.filler = filler;
    }

    // metoda pro překreslení všech tvarů
    public void repaintShapes() {
        raster.clear();
        filler.reFill();

        // ořezávání pomocí SimpleFrameCrop; pokud je v scéně přítomen ořezávací obdélník, použije se pro ořezávání segmentů
        boolean croppingRectanglePresent = scene.getCroppingPolygons().stream().anyMatch(polygon -> polygon instanceof shapes.CroppingRectangle);
        // ořezávání pomocí CroppingPolygon; pokud je v scéně přítomen ořezávací polygon, použije se pro ořezávání polygonů
        boolean croppingPolygonPresent = scene.getCroppingPolygons().stream().anyMatch(polygon -> polygon instanceof shapes.CroppingPolygon);

        // přenastavení velikosti croppingPolygon, pokud je přítomen
        if (croppingPolygonPresent) {
            Polygon croppingPolygon = scene.getCroppingPolygons().stream()
                    .filter(polygon -> polygon instanceof shapes.CroppingPolygon)
                    .findFirst()
                    .orElseThrow();

            polygonCrop.setCropPolygon(croppingPolygon);
        }

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
            // ořezávání polygonů pomocí algoritmu Sutherland-Hodgman, pokud je přítomen ořezávací polygon
            if (croppingPolygonPresent) {
                polygon = polygonCrop.CropSutherlandHodgman(polygon);
                if (polygon.getPoints().isEmpty()) {
                    // odstranění polygonu, pokud je zcela mimo ořezávací polygon
                    continue;
                }
            }
            polygonRasterizer.setColor(polygon.getColor());
            polygonRasterizer.rasterize(polygon);
        }

        // přenastavení velikosti croppingRectangle, pokud je přítomen
        if (croppingRectanglePresent) {
            Polygon croppingRectangle = scene.getCroppingPolygons().stream()
                    .filter(polygon -> polygon instanceof shapes.CroppingRectangle)
                    .findFirst()
                    .orElseThrow();

            simpleFrameCrop.setCropRectangle(croppingRectangle);
        }

        Iterator<Segment> iterator = scene.getSegments().iterator();
        while (iterator.hasNext()) {
            Segment segment = iterator.next();
            if (segment.getPoints().size() < 2) {
                // odstranění segmentu, pokud má méně než 2 body
                iterator.remove();
                continue;
            }
            // ořezávání segmentů pomocí algoritmu Cohen-Sutherland, pokud je přítomen ořezávací obdélník
            if (croppingRectanglePresent) {
                segment = simpleFrameCrop.CropCohenSutherland(segment);
                if (segment == null) {
                    // odstranění segmentu, pokud je zcela mimo ořezávací obdélník
                    continue;
                }
            }
            lineRasterizer.setColor(segment.getColor());
            lineRasterizer.rasterize(segment.getStart().getX(), segment.getStart().getY(), segment.getEnd().getX(), segment.getEnd().getY());
        }
        // vykreslení ořezávacího polygonu, pokud je přítomen
        if (croppingPolygonPresent || croppingRectanglePresent) {
            for (Polygon croppingPolygon : scene.getCroppingPolygons()) {
                polygonRasterizer.setColor(croppingPolygon.getColor());
                polygonRasterizer.rasterize(croppingPolygon);
            }
        }
    }
}
