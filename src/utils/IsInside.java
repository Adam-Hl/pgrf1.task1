package utils;

import shapes.Edge;
import shapes.Polygon;

import java.util.ArrayList;

public class IsInside {

    private boolean isInsideRC(int x, int y, Polygon polygon) {
        // metoda ray crossing pro určení, zda je bod (x, y) uvnitř polygonu
        int crossings = 0;
        ArrayList<Edge> edges = polygon.getEdgesOriented();
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

    public boolean isInsidePos(int x, int y, Edge edge) {
        // metoda normálového vektoru pro určení, zda je bod (x, y) uvnitř polygonu
        int Ax = edge.getX1();
        int Ay = edge.getY1();
        int Bx = edge.getX2();
        int By = edge.getY2();

        // tečný vektor hrany
        Vector t = new Vector(Bx - Ax, By - Ay);

        // spočítat normálu na levé straně hrany
        Vector n = new Vector(-t.getY(), t.getX());

        // vektor k bodu P (kam jsem kliknul)
        Vector v = new Vector(x - Ax, y - Ay);

        // skalární součin normály a vektoru k bodu P
        double dotProduct = n.dotProduct(v);

        // podívat se na znaménko skalárního součinu, pokud je záporné, bod je mimo polygon
        return !(dotProduct < 0);
    }

    public boolean isInsideNeg(int x, int y, Edge edge) {
        // metoda normálového vektoru pro určení, zda je bod (x, y) uvnitř polygonu
        int Ax = edge.getX1();
        int Ay = edge.getY1();
        int Bx = edge.getX2();
        int By = edge.getY2();

        // tečný vektor hrany
        Vector t = new Vector(Bx - Ax, By - Ay);

        // spočítat normálu na levé straně hrany
        Vector n = new Vector(t.getY(), -t.getX());

        // vektor k bodu P (kam jsem kliknul)
        Vector v = new Vector(x - Ax, y - Ay);

        // skalární součin normály a vektoru k bodu P
        double dotProduct = n.dotProduct(v);

        // podívat se na znaménko skalárního součinu, pokud je záporné, bod je mimo polygon
        return !(dotProduct < 0);
    }
}
