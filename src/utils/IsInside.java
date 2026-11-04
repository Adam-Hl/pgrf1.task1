package utils;

import shapes.Edge;
import shapes.Point;
import shapes.Polygon;

import java.util.ArrayList;

public class IsInside {

    private boolean isInsideRC(int x, int y, Polygon polygon) {
        // metoda ray crossing pro určení, zda je bod (x, y) uvnitř polygonu
        int crossings = 0;
        ArrayList<Edge> edges = getEdgesRC(polygon);
        for (Edge edge : edges) {
            if (!edge.isIntersection(y)){
                continue;
            }
            // x souradnice průsečíku s hranou
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
            if (isInsideRC(x, y, polygon)) {
                return polygon;
            }
        }
        return null;
    }

    public ArrayList<Edge> getEdgesRC(Polygon polygon) {
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

    public ArrayList<Edge> getEdges(Polygon polygon) {
        // vytvářím seznam hran z bodů v polygonu
        ArrayList<Edge> edges = new ArrayList<>();
        for (int i = 0; i < polygon.getPoints().size(); i++) {
            Point p1 = polygon.getPoints().get(i);
            Point p2 = polygon.getPoints().get((i + 1) % polygon.getPoints().size());
            Edge edge = new Edge(p1, p2);
            edges.add(edge);
        }
        return edges;
    }

    public boolean isInsideHP(int x, int y, ArrayList<Polygon> polygons) {
        // metoda normálového vektoru pro určení, zda je bod (x, y) uvnitř polygonu
        for (Polygon polygon : polygons) {
            boolean inside = true;

            for (Edge edge : getEdges(polygon)) {
                int Ax = edge.getX1();
                int Ay = edge.getY1();
                int Bx = edge.getX2();
                int By = edge.getY2();

                // TODO: zavést třídu vektor: výpočet normály a skalárního součinu
                // tečný vektor hrany
                record tangent(int x, int y) {
                }
                tangent t = new tangent(Bx - Ax, By - Ay);
                System.out.println("Tangent: " + t.x + ", " + t.y);

                // spočítat normálu na levé straně hrany
                record normal(int x, int y) {
                }
                normal n = new normal(-t.y, t.x);

                // vektor k bodu P (kam jsem kliknul)
                record vectorToPoint(int x, int y) {
                }
                vectorToPoint v = new vectorToPoint(x - Ax, y - Ay);

                // skalární součin normály a vektoru k bodu P
                double dotProduct = n.x * v.x + n.y * v.y;

                // podívat se na znaménko skalárního součinu, pokud je záporné, bod je mimo polygon
                if (dotProduct < 0) {
                    inside = false;
                    break;
                }
            }
            if (inside) {
                return true;
            }
        }
        return false;
    }
}
