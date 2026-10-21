package raster;

import java.awt.image.BufferedImage;
import java.util.OptionalInt;

// nový raster pro projekt
public class RasterBufferedImage implements Raster{
    // custom raster, který využívá BufferedImage pro vykreslování pixelů
    private final BufferedImage image;

    public RasterBufferedImage(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    // nový "setRGB" s kontrolou kreslení mimo raster
    public void setPixel(int x, int y, int color) {
        if (x >= 0 && 0 <= y && x < getWidth() && y < getHeight()) {
            image.setRGB(x, y, color);
        }
    }

    // getPixel() vrací OptionalInt pro případ, že by hledaný pixel byl mimo raster
    @Override
    public OptionalInt getPixel(int x, int y) {
        if (x >= 0 && y >= 0 && x < getWidth() && y < getHeight()) {
            return OptionalInt.of(image.getRGB(x, y));
        }
        return OptionalInt.empty();
    }

    @Override
    public void clear() {
        image.getGraphics().clearRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

    public BufferedImage getImage() {
        return image;
    }

}
