package com.cielyang.android.login.base;

/** */
public interface BasePresenter<T extends BaseView> {

    void bindView(T view);

    void unbindView();
}
