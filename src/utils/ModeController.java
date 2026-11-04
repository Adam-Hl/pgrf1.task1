package utils;

import view.ToolBar;

import javax.tools.Tool;

// třída sloužící k řízení módu aplikace (segment, polygon, fill) a informování posluchačů o změně módu
public class ModeController {
    private ToolBar toolBar;

    public enum Mode {
        SEGMENT,
        POLYGON,
        FILL
    }

    private Mode mode = Mode.SEGMENT;

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        if (this.mode == mode) {
            return;
        }

        this.mode = mode;
        toolBar.updateModeLabel(mode);
    }

    public void setToolBar(ToolBar toolBar) {
        this.toolBar = toolBar;
    }
}