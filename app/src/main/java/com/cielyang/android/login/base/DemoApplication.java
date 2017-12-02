package com.cielyang.android.login.base;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 *
 */
public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 日志管理工具初始化
        Logger.addLogAdapter(new AndroidLogAdapter());
    }
}
