package com.cielyang.android.login.base;

import com.cielyang.android.login.BuildConfig;
import com.cielyang.android.login.configs.Api;
import com.cielyang.android.login.di.DaggerAppComponent;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

/** */
public class DemoApplication extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        initLogger();
        setApiType();

        Logger.v("Demo application created.");
    }

    private void setApiType() {
        Api.setDebug(BuildConfig.DEBUG);
    }

    private void initLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder().tag("LoginDemo").build();
        Logger.addLogAdapter(
                new AndroidLogAdapter(formatStrategy) {
                    @Override
                    public boolean isLoggable(int priority, String tag) {
                        return BuildConfig.DEBUG;
                    }
                });
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }
}
