package shapes;

import java.util.ArrayList;

public class Segment {
    private ArrayList<Point> points;
    private int color;

    public Segment(Point start, Point end, int color) {
        points = new ArrayList<>();
        points.add(start);
        points.add(end);
        this.color = color;
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

    public int getColor() {
        return color;
    }
}
