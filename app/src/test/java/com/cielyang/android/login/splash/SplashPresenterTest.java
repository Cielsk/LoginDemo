package com.cielyang.android.login.splash;

import static org.mockito.Mockito.verify;

import com.cielyang.android.login.data.AccountManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/** */
public class SplashPresenterTest {

    @Mock
    private AccountManager mAccountManager;
    @Mock
    private SplashContract.View mView;

    private SplashPresenter mSplashPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mSplashPresenter = new SplashPresenter(mAccountManager);
        mSplashPresenter.bindView(mView);
    }

    @After
    public void tearDown() throws Exception {
        mSplashPresenter.unbindView();
    }

    @Test
    public void checkSessionToken() throws Exception {
        mSplashPresenter.checkSessionToken();

        verify(mAccountManager).checkSessionToken(mSplashPresenter);
    }

    @Test
    public void loginByToken() throws Exception {
        mSplashPresenter.loginByToken();

        verify(mAccountManager).loginByToken(mSplashPresenter);
    }

    @Test
    public void onTokenSaved() throws Exception {
        mSplashPresenter.onTokenSaved();

        verify(mAccountManager).loginByToken(mSplashPresenter);
    }

    @Test
    public void onTokenNotSaved() throws Exception {
        mSplashPresenter.onTokenNotSaved();

        verify(mView).launchLoginPage();
    }

    @Test
    public void onLoginSucceed() throws Exception {
        mSplashPresenter.onLoginSucceed();

        verify(mView).launchMainPage();
    }

    @Test
    public void onTokenExpired() throws Exception {
        mSplashPresenter.onTokenExpired();

        verify(mView).errorTokenExpired();
        verify(mView).launchLoginPage();
    }

    @Test
    public void onLoginFailed() throws Exception {
        mSplashPresenter.onLoginFailed();

        verify(mView).errorLoginFailed();
        verify(mView).launchLoginPage();
    }
}
