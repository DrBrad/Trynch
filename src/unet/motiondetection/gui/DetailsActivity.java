package unet.motiondetection.gui;

import unet.motiondetection.gui.activity.Bundle;
import unet.motiondetection.gui.activity.Fragment;
import unet.motiondetection.gui.componants.PolygonView;
import unet.motiondetection.gui.componants.ResizableImageView;
import unet.motiondetection.gui.layouts.RelativeConstraints;
import unet.motiondetection.gui.layouts.RelativeLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static unet.motiondetection.Main.*;

public class DetailsActivity extends Fragment {

    @Override
    public void onCreate(Bundle bundle){
        getRoot().setLayout(new RelativeLayout());

        getRoot().setBackground(Color.decode("#4a545c"));

        JLabel title = new JLabel();
        title.setText("Welcome to Trynch");
        title.setForeground(Color.decode("#ffffff"));
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
        getRoot().add(title, new RelativeConstraints().centerHorizontally().setMarginTop(40));

        JPanel nav = new JPanel();
        nav.setLayout(new RelativeLayout());
        nav.setBackground(Color.decode("#3a444c"));
        getRoot().add(nav, new RelativeConstraints().alignParentBottom().setSize(new Dimension(RelativeConstraints.MATCH_PARENT, 100)));

        JLabel description = new JLabel();
        description.setText("Welcome to the only true theft prevention!");
        description.setForeground(Color.decode("#ffffff"));
        description.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        getRoot().add(description, new RelativeConstraints().centerHorizontally().above(nav).setMarginBottom(40));

        //IMAGE
        ResizableImageView icon = new ResizableImageView();
        icon.setMaxWidth(300);
        icon.setMaxHeight(300);
        icon.setImage(new ImageIcon(getClass().getResource("/images/icon.png")));
        getRoot().add(icon, new RelativeConstraints().centerHorizontally().setWidth(RelativeConstraints.MATCH_PARENT).above(description).below(title).setMargins(new Insets(40, 0, 40, 0)));



        PolygonView next = new PolygonView();
        next.setForeground(Color.decode("#ffffff"));
        next.setPolygon(new int[]{ 22, 45, 22,  15, 32, 15 }, new int[]{ 11, 30, 49,  42, 30, 18 });
        nav.add(next, new RelativeConstraints().alignParentRight().setSize(new Dimension(60, 60)).setMargins(new Insets(20, 0, 20, 20)));
        next.setCursor(new Cursor(Cursor.HAND_CURSOR));

        next.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e){
                mactivity = new MainActivity();
                startFragment(mactivity);
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
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
    }
}
