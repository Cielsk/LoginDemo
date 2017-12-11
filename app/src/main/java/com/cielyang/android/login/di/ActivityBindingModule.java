package com.cielyang.android.login.di;

import com.cielyang.android.login.login.LoginActivity;
import com.cielyang.android.login.login.LoginModule;
import com.cielyang.android.login.main.MainActivity;
import com.cielyang.android.login.main.MainModule;
import com.cielyang.android.login.register.RegisterModule;
import com.cielyang.android.login.splash.SplashActivity;
import com.cielyang.android.login.splash.SplashModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/** */
@Module
public abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = SplashModule.class)
    abstract SplashActivity bindSplashActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {LoginModule.class, RegisterModule.class})
    abstract LoginActivity bindLoginActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = MainModule.class)
    abstract MainActivity bindMainActivity();
}
