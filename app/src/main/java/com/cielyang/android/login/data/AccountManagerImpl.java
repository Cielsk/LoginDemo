package com.cielyang.android.login.data;

import static com.cielyang.android.login.configs.leancloud.RequestBody.LOGIN_EMAIL_PARAM_NAME;
import static com.cielyang.android.login.configs.leancloud.RequestBody.PASSWORD_PARAM_NAME;
import static com.cielyang.android.login.configs.leancloud.RequestBody.REGISTER_EMAIL_PARAM_NAME;
import static com.cielyang.android.login.configs.leancloud.RequestBody.USERNAME_PARAM_NAME;

import android.support.annotation.NonNull;

import com.cielyang.android.login.common.http.HttpClient;
import com.cielyang.android.login.common.http.Request;
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

/** */
public class AccountManagerImpl implements AccountManager {

    private HttpClient mHttpClient;

    private AppExecutors mAppExecutors;

    private SessionDao mSessionDao;

    private Account mCachedAccount;

    public AccountManagerImpl(
            @NonNull OkHttpClientImpl httpClient, @NonNull SessionDao sessionDao,
            @NonNull AppExecutors appExecutors) {
        httpClient.addNetworkInterceptor(new CommonInterceptor());
        mHttpClient = httpClient;
        mSessionDao = sessionDao;
        mAppExecutors = appExecutors;
    }

    @Override
    public void loginByEmail(
            @NonNull CharSequence email,
            @NonNull CharSequence password,
            @NonNull LoginByEmailCallback callback) {
        Runnable runnable =
                () -> {
                    BaseRequest request = new BaseRequest(Api.getLoginUrl());
                    request.setMethod(Request.POST);
                    request.setBodyField(LOGIN_EMAIL_PARAM_NAME, email.toString());
                    request.setBodyField(PASSWORD_PARAM_NAME, password.toString());

                    BaseResponse response = (BaseResponse) mHttpClient.post(request, false);

                    if (response.getCode() == Response.STATE_OK) {
                        Account account = new Gson().fromJson(response.getData(), Account.class);
                        mSessionDao.saveSession(account);
                        mCachedAccount = account;
                        mAppExecutors.mainThread().execute(callback::onLoginSucceed);
                    } else {
                        ErrorResponse errorResponse =
                                new Gson().fromJson(response.getData(), ErrorResponse.class);
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
    public void loginByToken(@NonNull CharSequence token, @NonNull LoginByTokenCallback callback) {
        Runnable runnable =
                () -> {
                    BaseRequest request = new BaseRequest(Api.getLoginByTokenUrl());
                    request.setHeaderField(
                            RequestHeader.SESSION_TOKEN_PARAMETER_NAME, token.toString());
                    request.setMethod(Request.GET);

                    BaseResponse response = (BaseResponse) mHttpClient.post(request, false);

                    if (response.getCode() == Response.STATE_OK) {
                        Account account = new Gson().fromJson(response.getData(), Account.class);
                        mSessionDao.saveSession(account);
                        mCachedAccount = account;
                        mAppExecutors.mainThread().execute(callback::onLoginSucceed);
                    } else {
                        ErrorResponse errorResponse =
                                new Gson().fromJson(response.getData(), ErrorResponse.class);
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
                    request.setMethod(Request.POST);
                    request.setBodyField(USERNAME_PARAM_NAME, username.toString());
                    request.setBodyField(REGISTER_EMAIL_PARAM_NAME, email.toString());
                    request.setBodyField(PASSWORD_PARAM_NAME, password.toString());

                    BaseResponse response = (BaseResponse) mHttpClient.post(request, false);

                    if (response.getCode() == Response.STATE_OK) {
                        Account account = new Gson().fromJson(response.getData(), Account.class);
                        mSessionDao.saveSession(account);
                        mCachedAccount = account;
                        mAppExecutors.mainThread().execute(callback::onRegisterSucceed);
                    } else {
                        ErrorResponse errorResponse =
                                new Gson().fromJson(response.getData(), ErrorResponse.class);
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

    @Override
    public void queryUserByName(@NonNull CharSequence username, @NonNull QueryCallback callback) {
        queryUser("username", username, callback);
    }

    @Override
    public void queryUserByEmail(@NonNull CharSequence email, @NonNull QueryCallback callback) {
        queryUser("email", email, callback);
    }

    @Override
    public Account getAccount() {
        return mCachedAccount;
    }

    private void queryUser(
            @NonNull CharSequence key,
            @NonNull CharSequence value,
            @NonNull QueryCallback callback) {
        Runnable runnable =
                () -> {
                    BaseRequest request = new BaseRequest(Api.getQueryUserUrl());
                    request.setMethod(Request.GET);
                    String condition = String.format("{\"%s\":\"%s\"}", key, value);
                    request.setBodyField(Api.QUERY_PARAM_NAME, condition);

                    BaseResponse response = (BaseResponse) mHttpClient.post(request, false);

                    if (response.getCode() == Response.STATE_OK) {
                        UserQueryResponse queryResponse =
                                new Gson().fromJson(response.getData(), UserQueryResponse.class);
                        if (queryResponse.getResults().size() > 0) {
                            mAppExecutors.mainThread().execute(callback::onUserExisted);
                        } else {
                            mAppExecutors.mainThread().execute(callback::onUserNotExisted);
                        }
                    } else {
                        mAppExecutors.mainThread().execute(callback::onQueryFailed);
                    }
                };

        mAppExecutors.networkIO().execute(runnable);
    }
}
