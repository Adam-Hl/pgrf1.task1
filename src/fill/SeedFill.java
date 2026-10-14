package fill;

import shapes.Point;

public class SeedFill implements Filler {
    private Point seed;
    private int fillColor;
    private int backgroundColor;

    @Override
    public void fill() {
        if (seed != null) {
            // TODO: jednou zpustí druhý fill a ten se rekurzivně množí
        }
    }

    public void fill(int x, int y) {
//            raster.getPixel();
        // TODO: mám barvu, když je pozadí -> chci obarvit
        // TODO: obarvit pixel a rekurzivně i pro sousedy
        // TODO: pomocí modula volat pro všechny sousedy
    }

    public void setFillColor(int color) {
        this.fillColor = color;
    }

    public void setSeed(Point seed) {
        this.seed = seed;
    }

    public void setBackgroundColor(int color) {
        // TODO: barva pozadí by se měla nastavit podle toho, na jakou barvu jsem kliknul
        this.backgroundColor = color;
    }
}
