package com.cielyang.android.login.main;

import com.cielyang.android.login.di.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/** */
@Module
public abstract class MainModule {

    @Binds
    @ActivityScoped
    abstract MainContract.Presenter mainPresenter(MainPresenter presenter);
}
