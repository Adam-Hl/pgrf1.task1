package view;

import utils.ModeController;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    private final Panel panel;
    private final ToolBar toolBar;

    public Window(int width, int height, ModeController modeController) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("PGRF1 2024/2025");
        setVisible(true);

        panel = new Panel(width, height);
        add(panel);

        toolBar = new ToolBar(modeController) ;
        add(toolBar, BorderLayout.NORTH);
        pack();

        panel.setFocusable(true);
        panel.grabFocus();
    }

    public Panel getPanel() {
        return panel;
    }

    public ToolBar getToolBar() {
        return toolBar;
    }
}
