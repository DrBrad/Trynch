package unet.motiondetection.gui.componants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CurvedDropDownButton extends JButton {

    private Color clipColor;

    public CurvedDropDownButton(){
        setBorder(new EmptyBorder(0, 10, 0, 10));
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        g2.setColor(clipColor);
        g2.fillRect(30, 0, getWidth()-30, getHeight());

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        g2.setColor(getForeground());
        int size = getWidth()/8;
        g2.fillPolygon(new int[]{ size*3, size*5, size*4 }, new int[]{ size*3, size*3, size*5 }, 3);
    }

    public void setClipColor(Color clipColor){
        this.clipColor = clipColor;
    }
}
