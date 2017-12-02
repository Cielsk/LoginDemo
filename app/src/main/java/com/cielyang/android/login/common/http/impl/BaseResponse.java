package com.cielyang.android.login.common.http.impl;

import com.cielyang.android.login.common.http.Response;

/** Basic response class implements {@link Response}. */
public class BaseResponse implements Response {

    private int mCode;

    private String mData;

    @Override
    public int getCode() {
        return mCode;
    }

    public void setCode(int code) {
        mCode = code;
    }

    @Override
    public String getData() {
        return mData;
    }

    public void setData(String data) {
        mData = data;
    }
}
