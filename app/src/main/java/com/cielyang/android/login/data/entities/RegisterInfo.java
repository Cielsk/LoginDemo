package com.cielyang.android.login.data.entities;

import com.google.gson.annotations.SerializedName;

/** */
public class RegisterInfo {
    @SerializedName("username")
    private String mUsername;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("password")
    private String mPassword;

    public RegisterInfo(String username, String email, String password) {
        mUsername = username;
        mEmail = email;
        mPassword = password;
    }

    public RegisterInfo() {
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
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
