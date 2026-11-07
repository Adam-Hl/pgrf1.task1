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

    // metoda pro přesunutí bodu obdélníku na nové souřadnice, přičemž se zachovává tvar obdélníku
    public void movePoint(Point point, int x, int y) {
        int index = getPoints().indexOf(point);

        if (index < 0) {
            throw new IllegalArgumentException("Bod není součástí obdélníku");
        }

        Point opposite = getPoints().get((index + 2) % 4);

        int minX = Math.min(x, opposite.getX());
        int maxX = Math.max(x, opposite.getX());
        int minY = Math.min(y, opposite.getY());
        int maxY = Math.max(y, opposite.getY());

        getPoints().get(0).setX(minX);
        getPoints().get(0).setY(minY);

        getPoints().get(1).setX(maxX);
        getPoints().get(1).setY(minY);

        getPoints().get(2).setX(maxX);
        getPoints().get(2).setY(maxY);

        getPoints().get(3).setX(minX);
        getPoints().get(3).setY(maxY);
    }
}
