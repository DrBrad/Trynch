package unet.motiondetection.gui.componants;

import javax.swing.*;
import java.awt.*;

public class ResizableImageView extends JLabel {

    private ImageIcon image, resized;
    private int maxWidth = 200, maxHeight = 200;

    public ResizableImageView(){
    }

    public void setImage(ImageIcon image){
        this.image = image;
        resized = null;

        if(getWidth() == 0 || getHeight() == 0){
            setPreferredSize(new Dimension(maxWidth, maxHeight));
            setMinimumSize(new Dimension(maxWidth, maxHeight));
        }

        repaint();
    }

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

        if(image != null){
            Dimension size = getScaledDimension(new Dimension(getWidth(), getHeight()));
            if(size.width > maxWidth || size.height > maxHeight){
                size = new Dimension(maxWidth, maxHeight);
            }

            if(resized == null || size.width != resized.getIconWidth() || size.height != resized.getIconHeight()){
                resized = scaleImage(image, size);
            }

            int x = (size.width < getWidth()) ? (getWidth()-size.width)/2 : 0;
            int y = (size.height < getHeight()) ? (getHeight()-size.height)/2 : 0;

            g2.drawImage(resized.getImage(), x, y, size.width, size.height, null);
        }
    }

    public void setMaxWidth(int maxWidth){
        this.maxWidth = maxWidth;
    }

    public void setMaxHeight(int maxHeight){
        this.maxHeight = maxHeight;
    }

    private Dimension getScaledDimension(Dimension boundary){
        double widthRatio = boundary.getWidth()/image.getIconWidth();
        double heightRatio = boundary.getHeight()/image.getIconHeight();
        double ratio = Math.min(widthRatio, heightRatio);

        return new Dimension((int) (image.getIconWidth()*ratio), (int) (image.getIconHeight()*ratio));
    }

    private ImageIcon scaleImage(ImageIcon icon, Dimension boundary){
        return new ImageIcon(icon.getImage().getScaledInstance(boundary.width, boundary.height, Image.SCALE_DEFAULT));
    }
}
