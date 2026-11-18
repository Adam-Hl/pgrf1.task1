package shapes;

import java.util.ArrayList;

public class Polygon {
    private final ArrayList<Point2D> point2DS;
    private boolean isClosed;
    private int color;

    public Polygon(int color) {
        point2DS = new ArrayList<>();
        isClosed = false;
        this.color = color;
    }


    public void addPoint(Point2D point2D) {
        point2DS.add(point2D);
    }

    public ArrayList<Point2D> getPoints() {
        return point2DS;
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
            Point2D p1 = getPoints().get(i);
            Point2D p2 = getPoints().get((i + 1) % getPoints().size());
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
            Point2D p1 = getPoints().get(i);
            Point2D p2 = getPoints().get((i + 1) % getPoints().size());
            Edge edge = new Edge(p1, p2);
            edges.add(edge);
        }
        return edges;
    }
}
