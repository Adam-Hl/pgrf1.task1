import controller.Controller2D;
import utils.ModeController;
import view.Window;

public class Main {
    public static void main(String[] args) {
        ModeController modeController = new ModeController();
        Window window = new Window(800, 600, modeController);
        Controller2D controller2D = new Controller2D(window.getPanel(), modeController);
        window.getToolBar().setController2D(controller2D);
        modeController.setToolBar(window.getToolBar());
    }
}