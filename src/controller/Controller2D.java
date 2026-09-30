package controller;

import rasterize.LineRasterizer;
import rasterize.LineRasterizerGraphics;
import view.Panel;

import java.awt.event.*;

public class Controller2D {
    private final Panel panel;
    private LineRasterizer lineRasterizer;

    public Controller2D(Panel panel) {
        this.panel = panel;

        panel.getRaster().setPixel(50, 50, 0xffff00);

        lineRasterizer = new LineRasterizerGraphics(panel.getRaster());

        initListeners();
    }

    private void initListeners() {
        // Listener na kliknutí myší
        panel.addMouseListener(new  MouseAdapter() {
            @Override
            // MouseEvent je "kurzor samotný"
            public void mousePressed(MouseEvent e) {
                panel.getRaster().setPixel(e.getX(), e.getY(), 0xffff00);
                panel.repaint();
            }
        });
        // horizontální přímka
        panel.addKeyListener(new  KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_L) {
                    for (int n = 0; n <= 100; n++) {
                        panel.getRaster().setPixel(50+n, 50, 0xffff00);
                    }
                    panel.repaint();
                }
            }
        });
        // pomocí lineRasterizeru vykreslí přímku ze středu k myši během posouvání
        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int startX = panel.getWidth()/2;
                int startY = panel.getHeight()/2;
                int endX = e.getX();
                int endY = e.getY();

                panel.getRaster().clear();
                lineRasterizer.rasterize(startX, startY, endX, endY);
                panel.repaint();
            }
        });
    }
}
