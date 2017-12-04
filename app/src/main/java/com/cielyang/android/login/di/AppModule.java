package com.cielyang.android.login.di;

import android.app.Application;
import android.content.Context;

import dagger.Binds;
import dagger.Module;

/**
 *
 */
@Module
public abstract class AppModule {

    @Binds
    abstract Context provideContext(Application application);
}
