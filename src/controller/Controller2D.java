package controller;

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
        // Listener na kliknutí myší
//        panel.addMouseListener(new  MouseAdapter() {
//            @Override
//            // MouseEvent je "kurzor samotný"
//            public void mousePressed(MouseEvent e) {
//                panel.getRaster().setPixel(e.getX(), e.getY(), 0xffff00);
//                panel.repaint();
//            }
//        });
//        // horizontální přímka
//        panel.addKeyListener(new  KeyAdapter() {
//            public void keyPressed(KeyEvent e) {
//                if (e.getKeyCode() == KeyEvent.VK_L) {
//                    for (int n = 0; n <= 100; n++) {
//                        panel.getRaster().setPixel(50+n, 50, 0xffff00);
//                    }
//                    panel.repaint();
//                }
//            }
//        });
        // pomocí lineRasterizeru vykreslí přímku pomocí pružného tažení myší
        // MouseAdapter je třída, která implementuje MouseListener a MouseMotionListener
        MouseAdapter mouseAdapter = new MouseAdapter() {
            private int startX;
            private int startY;
            private boolean dragging;

            // začátek tažení myší
            @Override
            public void mousePressed(MouseEvent e) {
                startX = e.getX();
                startY = e.getY();
                dragging = true;

                panel.getRaster().clear();
                lineRasterizer.rasterize(startX, startY, startX, startY);
                panel.repaint();
            }

            // kdyý se myš pohybuje tak se vykreslí přímka od startovního bodu po aktuální pozici myši
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!dragging) {
                    return;
                }

                panel.getRaster().clear();
                lineRasterizer.rasterize(startX, startY, e.getX(), e.getY());
                panel.repaint();
            }

            // konec tažení myší
            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;
            }
        };

        panel.addMouseListener(mouseAdapter);
        panel.addMouseMotionListener(mouseAdapter);
    }
}
