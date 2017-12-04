package com.cielyang.android.login.data;

import android.support.annotation.NonNull;

import com.cielyang.android.login.data.entities.Account;

/** */
public interface AccountManager {

    void loginByEmail(
            @NonNull CharSequence email,
            @NonNull CharSequence password,
            @NonNull LoginByEmailCallback callback);

    void loginByToken(@NonNull CharSequence token, @NonNull LoginByTokenCallback callback);

    void register(
            @NonNull CharSequence username,
            @NonNull CharSequence email,
            @NonNull CharSequence password,
            @NonNull RegisterCallback callback);

    void queryUserByName(@NonNull CharSequence username, @NonNull QueryCallback callback);

    void queryUserByEmail(@NonNull CharSequence email, @NonNull QueryCallback callback);

    Account getAccount();

    interface LoginByEmailCallback {

        void onLoginSucceed();

        void onEmailNotExisted();

        void onPasswordIncorrect();

        void onLoginFailed();
    }

    interface LoginByTokenCallback {

        void onLoginSucceed();

        void onTokenExpired();

        void onLoginFailed();
    }

    interface RegisterCallback {

        void onRegisterSucceed();

        void onUsernameExisted();

        void onEmailExisted();

        void onRegisterFailed();
    }

    interface QueryCallback {

        void onUserExisted();

        void onUserNotExisted();

        void onQueryFailed();
    }
}
