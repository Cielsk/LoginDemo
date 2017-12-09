package com.cielyang.android.login.ui.activities;

import android.os.Bundle;

import com.cielyang.android.login.R;
import com.cielyang.android.login.base.BaseActivity;
import com.cielyang.android.login.common.async.AppExecutors;
import com.cielyang.android.login.common.utils.ToastUtils;
import com.cielyang.android.login.data.AccountManager;

import javax.inject.Inject;

/**
 * 应用启动页.
 *
 * <p>无 layout 文件，仅仅使用窗口背景显示图片。
 *
 * <p>职责是检查是否有 session 保存
 *
 * <ul>
 * <li>如果有 session 保存，则进行 session 登录
 * <ul>
 * <li>成功则跳转主页
 * <li>失败则跳转登录页面
 * </ul>
 * <li>如果无 session 保存，则跳转到登录页面
 * </ul>
 */
public class SplashActivity extends BaseActivity {

    @Inject AccountManager mAccountManager;
    AppExecutors mAppExecutors;
    private CheckCallback mCheckCallback;
    private LoginCallBack mLoginCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        checkSessionToken();
    }

    private void init() {
        mCheckCallback = new CheckCallback();
        mLoginCallBack = new LoginCallBack();
    }

    private void checkSessionToken() {
        mAccountManager.checkSessionToken(mCheckCallback);
    }

    private void loginByToken() {
        mAccountManager.loginByToken(mLoginCallBack);
    }

    private void launchMainPage() {
        MainActivity.actionStart(this);
        finish();
    }

    private void launchLoginPage() {
        LoginActivity.actionStart(this);
        finish();
    }

    private class CheckCallback implements AccountManager.CheckSessionTokenCallback {
        @Override
        public void onTokenSaved() {
            loginByToken();
        }

        @Override
        public void onTokenNotSaved() {
            launchLoginPage();
        }
    }

    private class LoginCallBack implements AccountManager.LoginByTokenCallback {

        @Override
        public void onLoginSucceed() {
            launchMainPage();
        }

        @Override
        public void onTokenExpired() {
            ToastUtils.error(SplashActivity.this, getString(R.string.msg_token_expired));
            launchLoginPage();
        }

        @Override
        public void onLoginFailed() {
            ToastUtils.error(
                    SplashActivity.this, getString(R.string.error_login_failed_unknown_cause));
            launchLoginPage();
        }
    }
}
