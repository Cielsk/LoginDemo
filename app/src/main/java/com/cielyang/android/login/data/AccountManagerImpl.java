package com.cielyang.android.login.data;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cielyang.android.login.common.async.AppExecutors;
import com.cielyang.android.login.configs.leancloud.ErrorCode;
import com.cielyang.android.login.configs.leancloud.ErrorResponse;
import com.cielyang.android.login.data.entities.Account;
import com.cielyang.android.login.data.entities.LoginInfo;
import com.cielyang.android.login.data.entities.RegisterInfo;
import com.cielyang.android.login.data.local.SessionDao;
import com.cielyang.android.login.data.remote.AccountService;
import com.cielyang.android.login.data.remote.UserQueryResponse;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/** */
@Singleton
public class AccountManagerImpl implements AccountManager {

    @Inject
    Lazy<Gson> mGsonInjector;

    private AppExecutors mAppExecutors;

    private SessionDao mSessionDao;

    private Account mCachedAccount;

    private AccountService mService;

    @Inject
    public AccountManagerImpl(
            @NonNull AppExecutors appExecutors,
            @NonNull SessionDao sessionDao,
            @NonNull Retrofit retrofit) {
        mAppExecutors = appExecutors;
        mSessionDao = sessionDao;
        mService = retrofit.create(AccountService.class);
    }

    @Override
    public void checkSessionToken(CheckSessionTokenCallback callback) {
        System.out.println("model");
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
                    Call<Account> call =
                            mService.loginByEmail(
                                    new LoginInfo(email.toString(), password.toString()));
                    try {
                        Response<Account> response = call.execute();
                        if (response.isSuccessful()) {
                            mCachedAccount = response.body();
                            saveAccountSession(mCachedAccount);
                            mAppExecutors.mainThread().execute(callback::onLoginSucceed);
                        } else {
                            ErrorResponse errorResponse = getErrorResponse(response);
                            parseLoginByEmailError(callback, errorResponse);
                        }
                    } catch (IOException e) {
                        Logger.e(e, "Http request for login by email failed.");
                        mAppExecutors.mainThread().execute(callback::onLoginFailed);
                    }
                };

        mAppExecutors.networkIO().execute(runnable);
    }

    private void parseLoginByEmailError(
            @NonNull LoginByEmailCallback callback, ErrorResponse errorResponse) {
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

    @Override
    public void loginByToken(@NonNull LoginByTokenCallback callback) {
        Runnable runnable =
                () -> {
                    String token = mSessionDao.getSessionToken();
                    Call<Account> call = mService.loginByToken(token);
                    try {
                        Response<Account> response = call.execute();
                        if (response.isSuccessful()) {
                            mCachedAccount = response.body();
                            saveAccountSession(mCachedAccount);
                            mAppExecutors.mainThread().execute(callback::onLoginSucceed);
                        } else {
                            ErrorResponse errorResponse = getErrorResponse(response);
                            parseLoginByTokenError(callback, errorResponse);
                        }
                    } catch (IOException e) {
                        Logger.e(e, "Http request for login by token failed.");
                        callback.onLoginFailed();
                    }
                };

        mAppExecutors.networkIO().execute(runnable);
    }

    private void parseLoginByTokenError(
            @NonNull LoginByTokenCallback callback, ErrorResponse errorResponse) {
        switch (errorResponse.getCode()) {
            case ErrorCode.USER_NOT_REGISTERED:
                mAppExecutors.mainThread().execute(callback::onTokenExpired);
                break;
            default:
                mAppExecutors.mainThread().execute(callback::onLoginFailed);
                break;
        }
    }

    @Override
    public void register(
            @NonNull CharSequence username,
            @NonNull CharSequence email,
            @NonNull CharSequence password,
            @NonNull RegisterCallback callback) {
        Runnable runnable =
                () -> {
                    Call<Account> call =
                            mService.register(
                                    new RegisterInfo(
                                            username.toString(),
                                            email.toString(),
                                            password.toString()));
                    try {
                        Response<Account> response = call.execute();
                        if (response.isSuccessful()) {
                            mCachedAccount = response.body();
                            saveAccountSession(mCachedAccount);
                            mAppExecutors.mainThread().execute(callback::onRegisterSucceed);
                        } else {
                            ErrorResponse errorResponse = getErrorResponse(response);
                            parseRegisterError(callback, errorResponse);
                        }
                    } catch (IOException e) {
                        Logger.e(e, "Http request for register failed.");
                        callback.onRegisterFailed();
                    }
                };

        mAppExecutors.networkIO().execute(runnable);
    }

    private void parseRegisterError(
            @NonNull RegisterCallback callback, ErrorResponse errorResponse) {
        switch (errorResponse.getCode()) {
            case ErrorCode.USERNAME_EXISTED:
                mAppExecutors.mainThread().execute(callback::onUsernameRegistered);
                break;
            case ErrorCode.EMAIL_EXISTED:
                mAppExecutors.mainThread().execute(callback::onEmailRegistered);
                break;
            default:
                mAppExecutors.mainThread().execute(callback::onRegisterFailed);
                break;
        }
    }

    private ErrorResponse getErrorResponse(Response<Account> response) throws IOException {
        ResponseBody body = response.errorBody();
        String json = body == null ? "" : body.string();
        return mGsonInjector.get().fromJson(json, ErrorResponse.class);
    }

    private void saveAccountSession(Account account) {
        mAppExecutors.diskIO().execute(() -> mSessionDao.saveSession(account));
    }

    @Override
    public void queryUserByName(
            @NonNull CharSequence username, @NonNull QueryUsernameCallback callback) {
        Runnable runnable =
                () -> {
                    String info = String.format("{\"%s\":\"%s\"}", "username", username);
                    Call<UserQueryResponse> call = mService.findUsers(info);
                    try {
                        Response<UserQueryResponse> response = call.execute();
                        if (response.isSuccessful()) {
                            UserQueryResponse queryResponse = response.body();
                            List<Account> list =
                                    queryResponse != null ? queryResponse.getResults() : null;
                            if (list != null && !list.isEmpty()) {
                                mAppExecutors.mainThread().execute(callback::onUsernameRegistered);
                            } else {
                                mAppExecutors
                                        .mainThread()
                                        .execute(callback::onUsernameNotRegistered);
                            }
                        }
                    } catch (IOException e) {
                        Logger.e(e, "Http request for finding user by name failed.");
                    }
                };

        mAppExecutors.networkIO().execute(runnable);
    }

    @Override
    public void queryUserByEmail(
            @NonNull CharSequence email, @NonNull QueryEmailCallback callback) {
        Runnable runnable =
                () -> {
                    String info = String.format("{\"%s\":\"%s\"}", "email", email);
                    Call<UserQueryResponse> call = mService.findUsers(info);
                    try {
                        Response<UserQueryResponse> response = call.execute();
                        if (response.isSuccessful()) {
                            UserQueryResponse queryResponse = response.body();
                            List<Account> list =
                                    queryResponse != null ? queryResponse.getResults() : null;
                            if (list != null && !list.isEmpty()) {
                                mAppExecutors.mainThread().execute(callback::onEmailRegistered);
                            } else {
                                mAppExecutors.mainThread().execute(callback::onEmailNotRegistered);
                            }
                        }
                    } catch (IOException e) {
                        Logger.e(e, "Http request for finding user by name failed.");
                    }
                };

        mAppExecutors.networkIO().execute(runnable);
    }

    @Override
    public Account getAccount() {
        return mCachedAccount;
    }
}
