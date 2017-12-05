package com.cielyang.android.login.di;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cielyang.android.login.common.http.impl.OkHttpClientImpl;
import com.cielyang.android.login.common.utils.AppExecutors;
import com.cielyang.android.login.common.utils.SharedPreferencesUtils;
import com.cielyang.android.login.configs.SharedPreferencesConfig;
import com.cielyang.android.login.data.AccountManager;
import com.cielyang.android.login.data.AccountManagerImpl;

import java.io.File;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;

/** */
@Module
public abstract class DataModule {

    @Provides
    @Singleton
    static OkHttpClientImpl provideHttpClient(@NonNull Context context) {
        File file = context.getCacheDir();
        Cache cache = new Cache(file, SharedPreferencesConfig.CACHE_SIZE);
        return new OkHttpClientImpl(cache);
    }

    @Provides
    @Singleton
    static SharedPreferencesUtils provideSharedPrefsUtils(@NonNull Context context) {
        return new SharedPreferencesUtils(context, SharedPreferencesConfig.TOKEN_FILE_NMAE);
    }

    @Provides
    @Singleton
    static AppExecutors provideAppExecutors() {
        return new AppExecutors();
    }

    @Binds
    abstract AccountManager provideAccountManager(AccountManagerImpl manager);
}