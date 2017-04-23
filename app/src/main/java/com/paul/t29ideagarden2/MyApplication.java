package com.paul.t29ideagarden2;

import android.app.Application;
import android.content.Context;

/**
 * Created by paul on 4/20/17.
 */

public class MyApplication extends Application {
    private  static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
