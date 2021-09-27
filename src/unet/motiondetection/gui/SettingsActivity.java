package unet.motiondetection.gui;

import org.json.JSONArray;
import org.json.JSONObject;
import unet.motiondetection.Main;
import unet.motiondetection.gui.activity.Activity;
import unet.motiondetection.gui.activity.Bundle;
import unet.motiondetection.gui.activity.Fragment;
import unet.motiondetection.gui.componants.PolygonView;
import unet.motiondetection.gui.componants.ToggleSwitch;
import unet.motiondetection.gui.layouts.RelativeConstraints;
import unet.motiondetection.gui.layouts.RelativeLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static unet.motiondetection.Config.*;

public class SettingsActivity extends Fragment {

    private boolean disableBack;

    @Override
    public void onCreate(Bundle bundle){
        getRoot().setLayout(new RelativeLayout());

        JPanel title = new JPanel();
        title.setLayout(new RelativeLayout());
        title.setBackground(Color.decode("#3a444c"));
        getRoot().add(title, new RelativeConstraints().setSize(new Dimension(RelativeConstraints.MATCH_PARENT, 60)));



        PolygonView back = new PolygonView();
        back.setForeground(Color.decode("#ffffff"));
        back.setPolygon(new int[]{ 24, 13, 24,  27, 19, 27 }, new int[]{ 10, 20, 30,  26, 20, 14 });
        title.add(back, new RelativeConstraints().centerVertically().setSize(new Dimension(40, 40)).setMargins(new Insets(10, 10, 10, 10)));
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));

        back.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(!disableBack){
                    onStop();
                }
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

        JLabel titleText = new JLabel();
        titleText.setText("Settings");
        titleText.setForeground(Color.decode("#ffffff"));
        titleText.setBorder(new EmptyBorder(0, 20, 0, 20));
        titleText.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        title.add(titleText, new RelativeConstraints().toRightOf(back).centerVertically());



        //LISTVIEW
        JList list = new JList();
        list.setBackground(Color.decode("#ffffff"));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);

        DefaultListModel model = new DefaultListModel();
        addModel(model);
        list.setCellRenderer(new CellRenderer());
        list.setModel(model);


        JScrollPane scrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBackground(Color.decode("#ffffff"));
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        scrollPane.setHorizontalScrollBar(null);
        getRoot().add(scrollPane, new RelativeConstraints().below(title).alignParentBottom().setWidth(RelativeConstraints.MATCH_PARENT));

        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI(){
            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r){
                Graphics2D graphics = (Graphics2D) g;
                graphics.setColor(Color.decode("#777777"));
                graphics.fillRoundRect(r.width-11, r.y, 10, r.height, 10, 10);
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle r){
                Graphics2D graphics = (Graphics2D) g;
                graphics.setColor(Color.decode("#ffffff"));
                graphics.fillRect(r.x, r.y, r.width, r.height);
            }

            @Override
            protected JButton createDecreaseButton(int orientation){
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation){
                return createZeroButton();
            }

            private JButton createZeroButton(){
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }
        });

        list.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(!disableBack){
                    JSONObject json = (JSONObject) list.getSelectedValue();
                    if(json.getInt("q") > 2){
                        savePreference(json.getString("id"), !readBooleanPreference(json.getString("id"), json.getBoolean("f")));

                    }else if(json.has("p")){
                        Bundle b = new Bundle();
                        Activity activity = null;

                        if(json.getInt("p") == 0){ //WE HAD SOMETHING FOR THIS I KNOW THAT...
                            b.putString("t", json.getString("t"));
                            b.putInt("f", readIntegerPreference(json.getString("id"), json.getInt("f")));
                            b.putString("id", json.getString("id"));

                            activity = new Activity(new PopupActivity(), b);
                            activity.setSize(new Dimension(400, 250));
                            activity.getFrame().setResizable(false);

                        }else if(json.getInt("p") == 1){
                            b.putString("t", json.getString("t"));
                            b.putString("f", readStringPreference(json.getString("id"), json.getString("f")));
                            b.putString("id", json.getString("id"));

                            activity = new Activity(new TextPopupActivity(), b);
                            activity.setSize(new Dimension(400, 250));
                            activity.getFrame().setResizable(false);

                        }else if(json.getInt("p") == 2){
                            activity = new Activity(new WebcamActivity());
                            activity.setMinimumSize(new Dimension(1000, 600));
                            activity.setSize(new Dimension(1000, 600));

                        }else if(json.getInt("p") == 3){
                            //b.putString("t", json.getString("t"));
                            //b.putString("f", readStringPreference(json.getString("id"), json.getString("f")));
                            //b.putString("id", json.getString("id"));

                            activity = new Activity(new DateActivity(), b);
                            activity.setSize(new Dimension(400, 400));
                            activity.getFrame().setResizable(false);
                        }

                        disableBack = true;

                        activity.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        try{
                            activity.getFrame().setIconImage(ImageIO.read(Main.class.getResource("/images/icon.png")));
                        }catch(IOException ex){
                            ex.printStackTrace();
                        }
                        activity.setLocation(((getFrame().getWidth()-activity.getWidth())/2)+getFrame().getLocation().x,
                                ((getFrame().getHeight()-activity.getHeight())/2)+getFrame().getLocation().y);
                        activity.setTitle("Trynch Popup");
                        activity.setVisible(true);

                        activity.addWindowListener(new WindowAdapter(){
                            @Override
                            public void windowClosing(WindowEvent windowEvent){
                                disableBack = false;
                            }

                            @Override
                            public void windowClosed(WindowEvent windowEvent){
                                disableBack = false;
                            }
                        });
                    }
                }

                list.clearSelection();
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

        list.addMouseMotionListener(new MouseMotionListener(){
            @Override
            public void mouseDragged(MouseEvent e){
            }

            @Override
            public void mouseMoved(MouseEvent e){
                JSONObject json = (JSONObject) model.getElementAt(list.locationToIndex(e.getPoint()));
                if(json.getInt("q") > 0){
                    list.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }else{
                    list.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
    }

    private void addModel(DefaultListModel model){
        try{
            //InputStream in = new FileInputStream(getClass().getResource("/data/settings.json").getPath());
            InputStream in = getClass().getResourceAsStream("/data/settings.json");

            String builder = "";
            byte[] buffer = new byte[4096];
            int length;

            while((length = in.read(buffer)) != -1){
                builder += new String(buffer, 0, length);
            }
            in.close();

            JSONArray json = new JSONArray(builder);

            for(int i = 0; i < json.length(); i++){
                model.addElement(json.get(i));
            }
        }catch(IOException e){
            e.printStackTrace();
        }
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

    public class CellRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean selected, boolean expanded){
            JSONObject json = (JSONObject) value;

            JPanel root = new JPanel();
            root.setBackground(Color.decode("#ffffff"));
            root.setPreferredSize(new Dimension(1000, 60)); //MAYBE 300
            root.setLayout(new RelativeLayout());

            JPanel pane = new JPanel();
            pane.setBackground(Color.decode("#ffffff"));
            pane.setPreferredSize(new Dimension(1000, 60)); //MAYBE 300
            root.add(pane, new RelativeConstraints().centerHorizontally().setSize(new Dimension(1000, 60)));

            if(json.getInt("q") == 0){
                pane.setLayout(new RelativeLayout());

                JLabel title = new JLabel();
                title.setForeground(Color.decode("#ebab34"));
                title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
                pane.setBorder(new MatteBorder(1, 0, 0, 0, Color.decode("#cccccc")));
                title.setText(json.getString("t"));
                pane.add(title, new RelativeConstraints().centerVertically().setMarginLeft(80));

            }else if(json.getInt("q") == 1){
                pane.setLayout(new RelativeLayout());
                pane.setCursor(new Cursor(Cursor.HAND_CURSOR));

                JLabel title = new JLabel();
                title.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
                title.setText(json.getString("t"));
                pane.add(title, new RelativeConstraints().centerVertically().setMarginLeft(80));

            }else if(json.getInt("q") == 2 || json.getInt("q") == -1){
                pane.setLayout(new GridBagLayout());
                pane.setCursor(new Cursor(Cursor.HAND_CURSOR));

                GridBagConstraints con = new GridBagConstraints();
                con.insets = new Insets(0, 80, 0, 0);
                con.fill = GridBagConstraints.HORIZONTAL;
                con.weightx = 1;
                con.weighty = 1;
                con.gridx = 0;
                con.gridy = 0;

                JLabel title = new JLabel();
                title.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
                title.setText(json.getString("t"));
                title.setVerticalTextPosition(JLabel.BOTTOM);
                title.setVerticalAlignment(JLabel.BOTTOM);
                title.setPreferredSize(new Dimension(100, 30));
                pane.add(title, con);

                con.gridy = 1;

                JLabel description = new JLabel();
                description.setForeground(Color.decode("#888888"));
                description.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
                description.setText(json.getString("d"));
                description.setVerticalAlignment(JLabel.TOP);
                description.setVerticalTextPosition(JLabel.TOP);
                description.setPreferredSize(new Dimension(100, 30));
                pane.add(description, con);

            }else if(json.getInt("q") == 3){
                pane.setLayout(new RelativeLayout());
                pane.setCursor(new Cursor(Cursor.HAND_CURSOR));

                JLabel title = new JLabel();
                title.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
                title.setText(json.getString("t"));
                pane.add(title, new RelativeConstraints().centerVertically().setMarginLeft(80));

                ToggleSwitch toggle = new ToggleSwitch();
                toggle.setMinimumSize(new Dimension(40, 20));
                toggle.setPreferredSize(new Dimension(40, 20));
                toggle.setChecked(readBooleanPreference(json.getString("id"), json.getBoolean("f")));
                pane.add(toggle, new RelativeConstraints().centerVertically().alignParentRight().setMarginRight(120));

            }else if(json.getInt("q") == 4){
                pane.setLayout(new RelativeLayout());
                pane.setCursor(new Cursor(Cursor.HAND_CURSOR));

                JPanel p2 = new JPanel();
                p2.setLayout(new GridBagLayout());
                p2.setOpaque(false);

                GridBagConstraints con = new GridBagConstraints();
                con.insets = new Insets(0, 80, 0, 0);
                con.fill = GridBagConstraints.HORIZONTAL;
                con.weightx = 1;
                con.weighty = 1;
                con.gridx = 0;
                con.gridy = 0;

                JLabel title = new JLabel();
                title.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
                title.setText(json.getString("t"));
                title.setVerticalTextPosition(JLabel.BOTTOM);
                title.setVerticalAlignment(JLabel.BOTTOM);
                title.setPreferredSize(new Dimension(100, 30));
                p2.add(title, con);

                con.gridy = 1;

                JLabel description = new JLabel();
                description.setForeground(Color.decode("#888888"));
                description.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
                description.setText(json.getString("d"));
                description.setVerticalAlignment(JLabel.TOP);
                description.setVerticalTextPosition(JLabel.TOP);
                description.setPreferredSize(new Dimension(100, 30));
                p2.add(description, con);

                ToggleSwitch toggle = new ToggleSwitch();
                toggle.setMinimumSize(new Dimension(40, 20));
                toggle.setPreferredSize(new Dimension(40, 20));
                toggle.setChecked(readBooleanPreference(json.getString("id"), json.getBoolean("f")));
                pane.add(toggle, con);


                pane.add(toggle, new RelativeConstraints().alignParentRight().centerVertically().setMarginRight(120));
                pane.add(p2, new RelativeConstraints().toLeftOf(toggle).alignParentLeft().setHeight(RelativeConstraints.MATCH_PARENT));
            }

            if(json.getInt("q") > 0 && selected){
                pane.setBackground(Color.decode("#e6e6e6"));
            }

            return root;
        }
    }
}
