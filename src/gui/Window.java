package gui;

import javax.swing.*;
import java.awt.BorderLayout;

public class Window extends JFrame{
    
    private final Panel panel;
    private final Tool tool;

    public Window(int width, int height) {
        tool = new Tool();

        setTitle("Project 1 - Kotera");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        panel = new Panel(width, height);
        add(tool, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public Panel getPanel() {
        return panel;
    }

    public Tool getTool() {
        return tool;
    }
}
