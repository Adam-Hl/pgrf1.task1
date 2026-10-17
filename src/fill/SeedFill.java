package fill;

import raster.Raster;

import java.util.ArrayList;
import java.util.Deque;

public class SeedFill implements Filler {
    private int fillColor;
    private final Raster raster;
    // ukládám všechny vyplněné pixely, abych mohl znovu vyplnit při překreslení scény
    private record FilledPixel(int x, int y, int color) {}
    private final ArrayList<FilledPixel> filledPixels = new ArrayList<>();

    public SeedFill(int fillColor, Raster raster) {
        this.raster = raster;
        this.fillColor = fillColor;
    }

    @Override
    public void fill(int x, int y) {
        if (isInBounds(x, y)) {
            fillR(x, y, fillColor);
        }
    }

    private void fillR(int x, int y, int color) {
        int backgroundColor = raster.getPixel(x, y);

        // použití deque (zásobník, LIFO) místo rekurze, aby se předešlo StackOverflowError
        Deque<int[]> pixels = new java.util.ArrayDeque<>();
        pixels.push(new int[]{x, y});

        while (!pixels.isEmpty()) {
            int[] pixel = pixels.pop();
            int px = pixel[0];
            int py = pixel[1];

            if (isInBounds(px, py)) {
                if (raster.getPixel(px, py) == backgroundColor) {
                    raster.setPixel(px, py, color);
                    filledPixels.add(new FilledPixel(px, py, color));

                    // přidání sousedních pixelů do zásobníku
                    pixels.push(new int[]{px + 1, py});
                    pixels.push(new int[]{px - 1, py});
                    pixels.push(new int[]{px, py + 1});
                    pixels.push(new int[]{px, py - 1});
                }
            }
        }
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < raster.getWidth() && y < raster.getHeight();
    }

    public void setFillColor(int color) {
        this.fillColor = color;
    }

    public void reFill() {
        for (FilledPixel pixel : filledPixels) {
            raster.setPixel(pixel.x, pixel.y, pixel.color);
        }
    }

    public void clearFilledPixels() {
        filledPixels.clear();
    }
}
