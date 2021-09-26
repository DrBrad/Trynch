package unet.motiondetection.gui.componants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;

public class CurvedTextField extends JTextField implements CaretListener {

    private Color clipColor;

    public CurvedTextField(){
        setBorder(new EmptyBorder(0, 10, 0, 10));
        addCaretListener(this);
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

        g2.setColor(getForeground());
        g2.drawString(getText(), 10, ((getHeight()-metrics.getHeight())/2)+metrics.getHeight()-5);

        g2.setColor(getCaretColor());
        int x = g2.getFontMetrics().charsWidth(getText().substring(0, getCaretPosition()).toCharArray(), 0, getCaretPosition());
        g2.fillRect(x+10, 9, 2, metrics.getHeight());


        if(getSelectedText() != null){
            g2.setColor(getSelectionColor());

            x = g2.getFontMetrics().charsWidth(getText().substring(0, getSelectionStart()).toCharArray(), 0, getSelectionStart());
            int width = g2.getFontMetrics().charsWidth(getSelectedText().toCharArray(), 0, getSelectedText().length());
            g2.fillRect(x+10, 9, width, metrics.getHeight());
        }
    }

    public void setClipColor(Color clipColor){
        this.clipColor = clipColor;
    }

    @Override
    public void caretUpdate(CaretEvent e){
        repaint();
    }
}
