package polygon;

import java.util.ArrayList;

public class Polygon {
    private final ArrayList<Point> points;
    private boolean isClosed;

    public Polygon() {
        points = new ArrayList<>();
        isClosed = false;
    }


    public void addPoint(Point point) {
        points.add(point);
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }
}
