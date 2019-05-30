package com.mark.knowledge.ninegrid.apps;

import android.app.Application;

/**
 * @author Mark
 * @Date on 2019/5/29
 **/
public class App extends Application {

    public static App instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
