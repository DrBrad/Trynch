package unet.motiondetection.gui.componants;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;

public class CustomDropDown extends BasicComboBoxUI {

    @Override
    protected void installDefaults(){
        super.installDefaults();
        LookAndFeel.uninstallBorder(comboBox);
    }

    @Override
    protected JButton createArrowButton(){
        CurvedDropDownButton button = new CurvedDropDownButton();
        button.setClipColor(Color.decode("#ffffff"));
        button.setName("ComboBox.arrowButton"); //Mandatory, as per BasicComboBoxUI#createArrowButton().
        return button;
    }

    @Override
    public void configureArrowButton(){
        super.configureArrowButton(); //Do not forget this!
        arrowButton.setBackground(Color.decode("#e6e6e6"));
        arrowButton.setForeground(Color.decode("#000000"));
    }

    @Override
    public Dimension getMinimumSize(JComponent c){
        Dimension mindim = super.getMinimumSize(c);
        Insets buttonInsets = arrowButton.getInsets();
        return new Dimension(mindim.width + buttonInsets.left + buttonInsets.right, mindim.height + buttonInsets.top + buttonInsets.bottom);
    }
}
