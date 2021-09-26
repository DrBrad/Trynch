package unet.motiondetection.gui;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import unet.motiondetection.gui.activity.Bundle;
import unet.motiondetection.gui.activity.Fragment;
import unet.motiondetection.gui.componants.*;
import unet.motiondetection.gui.layouts.RelativeConstraints;
import unet.motiondetection.gui.layouts.RelativeLayout;
import unet.motiondetection.listeners.SoundDetector;
import unet.motiondetection.listeners.SoundListener;

import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import static unet.motiondetection.Config.*;

public class WebcamActivity extends Fragment {

    private WebcamPanel wp;
    private SoundDetector sd;

    @Override
    public void onCreate(Bundle bundle){
        getRoot().setBackground(Color.decode("#ffffff"));
        getRoot().setLayout(new RelativeLayout());

        getFrame().addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent windowEvent){
                if(sd != null){
                    sd.stop();
                }

                if(wp != null){
                    wp.stop();
                }
            }

            @Override
            public void windowClosed(WindowEvent windowEvent){
                if(sd != null){
                    sd.stop();
                }

                if(wp != null){
                    wp.stop();
                }
            }
        });

        JPanel bar = new JPanel();
        //bar.setLayout(new RelativeLayout());
        bar.setLayout(new GridBagLayout());
        bar.setMinimumSize(new Dimension(100, 100));
        bar.setPreferredSize(new Dimension(100, 100));
        bar.setBackground(Color.decode("#ffffff"));
        getRoot().add(bar, new RelativeConstraints().alignParentBottom().setWidth(RelativeConstraints.MATCH_PARENT));


        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(0, 20, 0, 20);

        String cam = readStringPreference("cameraChoice", null);
        List<String> cams = new ArrayList<>();
        int selected = 0;

        for(Webcam w : Webcam.getWebcams()){
            cams.add(w.getName());
            if(w.getName().equals(cam)){
                selected = cams.size()-1;
            }
        }

        JComboBox<String> cb = new JComboBox<>(cams.toArray(new String[0]));
        cb.setUI(new CustomDropDown());
        cb.setPreferredSize(new Dimension(300, 40));
        cb.setBorder(new EmptyBorder(0, 0, 0, 0));
        cb.setBackground(Color.decode("#e6e6e6"));
        cb.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        cb.setRenderer(new CellRenderer());
        cb.setSelectedIndex(selected);
        //bar.add(cb, new RelativeConstraints().centerVertically().toLeftOf(mb).setMarginRight(30));
        bar.add(cb, constraints);

        if(!cams.isEmpty()){
            if(cam == null){
                wp = new WebcamPanel(Webcam.getDefault());
            }else{
                wp = new WebcamPanel(Webcam.getWebcamByName(cam));
            }
            wp.getWebcam().setCustomViewSizes(new Dimension(1000, 500));
            getRoot().add(wp, new RelativeConstraints().setWidth(RelativeConstraints.MATCH_PARENT).above(bar).alignParentTop());

            new Thread(new Runnable(){
                @Override
                public void run(){
                    wp.start();
                }
            }).start();

            cb.addItemListener(new ItemListener(){
                @Override
                public void itemStateChanged(ItemEvent e){
                    if(e.getID() == ItemEvent.ITEM_STATE_CHANGED && e.getStateChange() == ItemEvent.SELECTED){

                        new Thread(new Runnable(){
                            @Override
                            public void run(){
                                wp.stop();
                                getRoot().remove(wp);

                                wp = new WebcamPanel(Webcam.getWebcams().get(cb.getSelectedIndex()));
                                wp.getWebcam().setCustomViewSizes(new Dimension(1000, 500));
                                getRoot().add(wp, new RelativeConstraints().setWidth(RelativeConstraints.MATCH_PARENT).above(bar).alignParentTop());
                                wp.start();

                                wp.revalidate();
                                wp.repaint();
                            }
                        }).start();
                    }
                }
            });
        }





        String mic = readStringPreference("microphoneChoice", null);
        if(mic == null){
            sd = new SoundDetector();
        }else{
            sd = new SoundDetector(mic);
        }
        sd.start();

        List<String> mics = new ArrayList<>();
        selected = 0;

        for(Mixer.Info m : sd.getMicrophones()){
            mics.add(m.getName());
            if(m.getName().equals(mic)){
                selected = mics.size()-1;
            }
        }

        constraints.gridx = 1;
        JComboBox<String> mb = new JComboBox<>(mics.toArray(new String[0]));
        mb.setUI(new CustomDropDown());
        mb.setPreferredSize(new Dimension(300, 40));
        mb.setBorder(new EmptyBorder(0, 0, 0, 0));
        mb.setBackground(Color.decode("#e6e6e6"));
        mb.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        mb.setRenderer(new CellRenderer());
        mb.setSelectedIndex(selected);
        bar.add(mb, constraints);
        //bar.add(mb, new RelativeConstraints().centerInParent());



        constraints.gridx = 2;
        MixerBox mib = new MixerBox();
        mib.setMinimumSize(new Dimension(200, 10));
        mib.setPreferredSize(new Dimension(200, 10));
        mib.setClipColor(Color.decode("#ffffff"));
        mib.setBackground(Color.decode("#cccccc"));
        //bar.add(mib, new RelativeConstraints().centerVertically().toRightOf(mb).setWidth(200).setMarginLeft(30));
        bar.add(mib, constraints);


        sd.addSoundListener(new SoundListener(){
            @Override
            public void onSenseDecibels(float peak, float rms){
                mib.setDecibels(peak, rms);
            }

            @Override
            public void onSenseDecibelsPeak(int peak){
            }
        });

        mb.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(e.getID() == ItemEvent.ITEM_STATE_CHANGED && e.getStateChange() == ItemEvent.SELECTED){
                    sd.setMicrophone(sd.getMicrophones().get(mb.getSelectedIndex()));
                }
            }
        });








        constraints.gridx = 3;
        CurvedButton submit = new CurvedButton();
        submit.setText("Submit");
        submit.setForeground(Color.decode("#ffffff"));
        submit.setClipColor(Color.decode("#ffffff"));
        submit.setBackground(Color.decode("#3a444c"));
        submit.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        submit.setPreferredSize(new Dimension(170, 40));
        bar.add(submit, constraints);

        submit.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e){
                savePreference("cameraChoice", Webcam.getWebcams().get(cb.getSelectedIndex()).getName());
                savePreference("microphoneChoice", mics.get(mb.getSelectedIndex()));

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
        if(sd != null){
            sd.stop();
        }

        if(wp != null){
            wp.stop();
        }
    }

    public class CellRenderer implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean selected, boolean expanded){
            list.setSelectionBackground(Color.decode("#ffffff"));

            CurvedDropDownLabel label = new CurvedDropDownLabel();
            label.setPreferredSize(new Dimension(200, 40));
            label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
            label.setOpaque(true);

            if(selected){
                label.setBackground(Color.decode("#e6e6e6"));
                label.setClipColor(Color.decode("#e6e6e6"));
            }else{
                label.setBackground(Color.decode("#ffffff"));
                label.setClipColor(Color.decode("#ffffff"));
            }

            label.setText(value.toString());

            return label;
        }
    }
}
