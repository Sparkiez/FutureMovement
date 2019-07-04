package com.waleong.futuremovement;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by raymondleong on 03,July,2019
 */
public class FmApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
