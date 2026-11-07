package shapes;

public class CroppingPolygon extends Polygon {

    public CroppingPolygon() {
        super(0xFFBF00); // oranžová barva
        // hexagon uprostřed canvasu
        this.addPoint(new Point(400, 150));
        this.addPoint(new Point(550, 250));
        this.addPoint(new Point(550, 400));
        this.addPoint(new Point(400, 500));
        this.addPoint(new Point(250, 400));
        this.addPoint(new Point(250, 250));
        this.setClosed(true);
    }
}
