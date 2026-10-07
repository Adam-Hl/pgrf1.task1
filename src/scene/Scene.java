package scene;

import shapes.Polygon;
import shapes.Segment;

import java.util.ArrayList;

public class Scene {
    // objekt zodpovědný za správu tvarů na scéně
    private final ArrayList<Polygon> polygons = new ArrayList<>();
    private final ArrayList<Segment> segments = new ArrayList<>();


    public ArrayList<Polygon> getPolygons() {
        return polygons;
    }

    public ArrayList<Segment> getSegments() {
        return segments;
    }

    public void addPolygon(Polygon polygon) {
        polygons.add(polygon);
    }

    public void addSegment(Segment segment) {
        segments.add(segment);
    }
}
