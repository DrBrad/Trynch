package unet.motiondetection.gui;

import org.json.JSONArray;
import org.json.JSONObject;
import unet.motiondetection.gui.activity.Bundle;
import unet.motiondetection.gui.activity.Fragment;
import unet.motiondetection.gui.componants.*;
import unet.motiondetection.gui.layouts.RelativeConstraints;
import unet.motiondetection.gui.layouts.RelativeLayout;
import unet.motiondetection.listeners.ScriptListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static unet.motiondetection.Main.*;

public class MainActivity extends Fragment implements ScriptListener {

    private JList sideList;
    private DefaultListModel model;
    private ArrayList<BufferedImage> images = new ArrayList<>();

    @Override
    public void onCreate(Bundle bundle){
        getRoot().setLayout(new RelativeLayout());

        script.addScriptListener(this);

        JLabel title = new JLabel();
        title.setText("Trynch");
        title.setForeground(Color.decode("#ffffff"));
        title.setBackground(Color.decode("#3a444c"));
        title.setOpaque(true);
        title.setBorder(new EmptyBorder(0, 20, 0, 20));
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        getRoot().add(title, new RelativeConstraints().setSize(new Dimension(RelativeConstraints.MATCH_PARENT, 60)));



        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0.10;
        constraints.weighty = 1;

        JPanel content = new JPanel();
        content.setLayout(new GridBagLayout());
        getRoot().add(content, new RelativeConstraints().below(title).alignParentBottom().setSize(new Dimension(RelativeConstraints.MATCH_PARENT, RelativeConstraints.WRAP_CONTENT)));



        sideList = new JList();
        sideList.setLayout(new RelativeLayout());
        sideList.setBackground(Color.decode("#d6d6d6"));
        sideList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sideList.setLayoutOrientation(JList.VERTICAL);

        DefaultListModel sideModel = new DefaultListModel();
        addSideModel(sideModel);
        sideList.setCellRenderer(new SideCellRenderer());
        sideList.setModel(sideModel);


        JScrollPane sideScrollPane = new JScrollPane(sideList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sideScrollPane.setBackground(Color.decode("#d6d6d6"));
        sideScrollPane.setMinimumSize(new Dimension(180, 100));
        sideScrollPane.setPreferredSize(new Dimension(180, 100));
        sideScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        sideScrollPane.setHorizontalScrollBar(null);
        content.add(sideScrollPane, constraints);

        sideScrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI(){
            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r){
                Graphics2D graphics = (Graphics2D) g;
                graphics.setColor(Color.decode("#777777"));
                graphics.fillRoundRect(r.width-11, r.y, 10, r.height, 10, 10);
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle r){
                Graphics2D graphics = (Graphics2D) g;
                graphics.setColor(Color.decode("#d6d6d6"));
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

        sideList.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e){
                switch(sideList.getSelectedIndex()){
                    case 0:
                        if(script.isRunning()){
                            script.stop();

                        }else{
                            model.clear();
                            model.addElement(new JSONObject("{\"m\":\"Waiting for start...\",\"q\":2}"));
                            script.start(true);
                        }
                        break;

                    case 1:
                        if(autonomy.isRunning()){
                            model.addElement(new JSONObject("{\"m\":\"Autonomy has stopped.\",\"q\":2}"));
                            autonomy.stop();

                        }else{
                            model.addElement(new JSONObject("{\"m\":\"Autonomy is running.\",\"q\":2}"));
                            autonomy.start();
                        }
                        break;

                    case 2:
                        if(!script.isRunning() && !autonomy.isRunning()){
                            startFragment(new SettingsActivity());
                        }
                        break;

                    case 3:
                        try{
                            File f = new File(new File("").getAbsolutePath()+File.separator+"Trynch");
                            if(f.exists()){
                                Desktop.getDesktop().open(f);
                            }
                        }catch(IOException ex){
                            ex.printStackTrace();
                        }
                        break;
                }

                sideList.clearSelection();
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


        sideList.addMouseMotionListener(new MouseMotionListener(){
            @Override
            public void mouseDragged(MouseEvent e){
            }

            @Override
            public void mouseMoved(MouseEvent e){
                if(sideList.indexToLocation(sideModel.size()-1).getY()+60 > e.getY()){
                    sideList.setCursor(new Cursor(Cursor.HAND_CURSOR));

                }else{
                    sideList.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });



        constraints.gridx = 1;
        constraints.weightx = 0.90;

        JList list = new JList();
        list.setLayout(new RelativeLayout());
        list.setBackground(Color.decode("#ffffff"));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);

        model = new DefaultListModel();
        model.addElement(new JSONObject("{\"m\":\"Waiting for start...\",\"q\":2}"));

        list.setCellRenderer(new CellRenderer());
        list.setModel(model);

        JScrollPane scrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBackground(Color.decode("#ffffff"));
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        scrollPane.setHorizontalScrollBar(null);
        content.add(scrollPane, constraints);

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

        /*
        list.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e){
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
            }
        });
        */
    }

    private void addSideModel(DefaultListModel model){
        try{
            //InputStream in = new FileInputStream(getClass().getResource("/data/menu.json").getPath());
            InputStream in = getClass().getResourceAsStream("/data/menu.json");

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

    public void addImage(BufferedImage image){
        images.add(image);
    }

    public List<BufferedImage> getImages(){
        return images;
    }

    public int imageSize(){
        return images.size();
    }

    public DefaultListModel getModel(){
        return model;
    }

    @Override
    public void onScriptChange(){
        sideList.revalidate();
        sideList.repaint();
    }

    public class SideCellRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean selected, boolean expanded){
            JSONObject json = (JSONObject) value;

            MenuCurvedCorner pane = new MenuCurvedCorner();
            pane.setClipColor(Color.decode("#d6d6d6"));
            pane.setBorder(new MatteBorder(1, 0, 1, 0, Color.decode("#d6d6d6")));
            pane.setMinimumSize(new Dimension(180, 60));
            pane.setPreferredSize(new Dimension(180, 60));
            pane.setLayout(new RelativeLayout());

            JLabel title = new JLabel();
            title.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));

            if(json.getInt("q") == 0){
                PolygonView icon = new PolygonView();

                if((index == 0 && script.isRunning()) || (index == 1 && autonomy.isRunning())){
                    title.setText(json.getString("t2"));
                    icon.setForeground(Color.decode("#b82121"));
                    icon.setPolygon(new int[]{ 7, 23, 23, 7 }, new int[]{ 7, 7, 23, 23 });

                }else{
                    title.setText(json.getString("t"));
                    icon.setForeground(Color.decode("#30b821"));
                    icon.setPolygon(new int[]{ 7, 23, 7 }, new int[]{ 7, 15, 23 });
                }
                pane.add(title, new RelativeConstraints().centerVertically().setMarginLeft(40));
                pane.add(icon, new RelativeConstraints().centerVertically().alignParentRight().setSize(new Dimension(30, 30)).setMarginRight(20));

            }else if(json.getInt("q") == 1){
                title.setText(json.getString("t"));
                pane.add(title, new RelativeConstraints().centerVertically().setMarginLeft(40));
            }

            if(selected){
                pane.setBackground(Color.decode("#ffffff"));
            }else{
                pane.setBackground(Color.decode("#e6e6e6"));
            }

            return pane;
        }
    }

    public class CellRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean selected, boolean expanded){
            JSONObject json = (JSONObject) value;

            JPanel pane = new JPanel();
            pane.setBackground(Color.decode("#ffffff"));
            pane.setLayout(new RelativeLayout());

            if(json.getInt("q") == 0){
                pane.setPreferredSize(new Dimension(100, 40));

                CurvedLabel message = new CurvedLabel();
                message.setText(json.getString("m"));
                message.setClipColor(Color.decode("#ffffff"));
                message.setBackground(Color.decode("#dfdfdf"));
                message.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));

                pane.add(message, new RelativeConstraints().centerVertically().setHeight(30).setMargins(new Insets(0, 10, 0, 10)));

            }else if(json.getInt("q") == 1){
                pane.setPreferredSize(new Dimension(426, 240));

                ResizableCurvedImageView image = new ResizableCurvedImageView();
                image.setMaxWidth(426);
                image.setMaxHeight(240);

                image.setImage(new ImageIcon(images.get(json.getInt("f"))));

                image.setOpaque(true);
                image.setBackground(Color.decode("#ffffff"));

                pane.add(image, new RelativeConstraints().alignParentLeft().centerVertically().setSize(new Dimension(410, 231)).setMargins(new Insets(0, 10, 0, 10)));

            }else if(json.getInt("q") == 2){
                pane.setPreferredSize(new Dimension(100, 40));

                CurvedLabel message = new CurvedLabel();
                message.setText(json.getString("m"));
                message.setClipColor(Color.decode("#ffffff"));
                message.setBackground(Color.decode("#dfdfdf"));
                message.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));

                pane.add(message, new RelativeConstraints().centerInParent().setHeight(30).setMargins(new Insets(0, 10, 0, 10)));
            }

            return pane;
        }
    }
}
