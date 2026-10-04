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

            // začátek tažení myší
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (!drawing) {
                        // levé tlačítko myši začne kreslit polygon
                        startX = e.getX();
                        startY = e.getY();
                        // vytvoření nového polygonu
                        Polygon polygon = new Polygon();
                        polygon.addPoint(new Point(startX, startY));
                        // přidání polygonu do rasteru, pokud ještě není přidán
                        if (!((RasterBufferedImage)panel.getRaster()).getPolygons().contains(polygon)) {
                            ((RasterBufferedImage) panel.getRaster()).addPolygon(polygon);
                        }
                        drawing = true;
                    } else {
                        // přenastavení startovního bodu na aktuální pozici myši pro animaci
                        startX = e.getX();
                        startY = e.getY();
                        // levé tlačítko myši přidá bod do polygonu
                        ((RasterBufferedImage)panel.getRaster()).getPolygons().get(((RasterBufferedImage)panel.getRaster()).getPolygons().size() - 1).addPoint(new Point(e.getX(), e.getY()));
                        ((RasterBufferedImage)panel.getRaster()).repaintPolygons();
                        panel.repaint();
                    }
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    // pravé tlačítko myši ukončí kreslení polygonu a uzavře ho
                    if (drawing) {
                        drawing = false;
                        ((RasterBufferedImage)panel.getRaster()).getPolygons().get(((RasterBufferedImage)panel.getRaster()).getPolygons().size() - 1).setClosed(true);
                        ((RasterBufferedImage)panel.getRaster()).repaintPolygons();
                        panel.repaint();
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
        };
        // přidání listenerů do panelu uvnitř MouseAdapteru, aby se mohlo reagovat na pohyb myši a kliknutí
        panel.addMouseListener(mouseAdapter);
        panel.addMouseMotionListener(mouseAdapter);

        // přidání KeyListeneru pro stisknutí klávesy C, která vymaže raster a všechny polygony
        KeyListener keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    // kontrola; print všech polygonů v rasteru
                    for (Polygon polygon : ((RasterBufferedImage)panel.getRaster()).getPolygons()) {
                        System.out.print("\nPolygon: ");
                        for (Point point : polygon.getPoints()) {
                            System.out.print("Point: (" + point.getX() + ", " + point.getY() + ") ");
                        }
                    }
                    // stisknutí klávesy C vymaže raster a všechny polygony
                    ((RasterBufferedImage)panel.getRaster()).getPolygons().clear();
                    panel.getRaster().clear();
                    panel.repaint();
                }
            }
        };
        panel.addKeyListener(keyListener);
    }

}
