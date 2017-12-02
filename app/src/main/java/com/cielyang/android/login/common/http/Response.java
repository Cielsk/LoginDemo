package com.cielyang.android.login.common.http;

/** */
public interface Response {

    int STATE_UNKNOWN_ERROR = 100001;

    int STATE_OK = 200;

    int STATE_CREATED = 201;

    int STATE_NOT_FOUND = 404;

    int getCode();

    String getData();
}
