package com.snoopy.scancode.update;

import android.content.Context;
import android.util.Log;

import java.io.File;

/**
 * @Author: snoopy
 * @Date: 2018/10/24 9:54
 */
public class StorageUtils {

    private static final String TAG = StorageUtils.class.getSimpleName();
    /**
     * 获取应用的缓存目录
     */
    public static File getCacheDirectory(Context context) {

        File appCacheDir = context.getCacheDir();
        if (appCacheDir == null) {
            Log.w("StorageUtils", "Can't define system cache directory! The app should be re-installed.");
        }
        return appCacheDir;
    }

}
