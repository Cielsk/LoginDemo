package com.cielyang.android.login.data.entities;

import com.google.gson.annotations.SerializedName;

/** */
public class LoginInfo {

    @SerializedName("username")
    private String mEmail;
    @SerializedName("password")
    private String mPassword;

    public LoginInfo(String email, String password) {
        mEmail = email;
        mPassword = password;
    }

    public LoginInfo() {
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }
}
