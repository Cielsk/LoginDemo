package com.cielyang.android.login.splash;

import static com.cielyang.android.login.common.utils.PreconditionUtils.checkNotNull;

import com.cielyang.android.login.data.AccountManager;
import com.cielyang.android.login.di.ActivityScoped;

import javax.annotation.Nullable;
import javax.inject.Inject;

/** */
@ActivityScoped
public class SplashPresenter
        implements SplashContract.Presenter,
        AccountManager.CheckSessionTokenCallback,
        AccountManager.LoginByTokenCallback {

    private final AccountManager mAccountManager;

    @Nullable
    private SplashContract.View mView;

    @Inject
    public SplashPresenter(AccountManager accountManager) {
        mAccountManager = accountManager;
    }

    @Override
    public void bindView(SplashContract.View view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }

    @Override
    public void checkSessionToken() {
        mAccountManager.checkSessionToken(this);
    }

    @Override
    public void loginByToken() {
        mAccountManager.loginByToken(this);
    }

    @Override
    public void onTokenSaved() {
        checkNotNull(mView);
        loginByToken();
    }

    @Override
    public void onTokenNotSaved() {
        checkNotNull(mView);
        mView.launchLoginPage();
    }

    @Override
    public void onLoginSucceed() {
        checkNotNull(mView);
        mView.launchMainPage();
    }

    @Override
    public void onTokenExpired() {
        checkNotNull(mView);
        mView.errorTokenExpired();
        mView.launchLoginPage();
    }

    @Override
    public void onLoginFailed() {
        checkNotNull(mView);
        mView.errorLoginFailed();
        mView.launchLoginPage();
    }
}
