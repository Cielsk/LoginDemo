package com.cielyang.android.login.base;

import android.app.Application;

import com.cielyang.android.login.BuildConfig;
import com.cielyang.android.login.configs.Api;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/** */
public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initLogger();
        setApiType();
    }

    private void setApiType() {
        Api.setDebug(BuildConfig.DEBUG);
    }

    private void initLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder().tag("Login demo").build();
        Logger.addLogAdapter(
                new AndroidLogAdapter(formatStrategy) {
                    @Override
                    public boolean isLoggable(int priority, String tag) {
                        return BuildConfig.DEBUG;
                    }
                });
    }
}
