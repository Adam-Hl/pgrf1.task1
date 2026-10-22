package controller;

import fill.Filler;
import fill.ScanLine;
import fill.SeedFill;
import raster.Raster;
import scene.Scene;
import scene.SceneRenderer;
import shapes.Point;
import shapes.Polygon;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerTrivial;
import shapes.Segment;
import utils.IsInside;
import view.Panel;

import java.awt.event.*;
import java.util.Iterator;

public class Controller2D {
    private final Panel panel;
    private final LineRasterizer lineRasterizer;
    private final Raster raster;
    private final Scene scene;
    private final SceneRenderer sceneRenderer;
    private Filler filler;
    private final FindClosest findClosest = new FindClosest();
    private final IsInside isInside = new IsInside();

    enum Mode {
    SEGMENT,
    POLYGON,
    FILL
    }
    private Mode mode = Mode.SEGMENT; // výchozí režim

    public Controller2D(Panel panel) {
        this.panel = panel;
        int fillColor = 0x4eb896; // barva pro vyplnění
        int color = 0xc93b2b; // barva pro vykreslování tvarů
        int backgroundColor = 0x000000; // barva pozadí
        this.lineRasterizer = new LineRasterizerTrivial(panel.getRaster(), color);
        this.raster = panel.getRaster();
        this.scene = new Scene();
//        this.filler = new SeedFill(fillColor, raster);
        this.filler = new ScanLine(fillColor, raster);
        this.sceneRenderer = new SceneRenderer(scene, raster, lineRasterizer, filler);
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
                double maxDistanceDragAndDelete = 20; // maximální vzdálenost pro nazelezení vrcholu pro pohyb a mazání
                if (e.getButton() == MouseEvent.BUTTON1) {
                    startX = e.getX();
                    startY = e.getY();
                    if (!drawing) {
                        // levé tlačítko myši začne kreslit tvar
                        if (mode == Mode.SEGMENT) {
                            // vytvoření nového segmentu
                            Segment segment = new Segment(new Point(startX, startY), new Point(startX, startY));
                            scene.addSegment(segment);
                            drawing = true;
                        } else if (mode == Mode.POLYGON) {
                            // vytvoření nového polygonu
                            Polygon polygon = new Polygon();
                            polygon.addPoint(new Point(startX, startY));
                            // přidání polygonu do rasteru, pokud ještě není přidán
                            if (!scene.getPolygons().contains(polygon)) {
                                scene.addPolygon(polygon);
                            }
                            drawing = true;
                        } else if (mode == Mode.FILL) {
                            Polygon selectedPolygon = isInside.isInsidePolygon(startX, startY, scene.getPolygons());
                            ((ScanLine)filler).setPolygon(selectedPolygon); // nastaví polygon pro vyplnění
                            filler.fill(startX, startY);
                            sceneRenderer.repaintShapes();
                            panel.repaint();
                        }
                    } else {
                        // pokračování kreslení tvaru, pokud je již aktivní
                        if (mode == Mode.SEGMENT) {
                            // aktualizace koncového bodu segmentu na aktuální pozici myši
                            scene.getSegments().get(scene.getSegments().size() - 1).setEnd(new Point(e.getX(), e.getY()));
                            drawing = false; // segment je dokončen, takže se kreslení ukončí
                        } else if (mode == Mode.POLYGON) {
                            // levé tlačítko myši přidá bod do polygonu
                            scene.getPolygons().get(scene.getPolygons().size() - 1).addPoint(new Point(e.getX(), e.getY()));
                        }
                        sceneRenderer.repaintShapes();
                        panel.repaint();
                    }
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    // pravé tlačítko myši ukončí kreslení polygonu a uzavře ho
                    if (drawing) {
                        drawing = false;
                        if (mode == Mode.POLYGON) {
                            // uzavření polygonu
                            Polygon polygon = scene.getPolygons().get(scene.getPolygons().size() - 1);
                            if (polygon.getPoints().size() > 2) {
                                polygon.setClosed(true);
                            } else {
                                // odstranění polygonu, pokud má méně než 3 body (během kreslení, nikoliv při mazání bodů)
                                scene.getPolygons().remove(polygon);
                            }
                            sceneRenderer.repaintShapes();
                        }
                        panel.repaint();
                    } else {
                        // dvojité kliknutí pravým tlačítkem myši odstraní nejbližší bod tvaru
                        if (e.getClickCount() == 2) {
                            Point closestPoint = findClosest.findClosestPoint(e.getX(), e.getY(), maxDistanceDragAndDelete, scene, mode);
                            // použití iteratoru pro odstranění bodu z polygonu nebo segmentu, aby se předešlo ConcurrentModificationException (odstranění prvku z kolekce během iterace zde i uvnitř metody repaintShapes())
                            if (mode == Mode.POLYGON) {
                                Iterator<Polygon> iterator = scene.getPolygons().iterator();
                                while (iterator.hasNext()) {
                                    Polygon polygon = iterator.next();
                                    if (closestPoint != null && polygon.getPoints().contains(closestPoint)) {
                                        polygon.getPoints().remove(closestPoint);
                                        if (polygon.getPoints().size() < 3) {
                                            // odstranění polygonu, pokud má méně než 3 body
                                            iterator.remove();
                                        }
                                        sceneRenderer.repaintShapes();
                                        panel.repaint();
                                    }
                                }
                            } else if (mode == Mode.SEGMENT) {
                                Iterator<Segment> iterator = scene.getSegments().iterator();
                                while (iterator.hasNext()) {
                                    Segment segment = iterator.next();
                                    if (closestPoint != null && segment.getPoints().contains(closestPoint)) {
                                        segment.getPoints().remove(closestPoint);
                                        if (segment.getPoints().size() < 2) {
                                            // odstranění segmentu, pokud má méně než 2 body
                                            iterator.remove();
                                        }
                                        sceneRenderer.repaintShapes();
                                        panel.repaint();
                                    }
                                }
                            }
                        }
                    }
                } else if (e.getButton() == MouseEvent.BUTTON2) {
                    // prostřední tlačítko myši interaguje s bodem
                    movingPoint = true;
                    closestPoint = findClosest.findClosestPoint(e.getX(), e.getY(), maxDistanceDragAndDelete, scene, mode);
                    if (mode == Mode.POLYGON) {
                        if (e.getClickCount() == 2) {
                            // dvojité kliknutí prostředním tlačítkem myši přidá nový bod do polygonu na aktuální pozici myši a spojí ho s nejbližšímy body polygonu
                            Point closestPoint = findClosest.findClosestPoint(e.getX(), e.getY(), Double.MAX_VALUE, scene, mode);
                            Point secondClosestPoint = findClosest.findSecondClosestPoint(e.getX(), e.getY(), closestPoint, scene);
                            for (Polygon polygon : scene.getPolygons()) {
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
                                    sceneRenderer.repaintShapes();
                                    panel.repaint();
                                }
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
                sceneRenderer.repaintShapes();
                lineRasterizer.rasterize(startX, startY, e.getX(), e.getY());
                panel.repaint();
            }

            // když se myš pohybuje a je stisknuté prostřední tlačítko myši, přesune se nejbližší bod polygonu na aktuální pozici myši
            @Override
            public void mouseDragged(MouseEvent e) {
                if (movingPoint && closestPoint != null) {
                    closestPoint.setX(e.getX());
                    closestPoint.setY(e.getY());
                    sceneRenderer.repaintShapes();
                    panel.repaint();
                }
            }

            // když se kolečko myši uvolní, ukončí se přesouvání bodu polygonu
            @Override
            public void mouseReleased(MouseEvent e) {
                movingPoint = false;
            }
        };
        // přidání listenerů do panelu uvnitř MouseAdapteru, aby se mohlo reagovat na pohyb myši a kliknutí
        panel.addMouseListener(mouseAdapter);
        panel.addMouseMotionListener(mouseAdapter);

        KeyListener keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    // přidání KeyListeneru pro stisknutí klávesy C, která vymaže raster a všechny polygony
                    // kontrola; print všech tvarů v rasteru
                    for (Polygon polygon : scene.getPolygons()) {
                        System.out.print("\nPolygon: ");
                        for (Point point : polygon.getPoints()) {
                            System.out.print("Point: (" + point.getX() + ", " + point.getY() + ") ");
                        }
                    }
                    for (Segment segment : scene.getSegments()) {
                        System.out.print("\nSegment: Start: (" + segment.getStart().getX() + ", " + segment.getStart().getY() + ") End: (" + segment.getEnd().getX() + ", " + segment.getEnd().getY() + ")");
                    }
                    // stisknutí klávesy C vymaže raster a všechny tvary
                    filler.clearFilledPixels();
                    scene.getPolygons().clear();
                    scene.getSegments().clear();
                    raster.clear();
                    panel.repaint();
                }
                if (e.getKeyCode() == KeyEvent.VK_X) {
                    // přidání KeyListeneru pro stisknutí klávesy X, která změní režim mezi segmentem a polygonem
                    switch (mode) {
                        case SEGMENT -> mode = Mode.POLYGON;
                        case POLYGON -> mode = Mode.FILL;
                        case FILL -> mode = Mode.SEGMENT;
                    }
                    System.out.println("Mode changed to: " + mode);
                }
            }
        };
        panel.addKeyListener(keyListener);
    }
}
