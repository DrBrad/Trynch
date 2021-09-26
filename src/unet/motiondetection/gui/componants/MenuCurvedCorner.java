package unet.motiondetection.gui.componants;

import javax.swing.*;
import java.awt.*;

public class MenuCurvedCorner extends JPanel {

    private Color clipColor;

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        g2.setColor(clipColor);
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(getBackground());
        g2.fillRoundRect(-50, 0, getWidth()+50, getHeight(), getHeight(), getHeight());
    }

    public void setClipColor(Color clipColor){
        this.clipColor = clipColor;
    }
}
