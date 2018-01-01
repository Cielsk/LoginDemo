package com.cielyang.android.login.di;

import com.cielyang.android.login.viewmodel.MainViewModel;

import dagger.Subcomponent;

/** */
@Subcomponent
public interface ViewModelComponent {

    MainViewModel mainViewModel();

    @Subcomponent.Builder
    interface Builder {
        ViewModelComponent build();
    }
}
