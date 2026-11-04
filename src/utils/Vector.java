package utils;

public class Vector {
    private int x;
    private int y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector getNormalVector() {
        // vrací normálový vektor
        return new Vector(-y, x);
    }

    public int dotProduct(Vector other) {
        // vrací skalární součin dvou vektorů
        return this.x * other.x + this.y * other.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
