package com.madfoat.madfoatsdklib;

import android.app.Application;
import android.content.Context;
import android.util.Log;


public class MadfoatApplication extends Application {

    private static Context context;

    public void onCreate(){
        super.onCreate();
        Log.d("Divya","Context Started....");
        MadfoatApplication.context = getApplicationContext();
    }

    public static Context getContext(){
        return MadfoatApplication.context;
    }
}
