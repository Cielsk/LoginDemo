package com.cielyang.android.login.main;

import com.cielyang.android.login.base.BasePresenter;
import com.cielyang.android.login.base.BaseView;

/**
 *
 */
public interface MainContract {

    interface View extends BaseView<Presenter> {

        void showUserName(CharSequence username);

        void showEmail(CharSequence email);
    }

    interface Presenter extends BasePresenter<View> {
    }
}
