package com.cielyang.android.login.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.cielyang.android.login.R;
import com.cielyang.android.login.common.arch.SingleLiveEvent;
import com.cielyang.android.login.common.arch.ToastMessage;
import com.cielyang.android.login.common.utils.TextUtils;
import com.cielyang.android.login.common.utils.ToastUtils;
import com.cielyang.android.login.common.utils.ValidateUtils;
import com.cielyang.android.login.data.AccountManager;

import javax.inject.Inject;

/** */
public class LoginViewModel extends ViewModel implements AccountManager.LoginByEmailCallback {

    private final AccountManager mAccountManager;
    private final MutableLiveData<Boolean> mIsLogging = new MutableLiveData<>();
    private final ToastMessage mTokenMessage = new ToastMessage();
    private final SingleLiveEvent<Void> mLaunchMainPageCommand = new SingleLiveEvent<>();
    private final SingleLiveEvent<Integer> mEmailErrorResId = new SingleLiveEvent<>();
    private final SingleLiveEvent<Integer> mPasswordErrorResId = new SingleLiveEvent<>();
    private boolean mIsValidEmail;
    private boolean mIsValidPassword;

    @Inject
    public LoginViewModel(AccountManager accountManager) {
        mAccountManager = accountManager;
        mIsLogging.setValue(false);
    }

    public ToastMessage getTokenMessage() {
        return mTokenMessage;
    }

    public SingleLiveEvent<Void> getLaunchMainPageCommand() {
        return mLaunchMainPageCommand;
    }

    public MutableLiveData<Boolean> getLoginState() {
        return mIsLogging;
    }

    public SingleLiveEvent<Integer> getEmailErrorResId() {
        return mEmailErrorResId;
    }

    public SingleLiveEvent<Integer> getPasswordErrorResId() {
        return mPasswordErrorResId;
    }

    private void checkEmail(CharSequence email) {
        mEmailErrorResId.setValue(null);
        mIsValidEmail = false;
        if (TextUtils.isEmpty(email)) {
            mEmailErrorResId.setValue(R.string.error_field_required);
        } else if (!ValidateUtils.isValidEmail(email)) {
            mEmailErrorResId.setValue(R.string.error_invalid_email);
        } else {
            mIsValidEmail = true;
        }
    }

    private void checkPassword(CharSequence password) {
        mPasswordErrorResId.setValue(null);
        mIsValidPassword = false;
        boolean temp = true;
        if (TextUtils.isEmpty(password)) {
            mPasswordErrorResId.setValue(R.string.error_field_required);
            temp = false;
        } else {
            mIsValidPassword = true;
        }
        mIsLogging.setValue(temp);
    }

    public void loginByEmail(@NonNull CharSequence email, @NonNull CharSequence password) {
        checkEmail(email);
        checkPassword(password);
        if (mIsValidEmail && mIsValidPassword) {
            mIsLogging.setValue(true);
            mAccountManager.loginByEmail(email, password, this);
        }
    }

    @Override
    public void onLoginSucceed() {
        mIsLogging.setValue(false);
        showToastMessage(R.string.msg_success_login, ToastUtils.SUCCESS);
        mLaunchMainPageCommand.call();
    }

    @Override
    public void onEmailNotExisted() {
        mIsLogging.setValue(false);
        mEmailErrorResId.setValue(R.string.error_email_not_registered);
    }

    @Override
    public void onPasswordIncorrect() {
        mIsLogging.setValue(false);
        mPasswordErrorResId.setValue(R.string.error_incorrect_password);
    }

    @Override
    public void onLoginFailed() {
        mIsLogging.setValue(false);
        showToastMessage(R.string.error_login_failed_unknown_cause, ToastUtils.ERROR);
    }

    private void showToastMessage(@StringRes int messageId, @ToastUtils.ToastLevel int toastLevel) {
        ToastMessage.ToastInfo info = new ToastMessage.ToastInfo();
        info.resId = messageId;
        info.level = toastLevel;
        mTokenMessage.setValue(info);
    }
}
