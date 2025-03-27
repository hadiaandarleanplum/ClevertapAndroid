package com.hadia.clevertapandroid;
import android.content.Context;
import android.util.Log;

import java.io.File;

//Clear app cache
public class CacheUtils {

    private static final String TAG = "CacheUtils";

    public static void clearAppCache(Context context) {
        try {
            File cacheDir = context.getCacheDir();
            if (cacheDir != null && cacheDir.isDirectory()) {
                boolean result = deleteDir(cacheDir);
                Log.d(TAG, "Cache cleared: " + result);
            } else {
                Log.d(TAG, "Cache directory not found or not a directory.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error clearing cache: ", e);
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    boolean success = deleteDir(new File(dir, child));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        return dir != null && dir.delete();
    }
}
