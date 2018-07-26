package com.richardgrable.budgettracker.utils;

import android.util.Log;

public class LogUtil {

    public static final int ERROR = 0;
    public static final int WARNING = 1;
    public static final int LOG = 2;
    public static final int DEBUG = 3;
    public static final int DEBUG_LEVEL = 3;

    public static int e(String tag, String message) {
        if (DEBUG_LEVEL >= ERROR) {
            return Log.e(tag, message);
        }
        return 0;
    }

    public static int w(String tag, String message) {
        if (DEBUG_LEVEL >= WARNING) {
            return Log.w(tag, message);
        }
        return 0;
    }

    public static int i(String tag, String message) {
        if (DEBUG_LEVEL >= LOG) {
            return Log.i(tag, message);
        }
        return 0;
    }

    public static int d(String tag, String message) {
        if (DEBUG_LEVEL >= DEBUG) {
            return Log.d(tag, message);
        }
        return 0;
    }
}
