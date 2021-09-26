package unet.motiondetection;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import unet.motiondetection.gui.DetailsActivity;
import unet.motiondetection.gui.MainActivity;
import unet.motiondetection.gui.activity.Activity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static unet.motiondetection.Config.*;

public class Main {

    /*
    SEND DATA TO PHONE
    MAKE THE APP INTERFACE
    */

    // sudo apt install gnome-screensaver

    public static Script script;
    public static Autonomy autonomy;
    public static MainActivity mactivity;

    public static void main(String[] args)throws IOException {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

        logger.setUseParentHandlers(false);
        try{
            GlobalScreen.registerNativeHook();
        }catch(NativeHookException e){
        }

        script = new Script();
        autonomy = new Autonomy();

        Activity activity;

        if(readBooleanPreference("firstTime", false)){
            mactivity = new MainActivity();
            activity = new Activity(mactivity);

        }else{
            savePreference("firstTime", true);
            activity = new Activity(new DetailsActivity());
        }

        activity.setIconImage(ImageIO.read(Main.class.getResource("/images/icon.png")));
        activity.setMinimumSize(new Dimension(1000, 600));
        activity.setTitle("Trynch");
        activity.setVisible(true);

        if(args.length > 0){
            boolean auto = false, start = false;
            for(String arg : args){
                auto = (!auto) ? arg.equalsIgnoreCase("-auto") : auto;
                start = (!start) ? arg.equalsIgnoreCase("-start") : start;
            }

            if(auto){
                autonomy.start();
            }

            if(start){
                script.stop();
            }
        }
    }
}
