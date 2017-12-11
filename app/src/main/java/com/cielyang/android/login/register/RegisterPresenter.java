package com.cielyang.android.login.register;

import static com.cielyang.android.login.common.utils.PreconditionUtils.checkNotNull;

import android.text.TextUtils;

import com.cielyang.android.login.common.utils.ValidateUtils;
import com.cielyang.android.login.data.AccountManager;
import com.cielyang.android.login.di.ActivityScoped;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

/** */
@ActivityScoped
public class RegisterPresenter
        implements RegisterContract.Presenter,
        AccountManager.RegisterCallback,
        AccountManager.QueryEmailCallback,
        AccountManager.QueryUsernameCallback {

    private final AccountManager mAccountManager;

    private RegisterContract.View mView;

    private boolean mIsValidUsername;

    private boolean mIsValidEmail;

    private boolean mIsValidPassword;

    @Inject
    public RegisterPresenter(AccountManager accountManager) {
        mAccountManager = accountManager;
    }

    public void bindView(RegisterContract.View view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }

    @Override
    public void checkUsernameRegisteredOrNot(CharSequence username) {
        checkUsername(username);
        if (mIsValidUsername) {
            mAccountManager.queryUserByName(username, this);
        }
    }

    @Override
    public void checkEmailRegisteredOrNot(CharSequence email) {
        checkEmail(email);
        if (mIsValidEmail) {
            mAccountManager.queryUserByEmail(email, this);
        }
    }

    @Override
    public void checkUsername(CharSequence username) {
        checkNotNull(mView);
        mView.clearUsernameError();
        mIsValidUsername = false;
        if (TextUtils.isEmpty(username)) {
            mView.errorEmptyUsername();
        } else if (!ValidateUtils.isValidUsername(username)) {
            mView.errorInvalidUsername();
        } else {
            mIsValidUsername = true;
        }
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
        } else if (ValidateUtils.isShortPassword(password)) {
            mView.errorShortPassword();
        } else if (!ValidateUtils.isValidPassword(password)) {
            mView.errorInvalidPassword();
        } else {
            mIsValidPassword = true;
        }
        mView.setPasswordToggleEnabled(temp);
    }

    @Override
    public void register(CharSequence username, CharSequence email, CharSequence password) {
        checkUsername(username);
        checkEmail(email);
        checkPassword(password);
        if (mIsValidUsername && mIsValidEmail && mIsValidPassword) {
            setRegisteringState(true);
            mAccountManager.register(username, email, password, this);
        }
    }

    private void setRegisteringState(boolean registering) {
        checkNotNull(mView);
        mView.showLoadingIndicator(registering);
        mView.setRegisterBtnEnabled(!registering);
    }

    @Override
    public void onRegisterSucceed() {
        setRegisteringState(false);
        mView.showMsgRegisterSucceed();
        mView.launchMainPage();
    }

    @Override
    public void onUsernameRegistered() {
        setRegisteringState(false);
        Logger.d("Username registered.");
        mView.errorUsernameRegistered();
    }

    @Override
    public void onEmailRegistered() {
        setRegisteringState(false);
        Logger.d("Email registered.");
        mView.errorEmailRegistered();
    }

    @Override
    public void onRegisterFailed() {
        setRegisteringState(false);
        mView.errorRegisterFailed();
    }

    @Override
    public void onUsernameNotRegistered() {
        checkNotNull(mView);
        Logger.d("Username not registered.");
        mView.clearUsernameError();
    }

    @Override
    public void onEmailNotRegistered() {
        checkNotNull(mView);
        Logger.d("Email not registered.");
        mView.clearEmailError();
    }
}
