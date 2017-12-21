package com.cielyang.android.login.splash;

import android.os.Bundle;

import com.cielyang.android.login.R;
import com.cielyang.android.login.base.BaseActivity;
import com.cielyang.android.login.common.utils.ToastUtils;
import com.cielyang.android.login.login.LoginActivity;
import com.cielyang.android.login.main.MainActivity;

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
public class SplashActivity extends BaseActivity implements SplashContract.View {

    @Inject
    SplashContract.Presenter mPresenter;

    @BindString(R.string.msg_token_expired)
    String mMsgTokenExpired;

    @BindString(R.string.error_login_failed_unknown_cause)
    String mErrorLoginFailed;

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.bindView(this);
        mPresenter.checkSessionToken();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unbindView();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
    }

    @Override
    public void launchMainPage() {
        MainActivity.actionStart(this);
        finish();
    }

    @Override
    public void launchLoginPage() {
        LoginActivity.actionStart(this);
        finish();
    }

    @Override
    public void errorTokenExpired() {
        ToastUtils.error(SplashActivity.this, mMsgTokenExpired);
    }

    @Override
    public void errorLoginFailed() {
        ToastUtils.error(SplashActivity.this, mErrorLoginFailed);
    }
}
