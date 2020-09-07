package com.example.hw_imryashur;

import android.app.Application;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MySignalV2.initHelper(this);
       // Activity_Record record = new Activity_Record();
        MySharedPreferencesV4.initHelper(this);

    }
}
