package com.xtrapay.commons.utils;

import android.app.Application;
import android.content.Context;


public class BaseUtils {
    private static Context sApplication;

    private BaseUtils() {
    }

    public static void init(Application app) {
        sApplication = app;
    }

    public static Context getApp() {
        if (sApplication != null) {
            return sApplication;
        } else {
            throw new NullPointerException("u should init first");
        }
    }
}