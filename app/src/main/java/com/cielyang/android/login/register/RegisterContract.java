package com.cielyang.android.login.register;

import com.cielyang.android.login.base.BasePresenter;
import com.cielyang.android.login.base.BaseView;

/** */
public interface RegisterContract {

    interface View extends BaseView<Presenter> {

        void launchMainPage();

        void setRegisterBtnEnabled(boolean enabled);

        void setPasswordToggleEnabled(boolean enabled);

        void showLoadingIndicator(boolean enabled);

        void showMsgRegisterSucceed();

        void errorInvalidUsername();

        void errorEmptyUsername();

        void errorUsernameRegistered();

        void errorInvalidEmail();

        void errorEmptyEmail();

        void errorEmailRegistered();

        void errorInvalidPassword();

        void errorEmptyPassword();

        void errorShortPassword();

        void errorRegisterFailed();

        void clearUsernameError();

        void clearEmailError();

        void clearPasswordError();
    }

    interface Presenter extends BasePresenter<View> {

        void checkUsernameRegisteredOrNot(CharSequence username);

        void checkEmailRegisteredOrNot(CharSequence email);

        void checkUsername(CharSequence username);

        void checkEmail(CharSequence email);

        void checkPassword(CharSequence password);

        void register(CharSequence username, CharSequence email, CharSequence password);
    }
}
