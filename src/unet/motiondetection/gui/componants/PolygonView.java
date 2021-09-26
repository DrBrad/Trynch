package unet.motiondetection.gui.componants;

import javax.swing.*;
import java.awt.*;

public class PolygonView extends JLabel {

    private int[] x, y;

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        if(isOpaque()){
            g2.setColor(getBackground());
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        g2.setColor(getForeground());
        if(x != null && y != null){
            g2.fillPolygon(x, y, x.length);
        }
    }

    public void setPolygon(int[] x, int[] y){
        this.x = x;
        this.y = y;
        repaint();
    }
}
