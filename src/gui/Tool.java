package gui;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Tool extends JPanel {


    private final JButton btnLine;
    private final JButton btnPolygon;
    private final JButton btnClear;

    public Tool() {
        btnLine = new JButton("line");
        btnPolygon = new JButton("polygon");
        btnClear = new JButton("clear");

        btnLine.setFocusable(false);
        btnPolygon.setFocusable(false);
        btnClear.setFocusable(false);

        add(btnLine);
        add(btnPolygon);
        add(btnClear);
    }

    public JButton getBtnClear() {
        return btnClear;
    }

    public JButton getBtnLine() {
        return btnLine;
    }

    public JButton getBtnPolygon() {
        return btnPolygon;
    }
}
