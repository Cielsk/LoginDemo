package com.cielyang.android.login.configs.leancloud;

import com.google.gson.annotations.SerializedName;

/** 根据 JSON 文件自动生成的 LeanCloud 错误回应类. */
public class ErrorResponse {

    @SerializedName("code")
    private int code;

    @SerializedName("error")
    private String error;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" + "code = '" + code + '\'' + ",error = '" + error + '\'' + "}";
    }
}
