package com.cielyang.android.login.register;

import com.cielyang.android.login.di.ActivityScoped;
import com.cielyang.android.login.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 *
 */
@Module
public abstract class RegisterModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract RegisterFragment bindRegisterFragment();

    @ActivityScoped
    @Binds
    abstract RegisterContract.Presenter registerPresenter(RegisterPresenter presenter);
}
