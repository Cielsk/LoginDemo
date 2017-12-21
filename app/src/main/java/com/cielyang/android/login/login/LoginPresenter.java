package com.cielyang.android.login.login;

import static com.cielyang.android.login.common.utils.PreconditionUtils.checkNotNull;

import android.support.annotation.NonNull;

import com.cielyang.android.login.common.utils.TextUtils;
import com.cielyang.android.login.common.utils.ValidateUtils;
import com.cielyang.android.login.data.AccountManager;
import com.cielyang.android.login.di.ActivityScoped;

import javax.inject.Inject;

/**
 *
 */
@ActivityScoped
public class LoginPresenter implements LoginContract.Presenter,
        AccountManager.LoginByEmailCallback {

    private final AccountManager mAccountManager;

    private LoginContract.View mView;

    private boolean mIsValidEmail;

    private boolean mIsValidPassword;

    @Inject
    public LoginPresenter(AccountManager accountManager) {
        mAccountManager = accountManager;
    }

    @Override
    public void bindView(LoginContract.View view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }

    @Override
    public void checkEmail(CharSequence email) {
        checkNotNull(mView);
        mView.clearEmailError();
        mIsValidEmail = false;
        if (TextUtils.isEmpty(email)) {
            mView.errorEmptyEmail();
        } else if (!ValidateUtils.isValidEmail(email)) {
            mView.errorInvalidEmail();
        } else {
            mIsValidEmail = true;
        }
    }

    @Override
    public void checkPassword(CharSequence password) {
        checkNotNull(mView);
        mView.clearPasswordError();
        mIsValidPassword = false;
        boolean temp = true;
        if (TextUtils.isEmpty(password)) {
            mView.errorEmptyPassword();
            temp = false;
        } else {
            mIsValidPassword = true;
        }
        mView.setPasswordToggleEnabled(temp);
    }

    @Override
    public void loginByEmail(@NonNull CharSequence email, @NonNull CharSequence password) {
        checkEmail(email);
        checkPassword(password);
        if (mIsValidEmail && mIsValidPassword) {
            setLoggingInState(true);
            mAccountManager.loginByEmail(email, password, this);
        }
    }

    private void setLoggingInState(boolean isLoggingIn) {
        checkNotNull(mView);
        mView.showLoadingIndicator(isLoggingIn);
        mView.setLoginBtnEnabled(!isLoggingIn);
    }

    @Override
    public void onLoginSucceed() {
        setLoggingInState(false);
        mView.showMsgLoginSucceed();
        mView.launchMainPage();
    }

    @Override
    public void onEmailNotExisted() {
        setLoggingInState(false);
        mView.errorEmailNotExisted();
    }

    @Override
    public void onPasswordIncorrect() {
        setLoggingInState(false);
        mView.errorIncorrectPassword();
    }

    @Override
    public void onLoginFailed() {
        setLoggingInState(false);
        mView.errorLoginFailed();
    }
}
