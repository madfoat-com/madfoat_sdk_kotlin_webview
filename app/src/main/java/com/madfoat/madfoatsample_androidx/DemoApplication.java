package com.madfoat.madfoatsample_androidx;

import android.content.Context;
import android.util.Log;

import com.madfoat.madfoatsdklib.MadfoatApplication;


/**
 * Created by staff on 10/30/17.
 */

public class DemoApplication extends MadfoatApplication {

    private static Context context;

    public void onCreate(){
        super.onCreate();
        Log.d("Demo","Context Started....");
        DemoApplication.context = getApplicationContext();
    }

    public static Context getContext(){
        return DemoApplication.context;
    }
}
