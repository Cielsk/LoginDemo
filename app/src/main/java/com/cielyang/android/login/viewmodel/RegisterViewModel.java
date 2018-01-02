package com.cielyang.android.login.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
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
public class RegisterViewModel extends ViewModel
        implements AccountManager.RegisterCallback,
        AccountManager.QueryEmailCallback,
        AccountManager.QueryUsernameCallback {

    private final AccountManager mAccountManager;
    private final MutableLiveData<Boolean> mIsRegistering = new MutableLiveData<>();
    private final ToastMessage mTokenMessage = new ToastMessage();
    private final SingleLiveEvent<Void> mLaunchMainPageCommand = new SingleLiveEvent<>();
    private final SingleLiveEvent<Integer> mUsernameErrorResId = new SingleLiveEvent<>();
    private final SingleLiveEvent<Integer> mEmailErrorResId = new SingleLiveEvent<>();
    private final SingleLiveEvent<Integer> mPasswordErrorResId = new SingleLiveEvent<>();
    private boolean mIsValidUsername;
    private boolean mIsValidEmail;
    private boolean mIsValidPassword;

    @Inject
    public RegisterViewModel(AccountManager accountManager) {
        mAccountManager = accountManager;
    }

    public AccountManager getAccountManager() {
        return mAccountManager;
    }

    public MutableLiveData<Boolean> getRegisterState() {
        return mIsRegistering;
    }

    public ToastMessage getTokenMessage() {
        return mTokenMessage;
    }

    public SingleLiveEvent<Void> getLaunchMainPageCommand() {
        return mLaunchMainPageCommand;
    }

    public SingleLiveEvent<Integer> getUsernameErrorResId() {
        return mUsernameErrorResId;
    }

    public SingleLiveEvent<Integer> getEmailErrorResId() {
        return mEmailErrorResId;
    }

    public SingleLiveEvent<Integer> getPasswordErrorResId() {
        return mPasswordErrorResId;
    }

    public void checkUsernameRegisteredOrNot(CharSequence username) {
        checkUsername(username);
        if (mIsValidUsername) {
            mAccountManager.queryUserByName(username, this);
        }
    }

    public void checkEmailRegisteredOrNot(CharSequence email) {
        checkEmail(email);
        if (mIsValidEmail) {
            mAccountManager.queryUserByEmail(email, this);
        }
    }

    private void checkUsername(CharSequence username) {
        mUsernameErrorResId.setValue(null);
        mIsValidUsername = false;
        if (TextUtils.isEmpty(username)) {
            mUsernameErrorResId.setValue(R.string.error_field_required);
        } else if (!ValidateUtils.isValidUsername(username)) {
            mUsernameErrorResId.setValue(R.string.error_invalid_username);
        } else {
            mIsValidUsername = true;
        }
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
        } else if (ValidateUtils.isShortPassword(password)) {
            mPasswordErrorResId.setValue(R.string.error_short_password);
        } else if (!ValidateUtils.isValidPassword(password)) {
            mPasswordErrorResId.setValue(R.string.error_invalid_password);
        } else {
            mIsValidPassword = true;
        }
        setRegisteringState(temp);
    }

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
        mIsRegistering.setValue(registering);
    }

    @Override
    public void onRegisterSucceed() {
        setRegisteringState(false);
        showToastMessage(R.string.msg_success_register, ToastUtils.SUCCESS);
        mLaunchMainPageCommand.call();
    }

    @Override
    public void onUsernameRegistered() {
        setRegisteringState(false);
        mUsernameErrorResId.setValue(R.string.error_username_existed);
    }

    @Override
    public void onEmailRegistered() {
        setRegisteringState(false);
        mEmailErrorResId.setValue(R.string.error_email_existed);
    }

    @Override
    public void onRegisterFailed() {
        setRegisteringState(false);
        showToastMessage(R.string.error_register_failed_unknown_cause, ToastUtils.ERROR);
    }

    @Override
    public void onUsernameNotRegistered() {
        mUsernameErrorResId.setValue(null);
    }

    @Override
    public void onEmailNotRegistered() {
        mEmailErrorResId.setValue(null);
    }

    private void showToastMessage(@StringRes int messageId, @ToastUtils.ToastLevel int toastLevel) {
        ToastMessage.ToastInfo info = new ToastMessage.ToastInfo();
        info.resId = messageId;
        info.level = toastLevel;
        mTokenMessage.setValue(info);
    }
}
