package com.dhimandasgupta.jobtest;

import android.app.Application;

/**
 * Created by dhimandasgupta on 08/09/17.
 */

public class JobApp extends Application {
    private static JobApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
    }

    public static JobApp getInstance() {
        return sInstance;
    }
}
