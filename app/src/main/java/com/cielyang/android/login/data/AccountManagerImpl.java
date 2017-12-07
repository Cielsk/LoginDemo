package com.cielyang.android.login.data;

import static com.cielyang.android.login.configs.leancloud.RequestBody.LOGIN_EMAIL_PARAM_NAME;
import static com.cielyang.android.login.configs.leancloud.RequestBody.PASSWORD_PARAM_NAME;
import static com.cielyang.android.login.configs.leancloud.RequestBody.REGISTER_EMAIL_PARAM_NAME;
import static com.cielyang.android.login.configs.leancloud.RequestBody.USERNAME_PARAM_NAME;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cielyang.android.login.common.http.HttpClient;
import com.cielyang.android.login.common.http.Response;
import com.cielyang.android.login.common.http.impl.BaseRequest;
import com.cielyang.android.login.common.http.impl.BaseResponse;
import com.cielyang.android.login.common.http.impl.OkHttpClientImpl;
import com.cielyang.android.login.common.utils.AppExecutors;
import com.cielyang.android.login.configs.Api;
import com.cielyang.android.login.configs.leancloud.ErrorCode;
import com.cielyang.android.login.configs.leancloud.ErrorResponse;
import com.cielyang.android.login.configs.leancloud.RequestHeader;
import com.cielyang.android.login.data.entities.Account;
import com.cielyang.android.login.data.local.SessionDao;
import com.cielyang.android.login.data.remote.CommonInterceptor;
import com.cielyang.android.login.data.remote.UserQueryResponse;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;

/** */
@Singleton
public class AccountManagerImpl implements AccountManager {

    @Inject
    Lazy<Gson> mGsonInjector;
    private HttpClient mHttpClient;
    private AppExecutors mAppExecutors;
    private SessionDao mSessionDao;
    private Account mCachedAccount;

    @Inject
    public AccountManagerImpl(
            @NonNull OkHttpClientImpl httpClient,
            @NonNull SessionDao sessionDao,
            @NonNull AppExecutors appExecutors) {
        httpClient.addNetworkInterceptor(new CommonInterceptor());
        mHttpClient = httpClient;
        mSessionDao = sessionDao;
        mAppExecutors = appExecutors;
    }

    @Override
    public void checkSessionToken(CheckSessionTokenCallback callback) {
        Runnable runnable =
                () -> {
                    if (TextUtils.isEmpty(mSessionDao.getSessionToken())) {
                        mAppExecutors.mainThread().execute(callback::onTokenNotSaved);
                    } else {
                        mAppExecutors.mainThread().execute(callback::onTokenSaved);
                    }
                };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void loginByEmail(
            @NonNull CharSequence email,
            @NonNull CharSequence password,
            @NonNull LoginByEmailCallback callback) {
        Runnable runnable =
                () -> {
                    BaseRequest request = new BaseRequest(Api.getLoginUrl());
                    request.setBodyField(LOGIN_EMAIL_PARAM_NAME, email.toString());
                    request.setBodyField(PASSWORD_PARAM_NAME, password.toString());

                    BaseResponse response = (BaseResponse) mHttpClient.post(request, false);

                    if (response.getCode() == Response.STATE_OK) {
                        Account account = mGsonInjector.get().fromJson(response.getData(),
                                Account.class);
                        saveAccountSession(account);
                        mCachedAccount = account;
                        mAppExecutors.mainThread().execute(callback::onLoginSucceed);
                    } else {
                        ErrorResponse errorResponse =
                                mGsonInjector.get().fromJson(response.getData(),
                                        ErrorResponse.class);
                        switch (errorResponse.getCode()) {
                            case ErrorCode.USER_NOT_REGISTERED:
                                mAppExecutors.mainThread().execute(callback::onEmailNotExisted);
                                break;
                            case ErrorCode.INCORRECT_PASSWORD:
                                mAppExecutors.mainThread().execute(callback::onPasswordIncorrect);
                                break;
                            default:
                                mAppExecutors.mainThread().execute(callback::onLoginFailed);
                                break;
                        }
                    }
                };

        mAppExecutors.networkIO().execute(runnable);
    }

    @Override
    public void loginByToken(@NonNull LoginByTokenCallback callback) {
        Runnable runnable =
                () -> {
                    String token = mSessionDao.getSessionToken();
                    BaseRequest request = new BaseRequest(Api.getLoginByTokenUrl());
                    request.setHeaderField(RequestHeader.SESSION_TOKEN_PARAMETER_NAME, token);

                    BaseResponse response = (BaseResponse) mHttpClient.get(request, false);

                    if (response.getCode() == Response.STATE_OK) {
                        Account account = mGsonInjector.get().fromJson(response.getData(),
                                Account.class);
                        saveAccountSession(account);
                        mCachedAccount = account;
                        mAppExecutors.mainThread().execute(callback::onLoginSucceed);
                    } else {
                        ErrorResponse errorResponse =
                                mGsonInjector.get().fromJson(response.getData(),
                                        ErrorResponse.class);
                        switch (errorResponse.getCode()) {
                            case ErrorCode.USER_NOT_REGISTERED:
                                mAppExecutors.mainThread().execute(callback::onTokenExpired);
                                break;
                            default:
                                mAppExecutors.mainThread().execute(callback::onLoginFailed);
                                break;
                        }
                    }
                };

        mAppExecutors.networkIO().execute(runnable);
    }

    @Override
    public void register(
            @NonNull CharSequence username,
            @NonNull CharSequence email,
            @NonNull CharSequence password,
            @NonNull RegisterCallback callback) {
        Runnable runnable =
                () -> {
                    BaseRequest request = new BaseRequest(Api.getRegisterUrl());
                    request.setBodyField(USERNAME_PARAM_NAME, username.toString());
                    request.setBodyField(REGISTER_EMAIL_PARAM_NAME, email.toString());
                    request.setBodyField(PASSWORD_PARAM_NAME, password.toString());

                    BaseResponse response = (BaseResponse) mHttpClient.post(request, false);

                    if (response.getCode() == Response.STATE_CREATED) {
                        Account account = mGsonInjector.get().fromJson(response.getData(),
                                Account.class);
                        saveAccountSession(account);
                        mCachedAccount = account;
                        mAppExecutors.mainThread().execute(callback::onRegisterSucceed);
                    } else {
                        ErrorResponse errorResponse =
                                mGsonInjector.get().fromJson(response.getData(),
                                        ErrorResponse.class);
                        switch (errorResponse.getCode()) {
                            case ErrorCode.USERNAME_EXISTED:
                                mAppExecutors.mainThread().execute(callback::onUsernameExisted);
                                break;
                            case ErrorCode.EMAIL_EXISTED:
                                mAppExecutors.mainThread().execute(callback::onEmailExisted);
                                break;
                            default:
                                mAppExecutors.mainThread().execute(callback::onRegisterFailed);
                                break;
                        }
                    }
                };

        mAppExecutors.networkIO().execute(runnable);
    }

    private void saveAccountSession(Account account) {
        mAppExecutors.diskIO().execute(() -> mSessionDao.saveSession(account));
    }

    @Override
    public void queryUserByName(
            @NonNull CharSequence username, @NonNull QueryUsernameCallback callback) {
        Runnable runnable =
                () -> {
                    BaseRequest request = new BaseRequest(Api.getQueryUserUrl());
                    String condition = String.format("{\"%s\":\"%s\"}", "username", username);
                    request.setBodyField(Api.QUERY_PARAM_NAME, condition);

                    BaseResponse response = (BaseResponse) mHttpClient.get(request, false);

                    if (response.getCode() == Response.STATE_OK) {
                        UserQueryResponse queryResponse =
                                mGsonInjector.get().fromJson(response.getData(),
                                        UserQueryResponse.class);
                        if (queryResponse.getResults().size() > 0) {
                            mAppExecutors.mainThread().execute(callback::onUsernameRegistered);
                        } else {
                            mAppExecutors.mainThread().execute(callback::onUsernameNotRegistered);
                        }
                    }
                };

        mAppExecutors.networkIO().execute(runnable);
    }

    @Override
    public void queryUserByEmail(
            @NonNull CharSequence email, @NonNull QueryEmailCallback callback) {
        Runnable runnable =
                () -> {
                    BaseRequest request = new BaseRequest(Api.getQueryUserUrl());
                    String condition = String.format("{\"%s\":\"%s\"}", "email", email);
                    request.setBodyField(Api.QUERY_PARAM_NAME, condition);

                    BaseResponse response = (BaseResponse) mHttpClient.get(request, false);

                    if (response.getCode() == Response.STATE_OK) {
                        UserQueryResponse queryResponse =
                                mGsonInjector.get().fromJson(response.getData(),
                                        UserQueryResponse.class);
                        if (queryResponse.getResults().size() > 0) {
                            mAppExecutors.mainThread().execute(callback::onEmailRegistered);
                        } else {
                            mAppExecutors.mainThread().execute(callback::onEmailNotRegistered);
                        }
                    }
                };

        mAppExecutors.networkIO().execute(runnable);
    }

    @Override
    public Account getAccount() {
        return mCachedAccount;
    }
}
