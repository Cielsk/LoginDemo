package com.cielyang.android.login.di;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;

import com.cielyang.android.login.viewmodel.AppViewModelFactory;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/** */
@Module(subcomponents = ViewModelComponent.class)
public abstract class AppModule {

    @Singleton
    @Provides
    static ViewModelProvider.Factory provideViewModelFactory(
            ViewModelComponent.Builder viewModelComponentBuilder) {
        return new AppViewModelFactory(viewModelComponentBuilder.build());
    }

    @Binds
    abstract Context provideContext(Application application);
}
