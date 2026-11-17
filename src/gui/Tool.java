package gui;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Tool extends JPanel {


    private final JButton btnLine;
    private final JButton btnPolygon;
    private final JButton btnScanline;
    private final JButton btnClip;
    private final JButton btnClear;

    public Tool() {
        btnLine = new JButton("line");
        btnPolygon = new JButton("polygon");
        btnScanline = new JButton("scanline");
        btnClip = new JButton("clip");
        btnClear = new JButton("clear");

        btnLine.setFocusable(false);
        btnPolygon.setFocusable(false);
        btnScanline.setFocusable(false);
        btnClip.setFocusable(false);
        btnClear.setFocusable(false);

        add(btnLine);
        add(btnPolygon);
        add(btnScanline);
        add(btnClip);
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

    public JButton getBtnScanline() {
        return btnScanline;
    }

    public JButton getBtnClip() {
        return btnClip;
    }
}
