package unet.motiondetection.gui.activity;

import java.util.ArrayList;
import java.util.HashMap;

public class Bundle extends HashMap<String, Object> {

    public void putString(String key, String str){
        put(key, str);
    }

    public String getString(String key){
        Object obj = get(key);
        if(obj != null && obj instanceof String){
            return (String) get(key);
        }
        return null;
    }

    public void putInt(String key, int in){
        put(key, in);
    }

    public Integer getInt(String key){
        Object obj = get(key);
        if(obj != null && obj instanceof Integer){
            return (Integer) get(key);
        }
        return null;
    }

    public void putArrayList(String key, ArrayList<?> arr){
        put(key, arr);
    }

    public ArrayList<?> getArrayList(String key){
        Object obj = get(key);
        if(obj != null && obj instanceof ArrayList){
            return (ArrayList) get(key);
        }
        return null;
    }
}
