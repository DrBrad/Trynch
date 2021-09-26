package unet.motiondetection.gui.componants;

import javax.swing.*;
import java.awt.*;

public class SplashPane extends JPanel {

    public void TestPane(){

    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        GradientPaint blueToBlack = new GradientPaint(0, 0, Color.decode("#03adfc"), getWidth()/2, getHeight(), Color.decode("#52cca5"));
        g2.setPaint(blueToBlack);

        //g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}
