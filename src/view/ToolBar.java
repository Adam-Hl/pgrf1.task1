package view;
import controller.Controller2D;
import utils.ModeController;

import javax.swing.*;

public class ToolBar extends JToolBar {
    private JLabel modeLabel = new JLabel();
    private Controller2D controller2D;

    // toolbar obsahuje tlačítka pro výběr nástrojů
    public ToolBar(ModeController modeController) {
        modeLabel.setText("Current mode: " + modeController.getMode().toString());

        // Přidání tlačítek do toolbaru
        JButton bSegment = new JButton("Segment");
        bSegment.addActionListener(e -> {
            modeController.setMode(ModeController.Mode.SEGMENT);
        });
        add(bSegment);

        JButton bPolygon = new JButton("Polygon");
        bPolygon.addActionListener(e -> {
            modeController.setMode(ModeController.Mode.POLYGON);
        });
        add(bPolygon);

        JButton bFill = new JButton("Fill");
        bFill.addActionListener(e -> {
            modeController.setMode(ModeController.Mode.FILL);
        });
        add(bFill);

        JButton bCSutherland = new JButton("Cohen-Sutherland");
        bCSutherland.addActionListener(e -> {
            controller2D.addCroppingRectangle();
        });
        add(bCSutherland);

        JButton bClear = new JButton("Clear");
        bClear.addActionListener(e -> {
            controller2D.clear();
        });
        add(bClear);

        // pružné roztažení mezi tlačítky a štítkem
        add(Box.createHorizontalGlue());
        add(modeLabel);

    }

    public void updateModeLabel(ModeController.Mode mode) {
        modeLabel.setText("Current mode: " + mode.toString());
    }

    public void setController2D(Controller2D controller2D) {
        this.controller2D = controller2D;
    }
}
