package unet.motiondetection.gui.activity;

import javax.swing.*;
import java.awt.*;

public class Activity extends JFrame {

    public Activity(Fragment fragment){
        this(fragment, null);
    }

    public Activity(Fragment fragment, Bundle b){
        System.setProperty("sun.java2d.opengl", "true");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1000, 600));

        fragment.setParent(this);
        fragment.onCreate(b);
    }

    public JFrame getFrame(){
        return this;
    }

    /*
    public void setVisible(boolean visible){
        setVisible(visible);
    }

    public void setTitle(String title){
        setTitle(title);
    }

    public void setSize(Dimension dimension){
        setSize(dimension);
    }

    public void setMinimumSize(Dimension dimension){
        setMinimumSize(dimension);
    }

    public void setDefaultCloseOperation(int operation){
        setDefaultCloseOperation(operation);
    }
    */
}
