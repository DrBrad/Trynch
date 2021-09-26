package unet.motiondetection;

import javax.sound.sampled.*;
import java.io.IOException;

public class Actions {

    public static void shutdown()throws IOException {
        String shutdownCommand;
        String os = System.getProperty("os.name");

        if("Linux".equals(os) || "Mac OS X".equals(os)){
            shutdownCommand = "shutdown -h now";
        }else if("Windows".equals(os)){
            shutdownCommand = "shutdown.exe -s -t 0";
        }else{
            throw new RuntimeException("Unsupported operating system.");
        }

        Runtime.getRuntime().exec(shutdownCommand);
        System.exit(0);
    }

    public static void beep(){
        try{
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(Main.class.getResourceAsStream("/audio/beep.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        }catch(UnsupportedAudioFileException | IOException | LineUnavailableException e){
            e.printStackTrace();
        }
    }
}
