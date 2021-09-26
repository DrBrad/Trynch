package unet.motiondetection;

import com.github.sarxos.webcam.*;
import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.json.JSONArray;
import org.json.JSONObject;
import unet.motiondetection.listeners.ScriptListener;
import unet.motiondetection.listeners.SoundDetector;
import unet.motiondetection.listeners.SoundListener;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static unet.motiondetection.Config.*;
import static unet.motiondetection.Main.*;

public class Script implements SoundListener, WebcamMotionListener, NativeKeyListener, WebcamListener {

    private WebcamMotionDetector webcamDetector;
    private SoundDetector soundDetector;

    private boolean running, image, shutdown, beep, logs, beeping;
    private int maxPoints;

    private String logFolder;

    private JSONArray json;

    private ArrayList<ScriptListener> listeners = new ArrayList<>();

    //CREATE TIME-STAMP FOR LOGS - SO THAT THE USER CAN SEE THEIR LOGS
    // TRYNCH > TS FOLDER - LOGS

    public void start(boolean delay){
        if(!running){
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-YY-hh:mm:ss");
            logFolder = format.format(new Date().getTime());
            json = new JSONArray();

            running = true;
            image = false;
            maxPoints = readIntegerPreference("cameraSensitivity", 40);
            shutdown = readBooleanPreference("shutdownAction", true);
            beep = readBooleanPreference("soundAction", true);
            logs = readBooleanPreference("saveLogs", true);
            beeping = false;

            if(delay){
                new Thread(new Runnable(){
                    @Override
                    public void run(){
                        try{
                            Thread.sleep(readIntegerPreference("timeDelay", 0)*1000);
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                        script();
                    }
                }).start();

            }else{
                script();
            }
        }
    }

    private void script(){
        mactivity.getModel().addElement(new JSONObject("{\"m\":\"Script is now running.\",\"t\":"+new Date().getTime()+",\"q\":2}"));

        if(readBooleanPreference("cameraMotionDetection", true)){
            String cam = readStringPreference("cameraChoice", null);
            if(cam == null){
                webcamDetector = new WebcamMotionDetector(Webcam.getDefault());
            }else{
                webcamDetector = new WebcamMotionDetector(Webcam.getWebcamByName(cam));
            }
            webcamDetector.setInterval(500); // one check per 500 ms
            webcamDetector.getWebcam().setCustomViewSizes(new Dimension(426, 240));
            webcamDetector.addMotionListener(Script.this);

            //SEE IF WE CAN CHECK FOR WEBCAM DISCONNECT WITH THIS...
            webcamDetector.getWebcam().addWebcamListener(Script.this);
            webcamDetector.start();
        }

        if(readBooleanPreference("keyDetection", true)){
            GlobalScreen.addNativeKeyListener(Script.this);
        }

        if(readBooleanPreference("soundDetection", true)){
            String mic = readStringPreference("microphoneChoice", null);
            if(mic == null){
                soundDetector = new SoundDetector();
            }else{
                soundDetector = new SoundDetector(mic);
            }
            soundDetector.addSoundListener(Script.this);
            soundDetector.start();
        }

        if(!listeners.isEmpty()){
            for(ScriptListener listener : listeners){
                listener.onScriptChange();
            }
        }
    }

    public void stop(){
        if(running){
            mactivity.getModel().addElement(new JSONObject("{\"m\":\"Script is has stopped.\",\"t\":"+new Date().getTime()+",\"q\":2}"));

            if(webcamDetector != null){
                webcamDetector.getWebcam().removeWebcamListener(this);
                webcamDetector.stop();
            }

            if(soundDetector != null){
                soundDetector.stop();
            }

            try{
                GlobalScreen.removeNativeKeyListener(this);
            }catch(Exception e){
            }

            if(!listeners.isEmpty()){
                for(ScriptListener listener : listeners){
                    listener.onScriptChange();
                }
            }

            running = false;
        }
    }

    public boolean isRunning(){
        return running;
    }

    public int getMaxPoints(){
        return maxPoints;
    }

    public void setFirstImage(boolean image){
        this.image = image;
    }

    public boolean isFirstImage(){
        return image;
    }

    public void addScriptListener(ScriptListener listener){
        listeners.add(listener);
    }

    public void removeScriptListener(ScriptListener listener){
        if(listeners.contains(listener)){
            listeners.remove(listener);
        }
    }

    @Override
    public void motionDetected(WebcamMotionEvent wme){
        if(script.isFirstImage() && wme.getPoints().size() > script.getMaxPoints()){
            /*
            File f = new File(new Date().getTime()+".png");
            try{
                System.out.println(wme.getCurrentImage().getWidth()+"  "+wme.getCurrentImage().getHeight());
                ImageIO.write(wme.getCurrentImage(), "PNG", new File(new Date().getTime()+".png"));
            }catch(IOException e){
                e.printStackTrace();
            }
            */
            JSONObject j = new JSONObject("{\"f\":"+mactivity.imageSize()+",\"t\":"+new Date().getTime()+",\"q\":1}");
            json.put(j);
            mactivity.getModel().addElement(j);
            mactivity.addImage(wme.getCurrentImage());
            action();
        }
        script.setFirstImage(true);
    }

    @Override
    public void onSenseDecibels(float peak, float rms){
    }

    @Override
    public void onSenseDecibelsPeak(int peak){
        JSONObject j = new JSONObject("{\"m\":\"Sound detection at "+peak+" decibels.\",\"t\":"+new Date().getTime()+",\"q\":0}");
        json.put(j);
        mactivity.getModel().addElement(j);
        action();
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent){
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent){
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent){
        try{
            JSONObject j = new JSONObject("{\"m\":\"Key has been pressed: "+nativeKeyEvent.getKeyChar()+"\",\"t\":"+new Date().getTime()+",\"q\":0}");
            json.put(j);
            mactivity.getModel().addElement(j);
        }catch(Exception e){
        }
        action();
    }

    @Override
    public void webcamOpen(WebcamEvent webcamEvent){
    }

    @Override
    public void webcamClosed(WebcamEvent webcamEvent){
        if(running){
            JSONObject j = new JSONObject("{\"m\":\"Webcam stream has been closed.\",\"t\":"+new Date().getTime()+",\"q\":0}");
            json.put(j);
            mactivity.getModel().addElement(j);
            action();
        }
    }

    @Override
    public void webcamDisposed(WebcamEvent webcamEvent){
        if(running){
            JSONObject j = new JSONObject("{\"m\":\"Webcam stream has been disposed.\",\"t\":"+new Date().getTime()+",\"q\":0}");
            json.put(j);
            mactivity.getModel().addElement(j);
            action();
        }
    }

    @Override
    public void webcamImageObtained(WebcamEvent webcamEvent){
    }

    private void action(){
        if(logs){
            File root = new File(new File("").getAbsolutePath()+File.separator+"Trynch"+File.separator+logFolder);
            if(!root.exists()){
                root.mkdirs();
            }

            try{
                FileOutputStream out = new FileOutputStream(root.getPath()+File.separator+"log.json");

                out.write(json.toString().getBytes(), 0, json.toString().getBytes().length);
                out.flush();
                out.close();

                if(!mactivity.getImages().isEmpty()){
                    int i = 0;
                    for(BufferedImage image : mactivity.getImages()){
                        File f = new File(root.getPath()+File.separator+i+".png");
                        if(!f.exists()){
                            ImageIO.write(image, "png", f);
                        }
                        i++;
                    }
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        if(shutdown){
            try{
                Actions.shutdown();
            }catch(IOException e){
                e.printStackTrace();
            }

        }else if(beep && !beeping){
            if(soundDetector != null){
                soundDetector.stop();
            }

            beeping = true;

            new Thread(new Runnable(){
                @Override
                public void run(){
                    while(running){
                        Actions.beep();
                        try{
                            Thread.sleep(1000);
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }
}
