package unet.motiondetection.gui.componants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CurvedDropDownLabel extends JLabel {

    private Color clipColor;

    public CurvedDropDownLabel(){
        setBorder(new EmptyBorder(0, 10, 0, 10));
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(clipColor);
        g2.fillRoundRect(0, 0, getWidth()+30, getHeight(), 30, 30);

        FontMetrics metrics = g2.getFontMetrics();

        g2.setColor(getForeground());
        g2.drawString(getText(), 10, ((getHeight()-metrics.getHeight())/2)+metrics.getHeight()-3);
    }

    public void setClipColor(Color clipColor){
        this.clipColor = clipColor;
    }
}
