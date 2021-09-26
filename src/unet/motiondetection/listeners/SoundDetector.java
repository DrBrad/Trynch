package unet.motiondetection.listeners;

import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.List;

import static unet.motiondetection.Config.*;

public class SoundDetector {

    private ArrayList<SoundListener> listeners = new ArrayList<>();
    private boolean running;
    private int maxPeak;
    private static Mixer.Info mixer;
    private long time, delay;

    public SoundDetector(){
        maxPeak = readIntegerPreference("soundSensitivity", 70);
        delay = readLongPreference("notificationInterval", 3)*1000000000l;
    }

    public SoundDetector(Mixer.Info mixer){
        this.mixer = mixer;
        maxPeak = readIntegerPreference("soundSensitivity", 70);
        delay = readLongPreference("notificationInterval", 3)*1000000000l;
    }

    public SoundDetector(String mixerName){
        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
        Line.Info targetDLInfo = new Line.Info(TargetDataLine.class);

        for(int cnt = 0; cnt < mixerInfo.length; cnt++){
            Mixer currentMixer = AudioSystem.getMixer(mixerInfo[cnt]);

            if(currentMixer.isLineSupported(targetDLInfo) && mixerInfo[cnt].getName().equals(mixerName)){
                this.mixer = mixerInfo[cnt];
                maxPeak = readIntegerPreference("soundSensitivity", 70);
                delay = readLongPreference("notificationInterval", 3)*1000000000l;
                return;
            }
        }

        throw new IllegalArgumentException("Unable to find specified microphone.");
    }

    public void setMicrophone(Mixer.Info mixer){
        this.mixer = mixer;
    }

    public List<Mixer.Info> getMicrophones(){
        List<Mixer.Info> mics = new ArrayList<>();

        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
        Line.Info targetDLInfo = new Line.Info(TargetDataLine.class);

        for(int cnt = 0; cnt < mixerInfo.length; cnt++){
            Mixer currentMixer = AudioSystem.getMixer(mixerInfo[cnt]);

            if(currentMixer.isLineSupported(targetDLInfo)){
                mics.add(mixerInfo[cnt]);
            }
        }
        return mics;
    }

    public void start(){
        if(!running){
            new Thread(new SoundRunnable()).start();
        }
    }

    public void stop(){
        running = false;
    }

    public class SoundRunnable implements Runnable {

        @Override
        public void run(){
            running = true;
            time = System.nanoTime()+delay;

            Mixer.Info mixer = SoundDetector.mixer;

            AudioFormat fmt = new AudioFormat(44100f, 16, 1, true, false);

            TargetDataLine line;
            try{
                if(mixer == null){
                    line = AudioSystem.getTargetDataLine(fmt);
                }else{
                    line = AudioSystem.getTargetDataLine(fmt, mixer);//, mixerInfo[0]
                }

                //line = getTargetLine(fmt);
                line.open(fmt, 2048);
            }catch(LineUnavailableException e){
                e.printStackTrace();
                return;
            }
            line.start();

            byte[] buf = new byte[2048];
            float[] samples = new float[1024];
            float lastPeak = 0f;

            for(int b; (b = line.read(buf, 0, buf.length)) > -1;){
                // convert bytes to samples here
                for(int i = 0, s = 0; i < b;){
                    int sample = 0;

                    sample |= buf[i++] & 0xFF; // (reverse these two lines
                    sample |= buf[i++] << 8;   //  if the format is big endian)

                    // normalize to range of +/-1.0f
                    samples[s++] = sample/32768f;
                }

                float rms = 0f;
                float peak = 0f;
                for(float sample : samples){
                    float abs = Math.abs(sample);
                    if(abs > peak){
                        peak = abs;
                    }
                    rms += sample*sample;
                }

                rms = (float) Math.sqrt(rms/samples.length);

                if(lastPeak > peak){
                    peak = lastPeak * 0.875f;
                }

                lastPeak = peak;

                //int p = Math.round(peak*1000);
                if(!listeners.isEmpty()){
                    for(SoundListener listener : listeners){
                        listener.onSenseDecibels(peak, rms);
                    }
                }

                long now = System.nanoTime();

                if(peak*1000 > maxPeak && time < now){
                    time = now+delay;
                    if(!listeners.isEmpty()){
                        for(SoundListener listener : listeners){
                            listener.onSenseDecibelsPeak((int) (peak*1000));
                        }
                    }
                }

                if(mixer != SoundDetector.mixer){
                    line.close();
                    mixer = SoundDetector.mixer;

                    try{
                        line = AudioSystem.getTargetDataLine(fmt, mixer);

                        line.open(fmt, 2048);
                    }catch(LineUnavailableException e){
                        e.printStackTrace();
                        return;
                    }

                    line.start();
                }

                if(!running){
                    line.close();
                    return;
                }
            }
        }
    }

    public void addSoundListener(SoundListener listener){
        listeners.add(listener);
    }

    public void removeSoundListener(SoundListener listener){
        if(listeners.contains(listener)){
            listeners.remove(listener);
        }
    }
}
