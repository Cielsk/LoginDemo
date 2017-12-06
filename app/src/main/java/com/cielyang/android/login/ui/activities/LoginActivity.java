package com.cielyang.android.login.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.cielyang.android.login.R;
import com.cielyang.android.login.base.BaseActivity;
import com.cielyang.android.login.ui.fragments.LoginFragment;
import com.cielyang.android.login.ui.fragments.RegisterFragment;

/**
 * 登录和注册页面.
 *
 * <p>登录和注册页面功能相近，使用同一个 activity，用不同的 fragment 来呈现内容。
 */
public class LoginActivity extends BaseActivity
        implements LoginFragment.OnClickedListener, RegisterFragment.OnClickedListener {

    private FragmentManager mFragmentManager;

    private Fragment mFragment;

    public static void actionStart(@NonNull Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        mFragmentManager = getSupportFragmentManager();
        if (mFragmentManager.findFragmentById(R.id.fragment_container) == null) {
            mFragment = LoginFragment.newInstance();
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.add(R.id.fragment_container, mFragment);
            transaction.commit();
        }
    }

    @Override
    public void onLoginLinkClicked() {
        replaceFragmentWith(LoginFragment.newInstance());
    }

    @Override
    public void onRegisterLinkClicked() {
        replaceFragmentWith(RegisterFragment.newInstance());
    }

    private void replaceFragmentWith(Fragment fragment) {
        mFragment = fragment;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
