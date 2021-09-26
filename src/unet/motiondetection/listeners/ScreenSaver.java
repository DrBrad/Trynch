package unet.motiondetection.listeners;

import java.io.IOException;
import java.util.ArrayList;

public class ScreenSaver {

    private boolean running = false, ractive = false;

    private static String COMMAND = "gnome-screensaver-command -q |  grep -q 'is active'";
    private static String[] OPEN_SHELL = { "/bin/sh", "-c", COMMAND };
    private static int EXPECTED_EXIT_CODE = 0;

    private ArrayList<ScreenSaverListener> listeners = new ArrayList<>();

    public void addScreenSaverListener(ScreenSaverListener listener){
        listeners.add(listener);
    }

    public void removeScreenSaverListener(ScreenSaverListener listener){
        if(listeners.contains(listener)){
            listeners.remove(listener);
        }
    }

    public void start(){
        if(!running){
            new Thread(new ScreenSaverRunnable()).start();
        }
    }

    public void stop(){
        running = false;
    }

    public static boolean isScreenSaverActive(){
        try{
            Process process = Runtime.getRuntime().exec(OPEN_SHELL);
            return process.waitFor() == EXPECTED_EXIT_CODE;
        }catch(IOException | InterruptedException e){
            e.printStackTrace();
        }
        return false;
    }

    public class ScreenSaverRunnable implements Runnable {

        @Override
        public void run(){
            running = true;

            while(running){
                try{
                    Thread.sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }

                boolean active = isScreenSaverActive();

                if(active != ractive){
                    ractive = active;

                    if(!listeners.isEmpty()){
                        if(active){
                            for(ScreenSaverListener listener : listeners){
                                listener.userSessionDeactivated(null);
                            }

                        }else{
                            for(ScreenSaverListener listener : listeners){
                                listener.userSessionActivated(null);
                            }
                        }
                    }
                }
            }
        }
    }
}
