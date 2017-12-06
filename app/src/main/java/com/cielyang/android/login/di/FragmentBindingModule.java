package com.cielyang.android.login.di;

import com.cielyang.android.login.ui.fragments.LoginFragment;
import com.cielyang.android.login.ui.fragments.RegisterFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/** */
@Module
public abstract class FragmentBindingModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract LoginFragment bindLoginFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract RegisterFragment bindRegisterFragment();
}
