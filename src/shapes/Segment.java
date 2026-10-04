package shapes;

import java.util.ArrayList;

public class Segment {
    private ArrayList<Point> points;

    public Segment(Point start, Point end) {
        points = new ArrayList<>();
        points.add(start);
        points.add(end);
    }

    public Point getStart() {
        return points.get(0);
    }

    public Point getEnd() {
        return points.get(1);
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void setStart(Point start) {
        points.set(0, start);
    }

    public void setEnd(Point end) {
        points.set(1, end);
    }
}
