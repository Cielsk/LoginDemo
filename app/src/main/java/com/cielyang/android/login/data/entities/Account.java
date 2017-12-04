package com.cielyang.android.login.data.entities;

import com.google.gson.annotations.SerializedName;

/** LeanCloud 通用用户账号类，由 JSON 文件生成. */
public class Account {

    @SerializedName("createdAt")
    private String mCreatedAt;

    @SerializedName("emailVerified")
    private boolean mEmailVerified;

    @SerializedName("sessionToken")
    private String mSessionToken;

    @SerializedName("mobilePhoneVerified")
    private boolean mMobilePhoneVerified;

    @SerializedName("email")
    private String mEmail;

    @SerializedName("objectId")
    private String mUserId;

    @SerializedName("updatedAt")
    private String mUpdatedAt;

    @SerializedName("username")
    private String mUsername;

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        this.mCreatedAt = createdAt;
    }

    public boolean isEmailVerified() {
        return mEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.mEmailVerified = emailVerified;
    }

    public String getSessionToken() {
        return mSessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.mSessionToken = sessionToken;
    }

    public boolean isMobilePhoneVerified() {
        return mMobilePhoneVerified;
    }

    public void setMobilePhoneVerified(boolean mobilePhoneVerified) {
        this.mMobilePhoneVerified = mobilePhoneVerified;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.mUpdatedAt = updatedAt;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }

    @Override
    public String toString() {
        return "User{"
                + "mCreatedAt = '"
                + mCreatedAt
                + '\''
                + ",mEmailVerified = '"
                + mEmailVerified
                + '\''
                + ",mSessionToken = '"
                + mSessionToken
                + '\''
                + ",mMobilePhoneVerified = '"
                + mMobilePhoneVerified
                + '\''
                + ",mEmail = '"
                + mEmail
                + '\''
                + ",mUserId = '"
                + mUserId
                + '\''
                + ",mUpdatedAt = '"
                + mUpdatedAt
                + '\''
                + ",mUsername = '"
                + mUsername
                + '\''
                + "}";
    }
}
