package unet.motiondetection.gui;

import org.json.JSONObject;
import unet.motiondetection.gui.activity.Bundle;
import unet.motiondetection.gui.activity.Fragment;
import unet.motiondetection.gui.componants.CurvedButton;
import unet.motiondetection.gui.componants.CurvedLabel;
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

import static unet.motiondetection.Config.*;
import static unet.motiondetection.Main.*;

public class DateActivity extends Fragment {

    @Override
    public void onCreate(Bundle bundle){
        getRoot().setBackground(Color.decode("#ffffff"));
        getRoot().setLayout(new RelativeLayout());

        savePreference("scheduleDetails", null);

        JLabel title = new JLabel();
        title.setText("Set Schedule");
        //title.setBorder(new EmptyBorder(0, 20, 0, 20));
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        getRoot().add(title, new RelativeConstraints().centerHorizontally().setMargins(new Insets(30, 0, 30, 0)));



        JPanel pane = new JPanel();
        pane.setLayout(new FlowLayout());
        //pane.setBackground(Color.decode("#ff0000"));
        pane.setBackground(Color.decode("#ffffff"));
        getRoot().add(pane, new RelativeConstraints().below(title).centerHorizontally().setSize(new Dimension(RelativeConstraints.MATCH_PARENT, 60)).setMargins(new Insets(30, 0, 15, 0)));

        JSONObject json = new JSONObject(readStringPreference("scheduleDetails", "{" +
                "\"d\":[false,true,true,true,true,true,false]," +
                "\"sh\":12," +
                "\"sm\":00," +
                "\"eh\":12," +
                "\"em\":00," +
                "\"s\":true," +
                "\"a\":false" +
                "}"));


        char[] s = { 's', 'e' };

        for(int i = 0; i < 2; i++){
            JTextField h = new JTextField();
            h.setPreferredSize(new Dimension(50, 50));
            h.setText(json.getInt(s[i]+"h")+"");
            h.setBackground(Color.decode("#ffffff"));
            h.setBorder(new EmptyBorder(0, 10, 0, 0));
            h.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
            pane.add(h);

            final int tmp = i;

            h.addKeyListener(new KeyListener(){
                @Override
                public void keyTyped(KeyEvent e){
                    char c = e.getKeyChar();
                    if(!((c >= '0') && (c <= '9') ||
                            c == KeyEvent.VK_BACK_SPACE ||
                            c == KeyEvent.VK_DELETE)){
                        e.consume();
                    }

                    if(h.getText().length() == 0){
                        h.setText("1");
                    }
                }

                @Override
                public void keyPressed(KeyEvent e){
                }

                @Override
                public void keyReleased(KeyEvent e){
                    json.put(s[tmp]+"h", Integer.parseInt(h.getText()));
                }
            });


            JTextField m = new JTextField();
            m.setPreferredSize(new Dimension(50, 50));
            m.setText(json.getInt(s[i]+"m")+"");
            m.setBackground(Color.decode("#ffffff"));
            m.setBorder(new EmptyBorder(0, 0, 0, 10));
            m.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
            pane.add(m);

            m.addKeyListener(new KeyListener(){
                @Override
                public void keyTyped(KeyEvent e){
                    char c = e.getKeyChar();
                    if(!((c >= '0') && (c <= '9') ||
                            c == KeyEvent.VK_BACK_SPACE ||
                            c == KeyEvent.VK_DELETE)){
                        e.consume();
                    }

                    if(m.getText().length() == 0){
                        m.setText("1");
                    }
                }

                @Override
                public void keyPressed(KeyEvent e){
                }

                @Override
                public void keyReleased(KeyEvent e){
                    json.put(s[tmp]+"m", Integer.parseInt(m.getText()));
                }
            });
        }







        JPanel pane2 = new JPanel();
        pane2.setLayout(new FlowLayout());
        pane2.setBackground(Color.decode("#ffffff"));
        getRoot().add(pane2, new RelativeConstraints().below(pane).centerHorizontally().setSize(new Dimension(RelativeConstraints.MATCH_PARENT, 50)).setMargins(new Insets(15, 0, 30, 0)));


        String[] days = { "S", "M", "T", "W", "T", "F", "S" };
        int i = 0;
        for(String day : days){
            CurvedButton d = new CurvedButton();
            d.setPreferredSize(new Dimension(30, 30));
            d.setText(day);
            d.setClipColor(Color.decode("#ffffff"));
            d.setBackground(Color.decode("#ffffff"));

            if(json.getJSONArray("d").getBoolean(i)){
                d.setForeground(Color.decode("#ffffff"));
                d.setBackground(Color.decode("#3a444c"));

            }else{
                d.setForeground(Color.decode("#000000"));
                d.setBackground(Color.decode("#ffffff"));
            }


            d.setBorder(new EmptyBorder(10, 10, 10, 10));
            d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
            d.setCursor(new Cursor(Cursor.HAND_CURSOR));
            pane2.add(d);

            final int tmp = i;

            d.addMouseListener(new MouseListener(){
                private boolean clicked = json.getJSONArray("d").getBoolean(tmp);

                @Override
                public void mouseClicked(MouseEvent e){
                    if(clicked){
                        d.setForeground(Color.decode("#000000"));
                        d.setBackground(Color.decode("#ffffff"));

                    }else{
                        d.setForeground(Color.decode("#ffffff"));
                        d.setBackground(Color.decode("#3a444c"));
                    }
                    json.getJSONArray("d").put(tmp, !clicked);

                    clicked = !clicked;
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

            i++;
        }



        JPanel pane3 = new JPanel();
        pane3.setLayout(new FlowLayout());
        pane3.setBackground(Color.decode("#ffffff"));
        getRoot().add(pane3, new RelativeConstraints().below(pane2).centerHorizontally().setSize(new Dimension(RelativeConstraints.MATCH_PARENT, 50)).setMargins(new Insets(15, 0, 30, 0)));


        CurvedButton script = new CurvedButton();
        script.setPreferredSize(new Dimension(100, 40));
        script.setText("Script");
        script.setClipColor(Color.decode("#ffffff"));
        script.setBackground(Color.decode("#d6d6d6"));

        if(json.getBoolean("s")){
            script.setForeground(Color.decode("#ffffff"));
            script.setBackground(Color.decode("#3a444c"));

        }else{
            script.setForeground(Color.decode("#000000"));
            script.setBackground(Color.decode("#d6d6d6"));
        }

        script.setBorder(new EmptyBorder(10, 10, 10, 10));
        script.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        script.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pane3.add(script);



        CurvedButton auto = new CurvedButton();
        auto.setPreferredSize(new Dimension(100, 40));
        auto.setText("Auto");
        auto.setClipColor(Color.decode("#ffffff"));
        auto.setBackground(Color.decode("#d6d6d6"));

        if(json.getBoolean("a")){
            auto.setForeground(Color.decode("#ffffff"));
            auto.setBackground(Color.decode("#3a444c"));

        }else{
            auto.setForeground(Color.decode("#000000"));
            auto.setBackground(Color.decode("#d6d6d6"));
        }

        auto.setBorder(new EmptyBorder(10, 10, 10, 10));
        auto.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        auto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pane3.add(auto);


        script.addMouseListener(new MouseListener(){
            private boolean clicked = json.getBoolean("s");

            @Override
            public void mouseClicked(MouseEvent e){
                if(clicked){
                    script.setForeground(Color.decode("#000000"));
                    script.setBackground(Color.decode("#ffffff"));

                }else{
                    script.setForeground(Color.decode("#ffffff"));
                    script.setBackground(Color.decode("#3a444c"));
                }
                json.put("s", !clicked);

                clicked = !clicked;
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

        auto.addMouseListener(new MouseListener(){
            private boolean clicked = json.getBoolean("a");

            @Override
            public void mouseClicked(MouseEvent e){
                if(clicked){
                    auto.setForeground(Color.decode("#000000"));
                    auto.setBackground(Color.decode("#ffffff"));

                }else{
                    auto.setForeground(Color.decode("#ffffff"));
                    auto.setBackground(Color.decode("#3a444c"));
                }
                json.put("a", !clicked);

                clicked = !clicked;
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






        CurvedButton cancel = new CurvedButton();
        cancel.setText("Cancel");
        cancel.setClipColor(Color.decode("#ffffff"));
        cancel.setBackground(Color.decode("#d6d6d6"));
        cancel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

        getRoot().add(cancel, new RelativeConstraints().below(pane3).alignParentLeft().setSize(new Dimension(170, 40)).setMargins(new Insets(30, 20, 30, 0)));

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

        getRoot().add(submit, new RelativeConstraints().below(pane3).alignParentRight().setSize(new Dimension(170, 40)).setMargins(new Insets(30, 0, 30, 20)));

        submit.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e){
                savePreference("scheduleDetails", json.toString());
                scheduledTask.timerUpdate();
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
