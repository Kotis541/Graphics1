
import controller.Controller2D;
import gui.Window;

public class App {

    public static void main(String[] args) throws Exception {
        Window window = new Window(800, 600);
        new Controller2D(window.getPanel());
    }
}
