package com.example.textviewdemo;

import android.app.Application;

public class MyApplication extends Application {
    private static MyApplication application;
    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
    }

    public static MyApplication getIntence(){
     return application;
    }
}
