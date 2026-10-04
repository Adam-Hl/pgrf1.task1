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
            private boolean movingPoint = false; // zda se přesouvá bod polygonu
            private Point closestPoint; // nejbližší bod polygonu k kliknuté pozici myši

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
                    } else {
                        // dvojité kliknutí pravým tlačítkem myši odstraní nejbližší bod polygonu
                        if (e.getClickCount() == 2) {
                            Point closestPoint = findClosestPoint(e.getX(), e.getY());
                            for (Polygon polygon : ((RasterBufferedImage)panel.getRaster()).getPolygons()) {
                                if (polygon.getPoints().contains(closestPoint)) {
                                    polygon.getPoints().remove(closestPoint);
                                    ((RasterBufferedImage)panel.getRaster()).repaintPolygons();
                                    panel.repaint();
                                }
                            }
                        }
                    }
                } else if (e.getButton() == MouseEvent.BUTTON2) {
                    // prostřední tlačítko myši interaguje s bodem polygonu
                    movingPoint = true;
                    closestPoint = findClosestPoint(e.getX(), e.getY());
                    if (e.getClickCount() == 2) {
                        // dvojité kliknutí prostředním tlačítkem myši přidá nový bod do polygonu na aktuální pozici myši a spojí ho s nejbližšímy body polygonu
                        Point closestPoint = findClosestPoint(e.getX(), e.getY());
                        Point secondClosestPoint = findSecondClosestPoint(e.getX(), e.getY(), closestPoint);
                        for (Polygon polygon : ((RasterBufferedImage)panel.getRaster()).getPolygons()) {
                            if (closestPoint != null && polygon.getPoints().contains(closestPoint)) {
                                // vložení nového bodu mezi nejbližší bod a druhý nejbližší bod polygonu
                                int indexClosest = polygon.getPoints().indexOf(closestPoint);
                                int indexSecondClosest = polygon.getPoints().indexOf(secondClosestPoint);
                                // pokud je nejbližší bod poslední bod v polygonu, přidá se nový bod na konec polygonu
                                if (indexClosest == polygon.getPoints().size() -1 || indexSecondClosest == polygon.getPoints().size() -1) {
                                    polygon.getPoints().add(new Point(e.getX(), e.getY()));
                                    // jinak se nový bod přidá mezi nejbližší bod a druhý nejbližší bod polygonu podle jejich indexů
                                } else if (indexClosest < indexSecondClosest ) {
                                    polygon.getPoints().add(indexClosest + 1, new Point(e.getX(), e.getY()));
                                } else {
                                    polygon.getPoints().add(indexSecondClosest + 1, new Point(e.getX(), e.getY()));
                                }
                                ((RasterBufferedImage)panel.getRaster()).repaintPolygons();
                                panel.repaint();
                            }
                        }
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

            // když se myš pohybuje a je stisknuté prostřední tlačítko myši, přesune se nejbližší bod polygonu na aktuální pozici myši
            @Override
            public void mouseDragged(MouseEvent e) {
                if (movingPoint && closestPoint != null) {
                    closestPoint.setX(e.getX());
                    closestPoint.setY(e.getY());
                    ((RasterBufferedImage)panel.getRaster()).repaintPolygons();
                    panel.repaint();
                }
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
    private Point findClosestPoint(int mouseX, int mouseY) {
        double closestDistance = Double.MAX_VALUE;
        Point closestPoint = null;
        // hledání nejbližšího bodu polygonu k aktuální pozici myši pomocí Pythagorovy věty
        for (Polygon polygon : ((RasterBufferedImage)panel.getRaster()).getPolygons()) {
            for (Point point : polygon.getPoints()) {
                double distance = Math.sqrt(Math.pow(point.getX() - mouseX, 2) + Math.pow(point.getY() - mouseY, 2));
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestPoint = point;
                }
            }
        }
        return closestPoint;
    }

    private Point findSecondClosestPoint(int mouseX, int mouseY, Point closestPoint) {
        double secondClosestDistance = Double.MAX_VALUE;
        Point secondClosestPoint = null;
        // hledání druhého nejbližšího bodu polygonu k aktuální pozici myši pomocí Pythagorovy věty
        for (Polygon polygon : ((RasterBufferedImage)panel.getRaster()).getPolygons()) {
            for (Point point : polygon.getPoints()) {
                if (point == closestPoint) {
                    continue; // přeskočí již nalezený nejbližší bod
                } else if (!(polygon.getPoints().indexOf(point) == polygon.getPoints().indexOf(closestPoint) + 1 || polygon.getPoints().indexOf(point) == polygon.getPoints().indexOf(closestPoint) - 1)) {
                    continue; // přeskočí body, které nejsou vedle nejbližšího bodu v polygonu v ramci seznamu
                }
                double distance = Math.sqrt(Math.pow(point.getX() - mouseX, 2) + Math.pow(point.getY() - mouseY, 2));
                if (distance < secondClosestDistance) {
                    secondClosestDistance = distance;
                    secondClosestPoint = point;
                }
            }
        }
        return secondClosestPoint;
    }

}
