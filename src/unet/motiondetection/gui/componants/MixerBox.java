package unet.motiondetection.gui.componants;

import javax.swing.*;
import java.awt.*;

public class MixerBox extends JPanel {

    private Color clipColor;
    private float peak = 0, rms = 0;

    public MixerBox(){
        setMaximumSize(new Dimension(100, 10));
        setPreferredSize(new Dimension(100, 10));
    }

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
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

        g2.setColor(Color.decode("#e6b800"));
        g2.fillRoundRect(0, 0, (int) (getWidth()*peak), 10, 10, 10);

        g2.setColor(Color.decode("#30b821"));
        g2.fillRoundRect(0, 0, (int) (getWidth()*rms), 10, 10, 10);
    }

    public void setClipColor(Color clipColor){
        this.clipColor = clipColor;
    }

    public void setDecibels(float peak, float rms){
        this.peak = peak;
        this.rms = rms;
        repaint();
    }
}
