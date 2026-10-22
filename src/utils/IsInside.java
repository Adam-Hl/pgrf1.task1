package utils;

import shapes.Edge;
import shapes.Point;
import shapes.Polygon;

import java.util.ArrayList;

public class IsInside {

    private boolean isInside(int x, int y, Polygon polygon) {
        // metoda ray crossing pro určení, zda je bod (x, y) uvnitř polygonu
        int crossings = 0;
        ArrayList<Edge> edges = getEdges(polygon);
        for (Edge edge : edges) {
            if (!edge.isIntersection(y)){
                continue;
            }
            int intersectionX = (edge.getX1() + (edge.getX2() - edge.getX1()) * (y - edge.getY1()) / (edge.getY2() - edge.getY1()));

            if (x < intersectionX) {
                    crossings++;
            }
        }
        // pokud je počet průsečíků lichý, bod je uvnitř polygonu
        return crossings % 2 == 1;
    }

    public Polygon isInsidePolygon(int x, int y, ArrayList<Polygon> polygons) {
        // metoda vrací polygon, ve kterém se nachází bod (x, y)
        for (Polygon polygon : polygons) {
            if (isInside(x, y, polygon)) {
                return polygon;
            }
        }
        return null;
    }

    public ArrayList<Edge> getEdges(Polygon polygon) {
        // vytvářím seznam hran z bodů v polygonu
        ArrayList<Edge> edges = new ArrayList<>();
        for (int i = 0; i < polygon.getPoints().size(); i++) {
            Point p1 = polygon.getPoints().get(i);
            Point p2 = polygon.getPoints().get((i + 1) % polygon.getPoints().size());
            Edge edge = new Edge(p1, p2);
            if (!edge.isHorizontal()) {
                edge.orientate();
                edges.add(edge);
            }
        }
        return edges;
    }
}
