package utils;

import shapes.Point2D;

public class SegmentSnapper {

    // metoda pro snapování koncového bodu segmentu na 45° úhly
    public Point2D snapSegmentEndPoint(Point2D startPoint2D, Point2D endPoint2D) {
        int x1 = startPoint2D.getX();
        int y1 = startPoint2D.getY();
        int x2 = endPoint2D.getX();
        int y2 = endPoint2D.getY();

        if (Math.abs(x2 - x1) >= Math.abs(y2 - y1)) {
            // výpočet směrnice
            float k = (float) (y2 - y1) / (x2 - x1);

            // snap po 45°
            if (k > -0.4 && k < 0.4) {
                k = 0;
            } else if (k >= 0.4 && k <= 1.0) {
                k = 1;
            } else if (k <= -0.4 && k >= -1.0) {
                k = -1;
            }
            y2 = Math.round(y1 + k * (x2 - x1));
        }
        // vykreslování podle primární osy Y – pro vysoké k
        else {

            float k = (float) (x2 - x1) / (y2 - y1);

            if (k > -0.4 && k < 0.4) {
                k = 0;
            } else if (k >= 0.4 && k <= 1.0) {
                k = 1;
            } else if (k <= -0.4 && k >= -1.0) {
                k = -1;
            }
            x2 = Math.round(x1 + k * (y2 - y1));
        }
        return new Point2D(x2, y2);
    }
}
