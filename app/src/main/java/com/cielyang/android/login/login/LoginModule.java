package com.cielyang.android.login.login;

import com.cielyang.android.login.di.ActivityScoped;
import com.cielyang.android.login.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/** */
@Module
public abstract class LoginModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract LoginFragment bindLoginFragment();

    @ActivityScoped
    @Binds
    abstract LoginContract.Presenter loginPresenter(LoginPresenter presenter);
}
