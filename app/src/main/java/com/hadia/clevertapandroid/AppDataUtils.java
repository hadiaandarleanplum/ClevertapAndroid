package com.hadia.clevertapandroid;
import android.content.Context;
import android.util.Log;

import java.io.File;

//Clear app data
public class AppDataUtils {

    private static final String TAG = "AppDataUtils";

    // Clear all app data
    public static void clearAppData(Context context) {
        try {
            // Clear cache directory
            File cacheDir = context.getCacheDir();
            if (cacheDir != null && cacheDir.isDirectory()) {
                deleteDir(cacheDir);
            }

            // Clear files directory
            File filesDir = context.getFilesDir();
            if (filesDir != null && filesDir.isDirectory()) {
                deleteDir(filesDir);
            }

            // Clear SharedPreferences
            context.getSharedPreferences("your_preferences_name", Context.MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            Log.d(TAG, "App data cleared successfully.");
        } catch (Exception e) {
            Log.e(TAG, "Error clearing app data: ", e);
        }
    }

    // Helper method to delete a directory and its contents
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
