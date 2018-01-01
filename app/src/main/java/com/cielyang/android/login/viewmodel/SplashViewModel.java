package com.cielyang.android.login.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.StringRes;

import com.cielyang.android.login.R;
import com.cielyang.android.login.common.arch.SingleLiveEvent;
import com.cielyang.android.login.common.arch.ToastMessage;
import com.cielyang.android.login.common.utils.ToastUtils;
import com.cielyang.android.login.data.AccountManager;

import javax.inject.Inject;

/** */
public class SplashViewModel extends ViewModel
        implements AccountManager.CheckSessionTokenCallback, AccountManager.LoginByTokenCallback {

    private final AccountManager mAccountManager;

    private final SingleLiveEvent<Void> mLaunchMainPageCommand = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> mLaunchLoginPageCommand = new SingleLiveEvent<>();

    private final ToastMessage mTokenMessage = new ToastMessage();

    @Inject
    public SplashViewModel(AccountManager accountManager) {
        mAccountManager = accountManager;
    }

    private void loginByToken() {
        mAccountManager.loginByToken(this);
    }

    @Override
    public void onTokenSaved() {
        loginByToken();
    }

    @Override
    public void onTokenNotSaved() {
        mLaunchLoginPageCommand.call();
    }

    public SingleLiveEvent<Void> getLaunchLoginPageCommand() {
        return mLaunchLoginPageCommand;
    }

    public SingleLiveEvent<Void> getLaunchMainPageCommand() {
        return mLaunchMainPageCommand;
    }

    public ToastMessage getTokenMessage() {
        return mTokenMessage;
    }

    @Override
    public void onLoginSucceed() {
        mLaunchMainPageCommand.call();
    }

    @Override
    public void onTokenExpired() {
        showToastMessage(R.string.msg_token_expired, ToastUtils.ERROR);
        mLaunchLoginPageCommand.call();
    }

    @Override
    public void onLoginFailed() {
        showToastMessage(R.string.error_login_failed_unknown_cause, ToastUtils.ERROR);
        mLaunchLoginPageCommand.call();
    }

    public void start() {
        mAccountManager.checkSessionToken(this);
    }

    private void showToastMessage(@StringRes int messageId, @ToastUtils.ToastLevel int toastLevel) {
        ToastMessage.ToastInfo info = new ToastMessage.ToastInfo();
        info.resId = messageId;
        info.level = toastLevel;
        mTokenMessage.setValue(info);
    }
}
