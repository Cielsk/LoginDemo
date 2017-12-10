package com.cielyang.android.login.splash;

import com.cielyang.android.login.base.BasePresenter;
import com.cielyang.android.login.base.BaseView;

/** */
public interface SplashContract {

    interface View extends BaseView<Presenter> {

        void launchMainPage();

        void launchLoginPage();

        void errorTokenExpired();

        void errorLoginFailed();
    }

    interface Presenter extends BasePresenter<View> {

        void checkSessionToken();

        void loginByToken();
    }
}
