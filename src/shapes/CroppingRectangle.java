package shapes;

public class CroppingRectangle extends Polygon{

    public CroppingRectangle() {
        super(0xFFC107); // žlutá barva
        this.addPoint(new Point(250, 175));
        this.addPoint(new Point(550, 175));
        this.addPoint(new Point(550, 425));
        this.addPoint(new Point(250, 425));
        this.setClosed(true);
    }

}
