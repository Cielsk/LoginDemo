package com.cielyang.android.login.di;

import com.cielyang.android.login.login.LoginActivity;
import com.cielyang.android.login.login.LoginModule;
import com.cielyang.android.login.register.RegisterModule;
import com.cielyang.android.login.ui.activities.MainActivity;
import com.cielyang.android.login.ui.activities.SplashActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/** */
@Module
public abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector
    abstract SplashActivity bindSplashActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {LoginModule.class, RegisterModule.class})
    abstract LoginActivity bindLoginActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract MainActivity bindMainActivity();
}
