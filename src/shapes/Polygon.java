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
        return color;
    }

    public ArrayList<Edge> getEdgesOriented() {
        // vytvářím seznam hran z bodů v polygonu
        ArrayList<Edge> edges = new ArrayList<>();
        for (int i = 0; i < getPoints().size(); i++) {
            Point p1 = getPoints().get(i);
            Point p2 = getPoints().get((i + 1) % getPoints().size());
            Edge edge = new Edge(p1, p2);
            if (!edge.isHorizontal()) {
                edge.orientate();
                edges.add(edge);
            }
        }
        return edges;
    }

    public ArrayList<Edge> getEdges() {
        // vytvářím seznam hran z bodů v polygonu
        ArrayList<Edge> edges = new ArrayList<>();
        for (int i = 0; i < getPoints().size(); i++) {
            Point p1 = getPoints().get(i);
            Point p2 = getPoints().get((i + 1) % getPoints().size());
            Edge edge = new Edge(p1, p2);
            edges.add(edge);
        }
        return edges;
    }
}
