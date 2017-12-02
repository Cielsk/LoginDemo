package com.cielyang.android.login.common.http;

import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;

/** */
public interface Request {

    String GET = "GET";
    String POST = "POST";
    String PUT = "PUT";
    String DELETE = "DELETE";

    void setMethod(@Method String method);

    void setHeaderField(@NonNull String key, @NonNull String value);

    void setBodyField(@NonNull String key, @NonNull String value);

    String getUrl();

    Map<String, String> getHeader();

    Object getBody();

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({GET, POST, PUT, DELETE})
    @interface Method {
    }
}
