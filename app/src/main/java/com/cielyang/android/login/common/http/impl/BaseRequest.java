package com.cielyang.android.login.common.http.impl;

import android.support.annotation.NonNull;

import com.cielyang.android.login.common.http.Request;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Basic request class implements {@link Request}.
 *
 * <p>A request URL can be updated dynamically using replacement blocks and parameters on the
 * method. A replacement block is an alphanumeric string surrounded by { and }.</p>
 *
 * <p>A request body is a json string.</p>
 */
public class BaseRequest implements Request {

    // the default method is POST
    private String mMethod = "POST";
    private String mUrl;
    private Map<String, String> mHeader;
    private Map<String, Object> mBody;

    /**
     * Instantiates a new Base request.
     *
     * @param url the url
     */
    public BaseRequest(@NonNull String url) {
        mUrl = url;
        mHeader = new HashMap<>();
        mBody = new HashMap<>();
    }

    @Override
    public void setMethod(@Method String method) {
        mMethod = method;
    }

    @Override
    public void setHeaderField(@NonNull String key, @NonNull String value) {
        mHeader.put(key, value);
    }

    @Override
    public void setBodyField(@NonNull String key, @NonNull String value) {
        mBody.put(key, value);
    }

    @Override
    public String getUrl() {
        if (GET.equals(mMethod)) {
            for (String key : mBody.keySet()) {
                mUrl = mUrl.replace("{" + key + "}", mBody.get(key).toString());
            }
        }

        return mUrl;
    }

    @Override
    public Map<String, String> getHeader() {
        return mHeader;
    }

    @Override
    public Object getBody() {
        if (mBody != null) {
            return new Gson().toJson(mBody, HashMap.class);
        }

        // return empty json string by default
        return "{}";
    }
}
