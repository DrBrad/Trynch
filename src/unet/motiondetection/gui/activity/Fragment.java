package unet.motiondetection.gui.activity;

import javax.swing.*;

public class Fragment {

    private JFrame frame;
    private JPanel root;
    private Fragment hierarchy;

    public void setParent(JFrame frame){
        this.frame = frame;
        root = new JPanel();
        frame.add(root);
    }

    public void onCreate(Bundle bundle){
    }

    public void onResume(){
        frame.add(root);
        //root.setVisible(true);
        frame.revalidate();
        frame.repaint();
    }

    public void onPause(){
        frame.remove(root);
        //root.setVisible(false);
        frame.revalidate();
        frame.repaint();
    }

    public void onStop(){
        if(hierarchy != null){
            hierarchy.onResume();
            onPause();
            frame.remove(root);
            frame.revalidate();
            frame.repaint();
        }
    }

    public void startFragment(Fragment fragment){
        startFragment(fragment, null);
    }

    public void startFragment(Fragment fragment, Bundle bundle){
        fragment.hierarchy = this;
        fragment.setParent(frame);
        onPause();
        fragment.onCreate(bundle);
        frame.revalidate();
        frame.repaint();
    }

    public JFrame getFrame(){
        return frame;
    }

    public JPanel getRoot(){
        return root;
    }
}
