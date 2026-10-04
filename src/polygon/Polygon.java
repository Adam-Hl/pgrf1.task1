package polygon;

import java.util.ArrayList;

public class Polygon {
    private ArrayList<Point> points;

    public Polygon() {
        points = new ArrayList<>();
    }


    public void addPoint(Point point) {
        points.add(point);
    }

    public ArrayList<Point> getPoints() {
        return points;
    }
}
