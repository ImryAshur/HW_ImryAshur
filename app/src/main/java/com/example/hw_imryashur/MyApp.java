package com.example.hw_imryashur;

import android.app.Application;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MySignalV2.initHelper(this);

        MySharedPreferencesV4.initHelper(this);

    }
}
