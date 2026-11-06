package croppers;

import shapes.CroppingRectangle;
import shapes.Point;
import shapes.Polygon;
import shapes.Segment;

import java.util.ArrayList;

public class SimpleFrameCrop {
    // ořezávací obdélník
    Polygon cropRectangle = new CroppingRectangle();

    int insideCode = 0b0000;;
    int leftCode = 0b1000;
    int rightCode = 0b0100;
    int bottomCode = 0b0010;
    int topCode = 0b0001;

    int xMin;
    int xMax;
    int yMin;
    int yMax;

    public SimpleFrameCrop() {

        this.xMin = cropRectangle.getPoints().get(0).getX();
        this.xMax = cropRectangle.getPoints().get(1).getX();
        this.yMin = cropRectangle.getPoints().get(0).getY();
        this.yMax = cropRectangle.getPoints().get(2).getY();
    }

    public Polygon getCropRectangle() {
        return cropRectangle;
    }

    // implementace cohen-sutherland algoritmu pro ořezávání segmentů podle zadaného obdélníku
    private int getCode(Point p) {
        int code = insideCode;

        if (p.getX() < xMin) {
            code |= leftCode; // bitový součet
        } else if (p.getX() > xMax) {
            code |= rightCode;
        }
        if (p.getY() < yMin) {
            code |= bottomCode;
        } else if (p.getY() > yMax) {
            code |= topCode;
        }
        return code;
    }

    public Segment CropCohenSutherland(Segment segment) {
        Point p1 = segment.getStart();
        Point p2 = segment.getEnd();

        int code1 = getCode(p1);
        int code2 = getCode(p2);
        boolean accepted = false;

        while (true) {
            if ((code1 | code2) == 0) { // oba body jsou uvnitř obdélníku
                accepted = true;
                break;
            } else if ((code1 & code2) != 0) { // oba body jsou mimo obdélník
                break;
            } else {
                int codeOut = (code1 != 0) ? code1 : code2;

                int x1 = p1.getX();
                int y1 = p1.getY();
                int x2 = p2.getX();
                int y2 = p2.getY();

                int x = 0;
                int y = 0;

                // směrnice
                float k;
                float h;

                if (x1 != x2) {
                    k = (float) (y2 - y1) / (x2 - x1);
                } else {
                    k = 0;
                }
                if (y1 != y2) {
                    h = (float) (x2 - x1) / (y2 - y1);
                } else {
                    h = 0;
                }

                // hledání průsečíku
                if ((codeOut & topCode) != 0) { // bod je nahoře
                    x = Math.round(h * (yMax - y1) + x1);
                    y = yMax;
                } else if ((codeOut & bottomCode) != 0) { // bod je dole
                    x = Math.round(h * (yMin - y1) + x1);
                    y = yMin;
                } else if ((codeOut & rightCode) != 0) { // bod je napravo
                    y = Math.round(k * (xMax - x1) + y1);
                    x = xMax;
                } else if ((codeOut & leftCode) != 0) { // bod je vlevo
                    y = Math.round(k * (xMin - x1) + y1);
                    x = xMin;
                }

                // přesunutí bodu, který byl venku
                if (codeOut == code1) {
                    p1 = new Point(x, y);
                    code1 = getCode(p1);
                } else {
                    p2 = new Point(x, y);
                    code2 = getCode(p2);
                }
            }
        }
        if (accepted) {
            return new Segment(new Point(p1.getX(), p1.getY()), new Point(p2.getX(), p2.getY()));
        } else {
            return null; // segment je zcela mimo obdélník
        }
    }
}
