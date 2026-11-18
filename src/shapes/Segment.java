package shapes;

import java.util.ArrayList;

public class Segment {
    private ArrayList<Point2D> point2DS;
    private int color;

    public Segment(Point2D start, Point2D end, int color) {
        point2DS = new ArrayList<>();
        point2DS.add(start);
        point2DS.add(end);
        this.color = color;
    }

    public Point2D getStart() {
        return point2DS.get(0);
    }

    public Point2D getEnd() {
        return point2DS.get(1);
    }

    public ArrayList<Point2D> getPoints() {
        return point2DS;
    }

    public void setStart(Point2D start) {
        point2DS.set(0, start);
    }

    public void setEnd(Point2D end) {
        point2DS.set(1, end);
    }

    public int getColor() {
        return color;
    }
}
