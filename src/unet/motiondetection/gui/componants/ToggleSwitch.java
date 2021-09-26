package unet.motiondetection.gui.componants;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class ToggleSwitch extends JComponent {

    private boolean checked;
    private Color c = Color.decode("#ebab34");
    private ArrayList<CheckedListener> listeners = new ArrayList<>();

    public ToggleSwitch(){
        setMinimumSize(new Dimension(35, 20));

        addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e){
                setChecked(!checked);
            }

            @Override
            public void mousePressed(MouseEvent e){
            }

            @Override
            public void mouseReleased(MouseEvent e){
            }

            @Override
            public void mouseEntered(MouseEvent e){
            }

            @Override
            public void mouseExited(MouseEvent e){
            }
        });
    }

    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        int midY = (getHeight()-20)/2;
        int midX = (getWidth()-35)/2;

        if(checked){
            g2d.setColor(new Color(c.getRGB()*30/100));
            g2d.fillRoundRect(midX, midY+3, 35, 15, 15, 15);

            g2d.setColor(c);
            g2d.fillOval(midX+15, midY, 20, 20);

        }else{
            g2d.setColor(Color.decode("#bbbbbb"));
            g2d.fillRoundRect(midX, midY+3, 35, 15, 15, 15);

            g2d.setColor(Color.decode("#dddddd"));
            g2d.fillOval(midX, midY, 20, 20);
        }
    }

    public void setColor(Color c){
        this.c = c;
    }

    public void addOnCheckedListener(CheckedListener listener){
        listeners.add(listener);
    }

    public void removeOnCheckedListener(CheckedListener listener){
        if(listeners.contains(listener)){
            listeners.remove(listener);
        }
    }

    public void setChecked(boolean checked){
        this.checked = checked;
        for(CheckedListener listener : listeners){
            listener.onChecked(checked);
        }
        repaint();
    }

    public boolean isChecked(){
        return checked;
    }
}

interface CheckedListener {
    void onChecked(boolean checked);
}
