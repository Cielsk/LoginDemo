package com.cielyang.android.login.di;

import com.cielyang.android.login.ui.activities.LoginActivity;
import com.cielyang.android.login.ui.activities.MainActivity;
import com.cielyang.android.login.ui.activities.SplashActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 *
 */
@Module
public abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector
    abstract SplashActivity bindSplashActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract LoginActivity bindLoginActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract MainActivity bindMainActivity();
}
