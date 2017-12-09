package com.cielyang.android.login.di;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cielyang.android.login.common.async.AppExecutors;
import com.cielyang.android.login.common.utils.SharedPrefsUtil;
import com.cielyang.android.login.configs.Api;
import com.cielyang.android.login.configs.SharedPreferencesConfig;
import com.cielyang.android.login.data.AccountManager;
import com.cielyang.android.login.data.AccountManagerImpl;
import com.cielyang.android.login.data.remote.CommonInterceptor;
import com.google.gson.Gson;

import java.io.File;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/** */
@Module
public abstract class DataModule {

    @Provides
    @Singleton
    static Retrofit provideRetrofit(
            @NonNull Gson gson, @NonNull OkHttpClient client, @NonNull AppExecutors appExecutors) {
        return new Retrofit.Builder()
                .baseUrl(Api.getDomain())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .callbackExecutor(appExecutors.mainThread())
                .build();
    }

    @Provides
    @Singleton
    static OkHttpClient provideOkHttpClient(@NonNull Context context) {
        File file = context.getCacheDir();
        Cache cache = new Cache(file, SharedPreferencesConfig.CACHE_SIZE);
        Interceptor interceptor = new CommonInterceptor();
        return new OkHttpClient.Builder().addNetworkInterceptor(interceptor).cache(cache).build();
    }

    @Provides
    @Singleton
    static Gson provideGson() {
        return new Gson();
    }

    @Provides
    @Singleton
    static SharedPrefsUtil provideSharedPrefsUtils(@NonNull Context context) {
        return new SharedPrefsUtil(context, SharedPreferencesConfig.TOKEN_FILE_NMAE);
    }

    @Provides
    @Singleton
    static AppExecutors provideAppExecutors() {
        return new AppExecutors();
    }

    @Binds
    abstract AccountManager provideAccountManager(AccountManagerImpl manager);
}
