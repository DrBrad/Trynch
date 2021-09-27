package unet.motiondetection.gui;

import unet.motiondetection.gui.activity.Bundle;
import unet.motiondetection.gui.activity.Fragment;
import unet.motiondetection.gui.componants.CurvedButton;
import unet.motiondetection.gui.componants.CurvedTextField;
import unet.motiondetection.gui.layouts.RelativeConstraints;
import unet.motiondetection.gui.layouts.RelativeLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static unet.motiondetection.Config.savePreference;

public class PopupActivity extends Fragment {

    @Override
    public void onCreate(Bundle bundle){
        getRoot().setBackground(Color.decode("#ffffff"));
        getRoot().setLayout(new RelativeLayout());

        JLabel title = new JLabel();
        title.setText(bundle.getString("t"));
        //title.setBorder(new EmptyBorder(0, 20, 0, 20));
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        getRoot().add(title, new RelativeConstraints().centerHorizontally().setMargins(new Insets(30, 0, 30, 0)));

        CurvedTextField field = new CurvedTextField();
        field.setText(bundle.get("f")+"");
        field.setClipColor(Color.decode("#ffffff"));
        field.setBackground(Color.decode("#d6d6d6"));
        field.setCaretColor(Color.decode("#ebab34"));
        field.setSelectionColor(new Color(235, 171, 52, 100));
        field.setBorder(new EmptyBorder(10, 10, 10, 10));

        field.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));

        getRoot().add(field, new RelativeConstraints().below(title).centerHorizontally().setWidth(360).setMargins(new Insets(30, 0, 30, 0)));

        field.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e){
                char c = e.getKeyChar();
                if(!((c >= '0') && (c <= '9') ||
                        c == KeyEvent.VK_BACK_SPACE ||
                        c == KeyEvent.VK_DELETE)){
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e){
            }

            @Override
            public void keyReleased(KeyEvent e){
            }
        });



        CurvedButton cancel = new CurvedButton();
        cancel.setText("Cancel");
        cancel.setClipColor(Color.decode("#ffffff"));
        cancel.setBackground(Color.decode("#d6d6d6"));
        cancel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

        getRoot().add(cancel, new RelativeConstraints().below(field).alignParentLeft().setSize(new Dimension(170, 40)).setMargins(new Insets(30, 20, 30, 0)));

        cancel.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e){
                getFrame().dispose();
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


        CurvedButton submit = new CurvedButton();
        submit.setText("Submit");
        submit.setForeground(Color.decode("#ffffff"));
        submit.setClipColor(Color.decode("#ffffff"));
        submit.setBackground(Color.decode("#3a444c"));
        submit.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

        getRoot().add(submit, new RelativeConstraints().below(field).alignParentRight().setSize(new Dimension(170, 40)).setMargins(new Insets(30, 0, 30, 20)));

        submit.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e){
                savePreference(bundle.getString("id"), field.getText());
                getFrame().dispose();
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
