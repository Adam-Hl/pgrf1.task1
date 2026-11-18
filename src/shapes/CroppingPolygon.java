package shapes;

public class CroppingPolygon extends Polygon {

    public CroppingPolygon() {
        super(0xFFBF00); // oranžová barva
        // hexagon uprostřed canvasu
        this.addPoint(new Point2D(400, 150));
        this.addPoint(new Point2D(550, 250));
        this.addPoint(new Point2D(550, 400));
        this.addPoint(new Point2D(400, 500));
        this.addPoint(new Point2D(250, 400));
        this.addPoint(new Point2D(250, 250));
        this.setClosed(true);
    }
}
