package com.cielyang.android.login.di;

import android.app.Application;

import com.cielyang.android.login.base.DemoApplication;
import com.cielyang.android.login.data.AccountManager;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/** */
@Singleton
@Component(
        modules = {
                AppModule.class,
                DataModule.class,
                ActivityBindingModule.class,
                FragmentBindingModule.class,
                AndroidSupportInjectionModule.class
        }
)
public interface AppComponent extends AndroidInjector<DemoApplication> {

    AccountManager getAccountManager();

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
