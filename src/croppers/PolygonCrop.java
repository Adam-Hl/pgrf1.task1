package croppers;

import shapes.*;
import utils.IsInside;

import java.util.ArrayList;

public class PolygonCrop {
    private Polygon cropPolygon = new CroppingPolygon();
    IsInside isInside = new IsInside();

    public void setCropPolygon(Polygon cropPolygon) {
        this.cropPolygon = cropPolygon;
    }

    public Polygon CropSutherlandHodgman(Polygon inputPolygon) {
        // implementace algoritmu Sutherland-Hodgman pro ořezávání polygonů podle zadaného polygonu

        ArrayList<Point2D> inputPoint2DS = inputPolygon.getPoints();
        // orientace ořezávacího polygonu
        boolean positiveOrientation = getOrientation(cropPolygon) > 0;

        for (Edge edge : cropPolygon.getEdges()) {
            ArrayList<Point2D> outputPoint2DS = new ArrayList<>();

            // začátek v prvním bodu
            if (inputPoint2DS.isEmpty()) {
                break;
            }
            Point2D p1 = inputPoint2DS.get(inputPoint2DS.size() - 1);

            for (Point2D p2 : inputPoint2DS) {
                // zjištění, zda jsou body p1 a p2 uvnitř ořezávacího polygonu; řídí se orientací ořezávacího polygonu
                boolean p1Inside = positiveOrientation
                        ? isInside.isInsidePos(p1.getX(), p1.getY(), edge)
                        : isInside.isInsideNeg(p1.getX(), p1.getY(), edge);

                boolean p2Inside = positiveOrientation
                        ? isInside.isInsidePos(p2.getX(), p2.getY(), edge)
                        : isInside.isInsideNeg(p2.getX(), p2.getY(), edge);


                // zjištění polohy bodů vůči hraně ořezávacího polygonu
                if (p2Inside) {
                    if (!p1Inside) {
                        // hrana vstupuje do ořezávacího polygonu, přidá se průsečík
                        Point2D intersection = getIntersection(edge, p1, p2);
                        outputPoint2DS.add(intersection);
                    }
                    // bod p2 je uvnitř ořezávacího polygonu, přidá se do výstupního seznamu
                    outputPoint2DS.add(p2);
                } else if (p1Inside) {
                    // hrana vystupuje z ořezávacího polygonu, přidá se průsečík
                    Point2D intersection = getIntersection(edge, p1, p2);
                    outputPoint2DS.add(intersection);
                } // jinak jsou oba body vně ořezávacího polygonu, nic se nepřidává
                // aktualizace p1 pro další iteraci
                p1 = p2;
            }
            // aktualizace vstupního seznamu bodů pro další hranu ořezávacího polygonu
            inputPoint2DS = outputPoint2DS;
        }
        Polygon outputPolygon = new Polygon(inputPolygon.getColor());
        for (Point2D point2D : inputPoint2DS) {
            outputPolygon.addPoint(point2D);
        }
        outputPolygon.setClosed(true);
        return outputPolygon;
    }

    private Point2D getIntersection(Edge edge, Point2D p1, Point2D p2) {
        int p1x1 = edge.getX1();
        int p1y1 = edge.getY1();
        int p1x2 = edge.getX2();
        int p1y2 = edge.getY2();

        int p2x3 = p1.getX();
        int p2y3 = p1.getY();
        int p2x4 = p2.getX();
        int p2y4 = p2.getY();

        // výpočet průsečíku dvou přímek pomocí determinantů
        // determinant pro jmenovatele
        int det = (p1x1 - p1x2) * (p2y3 - p2y4) - (p1y1 - p1y2) * (p2x3 - p2x4);

        // když je jmenovatel 0, přímky jsou rovnoběžné a nemají průsečík, vrací se bod p2
        if (det == 0) {
            return new Point2D(p2.getX(), p2.getY());
        }

        // determinanty pro každou souřadnici průsečíku
        int p = (p1x1 * p1y2 - p1y1 * p1x2);
        int u = (p2x3 * p2y4 - p2y3 * p2x4);

        int intersectX = (p * (p2x3 - p2x4) - (p1x1 - p1x2) * u) / det;
        int intersectY = (p * (p2y3 - p2y4) - (p1y1 - p1y2) * u) / det;

        return new Point2D(intersectX, intersectY);
    }

    // metoda pro určení orientace polygonu
    private long getOrientation(Polygon polygon) {
        long area = 0;
        // použití vzorce pro výpočet plochy polygonu, který určuje orientaci
        // pokud je plocha kladná, polygon je orientován proti směru hodinových ručiček
        // pokud je plocha záporná, polygon je orientován ve směru hodinových ručiček
        for (int i = 0; i < polygon.getPoints().size(); i++) {
            Point2D p1 = polygon.getPoints().get(i);
            Point2D p2 = polygon.getPoints()
                    .get((i + 1) % polygon.getPoints().size());

            area += (long) p1.getX() * p2.getY()
                    - (long) p2.getX() * p1.getY();
        }
        // vrace se kladná nebo záporná hodnota podle orientace polygonu
        return Long.compare(area, 0);
    }
}
