package fraiburgo.ifc.edu.br.utils;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by iuryk on 13/07/2015.
 */
public class MemoryCache {

    private static final String TAG = "MemoryCache";
    private Map<String, Bitmap> cache = Collections.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));
    private long size = 0;
    private long limit = 4000000;

    public MemoryCache() {
        setLimit(Runtime.getRuntime().maxMemory() / 4);
    }

    public void setLimit(long new_limit) {
        limit = new_limit;
        Log.i(TAG, "Memory Cache will use up to " + limit / 1024. / 1024. + "MB");
    }

    public Bitmap get(String id) {
        try {
            if (!cache.containsKey(id)) {
                return null;
            } else {
                return cache.get(id);
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void put(String id, Bitmap bitmap) {
        try {
            if(cache.containsKey(id)){
                size -= getSizeInBytes(cache.get(id));
            }
            cache.put(id,bitmap);
            size += getSizeInBytes(bitmap);
            checkSize();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void checkSize(){
        Log.i(TAG,"cache size =" + size + "length = "+ cache.size());
        if(size > limit){
            Iterator<Map.Entry<String, Bitmap>> iter = cache.entrySet().iterator();
            while (iter.hasNext()){
                Map.Entry<String,Bitmap> entry = iter.next();
                size -= getSizeInBytes(entry.getValue());
                iter.remove();
                if(size <= limit){
                    break;
                }
            }
            Log.i(TAG,"Clean cache. new size "+ cache.size());
        }
    }

    public void clear(){
        try {
            cache.clear();
            size = 0;
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

    public long getSizeInBytes(Bitmap bitmap){
        if(bitmap == null){
            return 0;
        }else{
            return bitmap.getRowBytes() * bitmap.getHeight();
        }
    }
}
