package com.cielyang.android.login.login;

import android.support.annotation.NonNull;

import com.cielyang.android.login.base.BasePresenter;
import com.cielyang.android.login.base.BaseView;

/**
 *
 */
public interface LoginContract {

    interface View extends BaseView<Presenter> {

        void launchMainPage();

        void setLoginBtnEnabled(boolean enabled);

        void setPasswordToggleEnabled(boolean enabled);

        void showLoadingIndicator(boolean enabled);

        void showMsgLoginSucceed();

        void errorInvalidEmail();

        void errorEmptyEmail();

        void errorEmptyPassword();

        void errorIncorrectPassword();

        void errorEmailNotExisted();

        void errorLoginFailed();

        void clearEmailError();

        void clearPasswordError();
    }

    interface Presenter extends BasePresenter<View> {

        void checkEmail(CharSequence email);

        void checkPassword(CharSequence password);

        void loginByEmail(@NonNull CharSequence email, @NonNull CharSequence password);
    }
}

