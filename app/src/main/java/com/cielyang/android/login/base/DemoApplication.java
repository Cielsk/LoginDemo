package com.cielyang.android.login.base;

import com.cielyang.android.login.BuildConfig;
import com.cielyang.android.login.configs.Api;
import com.cielyang.android.login.di.DaggerAppComponent;
import com.facebook.stetho.Stetho;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.squareup.leakcanary.LeakCanary;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

/** */
public class DemoApplication extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        initLeakCanary();
        initStetho();
        initLogger();
        setApiType();

        Logger.v("Demo application created.");
    }

    private void initStetho() {
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
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
