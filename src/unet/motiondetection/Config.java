package unet.motiondetection;

import java.util.prefs.Preferences;

public class Config {

    public static void savePreference(String key, Object value){
        Preferences prefs = Preferences.userNodeForPackage(Main.class);

        if(value instanceof Double){
            prefs.putDouble(key, (Double) value);

        }else if(value instanceof Float){
            prefs.putFloat(key, (Float) value);

        }else if(value instanceof Integer){
            prefs.putInt(key, (Integer) value);

        }else if(value instanceof Long){
            prefs.putLong(key, (Long) value);

        }else if(value instanceof Boolean){
            prefs.putBoolean(key, (Boolean) value);

        }else if(value instanceof String){
            prefs.put(key, (String) value);
        }
    }

    //FIX THIS CLASS
    public static Object readPreference(String key, Object def){
        Preferences prefs = Preferences.userNodeForPackage(Main.class);

        if(def instanceof Double){
            return prefs.getDouble(key, (Double) def);

        }else if(def instanceof Float){
            return prefs.getFloat(key, (Float) def);

        }else if(def instanceof Integer){
            return prefs.getInt(key, (Integer) def);

        }else if(def instanceof Long){
            return prefs.getLong(key, (Long) def);

        }else if(def instanceof Boolean){
            return prefs.getBoolean(key, (Boolean) def);

        }else if(def instanceof String){
            return prefs.get(key, (String) def);
        }

        return def;
    }

    public static double readBooleanPreference(String key, double def){
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        return prefs.getDouble(key, def);
    }

    public static float readFloatPreference(String key, float def){
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        return prefs.getFloat(key, def);
    }

    public static int readIntegerPreference(String key, int def){
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        return prefs.getInt(key, def);
    }

    public static long readLongPreference(String key, long def){
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        return prefs.getLong(key, def);
    }

    public static boolean readBooleanPreference(String key, boolean def){
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        return prefs.getBoolean(key, def);
    }

    public static String readStringPreference(String key, String def){
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        return prefs.get(key, def);
    }
}
