package com.cielyang.android.login.data;

import android.support.annotation.NonNull;

import com.cielyang.android.login.data.entities.Account;

/** */
public interface AccountManager {

    void checkSessionToken(CheckSessionTokenCallback callback);

    void loginByEmail(
            @NonNull CharSequence email,
            @NonNull CharSequence password,
            @NonNull LoginByEmailCallback callback);

    void loginByToken(@NonNull LoginByTokenCallback callback);

    void register(
            @NonNull CharSequence username,
            @NonNull CharSequence email,
            @NonNull CharSequence password,
            @NonNull RegisterCallback callback);

    void queryUserByName(@NonNull CharSequence username, @NonNull QueryUsernameCallback callback);

    void queryUserByEmail(@NonNull CharSequence email, @NonNull QueryEmailCallback callback);

    Account getAccount();

    interface CheckSessionTokenCallback {

        void onTokenSaved();

        void onTokenNotSaved();
    }

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

        void onUsernameRegistered();

        void onEmailRegistered();

        void onRegisterFailed();
    }

    interface QueryUsernameCallback {

        void onUsernameRegistered();

        void onUsernameNotRegistered();
    }

    interface QueryEmailCallback {

        void onEmailRegistered();

        void onEmailNotRegistered();
    }
}
