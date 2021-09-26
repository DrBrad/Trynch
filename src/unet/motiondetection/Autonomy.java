package unet.motiondetection;

import org.json.JSONObject;
import unet.motiondetection.listeners.PhoneDetector;
import unet.motiondetection.listeners.PhoneListener;
import unet.motiondetection.listeners.ScreenSaver;
import unet.motiondetection.listeners.ScreenSaverListener;

import java.awt.*;
import java.awt.desktop.UserSessionEvent;
import java.awt.desktop.UserSessionListener;
import java.net.UnknownHostException;
import java.util.Date;

import static unet.motiondetection.Config.*;
import static unet.motiondetection.Main.*;

public class Autonomy implements UserSessionListener, PhoneListener, ScreenSaverListener {

    private boolean running, screen, phone;

    private ScreenSaver screenSaver;
    private Desktop desktop;
    private PhoneDetector phoneDetector;

    public void start(){
        if(!running){
            running = true;
            phone = false;
            screen = false;

            if(readBooleanPreference("lockDetection", true)){
                String os = System.getProperty("os.name");

                if("Linux".equals(os)){
                    screenSaver = new ScreenSaver();
                    screenSaver.addScreenSaverListener(this);
                    screenSaver.start();

                }else{
                    desktop = Desktop.getDesktop();
                    desktop.addAppEventListener(this);
                }
            }

            if(readBooleanPreference("phoneDetection", true)){
                try{
                    phoneDetector = new PhoneDetector();
                    phoneDetector.addPhoneListener(this);
                    phoneDetector.start();
                }catch(UnknownHostException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void stop(){
        if(running){
            if(desktop != null){
                desktop.removeAppEventListener(this);
            }

            if(screenSaver != null){
                screenSaver.stop();
            }

            if(phoneDetector != null){
                phoneDetector.stop();
            }

            running = false;
        }
    }

    public boolean isRunning(){
        return running;
    }

    //SCREEN IS LOCKED
    @Override
    public void userSessionDeactivated(UserSessionEvent e){
        screen = true;
        if(!script.isRunning()){
            if((phoneDetector != null && phone) || (phoneDetector == null)){
                mactivity.getModel().addElement(new JSONObject("{\"m\":\"Screen is locked, starting detection.\",\"t\":"+new Date().getTime()+",\"q\":2}"));
                script.start(true);
            }
        }
    }

    //SCREEN IS UNLOCKED
    @Override
    public void userSessionActivated(UserSessionEvent e){
        screen = false;
        if(script.isRunning()){
            mactivity.getModel().addElement(new JSONObject("{\"m\":\"Screen is unlocked, stopping detection.\",\"t\":"+new Date().getTime()+",\"q\":2}"));
            script.stop();
        }
    }

    //SCREEN IS NEAR
    @Override
    public void onPhoneActivated(){
        phone = false;
        if(script.isRunning()){
            mactivity.getModel().addElement(new JSONObject("{\"m\":\"Phone was found, stopping detection.\",\"t\":"+new Date().getTime()+",\"q\":2}"));
            script.stop();
        }
    }

    //SCREEN IS FAR
    @Override
    public void onPhoneDeactivated(){
        phone = true;
        if(!script.isRunning()){
            if(((desktop != null || screenSaver != null) && screen) || (desktop == null && screenSaver == null)){
                mactivity.getModel().addElement(new JSONObject("{\"m\":\"Phone was not found, starting detection.\",\"t\":"+new Date().getTime()+",\"q\":2}"));
                script.start(false);
            }
        }
    }
}
