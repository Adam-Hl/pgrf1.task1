package utils;

import shapes.Point;

public class SegmentSnapper {

    // metoda pro snapování koncového bodu segmentu na 45° úhly
    public Point snapSegmentEndPoint(Point startPoint, Point endPoint) {
        int x1 = startPoint.getX();
        int y1 = startPoint.getY();
        int x2 = endPoint.getX();
        int y2 = endPoint.getY();

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
        return new Point(x2, y2);
    }
}
