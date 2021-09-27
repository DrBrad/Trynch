package unet.motiondetection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

import static unet.motiondetection.Config.*;

public class ScheduledTask {

    private Timer timer;
    private boolean script, auto;
    private long start, end;
    private boolean day;
    private JSONArray days;

    public ScheduledTask(){
        timerUpdate();

        timer = new Timer();
        timer.schedule(new TimerTask(){
            private boolean kicked;

            @Override
            public void run(){
                if(readBooleanPreference("scheduleDetection", false)){
                    long now = System.currentTimeMillis();
                    Calendar cal = Calendar.getInstance(TimeZone.getDefault());

                    if(days.getBoolean(cal.get(Calendar.DAY_OF_WEEK)-1)){
                        if(start < now && start+5000 > now && !kicked){
                            kicked = true;
                            if(script && !Main.script.isRunning()){
                                Main.script.start(true);
                            }

                            if(auto && !Main.autonomy.isRunning()){
                                Main.autonomy.start();
                            }

                            timerUpdate();

                        }else if(end < now && end+5000 > now && kicked){
                            kicked = false;
                            if(script && Main.script.isRunning()){
                                Main.script.stop();
                            }

                            if(auto && Main.autonomy.isRunning()){
                                Main.autonomy.stop();
                            }

                            timerUpdate();
                        }
                    }
                }
            }
        }, 1000, 1000);
    }

    public void stop(){
        timer.purge();
        timer.cancel();
    }

    public void timerUpdate(){
        JSONObject json = new JSONObject(readStringPreference("scheduleDetails", "{" +
                "\"d\":[false,true,true,true,true,true,false]," +
                "\"sh\":12," +
                "\"sm\":00," +
                "\"eh\":12," +
                "\"em\":00," +
                "\"s\":true," +
                "\"a\":false" +
                "}"));

        script = json.getBoolean("s");
        auto = json.getBoolean("a");

        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.set(Calendar.HOUR_OF_DAY, json.getInt("sh"));
        cal.set(Calendar.MINUTE, json.getInt("sm"));
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        days = json.getJSONArray("d");

        start = cal.toInstant().toEpochMilli();

        cal = Calendar.getInstance(TimeZone.getDefault());
        cal.set(Calendar.HOUR_OF_DAY, json.getInt("eh"));
        cal.set(Calendar.MINUTE, json.getInt("em"));
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        end = cal.toInstant().toEpochMilli();
    }
}
