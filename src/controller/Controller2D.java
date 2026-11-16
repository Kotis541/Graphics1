package controller;

import gui.Panel;
import java.awt.event.*;

public class Controller2D implements KeyListener {
    private final Panel panel;

    public Controller2D(Panel panel) {
        this.panel = panel;
        panel.addKeyListener(this);
        panel.setFocusable(true);
        panel.requestFocusInWindow();
    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_C) {
            panel.getRaster().clear();
            panel.repaint();
        }
    }

    public void keyTyped(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {}
}
