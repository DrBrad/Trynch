package unet.motiondetection.listeners;

public interface SoundListener {

    void onSenseDecibels(float peak, float rms);

    void onSenseDecibelsPeak(int peak);
}
