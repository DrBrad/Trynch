package unet.motiondetection.listeners;

import java.awt.desktop.UserSessionEvent;

public interface ScreenSaverListener {

    void userSessionActivated(UserSessionEvent e);

    void userSessionDeactivated(UserSessionEvent e);
}
