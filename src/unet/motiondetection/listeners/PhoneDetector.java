package unet.motiondetection.listeners;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import static unet.motiondetection.Config.*;

public class PhoneDetector {

    private InetAddress address;
    private boolean running = false, rnear = true;

    private ArrayList<PhoneListener> listeners = new ArrayList<>();

    public PhoneDetector()throws UnknownHostException {
        address = InetAddress.getByName(readStringPreference("phoneIP", "192.168.0.2"));
    }

    public void addPhoneListener(PhoneListener listener){
        listeners.add(listener);
    }

    public void removePhoneListener(PhoneListener listener){
        if(listeners.contains(listener)){
            listeners.remove(listener);
        }
    }

    public void start(){
        if(!running){
            new Thread(new PhoneRunnable()).start();
        }
    }

    public void stop(){
        running = false;
    }

    public class PhoneRunnable implements Runnable {

        @Override
        public void run(){
            running = true;

            while(running){
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }

                try{
                    boolean near = address.isReachable(3000);

                    if(near != rnear){
                        rnear = near;
                        if(!listeners.isEmpty()){
                            if(near){
                                for(PhoneListener listener : listeners){
                                    listener.onPhoneActivated();
                                }

                            }else{
                                for(PhoneListener listener : listeners){
                                    listener.onPhoneDeactivated();
                                }
                            }
                        }
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
