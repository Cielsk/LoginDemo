package com.cielyang.android.login.common.http.impl;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cielyang.android.login.common.http.HttpClient;
import com.cielyang.android.login.common.http.Request;
import com.cielyang.android.login.common.http.Response;

import java.io.IOException;
import java.util.Map;

import okhttp3.CacheControl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * An universal http client implements {@link HttpClient}.
 *
 * <p>This basically is a wrapper class for {@link OkHttpClient}.
 *
 * <p>By default the type of post and put method is json string, whose charset is UTF-8.
 */
public class OkHttpClientImpl implements HttpClient {

    private final OkHttpClient mClient;

    public OkHttpClientImpl(Context context) {
        mClient = new OkHttpClient.Builder().build();
    }

    @Override
    public Response get(@NonNull Request request, boolean forceCache) {
        request.setMethod(Request.GET);
        Builder builder = getRequestBuilder(request, forceCache);

        return execute(builder.build());
    }

    @Override
    public Response post(@NonNull Request request, boolean forceCache) {
        request.setMethod(Request.POST);

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, request.getBody().toString());

        Builder builder = getRequestBuilder(request, forceCache);
        builder.post(body);

        return execute(builder.build());
    }

    @Override
    public Response put(@NonNull Request request, boolean forceCache) {
        request.setMethod(Request.PUT);

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, request.getBody().toString());

        Builder builder = getRequestBuilder(request, forceCache);
        builder.put(body);

        return execute(builder.build());
    }

    @Override
    public Response delete(@NonNull Request request, boolean forceCache) {
        request.setMethod(Request.DELETE);

        Builder builder = getRequestBuilder(request, forceCache);

        return execute(builder.build());
    }

    @NonNull
    private Builder getRequestBuilder(@NonNull Request request, boolean forceCache) {
        Map<String, String> header = request.getHeader();
        Builder builder = new Builder();
        for (Map.Entry<String, String> entry : header.entrySet()) {
            builder.header(entry.getKey(), entry.getValue());
        }

        String url = request.getUrl();

        builder.url(url).get();
        if (forceCache) {
            builder.cacheControl(CacheControl.FORCE_CACHE);
        }
        return builder;
    }

    private Response execute(@NonNull okhttp3.Request request) {
        BaseResponse re = new BaseResponse();

        try {
            okhttp3.Response response = mClient.newCall(request).execute();
            re.setCode(response.code());

            String data = null;
            ResponseBody body = response.body();
            if (body != null) {
                data = body.string();
            }

            re.setData(data);
        } catch (IOException e) {
            re.setCode(Response.STATE_UNKNOWN_ERROR);
            re.setData(e.getMessage());
        }

        return re;
    }
}
