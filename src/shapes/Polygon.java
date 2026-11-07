package shapes;

import java.util.ArrayList;

public class Polygon {
    private final ArrayList<Point> points;
    private boolean isClosed;
    private int color;

    public Polygon(int color) {
        points = new ArrayList<>();
        isClosed = false;
        this.color = color;
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

    public int getColor() {
        return 0;
    }
}
