package controller;

import polygon.Point;
import polygon.Polygon;
import raster.RasterBufferedImage;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerTrivial;
import view.Panel;

import java.awt.event.*;

public class Controller2D {
    private final Panel panel;
    private final LineRasterizer lineRasterizer;

    public Controller2D(Panel panel) {
        this.panel = panel;

        panel.getRaster().setPixel(50, 50, 0xffff00);

        this.lineRasterizer = new LineRasterizerTrivial(panel.getRaster());

        initListeners();
    }

    private void initListeners() {

        // pomocí lineRasterizeru vykreslí přímku pomocí pružného tažení myší
        // MouseAdapter je třída, která implementuje MouseListener a MouseMotionListener
        MouseAdapter mouseAdapter = new MouseAdapter() {
            private int startX;
            private int startY;
            private boolean drawing = false; // zda se kreslí polygon a pohybuje se myší
            private Polygon polygon;

            // začátek tažení myší
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (!drawing) {
                        // levé tlačítko myši začne kreslit polygon
                        startX = e.getX();
                        startY = e.getY();
                        // vytvoření nového polygonu
                        polygon = new Polygon();
                        polygon.addPoint(new Point(startX, startY));
                        drawing = true;
                    }
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    // pravé tlačítko myši ukončí kreslení polygonu
                    if (drawing) {
                        drawing = false;
                        ((RasterBufferedImage)panel.getRaster()).repaintPolygons();
                    }
                }
            }

            // když se myš pohybuje tak se vykreslí přímka od startovního bodu po aktuální pozici myši
            @Override
            public void mouseMoved(MouseEvent e) {
                if (!drawing) {
                    return;
                }
                // pouze animace přímky při tažení myší
                ((RasterBufferedImage)panel.getRaster()).repaintPolygons();
                lineRasterizer.rasterize(startX, startY, e.getX(), e.getY());
                panel.repaint();
            }

            // konec tažení myší
            @Override
            public void mouseReleased(MouseEvent e) {
                if (drawing) {
                    startX = e.getX();
                    startY = e.getY();
                }

                // přidání polygonu do seznamu polygonů a překreslení všech polygonů
                polygon.addPoint(new Point(e.getX(), e.getY()));
                ((RasterBufferedImage)panel.getRaster()).addPolygon(polygon);
                ((RasterBufferedImage)panel.getRaster()).repaintPolygons();
            }
        };

        panel.addMouseListener(mouseAdapter);
        panel.addMouseMotionListener(mouseAdapter);
    }

}
