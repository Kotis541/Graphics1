package gui;

import javax.swing.*;

public class Window extends JFrame{
    
    private final Panel panel;

    public Window(int width, int height) {
        setTitle("Project 1 - Kotera");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        panel = new Panel(width, height);
        add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public Panel getPanel() {
        return panel;
    }
}
