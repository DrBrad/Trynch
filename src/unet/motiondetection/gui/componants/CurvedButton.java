package unet.motiondetection.gui.componants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CurvedButton extends JButton {

    private Color clipColor;

    public CurvedButton(){
        setMinimumSize(new Dimension(100, 40));
        setBorder(new EmptyBorder(0, 10, 0, 10));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
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
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        FontMetrics metrics = g2.getFontMetrics();
        int width = g2.getFontMetrics().charsWidth(getText().toCharArray(), 0, getText().length());

        g2.setColor(getForeground());
        g2.drawString(getText(), (getWidth()-width)/2, ((getHeight()-metrics.getHeight())/2)+metrics.getHeight()-3);
    }

    public void setClipColor(Color clipColor){
        this.clipColor = clipColor;
    }
}
