package com.snoopy.scancode.update;

import android.content.Context;
import android.util.Log;

/**
 * @Author: snoopy
 * @Date: 2018/10/24 9:55
 */
public class UpdateChecker {

    public static void checkForDialog(Context context) {
        if (context != null) {
            new CheckUpdateTask(context, Constants.TYPE_DIALOG, true).execute();
        } else {
            Log.e(Constants.TAG, "The arg context is null");
        }
    }


    public static void checkForNotification(Context context) {
        if (context != null) {
            new CheckUpdateTask(context, Constants.TYPE_NOTIFICATION, false).execute();
        } else {
            Log.e(Constants.TAG, "The arg context is null");
        }

    }
}
