package com.cielyang.android.login.data.remote;

import android.support.annotation.NonNull;

import com.cielyang.android.login.configs.Api;
import com.cielyang.android.login.configs.leancloud.RequestHeader;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/** */
public class CommonInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Request leanCloudRequest =
                request.newBuilder()
                        .header(RequestHeader.APP_ID_PARAM_NAME, Api.getAppId())
                        .header(RequestHeader.APP_KEY_PARAM_NAME, Api.getAppKey())
                        .build();

        return chain.proceed(leanCloudRequest);
    }
}
