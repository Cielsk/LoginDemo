package com.cielyang.android.login.di;

import com.cielyang.android.login.viewmodel.MainViewModel;
import com.cielyang.android.login.viewmodel.SplashViewModel;

import dagger.Subcomponent;

/** */
@Subcomponent
public interface ViewModelComponent {

    MainViewModel mainViewModel();

    SplashViewModel splashViewModel();

    @Subcomponent.Builder
    interface Builder {
        ViewModelComponent build();
    }
}
