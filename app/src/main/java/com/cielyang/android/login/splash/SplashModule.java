package com.cielyang.android.login.splash;

import com.cielyang.android.login.di.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/** */
@Module
public abstract class SplashModule {

    @ActivityScoped
    @Binds
    abstract SplashContract.Presenter splashPresenter(SplashPresenter presenter);
}
