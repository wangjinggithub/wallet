package com.trywang.baibeiwallet;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;

/**
 * TODO 写清楚此类的作用
 *
 * @author Try
 * @date 2018/9/7 15:06
 */
public class BaseApplication extends Application {

    public static Application sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}
