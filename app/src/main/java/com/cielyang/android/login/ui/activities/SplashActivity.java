package com.cielyang.android.login.ui.activities;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;

import com.cielyang.android.login.R;
import com.cielyang.android.login.base.BaseActivity;
import com.cielyang.android.login.common.utils.ToastUtils;
import com.cielyang.android.login.login.LoginActivity;
import com.cielyang.android.login.viewmodel.SplashViewModel;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * 应用启动页.
 *
 * <p>无 layout 文件，仅仅使用窗口背景显示图片。
 *
 * <p>职责是检查是否有 session 保存
 *
 * <ul>
 *   <li>如果有 session 保存，则进行 session 登录
 *       <ul>
 *         <li>成功则跳转主页
 *         <li>失败则跳转登录页面
 *       </ul>
 *   <li>如果无 session 保存，则跳转到登录页面
 * </ul>
 */
public class SplashActivity extends BaseActivity {

    @BindString(R.string.msg_token_expired)
    String mMsgTokenExpired;

    @BindString(R.string.error_login_failed_unknown_cause)
    String mErrorLoginFailed;

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    private SplashViewModel mSplashViewModel;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        initViewModel();
    }

    private void initViewModel() {
        mSplashViewModel =
                ViewModelProviders.of(this, mViewModelFactory).get(SplashViewModel.class);

        mSplashViewModel.getLaunchMainPageCommand().observe(this, __ -> launchMainPage());
        mSplashViewModel.getLaunchLoginPageCommand().observe(this, __ -> launchLoginPage());

        mSplashViewModel
                .getTokenMessage()
                .observe(this, (resId, toastLevel) -> ToastUtils.msg(this, resId, toastLevel));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSplashViewModel.start();
    }

    public void launchMainPage() {
        MainActivity.actionStart(this);
        finish();
    }

    public void launchLoginPage() {
        LoginActivity.actionStart(this);
        finish();
    }
}
